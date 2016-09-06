package upbox.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedisPool;

import com.org.dao.impl.BaseDAOImpl;
import com.org.mongodb.impl.MongodbDaoImpl;
import com.org.redis.dao.impl.RedisUtilDaoImpl;


@Repository("mongoDao")
@SuppressWarnings("all")
public class MongoOperDAOImpl extends MongodbDaoImpl {
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}