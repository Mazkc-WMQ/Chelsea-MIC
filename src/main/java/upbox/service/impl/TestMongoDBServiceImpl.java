package upbox.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

import upbox.dao.impl.MongoOperDAOImpl;
import upbox.model.topic.TopicComment;
import upbox.model.topic.UUserTopic;
import upbox.pub.WebPublicMehod;
import upbox.service.TestMongoDBService;

@Service("mongoService")
public class TestMongoDBServiceImpl implements TestMongoDBService {
	@Resource
	private MongoOperDAOImpl mongoDao;
	

	@Override
	public Object findOne(Query query,Class c) throws Exception {
		return mongoDao.findOne(
				query,
				c);
	}

	@Override
	public void save(Object e) throws Exception {
		mongoDao.save(e);
	}

	@Override
	public List getList() throws Exception {
		int[] o = new int[]{0,1};
		BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
		BasicDBObject dbo2 = new BasicDBObject(); //内嵌分页
		BasicDBObject dbo3 = new BasicDBObject(); //排序
		dbo3.put("comment.createdate", 1);
		dbo3.put("createdate", 1);
		dbo1.put("_id", "785f2fba-4b99-4922-a622-14a4db201f8d");
		dbo2.put("comment", new BasicDBObject("$slice", o)); //按照评论内容进行分页
		List l = mongoDao.getListByZH("u_user_topic", dbo1, dbo2,dbo3);
		return l;
	}

	@Override
	public void saveAS() throws Exception {
		 TopicComment tag = new TopicComment();
	     tag.setContentId(WebPublicMehod.getUUID());
	     Query query = Query.query(Criteria.where("_id").is("785f2fba-4b99-4922-a622-14a4db201f8d"));
	     mongoDao.saveAs(query, UUserTopic.class, "comment",tag);
	}

	@Override
	public void deleteAs() throws Exception {
		 Query query = Query.query(Criteria.where("_id").is("785f2fba-4b99-4922-a622-14a4db201f8d").and("comment.contentId").is("aadad8b5-b6a4-45de-aa94-0c5c86998c4a"));
		 //mongoDao.removeTag(query, "comment", UUserTopic.class);
	}
}
