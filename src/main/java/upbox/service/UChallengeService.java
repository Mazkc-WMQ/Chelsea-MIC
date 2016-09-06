package upbox.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import upbox.model.UChallengeBs;
import upbox.model.UChallengeCh;
import upbox.model.UChampion;
import upbox.model.UMall;
import upbox.model.UOrder;

/**
 * 挑战信息接口
 * 
 * @author yc
 *
 */
public interface UChallengeService {
	
	/**
	 * 查询该球队当前是否在擂主状态
	 * @param teamId 球队id
	 * @param brCourtId 下属球场id
	 * @return
	 * 		true 当前是擂主
	 * 		false 当前不是擂主
	 * @throws Exception
	 */
	public boolean getTeamChStatus(String teamId,String brCourtId) throws Exception;
	
	/**
	 * 查询该球队所有擂主列表
	 * @param teamId 球队id
	 * @return
	 * 		List<UChampion> 擂主列表
	 * @throws Exception
	 */
	public List<UChampion> getTeamChList(String teamId) throws Exception;
	
	/**
	 * 查询该球队当前是否是本球场擂主
	 * @param teamId 球队id
	 * @param brCourtId 下属球场id
	 * @return
	 * 		true 当前是此球场擂主
	 * 		false 当前不是次球场擂主
	 * @throws Exception
	 */
	public boolean getTeamChStatusWithCourt(String teamId,String brCourtId) throws Exception;
	
	
	//预留
	/**
	 * 查询当前系统推送的擂主
	 * @return 
	 * 		List<UChallengeCh> 擂主列表
	 * @throws Exception
	 */
	public List<UChallengeCh> getSysSendCh() throws Exception;
	
	/**
	 * 根据当前球场查询当前擂主
	 * @param brCourtId 下属球场id
	 * @return
	 * 		UChampion 当前球场擂主对象（可能为null值）
	 * @throws Exception
	 */
	public UChampion getNowCh(String brCourtId) throws Exception;
	
	/**
	 * 查询当前球场的历史擂主
	 * @param brCourtId 下属球场id
	 * @return
	 * 		List<UChampion> 当前球场历史擂主列表（可能为null值）
	 * @throws Exception
	 */
	public List<UChampion> getOldChList(String brCourtId) throws Exception;
	
	/**
	 * 根据擂主id查询擂主对象
	 * @param id 擂主id
	 * @return
	 * 		UChallengeCh 擂主对象
	 * @throws Exception
	 */
	public UChampion getCp(String id) throws Exception;
	
	/**
	 * 球队各种第一次时间
	 * @param teamId 球队id
	 * 		  userFollowType  7-首次成为擂主 
	 * 							8-首次攻擂成功 
	 * 							9-首次守擂成功
	 * @return 
	 * 		Date 设擂时间
	 * @throws Exception
	 */
	public Date getFirstCh(String teamId,String userFollowType) throws Exception;
	
	//预留
	/**
	 * 获取所有当前是擂主列表
	 * @param 
	 * @return
	 * 		List<UChallengeCh> 当前为擂主的list
	 * @throws Exception
	 */
	public List<UChallengeCh> getChList() throws Exception;
	
	/**
	 * 获取所有挑战小场次信息
	 * @return
	 * @throws Exception
	 */
	public List<UChallengeBs> getChallengeBsList() throws Exception;
	
	/**
	 * 获取挑战小场次信息(单独使用)
	 * @return
	 * @throws Exception
	 */
	public UChallengeBs getChallengeBs(String bsId) throws Exception;
	
	/**
	 * 挑战关注及取消关注
	 * @param map
	 * 		challengeId 挑战id
	 * 		userId 用户id
	 * 		type 1关注
	 * 			 2取消关注
	 * @return HashMap
	 * 			success 成功
	 * 			error 失败
	 * @throws Exception
	 */
	public HashMap<String, Object> isFollowCh(HashMap<String, String> map) throws Exception;
	
	/**
	 * 激战列表（挑战父级大场次+子集小场次）
	 * @param map
	 * 			page 分页参数
	 * @return HashMap 
	 * 			ChListAndBsList 所有激战列表
	 * @throws Exception
	 */
	public HashMap<String, Object> getChListAndBsList(HashMap<String, String> map) throws Exception;
	
