package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.org.pub.PublicMethod;
import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UUser;
import upbox.pub.Public_Cache;
import upbox.service.PublicService;
import upbox.service.RedisTestService;

@Service("redisTestService")
public class RedisTestServiceImpl implements RedisTestService {
	@Resource
	private RedisOperDAOImpl redisDao;
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private PublicService publicService;
	private Object obj;
	private List<?> li;

	// private Jedis jedis;

	/**
	 * 
	 * 
	 TODO - 打印
	 * 
	 * @param temp
	 * @param obj
	 *            2015年12月1日 mazkc
	 */
	public void printInfo(String temp, Object obj) {
		// System.out.println(temp + JSON.toJSONString(obj));
	}

	@Override
	public void signUpdateRedisList() throws Exception {
		PageLimit page = new PageLimit(1, 0);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userType", "6");
		li = baseDAO
				.findPageHRedis(
						hash,
						"from UUser where userType = :userType order by createdate desc ",
						UUser.class, PublicMethod.getHRedisKey(
								Public_Cache.PROJECT_NAME, "ListPage"),
						"userId", page.getLimit(), page.getOffset());
		printInfo("UUserList++++++++++++++++++++++++++" + li.size(), li);
		// System.out.println("------------------------------");

		UUser uo = baseDAO.getHRedis(UUser.class,
				"071fc25d-1357-4791-909c-0cbbcf557e60",
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object"));
		uo.setBirthday(new Date());
		baseDAO.update(uo);
		publicService.updateHRedisByKey("071fc25d-1357-4791-909c-0cbbcf557e60",
				uo);

		li = baseDAO
				.findPageHRedis(
						hash,
						"from UUser where userType = :userType order by createdate desc ",
						UUser.class, PublicMethod.getHRedisKey(
								Public_Cache.PROJECT_NAME, "ListPage"),
						"userId", page.getLimit(), page.getOffset());
		printInfo("UUserList++++++++++++++++++++++++++" + li.size(), li);
		// System.out.println("------------------------------");
	}

	@Override
	public void testUUserInfo() throws Exception {
		obj = baseDAO.getHRedis(UUser.class,
				"071fc25d-1357-4791-909c-0cbbcf557e60",
				PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object"));
		printInfo("UUserInfo++++++++++++++++++++++++++", obj);
		// System.out.println("------------------------------");
	}

	@Override
	public void testUUserList() throws Exception {
		PageLimit page = new PageLimit(1, 0);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userType", "6");
		li = baseDAO
				.findPageHRedis(
						hash,
						"from UUser where userType = :userType order by createdate desc ",
						UUser.class, PublicMethod.getHRedisKey(
								Public_Cache.PROJECT_NAME, "ListPage"),
						"userId", page.getLimit(), page.getOffset());
		printInfo("UUserList++++++++++++++++++++++++++" + li.size(), li);
		// System.out.println("------------------------------");
	}

	@Override
	public void testRedisClean() throws Exception {
//		String hql = " from UMessage where type = 'team' and userId = '6ec12151-da0d-4cc2-b00d-dbedc3933b4f' and isShow = '1' order by mesReadStauts desc,createtime desc ";
//		List<UMessage> li = baseDAO.find(hql);
//		int index = 0;
//		for(UMessage mm : li){
//			redisDao.getRedisTemplate().opsForZSet().add("CHELSEA-MIC_Object_Message_6ec12151-da0d-4cc2-b00d-dbedc3933b4f_" + mm.getCreatetime().getTime(),SerializeUtil.serialize(mm),0);
//			redisDao.getRedisTemplate().opsForZSet().add("CHELSEA-MIC_Object_Message_ListOrder", SerializeUtil.serialize(mm.getCreatetime().getTime()),0);
//		}
//		UMessage mm = null;
//		Set<Serializable> set1 = null;
//		Set<Serializable> set = redisDao.getRedisTemplate().opsForZSet().reverseRange("CHELSEA-MIC_Object_Message_ListOrder", 0, 5);
//		Iterator<Serializable> it = set.iterator();
//		Iterator<Serializable> it1 = null;
//		while(it.hasNext()){
//			set1 = redisDao.getRedisTemplate().opsForZSet().range("CHELSEA-MIC_Object_Message_6ec12151-da0d-4cc2-b00d-dbedc3933b4f_" + it.next(),0,100);
//			it1 = set1.iterator();
//			while(it1.hasNext()){
//				mm = SerializeUtil.unSerializeToBean(it1.next().toString(), UMessage.class);
//				System.out.println(PublicMethod.getDateToString(mm.getCreatetime(), "yyyy-MM-dd"));
//			}
//		}
		
//		UMessage mm = null;
//		SortQueryBuilder<Serializable> s = SortQueryBuilder.sort("CHELSEA-MIC_Object_Message_ListOrder");
//		s.limit(0,100);
//		s.by("CHELSEA-MIC_Object_Message_6ec12151-da0d-4cc2-b00d-dbedc3933b4f_*");
//		s.order(Order.ASC);
//		List<Serializable> li = redisDao.getRedisTemplate().sort(s.build());
//		for(Serializable ss : li){
//			mm = SerializeUtil.unSerializeToBean(ss.toString(), UMessage.class);
//			System.out.println(PublicMethod.getDateToString(mm.getCreatetime(), "yyyy-MM-dd"));
//		}
	}
}
