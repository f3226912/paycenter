package cn.gdeng.paycenter.util.web.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量类;
 * 
 * @author Administrator
 */
public class CommonConstant {

	// ctrl+shift+G 查看各个字符串引用
	/**
	 * @公用
	 */
	public static final int COMMON_PAGE_NUM = 10;
	public static final String COMMON_AJAX_SUCCESS = "success";
	public static final String COMMON_AJAX_ERROR = "error";
	public static final String COMMON_EXCEPTION_MESSAGE = "exception";
	
	/**
	 * 当前用户可创建的的用户类型 
	 *
	 */
	public final static class UserLowType {
		
		private int id;
		
		private String text;
		
		/**
		 * 根据用户类型获取用户下的类型
		 * @date 创建时间：2015年8月13日 上午11:41:32
		 * @param type
		 * @return
		 *
		 */
		public static List<UserLowType> getLowTypeDTOList(int type){
			Map<Integer, List<UserLowType>> lowTypeMap = new HashMap<Integer, List<UserLowType>>();
			
			//0系统，1联采中心，2配送站，3学校，4食堂，5基地
			UserLowType dto0=new UserLowType();
			dto0.setId(0);
			dto0.setText("系统");
			
			UserLowType dto1=new UserLowType();
			dto1.setId(1);
			dto1.setText("联采中心");
			
			UserLowType dto2=new UserLowType();
			dto2.setId(2);
			dto2.setText("配送站");
			
			UserLowType dto3=new UserLowType();
			dto3.setId(3);
			dto3.setText("学校");
			
			UserLowType dto4=new UserLowType();
			dto4.setId(4);
			dto4.setText("食堂");
			
			UserLowType dto5=new UserLowType();
			dto5.setId(5);
			dto5.setText("基地");
			
			//系统可建：0系统，1联采中心，2配送站，3学校，4食堂，5基地
			List<UserLowType> sys=new ArrayList<UserLowType>();
			//sys.add(dto0);
			sys.add(dto1);
			sys.add(dto2);
			sys.add(dto3);
			sys.add(dto4);
			sys.add(dto5);
			lowTypeMap.put(0, sys);
			
			//联采中心可建：1联采中心，2配送站，3学校，4食堂，5基地
			List<UserLowType> purchase=new ArrayList<UserLowType>();
			purchase.add(dto1);
			purchase.add(dto2);
			purchase.add(dto3);
			purchase.add(dto4);
			purchase.add(dto5);
			lowTypeMap.put(1, purchase);
			
			//配送站可建：2配送站
			List<UserLowType> distrib=new ArrayList<UserLowType>();
			distrib.add(dto2);
			lowTypeMap.put(2, distrib);
			
			//学校可建：3学校，4食堂
			List<UserLowType> school=new ArrayList<UserLowType>();
			school.add(dto3);
			school.add(dto4);
			lowTypeMap.put(3, school);
			
			//食堂可建：4食堂
			List<UserLowType> cook=new ArrayList<UserLowType>();
			cook.add(dto4);
			lowTypeMap.put(4, cook);
			
			//基地可建：5基地
			List<UserLowType> base=new ArrayList<UserLowType>();
			base.add(dto5);
			lowTypeMap.put(5, base);
			
			return lowTypeMap.get(type);
		}
		
		/**
		 * 根据用户类型获取下级类型ID list
		 * @param type
		 * @return
		 *
		 */
		public static List<Integer> getLowTypeIdList(int type){
			
			Map<Integer, List<Integer>> lowTypeList = new HashMap<Integer, List<Integer>>();
			//系统可建：0系统，1联采中心，2配送站，3学校，4食堂，5基地
			List<Integer> sysList=Arrays.asList(new Integer[]{1,2,3,4,5});
			lowTypeList.put(0, sysList);
			//联采中心可建：1联采中心，2配送站，3学校，4食堂，5基地
			List<Integer> purList=Arrays.asList(new Integer[]{1,2,3,4,5});
			lowTypeList.put(1, purList);
			//配送站可建：2配送站
			List<Integer> disList=Arrays.asList(new Integer[]{2});
			lowTypeList.put(2, disList);
			//学校可建：3学校，4食堂
			List<Integer> scList=Arrays.asList(new Integer[]{3,4});
			lowTypeList.put(3, scList);
			//食堂可建：4食堂
			List<Integer> coList=Arrays.asList(new Integer[]{4});
			lowTypeList.put(4, coList);
			//基地可建：5基地
			List<Integer> baseList=Arrays.asList(new Integer[]{5});
			lowTypeList.put(5, baseList);
			return lowTypeList.get(type);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
	
	/**
	 * 用户类型
	 *
	 */
	public final static class UserType{
		/**系统*/
		public final static Integer SYSTEM=0;
		/**联采中心*/
		public final static Integer PURCHASE_CENTER=1;
		/**配送站*/
		public final static Integer DISTRIBUTE_CENTER=2;
		/**学校*/
		public final static Integer SCHOOL=3;
		/**食堂，*/
		public final static Integer CANTEEN=4;
		/**基地*/
		public final static Integer PRODUCT_BASE=5;
		
	}
	
	public static void main(String[] args) {
		List<UserLowType> list=UserLowType.getLowTypeDTOList(3);
		for (UserLowType userLowType : list) {
			System.out.println(userLowType.getId()+"  "+userLowType.getText());
		}
		System.out.println(UserLowType.getLowTypeIdList(5));
	}
}
