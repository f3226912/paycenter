package cn.gdeng.paycenter.entity.pay;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 农速通公告实体
 * @author xiaojun
 */
public class NstNoticeEntity implements Serializable {
	private static final long serialVersionUID = 4249590603865811312L;
	/**
	 * 农速通公告主键id
	 */
	private Long id;
	/**
	 * 公告内容
	 */
	private String notice;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 创建用户id
	 */
	private String createUserId;
	/**
	 * 修改时间
	 */
	private String updateTime;
	/**
	 * 修改用户id
	 */
	private String updateUserId;

	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "notice")
	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	@Column(name = "startTime")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	@Column(name = "endTime")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	@Column(name = "createTime")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "createUserId")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "updateTime")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "updateUserId")
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
}
