package upbox.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.outModel.OutPlayerList;

/**
 * 前端球员接口
 */
public interface UPlayerService {
	
	/**
	 * 
	 * 
	   TODO - 编辑球员信息 【2.0.0】
	   @param map
	   		playerId		球员ID
	   		height 			身高
	   		weight 			体重
	   		number			背号
	   		nickname		外号
	   		remark			简介
	   		position		球队中位置
	   		number			球队中背号
	   		canPosition		可踢位置  1-前锋 2-后卫 3-守门员 (多选)
	   		practiceFoot	惯用脚  1-左脚 2-右脚 3-双脚
	   		expertPosition	擅长位置	1-前锋 2-后卫 3-守门员 (单选)
	   		teamId			球队信息：编辑有队的球员信息时必传userId
			imgurl			图片显示地址		编辑无球队球员信息时更换头像
			imgSizeType		图片尺寸类型		编辑无球队球员信息时更换头像
			imgWeight		图片权重			编辑无球队球员信息时更换头像
			saveurl			图片保存地址		编辑无球队球员信息时更换头像
			userType		角色				1=普通用户、2=学员、3=裁判、4=教练、5=啦啦队、6=其他
			height			身高
			weight			体重
			birthday		出生年月
	   @return
	   		updatePlayer的hashMap<String,Object>
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	public HashMap<String, Object> editPlayerInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球员详情    用户头部获取球员信息 【2.0.0】
	   @param map
	    	userId   当前用户Id
	   @return
	   		uPlayer的hashMap<String,Object>
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
//	public HashMap<String, Object> getUplayer(HashMap<String, String> map) throws Exception;
	

	/**
	 * 
	 * 
	   TODO - 根据userId查询球员是否为队长 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @param type
	   		类型：1:所有球队；2：本队
	   @return
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public boolean isHeaderByUserId(HashMap<String, String> map,String type) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 加入球队，新增球员信息 【2.0.0】
	   @param uUser
	   		uUser对象
	   @param uTeam
	   		uTeam对象
	   @param memberType
	   		1：队长；2：队员
	   @param map
	   		position		球队中位置
	   		number			球队中背号
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		memberType      球员用户身份  1.队长、 2.总经理、3.财务后勤、4.主教练、5.队员、6.助理教练、7.队医、8.新闻官、9.啦啦队员、10.赞助商
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public UPlayer insertPlayerInTeam(UUser uUser, UTeam uTeam,HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队列表查询球员信息 【2.0.0】
	   @param uUser
	   		uUser对象
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年1月27日
	   dengqiuru
	 */
//	public UPlayer getUteamUplayerinfo(UUser uUser)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据球员Id获取球员信息，以及他所在的球队列表 【2.0.0】
	   @param map
	   		page		分页
			playerId	球员Id
	   @return
	   		uPlayer,uTeamList的hashMap<String,Object>
	   @throws Exception
	   2016年1月29日
	   dengqiuru
	 */
	public HashMap<String, Object> getUTeamListByPlayerId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 获取球员列表 【2.0.0】
	   @param map
	   		page		分页
	   		searchStr	搜索内容
	   @return
	   		uPlayerList的hashMap<String,Object>
	   @throws Exception
	   2016年1月29日
	   dengqiuru
	 */
	public HashMap<String, Object> getUPlayerList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 加入球队时，查询该用户是否存在该球队 【2.0.0】
	   @param map
	   		playerId		球员Id
	   		teamId			球队Id
	   @param type
	   		1:根据userId和teamId查找球员信息
	   		2:根据playerId和teamId查找是否为该球队队员
	   		3:根据userId和teamId查找球员信息,查看是否曾经加入过该球队
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public UPlayer getUteamUplayerinfoByTeamId(HashMap<String, String> map,String type) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 判断是否加入了两支球队 【2.0.0】
	   @param map
	   @return
	   		boolean  true:是；false：否
	   2016年2月3日
	   dengqiuru
	 * @throws Exception 
	 */
	public Boolean isJoinDoubleTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据球队Id获取该球队的球员列表 【2.0.0】
	   @param map
	   		teamId  球队Id
	   @return
	   		uPlayerList集合
	   @throws Exception
	   2016年2月15日
	   dengqiuru
	 */
	public List<UPlayer> getPlayerListByTeamId(HashMap<String, String> map) throws Exception;

