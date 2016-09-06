package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.UBrCourt;
import upbox.model.UBrCourtimage;
import upbox.model.UBrCourtsession;
import upbox.model.UCourt;
import upbox.model.UMall;

public interface UCourtService {

	/**
	 * 
	 * 
	   TODO - 查询球场列表
	   @param map
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtList(HashMap<String, String> params) throws Exception;
	
	/**
	 * 如支付成功 回调方法置场次预定
	 * @param orderId 订单编号
	 * @return  true成功 
	 * 			false失败
	 * @throws Exception
	 */
	public boolean saveCourtSessionOrderStatus(String orderId)throws Exception;
	
	
	/**
	 * 
	 * 
	   TODO - 根据订单ID查询球场详情
	   @param map
	   	order_id 订单ID
	   or
	   	subcourt_id 球场ID--第三方球场
	   @return
	   @throws Exception
	   2015年3月30日
	   kevinzhang
	 */
	@SuppressWarnings("rawtypes")
	HashMap queryCourtDetailByOrderid(HashMap<String, String> params) throws Exception;
	
	
	
	/**
	 * 
	 * 
	   TODO - 查询球场详情
	   @param map
	   subcourt_id 子球场ID
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */

	HashMap<String, Object> queryCourtDetail(HashMap<String, String> params) throws Exception;
	
	

	/**
	 * 
	 * 
	   TODO - 根据日期查询球场场次
	   @param map
	   subcourt_id 子球场ID
	   stdate 日期
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> querySessionByDate(HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据剩余场次数
	   @param map
	   subcourt_id 子球场ID
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> querySessionLeft(HashMap<String, String> params) throws Exception;
	
	/**
	 * 
	 * TODO 获取下属球场图片信息--订单service中调用
	 * @param map subcourtId-下属球场Id、imgSizeType-图片尺寸类型 、cimgUsingType-图片使用类型
	 * @return 
	 * @throws Exception
	 * UBrCourtimage
	 * xiao
	 * 2016年2月22日
	 */
	public UBrCourtimage queryBrCourtimg(HashMap<String, String> map) throws Exception;
	
	
	/**
	 * 
	 * 
	 TODO - 球场概括
	 
	 * @param map subcourtId-下属球场Id、imgSizeType-图片尺寸类型 、cimgUsingType-图片使用类型
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 *  subcourt_id 子球场ID
	 */
	HashMap<String, Object> queryCourtGeneral(HashMap<String, String> params)throws Exception;

	/**
	 * 
	 * 
	 TODO - 关注球场
	 
	 * @param map subcourtId-下属球场Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 *  subcourt_id 子球场ID
	 *  follow_status = 1 关注 ， 2 取消关注，100 查询用户是否关注过
	 */
	HashMap<String, Object> subscribeCourt(HashMap<String, String> params)throws Exception;

	/**
	 * 
	 * 
	 TODO - 查询场次可预订商品列表
	 
	 * @param map sessionids- 场次Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> queryProductBySession(HashMap<String, String> params)throws Exception;

	
	/**
	 * 
	 * 
	 TODO - 新建第三方球场
	 
	 * @param map sessionids- 场次Id 
	 * 		name		名称
	 * 		area		区域 第三级Id
	 * 		address		地址
	 * @throws Exception
	 * 2016年2月22日
	 * @return hashMap id-球场ID、name-球场名称
	 * 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> addCourt(HashMap<String, String> params,
			UBrCourt uBrCourt)throws Exception;

	
	/**
	 * 
	 * 
	   TODO - 查询球场列表 简要 包括第三方球场
	   @param map
	   @return
	   @throws Exception
	   2015年3月7日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtListShort(HashMap<String, String> params)throws Exception;
	
	
	
	/**
	 * 
	 * 
	   TODO - 查询指定场次是否被预定
	   @param sessionId 场次ID 
	   @return true 不可预定    false 可预定
	   @throws Exception
	   2015年3月8日
	   kevinzhang
	   
	 */
	boolean sessionBeOrdered(String uBrCourtsessionId)throws Exception;

	
	/**
	 * 
	 * 
	 TODO - 我关注的球场列表
	 
	 * @param map subcourtId-下属球场Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 *  subcourt_id 子球场ID
	 
	 */
	HashMap<String, Object> querySubscribedCourtList(
			HashMap<String, String> params)throws Exception;

