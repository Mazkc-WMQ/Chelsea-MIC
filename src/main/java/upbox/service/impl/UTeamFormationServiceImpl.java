package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.pub.PublicMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UFollow;
import upbox.model.UMatchBs;
import upbox.model.UParameterInfo;
import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UTeamFormation;
import upbox.model.UTeamFormationInfo;
import upbox.model.UUser;
import upbox.model.UUserImg;
import upbox.model.zhetao.MatchBean;
import upbox.pub.Public_Cache;
import upbox.pub.URLConnectionUtil;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.UCourtService;
import upbox.service.UMatchService;
import upbox.service.UParameterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerService;
import upbox.service.UTeamFormationService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 赛事接口实现
 * 
 * @author yc
 *
 *         13611929818
 */
@Service("uTeamFormationService")
public class UTeamFormationServiceImpl implements UTeamFormationService {

	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private UUserService uuserService;

	@Resource
	private UTeamService uteamService;
	
	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	
	@Resource
	private PublicService publicService;
	
	
	
	

	/**
	 * 统一处理游客模式的userid
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getUserId(HashMap<String, String> map) throws Exception {
		UUser user = uuserService.getUserinfoByUserId(map);
		if (user != null) {
			return user.getUserId();
		} else {
			return null;
		}

	}
	
	/**
	 * 用户是否有阵型权限
	 * @param map
	 * @return 1有权限 0无权限
	 * @throws Exception
	 */
	private String getFormationRole(HashMap<String, String> map) throws Exception{
		String role="0";//开始无权限
		map.put("type", "11");//获取阵型权限
		List<HashMap<String, Object>> templist=uPlayerRoleLimitService.playerIsRoleByType(map);
		if(templist!=null&&templist.size()>0){
			role="1";
		}
		return role;
	}

	/**
	 * 获得对应颜色的球衣图片
	 * @param strColor 颜色编号
	 * @return
	 * @throws Exception
	 */
	private String getShirtImgUrl(String colorNum) throws Exception{
		if (colorNum!=null&&!"".equals(colorNum)) {
			UParameterInfo uParameterInfo =  Public_Cache.HASH_PARAMS_OBJECT("home_team_shirts").get(colorNum);
			if (null != uParameterInfo) {
				if (publicService.StringUtil(uParameterInfo.getImgurl())) {
					return  uParameterInfo.getImgurl();
				}else{
					return "";
				}
			}else{
				return "";
			}
		}else{
			return "";
		}
	}

