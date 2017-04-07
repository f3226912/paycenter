package cn.gdeng.paycenter.util.server.sign;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *数字签名工具类
 * @author zhangnf20161109
 *
 */
@Entity(name = "aaa")
public class SignDto implements Serializable{

	private static final long serialVersionUID = -5398234881567599473L;

	/**主键**/
	private Long id;
	/**签名**/
	private String sign;
	
	@Id
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="sign")
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
