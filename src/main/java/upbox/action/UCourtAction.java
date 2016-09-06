package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ModelDriven;

import upbox.action.OperAction;
import upbox.model.UBrCourt;
import upbox.service.UCourtService;

/**
 * 前端用户action
 * 
 * @author kevinzhang
 *
 */
@Controller("ucourtAction")
@Scope("prototype")
public class UCourtAction extends OperAction implements ModelDriven<UBrCourt> {
	private static final long serialVersionUID = -933464370417457070L;
	
	
	@Resource
	private UCourtService uCourtService;
	private UBrCourt uBrCourt;
	public UBrCourt getuBrCourt() {
		return uBrCourt;
	}

	public void setuBrCourt(UBrCourt uBrCourt) {
		this.uBrCourt = uBrCourt;
	}

	
	
	
	
	private HashMap<String, Object> hashMap;

	/**
	 * 
	 * 
	 TODO - 新建第三方球场
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String addCourtMethod() {
		try {

			
			 hashMap = uCourtService.addCourt(super.getParams(),uBrCourt);
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
	 * 
	 TODO - 我关注过的 -- 球场列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtListSubscribedMethod() {
		try {

			
			 hashMap = uCourtService.queryCourtListSubscribed(super.getParams());
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
	 * 
	 TODO - 我使用过的 -- 球场列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtListOrderedMethod() {
		try {

			
			 hashMap = uCourtService.queryCourtListOrdered(super.getParams());
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
	 * 
	 TODO - 球场列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtListShortMethod() {
		try {
			 hashMap = uCourtService.queryCourtListShort(super.getParams());
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
	 * 
	 TODO - 球场详情
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtDetailMethod() {
		try {

		//	//System.out.println("hello");
			hashMap = uCourtService.queryCourtDetail(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}

	/**
	 * 
	 * 
	 TODO - 球场详情
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String queryCourtDetailIconMethod() {
		try {

		//	//System.out.println("hello");
			hashMap = uCourtService.queryCourtDetailIcon(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}

	/**
	 * 
	 * 
	 TODO - 球场概括
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String queryCourtGeneralMethod() {
		try {

 			hashMap = uCourtService.queryCourtGeneral(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	/**
	 * 
	 * 
	 TODO - 球场相册
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String queryCourtAlbumMethod() {
		try {

 			hashMap = uCourtService.queryCourtAlbum(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	/**
	 * 
	 * TODO 生成订单Check
	 * @return
	 * String
	 * xiao
	 * 2016年2月17日
	 */
	public String saveOrderCheckMethod(){
		try {
			hashMap = uCourtService.saveOrderCheck(super.getParams());
			return returnRet(hashMap, null,"success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UOrderAction:" + e.getMessage());
			return returnRet(null,e);
		}
		
	}
	
	
	/**
	 * 
	 * TODO 生成订单
	 * @return
	 * String
	 * xiao
	 * 2016年2月17日
	 */
	public String saveOrderMethod(){
		try {
			hashMap = uCourtService.saveOrder(super.getParams());
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
	 TODO - 场次预定剩余场次
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionLeftMethod() {
		try {

			hashMap = uCourtService.querySessionLeft(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}

	
	/**
	 * 
	 * 
	 TODO - 场次预定剩余场次
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionLeftDelayOneWeekMethod() {
		try {

			hashMap = uCourtService.querySessionLeftDelayOneWeek(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	/**
	 * 
	 * 
	 TODO - 场次预定-指定球场 查询用户所预定的场次列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionListBySubcourtIdMethod() {
		try {

			hashMap = uCourtService.querySessionListBySubcourtId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	
	/**
	 * 
	 * 
	 TODO - 场次预定-指定球场 查询用户 约战  所预定的场次列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionListAllSubcourtMethod() {
		try {

			hashMap = uCourtService.querySessionListAllSubcourtDuel(super.getParams(),"duel");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	
	/**
	 * 
	 * 
	 TODO - 场次预定- 查询场次订单信息 
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionOrderSubcourtMethod() {
		try {

			hashMap = uCourtService.querySessionOrderSubcourt(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	
	
	
	/**
	 * 
	 * 
	 TODO - 订单信息  查询场次    
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionByOrderIdMethod() {
		try {

			hashMap = uCourtService.querySessionByOrderId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	
	/**
	 * 
	 * 
	 TODO - 场次预定-指定球场 查询用户 挑战  所预定的场次列表
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionListAllSubcourtChalleageMethod() {
		try {

			hashMap = uCourtService.querySessionListAllSubcourtDuel(super.getParams(),"challeage");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	/**
	 * 
	 * 
	 TODO - 场次预定状态按日期查询
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionByDateMethod() {
		try {

			hashMap = uCourtService.querySessionByDate(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	
	
	
	/**
	 * 
	 * 
	 TODO - 场次预定状态按日期查询
	 * 
	 * @return 2015年1月26日 kevinzhang
	 */
	public String querySessionByDateFightMethod() {
		try {

			hashMap = uCourtService.querySessionByDateFight(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap,null,"success");
	}
	
	/**
	 * 
	 * 
	 TODO - 关注球场
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String subscribeCourtMethod() {
		try {

 			hashMap = uCourtService.subscribeCourt(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	
	
	/**
	 * 
	 * 
	 TODO - 球场评分
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String modifyCourtScoreMethod() {
		try {

 			hashMap = uCourtService.modifyCourtScore(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 TODO - 我关注的球场列表
	 * 
	 * @return 2015年3月9日 kevinzhang
	 */
	public String querySubscribedCourtListMethod() {
		try {

			
			 hashMap = uCourtService.querySubscribedCourtList(super.getParams());
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
	 * 
	 TODO - 
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String queryProductBySessionMethod() {
		try {

 			hashMap = uCourtService.queryProductBySession(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	

	/**
	 * 
	 * 
	 TODO - 
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String queryProductBySessionCPSTATICMethod() {
		try {

 			hashMap = uCourtService.queryProductBySessionCPSTATIC(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	
	/**
	 * 
	 * 
	 TODO - 
	 * 
	 * @return 2015年2月29日 kevinzhang
	 */
	public String queryProductTypeBySessionMethod() {
		try {

 			hashMap = uCourtService.queryProductTypeBySession(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		
		return returnRet(hashMap,null,"success");
		
	}
	
	
	/**
	 * 获取第三方球场可预订商品列表
	 */
	public String get3rdCourtService() {
		try {
			hashMap = uCourtService.get3rdCourtServiceById(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UCourtAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
	} 
	
	
	

	@Override
	public UBrCourt getModel() {
		if (null == uBrCourt)
			uBrCourt = new UBrCourt();
		return uBrCourt;
	}

}
