package upbox.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UPlayer;
import upbox.model.UTask;
import upbox.model.UTaskAll;
import upbox.model.UTaskInfo;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.UWorth;
import upbox.model.UWorthLog;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UUserService;
import upbox.service.UWorthService;
import upbox.util.YHDCollectionUtils;

/**
 *身价相关接口实现
 *
 */
@Service("uworthService")
public class UWorthServiceImpl implements UWorthService{
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private UUserService uUserService;
	@Resource
	private UWorthService  uworthService;
	@Resource
	private PublicService publicService;
	HashMap<String, Object> hashMap = new HashMap<>();

	@Override
	public HashMap<String, Object> findUserIdWorthCount(HashMap<String, String> map) throws Exception {
		this.getWorthInfo(map);
		return hashMap;
	}

	@Override
	public HashMap<String, Object> findUserIdCompleteWorthInfo(HashMap<String, String> map) throws Exception {
		if(map.get("userId")==null||"".equals(map.get("userId"))){
			map.put("userId", map.get("loginUserId"));
		}
		List<HashMap<String, Object>> list=new ArrayList<>();
		HashMap<String, Object> result=null;
		long between=0;
		long day = 0;
	    long month=0;
	    long year=0;
	    //身价值处理
		this.getWorthColorInfo(map);
		List<UTaskAll> listTaskAll=baseDAO.find(map,"from UTaskAll where UUser.userId=:userId group by UTask.keyid order by createtime desc,keyid desc");
		baseDAO.getSessionFactory().getCurrentSession().clear();
		for (UTaskAll uTaskAll : listTaskAll) {
			result=new HashMap<>();
			//描述
			if(uTaskAll.getUTask()!=null){
				result.put("remark", uTaskAll.getUTask().getRemark());
				result.put("count", uTaskAll.getUTask().getRewardsj());
			}
			//时间处理
			if(uTaskAll.getCreatetime()!=null){
				between = new Date().getTime() - uTaskAll.getCreatetime().getTime();
				day = between / (24 * 60 * 60 * 1000);
				month = day / 28;
				year = month / 12;
				if (day == 0) {
					result.put("handleDate", "1小时前");
					if(between / (60 * 60 * 1000)>0){
						result.put("handleDate", between / (60 * 60 * 1000) + "小时前");
					}
				} else if (day >= 1 && day < 7) {
					result.put("handleDate", day + "天前");
				} else if (day >= 7 && day < 28) {
					result.put("handleDate", day / 7 + "周前");
				} else if (month <= 12) {
					result.put("handleDate", month + "月前");
				} else if (year >= 1) {
					result.put("handleDate", year + "年");
				} else {
					result.put("handleDate", day + "天前");
				}
			}
			list.add(result);
		}
		hashMap.put("listTaskAll", list);//完成身价
		return hashMap;
	}
	@Override
	public HashMap<String, Object> findUserIdAllWorthInfo(HashMap<String, String> map) throws Exception {
		if(map.get("userId")==null||"".equals(map.get("userId"))){
			map.put("userId", map.get("loginUserId"));
		}
		List<HashMap<String, Object>> listTask=baseDAO.findSQLMap(map,"select t.keyid as taskid,t.remark,ta.keyid as taskAllId,t.imgurl,t.weburl,t.task_behavior as taskBehavior,t.rewardsj as count,t.count as taskCount,count(ta.keyid) as taskNumber from u_upbox_increment.u_task t left join u_upbox_increment.u_task_info ta on ta.taskid=t.keyid  and ta.userid=:userId where t.task_use_status='1' group by t.keyid order by t.task_behavior,ta.createtime,t.keyid ");
		if(listTask!=null&&listTask.size()>0){
			listTask.removeAll(YHDCollectionUtils.nullCollection());
			for (HashMap<String, Object> hashMap : listTask) {
				hashMap.put("isComplete", "1");
				hashMap.put("isCompleteName", "待领取");
				if(hashMap.get("taskCount")!=null&&hashMap.get("taskNumber")!=null&&Integer.parseInt(hashMap.get("taskCount").toString())<=Integer.parseInt(hashMap.get("taskNumber").toString())){
					hashMap.put("isComplete", "2");
					hashMap.put("isCompleteName", "已完成");
				}
			}
		}
		return WebPublicMehod.returnRet("listTask", listTask);
	}
	@Override
	public HashMap<String, Object> findUserIdAllWorthInfoWeb(HashMap<String, String> map) throws Exception {
		List<HashMap<String, Object>> listTask=baseDAO.findSQLMap(map,"select t.keyid as taskid,t.remark,ta.keyid as taskAllId,t.imgurl,t.weburl,t.task_behavior as taskBehavior,t.rewardsj as count,t.count as taskCount,count(ta.keyid) as taskNumber from u_upbox_increment.u_task t left join u_upbox_increment.u_task_info ta on ta.taskid=t.keyid  and ta.userid=:userId where t.task_use_status='1' group by t.keyid order by t.task_behavior,ta.createtime,t.keyid ");
		UUser user = baseDAO.getHRedis(UUser.class,map.get("userId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
		
		if(listTask!=null&&listTask.size()>0){
			listTask.removeAll(YHDCollectionUtils.nullCollection());
			for (HashMap<String, Object> hashMap : listTask) {
				hashMap.put("isComplete", "1");
				hashMap.put("isCompleteName", "待领取");
				if(hashMap.get("taskCount")!=null&&hashMap.get("taskNumber")!=null&&Integer.parseInt(hashMap.get("taskCount").toString())<=Integer.parseInt(hashMap.get("taskNumber").toString())){
					hashMap.put("isComplete", "2");
					hashMap.put("isCompleteName", "已完成");
				}
			}
		}
		if(user!=null){
			hashMap.put("realname", user.getRealname());
			hashMap.put("nickname", user.getNickname());
			if(user.getSex()!=null&&!"".equals(user.getSex())){
				hashMap.put("sex", user.getSex());
				hashMap.put("sexName", Public_Cache.HASH_PARAMS("sex").get(user.getSex()));
			}
		}
		map.put("loginUserId", map.get("userId"));
		 //身价值处理
		this.getWorthColorInfo(map);
		hashMap.put("listTask", listTask);
		return hashMap;
	}
	@Override
	public HashMap<String, Object> saveTaskInfo(HashMap<String, String> map) throws Exception {
		UTask task=null;
		List<UTaskInfo> listTaskinfo=null;
		List<HashMap<String, Object>> list=new ArrayList<>();
		List<UTaskAll> listTaskAll=null;
		UTaskAll taskAll=null;
		UTaskInfo taskInfo=null;
		UWorth worth=null;
		UWorthLog worthLog=null;
		if(map.get("taskBehavior")!=null&&map.get("loginUserId")!=null){
			task = baseDAO.get(map, "from UTask where taskBehavior =:taskBehavior and taskUseStatus='1'");
			if (task != null) {
				// 如果存在数据，就不执行
				listTaskinfo = baseDAO.find(map,
						"from UTaskInfo where UTask.taskBehavior=:taskBehavior and UUser.userId=:loginUserId ");
				if (listTaskinfo!=null&&task.getCount()<=listTaskinfo.size()) {
					return WebPublicMehod.returnRet("worthList", list);
				}
				if (map.get("teamId") != null && !"".equals(map.get("teamId"))) {
					map.put("teamid", map.get("teamId"));
					UPlayer player = baseDAO.get(map,
							"from UPlayer where UTeam.teamId=:teamid and UUser.userId=:loginUserId ");
					map.put("playerid", player != null ? player.getPlayerId() : null);
				}
				
				UUser user = uUserService.getUserinfoByToken(map);
				if (task.getRemark() != null && !"".equals(task.getRemark())) {
					map.put("remark", task.getRemark());
				}
				this.saveTaskAll(map,task,listTaskAll,taskAll,user);
				this.saveUTaskInfo(map, task, user, taskInfo);
				this.saveWorth(map, task, user, worth, worthLog);
				listTaskinfo = baseDAO.find(map,
						"from UTaskInfo where UTask.taskBehavior=:taskBehavior and UUser.userId=:loginUserId ");
				if (listTaskinfo!=null&&task.getCount()<=listTaskinfo.size()) {
					hashMap=new HashMap<>();
					if(task!=null){
						hashMap.put("image", task.getImgurl());
						hashMap.put("count",task.getRewardsj());
					}
					hashMap.put("code", map.get("taskBehavior"));
					hashMap.put("remark",map.get("remark"));
					hashMap.put("worthSuccess", "恭喜您!完成了一个身价任务！");
					hashMap.put("worth", "success");
					list.add(hashMap);
				}
			}
			
		}
		return WebPublicMehod.returnRet("worthList", list);
	}
	
	

	@Override
	public HashMap<String, Object> findUserIdTaskInfo(HashMap<String, String> map) throws Exception {
		long count=baseDAO.count("select count(*) from UTask where taskUseStatus='1'",true);
		List<String> listStr=new ArrayList<>();
		List<HashMap<String, Object>> listTask=baseDAO.findSQLMap(map,"select t.keyid as taskid,t.remark,ta.keyid as taskAllId,t.imgurl,t.weburl,t.task_behavior as taskBehavior,t.rewardsj as count,t.count as taskCount,count(ta.keyid) as taskNumber from u_upbox_increment.u_task t left join u_upbox_increment.u_task_info ta on ta.taskid=t.keyid  and ta.userid=:loginUserId where t.task_use_status='1' group by t.keyid order by t.task_behavior,ta.createtime,t.keyid ");
		String isShow="2";
		if(listTask!=null&&listTask.size()>0){
			listTask.removeAll(YHDCollectionUtils.nullCollection());
			for (HashMap<String, Object> hashMap : listTask) {
				if(hashMap.get("taskCount")!=null&&hashMap.get("taskNumber")!=null&&Integer.parseInt(hashMap.get("taskCount").toString())<=Integer.parseInt(hashMap.get("taskNumber").toString())){
					listStr.add((String)hashMap.get("taskid"));
				}
			}
		}
		if(listStr.size()>=count){
			isShow="1";
		}
		return WebPublicMehod.returnRet("isShow", isShow);
	}
	/**
	 * 
	 * TODO 添加任务
	 * @param map
	 * @param task
	 * @param listTaskinfo
	 * @param list
	 * @param listTaskAll
	 * @param taskAll
	 * @param taskInfo
	 * @param worth
	 * @param worthLog
	 * @param player
	 * @param user
	 * @return
	 * @throws Exception
	 * xiaoying 2016年8月29日
	 */
	private HashMap<String, Object> publicSaveTask(HashMap<String, String> map,UTask task, List<UTaskInfo> listTaskinfo,
			List<UTaskAll> listTaskAll, UTaskAll taskAll, UTaskInfo taskInfo, UWorth worth, UWorthLog worthLog,
			UPlayer player, UUser user) throws Exception{
		if(map.get("taskBehavior")!=null){
			task = baseDAO.get(map, "from UTask where taskBehavior =:taskBehavior and taskUseStatus='1'");
			if (task != null) {
//				// 如果存在数据，就不执行
				listTaskinfo = baseDAO.find(map,"from UTaskInfo where UTask.taskBehavior=:taskBehavior and UUser.userId=:loginUserId ");
				if (listTaskinfo!=null&&task.getCount()<=listTaskinfo.size()) {
					return WebPublicMehod.returnRet("worthList", "-1");
				}
				if (map.get("teamId") != null && !"".equals(map.get("teamId"))) {
					map.put("teamid", map.get("teamId"));
					player = baseDAO.get(map,
							"from UPlayer where UTeam.teamId=:teamid and UUser.userId=:loginUserId ");
					map.put("playerid", player != null ? player.getPlayerId() : null);
				}
				if (task.getRemark() != null && !"".equals(task.getRemark())) {
					map.put("remark", task.getRemark());
				}
				this.saveTaskAll(map,task,listTaskAll,taskAll,user);
				this.saveUTaskInfo(map, task, user, taskInfo);
				this.saveWorth(map, task, user, worth, worthLog);
			}
		}
		return WebPublicMehod.returnRet("worthList", "1");
	}
	/**
	 * 
	 * TODO 完成身价任务
	 * @param map
	 * @param task
	 * @param listTaskAll
	 * @param taskAll
	 * @param user
	 * @throws Exception
	 * xiaoying 2016年8月29日
	 */
	private void saveTaskAll(HashMap<String, String> map, UTask task, List<UTaskAll> listTaskAll, UTaskAll taskAll,
			UUser user) throws Exception{
		map.put("taskid", task.getKeyid());
		listTaskAll = baseDAO.find(map,
				"from UTaskAll where UUser.userId=:loginUserId and UTask.keyid=:taskid ");
		// 如果查到任务，那么累计数＋1，否者新建
		if (listTaskAll != null && listTaskAll.size() > 0) {
			for (UTaskAll uTaskAll : listTaskAll) {
				uTaskAll.setCount(uTaskAll.getCount() + 1);
				uTaskAll.setCreatetime(new Date());
				baseDAO.update(uTaskAll);
			}
		} else {
			taskAll = new UTaskAll();
			taskAll.setKeyid(WebPublicMehod.getUUID());
			taskAll.setUTask(task);
			taskAll.setUUser(user);
			taskAll.setTeamid(map.get("teamid"));
			taskAll.setCreatetime(new Date());
			taskAll.setCount(1);
			baseDAO.save(taskAll);
		}
	}
	/**
	 * 
	 * TODO 添加身价信息
	 * @param map
	 * @param task
	 * @param user
	 * @throws Exception
	 * xiaoying 2016年8月8日
	 */
	private void saveWorth(HashMap<String, String> map, UTask task, UUser user,UWorth worth,UWorthLog worthLog) throws Exception{
		//身价数据
		if(task.getRewardsj() != null){
			worth = baseDAO.get(map, "from UWorth where UUser.userId=:loginUserId");
			if (worth != null) {
				if (worth.getCount() != null) {
					worth.setCount(worth.getCount() + task.getRewardsj());
					baseDAO.update(worth);
				}
			} else {
				worth =new UWorth();
				worth.setKeyid(WebPublicMehod.getUUID());
				worth.setUUser(user);
				worth.setCount(task.getRewardsj());
				worth.setCreatedate(new Date());
				worth.setCountnum(WebPublicMehod.getCountNum(user.getPhone()));
				baseDAO.save(worth);
			}
			worthLog=new UWorthLog();
			worthLog.setKeyid(WebPublicMehod.getUUID());
			worthLog.setUUser(user);
			worthLog.setCount(task.getRewardsj());
			worthLog.setLogdate(new Date());
			worthLog.setTaskid(map.get("taskid"));
			worthLog.setResource(map.get("resource"));
			worthLog.setRemark(map.get("remark"));
			baseDAO.save(worthLog);
		}
		
	}

	/**
	 * 
	 * TODO 添加任务明细
	 * @param map
	 * @param task
	 * @param user
	 * @throws Exception
	 * xiaoying 2016年8月6日
	 */
	private void saveUTaskInfo(HashMap<String, String> map, UTask task, UUser user,UTaskInfo taskInfo) throws Exception{
		taskInfo=new UTaskInfo();
		taskInfo.setKeyid(WebPublicMehod.getUUID());
		taskInfo.setUTask(task);
		taskInfo.setUUser(user);
		taskInfo.setCreatetime(new Date());
		taskInfo.setTeamid(map.get("teamid"));
		taskInfo.setPlayerid(map.get("playerid"));
		taskInfo.setDuelid(map.get("duelid"));
		taskInfo.setChallegeid(map.get("challegeid"));
		taskInfo.setMatchid(map.get("matchid"));
		taskInfo.setRemark(map.get("remark"));
		baseDAO.save(taskInfo);
		
	}

	/**
	 * 
	 * TODO 根据登录用户获取身价值
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @throws Exception
	 * xiaoying 2016年8月6日
	 */
	private void getWorthInfo(HashMap<String, String> map) throws Exception{
		if(map.get("userId")==null||"".equals(map.get("userId"))){
			map.put("userId", map.get("loginUserId"));
		}
		UWorth worth=baseDAO.get(map,"from UWorth where UUser.userId=:userId");
		hashMap.put("count", 0);
		if(worth!=null){
//			hashMap.put("worth", worth);
			if(worth.getCount()!=null){
				hashMap.put("count", worth.getCount());
			}
		}
	}
	/**
	 * 
	 * TODO 获取身价及颜色处理
	 * @param map loginUserId:前端用户ID(登录用户Id)
	 * @throws Exception
	 * xiaoying 2016年8月5日
	 */
	private void getWorthColorInfo(HashMap<String, String> map)  throws Exception{
		this.getWorthInfo(map);
		int count=(Integer)hashMap.get("count");
		hashMap.put("background", "#f0f0f0"); // 底色灰色
		hashMap.put("changeColor", "#2dafff");// 变动色蓝色
		hashMap.put("mobileNumber", 0);// 移动多少
		if (count <= 100) {
			hashMap.put("background", "#f0f0f0");
			hashMap.put("changeColor", "#2dafff");
			hashMap.put("mobileNumber", count);
		}
		if (count > 100) {
			hashMap.put("mobileNumber", count % 100);
			if ((count / 100) % 2 != 0) {
				hashMap.put("background", "#2dafff");
				hashMap.put("changeColor", "#f09123");//橙色
			} else {
				hashMap.put("background", "#f09123");
				hashMap.put("changeColor", "#2dafff");
			}
		}
	}

	@Override
	public HashMap<String, Object> repairTaskInfo(HashMap<String, String> map) throws Exception {
		System.out.println(new Date()+"------------------");
		UTask task=null;
		List<UTaskInfo> listTaskinfo=null;
		List<UTaskAll> listTaskAll=null;
		UTaskAll taskAll=null;
		UTaskInfo taskInfo=null;
		UWorth worth=null;
		UWorthLog worthLog=null;
		UPlayer player=null;
		UUser user =null;
		List<UUser> listUser=baseDAO.find("from UUser where userId is not null and phone is not null and old_key_id is not null");
		if(listUser!=null){
			for (int i = 0; i < listUser.size(); i++) {
				map.put("loginUserId", listUser.get(i).getUserId());
				map.put("taskBehavior", "1");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
				this.publicSaveTask(map,task,listTaskinfo,listTaskAll,taskAll,taskInfo,worth,worthLog,null,listUser.get(i));
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		List<UPlayer> listPlayer=baseDAO.find("from UPlayer where inTeam='2' and UTeam.teamId is null and UUser.userId is not null and UUser.phone is not null and UUser.old_key_id is not null group by UUser.userId");
		if(listPlayer!=null){
			for (int i = 0; i < listPlayer.size(); i++) {
				user=listPlayer.get(i).getUUser();
				player=listPlayer.get(i);
				if (publicService.StringUtil(user.getRealname()) && publicService.StringUtil(user.getNickname())
						&& publicService.StringUtil(user.getSex()) && user.getBirthday() != null
						&& publicService.StringUtil(user.getWeight()) && publicService.StringUtil(user.getHeight())) {
					if (publicService.StringUtil(player.getPracticeFoot())
							&& publicService.StringUtil(player.getExpertPosition())
							&& publicService.StringUtil(player.getCanPosition())) {
						map.put("loginUserId", user.getUserId());
						map.put("taskBehavior", "2");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
						this.publicSaveTask(map,task,listTaskinfo,listTaskAll,taskAll,taskInfo,worth,worthLog,player,user);
					}
				}
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		List<UTeam> listTeam=baseDAO.find("from UTeam where UUser.userId is not null and teamId is not null and UUser.phone is not null and old_key_id is not null group by UUser.userId");
		if(listTeam!=null){
			for (int i = 0; i < listTeam.size(); i++) {
				user = listTeam.get(i).getUUser();
				map.put("loginUserId", user.getUserId());
				map.put("taskBehavior", "3");// 1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队4、首次加入球队
				map.put("teamId", listTeam.get(i).getTeamId());
				this.publicSaveTask(map, task, listTaskinfo, listTaskAll, taskAll, taskInfo, worth, worthLog, player,user);
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		listPlayer=baseDAO.find("from UPlayer where inTeam='1' and UTeam.teamId is not null and UUser.userId is not null and UUser.phone is not null and UUser.old_key_id is not null group by UUser.userId");
		if(listPlayer!=null){
			for (int i = 0; i < listPlayer.size(); i++) {
				user = listPlayer.get(i).getUUser();
				player=listPlayer.get(i);
				map.put("loginUserId", user.getUserId());
				map.put("taskBehavior", "4");// 1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队4、首次加入球队
				map.put("teamId", listPlayer.get(i).getUTeam().getTeamId());
				this.publicSaveTask(map, task, listTaskinfo, listTaskAll, taskAll, taskInfo, worth, worthLog, player,user);
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		System.out.println(new Date()+"----------5--------");
		return WebPublicMehod.returnRet("su", "1");
	}
	
}
