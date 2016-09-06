package upbox.action;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.org.dao.BaseDAO;
import com.org.pub.SerializeUtil;

import upbox.model.UDuel;
import upbox.model.UDuelChallengeImg;
import upbox.service.UDuelService;
import upbox.service.UOrderService;

/**
 * 约战Action
 * @author wmq
 *
 * 15618777630
 */
@Controller("uduelAction")
@Scope("prototype")
public class UDuelAction extends OperAction{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UDuelService uduelServiceImpl;
	private UDuel UDuel;
	private HashMap<String,Object> hashMap;
	private List<String> duelTeamList; //约战指定对手
	@Resource
	private UOrderService uorderService;
	
	/**
	 * 
	 * 
	   TODO - 我的约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String findMyDuelListMethod(){
		try {
			hashMap = uduelServiceImpl.findMyDuelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 是否可以补款
	   @return
	   2015年12月16日
	   wmq
	 */
	public String checkDuelBKMethod(){
		try {
			hashMap = uduelServiceImpl.checkDuelBK(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 判断是否发起过约战
	   @return
	   2015年12月16日
	   wmq
	 */
	public String duelFristMethod(){
		try {
			hashMap = uduelServiceImpl.duelFrist(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 约战分享事件数据
	   @return
	   2016年4月13日
	   mazkc
	 */
	public String getDuelShareInfoMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelShareInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球队约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String findTeamDuelListMethod(){
		try {
			hashMap = uduelServiceImpl.findTeamDuelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 球员约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String findPlayerDuelListMethod(){
		try {
			hashMap = uduelServiceImpl.findPlayerDuelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 所有约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String findAllDuelListMethod(){
		try {
			hashMap = uduelServiceImpl.findAllDuelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 约战过的对手列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelTeamListMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelTeamList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 发起约战-走一个
	   @return
	   2015年12月16日
	   wmq
	 */
	public String addDuelOrderMethod(){
		try {
			HashMap<String,String> hash = super.getParams();
			UDuelChallengeImg umi = new UDuelChallengeImg();
			List<UDuelChallengeImg> duelImgList = (List<UDuelChallengeImg>) SerializeUtil.unSerializeToList(hash.get("duelImgList"),UDuelChallengeImg.class);
			duelTeamList = null == hash.get("duelTeamList") ? null : (List<String>) JSONArray.toCollection(JSONArray.fromObject(hash.get("duelTeamList")));
			hashMap = uduelServiceImpl.addDuelOrder(hash,duelTeamList,duelImgList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 关注约战
	   @return
	   2015年12月16日
	   wmq
	 */
	public String followDuelMethod(){
		try {
			hashMap = uduelServiceImpl.followDuel(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 取消关注约战
	   @return
	   2015年12月16日
	   wmq
	 */
	public String delFollowDuelMethod(){
		try {
			hashMap = uduelServiceImpl.delFollowDuel(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 查看我关注的约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String myFollowDuelListMethod(){
		try {
			hashMap = uduelServiceImpl.myFollowDuelList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 查看激战详情
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelInfoMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 查看激战详情-概况
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelGKInfoMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelGKInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 
	 * 
	   TODO - 响应约战
	   @return
	   2015年12月16日
	   wmq
	 */
	public String respDuelMethod(){
		try {
			hashMap = uduelServiceImpl.respDuel(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 查询约战小场次列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelSListMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelSList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 约战取消
	   @return
	   2015年12月16日
	   wmq
	 */
	public String addDuelAgainMethod(){
		try {
			hashMap = uduelServiceImpl.addDuelAgain(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

	/**
	 * 
	 * 
	   TODO - 查询重发后的前约战信息
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelAgainInfoMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelAgainInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	

	/**
	 * 
	 * 
	   TODO - 获取发起成功约战列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelSuccessListMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelSuccessList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -验证响应状态
	   @return
	   2015年12月16日
	   wmq
	 */
	public String checkRespDuelStatusMethod(){
		try {
			hashMap = uduelServiceImpl.checkRespDuelStatus(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 查看约战相册列表
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelImgListMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelImgList(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO - 响应方响应约战 贴个图/捎句话
	   @return
	   2015年12月16日
	   wmq
	 */
	public String addRemarkImgDuelMethod(){
		try {
			HashMap<String,String> hash = super.getParams();
			List<UDuelChallengeImg> duelImgList = (List<UDuelChallengeImg>) SerializeUtil.unSerializeToList(hash.get("duelImgList"),UDuelChallengeImg.class);
			hashMap = uduelServiceImpl.addRemarkImgDuel(hash,duelImgList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -  响应方约战详情
	   @return
	   2015年12月16日
	   wmq
	 */
	public String getDuelRespInfoMethod(){
		try {
			hashMap = uduelServiceImpl.getDuelRespInfo(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 
	 * 
	   TODO -  判断约战是否可发起
	   @return
	   2015年12月16日
	   wmq
	 */
	public String checkAddDuelStatusMethod(){
		try {
			hashMap = uduelServiceImpl.checkAddDuelStatus(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UDuelAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * @return the uDuel
	 */
	public UDuel getUDuel() {
		return UDuel;
	}

	/**
	 * @param uDuel the uDuel to set
	 */
	public void setUDuel(UDuel uDuel) {
		UDuel = uDuel;
	}

	/**
	 * @return the duelTeamList
	 */
	public List<String> getDuelTeamList() {
		return duelTeamList;
	}

	/**
	 * @param duelTeamList the duelTeamList to set
	 */
	public void setDuelTeamList(List<String> duelTeamList) {
		this.duelTeamList = duelTeamList;
	}
}
