package cn.gdeng.paycenter.util.web.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * JSON处理工具
 * 
 *
 */
public class GSONUtils {
	private static Gson gson;
	/**
	 * 将对象转换为json字符串。 如果对象（或其持有的对象）的某个属性为日期类型，
	 * 转换时将使用本系统默认的日期字符串格式"yyyy-MM-dd HH:mm:ss"。
	 * 
	 * @param obj
	 *            需要准换的对象。
	 * @param isPrettyPrinting
	 *            一个转换规则标志，该标志表明：“是否要将转换的结果按照大家习惯的分行缩进方式进行排版。”
	 * @return 返回转换后的Json 字符串
	 */
	public static String toJson(Object obj, boolean isPrettyPrinting) {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

		if (isPrettyPrinting) {
			gsonBuilder.setPrettyPrinting();
		}

		Gson gson = gsonBuilder.create();

		String json = gson.toJson(obj);
		return json;
	}
	
	public static Gson getGson() {
		if (gson == null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm");
			return gsonBuilder.create();
		} else {
			return gson;
		}
	}
	
	/**
	 * 将结果加密后，封装进map，返回最终的json结果
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String finalJson(Object obj) throws Exception {
		String json = GSONUtils.getGson().toJson(obj);
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("data", Des3Response.encode(json));
		return  GSONUtils.getGson().toJson(temp);
	}
	
	/**
	 * String型jSon转成map
	 * @param jsonStr
	 * @return Map
	 * 
	 * map中的value，已经有特定属性（如Integer)，而非object，
	 */
	  @SuppressWarnings("unchecked")
	    public static Map<String, Object> fromJson(String jsonStr) {
	        ObjectMapper mapper = new ObjectMapper();
	        try {
	            return mapper.readValue(jsonStr, Map.class);
	        } catch (IOException e) {
	            return null;
	        }
	    }
	  
	  /**
		 * String型jSon转成map
		 * @param jsonStr
		 * @return  Map<String, String>
		 * 
		 */
	   public static Map<String, String> fromJsonToMapStr(String jsonStr){
			return  GSONUtils.getGson().fromJson(jsonStr, new TypeToken<Map<String, String>>(){}.getType());
	   }
	   
	   /**
		 * String型jSon转成map
		 * @param jsonStr
		 * @return  Map<String, String>
		 * 
		 */
	   public static Map<String, Object> fromJsonToMapObj(String jsonStr){
			return  GSONUtils.getGson().fromJson(jsonStr, new TypeToken<Map<String, Object>>(){}.getType());
	   }
	  
	
	  /**
		 * String型jSon,去对应的key值
		 * @param jsonStr,key
		 * @return Object
		 */
	  public static Object getJsonValue(String jsonStr,String key){
		  Object object=null;
			Map<?, ?> map = fromJson(jsonStr);
			if(map!=null && map.size()>0){
				if(key!=null && !"".equals(key)){
					object = (Object) map.get(key);
				}
			}
			return object;
	  }
	  
	  /**
		 * String型jSon,去对应的key值
		 * @param jsonStr,key
		 * @return String
		 */
	  public static String getJsonValueStr(String jsonStr,String key){
		  String object=null;
			Map<String, String> map = fromJsonToMapStr(jsonStr);
			if(map!=null && map.size()>0){
				if(key!=null && !"".equals(key)){
					object = (String) map.get(key);
				}
			}
			return object;
	  }
	  
		/**
		 * String型jSon转成 Object
		 * 
		 * @param <T>
		 * @param jsonStr
		 * @return
		 * @return Object
		 * 
		 */
	   public static <T> Object fromJsonToObject(String jsonStr,Class<T> classOfT){
			return  GSONUtils.getGson().fromJson(jsonStr,classOfT);
	   }
	  
	  

}
