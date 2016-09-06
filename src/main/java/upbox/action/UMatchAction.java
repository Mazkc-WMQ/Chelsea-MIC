package upbox.action;


import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ModelDriven;
import upbox.model.UMatchBs;
import upbox.service.UMatchService;

/**
 * 赛事action
 * @author yuancao
 *
 */
@Controller("umatchAction")
@Scope("prototype")
public class UMatchAction extends OperAction implements ModelDriven<UMatchBs>{
	
	private static final long serialVersionUID = 876473541268205156L;
	
	private HashMap<String,Object> hashMap;
	private UMatchBs umatchBs;
	
	@Resource
	private UMatchService umatchService;

	
	/**
	 * 关于球队的赛事列表
	 * @return
	 * @throws Exception 
	 */
	public String getUMatchBsListWithTeamMethod() throws Exception{
		try
		{
			hashMap = umatchService.getUMatchBsListWithTeam(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 关于球员的赛事列表
	 * @return
	 * @throws Exception 
	 */
	public String getUMatchBsListWithPlayerMethod() throws Exception{
		try
		{
			hashMap = umatchService.getUMatchBsListWithPlayer(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 赛事详细页头部
	 * @return
	 * @throws Exception 
	 */
	public String getUMatchBsTopMethod() throws Exception{
		try
		{
			hashMap = umatchService.getUMatchBs(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 赛事详细页中部
	 * @return
	 * @throws Exception 
	 */
	public String getUMatchBsBodyMethod() throws Exception{
		try
		{
			hashMap = umatchService.getUMatchBsBody(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 赛事的关注和取消关注
	 * @return
	 * @throws Exception 
	 */
	public String isFollowMatchMethod() throws Exception{
		try
		{
			hashMap = umatchService.isFollowMatch(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 我关注的赛事列表
	 * @return
	 * @throws Exception 
	 */
	public String getUMatchBsListWithFollowMethod() throws Exception{
		try
		{
			hashMap = umatchService.getUMatchBsListWithFollow(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	/**
	 * 得到第三方的大赛时列表
	 * @return
	 * @throws Exception
	 */
	public String getOtherUMatchBsListMethod() throws Exception{
		try
		{
			hashMap = umatchService.getOtherMatchAllList(super.getParams());
		} catch (Exception e)
		{
			logger.error("UChAction:" + e.getMessage());
			return returnRet(null,e);
		}
		return returnRet(hashMap, null,"success");
	}
	
	
	@Override
	public UMatchBs getModel()
	{
		if(null == umatchBs){
			umatchBs = new UMatchBs();
		}
		return umatchBs;
	}

	
	
	
	
}
