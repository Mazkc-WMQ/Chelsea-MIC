package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UInviteteam;
import upbox.model.UPlayer;
import upbox.model.UTeam;

/**
 * 队员被邀请加入队伍信息接口
 * @author mercideng
 *
 */
public interface UInviteteamService {

	/**
	 * 
	 * 
	   TODO - 邀请时，新增邀请信息 【2.0.0】
	   @param map
	   		loginUserId   当前用户的Id
	   @param uPlayer
	   		uPlayer		  对象
	   @param uTeam
	   		uTeam		  对象
	   @throws Exception
	   2016年3月30日
	   dengqiuru
	 */
	public void saveUinviteInfo(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查看邀请状态 【2.0.0】
	   @param map
	   		playerId	  	被邀请的球员Id
	   		myTeamId		邀请人的teamId
	   		loginUserId     当前用户的Id
	   @return
	   		UInviteteam		  对象
	   @throws Exception
	   2016年3月30日
	   dengqiuru
	 */
	public UInviteteam getInvateTeam(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 加队时，若有邀请，就修改邀请状态 【2.0.0】
	   @param map
	   		loginUserId   当前用户的Id
	   @param uPlayer
	   		uPlayer		  对象
	   @param uTeam
	   		uTeam		  对象
	   @throws Exception
	   2016年3月30日
	   dengqiuru
	 */
	public void updateInviteStatus(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam)throws Exception;

	/**
	 * 
	 * 
	   TODO - 查看邀请状态 【2.0.0】
	   @param map
	   		playerId	  	被邀请的球员Id
	   		myTeamId		邀请人的teamId
	   @return
	   		UInviteteam		  集合
	   @throws Exception
	   2016年3月30日
	   dengqiuru
	 */
	public List<UInviteteam> getInvateTeamList(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 退出球队时，清空邀请 【2.0.0】
	   @param map
	   @param uPlayer
	   @param uTeam
	   @throws Exception
	   2016年5月4日
	   dengqiuru
	 */
	public void delInviteStatus(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam) throws Exception;;
	
}
