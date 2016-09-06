package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.ULaunchCoverFlowCount;
import upbox.pub.WebPublicMehod;
import upbox.service.CoverFlowService;
/*
import com.upbox.bean.ULaunchCoverFlowCount;
import com.upbox.service.CoverFlowService;
import com.upbox.transmit.RetMsgResult;
import com.upbox.util.DateUtil;
import com.upbox.util.RandNum;

*/

@Service("coverFlowService")
public class CoverFlowServiceImpl  implements CoverFlowService {
	
	@Resource
	private OperDAOImpl baseDAO;

	
	@Override
	public HashMap<String, Object> updateLaunchCountInfo(HashMap<String, String> params)
			throws Exception {
		// TODO Auto-generated method stub
		
		String id = WebPublicMehod.getUUID();
		Integer launchid =   params.get("launchid")!=null?new Integer(params.get("launchid").toString()):-1;
		String requestfrom = params.get("requestfrom")!=null?params.get("requestfrom").toString():null;
		//
 
		String createdate = PublicMethod.getDateToString(new Date(), "yyyy-MM-dd");
		String act = params.get("act")!=null?params.get("act").toString():null;
		Long times = 0L;
	 
		
		HashMap<String, Object> queryHashMap =new HashMap<String, Object>();
	    queryHashMap.put("launchid", launchid);
	    queryHashMap.put("requestfrom", requestfrom);
	    queryHashMap.put("createdate", createdate);
	    queryHashMap.put("act", act);
	 
	    //
	    ULaunchCoverFlowCount launchCoverFlowCount ;
	    
	    String hql = "from ULaunchCoverFlowCount where launchid = :launchid and requestfrom = :requestfrom and createdate = :createdate and act = :act ";
	    
	    launchCoverFlowCount = baseDAO.get(hql, queryHashMap);
	    
	    if (launchCoverFlowCount==null||launchCoverFlowCount.getId()==null) {
	    	ULaunchCoverFlowCount coverFlowCount = new ULaunchCoverFlowCount();
	    	coverFlowCount.setId(id);
	    	coverFlowCount.setAct(act);
	    	coverFlowCount.setCreatedate(createdate);
	    	coverFlowCount.setLaunchid(launchid);
	    	coverFlowCount.setRequestfrom(requestfrom);
	    	coverFlowCount.setTimes(1L);
	    	baseDAO.save(coverFlowCount);
			
		}else {
         Long tmpTimes=			launchCoverFlowCount.getTimes();
         
         launchCoverFlowCount.setTimes(tmpTimes+1);
         
         baseDAO.update(launchCoverFlowCount);
		}
		 
		return WebPublicMehod.returnRet("success", launchCoverFlowCount);
	}

