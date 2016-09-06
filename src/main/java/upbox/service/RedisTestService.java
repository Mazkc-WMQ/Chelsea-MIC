package upbox.service;


/**
 * Redis测试service
 * @author wmq
 *
 * 15618777630
 */
public interface RedisTestService {
	/**
	 * 
	 * 
	   TODO - 列表缓存数据单独更新测试
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void signUpdateRedisList()throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 测试用户模块详情
	   KEY=WebPublicMehod.getHRedisKey(项目名+'Object');
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void testUUserInfo() throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 测试用户模块列表
	   KEY=WebPublicMehod.getHRedisKey(项目名+"ListPage");
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void testUUserList() throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 缓存更新机制测试
	   @throws Exception
	   2015年11月24日
	   mazkc
	 */
	public void testRedisClean() throws Exception;
}
