package upbox.service;

import java.util.HashMap;

import upbox.model.UCmsLog;

public interface UCmsLogService {
	/**
	 * 
	 * 
	   TODO - 获取球队cms列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年8月16日
	   dengqiuru
	 */
	public HashMap<String, Object> getCmsListByTeamId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据keyId获取球队对象
	   @param map
	   		keyId		主键
	   @return
	   @throws Exception
	   2016年8月16日
	   dengqiuru
	 */
	public UCmsLog getCmsObjectByKeyId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据cmsId获取球队对象
	   @param map
	   		cmsId		主键
	   @return
	   @throws Exception
	   2016年8月16日
	   dengqiuru
	 */
	public UCmsLog getCmsObjectByCmsId(HashMap<String, String> map)throws Exception;
}
