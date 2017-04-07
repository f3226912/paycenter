package cn.gdeng.paycenter.enums;

/**
 * 结算详情 代付款类型
 * @author huang
 * @Date:2016年11月12日上午11:20:31
 */
public enum SettleAccountOrderTypeEnum {
	
	/**  农商友采购订单**/
	NSY_PURCHASE_ORDER((byte)1, "农商友采购订单"),
	/** 农批商采购订单**/
	NPS_PURCHASE_ORDER((byte)2, "农批商采购订单"),
	/** 服务订单**/
	SERVICE_ORDER((byte)4,"服务订单"),
	/** 货运订单**/
	FREIGHT_ORDER((byte)21,"货运订单"),
	/** 信息费订单**/
	INFO_ORDER((byte)22,"信息费订单"),
	/**买家 佣金**/
	BUYERS_COMMISSION((byte)100,"买家佣金"),
	/**卖家 佣金**/
	Seller_COMMISSION((byte)101,"卖家佣金"),;

	private Byte code;
	
	private String name;

	private SettleAccountOrderTypeEnum(Byte code, String name) {
		this.code = code;
		this.name = name;
	}

	public Byte getCode() {
		return code;
	}

	public void setCode(Byte code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String getNameByCode(Byte code){
		SettleAccountOrderTypeEnum[] values = SettleAccountOrderTypeEnum.values();
		for(SettleAccountOrderTypeEnum val : values){
			if(val.getCode().equals(code)){
				return val.getName();
			}
		}
		return null;
	}
}
