package upbox.service;

import java.util.HashMap;
import java.util.List;

import upbox.model.BdLbsBean;
import upbox.model.PageLimit;
import upbox.model.UBaidulbs;

//LBS接口
public interface LBSService {
	/*
	 * 	0: 成功
		1: 服务器内部错误
		2: 参数错误
		3: http method错误
		21: 此操作为批量操作
		22: 同步到检索失败
		31: 服务端加锁失败
		32: 服务端释放锁失败
		1001: 表的name重复
		1002: 表的数量达到了最大值
		1003: 表中存在poi数据，不允许删除
		2001: 列的key重复
		2002: 列的key是保留字段
		2003: 列的数量达到了最大值
		2004: 唯一索引只能创建一个
		2005: 更新为唯一索引失败，原poi数据中有重复
		2011: 排序筛选字段只能用于整数或小数类型的列
		2012: 排序筛选的列已经达到了最大值
		2021: 检索字段只能用于字符串类型的列且最大长度不能超过512个字节
		2022: 检索的列已经达到了最大值
		2031: 索引的列已经达到了最大值
		2041: 指定的列不存在
		2042: 修改max_length必须比原值大
		3001: 更新坐标必须包含经纬度和类型
		3002: 唯一索引字段存在重复
		3031: 上传的文件太大
	 * 
	 * */
	
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
	 * 创建本地数据库(u_baidulbs)表数据(兼容新增或修改)
	 * @param objectId 各业务数据主键id
	 * @param lbsType 业务类型 1-用户 2-球队 3-球场 4-约战 5-挑战
	 * @param lbsId 百度lbs上传成功后返回的主键
	 * @throws Exception
	 * yuancao
	 */
	public void createUbaidulbsData(String objectId,String lbsType,String lbsId) throws Exception;
	

