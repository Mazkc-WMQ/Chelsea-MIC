package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UUser;
import upbox.model.UUserLog;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UUserLogService;

/**
 * 前端端用户操作记录接口实现类
 *
 */
@Service("uUserLogService")
public class UUserLogServiceImpl implements UUserLogService{
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private PublicService publicService;
	
	/**
	 * 
	 * 
	   TODO - 注册时记录用户操作表
	   @param uUser
	   2015年12月28日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void saveUserLog(UUser uUser,String regiResource) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("userId", uUser.getUserId());
		UUserLog uUserLog = baseDAO.get("from UUserLog where UUser.userId = :userId", hashMap);
		if (null == uUserLog) {//为空时，做操作
			if ("1".equals(regiResource) ) {//更新
				UUserLog uUserLogTemp = new UUserLog();
				uUserLogTemp.setPkId(WebPublicMehod.getUUID());
				uUserLogTemp.setUUser(uUser);
				uUserLogTemp.setCreatedate(new Date());
				uUserLogTemp.setCreatetime(new Date());
				uUserLogTemp.setRegiResource("4");
				baseDAO.save(uUserLogTemp);
			}else if ("2".equals(regiResource)) {//新增
				UUserLog uUserLogTemp = new UUserLog();
				uUserLogTemp.setPkId(WebPublicMehod.getUUID());
				uUserLogTemp.setUUser(uUser);
				uUserLogTemp.setCreatedate(new Date());
				uUserLogTemp.setCreatetime(new Date());
				uUserLogTemp.setFDate(new Date());
				uUserLogTemp.setFTime(new Date());
				uUserLogTemp.setLogindate(new Date());
				uUserLogTemp.setLogintime(new Date());
				uUserLogTemp.setRegiResource("1");
				baseDAO.save(uUserLogTemp);
			}
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 登录时，更新用户操作记录表
	   @param uUser
	   2015年12月28日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public void updateUuserLog(UUser uUser) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("userId", uUser.getUserId());
		UUserLog uUserLog = baseDAO.get("from UUserLog where UUser.userId = :userId", hashMap);
		if (null != uUserLog) {//不为空时，做操作
			uUserLog.setLogindate(new Date());
			uUserLog.setLogintime(new Date());
			baseDAO.update(uUserLog);
		}
	}

}
