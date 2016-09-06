package upbox.service;

import java.util.HashMap;
import java.util.List;

//LBS接口
public interface OLDLBSService {
	/**
	 * 
	 * 
	   TODO - 传入经纬度返回城市地址，并且验证是否和上一次登陆的城市相同
	   @param map
	   		location 纬度,经度 逗号分隔
	   @return
	   @throws Exception
	   2016年6月7日
	   mazkc
	 */
	public HashMap<String,Object> poiToAdress(HashMap<String,String> map) throws Exception;
	
	/**
	 * 通过用户ID获取经纬度
	 * @param map
	 * userId	用户ID (没有ID则默认所有地址非空用户)
	 * @return infoList 用户信息
	 * @throws Exception
	*/
	public List<HashMap<String, Object>> getGpsByUserId(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 通过球队ID获取经纬度
	 * @param map
	 * teamId 球队ID (没有ID则默认所有地址非空球队)
	 * @return infoList 球队信息
	 * @throws Exception
	*/
	public List<HashMap<String, Object>> getGpsByTeamId(HashMap<String, String> map) throws Exception; 
	
	/**
	 * 上传GEOTABLE数据
	 * @param infoList<info>
	 * info
	 * .title poi名称
	 * .coord_type 坐标类型
	 * .geotable_id	记录关联的geotable的标识
	 * .address 地址
	 * .lat 维度
	 * .lng 精度
	 * .objectId  唯一索引字段(通用ID,由tags决定ID类型)
	 * .tags (court-球场、team-球队、user-用户)
	 * @return 本地数据库(u_baidulbs)表数据
	 * @throws Exception 
	*/ 
	public List<HashMap<String, Object>> createGeodata(List<HashMap<String, Object>> infoList) throws Exception;
	
	/**
	 * 创建本地数据库(u_baidulbs)表数据
	 * @param lbsDataList u_baidulbs表结构数据(不含createtime和key_id)
	 * @throws Exception
	 */
	public void createUbaidulbsData(List<HashMap<String, Object>> lbsDataList) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - LBS本地检索
	   @param map
	   @throws Exception
	   2016年6月20日
	   mazkc
	 */
	public void getNearBy(HashMap<String, Object> map) throws Exception;




	/**
	 * 添加数据库约战挑战的lbs定位信息
	 * @param map
	 * @throws Exception
	 */
	public void addLbsDuelChallenge(HashMap<String,Object> map) throws Exception;
}
