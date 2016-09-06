package upbox.service;

import java.util.HashMap;

import upbox.model.UPlayer;

/**
 * 球员身份转让记录接口
 * @author mercideng
 *
 */
public interface UPlayerRoleLogService {

	/**
	 * 
	 * 
	   TODO - 转让角色时，新增一条转让记录
	   @param map
	   		teamId		球队Id
	   		loginUserId	当前用户Id
	   		memberType	转让角色
	   @param uPlayerTemp
	   		被转让这的球员对象
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	public void createNewLog202(HashMap<String, String> map, UPlayer uPlayerTemp)throws Exception;
}
