package upbox.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

public interface TestMongoDBService {
	/**
	 * 
	 * 
	   TODO - 查找一条数据
	   @return
	   @throws Exception
	   2016年7月27日
	   mazkc
	 */
	public Object findOne(Query query,Class c)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 保存数据
	   @param e
	   @throws Exception
	   2016年7月27日
	   mazkc
	 */
	public void save(Object e)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据条件查找出多条数据
	   @return
	   @throws Exception
	   2016年7月27日
	   mazkc
	 */
	public List getList()throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 插入内嵌文档数据
	   @return
	   @throws Exception
	   2016年7月27日
	   mazkc
	 */
	public void saveAS()throws Exception;
	
	/**
	 * 
	 * 
	   TODO -删除内嵌文档数据
	   @return
	   @throws Exception
	   2016年7月27日
	   mazkc
	 */
	public void deleteAs()throws Exception;
}
