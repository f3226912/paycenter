package cn.gdeng.paycenter.admin.dto.admin;

import cn.gdeng.paycenter.entity.pay.NstNoticeEntity;

/**
 * 农速通公告DTO
 * @author 小俊
 */
@SuppressWarnings("serial")
public class NstNoticeEntityDTO extends NstNoticeEntity {

	/**
	 * 开始时间查询起始时间
	 */
	private String startBeginTime;
	/**
	 * 开始时间查询结束时间
	 */
	private String startEndTime;
	/**
	 * 结束时间查询开始时间
	 */
	private String endBeginTime;
	/**
	 * 结束时间查询结束时间
	 */
	private String endEndTime;

	public String getStartBeginTime() {
		return startBeginTime;
	}

	public void setStartBeginTime(String startBeginTime) {
		this.startBeginTime = startBeginTime;
	}

	public String getStartEndTime() {
		return startEndTime;
	}

	public void setStartEndTime(String startEndTime) {
		this.startEndTime = startEndTime;
	}

	public String getEndBeginTime() {
		return endBeginTime;
	}

	public void setEndBeginTime(String endBeginTime) {
		this.endBeginTime = endBeginTime;
	}

	public String getEndEndTime() {
		return endEndTime;
	}

	public void setEndEndTime(String endEndTime) {
		this.endEndTime = endEndTime;
	}
}
