package upbox.service.impl;


import com.org.pub.PublicMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.UParameter;
import upbox.model.UParameterInfo;
import upbox.pub.Public_Cache;
import upbox.service.ParamService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 后端参数接口实现
 * @author wmq
 *
 * 15618777630
 */
@Service("paramService")
public class ParamServiceImpl implements ParamService {
	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private RedisOperDAOImpl redisDao;

	@Override
	public Object getParamInfo(HashMap<String, String> map,int type)
			throws Exception
	{
		Object obj = null;
		HashMap<String,Object> hashMap = null;
		if(1 == type){
			obj = baseDAO.get(map, "from UParameter where params = :params");
		}else if(2 == type){
			obj = baseDAO.get(map, "select upi from UParameter as up,UParameterInfo as upi "
					+ "where up.pkeyId = upi.UParameter.pkeyId and  up.params = :params and upi.params = :paramVal");
		}else if(3 == type){
			hashMap = new HashMap<String, Object>();
			String[] params = map.get("params").split(",");
			for(String s : params){
				map.put("params", s);
				obj = baseDAO.find(map, "select upif from UParameter upi,UParameterInfo upif where upi.pkeyId = upif.UParameter.pkeyId and upi.params = :params");
				hashMap.put(s, obj);
			}
			return hashMap;
		}else if(5 == type){
			obj = baseDAO.get(UParameter.class,map.get("pkId"));
		}
		return obj;
	}

	
	@Override
	public void getParamCache() throws Exception
	{
		HashMap<String, HashMap<String, String>> _map = null;
		Public_Cache.HASH_PARAMS_OBJECT = new HashMap<String, HashMap<String, UParameterInfo>>();
		HashMap<String, String> _map1 = null;
		HashMap<String, UParameterInfo> _map_temp = null;
		if(null == Public_Cache.HASH_PARAMS){
			List<UParameter> arrayList = baseDAO.find("from UParameter");
			List<UParameterInfo> _list = null;
			if(null != arrayList && arrayList.size() > 0){
				_map = new HashMap<String, HashMap<String,String>>();
				for(UParameter up : arrayList){
					_map1 = new HashMap<String, String>();
					_map_temp = new HashMap<String, UParameterInfo>();
					_list = baseDAO.find("from UParameterInfo upi where upi.UParameter.pkeyId = '" + up.getPkeyId() + "'");
					if(null != _list && _list.size() > 0){
						for(UParameterInfo upi : _list){
							_map1.put(upi.getParams(), upi.getName());
							_map_temp.put(upi.getParams(), upi);
						}
					}
					_map.put(up.getParams(), _map1);
					Public_Cache.HASH_PARAMS_OBJECT.put(up.getParams(), _map_temp);
				}
			}
			Public_Cache.HASH_PARAMS = _map;
		}
	}

	@Override
	public UParameterInfo getParameterInfo(HashMap<String, String> map) throws Exception {
		if(StringUtils.isEmpty(map.get("param")))
			throw new RuntimeException("param 不能为空");
		if(StringUtils.isEmpty(map.get("value")))
			throw new RuntimeException("value 不能为空");
		String hql = "from UParameterInfo ui where ui.UParameter.params=:param and ui.params=:value";
		List<UParameterInfo> list = this.baseDAO.find(map,hql);
		if(CollectionUtils.isEmpty(list))
			return null;
		return list.get(0);
	}

	/**
	 * 刷新缓存数据库中的 参数信息
	 * @param params
	 * @param list
	 * @throws Exception
	 */
	private void  refreshRedis(String params,List<UParameterInfo> list) throws Exception {
		this.getParamInfoMapByMySql(this.getParam(params));
	}
	@Override
	public HashMap<String,UParameterInfo> getParamInfoMapEntity(String params) throws Exception {
		HashMap<String,UParameterInfo> hashMap = new HashMap<String,UParameterInfo>();
		List<UParameterInfo> list = this.getParamInfoListByMySql(this.getParam(params));
		for(UParameterInfo info : list){
			hashMap.put(info.getParams(),info);
		}
		return hashMap;
	}
	@Override
	public HashMap<String, String> getParamInfoMap(String param) throws Exception {
		return this.getParamInfoMapByParam(param);
	}
	@Override
	public void initParamCache() throws Exception {
		this.getParamMapByMySql();
	}

