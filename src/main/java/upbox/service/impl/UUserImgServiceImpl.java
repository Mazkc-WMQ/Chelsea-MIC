package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.org.pub.PublicMethod;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.PageLimitPhoto;
import upbox.model.UUser;
import upbox.model.UUserImg;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UUserImgService;
import upbox.service.UUserService;

/**
 * 前端端用户图片接口实现类
 *
 */
@Service("uuserImgService")
public class UUserImgServiceImpl implements UUserImgService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private UUserService uUserService;
	
	@Resource
	private PublicService publicService;

	/**
	 * 
	 * 
	   TODO - 上传头像
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
	@Override
	public HashMap<String, Object> uploadHeadPic(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String,Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String,Object>();//传参敿
		UUser uUser = uUserService.getUserinfoByUserId(map);//查user用户
		UUserImg uUserImg = new UUserImg();
		if (null != uUser) {
			hashMap.put("userId", map.get("loginUserId"));
			map.put("uimgUsingType", "1");//1代表头像
			uUserImg = baseDAO.get("from UUserImg where UUser.userId=:userId and uimgUsingType='1' and imgSizeType='1' ", hashMap);
			if (null == uUserImg) {//如果为空，则新增
				uUserImg = createUuserImg(map,uUser);
				resultMap.put("success", "头像上传成功！");
			}else{//否则更新
//				UpYunFileBucket.deleteFile(uUserImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图牿
				uUserImg = updateUuserImg(map,uUserImg);
				resultMap.put("success", "头像更新成功！");
			}
		}else{
			return WebPublicMehod.returnRet("error","查不到用户！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 更换头像
	   @param map
	   		imgSizeType		尺寸类型
	   		imgurl			显示地址
	   		imgWeight		权重
	   		saveurl			存放地址
	   @param uUserImg
	   		uUserImg对象
	   @return
	   		uUserImg对象
	   @throws Exception
	   2015广12朿22旿
	   dengqiuru
	 */
	private UUserImg updateUuserImg(HashMap<String, String> map, UUserImg uUserImg) throws Exception {
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uUserImg.setImgSizeType(map.get("imgSizeType"));//尺寸类型
		}else{
			uUserImg.setImgSizeType("1");
		}
		uUserImg.setImgurl(map.get("imgurl"));//显示地址
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uUserImg.setWeight(Integer.parseInt(map.get("imgWeight")));//权重
		}
		uUserImg.setSaveurl(map.get("saveurl"));//存放地址
		baseDAO.update(uUserImg);
		return uUserImg;
	}

	/**
	 * 
	 * 
	   TODO - 添加用户头像
	   @param map
	   		imgSizeType		尺寸类型
	   		imgurl			显示地址
	   		imgWeight		权重
	   		saveurl			存放地址
	   		uimgUsingType	图片类型
	   2015广12朿22旿
	   dengqiuru
	 * @throws Exception 
	 */
	private UUserImg createUuserImg(HashMap<String, String> map,UUser uUser) throws Exception {
		UUserImg uUserImg = new UUserImg();
		uUserImg.setPkId(WebPublicMehod.getUUID());
		uUserImg.setUUser(uUser);
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uUserImg.setImgSizeType(map.get("imgSizeType"));
		}else{
			uUserImg.setImgSizeType("1");//默认1
		}
		uUserImg.setUimgUsingType(map.get("uimgUsingType"));
		uUserImg.setImgurl(map.get("imgurl"));
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uUserImg.setWeight(Integer.parseInt(map.get("imgWeight")));
		}
		uUserImg.setSaveurl(map.get("saveurl"));
		baseDAO.save(uUserImg);
		
		return uUserImg;
	}

	/**
	 * 
	 * 
	   TODO - 新增/删除相册
	   		loginUserId	前端用户Id
	   		pkId	图片主键Id
	   		imgSizeType	图片尺寸类型
	   		uimgUsingType	图片应用类型
	   		imgurl	图片显示地址
	   		weight	图片权重
	   		saveurl 图片存储地址
	   @param map
	   @return
	   @throws Exception
	   2015广12朿22旿
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> uploadGallery(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String,Object>();//输出
		UUser uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
		UUserImg uUserImg = new UUserImg();
		if (publicService.StringUtil(map.get("pkId"))) {//如果pkId不为空：删除；为空：新增
			uUserImg = baseDAO.get(UUserImg.class, map.get("pkId"));
			if (null != uUserImg) {
				if ("2".equals(uUserImg.getUimgUsingType()) ) {
					this.deleteFile(map,uUserImg);
					resultMap.put("success", "删除成功!");
				}else{
					return WebPublicMehod.returnRet("error","该图片不属于相册！");
				}
			}else{
				return WebPublicMehod.returnRet("error","查不到该相册！");
			}
		}else{
			if (null != uUser) {
				map.put("uimgUsingType", "2");//1代表头像
				this.createUuserImg(map, uUser);
				resultMap.put("success", "上传成功!");
			}
			else{
				return WebPublicMehod.returnRet("error","查不到用户！");
			}
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 删除服务上的图片
	   @param map
	   		pkId		图片的Id
	   @param uUserImg
	   		uUserImg   对象
	   2016年3月24日
	   dengqiuru
	 * @throws Exception 
	 */
	private void deleteFile(HashMap<String, String> map, UUserImg uUserImg) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String,Object>();//删除方法传参
		hashMap.put("pkId", map.get("pkId"));
