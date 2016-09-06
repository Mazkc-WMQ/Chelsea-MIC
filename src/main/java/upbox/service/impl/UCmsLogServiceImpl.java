package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.org.pub.PublicMethod;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UCmsLog;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UCmsLogService;

/**
 * 前端cms实现类
 *
 */

@Service("uCmsLogService")
public class UCmsLogServiceImpl implements UCmsLogService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	// 列表
	private StringBuffer getCmsListByTeamId_sql = new StringBuffer(
			" select c.key_id keyId,c.cms_id cmsId,c.team_id teamId,c.name cmsName,c.title cmsTitle, "
			+" c.url cmsUrl,c.createdate createdate  from u_cms_log c "
			+" LEFT JOIN u_team t on c.team_id=t.team_id "
			+" where c.team_id=:teamId "
			+" ORDER BY c.createdate desc ");
	
	@Override
	public HashMap<String, Object> getCmsListByTeamId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> uCmsLogList = new ArrayList<>();
		if (publicService.StringUtil(map.get("teamId"))) {
			hashMap.put("teamId", map.get("teamId"));
			StringBuffer sql = this.getCmsListByTeamId_sql;
			if (publicService.StringUtil(map.get("page"))) {
				uCmsLogList = this.getPageLimit(sql.toString(), map,hashMap);
				if (null != uCmsLogList && uCmsLogList.size() > 0) {
					for (HashMap<String, Object> hashMap2 : uCmsLogList) {
						if (null != hashMap2.get("createdate")) {
							if (hashMap2.get("createdate").toString().length() >= 10) {
								hashMap2.put("cmsCreateDate", hashMap2.get("createdate").toString().substring(0, 10));
							}
						}
					}
				}
			}else{
				return WebPublicMehod.returnRet("error", "请求参数page不能为空");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请求参数teamId不能为空");
		}
		resultMap.put("uCmsLogList", uCmsLogList);
		return resultMap;
	}

	/**
	 * 
	 * TODO 分页代码
	 * @param sql  语句
	 * @param hashMap 查询参数
	 * @param map 分页参数
	 * @param count 总记录数
	 * @return
	 * @throws Exception
	 * List<Object>
	 * xiao
	 * 2016年3月8日
	 */
	private List<HashMap<String, Object>> getPageLimit(String sql, HashMap<String, String> map,HashMap<String, Object> hashMap) throws Exception{
		List<HashMap<String, Object>> list;
		hashMap.put("page", map.get("page"));
		PageLimit pa = new PageLimit(Integer.parseInt(map.get("page")), 0);
		hashMap.put("limit", pa.getLimit());
		hashMap.put("offset", pa.getOffset());
		StringBuffer newSql = new StringBuffer();
		newSql.append( sql + " limit :limit offset :offset" );
		list = baseDAO.findSQLMap(newSql.toString(),hashMap);
		return list;
	}
	
	@Override
	public UCmsLog getCmsObjectByKeyId(HashMap<String, String> map) throws Exception {
		UCmsLog uCmsLog = null;
		if (publicService.StringUtil(map.get("keyId"))) {
			uCmsLog = baseDAO.get(map, " from UCmsLog where keyId=:keyId");
		}
		return uCmsLog;
	}

	@Override
	public UCmsLog getCmsObjectByCmsId(HashMap<String, String> map) throws Exception {
		UCmsLog uCmsLog = null;
		if (publicService.StringUtil(map.get("cmsId")) && publicService.StringUtil(map.get("teamId"))) {
			uCmsLog = baseDAO.get(map, " from UCmsLog where cmsId=:cmsId and teamId=:teamId");
		}
		return uCmsLog;
	}

}
