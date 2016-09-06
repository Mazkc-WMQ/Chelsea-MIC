package upbox.service;

import java.util.HashMap;

public interface CoverFlowService {

	HashMap<String, Object> getAppLaunchCoverFlow(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> getFrontDynamicCoverFlow(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> getFrontPageCoverFlow(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> getCourtServiceList(HashMap<String, String> params)throws Exception;

	HashMap<String, Object> getCourtDynamicInfo(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> updateLaunchCountInfo(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> getFrontDynamicCoverFlowMatch(HashMap<String, String> params)throws Exception;

	HashMap<String, Object> getFrontDynamicCoverFlowHot(HashMap<String, String> params)throws Exception;

	HashMap<String, Object> getAppResource(HashMap<String, String> params) throws Exception;

	HashMap<String, Object> hasNewVersionOrNotMethod(
			HashMap<String, String> params) throws Exception;

}
