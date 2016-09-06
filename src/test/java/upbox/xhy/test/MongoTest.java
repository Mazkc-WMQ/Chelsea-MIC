package upbox.xhy.test;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;

import net.sf.json.JSONObject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import upbox.dao.impl.MongoOperDAOImpl;
import upbox.model.topic.TopicComment;
import upbox.model.topic.UUserTopic;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class MongoTest {
    @Resource
    private MongoOperDAOImpl mongoDao;

    @Ignore
    public void test() throws Exception {
        CommandResult result = this.mongoDao.findCommand("{'_id':'785f2fba-4b99-4922-a622-14a4db201f8d'}");
        System.out.println();
    }


    @Ignore
    public void getCommentById() throws Exception {
        BasicDBObject dbo1 = new BasicDBObject(); //��Ҫ������ѯ
        BasicDBObject dbo2 = new BasicDBObject();
        dbo1.put("_id", "785f2fba-4b99-4922-a622-14a4db201f8d");
        dbo2.put("comment", new BasicDBObject("$elemMatch",new BasicDBObject("contentId","aadad8b5-b6a4-45de-aa94-0c5c86998c4a")));
        List l = mongoDao.getListByZH("u_user_topic", dbo1, dbo2, null);
        System.out.println(JSON.toJSONString(l));
//        UUserTopic topic = (UUserTopic) JSONObject.toBean(JSONObject.fromObject(l.get(0)), UUserTopic.class);
//        List<TopicComment> comments = (List<TopicComment>) topic.getComment();

    }
    @Test
    public void delete() throws Exception{

        Query query = Query.query(Criteria.where("_id").is("a2af5db1-8c74-4d24-a38b-468dbb642ead"));
        query.with(new Sort(new Order(Direction.DESC,"createdate")).and(new Sort(new Order(Direction.ASC,"11"))));
        mongoDao.removeTag(query, "comment",new BasicDBObject("contentId","2336c33d-72cc-45be-a570-0706bb14e3ef"), UUserTopic.class);
    }
}
