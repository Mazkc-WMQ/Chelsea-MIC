package upbox.service;

import java.util.HashMap;

/**
 * 积分信息接口
 * 
 * @author yc
 *
 */
public interface IntegralService {
	
	
	/**
	 * 获取当前小场次激数列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getIntegralListWithBs(HashMap<String, String> map) throws Exception;
	

}
