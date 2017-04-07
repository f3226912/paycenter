package cn.gdeng.paycenter.admin.dto.admin;

import java.util.Map;







import cn.gdeng.paycenter.entity.pay.BankRateConfigEntity;
import cn.gdeng.paycenter.enums.AreaBankFlagEnum;
import cn.gdeng.paycenter.enums.AreaBankTypEnum;
import cn.gdeng.paycenter.enums.BankRateConfigStatusEnum;
import cn.gdeng.paycenter.enums.CardTypeEnum;
import cn.gdeng.paycenter.enums.ProceduresEnum;
import cn.gdeng.paycenter.util.web.api.GSONUtils;

public class BankRateConfigDTO extends BankRateConfigEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String max;
	private String procedures="";
	private String proportion;
	private String fixed;
    private String payChannelName;
    
	public String getPayChannelName() {
		return payChannelName;
	}
	public void setPayChannelName(String payChannelName) {
		this.payChannelName = payChannelName;
	}
	public String getMax() { 
		if(getProcedures().equals(ProceduresEnum.BL.getCode())){
			Map<String ,Object> obj =  GSONUtils.fromJson(getFeeRuleJson());
			max=(String)obj.get("max");
		}
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getProcedures() {
		Map<String ,Object> obj =  GSONUtils.fromJson(getFeeRuleJson());
		if(obj!=null){
		procedures=(String)obj.get("procedures");
		}
		return procedures;
	}
	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}
	public String getProportion() {
		if(getProcedures().equals(ProceduresEnum.BL.getCode())){
			Map<String ,Object> obj =  GSONUtils.fromJson(getFeeRuleJson());
			if(null!=obj.get("proportion")){
			proportion=(String)obj.get("proportion");
			}
		}
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	public String getFixed() {
		if(getProcedures().equals(ProceduresEnum.GDZ.getCode())){
			Map<String ,Object> obj =  GSONUtils.fromJson(getFeeRuleJson());
			if(null!=obj.get("fixed")){
				fixed=(String)obj.get("fixed");
				}
		}
		return fixed;
	}
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public String getProceduresStr(){
		if(getProcedures().equals(ProceduresEnum.BL.getCode())){
			if(null!=getMax()&&!"".equals(getMax())&&!"0".equals(getMax())&&!"0.0".equals(getMax())&&!"0.00".equals(getMax())){
			return getProportion()+"%,封顶"+getMax()+"元";
			}else{
		    return getProportion()+"%,不封顶";	
			}
		}
		return getFixed();
	}
   public String getBusiness(){
	  String abf= AreaBankFlagEnum.getNameByCode(getAreaBankFlag());
	  String type= AreaBankTypEnum.getNameByCode(getType());
	  if(null!=type){
		  return abf+"-"+type;
	  }
	  return abf;
   }
   public String getCardTypeStr(){
	return   CardTypeEnum.getNameByCode(getCardType());
   }
   
   public String getStatusStr(){
	   
	   return BankRateConfigStatusEnum.getNameByCode(getStatus());
   }
}
