package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UBehaviorInfo;
import upbox.model.UChallenge;
import upbox.model.UChallengeBs;
import upbox.model.UDuel;
import upbox.model.UDuelBs;
import upbox.model.UDuelResp;
import upbox.model.UParameterInfo;
import upbox.model.UTeam;
import upbox.model.UTeamImg;
import upbox.model.UUser;
import upbox.model.UUserBehavior;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.URegionService;
import upbox.service.UTeamImgService;
import upbox.service.UUserBehaviorService;
import upbox.service.UWorthService;
/**
 * 用户首次行为记录接口
 * @author mercideng
 *
 */
@Service("uUserBehaviorService")
public class UUserBehaviorServiceImpl implements UUserBehaviorService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private PublicService publicService;
	@Resource
	private UParameterService uParameterService;
	@Resource
	private UWorthService uworthService;
	@Resource
	private UTeamImgService uTeamImgService;
	@Resource
	private URegionService uRegionService;
	/**
	 * 
	 * 
	   TODO - 根据用户和事件类型，查询该用户的事件类型是否存在 【2.0.0】
	   @param map
	   		userId		当前用户Id
	   		userFollowType		
	   				事件类型
	   					1-注册 
	   					2-首次建立球队 
	   					3-首次加入球队 
	   					4-首次发布动态 
	   					5-首次关注球队			
	   @return
	   		UUserBehavior对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UUserBehavior getuUserBehavior(HashMap<String, String> map) throws Exception {
		UUserBehavior uUserBehavior = baseDAO.get(map, "from UUserBehavior where UUser.userId=:userId and user_behavior_type=:behaviorType");
		return uUserBehavior;
	}

	/**
	 * 
	 * 
	   TODO - 获取当前球队所有的里程碑 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年5月5日
	   dengqiuru
	 */
	@Override
	public List<UUserBehavior> getuUserBehaviors(HashMap<String, String> map) throws Exception {
		List<UUserBehavior> uUserBehaviors = baseDAO.find(map, "from UUserBehavior where UUser.userId=:userId order by createDate desc,userBehaviorType+0 desc ");
		return uUserBehaviors;
	}

	/**
	 * 
	 * 
	   TODO - 判断事件为空时，将事件存入数据库 【2.0.0】
	   @param map
	   		behaviorType 	
	   				事件类型
	   					1-注册 
	   					2-首次建立球队 
	   					3-首次加入球队 
	   					4-首次发布动态 
	   					5-首次关注球队
	   		objectId	对应事件类型的Id
	   @param uUser
	   @return
	   		UUserBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UUserBehavior insertuUserBehavior(HashMap<String, String> map,UUser uUserTemp) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//获取码表icon对应的url
		UUserBehavior uUserBehavior = new UUserBehavior();
		if (publicService.StringUtil(map.get("behaviorType"))) {
			if (publicService.StringUtil(map.get("objectId"))) {
				uUserBehavior.setKeyId(WebPublicMehod.getUUID());
				uUserBehavior.setUserBehaviorType(map.get("behaviorType"));
				uUserBehavior.setUUser(uUserTemp);
				uUserBehavior.setObjectId(map.get("objectId"));
				if (publicService.StringUtil(map.get("objectType"))) {
					uUserBehavior.setObjectType(map.get("objectType"));
				}else{//如果为空，取码表icon
					hashMap.put("name", "user_behavior_type");
					hashMap.put("params", map.get("behaviorType"));
					UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
					if (null != uParameterInfo) {
						if (publicService.StringUtil(uParameterInfo.getImgurl())) {
							uUserBehavior.setObjectType(uParameterInfo.getImgurl());
						}
					}
				}
				uUserBehavior.setCreateDate(new Date());
				baseDAO.save(uUserBehavior);
			}
		}
		return uUserBehavior;
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情时间轴 【2.0.0】
	   @param map
	   		page    分页
	   		userId	用户Id
	   @return
	   		uUserBehaviorlist集合
	   @throws Exception
	   2016年3月9日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getuUserBehaviorList(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> galleryList = new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("userId"))) {
			hashMap.put("userId", map.get("userId"));
			StringBuffer sql = new StringBuffer(" select b.key_id keyId,b.createdate createDate,b.user_behavior_type userBehaviorType,b.object_type objectType,b.object_id objectId,upi.imgurl imgurl,b.remark remark from u_user_behavior b "
					+" left join u_user t on b.user_id=t.user_id "
					+" LEFT JOIN u_parameter_info upi ON upi.params=b.user_behavior_type "
					+" and upi.pkey_id in (select p.pkey_id from u_parameter p where p.params='user_behavior_type') "
					+" where  b.user_id=:userId  and upi.params_is_show='1' "
					+" order by b.createdate desc,b.user_behavior_type+0 desc ");
			if (publicService.StringUtil(map.get("roughly"))) {
				if ("1".equals(map.get("roughly"))) {
//					hashMap.put("limit", Public_Cache.BEHAVIORCOUNT_INPLAYER);
					hashMap.put("limit", 5);
					sql.append(" limit :limit ");
				}
			}
			galleryList = baseDAO.findSQLMap(sql.toString(), hashMap);
			if (null != galleryList && galleryList.size() > 0) {
				for (HashMap<String, Object> hashMap2 : galleryList) {
					//填充编号对象名称
					if (null == hashMap2.get("remark")) {
						this.displayData(hashMap2);
					}else{
						hashMap2.put("userBehaviorTypeName", hashMap2.get("remark"));
					}
					//除了标记队徽的都去码表里的url
					if ("3".equals(hashMap2.get("userBehaviorType"))) {
						if (null == hashMap2.get("objectType")) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}else if ("2".equals(hashMap2.get("userBehaviorType"))) {
						if (null == hashMap2.get("objectType")) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}else if("5".equals(hashMap2.get("userBehaviorType"))){
						if (null == hashMap2.get("objectType")) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}else{
						if (null == hashMap2.get("objectType")) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}
				}
			}
		}
		return galleryList;
	}

	/**
	 * 
	 * 
	   TODO - 填充编号对应名称
	   @param hashMap
	   		userBehaviorType   时间类型
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private void displayData(HashMap<String, Object> hashMap) throws Exception {
		if (null != hashMap.get("userBehaviorType") || !"".equals(hashMap.get("userBehaviorType"))) {
			hashMap.put("userBehaviorTypeName",Public_Cache.HASH_PARAMS("user_behavior_type").get(hashMap.get("userBehaviorType")));
		}else{
			hashMap.put("userBehaviorTypeName",null);
		}
	}

	/**
	 * 
	 * 
	   TODO - 将赛事传过来的球队名的分类去掉，并返回
	   @param map
	   		teamName		球队名称
	   @return
	   2016年7月13日
	   dengqiuru
	 * @throws Exception 
	 */
	private String getRealName(UTeam uTeam) throws Exception {
		String teamClassName1 = null;
		if (publicService.StringUtil(uTeam.getTeamClass())) {
			teamClassName1 = Public_Cache.HASH_PARAMS("team_class").get(uTeam.getTeamClass());
		}
		//队名
		String uteamName = uTeam.getName();
		if (null == uteamName || "".equals(uteamName)) {
			uteamName = uTeam.getShortName();
		}
		//获取赛事的球队名
		String teamRealName = uteamName;
		//查询这些球队名是否包含球队分类
		if (publicService.StringUtil(teamRealName)) {
			if (null != teamClassName1) {
				if(teamRealName.contains(teamClassName1)){
					uteamName = teamRealName;
				}else{
					uteamName = teamRealName + teamClassName1;
				}
			}
		}
		return uteamName;
	}
	
	/**
	 * 
	 * @TODO 获取球队里程碑字段详细 [从数据库]
	 * @Title: getUTeamBehaviorJsonStr 
	 * @param hashMap2
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月9日 下午5:42:11
	 */
	@Override
	public String getUTeamBehaviorJsonStr(HashMap<String, Object> hashMap2) throws Exception {
		String jsonstr = null;
		if("2".equals(hashMap2.get("userBehaviorType")) || "3".equals(hashMap2.get("userBehaviorType"))){//2-首次建立球队  3-首次加入球队
			String content = "";
			UTeam uTeam = baseDAO.get(UTeam.class,hashMap2.get("objectId").toString());
			HashMap<String, String> hashMap = new HashMap<String, String>();
			HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
			//队徽
			hashMap.put("teamId", uTeam.getTeamId());
			UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(hashMap);
			if(uImg!=null){
				hashMap.put("fimg",uImg.getImgurl());
			}else{
				hashMap.put("fimg","");
			}
			String holddate = "";
			//队名
			String uteamName =this.getRealName(uTeam);
			content = uteamName;
			//成立日期
			if (null != uTeam.getHoldDate()) {
				holddate = PublicMethod.getDateToString(uTeam.getHoldDate(), "yyyy年MM月dd日");
				content += "  成立于"+holddate;
			}
			//地区
			if (null != uTeam.getuRegion()) {
				hashMap1.put("area", uTeam.getuRegion().get_id());
				String address = uRegionService.getURegionInfoByArea(hashMap1);
				if (publicService.StringUtil(address)) {
					if (StringUtils.isEmpty(holddate)) {
						content += "  成立于"+address;
					}else{
						content += "  "+address;
					}
				}
			}
			hashMap.put("content",content);
			jsonstr = JSON.toJSONString(hashMap);
		}else if("7".equals(hashMap2.get("userBehaviorType")) || "8".equals(hashMap2.get("userBehaviorType"))){//7-首次参与约战  8-首次达成约战  9-首次约战获胜
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			HashMap<String, String> map1 = new HashMap<>();
			//发起方
			if (null != hashMap2.get("objectId")) {
				UDuel uDuel = baseDAO.get(UDuel.class,hashMap2.get("objectId").toString());
				UDuelResp uResp = baseDAO.get(" from UDuelResp where UDuel.duelId=:objectId ", hashMap2);
				if(uDuel!=null && null != uResp && null != uDuel.getUTeam() && null != uResp.getUTeam()){
					//队名(发起方)
					String futeamName = this.getRealName(uDuel.getUTeam());
					hashMap.put("fname",futeamName);
					hashMap.put("fteamId",uDuel.getUTeam().getTeamId());
					map1.put("teamId", uDuel.getUTeam().getTeamId());
					UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
					if(uImg!=null){
						hashMap.put("fimg",uImg.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
					//队名(响应方)
					String xuteamName = this.getRealName(uResp.getUTeam());
					hashMap.put("xname",xuteamName);
					hashMap.put("xteamId",uResp.getUTeam().getTeamId());
					map1.put("teamId", uResp.getUTeam().getTeamId());
					UTeamImg uImg2 = uTeamImgService.getHeadPicNotSetByTeamId(map1);
					if(uImg2!=null){
						hashMap.put("ximg",uImg2.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					hashMap.put("center"," VS ");
					hashMap.put("details","");
					jsonstr = JSON.toJSONString(hashMap);
				}else{
					jsonstr = "";
				}
//				UDuel uDuel = baseDAO.get(UDuel.class,hashMap2.get("objectId").toString());
//				if(uDuel!=null){
//					if (null != uDuel.getUTeam()) {
//						//队名
//						String futeamName = this.getRealName(uDuel.getUTeam());
//						hashMap.put("fname",futeamName);
//						hashMap.put("fteamId",uDuel.getUTeam().getTeamId());
//						map1.put("teamId", uDuel.getUTeam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
//						if(uImg!=null){
//							hashMap.put("fimg",uImg.getImgurl());
//						}else{
//							hashMap.put("fimg","");
//						}
//					}else{
//						hashMap.put("fname","");
//						hashMap.put("fimg","");
//					}
//				}else{
//					hashMap.put("fname","");
//					hashMap.put("fimg","");
//				}
//				hashMap.put("center"," VS ");
//				hashMap.put("details","");
//				//响应方
//				UDuelResp uResp = baseDAO.get(" from UDuelResp where UDuel.duelId=:objectId ", hashMap2);
//				if(uResp!=null){
//					if (null != uResp.getUTeam()) {
//						//队名
//						String xuteamName = this.getRealName(uResp.getUTeam());
//						hashMap.put("xname",xuteamName);
//						hashMap.put("xteamId",uResp.getUTeam().getTeamId());
//						map1.put("teamId", uDuel.getUTeam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
//						if(uImg!=null){
//							hashMap.put("ximg",uImg.getImgurl());
//						}else{
//							hashMap.put("ximg","");
//						}
//					}else{
//						hashMap.put("xname","");
//						hashMap.put("ximg","");
//					}
//				}else{
//					hashMap.put("xname","");
//					hashMap.put("ximg","");
//				}
//				//判断是否有多场比赛
//				hashMap.put("duelId",hashMap2.get("objectId").toString());
//				List<UDuelBs> uBsLsit = baseDAO.find(" from UDuelBs where UDuel.duelId = :duelId ",hashMap);
//				if(uBsLsit.size()>1){
//					hashMap.put("center"," VS ");
//					String details = "";
//					//分别显示每场比分
//					for(int i=0;i<uBsLsit.size();i++){
//						details = details + "第"+(i+1)+"场 "+uBsLsit.get(i).getFgoal()+" : "+uBsLsit.get(i).getKgoal()+"mmmm";
//					}
//					hashMap.put("details",details);
//				}else if(uBsLsit.size()==1){
//					//1.一场比赛
//					hashMap.put("center"," "+uBsLsit.get(0).getFgoal()+":"+uBsLsit.get(0).getKgoal()+" ");
//					hashMap.put("details","");
//				}else{
//					//没有比赛场次
//					hashMap.put("center"," VS ");
//					hashMap.put("details","");
//				}
			}else{
				jsonstr = "";
			}
		}else if( "9".equals(hashMap2.get("userBehaviorType"))){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			HashMap<String, String> map1 = new HashMap<>();
			//发起方
			if (null != hashMap2.get("objectId")) {
				//判断是否有多场比赛
				UDuelBs uDuelBs = baseDAO.get(" from UDuelBs where bsId = :objectId ",hashMap2);
				if (null != uDuelBs && null != uDuelBs.getUTeam() && null != uDuelBs.getXUTeam()) {
					//队名(发起方)
					String futeamName = this.getRealName(uDuelBs.getUTeam());
					hashMap.put("fname",futeamName);
					hashMap.put("fteamId",uDuelBs.getUTeam().getTeamId());
					map1.put("teamId", uDuelBs.getUTeam().getTeamId());
					UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
					if(uImg!=null){
						hashMap.put("fimg",uImg.getImgurl());
					}else{
						hashMap.put("fimg","");
					}

					//队名(响应方)
					String xuteamName = this.getRealName(uDuelBs.getXUTeam());
					hashMap.put("xname",xuteamName);
					hashMap.put("xteamId",uDuelBs.getXUTeam().getTeamId());
					map1.put("teamId", uDuelBs.getXUTeam().getTeamId());
					UTeamImg uImg2 = uTeamImgService.getHeadPicNotSetByTeamId(map1);
					if(uImg2!=null){
						hashMap.put("ximg",uImg2.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					String fGoal = "0";
					String kGoal = "0";
					if (publicService.StringUtil(uDuelBs.getFgoal())) {
						fGoal = uDuelBs.getFgoal();
					}
					if (publicService.StringUtil(uDuelBs.getKgoal())) {
						kGoal = uDuelBs.getKgoal();
					}
					//1.一场比赛
					hashMap.put("center"," "+fGoal+":"+kGoal+" ");
					hashMap.put("details","");
					jsonstr = JSON.toJSONString(hashMap);
				}else{
					jsonstr = "";
				}
				
//				//判断是否有多场比赛
//				UDuelBs uDuelBs = baseDAO.get(" from UDuelBs where bsId = :objectId ",hashMap2);
//				if (null != uDuelBs) {
//					if (null != uDuelBs.getUTeam()) {
//						//队名
//						String futeamName = this.getRealName(uDuelBs.getUTeam());
//						hashMap.put("fname",futeamName);
//						hashMap.put("fteamId",uDuelBs.getUTeam().getTeamId());
//						map1.put("teamId", uDuelBs.getUTeam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
//						if(uImg!=null){
//							hashMap.put("fimg",uImg.getImgurl());
//						}else{
//							hashMap.put("fimg","");
//						}
//					}else{
//						hashMap.put("fname","");
//						hashMap.put("fimg","");
//					}
//					if (null != uDuelBs.getXUTeam()) {
//						//队名
//						String xuteamName = this.getRealName(uDuelBs.getXUTeam());
//						hashMap.put("xname",xuteamName);
//						hashMap.put("xteamId",uDuelBs.getXUTeam().getTeamId());
//						map1.put("teamId", uDuelBs.getXUTeam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map1);
//						if(uImg!=null){
//							hashMap.put("ximg",uImg.getImgurl());
//						}else{
//							hashMap.put("ximg","");
//						}
//					}else{
//						hashMap.put("xname","");
//						hashMap.put("ximg","");
//					}
//					String fGoal = "0";
//					String kGoal = "0";
//					if (publicService.StringUtil(uDuelBs.getFgoal())) {
//						fGoal = uDuelBs.getFgoal();
//					}
//					if (publicService.StringUtil(uDuelBs.getKgoal())) {
//						kGoal = uDuelBs.getKgoal();
//					}
//					//1.一场比赛
//					hashMap.put("center"," "+fGoal+":"+kGoal+" ");
//					hashMap.put("details","");
//					jsonstr = JSON.toJSONString(hashMap);
//				}else{
//					jsonstr = "";
//				}
			}else{
				jsonstr = "";
			}
		}else if( "11".equals(hashMap2.get("userBehaviorType")) || "10".equals(hashMap2.get("userBehaviorType"))){//10-首次参与挑战  11-首次达成挑战  12-首次挑战获胜
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			HashMap<String, String> map2 = new HashMap<>();
			if (null != hashMap2.get("objectId")) {
				UChallenge uChallenge = baseDAO.get(UChallenge.class, hashMap2.get("objectId").toString());
				if(uChallenge!=null && null != uChallenge.getFteam() && null != uChallenge.getXteam()){
					//队名
					String futeamName = this.getRealName(uChallenge.getFteam());
					hashMap.put("fname",futeamName);
					hashMap.put("fteamId",uChallenge.getFteam().getTeamId());
					map2.put("teamId", uChallenge.getFteam().getTeamId());
					UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
					if(uImg!=null){
						hashMap.put("fimg",uImg.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
					//队名
					String xuteamName = this.getRealName(uChallenge.getXteam());
					hashMap.put("xname",xuteamName);
					hashMap.put("xteamId",uChallenge.getXteam().getTeamId());
					map2.put("teamId", uChallenge.getXteam().getTeamId());
					UTeamImg uImg2 = uTeamImgService.getHeadPicNotSetByTeamId(map2);
					if(uImg2!=null){
						hashMap.put("ximg",uImg2.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					hashMap.put("center"," VS ");
					hashMap.put("details","");
					jsonstr = JSON.toJSONString(hashMap);
				}else{
					jsonstr = "";
				}
				
				
				
				//发起方
//				UChallenge uChallenge = baseDAO.get(UChallenge.class, hashMap2.get("objectId").toString());
//				if(uChallenge!=null){
//					//主队
//					if (null != uChallenge.getFteam()) {
//						//队名
//						String futeamName = this.getRealName(uChallenge.getFteam());
//						hashMap.put("fname",futeamName);
//						hashMap.put("fteamId",uChallenge.getFteam().getTeamId());
//						map2.put("teamId", uChallenge.getFteam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
//						if(uImg!=null){
//							hashMap.put("fimg",uImg.getImgurl());
//						}else{
//							hashMap.put("fimg","");
//						}
//					}else{
//						hashMap.put("fname","");
//						hashMap.put("fimg","");
//					}
//					hashMap.put("center"," VS ");
//					hashMap.put("details","");
//					//客队
//					if (null != uChallenge.getXteam()) {
//						//队名
//						String xuteamName = this.getRealName(uChallenge.getXteam());
//						hashMap.put("xname",xuteamName);
//						hashMap.put("xteamId",uChallenge.getXteam().getTeamId());
//						map2.put("teamId", uChallenge.getXteam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
//						if(uImg!=null){
//							hashMap.put("ximg",uImg.getImgurl());
//						}else{
//							hashMap.put("ximg","");
//						}
//					}else{
//						hashMap.put("xname","");
//						hashMap.put("ximg","");
//					}
//////					//判断是否有多场比赛
////					hashMap.put("challengeId",hashMap2.get("objectId").toString());
////					List<UChallengeBs> uChallengeBsList = baseDAO.find(" from UChallengeBs where UChallenge.challengeId = :challengeId ",hashMap);
////					if(uChallengeBsList.size()>1){
////						hashMap.put("center"," VS ");
////						String details = "";
////						//分别显示每场比分
////						for(int i=0;i<uChallengeBsList.size();i++){
////							details = details + "第"+(i+1)+"场 "+uChallengeBsList.get(i).getFqGoal()+" : "+uChallengeBsList.get(i).getXyGoal()+"mmmm";
////						}
////						hashMap.put("details",details);
////					}else if(uChallengeBsList.size()==1){
////						//1.一场比赛
////						hashMap.put("center"," "+uChallengeBsList.get(0).getFqGoal()+":"+uChallengeBsList.get(0).getXyGoal()+" ");
////						hashMap.put("details","");
////					}else{
////						//没有比赛场次
////						hashMap.put("center"," VS ");
////						hashMap.put("details","");
////					}
////					jsonstr = JSON.toJSONString(hashMap);
//				}else{
//					jsonstr = "";
//				}
			}else{
				jsonstr = "";
			}
		}else if("12".equals(hashMap2.get("userBehaviorType"))){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			HashMap<String, String> map2 = new HashMap<>();
			if (null != hashMap2.get("objectId")) {
				//发起方
				UChallenge uChallenge = baseDAO.get(UChallenge.class, hashMap2.get("objectId").toString());
				if(uChallenge!=null && null != uChallenge.getFteam() && null != uChallenge.getXteam()){
					//队名
					String futeamName = this.getRealName(uChallenge.getFteam());
					hashMap.put("fname",futeamName);
					hashMap.put("fteamId",uChallenge.getFteam().getTeamId());
					map2.put("teamId", uChallenge.getFteam().getTeamId());
					UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
					if(uImg!=null){
						hashMap.put("fimg",uImg.getImgurl());
					}else{
						hashMap.put("fimg","");
					}

					//队名
					String xuteamName = this.getRealName(uChallenge.getXteam());
					hashMap.put("xname",xuteamName);
					hashMap.put("xteamId",uChallenge.getXteam().getTeamId());
					map2.put("teamId", uChallenge.getXteam().getTeamId());
					UTeamImg uImg2 = uTeamImgService.getHeadPicNotSetByTeamId(map2);
					if(uImg2!=null){
						hashMap.put("ximg",uImg2.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					
					//判断是否有多场比赛
					hashMap.put("challengeId",hashMap2.get("objectId").toString());
					List<UChallengeBs> uChallengeBsList = baseDAO.find(" from UChallengeBs where UChallenge.challengeId = :challengeId ",hashMap);
					if(uChallengeBsList.size()>1){
						hashMap.put("center"," VS ");
						String details = "";
						//分别显示每场比分
						for(int i=0;i<uChallengeBsList.size();i++){
							details = details + "第"+(i+1)+"场 "+uChallengeBsList.get(i).getFqGoal()+" : "+uChallengeBsList.get(i).getXyGoal()+"mmmm";
						}
						hashMap.put("details",details);
					}else if(uChallengeBsList.size()==1){
						//1.一场比赛
						hashMap.put("center"," "+uChallengeBsList.get(0).getFqGoal()+":"+uChallengeBsList.get(0).getXyGoal()+" ");
						hashMap.put("details","");
					}else{
						//没有比赛场次
						hashMap.put("center"," VS ");
						hashMap.put("details","");
					}
					jsonstr = JSON.toJSONString(hashMap);
				}else{
					jsonstr = "";
				}
//				
//				//发起方
//				UChallenge uChallenge = baseDAO.get(UChallenge.class, hashMap2.get("objectId").toString());
//				if(uChallenge!=null){
//					//主队
//					if (null != uChallenge.getFteam()) {
//						//队名
//						String futeamName = this.getRealName(uChallenge.getFteam());
//						hashMap.put("fname",futeamName);
//						hashMap.put("fteamId",uChallenge.getFteam().getTeamId());
//						map2.put("teamId", uChallenge.getFteam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
//						if(uImg!=null){
//							hashMap.put("fimg",uImg.getImgurl());
//						}else{
//							hashMap.put("fimg","");
//						}
//					}else{
//						hashMap.put("fname","");
//						hashMap.put("fimg","");
//					}
//					hashMap.put("center"," VS ");
//					hashMap.put("details","");
//					//客队
//					if (null != uChallenge.getXteam()) {
//						//队名
//						String xuteamName = this.getRealName(uChallenge.getXteam());
//						hashMap.put("xname",xuteamName);
//						hashMap.put("xteamId",uChallenge.getXteam().getTeamId());
//						map2.put("teamId", uChallenge.getXteam().getTeamId());
//						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map2);
//						if(uImg!=null){
//							hashMap.put("ximg",uImg.getImgurl());
//						}else{
//							hashMap.put("ximg","");
//						}
//					}else{
//						hashMap.put("xname","");
//						hashMap.put("ximg","");
//					}
////					//判断是否有多场比赛
//					hashMap.put("challengeId",hashMap2.get("objectId").toString());
//					List<UChallengeBs> uChallengeBsList = baseDAO.find(" from UChallengeBs where UChallenge.challengeId = :challengeId ",hashMap);
//					if(uChallengeBsList.size()>1){
//						hashMap.put("center"," VS ");
//						String details = "";
//						//分别显示每场比分
//						for(int i=0;i<uChallengeBsList.size();i++){
//							details = details + "第"+(i+1)+"场 "+uChallengeBsList.get(i).getFqGoal()+" : "+uChallengeBsList.get(i).getXyGoal()+"mmmm";
//						}
//						hashMap.put("details",details);
//					}else if(uChallengeBsList.size()==1){
//						//1.一场比赛
//						hashMap.put("center"," "+uChallengeBsList.get(0).getFqGoal()+":"+uChallengeBsList.get(0).getXyGoal()+" ");
//						hashMap.put("details","");
//					}else{
//						//没有比赛场次
//						hashMap.put("center"," VS ");
//						hashMap.put("details","");
//					}
//					jsonstr = JSON.toJSONString(hashMap);
//				}else{
//					jsonstr = "";
//				}
			}else{
				jsonstr = "";
			}
		}else if("13".equals(hashMap2.get("userBehaviorType"))){//13-参加赛事   
			HashMap<String, String> hashMap = new HashMap<String, String>();
			HashMap<String, String> map3 = new HashMap<>();
			//keyId
			UBehaviorInfo uBehaviorInfo = baseDAO.get(" from UBehaviorInfo where behaviorId=:keyId ", hashMap2);
			if (null != uBehaviorInfo) {
				String infoHead = "";
				String uteamImg = "";
				String uteamName = "";
				String eventsName = "";
				String infoEnd = "";
				//报名参赛
				String eventsType = uBehaviorInfo.getEventsType();
				if ("1".equals(uBehaviorInfo.getEventsType())) {
					infoHead = "随";
					//队徽、队名
					if (publicService.StringUtil(uBehaviorInfo.getTeamId())) {
						map3.put("teamId",uBehaviorInfo.getTeamId());
						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map3);
						if(uImg!=null){
							uteamImg = uImg.getImgurl();
						}
						//队名
						UTeam uTeam = baseDAO.get(map3,"from UTeam where teamId=:teamId");
						if (null != uTeam) {
							uteamName = this.getRealName(uTeam);
						}
					}
					//里程碑明细
					eventsName = Public_Cache.HASH_PARAMS("events_type").get(uBehaviorInfo.getEventsType());
				}else{// 2-冠军 3-亚军 4-季军
					//队徽、队名
					if (publicService.StringUtil(uBehaviorInfo.getTeamId())) {
						map3.put("teamId",uBehaviorInfo.getTeamId());
						UTeamImg uImg = uTeamImgService.getHeadPicNotSetByTeamId(map3);
						if(uImg!=null){
							uteamImg = uImg.getImgurl();
						}
						//队名
						UTeam uTeam = baseDAO.get(map3,"from UTeam where teamId=:teamId");
						if (null != uTeam) {
							uteamName = this.getRealName(uTeam);
						}
					}
					//里程碑明细
					eventsName = Public_Cache.HASH_PARAMS("events_type").get(uBehaviorInfo.getEventsType());
					//赛事明细
					String score = "0";
					Integer wcount = 0;
					Integer pcount = 0;
					Integer scount = 0;
					Integer gintegral = 0;
					Integer sintegral = 0;
					//总得分
					if (publicService.StringUtil(uBehaviorInfo.getScore())) {
						score = uBehaviorInfo.getScore();
					}
					//胜
					if (null != uBehaviorInfo.getWcount() && 0	!= uBehaviorInfo.getWcount()) {
						wcount = uBehaviorInfo.getWcount();
					}
					//平
					if (null != uBehaviorInfo.getPcount() && 0	!= uBehaviorInfo.getPcount()) {
						pcount = uBehaviorInfo.getPcount();
					}
					//败
					if (null != uBehaviorInfo.getScount() && 0	!= uBehaviorInfo.getScount()) {
						scount = uBehaviorInfo.getScount();
					}
					//得
					if (null != uBehaviorInfo.getGintegral() && 0	!= uBehaviorInfo.getGintegral()) {
						gintegral = uBehaviorInfo.getGintegral();
					}
					//失
					if (null != uBehaviorInfo.getSintegral() && 0	!= uBehaviorInfo.getSintegral()) {
						sintegral = uBehaviorInfo.getSintegral();
					}
					infoEnd = score + "分  " + wcount + "胜  " + pcount + "平 " + scount + "败  得" + gintegral + " 失" + sintegral;
				}
				hashMap.put("infoHead", infoHead);
				hashMap.put("uteamImg", uteamImg);
				hashMap.put("uteamName", uteamName);
				hashMap.put("eventsName", eventsName);
				hashMap.put("eventsType", eventsType);
				hashMap.put("infoEnd", infoEnd);
				jsonstr = JSON.toJSONString(hashMap);
			}else{
				jsonstr = "";
			}
		}else{//其它
			jsonstr = "";
		}
		return jsonstr;
	}
}
