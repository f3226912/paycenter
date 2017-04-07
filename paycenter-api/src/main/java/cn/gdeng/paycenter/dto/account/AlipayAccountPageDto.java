package cn.gdeng.paycenter.dto.account;

import java.util.List;

/**
 * 阿里账务DTO
 * @author sss
 *
 */
public class AlipayAccountPageDto {

	/**
	 * 当查询账务明细失败时返回的错误提示码
	 */
	private String error;
	
	private String sign;
	
	private String sign_type;
	
	/**
	 * 请求是否成功。请求成功不代表业务处理成功。 T 成功 F失败 
	 */
	private String is_success;
	
	/**
	 * 是否有下一页 T有 F没有
	 */
	private String has_next_page;
	
	/**
	 * 当前页码
	 */
	private String page_no;
	
	/**
	 * 分页大小
	 */
	private String page_size;
	
	/**
	 * 账务明细列表
	 */
	private List<AccountQueryAccountLogVO> account_log_list;
	
	private String xml;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	public String getHas_next_page() {
		return has_next_page;
	}

	public void setHas_next_page(String has_next_page) {
		this.has_next_page = has_next_page;
	}

	public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public List<AccountQueryAccountLogVO> getAccount_log_list() {
		return account_log_list;
	}

	public void setAccount_log_list(List<AccountQueryAccountLogVO> account_log_list) {
		this.account_log_list = account_log_list;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	
}
