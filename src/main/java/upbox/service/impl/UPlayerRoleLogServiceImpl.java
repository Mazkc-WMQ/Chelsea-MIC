package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UPlayer;
import upbox.model.UPlayerRolelog;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UPlayerRoleLogService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 前端球员身份转让记录接口实现类
 * @author mercideng
 *
 */

@Service("uPlayerRoleLogService")
public class UPlayerRoleLogServiceImpl implements UPlayerRoleLogService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UUserService uUserService;
	
	@Resource
	private UTeamService uTeamService;
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
	@Override
	public void createNewLog202(HashMap<String, String> map, UPlayer uPlayerTemp) throws Exception {
		UPlayerRolelog UPlayerRolelog = new UPlayerRolelog();
		UPlayerRolelog.setKeyId(WebPublicMehod.getUUID());
		UPlayerRolelog.setbUUser(uPlayerTemp.getUUser());
		UPlayerRolelog.setzUUser(uUserService.getUserinfoByUserId(map));
		UPlayerRolelog.setUTeam(uTeamService.findPlayerInfoById(map));
		UPlayerRolelog.setMemberType(map.get("memberType"));
		UPlayerRolelog.setCreateDate(new Date());
		baseDAO.save(UPlayerRolelog);
	}
	
}
