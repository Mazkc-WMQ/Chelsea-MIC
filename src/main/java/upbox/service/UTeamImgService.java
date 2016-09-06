package upbox.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import upbox.model.UTeam;
import upbox.model.UTeamImg;
import upbox.model.UUser;

/**
 * 前端球队图片接口
 */
public interface UTeamImgService {
	/**
	 * 
	 * 
	   TODO - 新建球队时，添加队徽 【2.0.0】
	   @param map
	   		teamId			球队Id
	   		imgSizeType		图片尺寸类型
			imgurl			图片显示地址
			imgWeight		图片权重
			saveurl			图片存放地址
	   @param uUser
	   		uUser对象
	   @return
	   		uTeamImg的hashMap<String,Object>
	   @throws Exception
	   2016年3月7日
	   dengqiuru
	 */
	public HashMap<String, Object> insertTeamLogo(HashMap<String, String> map,UUser uUser,UTeam uTeam)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 更新队徽 【2.0.0】
	   @param map
	   		userId			当前用户Id
	   		teamId			球队Id
	   		imgSizeType		图片尺寸类型
			imgurl			图片显示地址
			imgWeight		图片权重
			saveurl			图片存放地址
	   @return
	   		uTeamImg的hashMap<String,Object>
	   @throws Exception
	   2016年2月2日
	   dengqiuru
	 */
	public HashMap<String, Object> UpdateTeamLogo(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 更新、编辑相册 【2.0.0】
	   @param map
	   		teamId			球队Id
	   		teamImgId		球队相册Id
	   		imgSizeType		图片尺寸类型
			imgurl			图片显示地址
			imgWeight		图片权重
			saveurl			图片存放地址
	   @return
	   		uTeamImg的hashMap<String,Object>
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	public HashMap<String, Object> uploadTeamGallery(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 获取相册列表 【2.0.0】
	   @param map
	   		page    分页
	   		teamId	球队Id
	   @return
	   		galleryList的hashMap<String,Object>
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	public HashMap<String, Object> getTeamGalleryList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据teamId获取队徽  【2.0.0】
	   @param map
	   		teamId	球队Id
	   @return
	   		Set<UTeamImg>
	   @throws Exception
	   2016年2月29日
	   dengqiuru
	 */
	public Set<UTeamImg> getTeamLogoByTeamId(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 获取球队队徽对象 【2.0.0】
	   @param map
	   		teamId		球队Id
	   @return
	   		UTeamImg对象
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public UTeamImg getHeadPicNotSetByTeamId(HashMap<String, String> map)throws Exception;
	/**
	 * 
	 * 
	   TODO - 战队详情--概况 【2.0.0】
	   @param map
	   		teamId	球队Id
	   @return
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	public List<HashMap<String, Object>> getGalleryListInroughly(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 获取当前用户在球队上传的相册列表 【2.0.0】
	   @param map
	   		teamId	球队Id
	   		loginUserId  当前用户Id
	   @return
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	public HashMap<String, Object> getGalleryListByUserId(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 更新队徽(赛事系统)
	   @param map
	   		teamId			球队Id
	   		imgSizeType		尺寸类型
	   		saveurl			存放地址
	   		imgWeight		权重
	   		imgurl			存放地址
	   @return
	   @throws Exception
	   2016年2月2日
	   dengqiuru
	 */
	public HashMap<String, Object> updateTeamLogoByEvents(HashMap<String, String> map) throws Exception;
}
