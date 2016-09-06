package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.UInviteteam;
import upbox.service.PublicService;
import upbox.service.UInviteteamService;
import upbox.service.UPlayerService;
import upbox.service.UUserService;
/**
 * 队员被邀请加入队伍信息接口
 * @author mercideng
 *
 */
@Service("uInviteteamServiceImpl")
public class UInviteteamServiceImpl implements UInviteteamService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UUserService uUserService;
	
	@Resource
	private UPlayerService uPlayerService;
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
	@Override
	public void saveUinviteInfo(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam) throws Exception {
		UInviteteam uInviteteam = new UInviteteam();
		if (null != uTeam) {
			//查询用户信息
			UUser uUser = uUserService.getUserinfoByUserId(map);
			map.put("playerId", uPlayer.getPlayerId());
			map.put("teamId", uTeam.getTeamId());
			//查询当前用户是否邀请过这个球员
			uInviteteam = baseDAO.get(map, "from UInviteteam where uUser.userId=:loginUserId and uPlayer.playerId=:playerId and uTeam.teamId=:teamId");
			if (null == uInviteteam) {//没有，新增
				uInviteteam = new UInviteteam();
				uInviteteam.setId(WebPublicMehod.getUUID());
				uInviteteam.setCreateTime(new Date());
				uInviteteam.setuUser(uUser);
				uInviteteam.setuPlayer(uPlayer);
				uInviteteam.setuTeam(uTeam);
				uInviteteam.setInvStatus("0");//待回复
				baseDAO.save(uInviteteam);
			}
		}
	}
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
	@Override
	public UInviteteam getInvateTeam(HashMap<String, String> map) throws Exception {
		UInviteteam uInviteteam = baseDAO.get(map, "from UInviteteam where uUser.userId=:loginUserId and uPlayer.playerId=:playerId and uTeam.teamId=:teamId");
		return uInviteteam;
	}
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
	@Override
	public List<UInviteteam> getInvateTeamList(HashMap<String, String> map) throws Exception {
		List<UInviteteam> uInviteteams = baseDAO.get(map, "from UInviteteam where uUser.userId=:loginUserId and uTeam.teamId=:teamId");
		return uInviteteams;
	}
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
	@Override
	public void updateInviteStatus(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam) throws Exception {
		String playerId = "";
		//查询当前用户的球员Id
		if (null != uTeam) {
			if (null != uPlayer) {
				if (null != uPlayer.getUUser()) {
					map.put("loginUserId", uPlayer.getUUser().getUserId());
					UPlayer uPlayerTemp = uPlayerService.getUplayerByUserIdInJoinTeam(map);
					if (null != uPlayerTemp) {
						playerId = uPlayerTemp.getPlayerId();
					}
				}
			}
			map.put("playerId", playerId);
			map.put("teamId", uTeam.getTeamId());
			//查询这个球队里面球员是否还存在邀请信息
			List<UInviteteam> uInviteteams = baseDAO.find(map, "from UInviteteam where uPlayer.playerId=:playerId and uTeam.teamId=:teamId");
			if (null != uInviteteams && uInviteteams.size() > 0) {
				for (UInviteteam uInviteteam : uInviteteams) {
					if (null != uInviteteam) {
						if ("0".equals(uInviteteam.getInvStatus())) {//将邀请状态待回复改为接受
							uInviteteam.setInvStatus("1");//接受
							baseDAO.update(uInviteteam);
						}
					}
				}
			}
		}
	}
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
	@Override
	public void delInviteStatus(HashMap<String, String> map, UPlayer uPlayer, UTeam uTeam) throws Exception {
		String playerId = "";
		if (null != uTeam) {
			if (null != uPlayer) {
				if (null != uPlayer.getUUser()) {
					//查询当前用户的球员Id
					map.put("loginUserId", uPlayer.getUUser().getUserId());
					UPlayer uPlayerTemp = uPlayerService.getUplayerByUserIdInJoinTeam(map);
					if (null != uPlayerTemp) {
						playerId = uPlayerTemp.getPlayerId();
					}
				}
			}
			map.put("playerId", playerId);
			map.put("teamId", uTeam.getTeamId());
			//查询这个球队里面球员是否还存在邀请信息
			List<UInviteteam> uInviteteams = baseDAO.find(map, "from UInviteteam where uPlayer.playerId=:playerId and uTeam.teamId=:teamId");
			if (null != uInviteteams && uInviteteams.size() > 0) {
				for (UInviteteam uInviteteam : uInviteteams) {
					if (null != uInviteteam) {
						//存在，清空
						baseDAO.delete(uInviteteam);
					}
				}
			}
		}
	}

}