	/**
	 * 创建本地数据库(u_baidulbs)表失败数据(兼容新增或修改)
	 * @param objectId 各业务数据主键id
	 * @param lbsType 业务类型 1-用户 2-球队 3-球场 4-约战 5-挑战
	 * @param errorMsg 错误描述(例如删除失败，修改失败，新增失败)
	 * @throws Exception
	 * yuancao
	 */
	public void createUbaidulbsDataError(String objectId,String lbsType,String errorMsg) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 删除本地数据库(u_baidulbs)表数据
	   @param map
	   		objectId 标示主键ID
	   		tags court-球场、team-球队、user-用户 duel-约战  challege-挑战
	   @throws Exception
	   2016年6月20日
	   mazkc
	 */
	public void delUbaidulbsData(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - LBS周边检索
	    	@param map<br/>
	    	url 请求地址
	   		<p>ak	access_key	string(50)	字符串	必须</p>
			geotable_id	geotable主键	uint32	数字	必须<br/>
			q	检索关键字	string(45)	任意汉字或数字，英文字母，可以为空字符	可选<br/>
			<p>location	检索的中心点	string(25)	逗号分隔的经纬度	必须</p>
						<p class="indent">样例：116.4321,38.76623</p>
			<p>coord_type	坐标系	uint32	数字	可选<br/>
						<p class="indent">3代表百度经纬度坐标系统 4代表百度墨卡托系统<br/>
			<p>radius	检索半径	uint32	单位为米，默认为1000	可选</p>
						<p class="indent">样例：500<br/>
			<p>tags	标签	string(45)	空格分隔的多字符串	可选</p>
						样例：美食 小吃<br/>
			<p>sortby	排序字段	string	”分隔的多个检索条件。</p>
					<p class="indent">格式为sortby={key1}:value1|{key2:val2|key3:val3}。 最多支持16个字段排序 {keyname}:1 升序 {keyname}:-1 降序 以下keyname为系统预定义的： distance 距离排序 weight 权重排序<br/>
					可选<br/>
					默认为按weight排序 如果需要自定义排序则指定排序字段 样例：按照价格由便宜到贵排序 sortby=price:1</p>
			<p>filter	过滤条件	string(50)	竖线分隔的多个key-value对</p>
					<p class="indent">key为筛选字段的名称(存储服务中定义) 支持连续区间或者离散区间的筛选： a:连续区间 key:value1,value2 b:离散区间 key:[value1,value2,value3,...]<br/>
					可选<br/>
					样例: a:连续区间 样例：筛选价格为9.99到19.99并且生产时间为2013年的项 price:9.99,19.99|time:2012,2012 b:离散区间 筛选价格为8,9,13，并且生产时间为2013年的项 price:[8,9,13]|time:2012,2012 注：符号为英文半角中括号</p>
			<p>page_index	分页索引	uint32	当前页标，从0开始	可选</p>
					<p class="indent">默认为0</p>
			page_size	分页数量	uint32	当前页面最大结果数	可选 默认为10，最多为50<br/>
			callback	回调函数	string(20)	js回调函数	可选
	   @throws Exception
	   2016年6月20日
	   mazkc
	 */
	public String getNearBy(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - LBS本地检索
	    	ak	access_key	string(50)	字符串	必须<br/>
			geotable_id	geotable主键	uint32	数字	必须<br/>
			q	检索关键字	string(45)	任意汉字或数字，英文字母，可以为空字符	必须<br/>
			<p>coord_type	坐标系	uint32	数字	可选<br/>
				3代表百度经纬度坐标系统 4代表百度墨卡托系统</p>
			<p>region	检索区域名称	String(25)	市或区的名字，如北京市，海淀区。	可选,此接口推荐填写该参数 否则，默认按照全国范围来检索</p>
			tags	标签	string(45)	空格分隔的多字符串	可选 样例：美食 小吃<br/>
			<p>sortby	排序字段	string	”分隔的多个检索条件。<br/>
				格式为sortby={key1}:value1|{key2:val2|key3:val3}。 最多支持16个字段排序 {keyname}:1 升序 {keyname}:-1 降序 以下keyname为系统预定义的： distance 距离排序 weight 权重排序<br/>
				可选 默认为按weight排序 如果需要自定义排序则指定排序字段 样例：按照价格由便宜到贵排序 sortby=price:1</p>
			<p>filter	过滤条件	string(50)	竖线分隔的多个key-value对<br/>
				key为筛选字段的名称(存储服务中定义) 支持连续区间或者离散区间的筛选： a:连续区间 key:value1,value2 b:离散区间 key:[value1,value2,value3,...]<br/>
				可选 样例: a:连续区间 样例：筛选价格为9.99到19.99并且生产时间为2013年的项 price:9.99,19.99|time:2012,2012 b:离散区间 筛选价格为8,9,13，并且生产时间为2013年的项 price:[8,9,13]|time:2012,2012 注：符号为英文半角中括号</p>	
			page_index	分页索引	uint32	当前页标，从0开始	可选 默认为0 <br/>
			page_size	分页数量	uint32	当前页面最大结果数	可选 默认为10，最多为50<br/>
			callback	回调函数	string(20)	js回调函数	可选 <br/>
	   @throws Exception
	   2016年6月20日
	   mazkc
	 */
	public String getNearByCity(HashMap<String, Object> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 传入地址返回经纬度
	   @param map<br/>
	   		参数		是否必须		默认值		格式举例		含义 <br/>
	   		output	否	xml	json或xml	输出格式为json或者xml <br/>
			ak	是	无	E4805d16520de693a3fe707cdc962045	用户申请注册的key，自v2开始参数修改为“ak”，之前版本参数为“key” <br/>
			callback	否	无	callback=showLocation(JavaScript函数名)	将json格式的返回值通过callback函数返回以实现jsonp功能 <br/>
			address	是	无	北京市海淀区上地十街10号 <br/>
			city	否	“北京市”	“广州市” 
	   @return
	   @throws Exception
	   2016年6月7日
	   mazkc
	 */
	public String addressToApi(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 位置数据表（geotable）创建表数据
	   @param map <br/>
	   		title	poi名称	string(256)	可选<br/>
			address	地址	string(256)	可选<br/>
			tags	tags	string(256)	必选  court-球场、team-球队、user-用户 duel-约战  challege-挑战<br/>
			latitude	用户上传的纬度	double	必选<br/>
			longitude	用户上传的经度	double	必选<br/>
			<p>coord_type	用户上传的坐标的类型	uint32	必选 (默认3)</p>
						<p class="indent">1：GPS经纬度坐标<br/>
						2：国测局加密经纬度坐标<br/>
						3：百度加密经纬度坐标<br/>
						4：百度加密墨卡托坐标</p>
			geotable_id	记录关联的geotable的标识	string(50)	必选，加密后的id<br/>
			ak	用户的访问权限key	string(50)	必选<br/>
			subcourt_intid 下属球场数值类型ID
			court_intid 父级球场数值类型ID
			team_intid 球队数值类型ID
			user_intid 用户数值类型ID
			challege_intid 挑战数值类型ID
			duel_intid 约战数值类型ID
			object_id 主键ID
			params_type 标示类型
			date 当前时间  格式yyyy-MM-dd
	   @return
		   	status	状态码	int32	0代表成功，其它取值含义另行说明<br/>
			message	响应的信息	string(50)	状态码描述<br/>
			id	新增的数据的id	string	
	   @throws Exception
	   2016年6月7日
	   mazkc
	 */
	public String createGeodata(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 封装lbs接口返回 回来的json对象
	 * @param json 返回lbs的json数据
	 * @return
	 * @throws Exception
	 * 2016年6月22日
	 * yc 
	 */
	public List<BdLbsBean> packLbsDate(String json) throws Exception;
	
	/**
	 * 封装lbs接口返回 回来的json对象--传入地址返回经纬度
	 * @param json 返回lbs的json数据
	 * @return hashMap lat－纬度，lng－经度
	 * @throws Exception
	 * 2016年6月22日
	 * yc 
	 */
	public HashMap<String, Object> packLbsDateAddress(String json) throws Exception;
	/**
	 * 封装lbs接口返回 回来的json对象--位置数据表（geotable）创建表数据
	 * @param json 返回lbs的json数据
	 * @return hashMap id－上传到百度后返回的Id
	 * @throws Exception
	 * 2016年6月22日
	 * yc 
	 */
	public HashMap<String, Object> packLbsDateCreateGeodata(String json) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 修改表数据
	   @param map<br/>
	   		<p>id	poi的id	uint64	当不存在唯一索引字段时必须，存在唯一索引字段可选<br/>
				自定义唯一索引key	Value	用户自定义类型	可选，若自定义索引字段和id共存时，将以id为准，且自定义索引key将被新的value</p>
			title	poi名称	string(256)	<br/>
			address	地址		<br/>
			tags	tags		<br/>
			latitude	用户上传的纬度	double	<br/>
			longitude	用户上传的经度	double	<br/>
			<p>coord_type	用户上传的坐标的类型	uint32	必选<br/>
				1．GPS经纬度坐标<br/>
				2．测局加密经纬度坐标<br/>
				3．百度加密经纬度坐标<br/>
				4．百度加密墨卡托坐标</p>
			geotable_id	记录关联的geotable的标识	string(50)	必选，加密后的id<br/>
			ak	用户的访问权限key	string(50)	必选<br/>
			subcourt_intid 下属球场数值类型ID
			court_intid 父级球场数值类型ID
			team_intid 球队数值类型ID
			user_intid 用户数值类型ID
			challege_intid 挑战数值类型ID
			duel_intid 约战数值类型ID
			object_id 主键ID
			params_type 标示类型
			date 当前时间  格式yyyy-MM-dd
	   @return
	   @throws Exception
	   2016年6月7日
	   mazkc
	 */
	public String updateGeodata(HashMap<String,Object> map) throws Exception;
	
	/**
	 * 百度lbs查询调用接口
	 * @param page 分页对象 筛选时page传入null
	 * @param sendType 调用方法：1。为getNearBy周边检索  2。为getNearByCity区域检索
	 * @param location 当前坐标
	 * @param radius 半径范围
	 * @param tags 搜索的类型  例如：user（用户） court（球场） team（球队）duel（约战） challege（挑战）
	 * @param sortby 排序 例如：distance:1 距离升序 等等自定义排序条件
	 * @param region 区域（sendType为2时需要使用） 例如：宝山区
	 * @param filterType 过滤类型   1：user（用户）2：court（球场）3：team（球队） 4：duel（约战） 5：challege（挑战）
	 * @param filter （多条件排序时需要使用）
	 * 				竖线分隔的多个key-value对key为筛选字段的名称(存储服务中定义) 
	 * 				支持连续区间或者离散区间的筛选： a:连续区间 key:value1,value2 b:离散区间 key:[value1,value2,value3,...]
	 * 
	 * @return List<BdLbsBean> lbs实体bean
	 * @throws Exception
	 */
	public List<BdLbsBean> sendBdLbs(PageLimit page,String sendType,String location,String radius,
			String tags,String sortby,String region,String filterType,String filter) throws Exception;
		
	
	 /**
	  
	   TODO - 百度表数据根据ID删除
	   @param map<br/>
	   		<p>id	被删除的id	uint64	如果设置了这个参数，其它的删除条件会被忽略，只会根据id删除单条poi。此时此操作不是批量请求。
				自定义唯一索引key	Value	用户自定义类型	可选，若自定义索引字段和id共存时，优先选择根据id删除poi。</p>
			title	名称	string(256)	可选<br/>
			tags	标签	string(256)	可选<br/>
			geotable_id	geotable_id	string(50)	必选<br/>
			ak	用户的访问权限key	string(50)	必选<br/>
	   @return
	   		status	状态码	int32	0表示成功，其他值详见状态码说明 <br/>
			size	分页参数，当前页返回数量	int32	<br/>
			total	分页参数，所有召回数量	int32	<br/>
			contents	uid	数据id	string	<br/>
			geotable_id	geotable_id	string	<br/>
			title	poi名称	string	<br/>
			address	poi地址	string	<br/>
			province	poi所属省	string(20)	<br/>
			city	poi所属城市	string(20)	<br/>
			district	poi所属区	string(20)	<br/>
			coord_type	坐标系定义	int32	可选<br/>
			3代表百度经纬度坐标系统 4代表百度墨卡托系统<br/>
			location	经纬度	array	<br/>
			tags	poi的标签	string	<br/>
			distance	距离，单位为米	int32	<br/>
			weight	权重	int32	<br/>
			{column}	自定义列	自定义类型	自定义列/値，云存储未添加値时不返回
	   @throws Exception
	   2016年6月7日
	   mazkc
	 */
	public String deleteDateByID(HashMap<String,Object> map) throws Exception;
	/**
	 * 
	 * TODO UBaidulbs本地对象获取－修改上传数据使用
	 * @param 业务类型 1-用户 2-球队 3-球场 4-约战 5-挑战
	 * @param objectId -对应的主键ID
	 * @return UBaidulbs
	 * @throws Exception
	 * xiaoying 2016年7月20日
	 */
	public UBaidulbs getBaidulbs(String lbsType,String objectId) throws Exception;
}
