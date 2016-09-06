package upbox.service;

import java.util.HashMap;

import upbox.model.UMall;
import upbox.model.UMallCourt;

/**
 * 商品信息接口
 * 
 * @author 
 *
 */
public interface UMallService {

	
	public UMallCourt getUMallCourt(HashMap<String, String> map) throws Exception;
	
	/**
	 * 
	 * TODO 更新商品库存、更具商品ID更新同类商品所有库存
	 * @param map
	 * @param mall
	 * @param saleCount
	 * @param type
	 * @throws Exception
	 * void
	 * xiao
	 * 2016年2月22日
	 */
	public void updateMallCount(HashMap<String, String> map, UMall mall, int saleCount, int type) throws Exception;

}
