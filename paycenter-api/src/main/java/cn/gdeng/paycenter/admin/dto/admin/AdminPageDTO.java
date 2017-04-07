package cn.gdeng.paycenter.admin.dto.admin;

import java.io.Serializable;
import java.util.List;

public class AdminPageDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 597997720833615360L;

	private Integer total; 
	
	private List<?> rows;
	
	public AdminPageDTO() {
		super();
	}

	public AdminPageDTO(Integer total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
