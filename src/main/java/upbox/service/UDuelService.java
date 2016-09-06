package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UDuel;
import upbox.model.UDuelCh;
import upbox.model.UDuelChallengeImg;

/**
 * 约战接口
 * @author wmq
 *
 * 15618777630
 */
public interface UDuelService {
	/**
	 * 
	 * 
	   TODO - 验证球队是否处于约战
	   @param map teamId-队伍ID
	   @return map 
	   				-bool = 1 代表发起方，有待响应
	   				-bool = -1 代表发起方，无待响应
	   @throws Exception
	   2016年2月25日
	   mazkc
	 */
	public HashMap<String, Object> checkTeamDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询球队首次约战相关记录
	   @param map 
	   @return HashMap<String,Object>
	   @throws Exception
	   2016年2月25日
	   mazkc
	 */
	public HashMap<String,Object> duelFrist(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看我的约战列表
	   @param map userId-用户ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> findMyDuelList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看战队战绩列表
	   @param map teamId-队伍ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> findTeamDuelList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看球员战绩列表
	   @param map userId-用户ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> findPlayerDuelList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看约战列表
	   @param map 
	   			筛选条件：
	   				courtType 球场类型 1-自建 2-第三方
	   				payType 支付类型 1=发起方支付、2=线上AA、3=线下AA			
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> findAllDuelList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-在哪约
	   @param map 
	   			subCourtId 球场ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuelCh addDuelCourt(HashMap<String, String> map,UDuelCh ch)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-约几点
	   @param map 
	   			stdate 日期
	   			sttime 时间
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuelCh addDuelDate(HashMap<String, Object> map,UDuelCh ch)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-想约谁
	   @param map 
	   			duelTeamList 装载队伍ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuelCh addDuelTeam(HashMap<String, String> map,List<String> duelTeamList,UDuelCh ch,UDuel duel)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-涨咋算
	   @param map 
	   			duelPayType 支付方式
	   			payProportion 支付比例
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuelCh addDuelAccount(HashMap<String, String> map,UDuelCh ch)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-捎句话
	   @param map 
	   			remark 捎句话
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuelCh addDuelTalk(HashMap<String, String> map,UDuelCh ch)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-贴个图
	   @param map 
	   @param duelImgList 装载UDuelChallengeImg对象
	   @param UDuel 约战对象
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuel addDuelImg(HashMap<String, String> map,UDuel duel,List<UDuelChallengeImg> duelImgList)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 发起约战-走一个
	   @param map 
	   			token	用户登陆token验证	必填
				userStatus	用户登陆状态	必填
				duelTeamList	指定队伍List	选填，装在队伍ID
				duelImgList	贴个图List	选填，List中装载UDuelChallengeImg对象
				subCourtId	球场ID	必填
				stdate	开始日期	必填
				sttime	开始时间	必填
				duelPayType	支付方式	必填
				payProportion	支付比例	必填
				remark	捎句话	选填
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> addDuelOrder(HashMap<String, String> map,List<String> duelTeamList,List<UDuelChallengeImg> duelImgList)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 关注约战
	   @param map 
	   			duelId 约战ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> followDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 取消关注约战
	   @param map 
	   			keyId 主键ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> delFollowDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看我关注的约战列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> myFollowDuelList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看约战详情
	   @param map
	   			duelId 约战ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> duelInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 约战重发
	   @param map
	   			duelId 约战ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> addDuelAgain(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 取消约战
	   @param map
	   			duelId 约战ID
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> delDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 响应约战
	   @param map
	   			duelId 约战ID
	   			orderId 订单ID
	   			listMall 商品列表
	   @return
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> respDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询是否推荐约战
	   @param map
	   			duelId 约战ID
	   @return UDuel
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public UDuel recommendDuel(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询约过的对手
	   @param map
	   			token - 用户登录token
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelTeamList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询约战详情
	   @param map
	   			duelId 约战ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询约战概况详情
	   @param map
	   			duelId 约战ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelGKInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 响应约战支付成功后回调
	   @param map
	   			orderId 订单ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> respDuelPayCallBack(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询小场次列表
	   @param map
	   			duelId 约战ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelSList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查询重发后的前约战信息
	   @param map
	   			duelId 约战ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelAgainInfo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 约战分享
	   @param map
	   			duelId 约战ID
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelShareInfo(HashMap<String, String> map)throws Exception;
	
	
	/**
	 * 
	 * 
	   TODO - 获取发起成功约战列表
	   @param map
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelSuccessList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 验证约战是否存在同时间、同地点
	   @param map  subcourtId-球场ID
	   			   stdate - 日期
	   			   sttime - 时间
	   @return  1-存在同时间、同地点
	   			2-存在同时间
	   		    -1-不存在
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public int checkDuelDteAdd(HashMap<String, Object> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 查看约战相册列表
	   @param map  duelId 约战ID
	   			   type 1-发起  2-响应
	   @return 
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	public HashMap<String,Object> getDuelImgList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 判断约战是否可响应
	   @param map 
	   			duelId 约战ID
	   @param duel
	   @param team
	   @return
	   @throws Exception
	   2016年4月18日
	   mazkc
	 */
	public HashMap<String,Object> checkRespDuelStatus(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 判断约战是否可发起
	   @param map 
	   @param duel
	   @param team
	   @return
	   @throws Exception
	   2016年4月18日
	   mazkc
	 */
	public HashMap<String,Object> checkAddDuelStatus(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据约战ID、队伍ID 获取贴个图和捎句话信息
	   @param map 
	   			duelId 约战ID
	   @param duel
	   @param team
	   @return
	   @throws Exception
	   2016年4月18日
	   mazkc
	 */
	public List<Object> getDuel(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 响应方响应约战 贴个图/捎句话
	   @param map duelId 约战ID
	   @param duelImgList 贴个图列表
	   @return
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public HashMap<String,Object> addRemarkImgDuel(HashMap<String, String> map,List<UDuelChallengeImg> duelImgList) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 响应方约战详情
	   @param map 
	   		duelId 约战ID
	   		teamId 队伍ID
	   @return
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public HashMap<String,Object> getDuelRespInfo(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据球场session获取双方球队信息
	   @param map session_id 场次ID
	   @return
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public List<HashMap<String,Object>> getDuelInfoBySession(List<HashMap> sessionList) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 定时清除约战状态
	   @return
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public void updateDuelByTimeOut() throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 验证约战款项补足
	   	duelId 约战ID
	   @return 1-可以补款  2-不符合补款条件
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public int complementDuelPay(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 验证是否可以补款
	   	duelId 约战ID
	   @return 1-可以补款  2-不符合补款条件
	   @throws Exception
	   2016年4月20日
	   mazkc
	 */
	public HashMap<String,Object> checkDuelBK(HashMap<String, String> map) throws Exception;
}