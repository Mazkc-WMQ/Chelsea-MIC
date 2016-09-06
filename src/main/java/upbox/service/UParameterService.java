package upbox.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upbox.model.UParameterInfo;


/**
 * 前端参数接口
 */
public interface UParameterService {
	/**
	 * 
	 * 
	   TODO - 获取参数明细 【2.0.0】
	   @param uParameterList
	   		param的uParameterList
	   @return
	   		
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	public HashMap<String, Object> getUParameterInfoList(List<Map<String, Object>> uParameterList)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户在加队，修改信息，转让时的角色列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	public HashMap<String, Object> getMemberTypeListByTeamId202(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询某个角色的参数实体
	   @param map
	   		name    member_type
	   		params  1
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	public UParameterInfo getMemberTypeByTeamId202(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 获取所有身份集合
	   @param map
	   		name     参数对应的名称  例如：身份=member_type
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	public List<UParameterInfo> getMemberTypeList202(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 根据角色分配等级［2.0.3］
	 * @param memberType
	 * @return
	 * xiaoying 2016年7月27日
	 */
	public Integer getPlayerRoleLimitLvl(Integer memberType);

	/**
	 * 
	 * TODO 分配角色＋加入球队申请角色＋申请角色列表
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月8日
	 */
	public HashMap<String, Object> getMemberTypeListByApplyJoinTeam(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改排序
	   @param map
	   @return
	   @throws Exception
	   2016年8月15日
	   dengqiuru
	 */
	public HashMap<String, Object> updateRank(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * TODO 是否是唯一角色
	 * @param memberType
	 * @return
	 * xiaoying 2016年8月18日
	 */
	public Integer getPlayerRoleMemberTypeIsUnique(Integer memberType);
	
}
