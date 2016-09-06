package upbox.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UMall;
import upbox.model.UMallCourt;
import upbox.service.UMallService;;

/**
 * 商城基本信息接口实现类
 * 
 * @author xiao
 *
 */
@Service("uMallService")
public class UMallServiceImpl implements UMallService {
	@Resource
	private OperDAOImpl baseDAO;

	@Override
	public UMallCourt getUMallCourt(HashMap<String, String> map) throws Exception {
		return baseDAO.get(map,
				"from UMallCourt where courtId = :courtId and UMall.proId = :mallId");
	}
	@Override
	public void updateMallCount(HashMap<String, String> map, UMall mall, int saleCount, int type) throws Exception {
		if (null != mall) {
			if (1 == type) { // 减少库存
				List<UMall> mallList = baseDAO.find("from UMall where similarId = '" + mall.getSimilarId() + "'");
				for (UMall ma : mallList) {
					ma.setAllStock(ma.getAllStock() - saleCount);
					baseDAO.update(ma);
				}
			} else if (2 == type) { // 增加库存
				List<UMall> mallList = baseDAO.find("from UMall where similarId = '" + mall.getSimilarId() + "'");
				for (UMall ma : mallList) {
					ma.setAllStock(ma.getAllStock() + saleCount);
					baseDAO.update(ma);
				}
			}
			baseDAO.getSessionFactory().getCurrentSession().flush();
		}
		
	}
	

	/**
	 * 验证库存大小
	 * 
	 * @param count
	 * @return
	 * @throws Exception
	 */
	private int checkCourtCount(int count, UMall um) throws Exception {
		if (count >= 0) {
			return count;
		} else {
			throw new Exception(um.getName() + "库存不足");
		}
	}


	
	
}
