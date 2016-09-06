package upbox.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UFollow;
import upbox.service.PublicService;
import upbox.service.UFollowService;

@Service("uFollowService")
public class UFollowServiceImpl implements UFollowService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	/**
	 * 
	 * 
	   TODO - 判断是否关注过对应球员 【2.0.0】
	   @param map
	   		loginUserId		登录人
	   		objectId		对应事件的Id
	   @return
	   		关注状态   1：已关注，2：未关注
	   @throws Exception
	   2016年3月21日
	   dengqiuru
	 */
	@Override
	public String isFollow(HashMap<String, String> map) throws Exception {
		String isFollow = "2";
		UFollow uFollow = new  UFollow();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uFollow = baseDAO.get(map, "from UFollow where UUser.userId=:loginUserId and objectId=:objectId and user_follow_type=:userFollowType and follow_status='1'");
			if (null != uFollow) {
				isFollow = "1";
			}
		}
		return isFollow;
	}

}
