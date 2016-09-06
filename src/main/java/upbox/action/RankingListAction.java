package upbox.action;

import java.util.HashMap;

import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import com.org.dao.BaseDAO;
import upbox.model.UTeam;
import upbox.service.RankingListService;
import upbox.service.UTeamService;

/**
 * 
 * @TODO 天梯action
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午4:45:20 
 * @version 1.0
 */
@Controller("rankingListAction")
@Scope("prototype")
public class RankingListAction extends OperAction implements ModelDriven<UTeam>{
	private static final long serialVersionUID = -933464370417457070L;
	@Resource
	private BaseDAO baseDAO;
	@Resource
	private UTeamService uTeamService;
	@Resource
	private RankingListService rankingListService;
	
	private UTeam uTeam;
	private HashMap<String,Object> hashMap;
	
	/**
	 * 
	 * 
	   TODO - 天梯排行总榜接口【APP 2.0.0.1】
	   @return
	   2016年3月1日
	   charlesbin
	 */
	public String getRankingListMethod(){
		try {
			hashMap = rankingListService.getRankingList(super.getParams());
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
	   TODO - 天梯搜索接口【APP 2.0.0.1】
	   @return
	   2016年3月2日
	   charlesbin
	 */
	public String getRankingSearchMethod(){
		try {
			hashMap = rankingListService.getRankingSearch(super.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("rankingListAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}

	@Override
	public UTeam getModel() {
		if(null == uTeam)
			uTeam = new UTeam();
		return uTeam;
	}
	
}