	/**
	 * 
	 * 
	 TODO - 更新场次信息－空场
	 
	 * @param map subcourtId-下属球场Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 *  orderId 子球场ID
	 
	 */
	boolean updateCourtSession(
			String orderId)throws Exception;


	/**
	 * 
	 * 
	 TODO - 球场详情
	 
	 * @param map subcourtId-下属球场Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 *  subcourtid 子球场ID
	 
	 */
	UBrCourt getUCourt(String subcourtid )throws Exception;

	/**
	 * 
	 * 
	 TODO - 查询场次可预订商品分类列表
	 
	 * @param map sessionids- 场次Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> queryProductTypeBySession(HashMap<String, String> params)throws Exception;

	/**
	 * 
	 * 
	 TODO - 生成订单，场次预定
	 
	 * @param map listSession- 场次Id 
	 * @param map listMall- 商品Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> saveOrder(HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	 TODO - 查询球场已预定场次列表
	 
	 * @param map sessionids- 场次Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> querySessionListBySubcourtId(
			HashMap<String, String> params) throws Exception;
	/**
	 * 
	 * 
	 TODO - 查询已预定场次列表,在哪约
	 
	 * @param map sessionids- 场次Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> querySessionListAllSubcourtDuel(
			HashMap<String, String> params,String type) throws Exception;

	/**
	 * 
	 * 
	 TODO - 生成订单CHECK，场次预定
	 
	 * @param map listSession- 场次Id 
	 * @param map listMall- 商品Id 
	 * @throws Exception
	 * 2016年2月22日
	 * @return 2015年2月29日 kevinzhang
	 */
	HashMap<String, Object> saveOrderCheck(HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查询 我关注过的 球场列表
	   @param map
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtListSubscribed(
			HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	   TODO - 查询 我使用过的 球场列表
	   @param map
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtListOrdered(HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	   TODO - 球场 评分及查询
	   @param map
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> modifyCourtScore(HashMap<String, String> params) throws Exception;

	
	/**
	 * 
	 * 
	 TODO - 场次预定- 查询场次订单信息 
	 * 
	 * @return 2015年1月26日 kevinzhang
	 * @throws Exception 
	 */
	HashMap<String, Object> querySessionOrderSubcourt(
			HashMap<String, String> params) throws Exception;

	
	
	/**
	 * 
	 * 
	   TODO - 查询球场详情
	   @param map
	   subcourt_id 子球场ID
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtDetailIcon(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> querySessionByDateFight(
			HashMap<String, String> params) throws Exception;

	HashMap<String, Object> querySessionByOrderId(HashMap<String, String> params) throws Exception;
	/**
	 * 
	 * 
	   TODO - 查询球场相册
	   @param map
	   subcourt_id 子球场ID
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> queryCourtAlbum(HashMap<String, String> params) throws Exception;

	/**
	 * 
	 * 
	   TODO - 根据剩余场次数
	   @param map
	   subcourt_id 子球场ID
	   @return
	   @throws Exception
	   2015年1月24日
	   kevinzhang
	 */
	HashMap<String, Object> querySessionLeftDelayOneWeek(HashMap<String, String> params) throws Exception;
	 
	/**
	 * 
	 * TODO 查询优惠价
	 * @param sessionid 场次ID
	 * @return 最终价格（商品显示使用价格）
	 * @throws Exception
	 */
	Double	getFavPrice(String sessionid,boolean sprice) throws Exception;
	/**
	 * 
	 * TODO 查询商城信息－挑战固定裁判
	 * @param params 
	 * @return 返回挑战固定山坡
	 * @throws Exception
	 *  2016年7月18日
	 */
	HashMap<String, Object> queryProductBySessionCPSTATIC(HashMap<String, String> params) throws Exception;
	 
	/**
	 * 获取第三方球场可预订商品列表
	 * @param params
	 * sessionids 子球场ID
	 * productsubtype 商品类型
	 * @return 商品列表（原价，优惠价，图片，remark）
	 * @throws Exception
	 * @author zzy
	 */
	 public HashMap<String, Object> get3rdCourtServiceById(HashMap<String, String> params) throws Exception;
	 
	 /**
	  * 根据subcourtId获取UCourt
	  */
	public UCourt getCourtBysubCourtId(HashMap<String, String> params) throws Exception;
}
