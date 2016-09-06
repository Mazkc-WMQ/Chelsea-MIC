package upbox.action;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import com.org.pub.MessageUtils;
import com.org.pub.PublicMethod;

import net.sf.json.JSONObject;
import upbox.model.UPlayer;
import upbox.model.UUser;
import upbox.outModel.ReturnWxPayXml;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicService;
import upbox.service.RankingListService;
import upbox.service.UCourtService;
import upbox.service.UEquipmentService;
import upbox.service.UOrderService;
import upbox.service.UParameterService;
import upbox.service.UPayService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.UTouristService;
import upbox.service.UUserService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("interceptorAction")
@Scope("prototype")
public class InterceptorAction extends OperAction implements ModelDriven<UUser>{
	/**
	 * 
	   TODO - 
	   InterceptorAction.java
	   long
	   2016年3月30日
	   mazkc 
	 */
	private static final long serialVersionUID = 4156184004119599241L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UUserService userService;
	@Resource
	private UEquipmentService uEquipmentService;
	@Resource
	private UTouristService uTouristService;
	@Resource
	private UOrderService uOrderService;
	@Resource
	private UParameterService uParameterService;
	private UUser uUser;
	private UPlayer uPlayer;
	private HashMap<String,Object> hashMap;
	private String msg;
	@Resource
	private UPayService upayService;
	@Resource
	private MessageService mesService;
	@Resource
	private LBSService lbsService;
	@Resource
	private PublicService publicService;
	@Resource
	private RankingListService rankingListService;
	@Resource
	private UPlayerService uPlayerService;
	@Resource
	private UCourtService uCourtService;
	@Resource
	private UPlayerRoleService uPlayerRoleService;
	
//	/**
//	 * 
//	 * 
//	   TODO - 获取验证码 【2.0.0.1】
//	   @return
//	   2015年12月16日
//	   dengqiuru
//	 */
//	public String getBaidulbsMethod(){
//		try {
//			hashMap = userService.getBaidulbs(super.getParams());
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("UUserAction:" + e.getMessage());
//			return returnRet(null,e);
//		}
//		return returnRet(hashMap, null,"success");
//	}//	
	/**
//	 * 
//	 * 
//	   TODO - 获取验证码 【2.0.0.1】
//	   @return
//	   2015年12月16日
//	   dengqiuru
//	 */
	public String updateMemberType202Method(){
		try {
			hashMap = uPlayerService.updateMemberType202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 获取验证码 【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String getVerCodeMethod(){
		try {
			hashMap = userService.getVerCodeMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 注册【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String registerMethod(){
		try {
			hashMap = userService.registerMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

	/**
	 * 
	 * 
	   TODO - 忘记密码【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String getPasswordMethod(){
		try {
			hashMap = userService.getPasswordMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 用户没登录、没注册获取用户设备信息
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @throws Exception
	   2016年3月16日
	   dengqiuru
	 */
	public String insertEquipmentMethod(){
		try {
			uEquipmentService.insertEquipment(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(null, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 第三方登录【2.0.0.1】
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String loginAuthMethod(){
		try {
			hashMap = userService.loginAuthMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 第三方登录关联【2.0.0.1】
	   @return
	   2015年12月22日
	   dengqiuru
	 */
	public String loginAuthTiedMethod(){
		try {
			hashMap = userService.loginAuthTiedMethod(super.getParams(),uUser);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 登录【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String loginMethod(){
		try {
			hashMap = userService.loginMethod(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

	/**
	 * 
	 * 
	   TODO - 游客登录
	   @return
	   2015年12月9日
	   dengqiuru
	 */
	public String touristLoginMethod(){
		try {
			hashMap = uTouristService.touristLogin(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTouristAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 已过期
	   @return
	   2015年12月29日
	   dengqiuru
	 */
	public String uOrderExpiredMethod(){
		try {
			hashMap = uOrderService.uOrderExpired(super.getParams());//已过期
			uOrderService.uOrderAboutExpired(super.getParams());//即将过期
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 注册后，填写基本信息
	   @return
	   2016年3月4日
	   dengqiuru
	 */
	public String insertUserinfoMethod(){
		try {
			hashMap = userService.insertUserinfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UUserAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 获取参数明细
	   @param uParameterList
	   		param的uParameterList
	   @return
	   		
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	public String getUParameterInfoListMethod(){
		try {
			List<Map<String, Object>> uParameterList = PublicMethod.parseJSON2List(super.getParams().get("uParameterList")); 
			hashMap = uParameterService.getUParameterInfoList(uParameterList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 获取参数明细
	   @param uParameterList
	   		param的uParameterList
	   @return
	   		
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	public String updateBehaviorMethod(){
		try {
			msg = publicService.updateBehavior(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamImgAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 微信支付异步回调地址 APP
	 * @return
	 * @throws Exception 
	 */
	public String payWxCallBackMethod() throws Exception{
		Map<String, String> requestMap = MessageUtils.parseXml_Stream(request);
		try
		{
			if(upayService.appWxApiCallBack(requestMap).equals("success")){
				ReturnWxPayXml wxXml=new ReturnWxPayXml();
				String xmlStr="";
				wxXml.setReturn_code("SUCCESS");
				wxXml.setReturn_msg("OK");
				xmlStr=WebPublicMehod.textMessageToXml(wxXml).replaceAll("__", "_");
				return returnRet(xmlStr);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet("error");
	}

	/**
	 * 支付宝支付异步回调地址 APP
	 * @return
	 * @throws Exception 
	 */
	public String payAliCallBackMethod() throws Exception{
		try
		{
			if(upayService.appAliPayCallBack(super.getParams()).equals("success")){
				return returnRet("success");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		} 
		return returnRet("error");
	}
	
	/**
	 * applePay人工回调地址 APP
	 * @return
	 * @throws Exception 
	 */
	public String payApplePayCallBackMethod() throws Exception{
		InputStream is = null;
		DataInputStream input = null;
		try
		{
			int totalbytes = request.getContentLength();
			byte[] dataOrigin = new byte[totalbytes];
			is = request.getInputStream(); 
			input = new DataInputStream(is); 
			input.readFully(dataOrigin);
			String str=new String(dataOrigin);
			JSONObject jasonObject = JSONObject.fromObject(str);
			return returnRet(upayService.appApplePayPayCallBack(jasonObject));
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.error("PayAction:" + e.getMessage());
			return returnRet(null,e);
		}finally{
			if(is!=null){
				is.close();
			}
			if(input!=null){
				input.close();
			}
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 激战无人响应的推送(五天以外以内)【APP 2.0.0】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String fightingPushByNoResponseMethod(){
		try {
			mesService.fightingPushByNoResponse(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 订单提醒到场推送【APP 2.0.0】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String orderPushByRemindReachMethod(){
		try {
			mesService.orderPushByRemindReach(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 订单未约战推送【APP 2.0.0】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String orderPushByNoSponsorFightMethod(){
		try {
			mesService.orderPushByNoSponsorFight(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 给队内所有人发推送【APP 2.0.0】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String pushFightToPlayerOnTeamByTypeMethod(){
		try {
			mesService.pushFightToPlayerOnTeamByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 给队内所有人生成应用内通知【APP 2.0.0.1】
	   @return
	   2016年3月16日
	   charlesbin
	 */
	public String addMoreMessageByTypeMethod(){
		try {
			mesService.addMoreMessageByType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 更新排名【APP 2.0.0】
	   @return
	   2016年3月1日
	   charlesbin
	 */
	public String updateRankByListMethod(){
		try {
			rankingListService.updateRankByList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("rankingListAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 给APP老用户推：发布新的版本【APP 2.0.1】
	   @return
	   2016年5月18日
	   charlesbin
	 */
	public String pushOldUserByNewVersionsMethod(){
		try {
			mesService.pushOldUserByNewVersions(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 给APP所有用户且没有补全球员信息推：补全球员信息【APP 2.0.1】
	   @return
	   2016年5月19日
	   charlesbin
	 */
	public String pushUserByRepairPlayerInfoMethod(){
		try {
			mesService.pushUserByRepairPlayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 给APP所有用户且没有补全球队信息推：补全并约战【APP 2.0.1】
	   @return
	   2016年5月19日
	   charlesbin
	 */
	public String pushUserByRepairTeamInfoMethod(){
		try {
			mesService.pushUserByRepairTeamInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	/**
	 * 
	 * 
	   TODO - 给APP所有用户且没有补全球队球员信息推：补全球队球员信息
	   @return
	   2016年5月19日
	   charlesbin
	 */
	public String pushUserByRepairTeamInfoAndPlayerInfoMethod(){
		try {
			mesService.pushUserByRepairTeamInfoAndPlayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 验证登陆区域位置
	   @return
	   2016年6月13日
	   mazkc
	 */
	public String poiToAdressMethod() {
		try {
			 hashMap = lbsService.poiToAdress(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LBSAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	/**
	 * 
	 * 
	 TODO - 球场列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtListMethod() {
		try {

			
			 hashMap = uCourtService.queryCourtList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			//System.out.println("e"+e);
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
//		return returnRet(hashMap, hashMap);
	}
	
	/**
	 * 
	 * @TODO 夏不为利，短信推送
	 * @Title: pushUserByAgeMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年7月30日 下午5:39:27
	 */
	public String pushUserByAgeMethod(){
		try {
			mesService.pushUserByAge(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 批量处理没有审核的申请数据
	 * @return
	 * xiaoying 2016年8月24日
	 */
	public String batchApplyRefusePlayerMemberTypeMethod(){
		try {
			hashMap = uPlayerRoleService.batchApplyRefusePlayerMemberType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerRoleAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * @return the uUser
	 */
	public UUser getuUser() {
		return uUser;
	}
	/**
	 * @param uUser the uUser to set
	 */
	public void setuUser(UUser uUser) {
		this.uUser = uUser;
	}
	/**
	 * @return the uPlayer
	 */
	public UPlayer getuPlayer() {
		return uPlayer;
	}
	/**
	 * @param uPlayer the uPlayer to set
	 */
	public void setuPlayer(UPlayer uPlayer) {
		this.uPlayer = uPlayer;
	}
	/**
	 * @return the hashMap
	 */
	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}
	/**
	 * @param hashMap the hashMap to set
	 */
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public UUser getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
