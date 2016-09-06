package upbox.action;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import upbox.model.UMall;
import upbox.model.UOrder;
import upbox.service.UChallengeService;
import upbox.service.UPlayerService;
import upbox.service.URegionService;

/**
 * 挑战action
 * @author yuancao
 *
 */
@Controller("uchAction")
@Scope("prototype")
public class UChallengeAction extends OperAction {
	
	private static final long serialVersionUID = 2081259469715460964L;
	private HashMap<String,Object> hashMap;
	@Resource
	private UChallengeService uchallengeService;
	
	@Resource
	private URegionService uregionService;
	
	@Resource
	private UPlayerService uPlayerService;
	
	private List<UMall> mallList;
	
	
	private List<UOrder> perOrderList;
	/**
	 * 所有的激战列表
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelListMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChListAndBsList(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 关于战队的激战列表
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelWithTeamListMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChListAndBsListWithTeam(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 关于 球员的激战列表
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelWithCourtListMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChListAndBsListWithPlayer(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	/**
	 * 关于用户的激战列表
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelWithUserListMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChListAndBsListWithUser(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 激战详细-概况顶部
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelInfoMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChallengeAndBs(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 激战详细-概况中部
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelInfoBodyMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChallengeAndBsBody(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 激战详细-阵容
	 * @return
	 * @throws Exception 
	 */
	public String getChAndDuelPleyersMethod() throws Exception{
		try
		{
			hashMap = uPlayerService.getUplayerByTeamIds(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 关注或取消挑战
	 * @return
	 * @throws Exception 
	 */
	public String isFollowChMethod() throws Exception{
		try
		{
			return returnRet(uchallengeService.isFollowCh(super.getParams()), null,"success");
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	
	/**
	 * 走一个
	 * @return
	 * @throws Exception 
	 */
	public String startChallengeMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.startChallenge(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 取消
	 * @return
	 * @throws Exception 
	 */
	public String startAgainChallengeMethod() throws Exception{
		try
		{
			
			hashMap =uchallengeService.startAgainChallenge(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 发起方重发详细
	 * @return
	 * @throws Exception
	 */
	public String getAgainChallengeInfoMethod() throws Exception{
		try
		{
			
			hashMap =uchallengeService.getChallengeAgainInfo(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 响应方详细
	 * @return
	 * @throws Exception
	 */
	public String getAgainRespInfoMethod() throws Exception{
		try
		{
			
			hashMap =uchallengeService.getRespAgainInfo(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 判断挑战发起条件
	 * @return
	 * @throws Exception 
	 */
	public String getChVerifyMethod() throws Exception{
		try
		{
			return null;
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
	}
	
	/**
	 * 激战应战生成订单
	 * @return
	 * @throws Exception 
	 */
	public String responseChMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.saveChallengePerOrder(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 判断是否有激战应战资格
	 * @return
	 * @throws Exception 
	 */
	public String checkChallengePerOrderMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.checkChallengePerOrder(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 激战应战后的捎句话和贴个图
	 * @return
	 * @throws Exception 
	 */
	public String responseChMsgImgMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.responseChMsgImg(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	/**
	 * 个人关注大场次列表
	 * @return
	 * @throws Exception 
	 */
	public String getFollowChListMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.getFollowChList(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 个人关注小场次列表
	 * @return
	 * @throws Exception 
	 */
	public String getFollowBsListMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.getFollowChBsList(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 保存子订单球场以及子订单商品 并且关联挑战比赛
	 * @return
	 * @throws Exception 
	 */
	/*public String saveChallengePerOrderMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.saveChallengePerOrder(super.getParams(), xmallList);
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}*/
	
	/**
	 * 定时器调用如果订单未付清除主场次响应方子订单关联
	 * @return
	 * @throws Exception 
	 */
	public String timeChangeChallengePerMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.delPerOrder(super.getParams(),perOrderList);
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 判断此用户是否有发起挑战资格
	 * @return
	 * @throws Exception
	 */
	public String checkChampionMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.checkChampion(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 判断此用户是否有发起挑战资格并带相关参数并跳转详细页（最外面用）
	 * @return
	 * @throws Exception
	 */
	public String checkChampionWithCourtMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.checkChampionWithCourt(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 判断此用户是否有发起挑战资格并跳转详细页（球场下摆下擂台用）
	 * @return
	 * @throws Exception
	 */
	public String checkChampionWithCourtPageMethod() throws Exception{
		try
		{
			hashMap =uchallengeService.checkChampionWithCourtPage(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 球场中的擂主模块接口(擂台)
	 * @return
	 * @throws Exception 
	 */
	public String getChampionInfoWithCourtMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChampionInfoWithCourt(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 获得图片相册接口
	 * @return
	 * @throws Exception
	 */
	public String getChallengePhotoListMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChallengePhotoList(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 根据sessionid获得相应参数
	 * @return
	 * @throws Exception
	 */
	public String getWithCourtInfoMethod() throws Exception{
		try
		{
			/*List<HashMap> sessionList=new ArrayList<>();
			for(int i=0;i<3;i++){
				HashMap a=new HashMap<>();
				a.put("session_id", "6f31aa2d-5838-43aa-9d84-c473a9960ef7");
				sessionList.add(a);
			}
			List<HashMap<String, Object>> abc= uchallengeService.getWithCourtInfo(sessionList);*/
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 查询当前球队的挑战信息
	 * @return
	 * @throws Exception
	 */
	public String checkChallengeWithTeamMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.checkChallengeWithTeam(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 测试相册接口
	 * @return
	 * @throws Exception
	 */
	public String getChallengeOtherInfoMethod() throws Exception{
		try
		{
			hashMap = uchallengeService.getChallengeOtherInfo(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 临时倒码表
	 */
	public void setAreasMethod()throws Exception{
		uregionService.newInitAreas();
	}
	
	

	public List<UMall> getMallList() {
		return mallList;
	}

	public void setMallList(List<UMall> mallList) {
		this.mallList = mallList;
	}

	public List<UOrder> getPerOrderList() {
		return perOrderList;
	}

	public void setPerOrderList(List<UOrder> perOrderList) {
		this.perOrderList = perOrderList;
	}

	
	
	
}
