package upbox.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UPublicPrpe;
import upbox.service.PublicPropretiesService;

@Service("publicPropreties")
public class PublicPropretiesServiceImpl implements PublicPropretiesService {

	@Resource
	private OperDAOImpl baseDAO;
	
	/**
	 * 获取项目公共参数
	 */
	@Override
	public List<UPublicPrpe> getPublicPropreties(){
		// TODO Auto-generated method stub
		List<UPublicPrpe> result = new ArrayList<UPublicPrpe>();
		try {
			result = baseDAO.find(" from UPublicPrpe ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
