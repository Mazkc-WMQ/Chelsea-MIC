package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UPlayerRoleLimit;


/**
 * 球员身份权限接口
 * @author mercideng
 *
 */
public interface UPlayerRoleLimitService {
	/**
	 * 
	 * 
	   TODO - 在角色权限表填充初始值
	   @param map
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	public HashMap<String,Object> setPlayerRoleLimit(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据类型判断用户是否有相关类型的权限
	   @param map
	   		type 1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募;10:分配；11：阵型修改
	   		teamId			球队Id
	   		loginUserId		当前用户Id
	   @return
	   @throws Exception
	   2016年6月18日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> playerIsRoleByType(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据角色获取角色权限对象
	   @param map
	   		memberType		角色值
	   @return
	   @throws Exception
	   2016年6月18日
	   dengqiuru
	 */
	public UPlayerRoleLimit getUplayerRoleLimitByMemberType(HashMap<String, String> map) throws Exception;
}