	/**
	 * 关于战队的激战列表（挑战父级大场次+子集小场次）
	 * @param map
	 * 			teamId 球队id
	 * 			page 分页参数
	 * @return HashMap 
	 * 			ChListAndBsListWithTeam 所有关于战队的激战列表
	 * @throws Exception
	 */
	public HashMap<String, Object> getChListAndBsListWithTeam(HashMap<String, String> map) throws Exception;
	
	/**
	 * 关于 用户 的激战列表（挑战父级大场次+子集小场次）
	 * @param map
	 * 			userId 用户id
	 * 			page 分页参数
	 * @return HashMap 
	 * 			ChListAndBsListWithUser 所有关于用户的激战列表
	 * @throws Exception
	 */
	public HashMap<String, Object> getChListAndBsListWithUser(HashMap<String, String> map) throws Exception;
	
	/**
	 * 关于球员 的激战列表（挑战父级大场次+子集小场次）
	 * @param map
	 * 			userId 用户id
	 * 			page 分页参数
	 * @return HashMap 
	 * 			ChListAndBsListWithPlayer 所有关于球员的激战列表
	 * @throws Exception
	 */
	public HashMap<String, Object> getChListAndBsListWithPlayer(HashMap<String, String> map) throws Exception;
	
	/**
	 * 球场擂主自行设置比赛时间
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> chCheckCourtSession(HashMap<String, String> map,List<UMall> mallList) throws Exception;
	
/*	*//**
	 * 球场响应挑战
	 * @param map
	 * 			teamId 球队id
	 * 			challengeId 大场次id
	 * @return
	 * 		HashMap
	 * 			success 成功
	 * 			error 失败
	 * @throws Exception
	 *//*
	public HashMap<String, Object> responseCh(HashMap<String, String> map) throws Exception;*/
	
	/**
	 * 更具球队id获取所有当前是擂主列表
	 * @param teamId 队伍id
	 * @return
	 * 		List<UChallengeCh> 当前为擂主的list
	 * @throws Exception
	 */
	public List<UChallengeCh> getChListWithTeam(String teamId) throws Exception;
	
	/**
	 * 走一个
	 * @param map
	 * 			keyId 擂主id
	 * 			msg 捎话内容
	 * 			brCourtId 下属球场id
	 * @return
	 * 			success 成功
	 * 			error 失败（各种报错根据提示展示）
	 * @throws Exception
	 */
	public HashMap<String, Object> startChallenge(HashMap<String, String> map) throws Exception;
	
	/**
	 * 走一个重发
	 * @param map
	 * 			challengeId 大场次id
	 * @return
	 * 			success 成功
	 * 			error 失败（各种报错根据提示展示）
	 * @throws Exception
	 */
	public HashMap<String, Object> startAgainChallenge(HashMap<String, String> map) throws Exception;
	
	/**
	 * 响应激战的捎句话及响应方的图片
	 * @param map
	 * 			keyId 响应方id
	 * 			msg 捎句话
	 * @return
	 * 			success 成功
	 * 			error 失败（各种报错根据提示展示）
	 * @throws Exception
	 */
	public HashMap<String, Object> responseChMsgImg(HashMap<String, String> map) throws Exception;
	
