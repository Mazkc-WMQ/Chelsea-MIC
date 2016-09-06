package upbox.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.org.dao.impl.BaseDAOImpl;

@Repository("baseDAO")
@SuppressWarnings("all")
public class OperDAOImpl extends BaseDAOImpl {
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		super.sessionFactory = sessionFactory;
	}
}
