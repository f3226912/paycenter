package cn.gdeng.paycenter.util.web.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EnvUtil {

	private static List<String> SINGLE_NODES = new ArrayList<>();
	
	static {
		SINGLE_NODES.add("true");
		SINGLE_NODES.add("on");
		SINGLE_NODES.add("1");
		SINGLE_NODES.add("yes");
	}
	
	/**
	 * 是否单节点运行
	 * @param singleNode
	 * @return
	 */
	public static boolean isSingleNode(){
		String singleNode = System.getProperty("single.node");
		if(StringUtils.isEmpty(singleNode)){
			return false;
		}
		for(String s : SINGLE_NODES){
			if(s.equalsIgnoreCase(singleNode)){
				return true;
			}
		}
		return false;
	}
}