	@Override
	public HashMap<String, Object> getAppLaunchCoverFlow(HashMap<String, String> params)
			throws Exception {
		
		
		String versionInfo = null;
		
		////System.out.println(params);
		String appversion = params.get("appversion")!=null?params.get("appversion").toString():null;
		String requestfrom = params.get("requestfrom")!=null?params.get("requestfrom").toString():null;
		
		
		if (appversion!=null&&requestfrom!=null) {
			if (requestfrom.equals("ANDROID")||requestfrom.equals("IOS")) {
				versionInfo = requestfrom+appversion;
			}
		}else {
			versionInfo = "DEFAULT";
		}
		
		 
		// retMsgResult = new RetMsgResult();
		// TODO Auto-generated method stub
		String adSql = "SELECT * FROM u_launchcoverflow C WHERE C.TYPE = 'c' "
				+ "AND C.COVERFLOWID LIKE '%"+versionInfo+"%'"	
				+ "ORDER BY C.ORDERS DESC ";
		
		////System.out.println("sout->>>"+adSql);
		
		List<HashMap> adList = baseDAO.findSQLMap(adSql);
		if (adList.size()>0) {
		//	retMsgResult.setOperSuccess(null, adList);
			return WebPublicMehod.returnRet("success", adList);
		}else {
			String launchSql = "SELECT * FROM u_launchcoverflow C WHERE C.TYPE = 'b' "
					+ "AND C.COVERFLOWID LIKE '%"+versionInfo+"%'"
					+ "ORDER BY C.ORDERS DESC ";
			List<HashMap> launchList = baseDAO.findSQLMap(launchSql);
		//	retMsgResult.setOperSuccess(null, launchList);
			return WebPublicMehod.returnRet("success", launchList);
		}
		
		
	//	return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> getFrontDynamicCoverFlow(HashMap<String, String> params)
			throws Exception {
		
		Integer page = params.get("page")!=null?new Integer(params.get("page").toString()):1;
		
		// retMsgResult = new RetMsgResult();
		String sqlString = "SELECT CF.showseconds,CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno,CF.type,CF.remark,CF.createtime FROM u_frontdynamiccoverflow CF ORDER BY CF.ORDERBYNO ASC ";
		sqlString +=" limit 10 offset "+((page-1)*10);
		
		List<HashMap> frontDynamicList =baseDAO.findSQLMap(sqlString);
		HashMap<String ,Object> map =new HashMap<String ,Object>();
		
		
		String sqlTotalCount = "SELECT count(CF.ID) AS cnt FROM u_frontdynamiccoverflow CF";
		List<HashMap<String , Object>> cntList = baseDAO.findSQLMap(sqlTotalCount);

		//分页内容
		Integer totalCountInteger = 0;
		Integer pageCountInteger =0;
		if (cntList.size()>0) {
			cntList.get(0).get("cnt");
			totalCountInteger = cntList.get(0).get("cnt")!=null? new Integer(cntList.get(0).get("cnt").toString()):0;
		}
		
		if (totalCountInteger>0) {
			pageCountInteger = totalCountInteger/10+1;
		}
		
		map.put("totalPage", pageCountInteger);
		map.put("totalCount", totalCountInteger);
		map.put("page", page);
		map.put("data", frontDynamicList);
		
		
		return WebPublicMehod.returnRet("success", map);
		 
	}

	@Override
	public  HashMap<String, Object> getFrontPageCoverFlow(HashMap<String, String> params)
			throws Exception {
		// SELECT CF.showseconds,CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno FROM u_frontpagecoverflow CF ORDER BY CF.ORDERBYNO ASC 
		// retMsgResult = new RetMsgResult();
		String sqlString = "SELECT CF.showseconds,CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno,CF.type,CF.remark FROM u_frontpagecoverflow CF ORDER BY CF.ORDERBYNO ASC ";
		List<HashMap> frontDynamicList = baseDAO.findSQLMap(sqlString);
		//retMsgResult.setOperSuccess(null, frontDynamicList);
		//return retMsgResult;
		return WebPublicMehod.returnRet("success", frontDynamicList);
	}

	@Override
	public HashMap<String, Object> getCourtServiceList(HashMap<String, String> params)
			throws Exception {
	//	retMsgResult = new RetMsgResult();
	///	String sqlString = "SELECT CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno FROM u_courtservicestatic CF "+" where CF.courtid =" +params.get("courtid").toString()
		String sqlString = "SELECT CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno FROM u_courtservicestatic CF "+" where CF.courtid = :courtid " 
				+ "  ORDER BY CF.ORDERBYNO ASC ";
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("courtid", Integer.parseInt(params.get("courtid")));
		List<HashMap> frontDynamicList = baseDAO.findSQLMap(sqlString,hashMap);
		
		return WebPublicMehod.returnRet("success", frontDynamicList);
//		retMsgResult.setOperSuccess(null, frontDynamicList);
//		return retMsgResult;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap<String, Object> getCourtDynamicInfo(HashMap<String, String> params)
			throws Exception {
		// retMsgResult = new RetMsgResult();
		Integer page = params.get("page")!=null?new Integer(params.get("page").toString()):1;


		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		hashMap.put("cid", Integer.parseInt(params.get("cid")));
		
		
		String sqlString = "SELECT CF.* FROM u_courtdynamicinfo CF  WHERE CF.courtid = :cid ORDER BY CF.ORDERBYNO ASC ";
		sqlString+="limit 10 offset "+((page-1)*10);
		List<HashMap> frontDynamicList = baseDAO.findSQLMap(sqlString,hashMap);
		return WebPublicMehod.returnRet("success", frontDynamicList);
//		retMsgResult.setOperSuccess(null, frontDynamicList);
//		return retMsgResult;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> getFrontDynamicCoverFlowMatch(
			HashMap<String, String> params) throws Exception {
		
		Integer page = params.get("page")!=null?new Integer(params.get("page").toString()):1;
		
	//	retMsgResult = new RetMsgResult();
		String sqlString = "SELECT CF.showseconds,CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno,CF.type,CF.remark,CF.createtime FROM u_frontdynamiccoverflow_match CF ORDER BY CF.ORDERBYNO ASC ";
		sqlString +=" limit 10 offset "+((page-1)*10);
		
		List<HashMap> frontDynamicList = baseDAO.findSQLMap(sqlString);
		HashMap<String,Object> map =new HashMap<String,Object>();
		
		
		String sqlTotalCount = "SELECT count(CF.id) AS cnt FROM u_frontdynamiccoverflow_match CF";
		List<HashMap> cntList = baseDAO.findSQLMap(sqlTotalCount);

		//分页内容
		Integer totalCountInteger = 0;
		Integer pageCountInteger =0;
		if (cntList.size()>0) {
			cntList.get(0).get("cnt");
			totalCountInteger = cntList.get(0).get("cnt")!=null? new Integer(cntList.get(0).get("cnt").toString()):0;
		}
		
		if (totalCountInteger>0) {
			pageCountInteger = totalCountInteger/10+1;
		}
		
		map.put("totalPage", pageCountInteger);
		map.put("totalCount", totalCountInteger);
		map.put("page", page);
		map.put("data", frontDynamicList);
		return WebPublicMehod.returnRet("success", frontDynamicList);
//		retMsgResult.setOperSuccess(null, map);
//		return retMsgResult;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> getFrontDynamicCoverFlowHot(
			HashMap<String, String> params) throws Exception {
		
		Integer page = params.get("page")!=null?new Integer(params.get("page").toString()):1;
		
		// retMsgResult = new RetMsgResult();
		String sqlString = "SELECT CF.showseconds,CF.content,CF.title,CF.img,CF.linkurl,CF.orderbyno,CF.type,CF.remark,CF.createtime FROM u_frontdynamiccoverflow_hot CF ORDER BY CF.ORDERBYNO ASC ";
		sqlString +=" limit 10 offset "+((page-1)*10);
		
		List<HashMap> frontDynamicList = baseDAO.findSQLMap(sqlString);
		HashMap map =new HashMap();
		
		
		String sqlTotalCount = "SELECT count(CF.ID) AS cnt FROM u_frontdynamiccoverflow_hot CF";
		List<HashMap> cntList = baseDAO.findSQLMap(sqlTotalCount);

		//分页内容
		Integer totalCountInteger = 0;
		Integer pageCountInteger =0;
		if (cntList.size()>0) {
			cntList.get(0).get("cnt");
			totalCountInteger = cntList.get(0).get("cnt")!=null? new Integer(cntList.get(0).get("cnt").toString()):0;
		}
		
		if (totalCountInteger>0) {
			pageCountInteger = totalCountInteger/10+1;
		}
		
		map.put("totalPage", pageCountInteger);
		map.put("totalCount", totalCountInteger);
		map.put("page", page);
		map.put("data", frontDynamicList);
		
		return WebPublicMehod.returnRet("success", frontDynamicList);
		
//		retMsgResult.setOperSuccess(null, map);
//		return retMsgResult;
	}

	@Override
	public HashMap<String, Object> getAppResource(HashMap<String, String> params)
			throws Exception {
		
		String versionInfo = null;
		
		////System.out.println(params);
		String appversion = params.get("appversion")!=null?params.get("appversion").toString():null;
		String requestfrom = params.get("requestfrom")!=null?params.get("requestfrom").toString():null;
		
		
		if (appversion!=null&&requestfrom!=null) {
			if (requestfrom.equals("ANDROID")||requestfrom.equals("IOS")) {
				versionInfo = requestfrom+appversion;
			}
		}else {
			versionInfo = "DEFAULT";
		}
		
		 
		// retMsgResult = new RetMsgResult();
		// TODO Auto-generated method stub
		String adSql = "SELECT * FROM u_appresourse C WHERE C.TYPE = 'c' "
				+ "AND C.COVERFLOWID LIKE '%"+versionInfo+"%'"	
				+ "ORDER BY C.orders DESC ";
		
		////System.out.println("sout->>>"+adSql);
		
		List<HashMap> adList = baseDAO.findSQLMap(adSql);
		if (adList.size()>0) {
		//	retMsgResult.setOperSuccess(null, adList);
			return WebPublicMehod.returnRet("success", adList);
		}else {
			String launchSql = "SELECT * FROM u_appresourse C WHERE C.TYPE = 'b' "
					+ "AND C.COVERFLOWID LIKE '%"+versionInfo+"%'"
					+ "ORDER BY C.orders DESC ";
			List<HashMap> launchList = baseDAO.findSQLMap(launchSql);
		//	retMsgResult.setOperSuccess(null, launchList);
			return WebPublicMehod.returnRet("success", launchList);
		}
		
		
	//	return null;
	}

	@Override
	public HashMap<String, Object> hasNewVersionOrNotMethod(
			HashMap<String, String> params) throws Exception {

        //System.out.println("new verson!");
		// TODO Auto-generated method stub
//	    retMsgResult = new RetMsgResult();
	    HashMap resultHashMap = new HashMap();
		
		
		String requestfrom = null;
		String clientversion = params.get("clientversion")!=null?params.get("clientversion").toString():null;
		
		
		
		
		if (params.get("requestfrom")!=null&&params.get("requestfrom").toString()!=null&&!"".equals(params.get("requestfrom").toString())) {
			requestfrom = params.get("requestfrom").toString();
		}
		HashMap map = new HashMap();
		
		
		
		
		if (clientversion==null||clientversion.length()<=1) {
			resultHashMap.put("hasnew", -1);
			resultHashMap.put("info", map);
			return WebPublicMehod.returnRet("success", resultHashMap);
//	      	retMsgResult.setOperSuccess(null, resultHashMap);
//	    	return retMsgResult;

		}
		
		
		
		if (requestfrom!=null) {
			String sql = "SELECT * FROM u_appversion WHERE type = '"+requestfrom+"' ORDER BY ID DESC";
			List<HashMap> list = baseDAO.findSQLMap (sql);
			if (list.size()>0) {
				  map = list.get(0);
				if (map!=null&&map.get("versions")!=null) {
					String appversion = map.get("versions").toString();
					int compare = clientversion.compareTo(appversion);
					if (compare<0) {
						resultHashMap.put("hasnew", 1);
						resultHashMap.put("info", map);
						return WebPublicMehod.returnRet("success", resultHashMap);
//					    
					} 
					
					
				}
			}
		}
		
		
		
		//
		resultHashMap.put("hasnew", -1);
		resultHashMap.put("info", map);
		return WebPublicMehod.returnRet("success", resultHashMap);
//		
		
		//
		
	//	return null;
	
	
	}

	

}