	/**
	 * TODO - 获取用户在指定队伍中的球员信息 如果为空则返回NULL
	 * @param param userId 用户Id
	 *              teamId 指定球队
	 * @return
	 * @throws Exception
	 */
	public UPlayer getPlayerByUserAndTeam(HashMap<String,String> param)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 编辑用户信息时，体重 身高 年龄修改后再计算球队平均值 【2.0.0】
	   @param uUser
	   		uUser对象
	   2016年2月16日
	   dengqiuru
	 * @throws Exception 
	 */
	public void updateTeamAvg(UUser uUser) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 我的球队信息，根据userId获取该用户的球员列表 【2.0.0】
	   @param map
	   @return
	   		uPlayerList集合
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
	public List<UPlayer> getUPlayerListByUserId(HashMap<String, String> map) throws Exception;

	/**
	 *
	 *
	 TODO - 我的球队信息，根据userId获取该用户的球员列表
	 @param userId
	 @return
	 uPlayerList集合
	 @throws Exception
	 2016年2月17日
	 dengqiuru
	 */
	public List<UPlayer> getUPlayerListByUserId(String userId) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队详情--概况--球员信息 【2.0.0】
	   @param params
	   		teamId		球队Id
	   		page		分页
	   @return
	   		uPlayerList的hashMap<String,Object>
	   2016年2月17日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> listPlayerInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 加入球队时，判断用户是否退队超过24小时 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		boolean  true:是；false：否
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
	public Boolean isExceedOneDay(HashMap<String, String> map) throws Exception;
	
	
	/**
	 * 
	 * 
	   TODO - 球员详情  概况 【2.0.0】
	   @param map
	   		playerId   球员Id
	   @return
	   		uPlayer，uPlayerList的hashMap<String,Object>
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public HashMap<String, Object> getUplayerinfoByPlayerId(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 球员详情  相册列表 【2.0.0】
	   @param map
	   		playerId   球员Id
	   @return
	   		galleryList的hashMap<String,Object>
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public HashMap<String, Object> getUplayerGalleryList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球员详情  编辑相册 【2.0.0】
	   @param map
	   		playerId		球员Id
	   		pkId			相册Id（只删除时传此参数）
	   		imgSizeType		图片尺寸类型
			imgurl			图片显示地址
			imgWeight		图片权重
			saveurl			图片存放地址
	   @return
	   		uUserImg的hashMap
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUplayerGallery(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 个人中心头部 【2.0.0】
	   @param map
	   @return
	   		OutPlayerList对象
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	public HashMap<String, Object> getUTeamforuserId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO -  新增球员信息 【2.0.0】
	   @param map
	   		position		球队中位置
	   		number			球队中背号
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   @param uUser
	   		uUser对象
	   @param uTeam
	   		uTeam对象
	   @return
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	public UPlayer createUplayer(HashMap<String, String> map,UUser uUser,UTeam uTeam) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 退出队伍时，更新数据库 【2.0.0】
	   @param uPlayer
	   		uPlayer对象
	   @param exitType
	   		退队类型  1-自己退队 2-队长踢出 3-球队解散			
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public UPlayer exitTeam(UPlayer uPlayer,String exitType) throws Exception ;

	/**
	 * 
	 * 
	   TODO - 球队解散，先将球员解散 【2.0.0】
	   @param uTeam
	   		uTeam对象
	   2016年2月29日
	   dengqiuru
	 */
	public void disBandUTeam(UTeam uTeam) throws Exception;
	/**
	 * 
	 * 
	   TODO - 无球队的球员查询 【2.0.0】
	   @param map
	   		playerId  球员的Id
	   @return
	   		uPlayer 的hashMap<String,Object>
	   @throws Exception
	   2016年3月1日
	   dengqiuru
	 */
	public HashMap<String, Object> getUPlayerinfoOutUteam(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO -  战队列表头部，查询用户在球队里的球员信息 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		Set<UPlayer>
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public Set<UPlayer>  getUplayerinfoByuserIdteamId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改球员信息，加队球员曾经加入过该球队 【2.0.0】
	   @param uPlayer
	   		uPlayer对象
	   @param map
	   @param uUser
	   		uUser对象
	   @param uTeam
	   		uTeam对象
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public UPlayer updateUPlayerInTeam(UPlayer uPlayer, HashMap<String, String> map, UUser uUser,UTeam uTeam) throws Exception;
	/**
	 * 
	 * 
	   TODO - 将头像填充到球员中 【2.0.0】
	   @param uPlayer
	   		uPlayer对象
	   @throws Exception
	   2016年3月8日
	   dengqiuru
	 */
	public void setUUserImg(UPlayer uPlayer) throws Exception ;
	/**
	 * 
	 * 
	   TODO - 根据loginUserId查询用户球员信息 【2.0.0】
	   @param map
	   @return
	   		uPlayer对象
	   @throws Exception
	   2016年3月8日
	   dengqiuru
	 */
	public UPlayer getUplayerByUserId(HashMap<String, String> map) throws Exception ;
	/**
	 * 
	 * 
	   TODO - 球员详情   概况 【2.0.0】
	   @param map
	   		playerId	球员Id
	   @return
	   		galleryList、uTeamBehaviorlist、uplayerMap 的resultMap<String,Object>
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	public HashMap<String, Object> roughlyStateOfUPlayer(HashMap<String, String> map)throws Exception;
	/**
	 *
	 *
	 TODO - 球员详情   概况 【2.0.3】
	 @param map
	 playerId	球员Id
	 @return
	 galleryList、uTeamBehaviorlist、uplayerMap 的retMap<String,Object>
	 @throws Exception
	 2016年2月26日
	 dengqiuru
	 */
	public HashMap<String, Object> uPlayerInfoDetail203(HashMap<String, String> map)throws Exception;


	/**
	 * TODO - 获取球员关于我信息
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Object> getAboutPlayerInfo(HashMap<String,String> param) throws Exception;


	/**
	 * 
	 * 
	   TODO - 球员详情   头部 【2.0.0】
	   @param map
	   		playerId		球员Id
	   @return
	   		uplayer的resultMap<String,Object>
	   @throws Exception
	   2016年2月26日
	   dengqiuru
	 */
	public HashMap<String, Object> roughlyStateOfUPlayerHead(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据多个teamId获取球员信息 【2.0.0】
	   @param map
	   		teamIds  球队Id，多个以“，”隔开
	   		page 	 分页
	   @return
	   		team[0]的resultMap<String,Object>
	   @throws Exception
	   2016年3月9日
	   dengqiuru
	 */
	public HashMap<String, Object> getUplayerByTeamIds(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 关注基友 【2.0.0】
	   @param map
	   		playerId		被关注球员的Id
	   		type			事件类型   1：关注；2：取消关注
	   @return
	   		uFollow的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public HashMap<String, Object> followUPlayer(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 我关注的球员 【2.0.0】
	   @param map
	   		page		分页
	   @return
	   		uPlayerList的resultMap<String,Object>
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public HashMap<String, Object> myFollowUPlayer(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 有球队的球员查询 【2.0.0】
	   @param map
	   		playerId   球员Id
	   @return
	   @throws Exception
	   2016年3月16日
	   dengqiuru
	 */
	public HashMap<String, Object> getUPlayerinfoInUteam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 建队前，检测用户信息是否填写完成 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月18日
	   dengqiuru
	 */
	public HashMap<String, Object> checkUplayerInfo(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 加队时，检测他的球员基本信息是否填写完整 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public UPlayer getUplayerByUserIdInJoinTeam(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 战队列表查询球员信息《头部》  20160319 【2.0.0】
	   @param map
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public UPlayer getUplayerinfoByuserIdteamIdNotSet(HashMap<String, String> map,String type) throws Exception;

	/**
	 * 
	 * 
	   TODO - 将球员信息添加到输出球员信息 【2.0.0】
	   @param uPlayer
	   		uPlayer		 uPlayer
	   @param map
	   		userId		  用户Id
	   @return
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public OutPlayerList setOutPlayerList(UUser uUser,UPlayer uPlayer, HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据userId查询球队信息 【2.0.0】
	   @param map
	   		loginUserId		登录人的Id
	   @return
	   		UTeam 对象
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	public UTeam getUTeaminfoforuserId(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 判断当前用户是否是该球队成员 【2.0.0】
	   @param uTeam
	   		uTeam对象
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年3月23日
	   dengqiuru
	 */
	public UPlayer isMyTeam(UTeam uTeam, HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 战队概况  球员列表 【2.0.0】
	   @param map
	   		teamId     球队Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUplayerListInRoughly(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 战队概况  球员列表 【2.0.0】
	   @param map
	   		teamId     球队Id
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	public Integer getUplayerListInRoughlySize(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据userId查询自己的队长信息 【2.0.0】
	   @param map
	   		loginUserId		当前用户的Id
	   @return
	   		UPlayer   对象
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	public UPlayer getUteamByuserId(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球队新建后，新增队长信息 【2.0.0】
	   @param uUser
	   @param uTeam
	   @param memberType
	   @param map
	   2016年4月4日
	   dengqiuru
	 * @throws Exception 
	 */
	public void insertTeamLeader(UUser uUser, UTeam uTeam, String memberType, HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球员分享 【2.0.0】
	   @param map
	   		playerId   球员的Id
	   @throws Exception
	   2016年4月5日
	   dengqiuru
	 */
	public HashMap<String, Object> uPlayerinfoShare(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据userId查看他是否为队长，并获取他创建的球队Id 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	public String getUteamIdByuserId(HashMap<String, Object> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 激战详情--对手--球员列表 【2.0.0】
	   @param map
	   		teamId 		球队Id
	   @return
	   @throws Exception
	   2016年4月12日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUplayerListInCompetitor(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据用户Id获取用户为队员的球员信息 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年4月14日
	   dengqiuru
	 */
	public List<UPlayer> getUPlayerIsNotTeamLeader(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 位置处理【2.0.0】
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
	   TODO - 转让时，球员列表
	   @param map
	   		loginUserId	当前用户Id
	   		teamId		球队Id
	   		page 		分页
	   @return
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	public HashMap<String, Object> transferPlayerList202(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 将球员身份添加到身份表里去
	   @param map
	   @return
	   @throws Exception
	   2016年6月12日
	   dengqiuru
	 */
	public HashMap<String, Object> updateMemberType202(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 根据userId查看他是否为队长，并获取他所有创建的球队Id 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		UPlayer对象
	   @throws Exception
	   2016年4月6日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getUteamIdListByuserId(HashMap<String, Object> hashMap) throws Exception;

	/**
	 * 
	 * 
	   TODO - 设置默认球队
	   @param map
	   		playerId		球员Id
	   		loginUserId		当前用户Id
	   @return
	   @throws Exception
	   2016年6月20日
	   dengqiuru
	 */
	public HashMap<String, Object> setDefaultUteam202(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 招募球员
	   @param map
	   		teamId			球队Id
	   @param addressBooks  通讯录计划
	   @return
	   @throws Exception
	   2016年6月21日
	   dengqiuru
	 */
	public HashMap<String, Object> recruitPlayerList202(HashMap<String, String> map,List<Map<String, Object>> addressBooks) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 招募球员发送通知
	   @param map
	   		cellPhones		手机号
	   		teamId			球队Id
	   @return
	   @throws Exception
	   2016年6月22日
	   dengqiuru
	 */
	public HashMap<String, Object> recruitPlayer202(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 球员筛选
	 * @param map teamId-球队Id、loginUserId-用户Id、expertPosition-擅长位置、age-年龄、 height-身高 、weight-体重、page-分页
	 * @return
	 * @throws Exception
	 * HashMap<String,Object>
	 * xiao
	 * 2016年3月7日
	 */
	public HashMap<String, Object> findFilterPlayer(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球员列表2.0.2
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月25日
	   dengqiuru
	 */
	public HashMap<String, Object> findPlayerListByType202(HashMap<String, String> map)throws Exception;

	/**
	 * 
	 * 
	   TODO - 官员列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月25日
	   dengqiuru
	 */
	public HashMap<String, Object> findManagerListByType202(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新初始球员信息
	   @param map
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @return
	   2016年7月13日
	   dengqiuru
	 */
	public UPlayer setNoTeamPlayerInfoByUser(HashMap<String, String> map, UUser uUser) throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新有球队的球员信息
	   @param map
	   		number			背号
	   		position		位置
	   		memberType		角色
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @return
	   2016年7月14日
	   dengqiuru
	 * @throws Exception 
	 */
	public UPlayer setInTeamPlayerInfoByUser(HashMap<String, String> map, UUser uUser, UTeam uTeam) throws Exception;

	/**
	 * 
	 * 
	   TODO - 修改球队信息（赛事系统）
	   @param map
	   		playerId		球员id 
	   		teamId			球队Id
	   		eventsPlayerId	赛事系统球员Id
	   		phone			手机号
	   		realName		真实姓名
	   		nickName		昵称
	   		sex				性别
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		area			区域
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		canPosition		可踢位置
	   		number			背号
	   		position		位置
	   		memberType		角色
	   @return
	   @throws Exception
	   2016年7月18日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUPlayerInfoByEvents(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 新增球员信息（赛事系统）
	   @param map
	   		teamId			球队Id
	   		eventsPlayerId	赛事系统球员Id
	   		phone			手机号
	   		realName		真实姓名
	   		nickName		昵称
	   		sex				性别
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		area			区域
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		canPosition		可踢位置
	   		number			背号
	   		position		位置
	   		memberType		角色
	   @return
	   @throws Exception
	   2016年7月19日
	   dengqiuru
	 */
	public HashMap<String, Object> createNewPlayerByEvents(HashMap<String, String> map)throws Exception;


	/**
	 * 
	 * 
	   TODO - 用户LBS获取球员信息
	   @param userId	用户Id
	   @return
	   2016年7月21日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, String> getLBSUplayerinfo(String userId) throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新有球队的球员信息
	   @param map
	   		number			背号
	   		position		位置
	   		memberType		角色
	   		practiceFoot		惯用脚
	   		expertPosition		擅长位置
	   		canPosition			可踢位置
	   @param uUser
	   @return
	   2016年7月14日
	   dengqiuru
	 * @throws Exception 
	 */
//	public UPlayer setInTeamPlayerInfoByUserByEnter(Map<String, Object> map, UUser uUser, UTeam uTeam) throws Exception;


	/**
	 * TODO - 分页查询球员荣誉列表
	 * @param hashMap
	 * 			fuserId 球员Id
	 * 			page 当前页数
	 * @return
	 * @throws Exception
	 * @date 2016-07-20
	 * @author xhy
	 */
	public HashMap<String,Object> playerHonorListPage(HashMap<String,String> hashMap) throws Exception;


	/**
	 * TODO - 新增球员荣誉墙
	 * @param hashMap
	 * fuserId 新增荣誉墙用户Id
	 * remark 事件描述
	 * @return
	 *
	 * @throws Exception
	 * @date 2016-07-20
	 * @author xhy
	 */
	public HashMap<String,Object> savePlayerHonor(HashMap<String,String> hashMap) throws Exception;


	/**
	 * TODO - 修改球员荣誉墙
	 * @param hashMap
	 * fuserId 修改荣誉墙用户Id
	 * remark 事件描述
	 * @return
	 *
	 * @throws Exception
	 * @date 2016-07-20
	 * @author xhy
	 */
	public HashMap<String,Object> updatePlayerHonor(HashMap<String,String> hashMap) throws Exception;

	/**
	 * TODO - 球员荣誉排序
	 * @param hashMap
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> sortPlayerHonor(HashMap<String, String> hashMap) throws Exception;


	/**
	 * TODO - 删除球员荣誉墙
	 * @param hashMap
	 * 	keyId 荣誉墙Id
	 * @return
	 *
	 * @throws Exception
	 * @date 2016-07-20
	 * @author xhy
	 */
	public HashMap<String,Object> deletePlayerHonor(HashMap<String,String> hashMap) throws Exception;
	/**
	 * 
	 * TODO 获取球员信息－包括其他队员信息【2.0.3】
	 * @param map playerId-球员ID
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月25日
	 */
	public HashMap<String, Object> getUPlayerinfoManage(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 修改球员信息－包括其他队员信息【2.0.3】
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月26日
	 */
	public HashMap<String, Object> editPlayerInfoManage(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 获取自身最高权限【2.0.3】
	 * @param map
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月26日
	 */
	public Integer getTeamIdAndUserIdPlayer(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 将关注基友里的objectId从球员Id改成用户Id
	   @param map
	   @return
	   @throws Exception
	   2016年8月5日
	   dengqiuru
	 */
	public HashMap<String, Object> updateUplayerFollowObject(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球员里程碑【2.0.3】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年8月11日
	   dengqiuru
	 */
	public HashMap<String, Object> uplayerBehaviorType(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * TODO 比较等级，同时将数据放入到hashMap中 【2.0.3】
	 * @param hashMap2
	 * @param lvl1
	 * xiaoying 2016年7月26日
	 */
	public void compareLvl(HashMap<String, Object> hashMap, Integer lvl1, Integer lvl2, HashMap<String, String> map)
			throws Exception;
	/**
	 * 
	 * TODO 根据球员ID获取最高权限 【2.0.3】
	 * @param map
	 * @param lvl2 
	 * @param listPlayerRoleLimit 
	 * @return
	 * @throws Exception
	 * xiaoying 2016年7月27日
	 */
	public Integer getPlayerIdPlayer(HashMap<String, String> map, List<HashMap<String, Object>> listPlayerRoleLimit,
			Integer lvl2) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球员详情-新的球队列表
	   @param map
	   		userId		用户Id
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年8月23日
	   dengqiuru
	 */
	public HashMap<String, Object> getUTeamListInRoungh(HashMap<String, String> map) throws Exception;


	/**
	 * TODO - 获取球队所有管理角色的球员
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<UPlayer> getTeamManagePlayer(HashMap<String,String> param) throws Exception;

}
