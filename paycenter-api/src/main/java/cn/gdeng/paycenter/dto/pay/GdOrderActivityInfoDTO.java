package cn.gdeng.paycenter.dto.pay;

import java.util.List;

/**
 * 订单活动信息实体类
 * @author TerryZhang
 */
public class GdOrderActivityInfoDTO implements java.io.Serializable{

	private static final long serialVersionUID = 140676689317588192L;

	/** 活动id */
	private Integer actId;
	
	/** 活动版本 */
	private Integer actVersion;

	/** 活动名称 */
	private String actName;
	
	/** 配送方式 0自提(默认) 1平台配送 */
	private List<Integer> distributeModeList;
	
	/** 平台佣金 */
	private Double platCommision = 0D;

	/** 市场佣金 */
	private Double marketCommision = 0D;

	/** 补贴金额 */
	private Double subsidy = 0D;

	/** 预付金额 */
	private Double prepayAmt = 0D;
	
	/** 卖家违约金 */
	private Double sellerPenalty = 0D;
	
	/** 平台违约金 */
	private Double platPenalty = 0D;
	
	/** 物流公司违约金 */
	private Double companyPenalty = 0D;
	
	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public Integer getActVersion() {
		return actVersion;
	}

	public void setActVersion(Integer actVersion) {
		this.actVersion = actVersion;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	
	public List<Integer> getDistributeModeList() {
		return distributeModeList;
	}

	public void setDistributeModeList(List<Integer> distributeModeList) {
		this.distributeModeList = distributeModeList;
	}
	
	public Double getPlatCommision() {
		return platCommision;
	}

	public void setPlatCommision(Double platCommision) {
		this.platCommision = platCommision;
	}

	public Double getMarketCommision() {
		return marketCommision;
	}

	public void setMarketCommision(Double marketCommision) {
		this.marketCommision = marketCommision;
	}

	public Double getSubsidy() {
		return subsidy;
	}

	public void setSubsidy(Double subsidy) {
		this.subsidy = subsidy;
	}

	public Double getPrepayAmt() {
		return prepayAmt;
	}

	public void setPrepayAmt(Double prepayAmt) {
		this.prepayAmt = prepayAmt;
	}

	public Double getSellerPenalty() {
		return sellerPenalty;
	}

	public void setSellerPenalty(Double sellerPenalty) {
		this.sellerPenalty = sellerPenalty;
	}

	public Double getPlatPenalty() {
		return platPenalty;
	}

	public void setPlatPenalty(Double platPenalty) {
		this.platPenalty = platPenalty;
	}

	public Double getCompanyPenalty() {
		return companyPenalty;
	}

	public void setCompanyPenalty(Double companyPenalty) {
		this.companyPenalty = companyPenalty;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("actId: " + getActId());
		sb.append(", actVersion: " + getActVersion());
		sb.append(", actName: " + getActName());
		if(getDistributeModeList() != null && getDistributeModeList().size() > 0){
			for(int i=0, len=getDistributeModeList().size(); i<len; i++){
				Integer mode = getDistributeModeList().get(i);
				sb.append(", distributeMode"+i+": " + mode);
			}
		}
		
		sb.append(", platCommision: " + getPlatCommision());
		sb.append(", marketCommision: " + getMarketCommision());
		sb.append(", subsidy: " + getSubsidy());
		
		return sb.toString();
	}
	
	
}
