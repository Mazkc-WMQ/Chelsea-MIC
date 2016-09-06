package upbox.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import upbox.model.UUser;
import upbox.model.UUserImg;

/**
 * 前端用户图片接口
 */
public interface UUserImgService {
	/**
	 * 
	 * 
	   TODO - 上传头像 【2.0.0】
	   @param map
	   		loginUserId			前端用户Id
	   		pkId			图片主键Id
	   		imgSizeType		图片尺寸类型
	   		imgurl			图片显示地址
	   		weight			图片权重
	   		saveurl 		图片存储地址
	   @return
	   		uUserImg的hashMap
	   @throws Exception
	   2015年12月22日
	   dengqiuru
	 */
	public HashMap<String, Object> uploadHeadPic(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 新增/删除相册 【2.0.0】
	   @param map
	   		userId			前端用户Id
	   		pkId			图片主键Id
	   		imgSizeType		图片尺寸类型
	   		imgurl			图片显示地址
	   		weight			图片权重
	   		saveurl 		图片存储地址
	   @return
	   		uUserImg的hashMap
	   @throws Exception
	   2015年12月22日
	   dengqiuru
	 */
	public HashMap<String, Object> uploadGallery(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 获取相册列表 【2.0.0】
	   @param map
	   		userId  前端用户ID
	   @return
	   @throws Exception
	   2015年12月22日
	   dengqiuru
	 */
	public HashMap<String, Object> getGalleryList(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 根据用户获取头像 【2.0.0】
	   @param map
	   		userId  前端用户ID
	   @return
	   		UUserImg 对象
	   @throws Exception
	   2015年12月22日
	   dengqiuru
	 */
	public Set<UUserImg> getHeadPicByuserId(HashMap<String, String> map)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球员概况里的相册列表 【2.0.0】
	   @param map
	   		page  	 	分页
	   		userId		被查询相册的userId
	   @return
	   		galleryList集合
	   @throws Exception
	   2016年3月8日
	   dengqiuru
	 */
	public List<HashMap<String, Object>> getGalleryListInroughly(HashMap<String, String> map) throws Exception;

	/**
	 * 
	 * 
	   TODO - 获取球员头像对象 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   		UUserImg对象
	   @throws Exception
	   2016年3月19日
	   dengqiuru
	 */
	public UUserImg getHeadPicNotSetByuserId(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * 
	   TODO - 上传头像(赛事系统)
	   @param map
	   		loginUserId	前端用户Id
	   		pkId	图片主键Id
	   		imgSizeType	图片尺寸类型
	   		imgurl	图片显示地址
	   		imgWeight	图片权重
	   		saveurl 图片存储地址
	   @return
	   @throws Exception
	   2015广12朿22旿
	   dengqiuru
	 */
	public HashMap<String, Object> uploadHeadPicByEvents(HashMap<String, String> map,UUser uUser) throws Exception;
}
