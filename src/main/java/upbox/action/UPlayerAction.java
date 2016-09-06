package upbox.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import com.org.pub.PublicMethod;
import upbox.model.UPlayer;
import upbox.model.UUser;
import upbox.service.FilterService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerService;

/**
 * 前端用户action
 * @author dengqiuru
 *
 */
@Controller("uPlayerAction")
@Scope("prototype")
public class UPlayerAction extends OperAction implements ModelDriven<UPlayer>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UPlayerService uPlayerService;
	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	@Resource
	private FilterService filterService;
	private UUser uUser;
	private UPlayer uPlayer;
	private HashMap<String,Object> hashMap;
	private List<HashMap<String,Object>> addressBooks;
	
	/**
	 * 
	 * 
	   TODO - 编辑球员信息
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public String editPlayerInfoMethod(){
		try {
			hashMap = uPlayerService.editPlayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 根据球员Id获取球员信息，以及他所在的球队列表
	   @param map
	   @return
	   @throws Exception
	   2016年1月29日
	   dengqiuru
	 */
	public String getUTeamListByPlayerIdMethod(){
		try {
			hashMap = uPlayerService.getUTeamListByPlayerId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

	/**
	 * 
	 * 
	   TODO - 战队详情  队员列表
	   @return
	   2016年1月25日
	   dengqiuru
	 */
	public String listPlayerInfoMethod(){
		try {
			hashMap = uPlayerService.listPlayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员接口   球员列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String getUPlayerListMethod(){
		try {
			hashMap = uPlayerService.getUPlayerList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情  概况
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String getUplayerinfoByPlayerIdMethod(){
		try {
			hashMap = uPlayerService.getUplayerinfoByPlayerId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情  相册列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String getUplayerGalleryListMethod(){
		try {
			hashMap = uPlayerService.getUplayerGalleryList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员详情  编辑相册
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String updateUplayerGalleryMethod(){
		try {
			hashMap = uPlayerService.updateUplayerGallery(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 有球队的球员查询
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String getUPlayerinfoInUteamMethod(){
		try {
			hashMap = uPlayerService.getUPlayerinfoInUteam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 无球队的球员查询
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String getUPlayerinfoOutUteamMethod(){
		try {
			hashMap = uPlayerService.getUPlayerinfoOutUteam(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球员筛选
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String findFilterPlayerMethod(){
		try {
			hashMap = filterService.findFilterPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球员筛选【2.0.2】
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String findFilterPlayer202Method(){
		try {
			hashMap = uPlayerService.findFilterPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球员详情头部
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
	public String roughlyStateOfUPlayerHeadMethod(){
		try {
			hashMap = uPlayerService.roughlyStateOfUPlayerHead(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球员详情  概况  
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String roughlyStateOfUPlayerMethod(){
		try {
			hashMap = uPlayerService.roughlyStateOfUPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 *
	 *
	 TODO - 球员详情 概况 2.0.3
	 @param map
	 @return
	 @throws Exception
	 2016年2月19日
	 xhy
	 */
	public String uPlayerInfoDetail203Method(){
		try {
			hashMap = uPlayerService.uPlayerInfoDetail203(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 *
	 *
	 TODO - 获取球员关于我详情页面
	 @param
	 @return
	 @throws Exception
	 2016年2月19日
	 xhy
	 */
	public String getAboutPlayerInfoMethod(){
		try {
			hashMap = uPlayerService.getAboutPlayerInfo(super.getParams());
		} catch (Exception e) {

			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}



	/**
	 * 
	 * 
	   TODO - 根据多个teamId查询球员信息  
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String getUplayerByTeamIdsMethod(){
		try {
			hashMap = uPlayerService.getUplayerByTeamIds(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}	
	/**
	 * 
	 * 
	   TODO - 球员关注/取消关注
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String followUPlayerMethod(){
		try {
			hashMap = uPlayerService.followUPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 我关注的球员列表
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String myFollowUPlayerMethod(){
		try {
			hashMap = uPlayerService.myFollowUPlayer(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 建队前检测球员信息是否填写完成
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String checkUplayerInfoMethod(){
		try {
			hashMap = uPlayerService.checkUplayerInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 个人中心头部
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String getUTeamforuserIdMethod(){
		try {
			hashMap = uPlayerService.getUTeamforuserId(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 球员分享
	   @return
	   2016年3月9日
	   dengqiuru
	 */
	public String uPlayerinfoShareMethod(){
		try {
			hashMap = uPlayerService.uPlayerinfoShare(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 角色转让时，球员列表
	   @return
	   2016年6月12日
	   dengqiuru
	 */
	public String transferPlayerList202Method(){
		try {
			hashMap = uPlayerService.transferPlayerList202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 系统导入权限值
	   @return
	   2016年6月20日
	   dengqiuru
	 */
	public String setPlayerRoleLimitMethod(){
		try {
			hashMap = uPlayerRoleLimitService.setPlayerRoleLimit(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 设置默认球队
	   @return
	   2016年6月20日
	   dengqiuru
	 */
	public String setDefaultUteam202Method(){
		try {
			hashMap = uPlayerService.setDefaultUteam202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 招募球员List
	   @return
	   2016年6月22日
	   dengqiuru
	 */
	public String recruitPlayerList202Method(){
		try {
			List<Map<String, Object>> addressbooks = PublicMethod.parseJSON2List(super.getParams().get("addressbooks")); 
			hashMap = uPlayerService.recruitPlayerList202(super.getParams(),addressbooks);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	
	}
	/**
	 * 
	 * 
	   TODO - 招募球员发送通知
	   @return
	   2016年6月22日
	   dengqiuru
	 */
	public String recruitPlayer202Method(){
		try {
			hashMap = uPlayerService.recruitPlayer202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	
	}
	/**
	 * 
	 * 
	   TODO - 战队详情--球员列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月21日
	   dengqiuru
	 */
	public String findPlayerListByType202Method(){
		try {
			hashMap = uPlayerService.findPlayerListByType202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * 
	   TODO - 战队详情--官员列表
	   @param map
	   		teamId		球队Id
	   @return
	   @throws Exception
	   2016年6月21日
	   dengqiuru
	 */
	public String findManagerListByType202Method(){
		try {
			hashMap = uPlayerService.findManagerListByType202(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 更新球员信息
	   @return
	   2016年7月18日
	   dengqiuru
	 */
	public String updateUPlayerInfoByEventsMethod(){
		try {
			hashMap = uPlayerService.updateUPlayerInfoByEvents(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 录入球员信息（赛事系统）
	   @return
	   2016年7月19日
	   dengqiuru
	 */
	public String createNewPlayerByEventsMethod(){
		try {
			hashMap = uPlayerService.createNewPlayerByEvents(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}



	/**
	 * TODO - 获取荣誉列表分页
	 * @return
	 */
	public String getPlayerHonerPageMethod(){
		try {
			this.hashMap = this.uPlayerService.playerHonorListPage(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * TODO - 保存球员荣誉墙
	 * @return
	 * @date 2016-07-20
	 */
	public String savePlayerHonorMethod(){
		try {
			this.hashMap = this.uPlayerService.savePlayerHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * TODO - 修改球员荣誉墙
	 * @return
	 * @date 2016-07-20
	 */
	public String updatePlayerHonorMethod(){
		try {
			this.hashMap = this.uPlayerService.updatePlayerHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * TODO - 删除球员荣誉墙
	 * @return
	 * @date 2016-07-20
	 */
	public String deletePlayerHonorMethod(){
		try {
			this.hashMap = this.uPlayerService.deletePlayerHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * TODO - 球员荣誉墙排序方法
	 * @return
	 */
	public String sortPlayerHonorMethod(){
		try {
			this.hashMap = this.uPlayerService.sortPlayerHonor(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * TODO 根据球员ID获取球员信息－修改球员和分配的查询
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String getUPlayerinfoManageMethod(){
		try {
			this.hashMap = this.uPlayerService.getUPlayerinfoManage(super.getParams());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 修改关注objectId
	   @return
	   2016年8月5日
	   dengqiuru
	 */
	public String updateUplayerFollowObjectMethod(){
		try {
			hashMap = uPlayerService.updateUplayerFollowObject(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	/**
	 * 
	 * TODO 修改球员-被修改
	 * @return
	 * xiaoying 2016年8月3日
	 */
	public String editPlayerInfoManageMethod(){
		try {
			this.hashMap = this.uPlayerService.editPlayerInfoManage(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员里程碑
	   @return
	   2016年8月11日
	   dengqiuru
	 */
	public String uplayerBehaviorTypeMethod(){
		try {
			this.hashMap = this.uPlayerService.uplayerBehaviorType(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员里程碑
	   @return
	   2016年8月11日
	   dengqiuru
	 */
	public String getUTeamListInRounghMethod(){
		try {
			this.hashMap = this.uPlayerService.getUTeamListInRoungh(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UPlayerAction:" + e.getMessage());
			return returnRet(null, e);
		}
		return returnRet(hashMap, null, "success");
	}
	
	@Override
	public UPlayer getModel()
	{
		if(null == uPlayer)
			uPlayer = new UPlayer();
		return uPlayer;
	}

	public UUser getUUser()
	{
		return uUser;
	}

	public void setUser(UUser uUser)
	{
		this.uUser = uUser;
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
	
	public UPlayer getuPlayer() {
		return uPlayer;
	}

	public void setuPlayer(UPlayer uPlayer) {
		this.uPlayer = uPlayer;
	}

	public List<HashMap<String, Object>> getAddressBooks() {
		return addressBooks;
	}

	public void setAddressBooks(List<HashMap<String, Object>> addressBooks) {
		this.addressBooks = addressBooks;
	}


}