//		UpYunFileBucket.deleteFile(uUserImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图牿
		baseDAO.executeHql("delete from UUserImg where pkId=:pkId", hashMap);
	}

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
	@Override
	public HashMap<String, Object> getGalleryList(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String,Object> hashMap = new HashMap<String, Object>();//参数
		List<HashMap<String, Object>> galleryList = new ArrayList<HashMap<String, Object>>();
		hashMap.put("userId",map.get("userId"));
		String sql = "select i.pk_id pkId,i.imgurl imgurl,i.saveurl saveurl,i.uimg_using_type timgUsingType,i.user_id userId,i.weight weight from u_user_img i " 
				+" where i.uimg_using_type='2'"
				+" and i.user_id=:userId order by i.weight desc ";

		//分页
		if(null != map.get("page") && !"".equals(map.get("page"))){
			String maxWeight = "0";
			galleryList = this.getPageLimit(sql, map, hashMap);
			if (null != galleryList) {
				List<HashMap<String, Object>> list  = baseDAO.findSQLMap(sql,hashMap);
				if (null != list && list.size() > 0) {
					//获取最大权重：前端传权重 按最大权重值逐步递增
					if (null != list.get(0).get("weight")) {
						maxWeight = list.get(0).get("weight").toString();
					}else{
						maxWeight = "0";
					}
				}else{
					maxWeight = "0";
				}
			}
			resultMap.put("maxWeight", maxWeight);
		}
		resultMap.put("galleryList", galleryList);
		return resultMap;
	}

	/**
	 * 
	 * TODO 分页代码
	 * @param sql  语句
	 * @param hashMap 查询参数
	 * @param map 分页参数
	 * @param count 总记录数
	 * @return
	 * @throws Exception
	 * List<Object>
	 * xiao
	 * 2016年3月8日
	 */
	private List<HashMap<String, Object>> getPageLimit(String sql, HashMap<String, String> map,HashMap<String, Object> hashMap) throws Exception{
		hashMap.put("page", map.get("page"));
		PageLimitPhoto pa = new PageLimitPhoto(Integer.parseInt(map.get("page")), 0);
		hashMap.put("limit", pa.getLimit());
		hashMap.put("offset", pa.getOffset());
		StringBuffer newSql = new StringBuffer();
		newSql.append( sql + " limit :limit offset :offset" );
		List<HashMap<String, Object>> list  = baseDAO.findSQLMap(newSql.toString(),hashMap);
		return list;
	}
	

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
	@Override
	public Set<UUserImg> getHeadPicByuserId(HashMap<String, String> map) throws Exception {
		Set<UUserImg> uUserImg = new HashSet<>();
		if (publicService.StringUtil(map.get("userId"))) {
			UUserImg uUserImgTemp = baseDAO.get(map, "from UUserImg where UUser.userId=:userId and uimgUsingType='1' and imgSizeType='1' ");
			uUserImg.add(uUserImgTemp);
		}
		return uUserImg;
	}

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
	@Override
	public UUserImg getHeadPicNotSetByuserId(HashMap<String, String> map) throws Exception {
		UUserImg uUserImgTemp = new UUserImg();
		if (publicService.StringUtil(map.get("userId"))) {
			uUserImgTemp = baseDAO.get(map, "from UUserImg where UUser.userId=:userId and uimgUsingType='1' and imgSizeType='1' ");
		}
		return uUserImgTemp;
	}

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
	@Override
	public List<HashMap<String, Object>> getGalleryListInroughly(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> galleryList = new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("userId"))) {
			hashMap.put("userId", map.get("userId"));
			StringBuffer sql = new StringBuffer("select i.pk_id pkId,i.imgurl imgurl,i.saveurl saveurl,i.uimg_using_type timgUsingType from u_user_img i " 
					+" left join u_user u on i.user_id=u.user_id"
					+" where i.uimg_using_type='2'"
					+" and i.user_id=:userId order by i.weight desc  ");
			if (publicService.StringUtil(map.get("roughly"))) {
				if ("1".equals(map.get("roughly"))) {
//					hashMap.put("limit", Public_Cache.BEHAVIORCOUNT_INPLAYER);
					hashMap.put("limit", 5);
					sql.append(" limit :limit ");
				}
			}
			galleryList = baseDAO.findSQLMap(sql.toString(), hashMap);
			
		}
		return galleryList;
	}
	/**
	 * 
	 * 
	   TODO - 上传头像
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
	@Override
	public HashMap<String, Object> uploadHeadPicByEvents(HashMap<String, String> map,UUser uUser) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String,Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String,Object>();//传参敿
		UUserImg uUserImg = new UUserImg();
		if (null != uUser) {
			hashMap.put("userId", uUser.getUserId());
			map.put("uimgUsingType", "1");//1代表头像
			uUserImg = baseDAO.get("from UUserImg where UUser.userId=:userId and uimgUsingType='1' and imgSizeType='1' ", hashMap);
			if (null == uUserImg) {//如果为空，则新增
				uUserImg = createUuserImgByEvents(map,uUser);
				resultMap.put("success", "头像上传成功！");
			}else{//否则更新
//				UpYunFileBucket.deleteFile(uUserImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图牿
				uUserImg = updateUuserImgByEvents(map,uUserImg);
				resultMap.put("success", "头像更新成功！");
			}
		}else{
			return WebPublicMehod.returnRet("error","查不到用户！");
		}
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 添加用户头像
	   @param map
	   		imgSizeType		尺寸类型
	   		imgurl			显示地址
	   		imgWeight		权重
	   		saveurl			存放地址
	   		uimgUsingType	图片类型
	   2015广12朿22旿
	   dengqiuru
	 * @throws Exception 
	 */
	private UUserImg createUuserImgByEvents(HashMap<String, String> map,UUser uUser) throws Exception {
		UUserImg uUserImg = new UUserImg();
		uUserImg.setPkId(WebPublicMehod.getUUID());
		uUserImg.setUUser(uUser);
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uUserImg.setImgSizeType(map.get("imgSizeType"));
		}else{
			uUserImg.setImgSizeType("1");//默认1
		}
		uUserImg.setUimgUsingType(map.get("uimgUsingType"));
		uUserImg.setImgurl(map.get("uUserImgurl"));
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uUserImg.setWeight(Integer.parseInt(map.get("imgWeight")));
		}
		if (publicService.StringUtil(map.get("saveurl"))) {
			uUserImg.setSaveurl(map.get("saveurl"));
		}else{
			uUserImg.setImgurl(map.get("uUserImgurl"));
		}
		baseDAO.save(uUserImg);
		
		return uUserImg;
	}

	/**
	 * 
	 * 
	   TODO - 更换头像
	   @param map
	   		imgSizeType		尺寸类型
	   		imgurl			显示地址
	   		imgWeight		权重
	   		saveurl			存放地址
	   @param uUserImg
	   		uUserImg对象
	   @return
	   		uUserImg对象
	   @throws Exception
	   2015广12朿22旿
	   dengqiuru
	 */
	private UUserImg updateUuserImgByEvents(HashMap<String, String> map, UUserImg uUserImg) throws Exception {
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uUserImg.setImgSizeType(map.get("imgSizeType"));//尺寸类型
		}else{
			uUserImg.setImgSizeType("1");
		}
		uUserImg.setImgurl(map.get("uUserImgurl"));//显示地址
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uUserImg.setWeight(Integer.parseInt(map.get("imgWeight")));//权重
		}
		if (publicService.StringUtil(map.get("saveurl"))) {//存放地址
			uUserImg.setSaveurl(map.get("saveurl"));
		}else{
			uUserImg.setImgurl(map.get("uUserImgurl"));
		}
		baseDAO.update(uUserImg);
		return uUserImg;
	}
}
