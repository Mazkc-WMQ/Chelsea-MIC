package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
//
//import com.upbox.service.CoverFlowService;
//import com.upbox.transmit.RetMsgResult;

import upbox.service.CoverFlowService;

@Controller("coverAction")
@Scope("prototype")
public class CoverFlowAction extends OperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6286989320679644821L;
	/**
	 * 
	 */
	private HashMap<String, Object> hashMap;

	private CoverFlowService coverFlowService;

	public CoverFlowService getCoverFlowService() {
		return coverFlowService;
	}

	@Resource
	public void setCoverFlowService(CoverFlowService coverFlowService) {
		this.coverFlowService = coverFlowService;
	}

	/**
	 * 获取 启动图 COVERFLOW
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAppLaunchCoverFlowMethod() {

		try {
			hashMap = coverFlowService.getAppLaunchCoverFlow(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");

	}
	
	
	/**
	 * 获取 版本更新
	 * 
	 * @return
	 * @throws Exception
	 */
	public String hasNewVersionOrNotMethod() {

		//System.out.println("has action");
		try {
			hashMap = coverFlowService.hasNewVersionOrNotMethod(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");

	}
	
	
	/**
	 * 获取 启动图 COVERFLOW
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAppResourseMethod() {

		try {
			hashMap = coverFlowService.getAppResource(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");

	}

	/**
	 * 
	 * 
	 * 更新启动图 统计信息
	 */

	public String updateLaunchCountInfoMethod() {
		
	//
		try {
			hashMap = coverFlowService.updateLaunchCountInfo(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//

		 
	}

	/**
	 * 获取 动态列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFrontDynamicCoverFlowMethod() {
		//
		try {
			hashMap = coverFlowService.getFrontDynamicCoverFlow(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//


	 
	}

	/**
	 * 获取 赛事列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFrontDynamicCoverFlowMatchMethod() {
		//
		try {
			hashMap = coverFlowService.getFrontDynamicCoverFlowMatch(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//


//		 
	}

	/**
	 * 获取 热点列表
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public String getFrontDynamicCoverFlowHotMethod() {
		//
		try {
			hashMap = coverFlowService.getFrontDynamicCoverFlowHot(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//

	 
	}

	/**
	 * 获取 球场动态动态信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCourtDynamicInfoMethod() {

		//
		try {
			hashMap = coverFlowService.getCourtDynamicInfo(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//

//		try {
//			return super.outDatas(coverFlowService
//					.getCourtDynamicInfo(getParams()));
//		} catch (Exception e) {
//			logger.error("CoverflowAction:" + e.getMessage());
//			return returnRet(null, null, e);
//
//		}
	}

	/**
	 * 获取 首页 焦点图
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFrontPageCoverFlowMethod() {

		//
		try {
			hashMap = coverFlowService
					.getFrontPageCoverFlow(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
		//
//		
//		try {
//			return super.outDatas(coverFlowService
//					.getFrontPageCoverFlow(getParams()));
//		} catch (Exception e) {
//			logger.error("CoverflowAction:" + e.getMessage());
//			return returnRet(null, null, e);
//
//		}
	}

	/**
	 * 获取 球场服务
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCourtServiceListMethod() {
		
		//
		try {
			hashMap =coverFlowService
					.getCourtServiceList(getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CoverflowAction:" + e.getMessage());
			//System.out.println("e" + e);
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
//	
//
//		try {
//			return super.outDatas(coverFlowService
//					.getCourtServiceList(getParams()));
//		} catch (Exception e) {
//			logger.error("CoverflowAction:" + e.getMessage());
//			return returnRet(null, null, e);
//
//		}
	}

}
