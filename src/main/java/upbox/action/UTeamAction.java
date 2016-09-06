package upbox.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.org.dao.BaseDAO;
import com.org.pub.PublicMethod;

import upbox.model.UTeam;
import upbox.service.FilterService;
import upbox.service.UTeamService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uTeamAction")
@Scope("prototype")
public class UTeamAction extends OperAction{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UTeamService uTeamService;
	@Resource
	private FilterService filterService;
	private UTeam uTeam;
	private HashMap<String, Object> filterMap;// 战队筛选
	private HashMap<String,Object> hashMap;
	private HashMap<String,List<Object>> mapList;//约过的对手
	private List<HashMap<String,Object>> uteamList;//测试
	private List<Map<String,Object>> playerList;//测试
	
	/**
	 * 
	 * 
	   TODO - 战队详情 头部【2.0.0.1】
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String getHeadUteaminfoMethod(){
		try {
			hashMap = uTeamService.findPlayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 建立球队【2.0.0.1】
	   @return
	   2016年1月27日
	   dengqiuru
	 */
	public String insertNewTeamMethod(){
		try {
			hashMap = uTeamService.insertNewTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球队列表【2.0.0.1】
	   @return
	   2016年1月28日
	   dengqiuru
	 */
	public String getUteamListMethod(){
		try {
			hashMap = uTeamService.getUteamList(super.getParams(),mapList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 加入球队【2.0.0.1】
	   @return
	   2016年1月28日
	   dengqiuru
	 */
	public String joinInTeamMethod(){
		try {
			hashMap = uTeamService.joinInTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 我的球队信息【2.0.0.1】
	   @return
	   2016年2月17日
	   dengqiuru
	 */
	public String myTeamInfoMethod(){
		try {
			hashMap = uTeamService.myTeamInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 队长踢人【2.0.0.1】
	   @return
	   2016年2月18日
	   dengqiuru
	 */
	public String excludeByTeamLeaderMethod(){
		try {
			hashMap = uTeamService.excludeByTeamLeader(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 退出队伍【2.0.0.1】
	   @return
	   2016年2月18日
	   dengqiuru
	 */
	public String exitTeamByUserIdMethod(){
		try {
			hashMap = uTeamService.exitTeamByUserId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 解散球队【2.0.0.1】
	   @return
	   2016年3月7日
	   dengqiuru
	 */
	public String disbandOfUTeamMethod(){
		try {
			hashMap = uTeamService.disbandOfUTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 战队详情--概况【2.0.0.1】
	   @return
	   2016年3月3日
	   dengqiuru
	 */
	public String roughlyStateOfUteamMethod(){
		try {
			hashMap = uTeamService.roughlyStateOfUteam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 战队详情--头部【2.0.0.1】
	   @return
	   2016年3月3日
	   dengqiuru
	 */
	public String findUteaminfoHeadMethod(){
		try {
			hashMap = uTeamService.findUteaminfoHead(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 关注/取消关注球队
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String followUTeamMethod(){
		try {
			hashMap = uTeamService.followUTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 我关注的球队
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String myFollowUTeamMethod(){
		try {
			hashMap = uTeamService.myFollowUTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改球队信息
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String updateUTeaminfoMethod(){
		try {
			hashMap = uTeamService.updateUTeaminfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 战队筛选【2.0.0.1】
	   @return
	   2016年3月3日
	   dengqiuru
	 */
	public String findFilterTeamMethod(){
		try {
			hashMap = filterService.findFilterTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 战队筛选【2.0.0.2】
	   @return
	   2016年3月3日
	   dengqiuru
	 */
	public String findFilterTeam202Method(){
		try {
			hashMap = uTeamService.findFilterTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 邀请
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String invitePlayerMethod(){
		try {
			hashMap = uTeamService.invitePlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 对手
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String competitorofuteamMethod(){
		try {
			hashMap = uTeamService.competitorOfUteam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 赛事对手
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String eventCompetitorMethod(){
		try {
			hashMap = uTeamService.eventCompetitorOfUteam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 推荐的对手
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String getrecommendTeamMethod(){
		try {
			hashMap = uTeamService.getrecommendTeam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 天梯头部
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String findUteamListHeadMethod(){
		try {
			hashMap = uTeamService.findUteamListHead(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球队分享
	   @return
	   2016年3月24日
	   dengqiuru
	 */
	public String uTeaminfoShareMethod(){
		try {
			hashMap = uTeamService.uTeaminfoShare(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 约战更多对手
	   @return
	   2016年4月30日
	   dengqiuru
	 */
	public String addMoreCompetitorMethod(){
		try {
			hashMap = uTeamService.addMoreCompetitor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 检测球队是否正常
	   @return
	   2016年5月1日
	   dengqiuru
	 */
	public String checkUTeamStatusMethod(){
		try {
			hashMap = uTeamService.checkUTeamStatus(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 编辑球队信息  安卓
	   @return
	   2016年5月1日
	   dengqiuru
	 */
	public String updateUTeaminfoAndroidMethod(){
		try {
			hashMap = uTeamService.updateUTeaminfoAndroid(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 我的默认球队列表
	   @return
	   2016年6月20日
	   dengqiuru
	 */
	public String myDefaultTeamInfo202Method(){
		try {
			hashMap = uTeamService.myDefaultTeamInfo202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 我可以约战的球队
	   @return
	   2016年6月20日
	   dengqiuru
	 */
	public String myDuelUteam202Method(){
		try {
			hashMap = uTeamService.myDuelUteam202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 我可以挑战的球队
	   @return
	   2016年6月20日
	   dengqiuru
	 */
	public String myChallengeUteamList202Method(){
		try {
			hashMap = uTeamService.myChallengeUteamList202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

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
	   http://localhost:8080/Chelsea-MIC/uTeam_getUteamRoleByUserId.do?userStatus=1&token=581c9eef-fec0-4342-a0fe-15b02f3442cf&userId=03009df2-f963-4c65-85ac-1d6531d39e0e&type=6
	 */
	public String getUteamRoleByUserIdMethod(){
		try {
			uteamList = uTeamService.getUteamRoleByUserId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(uteamList, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改球队信息（赛事系统）
	   @return
	   2016年7月18日
	   dengqiuru
	 */
	public String updateUteamInfoByEventsMethod(){
		try {
			hashMap = uTeamService.updateUteamInfoByEvents(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 新增球队信息（赛事系统）
	   @return
	   2016年7月20日
	   dengqiuru
	 */
	public String entryUteaminfoMethod(){
		try {
			List<Map<String, Object>> playerList = PublicMethod.parseJSON2List(super.getParams().get("eventsPlayerList"));
			hashMap = uTeamService.entryUteaminfo(super.getParams(),playerList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO -战队荣誉墙列表
	 * @Title: getUteamHonorMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月12日 下午3:37:56
	 */
	public String getUteamHonorMethod(){
		try {
			hashMap = uTeamService.getUteamHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO -新增战队荣誉墙
	 * @Title: insUTeamHonorMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月12日 下午3:37:56
	 */
	public String insUTeamHonorMethod(){
		try {
			hashMap = uTeamService.insUTeamHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO -修改战队荣誉墙
	 * @Title: updUteamHonorMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月12日 下午3:37:56
	 */
	public String updUteamHonorMethod(){
		try {
			hashMap = uTeamService.updUteamHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO - 删除战队荣誉墙
	 * @Title: delUteamHonorMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月12日 下午3:37:56
	 */
	public String delUteamHonorMethod(){
		try {
			hashMap = uTeamService.delUteamHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO - 战队荣誉墙排序
	 * @Title: sortTeamHonorMethod 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月12日 下午3:37:56
	 */
	public String sortTeamHonorMethod(){
		try {
			hashMap = uTeamService.sortTeamHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO 关于球队LIST
	 * @Title: getAboutTeamList 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月18日 下午2:40:48
	 */
	public String getAboutTeamListMethod(){
		try {
			hashMap = uTeamService.getAboutTeamList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * @TODO 球队里程碑LIST
	 * @Title: getUTeamBehaviorList 
	 * @return
	 * @author charlesbin
	 * @date 2016年8月18日 下午2:40:48
	 */
	public String getUTeamBehaviorListMethod(){
		try {
			hashMap = uTeamService.getUTeamBehaviorList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UTeamAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
//	@Override
//	public UTeam getModel() {
//		if(null == uTeam)
//			uTeam = new UTeam();
//		return uTeam;
//	}
//	
	public UTeam getuTeam() {
		return uTeam;
	}

	public void setuTeam(UTeam uTeam) {
		this.uTeam = uTeam;
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
	public HashMap<String, Object> getFilterMap() {
		return filterMap;
	}
	public void setFilterMap(HashMap<String, Object> filterMap) {
		this.filterMap = filterMap;
	}
	public HashMap<String, List<Object>> getMapList() {
		return mapList;
	}
	public void setMapList(HashMap<String, List<Object>> mapList) {
		this.mapList = mapList;
	}
	public List<HashMap<String, Object>> getUteamList() {
		return uteamList;
	}
	public void setUteamList(List<HashMap<String, Object>> uteamList) {
		this.uteamList = uteamList;
	}
	public List<Map<String, Object>> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(List<Map<String, Object>> playerList) {
		this.playerList = playerList;
	}
	
}
