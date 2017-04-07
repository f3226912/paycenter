package cn.gdeng.paycenter.dto.pay;
/**
* @author DJB
* @version 创建时间：2016年11月24日 上午9:55:12
* 类说明
*/

public class AccessSysConfigDTO implements java.io.Serializable{

	/**
	 * cn.gdeng.paycenter.dto.pay
	 */
	private static final long serialVersionUID = 126335727761482612L;
	
	private String appKey;
	
	private String name;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
