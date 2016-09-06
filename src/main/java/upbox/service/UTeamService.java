package upbox.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.outModel.OutUbCourtMap;
import upbox.outModel.OutUteamList;

/**
 * 前端球队接口
 */
public interface UTeamService {
	/**
	 * 
	 * 
	   TODO - 战队详情 头部 【2.0.0】
	   @param map
	   		teamId		球队Id
	   		userId		当前用户Id
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月25日
	   dengqiuru
	 */
	public HashMap<String, Object> findPlayerInfo(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新队徽时，查看球队是否存在 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		uTeam对象
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	public UTeam findPlayerInfoById(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据球员的teamId集合查询球队集合 【2.0.0】
	   @param uplayerList
	   		球员list集合
	   @return
	   		resultList：球队list
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	public List<UTeam> getUteamNameList(List<UPlayer> uplayerList) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 建立球队 【2.0.0】
	   @param map
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
		 *  imgSizeType 	图片尺寸类型
		 *  imgurl			图片显示地址
		 *  imgWeight		图片权重
		 *  saveurl			图片保存地址
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	public HashMap<String, Object> insertNewTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改球队信息 【2.0.0】
	   @param map
		 *  teamId			球队Id
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
		 *  imgSizeType 	图片尺寸类型
		 *  imgurl			图片显示地址
		 *  imgWeight		图片权重
		 *  saveurl			图片保存地址
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUTeaminfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改球队信息 【2.0.0】
	   @param map
		 *  teamId			球队Id
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  teamSimpleName	简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
		 *  imgSizeType 	图片尺寸类型
		 *  imgurl			图片显示地址
		 *  imgWeight		图片权重
		 *  saveurl			图片保存地址
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUTeaminfoAndroid(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队列表 及搜索 【2.0.0】
	   @param map
	   		searchStr   搜索内容
	   		page 		分页
	   		teamListId		约战的战队Id列表
	   @return
	   		uTeamList的hashMap<String,Object>
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
	public HashMap<String, Object> getUteamList(HashMap<String, String> map,HashMap<String,List<Object>> mapList)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 加入一个球队 【2.0.0】
	   @param map
	   		teamId			球队Id
	   		userId			当前用户Id
	   		position		球队中位置
	   		number			球队中背号
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
			userType		角色				1=普通用户、2=学员、3=裁判、4=教练、5=啦啦队、6=其他
			height			身高
			weight			体重
			birthday		出生年月
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年2月1日
	   dengqiuru
	 */
	public HashMap<String, Object> joinInTeam(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 计算球队平均值 【2.0.0】
	   @param players
	   		一个球队的球员列表
	   @param uTeam
	   		uTeam对象
	   @throws Exception
	   2016年2月16日
	   dengqiuru
	 */
	public void updateAvgWHAge(List<UPlayer> players,UTeam uTeam) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 我的球队信息 【2.0.0】
	   @param map
	   @return
	   		uTeamList的hashMap<String,Object>
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
	public HashMap<String, Object> myTeamInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 队长踢人 【2.0.0】
	   @param map
		   	playerId  	被踢人的球员Id
		   	teamId		踢人的队伍id
	   @return
	   		uTeam 的hashMap<String,Object>
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	public HashMap<String, Object> excludeByTeamLeader(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 退出队伍 【2.0.0】
	   @param map
	   		teamId  队伍Id
	   @return
	   		uTeam 的hashMap<String,Object>
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	public HashMap<String, Object> exitTeamByUserId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队列表头部及天梯头部,还有个人中心头部信息 【2.0.0】
	   @param map
	   @return
	   		uPlayer/uTeam 的hashMap <String,Object>
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	public OutUteamList findUteaminfoListHead(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 战队详情头部 【2.0.0】
	   @param map
	   		teamId  	查看的球队Id
	   @return
	   		uTeam的hashMap<String,Object>
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public HashMap<String, Object> findUteaminfoHead(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队详情   概况 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	public HashMap<String, Object> roughlyStateOfUteam(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 解散队伍 【2.0.0】
	   @param map
	   		teamId  队伍Id
	   @return
	   		uTeam 的hashMap<String,Object>
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	public HashMap<String, Object> disbandOfUTeam(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据teamId查询队伍详情 【2.0.0】
	   @param map
	   		teamId  球队
	   @return
	   		UTeam对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public UTeam getUteaminfoByteamId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据userId查询用户自己创建的球队 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		uTeam的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public HashMap<String, Object> getUteaminfoByUserId(HashMap<String, String> map)throws Exception;
	
//	/**
//	 * 
//	 * 
//	   TODO - 根据userId查询用户所有在队的球队 【2.0.0】
//	   @param map
//	   		userId		用户Id
//	   @return
//	   		uTeamList集合
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	public List<UTeam> getUteamlistByUserId(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 关注战队 【2.0.0】
	   @param map
	   		teamId			被关注球队的Id
	   		type			事件类型   1：关注；2：取消关注
	   @return
	   		uFollow的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public HashMap<String, Object> followUTeam(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 我关注的战队 【2.0.0】
	   @param map
	   		page		分页
	   @return
	   		uTeamList的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public HashMap<String, Object> myFollowUTeam(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球队主场，区域，擂台轴 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年3月11日
	   dengqiuru
	 */
	public List<OutUbCourtMap> getBrCourtlist(HashMap<String, String> map)throws Exception;
//	public HashMap<String, Object> getBrCourtlist(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 球员概况查询球队列表根据球员信息 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	public List<UTeam> getUTeamListByUserId(HashMap<String, String> map)throws Exception;
	

	/**
	 * 
	 * 
	   TODO - 将球队信息添加到输出球队信息 【2.0.0】
	   @param uTeam
	   		uTeam		 uTeam对象
	   @param map
	   		userId		  用户表
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public OutUteamList setOutUTeamList(UTeam uTeam,HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球队设置排名 【2.0.0】
	   @param map
	   		reank     	排名
	   		teamId		球队Id
	   @throws Exception
	   2016年3月22日
	   dengqiuru
	 */
	public void setOutUTeamRank(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球员概况里面的球员信息 【2.0.0】
	   @param map
	   		userId			查看球员的userId
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUteamListInroughly(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 邀请球员 【2.0.0】
	   @param map
	   		loginUserId		当前用户Id
	   		teamId			进入的球队Id
	   		playerId		被邀请人的球员Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	public HashMap<String, Object> invitePlayer(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 对手 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		competitor、galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年3月28日
	   dengqiuru
	 */
	public HashMap<String, Object> competitorOfUteam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 推荐的对手 【2.0.0】
	   @param map
	   		loginUserId		登录人的Id
	   @return
	   		outUteamLists 的resultMap<String,Object>
	   @throws Exception
	   2016年3月28日
	   dengqiuru
	 */
	public HashMap<String, Object> getrecommendTeam(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 天梯列表头部 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年4月4日
	   dengqiuru
	 */
	public HashMap<String, Object> findUteamListHead(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球队分享 【2.0.0】
	   @param map
	   		playerId   球员的Id
	   @throws Exception
	   2016年4月5日
	   dengqiuru
	 */
	public HashMap<String, Object> uTeaminfoShare(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新球队排名 【2.0.0】
	   @param map
	   		rank  排名
	   		teamId  球队Id
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	public void updateUTeamRank(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 赛事对手 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		competitor、galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年3月28日
	   dengqiuru
	 */
	public HashMap<String, Object> eventCompetitorOfUteam(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 约战指定更多对手 【2.0.0】
	   @param map
	   		token
	   @return
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	public HashMap<String, Object> addMoreCompetitor(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 检测球队是否已解散和禁用（统一） 【2.0.0】
	   @param map
	   		token
	   @return
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	public HashMap<String, Object> checkUTeamStatus(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球队详情内，所有接口判断球队是否正常（每个接口分开） 【2.0.0】
	   @param map
	   		token    登录token
	   		teamId   当前的球队Id
	   @return
	   @throws Exception
	   2016年5月2日
	   dengqiuru
	 */
	public HashMap<String, Object> checkAllUTeamStatus(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 球员身份处理 【2.0.0】
	 * @param hashMap
	 * @param map
	 * @throws Exception
	 * void
	 * dengqiuru
	 * 2016年5月6日
	 */
	public void displayData(HashMap<String, Object> hashMap, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据用户Id检测用户与球队的各种状态
	   @param map
	   		loginUserId			当前用户Id
	   @return
	   		userUteamStatus		
	   		1:他是队长，但是没有创建过球队；
	   		2：他是队长，但是没有创建过球队；
	   		3：他创建过球队，但是他不是队长 ；
	   		4：他创建过球队，他也是队长，但是为队长的球队中不存在他创建的球队
	   		5：他创建过球队，他也是队长，但是为队长的球队中存在他创建的球队  （这一状态不可以在创建球队）
	   2016年6月16日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, String> checkUuserUteamStatus(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据userId和teamId获取他有权限的球队
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   		teamId  球队Id 
	   @return
	   @throws Exception
	   2016年6月17日
	   dengqiuru
	 */
	public UTeam getUteamByUserIdTeamId(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO -默认的球队
	   @param map
	   @return
	   @throws Exception
	   2016年6月20日
	   dengqiuru
	 */
	public HashMap<String, Object> myDefaultTeamInfo202(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户可发起约战的球队列表
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> myDuelUteam202(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据userId获取他有权限的球队列表
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   @return
	   @throws Exception
	   2016年6月20日
	   dengqiuru
	 */
	public UTeam getUteamListByUserId(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据用户获取他所有的球队及权限值
	   @param map
	   		type	1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
	   		userId  用户Id 
	   @return
	   @throws Exception
	   2016年6月21日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUteamRoleByUserId(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户可发起约战的球队列表
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   2016年6月20日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> myChallengeUteamList202(HashMap<String, String> map) throws Exception;

	
	/**
	 * 
	 * TODO 战队筛选
	 * @param map  teamId-球队Id、loginUserId-用户Id、chances-胜率((约战+挑战)/2)、rank-排名、 avgAge-平均年龄、 avgHeight-平均身高 、avgWeight-平均体重、page-分页
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月4日
	 */
	public HashMap<String, Object> findFilterTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO  新增球队区域百度云LBS数据
	 * @Title: insTeamBaiduLBSData 
	 * @param map
	 * 	teamId 用户主键
	 * 	address 地区
	 *  team_id_int 球队数值类型ID
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月29日 下午6:16:33
	 */
	public void insOrUpdTeamBaiduLBSData(HashMap<String, String> map,UTeam team) throws Exception;
	
//	/**
//	 * 
//	 * @TODO  修改球队区域百度云LBS数据
//	 * @Title: updTeamBaiduLBSData 
//	 * @param map
//	 * 	teamId 用户主键
//	 * 	address 地区
//	 *  team_id_int 用户数值类型ID 
//	 * @throws Exception
//	 * @author charlesbin
//	 * @date 2016年6月29日 下午6:16:56
//	 */
//	public void updTeamBaiduLBSData(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 赛事导入球队球员数据
	   @param map
	   		phone		手机号
	   		realName	真实姓名
	   		nickName	昵称
	   		sex			性别
	   		birthday	出生年月
	   		height		身高
	   		weight		体重
	   		area		区域
	   @return
	   @throws Exception
	   2016年7月7日
	   dengqiuru
	 */
	public HashMap<String, Object> entryUteaminfo(HashMap<String, String> map,
			List<Map<String, Object>> playerList)throws Exception;


	/**
	 * 
	 * 
	   TODO - 赛事系统修改球队信息
	   @param map
		 *  teamName		球队全名
		 *  teamClass		球队分类		1-俱乐部、2-FC、3-公司
		 *  shortName		简称
		 *  holdTime		球队成立时间
		 *  area			地区
		 *  subcourtId		主场
		 *  homeTeamShirts	主队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  awayTeamShirts	客队球衣		1-红、2-蓝、3-黄、4-绿、5-白、6-黑
		 *  remark			简介
	   @return
	   @throws Exception
	   2016年7月18日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUteamInfoByEvents(HashMap<String, String> map) throws Exception;


	/**
	 * 
	 * @TODO 新建或更新球队百度LBS数据
	 * @Title: setAreaToBaiduLBSCreateTeam 
	 * @param uTeam
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月26日 下午5:33:16
	 */
	public void setAreaToBaiduLBSCreateTeam(UTeam uTeam, HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 更新球队百度LBS数据
	 * @param teamId  球队ID
	 * @param chances 球队胜率数据
	 * @throws Exception
	 * xiaoying 2016年7月22日
	 */
	public void editChancesBaidulbs(String teamId, String chances) throws Exception;

	
	/**
	 * 
	 * @TODO 战队关注数
	 * @Title: pubGetUTeamFollowCount 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月21日 下午8:41:26
	 */
	public HashMap<String, Object> pubGetUTeamFollowCount(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 球队数据更新至百度云【跑批】
	 * @Title: pubUpdAllTeamLBS 
	 * @param teamId
	 * @param chances
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月27日 下午4:16:56
	 */
	public void pubUpdAllTeamLBS(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 添加球队荣誉墙
	 * @Title: insUTeamHonor 
	 * @param remark 描述
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月28日 下午3:06:47
	 */
	public HashMap<String, Object> insUTeamHonor(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 查询球队荣誉墙
	 * @Title: getUteamHonor 
	 * @param map
	 * 	teamId 球队主键
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年7月28日 下午3:22:12
	 */
	public HashMap<String, Object> getUteamHonor(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 修改球队荣誉墙
	 * @Title: updUteamHonor 
	 * @param map
	 * 	keyId 荣誉主键
	 * 	remark 荣誉描叙
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月3日 下午8:42:37
	 */
	public HashMap<String, Object> updUteamHonor(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 删除球队荣誉墙
	 * @Title: delUteamHonor 
	 * @param map
	 * 	keyId 荣誉主键
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月3日 下午8:43:04
	 */
	public HashMap<String, Object> delUteamHonor(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 球队荣誉排序
	 * @Title: sortTeamHonor 
	 * @param hashMap
	 * 	teamId 球队主键
	 * 	keys 荣誉主键List
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月12日 下午7:55:45
	 */
	public HashMap<String, Object> sortTeamHonor(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 关于球队LIST
	 * @Title: getAboutTeamList 
	 * @param map
	 * 		teamId 球队Id
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月18日 下午2:42:59
	 */
	public HashMap<String, Object> getAboutTeamList(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球员详情球队列表
	   @param map
	   @return
	   @throws Exception
	   2016年8月23日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUteamListInplayerRoughly(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO  球队里程碑列表
	 * @Title: getUTeamBehaviorList 
	 * @param map
	 * 	teamId
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月30日 下午5:42:05
	 */
	public HashMap<String, Object> getUTeamBehaviorList(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO  解散球队弹框接口
	 * @Title: getDissolveTeamBtuText 
	 * @param map
	 * 	teamId
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年8月30日 下午5:42:05
	 */
	public HashMap<String, Object> getDissolveTeamBtuText(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 发送解散通知至唯一角色 【多个】
	 * @Title: getDissolveTeamSendManagement 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年9月3日 下午7:38:48
	 */
	public HashMap<String, Object> getDissolveTeamSendManagement(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * @TODO 解散球队申请响应 同意/拒绝
	 * @Title: getDissolveTeamResponse 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年9月3日 下午9:45:39
	 */
	public HashMap<String, Object> getDissolveTeamResponse(HashMap<String, String> map) throws Exception;
	
	
}
