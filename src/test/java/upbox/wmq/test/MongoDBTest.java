package upbox.wmq.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.model.UUser;
import upbox.model.topic.TopicImg;
import upbox.model.topic.UUserTopic;
import upbox.pub.WebPublicMehod;
import upbox.service.TestMongoDBService;

/**
 * Spring + Mongo测试用例
 * 
 * @author wmq
 *
 *         15618777630
 */
@ContextConfiguration("applicationContext.xml")
public class MongoDBTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private TestMongoDBService mongoService;
	private Query query = new Query();
	
	@Ignore
	public void testFindOne() throws Exception{
		UUserTopic user = (UUserTopic) mongoService.findOne(query.addCriteria(Criteria.where("_id").is("785f2fba-4b99-4922-a622-14a4db201f8d")),UUserTopic.class);
		System.out.println(JSON.toJSONString(user));
	}

	@Ignore
	public void save() throws Exception {
		UUserTopic up = new UUserTopic();
		HashMap<String,Object> map3 = new HashMap<String,Object>();
		HashMap<String,Object> map4 = new HashMap<String,Object>();
		List l = new ArrayList<Object>();
		List ll = JSON
				.parseArray(
						"[{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"},{\"imgId\":\"\",\"createdate\":\"\",\"showUrl\":\"\"}]",
						TopicImg.class);

		up.setTopicName("测试");
		up.setUserId(WebPublicMehod.getUUID());
		up.setCuserId(WebPublicMehod.getUUID());
		up.setCreateTime(new Date());
		up.setImgUrl(ll);
		up.setCount(300);
		up.setDcount(200);
		up.setContent("test_test");
		up.set_id(WebPublicMehod.getUUID());
		for (int i = 0; i < 3; i++) {
			map3 = new HashMap<String,Object>();
			map4 = new HashMap<String,Object>();

			map4.put("buserId", WebPublicMehod.getUUID());
			map4.put("contentId", WebPublicMehod.getUUID());
			map4.put("createdate", new Date());
			map4.put("userId", WebPublicMehod.getUUID());
			map4.put("bcontent", "");
			map4.put("content", "测试第一层");

			map3.put("buserId", WebPublicMehod.getUUID());
			map3.put("contentId", WebPublicMehod.getUUID());
			map3.put("createdate", new Date());
			map3.put("userId", WebPublicMehod.getUUID());
			map3.put("bcontent", map4);
			map3.put("content", "测试第二层");

			l.add(map3);
		}
		up.setComment(l);
		up.setNum(1);
		System.out.println(JSON.toJSONString(up));
		mongoService.save(up);
	}

	@Test
	public void get() throws Exception {
		System.out.println(JSON.toJSONString(mongoService.getList()));;
	}
}
