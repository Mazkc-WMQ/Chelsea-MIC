package upbox.action;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import upbox.model.RetMsgResult; 
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.service.UUserService;

import com.org.action.IocAction;

/**
 * 所有action都统一继承此类
 * 
 * @author mazkc
 *
 */
public class OperAction extends IocAction {
	private static final long serialVersionUID = -7529134383201096913L;
	protected List jsonData;
	public final String DATA = "datas";
	public RetMsgResult retmsgresult = new RetMsgResult();
	public final Logger logger = Logger.getLogger(OperAction.class);
	private Object ajaxJson;
	@Resource
	private UUserService uuserService;

	public String outDatas(RetMsgResult ret)
	{
		if (ret != null)
		{
			retmsgresult = ret;
		}
		return DATA;
	}

	public String returnRet(Object result,Object... obj)
	{
		if (null != obj && obj.length > 0)
		{
			Exception e = (Exception) obj[0];
			if(null != e){
				String[] str = e.getMessage().split("_");
				if (str.length > 1)
				{
					retmsgresult.setOperFail(str[1], Integer.parseInt(str[0]),checkObj(obj,1));
				} else
				{
					retmsgresult.setOperFail(null, -1, checkErrorMsg(e.getMessage()));
				}
			} else
			{
				retmsgresult.setOperSuccess(null == result ? "" : result, 1,checkObj(obj,1));
			}
		}
		return outDatas(retmsgresult);
	}
	
	/**
	 * 
	 * 
	   TODO - 判断errorMsg
	   @param e
	   @return
	   2016年7月5日
	   mazkc
	 */
	private String checkErrorMsg(String e){
		if(WebPublicMehod.isEnglish(e)){
			return "系统错误，请稍后重试！";
		}else{
			return e;
		}
	}
	
	private Object checkObj(Object[] obj,int index){
		if(obj.length > index){
			return obj[index];
		}
		return null;
	}

	public String getRealPath()
	{
		return request.getSession().getServletContext().getRealPath("/");
	}

	/**
	 * 获取session存储信息
	 * 
	 * @param type
	 * @return
	 */
	public Object getSessionInfo(String type)
	{
		return session.get(type);
	}

	public HashMap<String,String> getParams()
	{
		HashMap<String,String> hashMap = super.getParams();
		UUser user = null;
		String page = hashMap.get("page");
		try {
			//获取接口名称
			String actionMethod = request.getServletPath();
//			System.out.println("actionMethod==============="+actionMethod);
			
			user = uuserService.getUserinfoByToken(hashMap);
			if(null != user)
				hashMap.put("loginUserId", user.getUserId());
			else
				hashMap.put("loginUserId", "");
			if(null == page || "".equals(page)){
				page = "0";
			}
			hashMap.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(request);
		return hashMap;
	}
	
	public String returnRet(Object result)
	{
		this.ajaxJson = result;
		return "ajaxJson";
	}

	/**
	 * @return the ajaxJson
	 */
	public Object getAjaxJson() {
		return ajaxJson;
	}

	/**
	 * @param ajaxJson the ajaxJson to set
	 */
	public void setAjaxJson(Object ajaxJson) {
		this.ajaxJson = ajaxJson;
	}
}
