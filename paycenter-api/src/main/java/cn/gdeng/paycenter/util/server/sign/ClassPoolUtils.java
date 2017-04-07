package cn.gdeng.paycenter.util.server.sign;

import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * 对象池工具类
 * @author zhangnf20161109
 *
 */
public class ClassPoolUtils {
    
    
    /**
     * 运行时动态ORM表映射
     * 
     * 
     * @param entityClassName   待映射的实体全限定类名
     * @param tableName         待映射的表名
     * @return                  映射后的类对象
     */
    public static Class<?> tableMapping(String entityClassName, String tableName){
        Class<?> c = null;
        
        if(StringUtils.isEmpty(entityClassName) || StringUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("The mapping parameter is invalid!");
        }
        
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new ClassClassPath(ClassPoolUtils.class));
            classPool.importPackage("javax.persistence");
            CtClass clazz = classPool.get(entityClassName);
            clazz.defrost();
            ClassFile classFile = clazz.getClassFile();
           
            ConstPool constPool = classFile.getConstPool();
            Annotation tableAnnotation = new Annotation("javax.persistence.Entity", constPool);
            tableAnnotation.addMemberValue("name", new StringMemberValue(tableName, constPool));
            // 获取运行时注解属性
            AnnotationsAttribute attribute = (AnnotationsAttribute)classFile.getAttribute(AnnotationsAttribute.visibleTag);
            attribute.addAnnotation(tableAnnotation);
            classFile.addAttribute(attribute);
            classFile.setVersionToJava5();
            //clazz.writeFile();
            
            //TODO 当前ClassLoader中必须尚未加载该实体。（同一个ClassLoader加载同一个类只会加载一次）
            //c = clazz.toClass();
            EntityClassLoader loader = new EntityClassLoader(ClassPoolUtils.class.getClassLoader());
            c = clazz.toClass(loader , null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return c;
    }  

    public static void main(String[] args) {
        Class<?> clazz = ClassPoolUtils.tableMapping("cn.gdeng.paycenter.util.server.sign.SignDto", "order1");
        System.out.println("修改后的@Table: " + clazz.getAnnotation(Table.class));
    }
}

