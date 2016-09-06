package upbox.service;

import java.util.HashMap;



/**
 * 前端关注接口
 */
public interface UFollowService {
	
	/**
	 * 
	 * 
	   TODO - 判断是否关注过对应球员 【2.0.0】
	   @param map
	   		loginUserId		登录人
	   		objectId		对应事件的Id
	   @return
	   		关注状态   1：已关注，2：未关注
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	public String isFollow(HashMap<String, String> map) throws Exception;
}
