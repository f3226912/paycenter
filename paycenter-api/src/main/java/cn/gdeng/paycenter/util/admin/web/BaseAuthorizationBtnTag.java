package cn.gdeng.paycenter.util.admin.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import cn.gdeng.paycenter.util.web.api.Constant;

/**
 * 页面button授权部分标签;
 * 
 * @author huyong
 * @date 2012-06-20;
 * 
 */
public class BaseAuthorizationBtnTag extends TagSupport {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 3456814528101101883L;

	/** 按钮code */
	private String btncode;

	/**
	 * 显示tag;如果存在按钮，则显示该按钮，否则不显
	 */
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {

		// 如果有取得session的用户按钮权限集合，则进行button的权限的check;如果取到，则不显
		if (pageContext.getSession().getAttribute(Constant.SYSTEM_BUTTONCODE) != null) {
			// 取得Button集合<000001,"新增">;
			Map<String, String> map = (Map<String, String>) pageContext.getSession().getAttribute(Constant.SYSTEM_BUTTONCODE);

			// 当为多个按钮权限则以"|"分隔
			if (btncode.contains("||")) {
				List<String> codeList = Arrays.asList(btncode.split("\\|\\|"));
				for (String code : codeList) {
					if (map.get(StringUtils.trimToNull(code.toUpperCase())) != null) {
						return SKIP_PAGE;
					}
				}
			} else {
				if (map.get(btncode.toUpperCase()) != null) {
					return SKIP_PAGE;
				}
			}
		}

		// 跳过body,body部分不会显示
		return SKIP_BODY;
	}

	public String getBtncode() {
		return btncode;
	}

	public void setBtncode(String btncode) {
		this.btncode = btncode;
	}

	public static void main(String[] args) {
		String a = "ad||bggg";
		List<String> ar = Arrays.asList(a.split("\\|\\|"));
		System.out.println(ar);
	}

}