	/**
	 * 从缓存数据库中获取 码表信息 如果没有则从mysql中获取
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private HashMap<String,String> getParamInfoMapByParam(String param) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		String paramKey = PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "params", param);
		String filed = null;
		Object o = null;
		String value = null;
		UParameterInfo info = null;
		Set<Object> keys = this.redisDao.hkeys(paramKey);
		if(keys != null && keys.size()>0){
			Iterator<Object> iterator = keys.iterator();
			while(iterator.hasNext()){
				filed = (String) iterator.next();
				o =  this.redisDao.getHSet(paramKey,filed);
				if(o != null){
					value = o.toString();
				}else{
					//从MySQL数据库中获取 参数表中信息
					info = this.getParamInfo(param, filed);
					if(info==null)
						continue;
					value = info.getName();
					this.redisDao.setHSet(paramKey,filed,value);
				}
				map.put(filed,value);
			}
		}else{
			map = this.getParamInfoMapByMySql(this.getParam(param));
		}

		return map;
	}
	//获取码表小类
	private UParameterInfo getParamInfo(String paramKey,String infoKey) throws Exception {
		List<UParameterInfo> list = this.baseDAO.find("from UParameterInfo upi where upi.UParameter.params='" + paramKey + "' and upi.params = '" + infoKey + "'");
		if(CollectionUtils.isNotEmpty(list))
			return list.get(0);
		return null;
	}
	//获取码表大类
	private UParameter getParam(String paramKey) throws Exception {
		List<UParameter> list = this.baseDAO.find("from UParameter up where up.params='" + paramKey + "'");
		if(CollectionUtils.isNotEmpty(list))
			return list.get(0);
		return null;
	}
	//从MySQL中获取 参数表数据 并缓存redis中
	private HashMap<String, HashMap<String, String>> getParamListByMySql() throws Exception {
		HashMap<String, HashMap<String, String>> _map = null;
		List<UParameter> arrayList = baseDAO.find("from UParameter");
		String paramKey = PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"param");
		this.redisDao.del(paramKey);
		if (null != arrayList && arrayList.size() > 0) {
			for (UParameter up : arrayList) {
				up.setUParameterInfos(new HashSet(this.getParamInfoListByMySql(up)));
				this.redisDao.addList(paramKey,up);
			}
		}
		_map = this.paramListToParamMap(arrayList);
		return _map;
	}
	//码表List转换为码表Map
	private HashMap<String,HashMap<String,String>> paramListToParamMap(List<UParameter> list) throws Exception {

		HashMap<String,HashMap<String,String>> paramMap = new HashMap<String,HashMap<String,String>>();
		HashMap<String,String> infoMap = null;
		for(UParameter uParameter : list){
			infoMap = new HashMap<String,String>();
			for(UParameterInfo info : uParameter.getUParameterInfos()){
				infoMap.put(info.getParams(),info.getName());
			}
			paramMap.put(uParameter.getParams(),infoMap);
		}
		return paramMap;
	}
	//返回paramList
	private List<UParameterInfo> getParamInfoListByMySql(UParameter up) throws Exception {
		List<UParameterInfo> _list = baseDAO.find("from UParameterInfo upi where upi.UParameter.pkeyId = '" + up.getPkeyId() + "'");
		return _list;
	}
	//把数据库中码表信息缓存到缓存数据库中，并返回码表信息
	public HashMap<String,HashMap<String,String>> getParamMapByMySql() throws Exception{
		HashMap<String, HashMap<String, String>> _map = null;
		HashMap<String, String> _map1 = null;

		List<UParameter> arrayList = baseDAO.find("from UParameter");
		List<UParameterInfo> _list = null;
		if (null != arrayList && arrayList.size() > 0) {
			_map = new HashMap<String, HashMap<String, String>>();
			for (UParameter up : arrayList) {
				_map1 = this.getParamInfoMapByMySql(up);
				_map.put(up.getParams(), _map1);
			}
		}

		return _map;
	}
	//返回paramMap，更新数据库缓存
	private HashMap<String,String> getParamInfoMapByMySql(UParameter up) throws Exception {
		String redisKey = PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"params",up.getParams());
		this.redisDao.del(redisKey);
		HashMap<String,String> _map1 = new HashMap<String, String>();
		List<UParameterInfo> _list = baseDAO.find("from UParameterInfo upi where upi.UParameter.pkeyId = '" + up.getPkeyId() + "'");
		if (null != _list && _list.size() > 0) {
			for (UParameterInfo upi : _list) {
				_map1.put(upi.getParams(), upi.getName());
				this.redisDao.setHSet(redisKey,upi.getParams(),upi.getName());
			}
		}
		return _map1;
	}
}
