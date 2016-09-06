package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UPlayer;
import upbox.model.UPlayerRole;
import upbox.model.UTeam;
import upbox.outModel.OutPlayerList;
import upbox.outModel.OutUteamList;

/**
 * 球员身份接口
 * @author mercideng
 *
 */
public interface UPlayerRoleService {
	/**
	 * 
	 * 
	   TODO - 身份转让
	   @param map
	   		memberType  转让者角色
	   		teamId		球队Id
	   		playerId	被转让者的球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	public HashMap<String, Object> memberTypeTransfer202(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询当前用球员有几个角色
	   @param map
	   		playerId    球员Id
	   @return
	   @throws Exception
	   2016年6月3日
	   dengqiuru
	 */
	public List<UPlayerRole> getMemberTypeByPlayerId202(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 查询并填充球员的角色
	   @param hashMap2
	   		playerId   球员Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public HashMap<String, Object> setMembertype202(HashMap<String, Object> hashMap2)throws Exception;

	/**
	 * 
	 * 
	   TODO - 选择角色时，判断该角色是否已被别人选了
	   @param map
	   		memberType   	角色
	   		loginUserId		当前用户Id
	   		teamId			当前球队Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public Boolean isOtherSelected202(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 选择角色时，判断该角色是否已被自己选了
	   @param map
	   		memberType   	角色
	   		loginUserId		当前用户Id
	   		teamId			当前球队Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public Boolean isMyselfSelected202(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 添加数据
	   @param uPlayer	对象
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	public void insertNew(UPlayer uPlayer)throws Exception;

	/**
	 * 
	 * 
	   TODO - 加队时，球员新增角色模块
	   @param uPlayer    
	   @param map
	   2016年6月13日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> setNewMemberTypeForJoinTeam(UPlayer uPlayer, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 获取球员信息时，将角色填充到OutPlayerList中
	 * @param outPlayerList 
	   @param uPlayer
	   		对象
	   @param map
	   @return
	   2016年6月13日
	   dengqiuru
	 * @throws Exception 
	 */
	public OutPlayerList setMemberTypeByGetUplayerinfo202(OutPlayerList outPlayerList, UPlayer uPlayer, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改球员信息时，如果参数角色不为空，就更新角色表
	   @param map
	   		memberType		角色
	   @param updatePlayer		对象
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> setMemberTypeByEditplayer202(HashMap<String, String> map, UPlayer updatePlayer) throws Exception;

	/**
	 * 
	 * 
	   TODO - 创建球队时，队长新增一条角色记录
	   @param uPlayer	对象
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	public void createNewMembertypeByInsertNewTeam(UPlayer uPlayer) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查看球队信息时，填充球队球员角色
	   @param outUteamList		对象
	   @param map		
	   		loginUserId			当前用户
	   @return
	   2016年6月15日
	   dengqiuru
	 * @throws Exception 
	 */
	public OutUteamList setMemberTypeByGetUTeaminfo202(OutUteamList outUteamList, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 给球员实体类填充角色
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年6月15日
	   dengqiuru
	 */
	public UPlayer setMemberTypeByGetNoOutUplayerinfo202(UPlayer uPlayer) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查询当前用户是否存在管理角色
	   @param uPlayer
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	public Boolean isUniqueByuserId(UPlayer uPlayer) throws Exception;

	/**
	 * 
	 * 
	   TODO - 填充当前球队为球员的身份
	   @param hashMap2
	   		playerId		球员Id
	   2016年6月25日
	   dengqiuru
	 * @throws Exception 
	 */
	public void setMembertypeByplayer202(HashMap<String, Object> hashMap2) throws Exception;

	/**
	 * 
	 * 
	   TODO - 填充当前球队为官员的身份
	   @param hashMap2
	   		playerId		球员Id
	   2016年6月25日
	   dengqiuru
	 * @throws Exception 
	 */
	public void setMembertypeByManager202(HashMap<String, Object> hashMap2) throws Exception;

	/**
	 * 
	 * 
	   TODO - 给有球队的球员实体类填充角色
	   @param uPlayer
	   @return
	   @throws Exception
	   2016年6月15日
	   dengqiuru
	 */
	public OutPlayerList setMemberTypeByGetUplayerinfoInteam202(OutPlayerList outPlayerList, UPlayer uPlayer,
			HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 查询并填充球员的角色(无顺序)
	   @param hashMap2
	   		playerId   球员Id
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public HashMap<String, Object> setMembertypeNoOrderByRank202(HashMap<String, Object> hashMap2) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改球员信息时，如果参数角色不为空，就更新角色表(第三方系统)
	   @param map
	   		memberType		角色
	   @param updatePlayer		对象
	   @return
	   2016年6月14日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> setEventsMemberTypeByEditplayer202(HashMap<String, String> map, UPlayer updatePlayer)
			throws Exception;
	/**
	 * 
	 * TODO 角色分配[2.0.3]
	 * @param map teamId-球队Id,loginUserId-登录ID，playerId-球员ID,memberTypes-分配给球员的角色（1,2）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月28日
	 */
	public HashMap<String, Object> editMemberTypeAllot(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 申请角色-验证［2.0.3］
	 * @param map teamId-球队Id,loginUserId-登录ID,memberType-申请的角色1=队长
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月30日
	 */
	public HashMap<String, Object> checkApplyPlayerMemberType(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 申请角色［2.0.3］
	 * @param map teamId-球队Id,loginUserId-登录ID,memberType-申请的角色1=队长
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月30日
	 */
	public HashMap<String, Object> applyPlayerMemberType(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * TODO 申请审核－同意［2.0.3］
	 * @param map map teamId-球队Id,loginUserId-登录ID,memberType-申请的角色1=队长,playerId-审核人的球员ID
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月1日
	 */
	public HashMap<String, Object> applyCheckAgreePlayerMemberType(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 申请审核－拒绝［2.0.3］
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月1日
	 */
	public HashMap<String, Object> applyCheckRefusePlayerMemberType(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 申请角色加入球队[2.0.3]
	 * @param player 球员对象
	 * @param uTeam 球队对象
	 * @param map teamId-球队Id,loginUserId-登录ID,memberTypes-申请的角色（1,2）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月30日
	 */
	public HashMap<String, Object> applyPlayerJoinTeamMemberType(UPlayer player,UTeam uTeam, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * TODO 修改角色-个人中[2.0.3]
	 * @param map teamId-球队Id,loginUserId-登录ID,memberTypes-申请的角色（1,2）
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月17日
	 */
	public HashMap<String, Object> editPlayerTeamMemberType(HashMap<String, String> map,UPlayer player) throws Exception;
	/**
	 * 
	 * TODO 查询当前用户是否存在管理角色
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月18日
	 */
	public Boolean isUniqueByuserId(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 超过一周没审核的申请角色数据-拒绝
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月24日
	 */
	public HashMap<String, Object> batchApplyRefusePlayerMemberType(HashMap<String, String> map) throws Exception;


	/**
	 * 
	 * 
	   TODO - 查询当前用户在这个球队中有哪些角色，并拼接成字符串
	   @param uPlayer
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	public String memberTypeStrByPlayerId(UPlayer uPlayer) throws Exception;

	/**
	 * 
	 * 
	   TODO - 阵容
	   @param uPlayer
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> setMembertypebyZhenrong202(HashMap<String, Object> hashMap2) throws Exception;

	
	
}