	/**
	 * 关注的大场次列表
	 * @param map
	 * 			userId 用户id
	 * @return
	 * 			clList 大场次对象
	 * 			error 失败（各种报错根据提示展示）
	 * @throws Exception
	 */
	public HashMap<String, Object> getFollowChList(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 关注的大场次对应的小场次列表
	 * @param map
	 * 			challengeId 大场次id
	 * @return
	 * 			bsList 小场次对象
	 * 			error 失败（各种报错根据提示展示）
	 * @throws Exception
	 */
	public HashMap<String, Object> getFollowChBsList(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 生成子订单并生成响应者
	 * @param map
	 * 			teamId 球队id
	 * 			challengeId 大场次id
	 * 			orderId 主订单id
	 * 			xmallList 最佳的子商品
	 * @return
	 * 		HashMap
	 * 			success 成功
	 * 			error 失败
	 */
	public HashMap<String, Object> saveChallengePerOrder(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 根据定时器调用删除关联订单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> delPerOrder(HashMap<String, String> map,List<UOrder> perOrderList) throws Exception; 
	
	/**
	 * 激战概况详细页面顶部
	 * @param map
	 * 			challengeId 大场次id
	 * 			bsId 小场地id
	 * 			challengeType 区分类型
	 * 				1 为大场次
	 * 				2 为小场次
	 * @return map
	 * 			cl 大场次对象（包含发起方订单对象 响应方订单对象）
	 *		 	bs 小场次对象
	 * @throws Exception
	 */
	public HashMap<String, Object> getChallengeAndBs(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 激战概况详细页面中部
	 * @param map
	 * 			challengeId 大场次id
	 * @return map
	 * 			or 订单对象
	 *		 	imgList 图片对象
	 * @throws Exception
	 */
	public HashMap<String, Object> getChallengeAndBsBody(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 查询当前球队是否是推荐擂主
	 * @param map
	 * 			teamId 球队id
	 * @return
	 * @throws Exception
	 */
	public boolean getRecommendStatus(String teamId) throws Exception; 
	
	/**
	 * 支付成功后挑战的业务处理
	 * @param map
	 * 			orderId 订单号
	 * @throws Exception
	 */
	public void respChallengePayCallBack(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 查询当前用户的球队是否是擂主球队
	 * @param map
	 * 			brCourtId 下属球场id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> checkChampion(HashMap<String, String> map) throws Exception;  
	
	/**
	 * 查询当前用户的球队是否是擂主球队并返回需要对象
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> checkChampionWithCourt(HashMap<String, String> map) throws Exception;  
	
	/**
	 * 球场下摆下擂台用
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> checkChampionWithCourtPage(HashMap<String, String> map) throws Exception;  
	
	
	/**
	 * 更具球场获取所有擂主信息
	 * @param map
	 * 			brCourtId 下属球场id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getChampionInfoWithCourt(HashMap<String, String> map) throws Exception;  
	
	/**
	 * 获得相册列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getChallengePhotoList(HashMap<String, String> map) throws Exception;  
	
	/**
	 * 重发详细
	 * @param map
	 * 			challengeId 挑战主键id
	 * @return
	 * @throws Exception
	 */
	public  HashMap<String, Object> getChallengeAgainInfo(HashMap<String, String> map) throws Exception;   
	
	/**
	 * 响应详细
	 * @param map
	 * 			challengeId 挑战主键id
	 * @return
	 * @throws Exception
	 */
	public  HashMap<String, Object> getRespAgainInfo(HashMap<String, String> map) throws Exception;   
	
	
	
	/**
	 * 获得 对手的相册信息 留言信息
	 * @param map
	 * 			token 用户token
	 * 			challengeId 挑战大场次id
	 * @return map
	 * 			remark	捎句话
	 * 			imgList 图片list
	 * @throws Exception
	 */
	public  HashMap<String, Object> getChallengeOtherInfo(HashMap<String, String> map) throws Exception;   
	
	/**
	 * 获得球场列表下的相关信息
	 * @param sessionId 场次id
	 * @return map
	 * 			wincount 战胜激数
				home_teamname 主队队名
				home_short_teamname 主队简称
				home_teamimgurl 主队队徽
				away_teamname 客队队名
				away_short_teamname 客队简称
				away_teamimgurl 客队队徽
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getWithCourtInfo(List<HashMap> sessionList) throws Exception;   
	
	/**
	 * 告诉订单接口是否可以再次发起订单
	 * @param orderId 最近一笔已经支付的订单id
	 * @return  true 可以再次下单（村长调用时 true表示可以去下新订单）
	 * 			fasle 不可以下订单（村长调用时 false表示请选择当前的订单）
	 * @throws Exception
	 */
	public boolean returnOrderStatus(String orderId) throws Exception;   
	
	/**
	 * 挑战应战按钮判断
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> checkChallengePerOrder(HashMap<String, String> map) throws Exception;   
	
	/**
	 * 插入首次事件
	 * @param type  		1:用户 2：球队
	 * @param behaviorType	事件类型
	 * @param userId		用户id
	 * @param teamId		球队Id
	 * @param objectId		对应事件的Id
	 * @param objectType	事件类型对应的简称
	 */
	public void saveBehavior(String type,String behaviorType,String teamId,String objectId,String objectType) throws Exception;
	
	/**
	 * 定时器调用方法
	 * @throws Exception
	 */
	public void updateChallengeByTimeOut() throws Exception ;
	
	/**
	 * 查询当前球队是否有发起的挑战
	 * @param map
	 * 			teamId 球队id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> checkChallengeWithTeam(HashMap<String, String> map) throws Exception;    
	
}
