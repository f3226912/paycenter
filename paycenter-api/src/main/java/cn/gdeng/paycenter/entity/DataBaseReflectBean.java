package cn.gdeng.paycenter.entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;



/**
* <p>Title:AutoBean </p>
* <p>Description: 
*  
*  选填项：workspace(工作空间)，project(工程名) ，配置了工作空间和工程名后，生成的实体类会直接放至指定目录下。
*  
*  必填项：请输入数据库连接（url） ,账号(user)，密码(password)，表名（table），才能对应生成实体。
* 
* </p>
* 
* @author bdhuang
* 
* @date 2016年7月14日
* 
*/
/**
 * 数据库实体bean的生成
 * 必填项：请输入数据库连接（url） ,账号(user)，密码(password)，才能对应生成实体。
 * @author wjguo
 * datetime 2016年9月28日 上午11:54:05  
 *
 */
public class DataBaseReflectBean {
	
	//生产的java文件保存的路径，如果没有配置，则仅仅在控制台输出
    private static String filiePath="";
    
    /*mysql url的连接字符串*/
    private static String url = "jdbc:mysql://10.17.1.180:3306/gd_pay?useUnicode=true&characterEncoding=utf-8&useOldAliasMetadataBehavior=true";
    //账号
    private static String user = "gd_dev";
    //密码
    private static String password = "gddev_1607";
    //数据库连接对象
    private Connection connection;
    //mysql jdbc的java包驱动字符串
    private String driverClassName = "com.mysql.jdbc.Driver";
    //主键列名
    private String key;
    //数据库中的表名
    private String table;
    //包路径
    private String packagePath;
    
    //数据库的列名称
    private String[] colnames;
    //列名类型数组  
    private String[] colTypes;
    //列名备注数组  
    private String[] contents;
    
    /**
     * @param table 表名
     * @param packagePath 包名
     */
    public DataBaseReflectBean(String table, String packagePath){
    	this.table = table;
    	this.packagePath = packagePath;
    	init();
    }
    
