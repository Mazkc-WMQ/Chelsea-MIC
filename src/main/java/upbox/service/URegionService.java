package upbox.service;

import java.util.HashMap;
import java.util.Set;

import upbox.model.URegion;

/**
 * 行政区域接口
 * 
 * @author yc
 *
 */
public interface URegionService {
	
	/**
	 * 创建新政区域码表内容 【2.0.0】
	 * @throws Exception
	 */
	public void initAreas() throws Exception;
	


	/**
	 * 
	 * 
	   TODO - 获取整体区域 【2.0.0】
	   @param uUser
	   		uUser对象
	   2016年3月14日
	   dengqiuru
	 */
	public Set<URegion> getURegionSet(HashMap<String, String> map) throws Exception;
	
	
	/**
	 * 创建新政区域码表内容 【2.0.0】
	 * @throws Exception
	 */
	public void newInitAreas() throws Exception;


	/**
	 * 
	 * 
	   TODO - 根据area 获取区域对象 【2.0.0】
	   @param map
	    	area   区域主键
	   2016年3月18日
	   dengqiuru
	 * @throws Exception 
	 */
	public URegion getURegionInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 获取区域(一层一层选择)
	   @param map
	   		lvl   	1:省;2:市区;3:区/县
	   		keyId  	区域Id
	   @return
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	public HashMap<String, Object> getURegionListByType(HashMap<String, String> map) throws Exception;


	/**
	 * 
	 * 
	   TODO - 根据区域返回地区字符串
	   @param map
	   		area		区域
	   @return
	   2016年7月2日
	   dengqiuru
	 * @throws Exception 
	 */
	public String getURegionInfoByArea(HashMap<String, Object> map) throws Exception;
	
	
}
