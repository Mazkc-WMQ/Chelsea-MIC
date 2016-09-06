package upbox.service;

import upbox.model.UParameterInfo;

import java.util.HashMap;

/**
 * 后端参数接口
 * @author wmq
 *
 * 15618777630
 */
public interface ParamService {
	/**
	 * 获取后端参数信息
	 * @param map --参数type值
	 * @param type --1-查询总详细信息 2-查询具体字段详细
	 * @return
	 * @throws Exception
	 */
	public Object getParamInfo(HashMap<String,String> map,int type)throws Exception;
	
	/**
	 * 获取全部的参数 组装成缓存
	 * @return
	 * @throws Exception
	 */
	public void getParamCache()throws Exception;

	/**
	 * TODO-获取参数的详细信息
	 * @param map params：参数类型 value：对应的值
	 * @return
	 * @throws Exception
	 */
	public UParameterInfo getParameterInfo(HashMap<String,String> map) throws Exception;

	/**
	 * TODO - 以对象的形式获取字表对象
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,UParameterInfo> getParamInfoMapEntity(String param) throws Exception;

	/**
	 * TODO - 以Map形式获取参数表数据
	 * @param param 大类名
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> getParamInfoMap(String param) throws Exception;

	/**
	 * TODO - 初始化参数表中 数据到 redis 缓存数据库中
	 */
	public void initParamCache() throws Exception;

}
