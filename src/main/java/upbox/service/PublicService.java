package upbox.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * 通用接口 
 * @author wmq
 *
 * 15618777630
 */
public interface PublicService {
	/**
	 * 使用hibernate循环保存list对象
	 * @param list
	 * @throws Exception
	 */
	public void saveListObject(List<?> list) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据KEY删除redis缓存数据
	   @param key
	   @throws Exception
	   2015年12月14日
	   mazkc
	 */
	public void removeHRedis(String key) throws Exception;
	/**
	 * 
	 * TODO - 根据key 和 filed 删除HSET
	 * @param key
	 * @param filed
	 * @throws Exception
	 * void
	 * mazkc
	 * 2016年4月11日
	 */
	public void removeHRedis_HSET(String key,String filed) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据KEY更新单个缓存数据
	   @param key
	   @throws Exception
	   2015年12月14日
	   mazkc
	 */
	public void updateHRedisByKey(String key,Object val) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 更新时间轴时间
	   @param map
	   		type  			1:用户 2：球队
	   		behaviorType	事件类型
	   			用户：
	   				1、加入，（注册加入平台时间）
					2、建立，首次建立球队（标记队徽）
					3、加盟，首次加入球队（标记队徽）
					4、首次发布动态？？
					5、关注，首次关注球队（标记队徽）
				战队：
					1、成立，球队建立时间
					2、加入，球队加入UPBOX时间
					3、成立，球队正式成立（满7人时间）
					4、发起，球队首次发起约战
					5、约战，球队首次成功响应约战
					6、响应，球队首次响应其他球队约战
					7、首胜，球队首次约战成功（约战比赛获胜）
					8、擂主，球队首次成为擂主
					9、攻擂，球队首次攻擂成功
					10、守擂，球队首次守擂成功
					11、球队首次参加赛事
					12、 首次参与约战
					13、 首次擂主挑战
	   		userId			用户id/teamId			球队Id
	   		objectId		对应事件的Id
	   		objectType		标记的队徽，如没有传空
	   		createDate		时间（如果事件不是当前时间，就得传这个参数 【例如：球队成立时间】）
	   @return
	   		success:成功   fail:失败  null:没有任何操作
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	public String updateBehavior(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 根据类型获取事件对应的类型和简称
	   @param map
	   		type   时间类型  1:用户 2：球队
	   		userId		用户Id	用户查
	   		teamId		球队Id	球队查/用户达成约战、挑战时也要传参
	   		eventsType	里程碑状态
	   				4	达成约战 
	   				5	约战胜利 
	   				6	达成挑战 
	   				7	挑战胜利
	   		behaviorType	时间类型
	   			用户：
	   				1	加入，（注册加入平台时间）
					2	建立，首次建立球队（标记队徽）
					3	加盟，首次加入球队（标记队徽）
					4	首次发布动态？？
					5	关注，首次关注球队（标记队徽）
				战队：
					1	成立，球队建立时间
					2	加入，球队加入UPBOX时间
					3	成立，球队正式成立（满7人时间）
					4	发起，球队首次发起约战
					5	约战，球队首次成功响应约战
					6	响应，球队首次响应其他球队约战
					7	首胜，球队首次约战胜利（约战比赛获胜）
					8	擂主，球队首次成为擂主
					9	攻擂，球队首次攻擂成功
					10	守擂，球队首次守擂成功
	   @return
	   @throws Exception
	   2016年3月22日
	   dengqiuru
	 */
	public Object getBehavior(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据类型获取事件对应的类型和简称-LIST
	   @param map
	   		type   时间类型  1:用户 2：球队
	   		userId		用户Id	用户查
	   		teamId		球队Id	球队查
	   @return
	   @throws Exception
	   2016年3月22日
	   dengqiuru
	 */
	public List<Object> getBehaviorList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 验证是否为空
	   @param param
	   @throws Exception
	   2016年3月11日
	   dengqiuru
	 */
	public Boolean StringUtil(String param)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 生成球队活跃记录
	   @param map
	   	球队活跃状态	activity_status		varchar 20		1=约战、2=挑战、3=赛事、4=订场							
		队伍ID	team_id		varchar 60		/			
	   @throws Exception
	   2016年4月26日
	   mazkc
	 */
	public void addTeamActivity(String activity_status,String team_id) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据主键ID查找数值类型ID
	   @param type 1-用户 2-球队 3-父级球场 4-子球场 5-约战 6-挑战
	   @param id 主键ID
	   @return
	   @throws Exception
	   2016年6月30日
	   mazkc
	 */
	public int getIntKeyId(int type,HashMap<String,String> map) throws Exception;

	/** 
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty 
     *  
     * @param obj 
     * @return 
     */ 
	public boolean isNullOrEmpty(Object obj);

	/**
	 * 
	 * 
	   TODO - 检测url是否有效
	   @param urlStr
	   @return
	   @throws IOException
	   2016年8月24日
	   dengqiuru
	 */
	public String isConnect(String urlStr) throws IOException;

	/**
	 * 
	 * 
	   TODO - 判断手机号格式是否正确
	   @param phone
	   @return
	   2016年8月26日
	   dengqiuru
	 */
	public boolean validateMoblie(String phone);
} 