	@Override
	public HashMap<String, Object> getAllFormationSelList(HashMap<String, String> map) throws Exception {
		String homeTeamShirts="";//主队球衣颜色
		String awayTeamShirts="";//客队球衣颜色
		String teamId=map.get("teamId");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (teamId != null && !"".equals(teamId)){
			List<HashMap<String, Object>> reList=new ArrayList<HashMap<String, Object>>();
			List<UTeamFormation> utf=baseDAO.find(map,"from UTeamFormation where UTeam.teamId=:teamId and delStatus='0' and initStatus='1' order by isDefault desc, createdate desc");
			for(UTeamFormation temp:utf){
				HashMap<String, Object> utfMap=new HashMap<String, Object>();
				utfMap.put("formationId", temp.getKeyId());
				utfMap.put("formationName", temp.getRemark());
				utfMap.put("isDefault", temp.getIsDefault());
				reList.add(utfMap);
			}
			if(utf!=null&&utf.size()>0){
				homeTeamShirts=utf.get(0).getUTeam().getHomeTeamShirts();
				awayTeamShirts=utf.get(0).getUTeam().getAwayTeamShirts();
			}
			UParameterInfo uParameterInfo =  Public_Cache.HASH_PARAMS_OBJECT("home_team_shirts").get(hashMap.get("homeTeamShirts"));
			if (null != uParameterInfo) {
				if (publicService.StringUtil(uParameterInfo.getImgurl())) {
					hashMap.put("homeTeamShirtsImgurl", uParameterInfo.getImgurl());
				}else{
					hashMap.put("homeTeamShirtsImgurl", null);
				}
			}
			hashMap.clear();
			hashMap.put("formationSelList", reList);
			hashMap.put("editable", getFormationRole(map));//是否有编辑权限
			hashMap.put("homeTeamShirts", homeTeamShirts);//主队球衣颜色编号
			hashMap.put("awayTeamShirts", awayTeamShirts);//客队球衣颜色编号
			hashMap.put("homeTeamShirtsImgUrl", getShirtImgUrl(homeTeamShirts));//主队球衣颜色图片
			hashMap.put("awayTeamShirtsImgUrl", getShirtImgUrl(awayTeamShirts));//客队球衣颜色图片
			return hashMap;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}



	@Override
	public HashMap<String, Object> getFormationInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> tempMap=null;
		List<HashMap<String, Object>> fList=new ArrayList<HashMap<String, Object>>();//主队阵型list
		List<HashMap<String, Object>> xList=new ArrayList<HashMap<String, Object>>();//客队阵型list
		HashMap<String, Object> formation=new HashMap<String, Object>();//阵型主对象
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String formationId=map.get("formationId");
		if (formationId != null && !"".equals(formationId)){
			UTeamFormation utf=baseDAO.get(map," from UTeamFormation where keyId=:formationId and delStatus='0' ");
			if(utf!=null){
				formation.put("formationId", utf.getKeyId());//处理阵型主对象
				formation.put("remark", utf.getRemark());
				formation.put("formationNum", utf.getFormationNum());
				formation.put("isDefault", utf.getIsDefault());
				List<UTeamFormationInfo> utIfList=baseDAO.find(map," from UTeamFormationInfo where UTeamFormation.keyId=:formationId ");
				for(UTeamFormationInfo temp:utIfList){
					tempMap=checkUTeamFormationInfo(temp);
					if(temp.getHomeStatus().equals("0")){
						fList.add(tempMap);
					}else if(temp.getHomeStatus().equals("1")){
						xList.add(tempMap);
					}
				}
				hashMap.clear();
				hashMap.put("formation", formation);
				hashMap.put("fList", fList);
				hashMap.put("xList", xList);
				return hashMap;
			}else{
				return WebPublicMehod.returnRet("error", "当前阵型不存在！");
			}
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	/**
	 * 处理阵型对象
	 * @param temp 阵型对象
	 * @return
	 * @throws Exception 
	 */
	private HashMap<String, Object> checkUTeamFormationInfo(UTeamFormationInfo temp) throws Exception{
		HashMap<String, Object> fp=new HashMap<String, Object>();
		HashMap<String, Object> sqlMap=new HashMap<String, Object>();
		
		if(temp.getPlayer()!=null){
			sqlMap.put("playerId", temp.getPlayer().getPlayerId());
			UPlayer up=baseDAO.get("from UPlayer where playerId=:playerId and inTeam='1'",sqlMap);//判断球员在队不在队
			if(up!=null){
				fp.put("inTeam", "1");//在队
			}else{
				fp.put("inTeam", "0");//不在队
			}
			if(temp.getPlayer().getUUser()!=null){
				fp.put("playerRealName", temp.getPlayer().getUUser().getRealname());
				fp.put("playerNickName", temp.getPlayer().getUUser().getNickname());
			}else{
				fp.put("playerRealName", "");
				fp.put("playerNickName", "");
			}
			
			fp.put("playerNum", temp.getPlayer().getNumber());
			fp.put("playerId", temp.getPlayer().getPlayerId());
		}else{
			fp.put("inTeam", "0");
			fp.put("playerNickName", "");
			fp.put("playerNum", "");
			fp.put("playerId", "");
			fp.put("playerRealName", "");
		}
		fp.put("pos", temp.getPos());
		fp.put("x", temp.getX());
		fp.put("y", temp.getY());
		fp.put("num", temp.getNum());
		fp.put("captainStatus", temp.getCaptainStatus());
		fp.put("homeStatus", temp.getHomeStatus());
		fp.put("location", temp.getLocation());
		return fp;
	}

	@Override
	public HashMap<String, Object> saveInitFormationInfo(HashMap<String, String> map) throws Exception {
		String teamId=map.get("teamId");//球队id
		String formation=map.get("formation");//阵型编制 3-2-1
		String remark=map.get("remark");//阵型自定义名
		String formationNum=map.get("formationNum");//阵型人数
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (teamId != null && !"".equals(teamId)&&formation != null && !"".equals(formation)
				&&remark != null && !"".equals(remark)&&formationNum != null && !"".equals(formationNum)){
			if("1".equals(getFormationRole(map))){
				UTeamFormation utf=this.saveFormation(map, teamId);
				hashMap.clear();
				hashMap.put("formationId", utf.getKeyId());
				hashMap.put("success", " 保存成功！");
				return hashMap;
			}else{
				return WebPublicMehod.returnRet("error", "您没有操作阵型权限！");
			}
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	/**
	 * 新建初始化的阵型
	 * @param map
	 * @param teamId 球队id
	 * @throws Exception
	 */
	private UTeamFormation saveFormation(HashMap<String, String> map,String teamId) throws Exception{
		String uuid=WebPublicMehod.getUUID();
		UTeamFormation utf=new UTeamFormation();
		UTeam utem=new UTeam();
		utem.setTeamId(teamId);
		utf.setKeyId(uuid);
		utf.setFormation(map.get("formation"));
		utf.setRemark(map.get("remark"));
		utf.setCreatedate(new Date());
		utf.setUTeam(utem);
		utf.setUserid(getUserId(map));
		utf.setFormationNum(Integer.parseInt(map.get("formationNum")));
		utf.setDelStatus("0");
		utf.setIsDefault("0");
		utf.setInitStatus("0");
		baseDAO.save(utf);
		this.saveOrUpdateFormationInfo(map, uuid);
		return utf;
	}

	/**
	 * 保存或修改阵容详细信息
	 * @param map
	 * @param keyId 阵型主键id
	 * @throws Exception
	 */
	private void saveOrUpdateFormationInfo(HashMap<String, String> map,String keyId) throws Exception{
		Gson gson = new Gson();
		List<UTeamFormationInfo> fList=gson.fromJson(map.get("fformationList"), new TypeToken<List<UTeamFormationInfo>>() {}.getType()); 
		List<UTeamFormationInfo> xList=gson.fromJson(map.get("xformationList"), new TypeToken<List<UTeamFormationInfo>>() {}.getType()); 
		baseDAO.executeHql("delete from UTeamFormationInfo where UTeamFormation.keyId='"+keyId+"'");//删除原有位置信息
		if(fList!=null){
			this.whileFormationInfo(fList,keyId,"0");//主队入库
		}
		if(xList!=null){
			this.whileFormationInfo(xList,keyId, "1");//客队入库
		}
	}

	/**
	 * 阵型详细信息处理
	 * @param list 阵型详细list
	 * @param formationType 主客队类型 0主队 1客队
	 * @throws Exception
	 */
	private void whileFormationInfo(List<UTeamFormationInfo> list,String keyId,String formationType) throws Exception{
		for(UTeamFormationInfo temp:list){
			UTeamFormation utf=new UTeamFormation();
			utf.setKeyId(keyId);
			if(temp.getPlayerId()!=null&&!"".equals(temp.getPlayerId())){
				UPlayer player=baseDAO.get(UPlayer.class,temp.getPlayerId());
				temp.setPlayer(player);
			}
			temp.setUTeamFormation(utf);
			temp.setCreatedate(new Date());
			temp.setHomeStatus(formationType);
			temp.setKeyId(WebPublicMehod.getUUID());
			
			baseDAO.save(temp);
		}
	}
	
	@Override
	public HashMap<String, Object> updateFormationInfo(HashMap<String, String> map) throws Exception {
		String formationId=map.get("formationId");
		String remark=map.get("remark");
		String imgUrl=map.get("imgUrl");
		String isDefault=map.get("isDefault");
		if (formationId != null && !"".equals(formationId)&&remark != null && !"".equals(remark)
				&&imgUrl != null && !"".equals(imgUrl)&&isDefault != null && !"".equals(isDefault)){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			if("1".equals(getFormationRole(map))){
				UTeamFormation utf=baseDAO.get(UTeamFormation.class,formationId);
				
				if(utf!=null){
					if(isDefault.equals("1")&&utf.getUTeam()!=null){
						baseDAO.executeHql("update UTeamFormation set isDefault='0' where  keyId<>'"+utf.getKeyId()+"' and UTeam.teamId='"+utf.getUTeam().getTeamId()+"'");//置为不是默认
					}
					if(!"1".equals(utf.getDelStatus())){
						utf.setRemark(remark);
						utf.setImgurl(imgUrl);
						utf.setIsDefault(isDefault);
						utf.setInitStatus("1");
						baseDAO.save(utf);
						//baseDAO.getSessionFactory().getCurrentSession().clear();
						this.saveOrUpdateFormationInfo(map, utf.getKeyId());
						hashMap.clear();
						hashMap.put("success", " 保存成功！");
						return hashMap;
					}else{
						return WebPublicMehod.returnRet("error", "当前阵型已删除！");
					}
				
				}else{
					return WebPublicMehod.returnRet("error", "当前阵型不存在！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "您没有操作阵型权限！");
			}
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}



	@Override
	public HashMap<String, Object> delFormationInfo(HashMap<String, String> map) throws Exception {
		String formationId=map.get("formationId");
		if (formationId != null && !"".equals(formationId)){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			if("1".equals(getFormationRole(map))){
				UTeamFormation utf=baseDAO.get(UTeamFormation.class,formationId);
				if(utf!=null){
					if(!"1".equals(utf.getDelStatus())){
						utf.setDelStatus("1");
						baseDAO.save(utf);
						hashMap.clear();
						hashMap.put("success", "删除成功！");
						return hashMap;
					}else{
						return WebPublicMehod.returnRet("error", "当前阵型已删除！");
					}
				}else{
					return WebPublicMehod.returnRet("error", "当前阵型不存在！");
				}
			}else{
				return WebPublicMehod.returnRet("error", "您没有操作阵型权限！");
			}
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}



	@Override
	public HashMap<String, Object> selectFormationPlayers(HashMap<String, String> map) throws Exception {
		HashMap<String,String> posMap=null;
		String teamId=map.get("teamId");
		if (teamId != null && !"".equals(teamId)){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> reList=new ArrayList<HashMap<String, Object>>();
			List<UPlayer> players=baseDAO.find(map,"from UPlayer where UTeam.teamId=:teamId and in_team='1' order by adddate,addtime ");
			posMap=Public_Cache.HASH_PARAMS("position");
			for(UPlayer temp:players){
				HashMap<String, Object> utfMap=new HashMap<String, Object>();
				utfMap.put("playerId", temp.getPlayerId());
				if(temp.getUUser()!=null){
					utfMap.put("playerNickName", temp.getUUser().getNickname());
					map.put("thisUserId", temp.getUUser().getUserId());
					UUserImg img=baseDAO.get(map,"from UUserImg where UUser.userId=:thisUserId and uimgUsingType='1' and imgSizeType='1' ");
					if(img!=null){
						utfMap.put("playerImg", img.getImgurl());//球员头像
					}else{
						utfMap.put("playerImg", "");
					}
					utfMap.put("playerRealName", temp.getUUser().getRealname());
				}else{
					utfMap.put("playerNickName", "");
					utfMap.put("playerImg", "");
					utfMap.put("playerRealName", "");
				}
				
				utfMap.put("playerNum", temp.getNumber());
				utfMap.put("playerPosition", posMap.get(temp.getPosition()));
				reList.add(utfMap);
			}
			hashMap.clear();
			hashMap.put("playerList", reList);
			return hashMap;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

}