    /**初始化
     * 
     */
    private void init() {
    	try {//驱动注册
    		Class.forName(driverClassName);
    		if (connection == null || connection.isClosed())
    			//获得链接
    			connection = DriverManager.getConnection(url, user, password);
    		
    		//注册hook服务，关闭数据库连接。
    		Runtime.getRuntime().addShutdownHook(new Thread(){
    			@Override
    			public void run() {
    				try {
    					if (connection != null && !connection.isClosed()) {
    						connection.close();
    					}
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
    			}
    			
    		});
    	} catch (ClassNotFoundException ex) {
    		ex.printStackTrace();
    		System.out.println("Oh,not");
    	} catch (SQLException e) {
    		e.printStackTrace();
    		System.out.println("Oh,not");
    	}
    
    }
    
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void doAction(){
        String sql = "select * from "+table;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            //获取数据库的元数据 
            ResultSetMetaData metadata = statement.getMetaData();
            
            DatabaseMetaData dbMetaData = connection.getMetaData();
            
            ResultSet pkRSet = dbMetaData.getPrimaryKeys(null, null, table);
            
            while( pkRSet.next() ) {
                if("PRIMARY".equals(pkRSet.getObject(6).toString())){
                    key=pkRSet.getObject(4).toString();
                }
//              System.out.println("COLUMN_NAME: "+pkRSet.getObject(4));
//              System.out.println("PK_NAME : "+pkRSet.getObject(6));
            } 
            
//          以下获取表备注，还有问题，后续再优化修改
//          String [ ] t = { "TABLE", "VIEW" };
//          ResultSet rst = dbMetaData.getTables(null, null , table, t);
//          while(rst.next()){
//            System.out.print("目录名："+rst.getString(1)); 
//            System.out.print(" 模式名："+rst.getString(2));
//            System.out.print(" 表名："+rst.getString(3));
//            System.out.print(" 表的类型："+rst.getString(4));
//            System.out.println(" 注释："+rst.getString(5));
//            }
//          
            
            
            //数据库的字段个数
            int len = metadata.getColumnCount();
            //字段名称
            colnames = new String[len+1];
            //字段类型 --->已经转化为java中的类名称了
            colTypes = new String[len+1];
            
            contents = new String[len+1];
            for(int i= 1;i<=len;i++){
                colnames[i] = metadata.getColumnName(i); //获取字段名称
                colTypes[i] = sqlType2JavaType(metadata.getColumnTypeName(i)); //获取字段类型 
                
                ResultSet rs = dbMetaData.getColumns(null,null,table,metadata.getColumnName(i));
                if (rs.next()) {
                    Object object = rs.getObject(12);
                    contents[i]=object==null?"":object.toString();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**mysql的字段类型转化为java的类型
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {  
       
       if(sqlType.equalsIgnoreCase("bit")){  
           return "Boolean";  
       }else if(sqlType.equalsIgnoreCase("tinyint")|| sqlType.equalsIgnoreCase("TINYINT UNSIGNED")){  
           return "Byte";  
       }else if(sqlType.equalsIgnoreCase("smallint")){  
           return "Short";  
       }else if(sqlType.equalsIgnoreCase("int")||sqlType.equalsIgnoreCase("INT UNSIGNED")){  
           return "Integer";  
       }else if(sqlType.equalsIgnoreCase("bigint")){  
           return "Long";  
       }else if(sqlType.equalsIgnoreCase("float")){  
           return "Float";  
       }else if(sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")   
               || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")   
               || sqlType.equalsIgnoreCase("smallmoney") ||sqlType.equalsIgnoreCase("DECIMAL UNSIGNED") ){  
           return "Double";  
       }else if(sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")   
               || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")   
               || sqlType.equalsIgnoreCase("text")){  
           return "String";  
       }else if(sqlType.equalsIgnoreCase("datetime") ||sqlType.equalsIgnoreCase("date") ||sqlType.equalsIgnoreCase("timestamp")){  
           return "Date";  
       }else if(sqlType.equalsIgnoreCase("image")){  
           return "Blod";  
       }  
         
       return null;  
   }

    /**获取整个类的字符串并且输出为java文件
     * @return
     * @throws IOException 
     */
    public  StringBuffer getClassStr() throws IOException{
        //输出的类字符串
        StringBuffer str = new StringBuffer("");
        //获取表类型和表名的字段名
        this.doAction();
        //校验
        if(null == colnames && null == colTypes) return null;
        
        //拼接
        if(packagePath!=null && !packagePath.trim().equals("")){
            str.append("package " + packagePath + ";\n");
        }
        
        str.append("\r\nimport java.util.Date;\n");
        str.append("import javax.persistence.Column;\n");
        str.append("import javax.persistence.Entity;\n");
        str.append("import javax.persistence.Id;\r\n");
        str.append("\r\n");
        
        str.append("@Entity(name = \""+table+"\")\n");
        String names[]=table.split("_");
        String className = "";
        for(String s:names){
            className += convertHumpStyle(s);
        }
        className +="Entity";
        str.append("public class "+className+"  implements java.io.Serializable{\r\n");
        //拼接属性
        for(int index=1; index < colnames.length ; index++){
            str.append(getAttrbuteString(colnames[index],colTypes[index],contents[index]));
        }
        //拼接get，Set方法       
        for(int index=1; index < colnames.length ; index++){
            str.append(getGetMethodString(colnames[index],colTypes[index]));
            str.append(getSetMethodString(colnames[index],colTypes[index]));
        }
        str.append("}\r\n");
        
        if(filiePath!=null && !filiePath.trim().equals("")){
            //输出到文件中
            String fileName=filiePath+className+".java";
            System.out.println("生成的文件路径:"+fileName);
            
            wirtiFile(fileName, str.toString());
        }
        
        
        return str;
    }
    
    /**写入文件
     * @param fileName
     * @param content
     * @throws IOException 
     */
    public void wirtiFile(String fileName,String content) throws IOException{
        File file = new File(fileName);
        
        //创建文件目录
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
        	parentFile.mkdirs();
        }
        
        //创建java文件
        file.createNewFile();
        
        
        BufferedWriter write = null;
        try {
            write = new BufferedWriter(new FileWriter(file));
            write.write(content);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写文件异常，请直接拷贝下面字符串到对应java文件中");
            if (write != null)
                try {
                    write.close();
                } catch (IOException e1) {          
                    e1.printStackTrace();
                }
        }
    }
    
    
    /**获取属性字段字符串
     * @param name
     * @param type
     * @param content
     * @return
     */
    public StringBuffer getAttrbuteString(String name, String type,String content) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        };
        
        //标准化属性字段名称。
        name = normAttrStr(name);
        
        String format = String.format("    /**\n     *%s\n     */\n    private %s %s;\n\r", content,type,name);
        return new StringBuffer(format);
    }
    
    /**以驼峰命名法标准化属性名称。ps：首字母小写，去掉_下划线。
     * @param name
     * @return
     */
    private String normAttrStr(String name) {
    	if (name.indexOf("_") == -1) {
    		return name;
    	}
    	
    	String[] split = name.split("_");
    	String humpName = "";
    	for (String str : split) {
    		humpName += convertHumpStyle(str);
		}
    	//首字母小写化。
    	return humpName.substring(0, 1).toLowerCase() + humpName.substring(1);
    }
    
    /**校验name和type是否合法
     * @param name
     * @param type
     * @return
     */
    public boolean check(String name, String type) {
        if("".equals(name) || name == null || name.trim().length() ==0){
            return false;
        }
        if("".equals(type) || type == null || type.trim().length() ==0){
            return false;
        }
        return true;
        
    }
    
    /**获取get方法字符串
     * @param name
     * @param type
     * @return
     */
    private StringBuffer getGetMethodString(String name, String type) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        };
        
        //标准化名称
        String humpName = normAttrStr(name);
        
        String methodName = "get"+convertHumpStyle(humpName);
        String format ="";
        if(name.equals(key)){
            format += "    @Id\n";
        }
        format += "    @Column(name = \""+name+"\")\n";
        format +=String.format("    public %s %s(){\n\r", new Object[]{type,methodName});
        format += String.format("        return this.%s;\r\n", new Object[]{humpName});
        format += "    }\r\n";
        return new StringBuffer(format);
    }
    
     /** 转换为驼峰命名法。将名称首字符大写。
     * @param name
     * @return
     */
    String convertHumpStyle(String name) {
        name = name.trim();
        if (name.length() > 1) {
            name = name.substring(0, 1).toUpperCase()+name.substring(1);
        } else {
            name = name.toUpperCase();
        }
        return name;
    }
    /**获取字段的get方法字符串
     * @param name
     * @param type
     * @return
     */
    private Object getSetMethodString(String name, String type) {
        if(!check(name,type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        };
        //标准化名称
        name = normAttrStr(name);
        String methodname = "set"+convertHumpStyle(name);
        String format = String.format("    public void %s(%s %s){\n\r", new Object[]{methodname,type,name});
        format += String.format("        this.%s = %s;\r\n", new Object[]{name,name});
        format += "    }\r\n";
        return new StringBuffer(format);
    }
    
    

    /** demo
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    	//直接在构造方法中指定数据库表名 和 java类的包名
        DataBaseReflectBean bean = new DataBaseReflectBean("bill_trans_detail", "cn.gdeng.paycenter.entity.pay");
        String content=bean.getClassStr().toString();
        System.err.println(content);
 
    }
    
}

