package upbox.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UMessage;
import upbox.model.UMessageSwitch;
import upbox.model.UPlayer;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.PublicService;
import upbox.service.UUserService;

/**
 * 
 * @TODO 通知service实现类
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午3:22:12 
 * @version 1.0
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private RedisOperDAOImpl redisDao;

	@Resource
	private PublicService publicService;

	@Resource
	private UUserService uuserService;

	@Resource
	private PublicPushService publicPush;
	
	/**
	 * 
	 * @TODO 应用通知：生成一条通知
	 * @Title: addTheMessageByType 
	 * @param map
	 * 		必填参数
	 * 		type 通知类型
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数  
	 * 		params 点击通知跳转所需参数
	 * 		userId 与通知关联的用户id(不一定是当前登录用户)
	 * 		可选参数
	 * 		repetition 是否去重 1 是 0否  
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:29:42
	 */
	@Override
	public boolean addTheMessageByType(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub

		// 新增订单预定通知
		this.pubInsertMes(map);
		// 获取新增的通知对象
		UMessage um = this.pubGetMesByIns(map);
		if (null != um) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @TODO 通用方法 生成通知 -->根据类型查询HQL
	 * @Title: pubMesQueryHqlByType 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:27:43
	 */
	public String pubMesQueryHqlByType(HashMap<String, String> map)
			throws Exception {
		String hql = null;
		if (map.get("type") != null && map.get("userId") != null) {
			hql = " from UMessage where type = "
					+ map.get("type")
					+ " and userId = "
					+ map.get("userId")
					+ " and isShow = '1' order by mesReadStauts desc,createtime desc ";
		}
		return hql;
	}

	/**
	 * 
	 * @TODO 通用方法 新增通知
	 * @Title: pubInsertMes 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:17:44
	 */
	public void pubInsertMes(HashMap<String, String> map) throws Exception {
		if (map.get("type") != null && map.get("mes_type") != null
				&& map.get("contentName") != null && map.get("params") != null
				&& map.get("userId") != null) {
			String type = map.get("type");
			String mesType = map.get("mes_type");
			String contentName = "";
			if(map.get("contentName") != null){
				contentName = map.get("contentName");
			}
			String params = map.get("params");
			String userId = map.get("userId");
			UMessage um = null;
			String hql = null;

			if (null != map.get("repetition")) {
				um = new UMessage();
				um.setKeyId(WebPublicMehod.getUUID());
				um.setContent(Public_Cache.getMessageCon(type, mesType)
						.replace("XXX", contentName));
				um.setCreatetime(new Date());
				um.setUserId(userId);
				um.setParams(params);
				um.setMesReadStauts("1");
				um.setIsShow("1");
				um.setType(type);
				um.setMesType(mesType);
				baseDAO.save(um);

				baseDAO.getSessionFactory().getCurrentSession().flush();
//				// 通知列表 缓存KEY
//				hql = this.pubMesQueryHqlByType(map);
//				// 写入缓存
//				// redisDao.addList(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
//				// "ListPage","Message", map.get("userId"), hql),
//				// um.getKeyId());
//				redisDao.setSortedSet(PublicMethod.getHRedisKey(
//						Public_Cache.PROJECT_NAME, "ListPage", "Message",
//						map.get("userId"), hql), 0, SerializeUtil.serialize(um));
			} else {
				hql = "from UMessage where type = :type and params = :params and mesType = :mes_type and userId = :userId  ";
				um = baseDAO.get(map, hql);
				if (null == um) {
					um = new UMessage();
					um.setKeyId(WebPublicMehod.getUUID());
					um.setContent(Public_Cache.getMessageCon(type, mesType)
							.replace("XXX", contentName));
					um.setCreatetime(new Date());
					um.setUserId(userId);
					um.setParams(params);
					um.setMesReadStauts("1");
					um.setIsShow("1");
					um.setType(type);
					um.setMesType(mesType);
					baseDAO.save(um);

					baseDAO.getSessionFactory().getCurrentSession().flush();
//					// 通知列表 缓存KEY
//					hql = this.pubMesQueryHqlByType(map);
//					// 写入缓存
//					/*
//					 * redisDao.addList(PublicMethod.getHRedisKey(Public_Cache.
//					 * PROJECT_NAME, "ListPage","Message", map.get("userId"),
//					 * hql), um.getKeyId());
//					 */
//					redisDao.setSortedSet(PublicMethod.getHRedisKey(
//							Public_Cache.PROJECT_NAME, "ListPage", "Message",
//							map.get("userId"), hql), 0, SerializeUtil
//							.serialize(um));
				}
			}

		}
	}

	/**
	 * 
	 * @TODO 通用方法 获取通知对象
	 * @Title: pubGetMesByIns 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:18:04
	 */
	public UMessage pubGetMesByIns(HashMap<String, String> map)
			throws Exception {

		UMessage um = null;
		if (map.get("type") != null && map.get("mes_type") != null
				&& map.get("params") != null && map.get("userId") != null) {
			String hql = "from UMessage where type = :type and params = :params and mesType = :mes_type and userId = :userId";

			um = baseDAO.get(map, hql);
		}

		return um;
	}

	/**
	 * 
	 * @TODO 应用通知：生成多条通知
	 * @Title: addMoreMessageByType 
	 * @param map
	 * 		type 通知类型
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数    
	 * 		params 点击通知跳转所需参数
	 * 		teamId 球队id(用于通知队内所有球员)
	 * 		可选参数
	 * 		repetition 是否去重 1 是 0否  
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:30:31
	 */
	@Override
	public boolean addMoreMessageByType(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		List<UPlayer> uPlayerList = new ArrayList<UPlayer>();
		String hql = " from UPlayer where UTeam.teamId = :teamId ";
		if (null != map.get("mes_type")) {
			if (map.get("mes_type").equals("tmDissolve")) {
				hql = hql + "and exitType = '3' ";
			} else {
				hql = hql + "and inTeam = '1' ";
			}
		}

		uPlayerList = baseDAO.find(map, hql);

		if (uPlayerList.size() > 0) {
			for (int i = 0; i < uPlayerList.size(); i++) {
				map.put("userId", uPlayerList.get(i).getUUser().getUserId());
				// 新增通知
				this.pubInsertMes(map);

			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @TODO 个人通知列表 - 通知分类 【球队、订场、激战】
	 * @Title: getMesListByType 
	 * @param map
	 * 		type 通知类型
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:30:52
	 */
	@Override
	public HashMap<String, Object> getMesListByType(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合

		Integer pageNum = map.get("page") == null
				|| Integer.parseInt(map.get("page")) <= 0 ? 1 : Integer
				.parseInt(map.get("page"));
		PageLimit page = new PageLimit(pageNum, 0);

		String hql = " from UMessage where type = :type and userId = :loginUserId and isShow = '1' order by mesReadStauts desc,createtime desc ";

//		// 从缓存里取数据
//		List<UMessage> mesList = baseDAO.findPageHRedis(map, hql,
//				UMessage.class, PublicMethod.getHRedisKey(
//						Public_Cache.PROJECT_NAME, "ListPage", "Message",
//						map.get("loginUserId")), "keyId", page.getLimit(), page
//						.getOffset());
		
		List<UMessage> mesList =  baseDAO.findByPage(map, hql,page.getLimit(), page.getOffset());

		resultMap.put("mesList", mesList);

		return resultMap;
	}

	/**
	 * 
	 * @TODO 根据类型获取未读信息数量
	 * @Title: getUnreadCountByType 
	 * @param map
	 * 		type 通知类型
	 * 		userId 当前用户
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:31:18
	 */
	@Override
	public HashMap<String, Object> getUnreadCountByType(
			HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		int count = 0;
		String hql = " from UMessage where type = :type and  userId = :loginUserId and mesReadStauts = '1' and isShow = '1' ";
		List<UMessage> mesList = baseDAO.find(map, hql);
		count = mesList.size();

		resultMap.put("count", count);

		return resultMap;
	}

	/**
	 * 
	 * @TODO 改变通知状态
	 * @Title: updateMessageStauts 
	 * @param map
	 * 		mesId 通知主键
	 * 		type 通知类型
	 * 		userId 通知关联的用户Id 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:31
	 */
	@Override
	public void updateMessageStauts(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub

		// 通知主键
		if (null != map.get("mesId")) {
			// 通知列表 缓存KEY
//			String hql = this.pubMesQueryHqlByType(map);

			//List<Serializable> keyList = new ArrayList<Serializable>();
			//keyList.add(um.getKeyId());

			UMessage um = baseDAO.get(UMessage.class, map.get("mesId"));
//			redisDao.delSortedSet(PublicMethod.getHRedisKey(
//					Public_Cache.PROJECT_NAME, "ListPage", "Message",
//					um.getUserId(), hql), SerializeUtil.serialize(um));
			um.setMesReadStauts("-1");
			baseDAO.update(um);
//			redisDao.setSortedSet(PublicMethod.getHRedisKey(
//					Public_Cache.PROJECT_NAME, "ListPage", "Message",
//					um.getUserId(), hql), 0, SerializeUtil.serialize(um));

			// 缓存删除 deleteRedis list->key
			// redisDao.removeListValue(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "ListPage","Message",um.getUserId(), hql), keyList);

			// 缓存更新
			// publicService.removeHRedis_HSET(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "Object","Message",um.getUserId()),um.getKeyId());
		}

	}

	/**
	 * 
	 * @TODO 删除通知
	 * @Title: updateMessageIsShow 
	 * @param map
	 * 		mesId 通知主键
	 * 	 	type 通知类型
	 * 		userId 通知关联的用户Id 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:34:15
	 */
	@Override
	public void updateMessageIsShow(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		// 通知主键
		if (null != map.get("mesId")) {
			UMessage um = baseDAO.get(UMessage.class, map.get("mesId"));
			um.setIsShow("2");
			baseDAO.update(um);

			// 通知列表 缓存KEY
//			String hql = this.pubMesQueryHqlByType(map);
//
//			List<Serializable> keyList = new ArrayList<Serializable>();
//			keyList.add(um.getKeyId());

			// 缓存删除 deleteRedis list->key
			// redisDao.removeListValue(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "ListPage","Message",um.getUserId(), hql), keyList);
			// 删除查询详情里已经变更的缓存
			// 缓存删除 deleteRedis Object
			// publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "Object","Message",um.getUserId(), um.getKeyId()));
			// redisDao.removeListValue(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "Object","Message",um.getUserId()), keyList);
			// 缓存更新
			// publicService.removeHRedis_HSET(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
			// "Object","Message",um.getUserId()),um.getKeyId());
//			redisDao.delSortedSet(PublicMethod.getHRedisKey(
//					Public_Cache.PROJECT_NAME, "ListPage", "Message",
//					um.getUserId(), hql), SerializeUtil.serialize(um));
		}
	}

	/**
	 * 
	 * @TODO 获取未读信息数量
	 * @Title: getUnreadCount 
	 * @param map
	 * 		userId 当前用户
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:14
	 */
	@Override
	public HashMap<String, Object> getUnreadCount(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		int count = 0;

		String hql = " from UMessage where userId = :loginUserId and mesReadStauts = '1' and isShow = '1' ";
		List<UMessage> mesList = baseDAO.find(map, hql);
		count = mesList.size();

		resultMap.put("count", count);

		return resultMap;
	}

	/*----------------------------------- 无人响应提醒--------------------------------------*/
	/**
	 * 
	 * @TODO 激战无人响应的推送
	 * @Title: fightingPushByNoResponse 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:03
	 */
	@Override
	public void fightingPushByNoResponse(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub

		// 挑战待响应
		this.challengePushOutsideFiveDay(map);
		// 约战待响应
		this.duelPushOutsideFiveDay(map);

	}

	/**
	 * 
	 * @TODO 通用方法 挑战无人响应推送sql
	 * @Title: pubChallengeNoResponsePushSql 
	 * @param sql
	 * @return
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:32:34
	 */
	public StringBuffer pubChallengeNoResponsePushSql(StringBuffer sql) {
		sql.append(" select DISTINCT ucl.challenge_id as objectId,uu.phone,ue.code,ubc.name,uu.user_id,ucl.f_team_id,ucl.challenge_type,chbs.xteam_id ");
		sql.append(" from u_challenge ucl ");
		sql.append(" left join u_challenge_bs chbs on chbs.challenge_id = ucl.challenge_id ");
		sql.append(" left join u_challenge_ch ucch on ucch.key_id = ucl.fch_id ");
		sql.append(" left join u_order uo on uo.order_id = ucl.order_id ");
		sql.append(" left join u_order_court uoc on uoc.order_id = ucl.order_id ");
		sql.append(" left join u_br_court ubc on ubc.subcourt_id = uoc.subcourt_id ");
		sql.append(" left join u_user uu on uu.user_id = uo.user_id ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid  ");
		sql.append(" left join u_push_message upm on upm.object_id = ucl.challenge_id ");
		sql.append(" where (upm.push_status = '1' or upm.push_status is null) ");
		return sql;
	}

	/**
	 * 
	 * @TODO 通用方法 约战无人响应推送sql
	 * @Title: pubDuelNoResponsePushSql 
	 * @param sql
	 * @return
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:32:47
	 */
	public StringBuffer pubDuelNoResponsePushSql(StringBuffer sql) {
		sql.append(" select DISTINCT udl.duel_id as objectId,uu.phone,ue.code,ubc.name,udl.f_team_id,udl.duel_status,udbs.xteam_id ");
		sql.append(" from u_duel udl ");
		sql.append(" left join u_duel_bs udbs on udbs.duel_id = udl.duel_id ");
		sql.append(" left join u_order uo on uo.order_id = udl.order_id ");
		sql.append(" left join u_duel_ch udch on udch.key_id = udl.fch_id ");
		sql.append(" left join u_order_court uoc on uoc.order_id = udl.order_id ");
		sql.append(" left join u_br_court ubc on ubc.subcourt_id = uoc.subcourt_id ");
		sql.append(" left join u_user uu on uu.user_id = uo.user_id ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid ");
		sql.append(" left join u_push_message upm on upm.object_id = udl.duel_id ");
		sql.append(" where (upm.push_status = '1' or upm.push_status is null) ");
		return sql;
	}

	/**
	 * 
	 * @TODO 通用方法 发送推送
	 * @Title: sendPush 
	 * @param uchaPushList
	 * @param type
	 * @param jumpName
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午4:33:04
	 */
	public void sendPush(List<HashMap<String, Object>> uchaPushList,
			String type, String jumpName) throws Exception {
		HashMap<String, String> pushMap = null;

		for (int i = 0; i < uchaPushList.size(); i++) {
			pushMap = new HashMap<String, String>();

			if (jumpName.equals("duel")) {
				if(null!=uchaPushList.get(i).get("xteam_id")){
					pushMap.put("guest_teamId",uchaPushList.get(i).get("xteam_id").toString()); // 客队ID
				}else{
					pushMap.put("guest_teamId", ""); // 客队ID
				}
				pushMap.put("teamId", uchaPushList.get(i).get("f_team_id")
						.toString()); // 主队ID
				pushMap.put("duelId", uchaPushList.get(i).get("objectId")
						.toString()); // 约战ID
				pushMap.put("bs_id", "");// 比赛ID
				pushMap.put("duelStatus", uchaPushList.get(i)
						.get("duel_status").toString()); // 约战状态
				pushMap.put("jump", "b03");// 跳转类型
			} else if (jumpName.equals("challenge")) {
				pushMap.put("challengeId", uchaPushList.get(i).get("objectId")
						.toString());// 挑战ID
				pushMap.put("teamId", uchaPushList.get(i).get("f_team_id")
						.toString()); // 主队ID
				pushMap.put("bs_id", "");// 比赛ID
				pushMap.put("f_team_id", uchaPushList.get(i).get("f_team_id")
						.toString());// 发起队伍ID
				if(null!=uchaPushList.get(i).get("xteam_id")){
					pushMap.put("x_team_id",uchaPushList.get(i).get("xteam_id").toString());// 响应队伍ID
				}else{
					pushMap.put("x_team_id", "");// 响应队伍ID
				}
				pushMap.put("jump", "b07");// 跳转类型
				pushMap.put("challengeType", "1");// 挑战类型
			} else if (jumpName.equals("order")) {
				pushMap.put("orderId", uchaPushList.get(i).get("objectId")
						.toString());// 订单ID
				pushMap.put("jump", "b06");// 跳转类型
			}

			// 获取设备ID
			if (null != uchaPushList.get(i).get("code")) {
				pushMap.put("code", uchaPushList.get(i).get("code").toString());
			} else {
				pushMap.put("code", null);
			}

			// 生成内容参数 XXX
			String content = null;
			if (null != uchaPushList.get(i).get("name")) {
				content = uchaPushList.get(i).get("name").toString();
			}
			pushMap.put("content", content);
			pushMap.put("mes_type", type);

			// 极光推送
			publicPush.publicAppPush(pushMap);

			// 修改推送状态
			// 1.事件ID
			pushMap.put("object_id", uchaPushList.get(i).get("objectId")
					.toString());
			// 2.事件类型 1.订单 2.约战 3.挑战 4.球队
			pushMap.put("type", "2");
			// 3.具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场
			pushMap.put("event", "3");
			// 4.推送类型 1.手机 2.推送 3.邮箱
			pushMap.put("push_type", "2");
			// 5.推送状态 1-未推 -1-已推
			pushMap.put("push_status", "-1");
			publicPush.addPushMessage(pushMap);

		}

	}

	/**
	 * 通用方法 发送手机短信
	 * 
	 * @param uchaPushList
	 * @param type
	 * @param jumpName
	 * @throws Exception
	 */
	public void sendPhone(List<HashMap<String, Object>> uchaPushList,
			String type) throws Exception {
		HashMap<String, String> pushMap = null;

		for (int i = 0; i < uchaPushList.size(); i++) {
			pushMap = new HashMap<String, String>();

			// 获取设备ID
			if (null != uchaPushList.get(i).get("phone")) {
				pushMap.put("phone", uchaPushList.get(i).get("phone")
						.toString());
			} else {
				pushMap.put("phone", null);
			}

			// 生成内容参数 XXX
			String content = null;
			if (null != uchaPushList.get(i).get("name")) {
				content = uchaPushList.get(i).get("name").toString();
			}
			pushMap.put("product", content);
			pushMap.put("mes_type", type);

			// 手机短信
			publicPush.publicSendMessage(pushMap);

			// 修改推送状态
			// 1.事件ID
			pushMap.put("object_id", uchaPushList.get(i).get("objectId")
					.toString());
			// 2.事件类型 1.订单 2.约战 3.挑战 4.球队
			pushMap.put("type", "2");
			// 3.具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场
			pushMap.put("event", "3");
			// 4.推送类型 1.手机 2.推送 3.邮箱
			pushMap.put("push_type", "1");
			// 5.推送状态 1-未推 -1-已推
			pushMap.put("push_status", "-1");
			publicPush.addPushMessage(pushMap);

		}
	}

	/**
	 * 挑战无人响应提醒
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void challengePushOutsideFiveDay(HashMap<String, String> map)
			throws Exception {

		List<HashMap<String, Object>> uchaPushList = new ArrayList<HashMap<String, Object>>();

		StringBuffer sql = new StringBuffer();
		sql = pubChallengeNoResponsePushSql(sql);
		sql.append(" and ucl.challenge_status = '1' ");
		uchaPushList = baseDAO.findSQLMap(sql.toString());
		if (uchaPushList.size() > 0) {
			this.sendPush(uchaPushList, "pushResponse", "challenge");
		}

	}

	/**
	 * 约战无人响应提醒
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void duelPushOutsideFiveDay(HashMap<String, String> map)
			throws Exception {

		// HashMap<String, String> resultMap = new HashMap<>();// 返回集合

		List<HashMap<String, Object>> uchaPushList = new ArrayList<HashMap<String, Object>>();

		StringBuffer sql = new StringBuffer();
		sql = pubDuelNoResponsePushSql(sql);
		sql.append(" and udl.duel_status = '1' ");
		uchaPushList = baseDAO.findSQLMap(sql.toString());
		if (uchaPushList.size() > 0) {
			this.sendPush(uchaPushList, "pushResponse", "duel");
		}
	}

	/*----------------------------------- 提醒到场--------------------------------------*/

	/**
	 * 挑战提醒到场
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void challengePushRemindReach(HashMap<String, String> map)
			throws Exception {

		List<HashMap<String, Object>> uchaPushList = new ArrayList<HashMap<String, Object>>();

		StringBuffer sql = new StringBuffer();
		sql = pubChallengeNoResponsePushSql(sql);
		sql.append("and ucl.challenge_status = '5' and TO_DAYS(ucch.stdate)- TO_DAYS(NOW()) = 2 ");
		uchaPushList = baseDAO.findSQLMap(sql.toString());
		if (uchaPushList.size() > 0) {
			this.sendPush(uchaPushList, "pushRemindReach", "challenge");
			this.sendPhone(uchaPushList, "pushRemindReach");
		}

	}

	/**
	 * 
	 * @TODO 激战无人响应的推送
	 * @Title: fightingPushByNoResponse 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:03
	 */
	public void duelPushRemindReach(HashMap<String, String> map)
			throws Exception {

		List<HashMap<String, Object>> uchaPushList = new ArrayList<HashMap<String, Object>>();

		StringBuffer sql = new StringBuffer();
		sql = pubDuelNoResponsePushSql(sql);
		sql.append("and udl.duel_status = '4'and TO_DAYS(udch.stdate)- TO_DAYS(NOW()) = 2 ");
		uchaPushList = baseDAO.findSQLMap(sql.toString());
		if (uchaPushList.size() > 0) {
			this.sendPush(uchaPushList, "pushRemindReach", "duel");
			this.sendPhone(uchaPushList, "pushRemindReach");
		}

	}


	/**
	 * 
	 * @TODO 订单提醒到场推送
	 * @Title: orderPushByRemindReach 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:21
	 */
	@Override
	public void orderPushByRemindReach(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub

		// 挑战提醒到场
		this.challengePushRemindReach(map);
		// 约战提醒到场
		this.duelPushRemindReach(map);

	}

	/*----------------------------------- 未发起提醒 -----------------------------------*/

	/**
	 * 
	 * @TODO 订单未约战推送
	 * @Title: orderPushByNoSponsorFight 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:01:30
	 */
	@Override
	public void orderPushByNoSponsorFight(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub

		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT uo.order_id as objectId,uu.phone,ue.code ");
		sql.append(" from u_order uo ");
		sql.append(" left join u_user uu on uu.user_id = uo.user_id ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid ");
		sql.append(" where (uo.order_type = '2' or uo.order_type = '3') and uo.orderstatus = '1'  ");
		sql.append(" and uo.order_id not in  ");
		sql.append(" (select order_id from u_challenge UNION select order_id from u_duel) ");

		List<HashMap<String, Object>> uchaPushList = baseDAO.findSQLMap(sql
				.toString());
		this.sendPush(uchaPushList, "pushUnSponsor", "order");
		this.sendPhone(uchaPushList, "pushUnSponsor");

	}

	/**
	 * 
	 * @TODO 获取球队队员的设备号
	 * @Title: getTeamPlayerCodeAndPhone 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:06:09
	 */
	@Override
	public List<HashMap<String, String>> getTeamPlayerCodeAndPhone(
			HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ue.code,uu.phone from u_player up left join u_team ut on ut.team_id = up.team_id left join u_user uu on up.user_id = uu.user_id left join u_equipment ue on uu.numberid = ue.key_id where up.team_id = :teamId and up.in_team = '1'  ");
		result = baseDAO.findSQLMap(map, sql.toString());
		return result;
	}
	
	/**
	 * 获取球队队员的设备号 解散球队
	 */
	public List<HashMap<String, String>> getTeamPlayerCodeAndPhoneByDissolve(
			HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ue.code,uu.phone from u_player up left join u_team ut on ut.team_id = up.team_id left join u_user uu on up.user_id = uu.user_id left join u_equipment ue on uu.numberid = ue.key_id where up.team_id = :teamId and up.exit_type = '3'  ");
		result = baseDAO.findSQLMap(map, sql.toString());
		return result;
	}

	/**
	 * 
	 * @TODO 给队内所有人发推送
	 * @Title: pushFightToPlayerOnTeamByType 
	 * @param map
	 * 		teamId 球队主键
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:06:18
	 */
	@Override
	public void pushFightToPlayerOnTeamByType(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		if (null != map.get("teamId") && null != map.get("mes_type")
				&& null != map.get("contentName")) {

			List<HashMap<String, String>> pushMap = this
					.getTeamPlayerCodeAndPhone(map);

			if (0 < pushMap.size()) {

				// 推送内容参数
				map.put("content", map.get("contentName"));

				for (int i = 0; i < pushMap.size(); i++) {

					if (null != pushMap.get(i).get("code")) {
						map.put("code", pushMap.get(i).get("code"));
					} else {
						map.put("code", null);
					}
					// 推送
					publicPush.publicAppPush(map);

				}
			}
		}

	}
	
	/**
	 * 
	 * @TODO 给队内所有人发推送 【解散球队】
	 * @Title: pushTeamToPlayerOnByDissolve 
	 * @param map
	 * 		teamId 球队主键
	 * 		mes_type 通知类型子集
	 * 		contentName 通知内容参数 
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:17
	 */
	@Override
	public void pushTeamToPlayerOnByDissolve(HashMap<String, String> map)
			throws Exception {
		// TODO Auto-generated method stub
		if (null != map.get("teamId") && null != map.get("mes_type")
				&& null != map.get("contentName")) {

			List<HashMap<String, String>> pushMap = this
					.getTeamPlayerCodeAndPhoneByDissolve(map);

			if (0 < pushMap.size()) {

				// 推送内容参数
				map.put("content", map.get("contentName"));

				for (int i = 0; i < pushMap.size(); i++) {

					if (null != pushMap.get(i).get("code")) {
						map.put("code", pushMap.get(i).get("code"));
					} else {
						map.put("code", null);
					}
					// 推送
					publicPush.publicAppPush(map);

				}
			}
		}

	}

	
	/**
	 * 
	 * @TODO 给APP老用户推：发布新的版本
	 * @Title: pushOldUserByNewVersions 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:33
	 */
	@Override
	public void pushOldUserByNewVersions(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select DISTINCT uu.createdate,ue.code,uu.phone from u_user uu left join u_equipment ue on ue.key_id = uu.numberid where uu.createdate < :appCommitTime ");
		List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();
		map.put("appCommitTime",Public_Cache.APP_COMMIT_TIME);
		codeList = baseDAO.findSQLMap(map, sql.toString());
		map.put("mes_type","version");
		
		if(0<codeList.size()){
//			this.pubSPAndPushOnGlobalBySend(codeList,map,index);
		}
		
	} 
	
	/**
	 * 
	 * @TODO 给APP所有用户且没有补全球员信息推：补全球员信息
	 * @Title: pushUserByRepairPlayerInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:43
	 */
	@Override
	public void pushUserByRepairPlayerInfo(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT ue.code,uu.phone from u_user uu  ");
		sql.append(" left join u_player up on up.user_id =  uu.user_id  ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid ");
		sql.append(" where uu.user_status = '1' and up.team_id is null  ");
		sql.append(" and (uu.area is null or uu.birthday is null or uu.height is null or weight is null or uu.realname is null or up.can_position is null) ");
		List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();
		codeList = baseDAO.findSQLMap(map, sql.toString());
		map.put("mes_type","repairPlayer");
		
		if(0<codeList.size()){
//			this.pubSPAndPushOnGlobalBySend(codeList,map,index);
		}
	}
	
	
	/**
	 * 
	 * @TODO  给APP所有用户且没有补全球队信息推：补全并约战
	 * @Title: pushUserByRepairTeamInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午3:07:56
	 */
	@Override
	public void pushUserByRepairTeamInfo(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT ue.code,uu.phone from u_user uu  ");
		sql.append(" left join u_team ut on ut.user_id =  uu.user_id  ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid ");
		sql.append(" where uu.user_status = '1' and (ut.name is null or ut.area is null or ut.hold_date is null) ");
		List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();
		codeList = baseDAO.findSQLMap(map, sql.toString());
		map.put("mes_type","repairTeam");
		
		if(0<codeList.size()){
//			this.pubSPAndPushOnGlobalBySend(codeList,map,index);
		}
	}
	
	/**
	 * 
	 * @TODO 通用全局SP+推送
	 * @Title: pubSPAndPushOnGlobal
	 * @param codeList
	 * @param map
	 *            codeList 推送参数list map 推送参数集合 pageNum 分批推送的批次
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午7:25:30
	 */
	public void pubSPAndPushOnGlobal(HashMap<String, String> map) throws Exception {
		if (null != map.get("pageNum") || "".equals(map.get("pageNum"))) {
			Integer pageNum = Integer.parseInt(map.get("pageNum"));
			int index = 0;
			for (int j = 0; j < pageNum; j++) {
				String phone = "";
				StringBuffer sql = this.pubPushSQL(false);
				sql.append(" LIMIT ");
				sql.append(Public_Cache.GLOBAL_PUSH_LIMIT);
				sql.append(" OFFSET 0 ");

				// System.out.println("本页是第几页="+j);
				// System.out.println("本页多少条数据="+Public_Cache.GLOBAL_PUSH_LIMIT);
				// System.out.println("本页分页起始位置="+Integer.parseInt(Public_Cache.GLOBAL_PUSH_LIMIT)*j);

				List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();
				codeList = baseDAO.findSQLMap(map, sql.toString());

				if (0 < codeList.size()) {
					// phone = phone +
					// this.pubSPAndPushOnGlobalBySend(codeList,map);
					for (int i = 0; i < codeList.size(); i++) {
						index++;
						if (null != codeList.get(i).get("code")) {
							HashMap<String, String> pushMap = new HashMap<String, String>();
							// 1.事件ID
							pushMap.put("object_id",codeList.get(i).get("code").toString());
//							pushMap.put("object_id", "101d85590947ebc7b61");
							// 添加推送记录

							// 2.事件类型 1.订单 2.约战 3.挑战 4.球队 5.全局推送+sp
							pushMap.put("type", "5");
							// 3.具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期
							// 6.生成订单 7.支付成功 8.提示更新补全球员球队信息
							pushMap.put("event", "8");
							// 4.推送类型 1.手机 2.推送 3.邮箱
							pushMap.put("push_type", "2");
							// 5.推送状态 1-未推 -1-已推
							pushMap.put("push_status", "-1");
							if (publicPush.addPushMessage(pushMap)) {
								// 发推送
								// map.put("code",
								// codeList.get(i).get("code").toString());
//								map.put("code", "101d85590947ebc7b61");
//								map.put("jump", "d01");
//								publicPush.publicAppPush(map);
								System.out.println("code="+codeList.get(i).get("code").toString());
							}
						}

						if (null != codeList.get(i).get("phone")) {
							HashMap<String, String> pushMap = new HashMap<String, String>();
							// 1.事件ID
							pushMap.put("object_id", codeList.get(i).get("phone").toString());
							// pushMap.put("object_id", "13120501633");
							// 添加推送记录

							// 2.事件类型 1.订单 2.约战 3.挑战 4.球队 5.全局推送+sp
							pushMap.put("type", "5");
							// 3.具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期
							// 6.生成订单 7.支付成功 8.提示更新补全球员球队信息
							pushMap.put("event", "8");
							// 4.推送类型 1.手机 2.推送 3.邮箱
							pushMap.put("push_type", "1");
							// 5.推送状态 1-未推 -1-已推
							pushMap.put("push_status", "-1");
							if (publicPush.addPushMessage(pushMap)) {
								// 发短信
								phone = phone + codeList.get(i).get("phone").toString() + ",";
								// phone = phone + "13120501633" + ",";
							}
						}
						System.out.println("推送下标="+index);
					}
				}
				if (!"".equals(phone)) {
					phone = phone.substring(0, phone.length() - 1);
					System.out.println("---------------手机号码" + phone);
//					map.put("phone", phone);
//					map.put("content", Public_Cache.getSPCon(map.get("mes_type")));
//					publicPush.publicSendPhoneByHuax(map);
				}

			}
		}

	}

	/**
	 * 
	 * @TODO 全局push通用sql
	 * @Title: pubPushSQL 
	 * @param condition 条件 fasle/true
	 * @return
	 * @author charlesbin
	 * @date 2016年6月12日 下午4:15:23
	 */
	public StringBuffer pubPushSQL(boolean condition){
		StringBuffer sql = new StringBuffer();
		if(condition){
			sql.append(" select count(1) as count,push_table.code,push_table.phone from (   ");
		}
		sql.append(" select DISTINCT ue.code,uu.phone from u_user uu   ");
		sql.append(" left join u_player up on up.user_id =  uu.user_id   ");
		sql.append(" left join u_equipment ue on ue.key_id = uu.numberid  ");
		sql.append(" left join u_team ut on ut.user_id =  uu.user_id  ");
		sql.append(" left join u_push_message upm on upm.object_id = ue.code   ");
		sql.append(" LEFT JOIN u_push_message upm_phone ON upm_phone.object_id = uu.phone   ");
		sql.append(" where uu.user_status = '1'   ");
		sql.append(" and (upm.push_status = '1' or upm.push_status is null)  ");
		sql.append(" AND (upm_phone.push_status = '1' OR upm_phone.push_status IS NULL)  ");
		sql.append(" and (uu.area is null or uu.birthday is null or uu.height is null or weight is null   ");
		sql.append(" 		or uu.realname is null or up.can_position is null   ");
		sql.append(" 		or ut.name is null or ut.area is null or ut.hold_date is null  ");
		sql.append(" 		or up.team_id is null)  ");
		if(condition){
			sql.append(" ) as push_table   ");
		}
		return sql;
	}
	
	/**
	 * 
	 * @TODO 给APP所有用户且没有补全球队球员信息推：补全球队球员信息
	 * @Title: pushUserByRepairTeamInfoAndPlayerInfo 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月2日 下午5:46:57
	 */
	@Override
	public void pushUserByRepairTeamInfoAndPlayerInfo(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
//		long startTime = System.currentTimeMillis();
			
			StringBuffer sql = this.pubPushSQL(true);
			List<HashMap<String, Object>> pushNum = new ArrayList<HashMap<String, Object>>();
			pushNum = baseDAO.findSQLMap(map, sql.toString());
			if(0<pushNum.size()){
				BigInteger pushTotalNum = new BigInteger(pushNum.get(0).get("count").toString());
				System.out.println("总数="+pushNum.get(0).get("count").toString());
				if(0<Integer.parseInt(pushNum.get(0).get("count").toString())){
					// 计算限制页数
					Double a = pushTotalNum.doubleValue()/300;
					Double b = Math.ceil(a);
					Integer pageNum = b.intValue();
					map.put("pageNum", pageNum.toString());
					map.put("mes_type","repairTeamAndPlayer");
					this.pubSPAndPushOnGlobal(map);
				}
			}
//		long endTime = System.currentTimeMillis();
//		System.out.println("耗费时间： " + (endTime - startTime) + " ms");
	}

	/**
	 * 
	 * @TODO 批量已读 2.0.3
	 * @Title: updateMessageStautsByBatch 
	 * @param map
	 * 		mesId 通知主键
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:33:31
	 */
	@Override
	public void updateMessageStautsByBatch(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if (null != map.get("mesIdList") && !"".equals(map.get("mesIdList"))){
			String[] mesIdList = map.get("mesIdList").split(",");
			for(String mesId : mesIdList){
				UMessage um = baseDAO.get(UMessage.class,mesId);
				if("1".equals(um.getMesReadStauts())){
					um.setMesReadStauts("-1");
					baseDAO.update(um);
				}
			}
		}
	}

	/**
	 * 
	 * @TODO 批量删除 2.0.3
	 * @Title: delMessageByBatch 
	 * @param map
	 * 		mesId 通知主键
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午2:34:15
	 */
	@Override
	public void delMessageByBatch(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		if (null != map.get("mesIdList") && !"".equals(map.get("mesIdList"))){
			String[] mesIdList = map.get("mesIdList").split(",");
			for(String mesId : mesIdList){
				UMessage um = baseDAO.get(UMessage.class,mesId);
				um.setIsShow("2");
				baseDAO.update(um);
			}
		}
	}

	/**
	 * 
	 * @TODO 招募球员发送短信
	 * @Title: sendRecruitPlayer 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年6月22日 下午4:28:23
	 */
	@Override
	public boolean sendRecruitPlayer(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		boolean result = false;
		if(null!=map.get("phoneList")&&!"".equals(map.get("phoneList"))&&null!=map.get("content")&&!"".equals(map.get("content"))&&null!=map.get("teamId")&&!"".equals(map.get("teamId"))){
			String[] phoneList = map.get("phoneList").split(",");
			String phone = "";
			for(String s : phoneList){
				// 先做记录
				HashMap<String, String> pushMap = new HashMap<String, String>();
				
				// 添加招募记录
				// 1.事件ID teamId + 手机号码
				pushMap.put("object_id",map.get("teamId")+s);
				// 2.事件类型  4.球队 
				pushMap.put("type", "4");
				// 3.具体事件   9.招募球员
				pushMap.put("event", "9");
				// 4.推送类型 1.手机
				pushMap.put("push_type", "1");
				// 5.推送状态-1-已推
				pushMap.put("push_status", "-1");
				if (publicPush.addPushMessage(pushMap)) {
					phone = phone+s+",";
				}
			}
			
			// 再发短信
			if (!"".equals(phone)) {
				phone = phone.substring(0, phone.length() - 1);
				map.put("phone", phone);
				System.out.println("招募发短信="+phone);
				publicPush.publicSendPhoneByHuax(map);
				result = true;
			}
		}
		return result;		
	}

	/**
	 * 
	 * @TODO 夏不为利 SQL
	 * @Title: pushUserByAge 
	 * @return
	 * @author charlesbin
	 * @date 2016年7月30日 下午6:16:29
	 */
	public StringBuffer pushUserByAgeSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select uu.phone from u_user uu ");
		sql.append(" where uu.age <=19 and uu.age >0 and uu.age is not null and user_status = '1' ");
		return sql;
	}
	
	/**
	 * 
	 * @TODO 夏不为利，短信推送
	 * @Title: pushUserByAge 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月29日 下午1:27:39
	 */
	@Override
	public void pushUserByAge(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = this.pushUserByAgeSQL();
		List<HashMap<String, Object>> pushNum = new ArrayList<HashMap<String, Object>>();
		pushNum = baseDAO.findSQLMap(map, sql.toString());
		if(0<pushNum.size()){
			Integer pushTotalNum = pushNum.size();
			System.out.println("总数="+pushTotalNum);
				// 计算限制页数
				Double a = pushTotalNum.doubleValue()/500;
				Double b = Math.ceil(a);
				Integer pageNum = b.intValue();
				
				map.put("mes_type","tmUserByAge");

				for (int j = 0; j < pageNum; j++) {
					String phone = "";
					sql = this.pushUserByAgeSQL();
					sql.append(" LIMIT 500");
					sql.append(" OFFSET ");
					sql.append(j*500);

					//获取符合条件的号码LIST
					List<HashMap<String, Object>> codeList = new ArrayList<HashMap<String, Object>>();
					codeList = baseDAO.findSQLMap(map, sql.toString());

					if (0 < codeList.size()) {
						for (int i = 0; i < codeList.size(); i++) {
							if (null != codeList.get(i).get("phone")) {
								HashMap<String, String> pushMap = new HashMap<String, String>();
								// 1.事件ID
								pushMap.put("object_id", codeList.get(i).get("phone").toString());
								// 添加推送记录

								// 2.事件类型 1.订单 2.约战 3.挑战 4.球队 5.全局推送+sp
								pushMap.put("type", "5");
								// 3.具体事件 1.30分钟未支付 2.订场未发起 3.未响应激战 4.提醒到场 5.即将过期
								// 6.生成订单 7.支付成功 8.提示更新补全球员球队信息  9.夏不为利暑假活动
								pushMap.put("event", "9");
								// 4.推送类型 1.手机 2.推送 3.邮箱
								pushMap.put("push_type", "1");
								// 5.推送状态 1-未推 -1-已推
								pushMap.put("push_status", "-1");
								if (publicPush.addPushMessage(pushMap)) {
									// 发短信
									phone = phone + codeList.get(i).get("phone").toString() + ",";
								}
							}
						}
					}
					if (!"".equals(phone)) {
						phone = phone.substring(0, phone.length() - 1);
						System.out.println("第"+j+"页手机号码=" + phone);
						map.put("phone", phone);
						map.put("content", Public_Cache.getSPCon(map.get("mes_type")));
						publicPush.publicSendPhoneByHuax(map);
					}

				}
		}
	}

	/**
	 * 
	 * @TODO 获取用户通知开关设置状态
	 * @Title: getMessageSwitch 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年8月22日 下午5:05:04
	 */
	@Override
	public HashMap<String, Object> getMessageSwitch(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		if(null==map.get("loginUserId")||"".equals(map.get("loginUserId"))){
			return WebPublicMehod.returnRet("error", "用户不存在");
		}
		//默认开关状态正常打开  1正常 0关闭 
		resultMap.put("topic_reply",1);
		resultMap.put("topic_zan",1);
		resultMap.put("team_join",1);
		resultMap.put("team_quit",1);
		resultMap.put("role_askfor_transfer",1);
		
		
		//查询开关状态 1正常 0关闭 
		UMessageSwitch uSwitch1 = baseDAO.get(map," from UMessageSwitch where userId = :loginUserId and type='topic_reply' ");
		if(null!=uSwitch1){
			if("0".equals(uSwitch1.getSwitchStauts())){
				resultMap.put("topic_reply",0);
			}
		}
		UMessageSwitch uSwitch2 = baseDAO.get(map," from UMessageSwitch where userId = :loginUserId and type='topic_zan' ");
		if(null!=uSwitch2){
			if("0".equals(uSwitch2.getSwitchStauts())){
				resultMap.put("topic_zan",0);
			}
		}
		UMessageSwitch uSwitch3 = baseDAO.get(map," from UMessageSwitch where userId = :loginUserId and type='team_join' ");
		if(null!=uSwitch3){
			if("0".equals(uSwitch3.getSwitchStauts())){
				resultMap.put("team_join",0);
			}
		}
		UMessageSwitch uSwitch4 = baseDAO.get(map," from UMessageSwitch where userId = :loginUserId and type='team_quit' ");
		if(null!=uSwitch4){
			if("0".equals(uSwitch4.getSwitchStauts())){
				resultMap.put("team_quit",0);
			}
		}
		
		UMessageSwitch uSwitch5 = baseDAO.get(map," from UMessageSwitch where userId = :loginUserId and type='role_askfor_transfer' ");
		if(null!=uSwitch5){
			if("0".equals(uSwitch5.getSwitchStauts())){
				resultMap.put("role_askfor_transfer",0);
			}
		}
		
		return resultMap;
	}

	/**
	 * 
	 * @TODO 根据开关类型获取通知类型子集 
	 * @Title: getMessageSwitchMesType 
	 * @param type
	 * @return
	 * @author charlesbin
	 * @date 2016年8月23日 下午4:53:49
	 */
	public String getMessageSwitchMesType(String type) {
		String _temp = "";
		switch (type) {
		case "topic_reply":
			_temp = "topicComment,commentToComment";
			break;
		case "topic_zan":
			_temp = "topicThumbs";
			break;
		case "team_join":
			_temp = "tmJoin";
			break;
		case "team_quit":
			_temp = "tmExit";
			break;
		case "role_askfor_transfer":
			_temp = "tmApply,tmApplyYet,tmApplyRefuse,tmApplyAgree,tmTransfer";
			break;
		default:
			break;
		}
		return _temp;
	}
	
	/**
	 * 
	 * @TODO 修改通知开关状态
	 * @Title: updMessageSwitch 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年8月23日 下午4:38:09
	 */
	@Override
	public HashMap<String, Object> updMessageSwitch(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		if(null==map.get("loginUserId")||"".equals(map.get("loginUserId"))){
			return WebPublicMehod.returnRet("error", "用户不存在");
		}
		if(null==map.get("type")||"".equals(map.get("type"))){
			return WebPublicMehod.returnRet("error", "必传参数type为空");
		}
		if(null==map.get("status")||"".equals(map.get("status"))){
			return WebPublicMehod.returnRet("error", "必传参数status为空");
		}
		
		//1.判断通知开关设置是否存在
		//	1.1 yes 修改 no 新增
		UMessageSwitch uSwitch = baseDAO.get(map,"from UMessageSwitch where userId = :loginUserId and type=:type");
		if(null!=uSwitch){
			uSwitch.setSwitchStauts(map.get("status"));
			
			baseDAO.update(uSwitch);
		}else{
//			String mesType = this.getMessageSwitchMesType(map.get("type"));
//			String[] mesTypeList = mesType.split(",");
//			for(String s : mesTypeList){
//				//新增通知开关类型详细
//				UMessageSwitchinfo uSwitchinfo = new UMessageSwitchinfo();
//				uSwitchinfo.setSwitchinfoId(WebPublicMehod.getUUID());
//				uSwitchinfo.setMesType(s);
//				baseDAO.save(uSwitchinfo);
//			}
			//新增通知开关
			uSwitch = new UMessageSwitch();
			uSwitch.setKeyId(WebPublicMehod.getUUID());
			uSwitch.setSwitchStauts("0");
			uSwitch.setCreatetime(new Date());
			uSwitch.setType(map.get("type"));
			uSwitch.setUserId(map.get("loginUserId"));
			baseDAO.save(uSwitch);
		}
		
		return resultMap;
	}
	
}
