package upbox.service;

import java.util.List;

import upbox.model.UPublicPrpe;


/**
 * 项目公共参数service
 * @author charlescai
 *
 */
public interface PublicPropretiesService {

	/**
	 * 获取项目公共参数
	 * @param name 参数名
	 * @return
	 */
	public List<UPublicPrpe> getPublicPropreties();
}
