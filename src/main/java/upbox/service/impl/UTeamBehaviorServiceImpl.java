package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

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
import upbox.model.UTeamBehavior;
import upbox.model.UTeamImg;
import upbox.model.UUserImg;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.URegionService;
import upbox.service.UTeamBehaviorService;
import upbox.service.UTeamImgService;
import upbox.service.UUserImgService;
/**
 * 队伍首次行为记录接口
 * @author mercideng
 *
 */
@Service("uTeamBehaviorService")
public class UTeamBehaviorServiceImpl implements UTeamBehaviorService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private PublicService publicService;
	@Resource
	private UParameterService uParameterService;
	@Resource
	private UTeamImgService uteamImgService;
	@Resource
	private UUserImgService uUserImgService;
	@Resource
	private URegionService 	uRegionService;
	
	/**
	 * 
	 * 
	   TODO - 根据球队和事件类型，查询该球队的事件类型是否存在 【2.0.0】
	   @param map
	   		teamId			球队Id
	   		userFollowType	
	   			事件类型  
	   				1-球队建立时间 
	   				2-球队加入upbox时间 
	   				3-首次发起约战 
	   				4-首次成功响应约战 
	   				5-首次响应约战 
	   				6-首次约战成功 
	   				7-首次成为擂主 
	   				8-首次攻擂成功 
	   				9-首次守擂成功			
	   @return
	   		uTeamBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UTeamBehavior getuTeamBehavior(HashMap<String, String> map) throws Exception {
		UTeamBehavior uTeamBehavior = baseDAO.get(map, "from UTeamBehavior where uTeam.teamId=:teamId and teamBehaviorType=:behaviorType");
		return uTeamBehavior;
	}

	/**
	 * 
	 * 
	   TODO - 根据球队获取该球队所有里程碑的数据 【2.0.0】
	   @param map
	   		teamId			球队Id
	   @return
	   		uTeamBehaviors  集合
	   @throws Exception
	   2016年5月5日
	   dengqiuru
	 */
	@Override
	public List<UTeamBehavior> getuTeamBehaviors(HashMap<String, String> map) throws Exception {
		List<UTeamBehavior> uTeamBehaviors = baseDAO.find(map, "from UTeamBehavior where uTeam.teamId=:teamId order by createDate desc,createtime desc,teamBehaviorType+0  desc ");
		return uTeamBehaviors;
	}

	/**
	 * 
	 * 
	   TODO - 判断事件为空时，将事件存入数据库 【2.0.0】
	   @param map
	   		behaviorType 
	   			事件类型  
	   				1-球队建立时间 
	   				2-球队加入upbox时间 
	   				3-首次发起约战 
	   				4-首次成功响应约战 
	   				5-首次响应约战 
	   				6-首次约战成功 
	   				7-首次成为擂主 
	   				8-首次攻擂成功 
	   				9-首次守擂成功		
	   		objectId	对应事件类型的Id
	   @param uTeam
	   @return
	   		uTeamBehavior  对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UTeamBehavior insertuTeamBehavior(HashMap<String, String> map, UTeam uTeam) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//获取码表icon对应的url
		UTeamBehavior uTeamBehavior = new UTeamBehavior();
		//判断是否有事件类型
		if (publicService.StringUtil(map.get("behaviorType"))) {
			//判断是否事件类型有对应的Id
			if (publicService.StringUtil(map.get("objectId"))) {
				uTeamBehavior.setKeyId(WebPublicMehod.getUUID());
				uTeamBehavior.setTeamBehaviorType(map.get("behaviorType"));
				uTeamBehavior.setuTeam(uTeam);
				uTeamBehavior.setObjectId(map.get("objectId"));
				//objectType   简介，如建立球队：对应队徽
				if (publicService.StringUtil(map.get("objectType"))) {
					uTeamBehavior.setObjectType(map.get("objectType"));
				}else{
					hashMap.put("name", "team_behavior_type");
					hashMap.put("params", map.get("behaviorType"));
					UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
					if (null != uParameterInfo) {
						if (publicService.StringUtil(uParameterInfo.getImgurl())) {
							uTeamBehavior.setObjectType(uParameterInfo.getImgurl());
						}
					}
				}
				//时间   例如：成立时间
				if (publicService.StringUtil(map.get("createdate"))) {
					Date createDate = PublicMethod.getStringToDate(map.get("createdate"), "yyyy-MM-dd");
					uTeamBehavior.setCreateDate(createDate);
				}else{
					uTeamBehavior.setCreateDate(new Date());
				}
				uTeamBehavior.setCreatetime(new Date());
				baseDAO.save(uTeamBehavior);
			}
		}
		return uTeamBehavior;
	}
	/**
	 * 
	 * 
	   TODO - 成立日期变化时，更新数据库 【2.0.0】
	   @param uTeamBehavior
	   		uTeamBehavior对象
	   @param map
	 * 		createDate 事件时间  不是当前时间  例如：球队成立时间
	   @param uTeam
	   		uTeam  对象
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	@Override
	public void updateDate(UTeamBehavior uTeamBehavior, HashMap<String, String> map,UTeam uTeam) throws Exception {
		HashMap<String, String> hashMap = new HashMap<>();//获取码表icon对应的url
		Date createdate = PublicMethod.getStringToDate(map.get("createdate"), "yyyy-MM-dd");
		uTeamBehavior.setCreateDate(createdate);
		uTeamBehavior.setCreatetime(new Date());
		uTeamBehavior.setuTeam(uTeam);
		//objectType   简介，如建立球队：对应队徽
		if (publicService.StringUtil(map.get("objectType"))) {
			uTeamBehavior.setObjectType(map.get("objectType"));
		}else{
			hashMap.put("name", "team_behavior_type");
			hashMap.put("params", map.get("behaviorType"));
			UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(hashMap);
			if (null != uParameterInfo) {
				if (publicService.StringUtil(uParameterInfo.getImgurl())) {
					uTeamBehavior.setObjectType(uParameterInfo.getImgurl());
				}
			}
		}
		baseDAO.update(uTeamBehavior);
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
	public String getUTeamBehaviorJsonStr(HashMap<String, Object> hashMap2) throws Exception {
		String jsonstr = "";
		if("1".equals(hashMap2.get("teamBehaviorType"))){//首次建队
			UTeam uTeam2 = baseDAO.get(UTeam.class,hashMap2.get("objectId").toString());
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			if(null==uTeam2.getUUser()||((null==uTeam2.getUUser().getRealname()||"".equals(uTeam2.getUUser().getRealname()))&&(null==uTeam2.getUUser().getNickname()||"".equals(uTeam2.getUUser().getNickname())))){
				jsonstr = "";
			}else{
				hashMap.put("userId", uTeam2.getUUser().getUserId());
				hashMap.put("left","由");
				UUserImg userImg = uUserImgService.getHeadPicNotSetByuserId(hashMap);
				if(userImg!=null){
					hashMap.put("img",userImg.getImgurl());
				}else{
					hashMap.put("img","");
				}
				//用户名为空，取真实姓名
				if(null!=uTeam2.getUUser().getRealname()&&!"".equals(uTeam2.getUUser().getRealname())){
					hashMap.put("name",uTeam2.getUUser().getRealname());
				}else{
					hashMap.put("name",uTeam2.getUUser().getNickname());
				}
				if(null!=uTeam2.getuRegion()&&!"".equals(uTeam2.getuRegion().get_id())){
					HashMap<String, Object> hashMap3 = new HashMap<String, Object>();
					hashMap3.put("area", uTeam2.getuRegion().get_id());
					hashMap.put("right","组织成立于"+uRegionService.getURegionInfoByArea(hashMap3));
				}else{
					hashMap.put("right","组织成立");
				}
			}
//			if(null!=uTeam2.getUUser()){
//				hashMap.put("userId", uTeam2.getUUser().getUserId());
//				hashMap.put("left","由");
//				UUserImg userImg = uUserImgService.getHeadPicNotSetByuserId(hashMap);
//				if(userImg!=null){
//					hashMap.put("img",userImg.getImgurl());
//				}else{
//					hashMap.put("img","");
//				}
//				//用户名为空，取真实姓名
//				if(null!=uTeam2.getUUser().getRealname()&&!"".equals(uTeam2.getUUser().getRealname())){
//					hashMap.put("name",uTeam2.getUUser().getRealname());
//				}else{
//					hashMap.put("name",uTeam2.getUUser().getNickname());
//				}
//				
//				if(null!=uTeam2.getuRegion()&&!"".equals(uTeam2.getuRegion().getName())){
//					hashMap.put("right","组织成立于"+uTeam2.getuRegion().getName());
//				}else{
//					hashMap.put("right","组织成立");
//				}
//			}
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
			
		}else if("14".equals(hashMap2.get("teamBehaviorType"))){//首次达成约战
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			//发起方
			UDuel uDuel = baseDAO.get(UDuel.class,hashMap2.get("objectId").toString());
			//响应方
//			UDuelResp uResp = baseDAO.get(UDuelResp.class, hashMap2.get("objectId").toString());
			hashMap.put("objectId", hashMap2.get("objectId").toString());
			UDuelResp uResp = baseDAO.get(hashMap,"from UDuelResp where UDuel.duelId = :objectId ");
			hashMap.remove("objectId");
			
			if(null!=uDuel&&null!=uResp&&null!=uDuel.getUTeam()&&null!=uResp.getUTeam()){
//				if(null!=uDuel.getUTeam()){
				//发起
					hashMap.put("fname",uDuel.getUTeam().getName());
					hashMap.put("teamId",uDuel.getUTeam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				//响应
					hashMap.put("xname",uResp.getUTeam().getName());
					hashMap.put("teamId",uResp.getUTeam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					hashMap.put("center"," VS ");
					hashMap.put("details","");
			}
//				}else{
//					hashMap.put("fname","");
//					hashMap.put("fimg","");
//				}
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//			}
			
			
//			if(uResp!=null){
//				if(null!=uResp.getUTeam()){
//					
//				}else{
//					hashMap.put("xname","");
//					hashMap.put("ximg","");
//				}
//			}else{
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//			}
			
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
			
		}else if("7".equals(hashMap2.get("teamBehaviorType"))){//首次约战胜利
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			//根据比赛id 获取具体获胜的场次
			UDuelBs uBs = baseDAO.get(UDuelBs.class, hashMap2.get("objectId").toString());
			if(null!=uBs&&null!=uBs.getUTeam()&&null!=uBs.getXUTeam()){
				hashMap.put("center"," VS ");
				//发起方
//				if(null!=uBs.getUTeam()){
					hashMap.put("fname",uBs.getUTeam().getName());
					hashMap.put("teamId",uBs.getUTeam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
//				}else{
//					hashMap.put("fname","");
//					hashMap.put("fimg","");
//					hashMap.put("center","");
//				}
				
				//响应方
//				if(null!=uBs.getXUTeam()){
					hashMap.put("xname",uBs.getXUTeam().getName());
					hashMap.put("teamId",uBs.getXUTeam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
//				}else{
//					hashMap.put("xname","");
//					hashMap.put("ximg","");
//				}
				if(null!=uBs.getFgoal()&&null!=uBs.getKgoal()){
					hashMap.put("center"," "+uBs.getFgoal()+":"+uBs.getKgoal()+" ");
				}
				hashMap.put("details","");
			}
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//				hashMap.put("center","");
//				hashMap.put("details","");
//			}
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
			
		}else if("15".equals(hashMap2.get("teamBehaviorType"))){//首次达成挑战
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			UChallenge uBs = baseDAO.get(UChallenge.class, hashMap2.get("objectId").toString());
			if(uBs!=null&&null!=uBs.getFteam()&&null!=uBs.getXteam()){
				//发起方
//				if(null!=uBs.getFteam()){
					hashMap.put("fname",uBs.getFteam().getName());
					hashMap.put("teamId",uBs.getFteam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
//				}else{
//					hashMap.put("fname","");
//					hashMap.put("fimg","");
//					hashMap.put("center","");
//				}
				
				//响应方
//				if(null!=uBs.getXteam()){
					hashMap.put("xname",uBs.getXteam().getName());
					hashMap.put("teamId",uBs.getXteam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
					hashMap.put("center"," VS ");
					hashMap.put("details","");
			}
//				}else{
//					hashMap.put("xname","");
//					hashMap.put("ximg","");
//				}
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//				hashMap.put("center","");
//			}
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
			
		}else if("16".equals(hashMap2.get("teamBehaviorType"))){//首次挑战胜利
			HashMap<String, String> hashMap = new HashMap<String, String>();
			
			//挑战大场次
			UChallenge uBs = baseDAO.get(UChallenge.class,hashMap2.get("objectId").toString());
			if(uBs!=null&&null!=uBs.getFteam()&&null!=uBs.getXteam()){
				//发起方
				if(null!=uBs.getFteam()){
					hashMap.put("fname",uBs.getFteam().getName());
					hashMap.put("teamId",uBs.getFteam().getTeamId());
					UTeamImg uImgf = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgf!=null){
						hashMap.put("fimg",uImgf.getImgurl());
					}else{
						hashMap.put("fimg","");
					}
				}else{
					hashMap.put("fname","");
					hashMap.put("fimg","");
				}
				
				//响应方
				if(null!=uBs.getXteam()){
					hashMap.put("xname",uBs.getXteam().getName());
					hashMap.put("teamId",uBs.getXteam().getTeamId());
					UTeamImg uImgx = uteamImgService.getHeadPicNotSetByTeamId(hashMap);
					if(uImgx!=null){
						hashMap.put("ximg",uImgx.getImgurl());
					}else{
						hashMap.put("ximg","");
					}
				}else{
					hashMap.put("xname","");
					hashMap.put("ximg","");
				}
				
				//判断是否有多场比赛
				hashMap.put("challengeId",hashMap2.get("objectId").toString());
				List<UChallengeBs> uBsLsit = baseDAO.find(hashMap," from UChallengeBs where UChallenge.challengeId = :challengeId ");
				if(uBsLsit.size()>1){
					hashMap.put("center"," VS ");
					String details = "";
//					HashMap<String, String> hashMap3 = new HashMap<String, String>();
					//分别显示每场比分
					for(int i=0;i<uBsLsit.size();i++){
						details = details + "第"+(i+1)+"场 "+uBsLsit.get(i).getFqGoal()+" : "+uBsLsit.get(i).getXyGoal()+"mmmm";
						
//						details = JSON.toJSONString(hashMap3);
					}
//					System.out.println("details="+details);
					hashMap.put("details",details);
				}else if(uBsLsit.size()==1){
					//1.一场比赛
					hashMap.put("center"," "+uBsLsit.get(0).getFqGoal()+":"+uBsLsit.get(0).getXyGoal()+" ");
					hashMap.put("details","");
				}else{
					//没有比赛场次
					hashMap.put("center"," VS ");
					hashMap.put("details","");
				}
			}	
//			}else{
//				hashMap.put("fname","");
//				hashMap.put("fimg","");
//				hashMap.put("xname","");
//				hashMap.put("ximg","");
//				hashMap.put("center","");
//			}
			
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
			
		}else if(hashMap2.get("teamBehaviorType").equals("11")){//赛事
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("behaviorId", hashMap2.get("keyId").toString());
			UBehaviorInfo uInfo = baseDAO.get(hashMap,"from UBehaviorInfo where behaviorId = :behaviorId ");
			hashMap.remove("behaviorId");
			if(null!=uInfo){
				if("1".equals(uInfo.getEventsType())){
					hashMap.put("details", "报名参赛");
				}else{
//					Public_Cache.HASH_PARAMS("events_type").get(uInfo.getEventsType());
//					if(null!=Public_Cache.HASH_PARAMS("events_type").get(uInfo.getEventsType())&&!"".equals(Public_Cache.HASH_PARAMS("events_type").get(uInfo.getEventsType()))&&null!=uInfo.getScore()&&null!=uInfo.getWcount()&&null!=uInfo.getPcount()&&null!=uInfo.getScount()&&null!=uInfo.getGintegral()&&null!=uInfo.getSintegral()){
						//里程碑明细
						String eventsName = Public_Cache.HASH_PARAMS("events_type").get(uInfo.getEventsType());
						//赛事明细
						String score = "0";
						Integer wcount = 0;
						Integer pcount = 0;
						Integer scount = 0;
						Integer gintegral = 0;
						Integer sintegral = 0;
						//总得分
						if (publicService.StringUtil(uInfo.getScore())) {
							score = uInfo.getScore();
						}
						//胜
						if (null != uInfo.getWcount() && 0	!= uInfo.getWcount()) {
							wcount = uInfo.getWcount();
						}
						//平
						if (null != uInfo.getPcount() && 0	!= uInfo.getPcount()) {
							pcount = uInfo.getPcount();
						}
						//败
						if (null != uInfo.getScount() && 0	!= uInfo.getScount()) {
							scount = uInfo.getScount();
						}
						//得
						if (null != uInfo.getGintegral() && 0	!= uInfo.getGintegral()) {
							gintegral = uInfo.getGintegral();
						}
						//失
						if (null != uInfo.getSintegral() && 0	!= uInfo.getSintegral()) {
							sintegral = uInfo.getSintegral();
						}
//						String details = Public_Cache.HASH_PARAMS("events_type").get(uInfo.getEventsType()) +"  "+uInfo.getScore()+"分 "+uInfo.getWcount()+"胜 "+uInfo.getPcount()+"平 "+uInfo.getScount()+"败 得"+uInfo.getGintegral()+" 失"+uInfo.getSintegral();
						String details = eventsName+ "  "+score+"分 "+wcount+"胜 "+pcount+"平 "+scount+"败 得"+gintegral+" 失"+sintegral;
						hashMap.put("details", details);
//					}
				}
			}
			
			if(hashMap.size()>0){
				jsonstr = JSON.toJSONString(hashMap);
			}
		}
		return jsonstr;
	}
	
	/**
	 * 
	 * 
	   TODO - 战队详情--概况--时间轴 【2.0.0】
	   @param map
	   		page    分页
	   @return
	   		uTeamBehaviorList
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	@Override
	public List<HashMap<String, Object>> getuTeamBehaviorList(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap1 = new HashMap<>();
		List<HashMap<String, Object>> galleryList = new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("teamId"))) {
			hashMap1.put("teamId", map.get("teamId"));
			StringBuffer sql = new StringBuffer("select b.key_id keyId,b.createdate createDate,b.team_behavior_type teamBehaviorType,b.object_type objectType,b.object_id objectId,upi.imgurl imgurl,b.remark remark from u_team_behavior b "
					+" left join u_team t on b.team_id=t.team_id "
					+" LEFT JOIN u_parameter_info upi ON upi.params=b.team_behavior_type "
					+" and upi.pkey_id in (select p.pkey_id from u_parameter p where p.params='team_behavior_type') "
					+" where b.team_id=:teamId  and upi.params_is_show='1'  "
					+" order by b.createdate desc,b.team_behavior_type+0 desc ");
			if (publicService.StringUtil(map.get("roughly"))) {
				if ("1".equals(map.get("roughly"))) {
//					hashMap.put("limit", Public_Cache.BEHAVIORCOUNT_INPLAYER);
					hashMap1.put("limit", 5);
					sql.append(" limit :limit ");
				}
			}
			galleryList = baseDAO.findSQLMap(sql.toString(), hashMap1);
			if (null != galleryList && galleryList.size() > 0) {
				for (HashMap<String, Object> hashMap2 : galleryList) {
					
					//设置对应里程碑名称
					if (null == hashMap2.get("remark") || "".equals(hashMap2.get("remark"))) {
						this.displayData(hashMap2);
					}else{
						hashMap2.put("teamBehaviorTypeName", hashMap2.get("remark"));
					}
					
					//里程碑对应图片
					if ("1".equals(hashMap2.get("teamBehaviorType"))) {
						if (null == hashMap2.get("objectType") || "".equals(hashMap2.get("objectType"))) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}else if ("2".equals(hashMap2.get("teamBehaviorType"))) {
						if (null == hashMap2.get("objectType") || "".equals(hashMap2.get("objectType"))) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}else{
						if (null == hashMap2.get("objectType") || "".equals(hashMap2.get("objectType"))) {
							hashMap2.put("objectType", hashMap2.get("imgurl"));
						}
					}
					//获取里程碑详细字段
					hashMap2.put("jsonStr",this.getUTeamBehaviorJsonStr(hashMap2));
				}
			}
		}
		return galleryList;
	}

	/**
	 * 
	 * 
	   TODO - 设置对应名称
	   @param hashMap
	   		teamBehaviorType  事件类型
	   2016年6月2日
	   dengqiuru
	 */
	private void displayData(HashMap<String, Object> hashMap) {
		//teamBehaviorTypeName:事件类型对应的名称
		if (null != hashMap.get("teamBehaviorType") && !"".equals(hashMap.get("teamBehaviorType")) &&  !"null".equals(hashMap.get("teamBehaviorType"))) {//事件类型名称
			hashMap.put("teamBehaviorTypeName",Public_Cache.HASH_PARAMS("team_behavior_type").get(hashMap.get("teamBehaviorType")));
		}else{
			hashMap.put("teamBehaviorTypeName", null);
		}
	}

}
