package upbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;


import upbox.dao.impl.OperDAOImpl;
import upbox.model.PageLimitPhoto;
import upbox.model.UPlayer;
import upbox.model.UPlayerRole;
import upbox.model.UTeam;
import upbox.model.UTeamImg;
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UPlayerRoleLimitService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.UTeamImgService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 前端端用户接口实现类
 *
 */
@Service("uteamImgService")
public class UTeamImgServiceImpl implements UTeamImgService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UTeamService uTeamService;
	
	@Resource
	private UPlayerService uPlayerService;
	
	@Resource
	private UUserService uUserService;
	
	@Resource
	private UPlayerRoleService uPlayerRoleService;

	@Resource
	private UPlayerRoleLimitService uPlayerRoleLimitService;
	/**
	 * 建立球队后，新增队徽
	 * map:
	 *  imgSizeType 	图片尺寸类型
	 *  imgurl			图片显示地址
	 *  imgWeight		图片权重
	 *  saveurl			图片保存地址
	 *  
	 */
	@Override
	public HashMap<String, Object> insertTeamLogo(HashMap<String, String> map,UUser uUser,UTeam uTeam) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != uTeam) {
			//判断该用户是否属于该球队
			String typeStr = "1";
			map.put("teamId", uTeam.getTeamId());
			UPlayer uPlayer = uPlayerService.getUteamUplayerinfoByTeamId(map,typeStr);
			if (null != uPlayer) {
				String membertype = "1";
				List<UPlayerRole> uPlayerRoles = uPlayerRoleService.getMemberTypeByPlayerId202(map);
				if (null != uPlayerRoles && uPlayerRoles.size() > 0) {
					for (UPlayerRole uPlayerRole : uPlayerRoles) {
						if (!"1".equals(uPlayerRole.getMemberType())) {
							membertype = uPlayerRole.getMemberType();
						}
					}
				}
				//判断是否为队长
				if (membertype.equals("1")) {
					String timgUsingType = "1";
					UTeamImg uTeamImg = this.saveUTeamImg(timgUsingType,uTeam,uUser,map);
					if (null != uTeamImg) {
						resultMap.put("success", "队徽上传成功！");
					}else{
						resultMap.put("success", "队徽上传失败！");
					}
				}else{
					return WebPublicMehod.returnRet("error","你不是本队队长，无法上传队徽！");
				}
			}
		}else{
			return WebPublicMehod.returnRet("error","查不到球队信息！");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 新增图片信息
	   @param uTeam
	   @param uUser
	   @param map
	   		imgSizeType		尺寸类型
	   		saveurl			存放地址
	   		imgWeight		权重
	   		imgurl			存放地址
	   @return
	   @throws Exception
	   2016年3月24日
	   dengqiuru
	 */
	private UTeamImg saveUTeamImg(String timgUsingType,UTeam uTeam, UUser uUser, HashMap<String, String> map) throws Exception {
		UTeamImg uTeamImg = new UTeamImg();
		try {
			uTeamImg.setTeamImgId(WebPublicMehod.getUUID());
			if (publicService.StringUtil(map.get("imgSizeType"))) {
				uTeamImg.setImgSizeType(map.get("imgSizeType"));
			}else{
				uTeamImg.setImgSizeType("1");
			}
			if (publicService.StringUtil(map.get("saveurl"))) {
				uTeamImg.setSaveurl(map.get("saveurl"));
			}
			if (publicService.StringUtil(map.get("imgWeight"))) {
				uTeamImg.setImgWeight(Integer.parseInt(map.get("imgWeight")));
			}
			uTeamImg.setTimgUsingType(timgUsingType);
			uTeamImg.setImgurl(map.get("imgurl"));
			uTeamImg.setUTeam(uTeam);
			uTeamImg.setUUser(uUser);
			baseDAO.save(uTeamImg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uTeamImg;
		
	}


	/**
	 * 
	 * 
	   TODO - 更新队徽
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
	@Override
	public HashMap<String, Object> UpdateTeamLogo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//查看球队是否存在
		UTeam uTeam = uTeamService.findPlayerInfoById(map);
		UTeamImg uTeamImg = new UTeamImg();
		map.put("timgUsingType", "1");//1代表头像
		//判断该用户是否属于该球队
		String typeStr = "1";
		UPlayer uPlayer = uPlayerService.getUteamUplayerinfoByTeamId(map,typeStr);
		if (null != uPlayer) {
			//判断是否为队长
//			if (uPlayer.getMemberType().equals("1")) {
				if (null != uTeam) {
					//查看该球队是否存在队徽
					uTeamImg = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and timgUsingType='1' and imgSizeType='1' ");
					UUser uUser = uUserService.getUserinfoByUserId(map);
					if (null != uTeamImg) {//不为空，更新
//						UpYunFileBucket.deleteFile(uTeamImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图片   更新队徽不能删除队徽
						String type = "1";
						uTeamImg = updateTeamImg(map,uTeamImg,type,uTeam,uUser);
					}else{//为空，新增
						String type = "2";
						uTeamImg = saveTeamImg(map,uTeamImg,type,uTeam,uUser);
					}
				}else{
					return WebPublicMehod.returnRet("error","查不到球队信息！");
				}
//			}else{
//				return WebPublicMehod.returnRet("error","你不是本队队长，无法更换队徽！");
//			}
		}else{
			return WebPublicMehod.returnRet("error","你不是本队球员，无法更换队徽！");
		}
		resultMap.put("success", "队徽更新成功！");
		return resultMap;
	}
	/**
	 * 
	 * 
	   TODO - 更新队徽
	   @param map
	   		imgSizeType		尺寸类型
	   		saveurl			存放地址
	   		imgWeight		权重
	   		imgurl			存放地址
	   @param uTeamImg
	   @param type
	   @param uTeam
	   @return
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	private UTeamImg saveTeamImg(HashMap<String, String> map,UTeamImg uTeamImg, String type,UTeam uTeam,UUser uUser) throws Exception {
		UTeamImg uTeamImgTemp = new UTeamImg();
		uTeamImgTemp.setTeamImgId(WebPublicMehod.getUUID());
		uTeamImgTemp.setUTeam(uTeam);
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uTeamImgTemp.setImgSizeType(map.get("imgSizeType"));
		}else{
			uTeamImgTemp.setImgSizeType("1");
		}
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uTeamImgTemp.setImgWeight(Integer.parseInt(map.get("imgWeight")));
		}
		uTeamImgTemp.setTimgUsingType(map.get("timgUsingType"));
		uTeamImgTemp.setImgurl(map.get("imgurl"));
		uTeamImgTemp.setSaveurl(map.get("saveurl"));
		uTeamImgTemp.setUUser(uUser);
		baseDAO.save(uTeamImgTemp);
		return uTeamImgTemp;
	}

	/**
	 * 
	 * 
	   TODO - 更新队徽
	   @param map
	   		imgSizeType		尺寸类型
	   		saveurl			存放地址
	   		imgWeight		权重
	   		imgurl			存放地址
	   @param uTeamImg
	   @param type
	   @param uTeam
	   @return
	   @throws Exception
	   2016年2月3日
	   dengqiuru
	 */
	private UTeamImg updateTeamImg(HashMap<String, String> map,UTeamImg uTeamImg, String type,UTeam uTeam,UUser uUser) throws Exception {
		if (publicService.StringUtil(map.get("imgSizeType"))) {
			uTeamImg.setImgSizeType(map.get("imgSizeType"));
		}else{
			uTeamImg.setImgSizeType("1");
		}
		if (publicService.StringUtil(map.get("imgWeight"))) {
			uTeamImg.setImgWeight(Integer.parseInt(map.get("imgWeight")));
		}
		uTeamImg.setImgurl(map.get("imgurl"));
		uTeamImg.setSaveurl(map.get("saveurl"));
		baseDAO.update(uTeamImg);
		return uTeamImg;
	}


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
	@Override
	public HashMap<String, Object> uploadTeamGallery(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
		if (null != uTeam) {
			if (null == map.get("appCode") || "".equals(map.get("appCode")) || "null".equals(map.get("appCode"))) {
				HashMap<String, Object> teamStatusMap = uTeamService.checkAllUTeamStatus(map);
				if ("3".equals(teamStatusMap.get("success").toString())) {
					return WebPublicMehod.returnRet("error","-2^<}}当前球队审核未通过\n请重新登记信息");
				}
			}
			UTeamImg uTeamImg = new UTeamImg();
			//判断该用户是否属于该球队
//			String typeStr = "1";
//			UPlayer uPlayer = uPlayerService.getUteamUplayerinfoByTeamId(map,typeStr);
			UUser uUser = uUserService.getUserinfoByUserId(map);//查询用户对象
			if (publicService.StringUtil(map.get("teamImgId"))) {//如果pkId不为空：删除；为空：新增
				uTeamImg = baseDAO.get(UTeamImg.class, map.get("teamImgId"));
				if (null != uTeamImg) {
					List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
					if (null != playerIds && playerIds.size() > 0) {
						if (uTeamImg.getTimgUsingType().equals("2")) {
							hashMap.put("teamImgId", map.get("teamImgId"));
//								UpYunFileBucket.deleteFile(uTeamImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图片
							baseDAO.executeHql("delete from UTeamImg where teamImgId=:teamImgId", hashMap);
							resultMap.put("success", "删除成功!");
						}else{
							return WebPublicMehod.returnRet("error","该球队照片不属于相册！");
						}
					}else{
						if (uUser.getUserId().equals(uTeamImg.getUUser().getUserId())) {
							if (uTeamImg.getTimgUsingType().equals("2")) {
								hashMap.put("teamImgId", map.get("teamImgId"));
//									UpYunFileBucket.deleteFile(uTeamImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图片
								baseDAO.executeHql("delete from UTeamImg where teamImgId=:teamImgId", hashMap);
								resultMap.put("success", "删除成功!");
							}else{
								return WebPublicMehod.returnRet("error","该球队照片不属于相册！");
							}
						}else{
							return WebPublicMehod.returnRet("error","只能编辑自己上传的球队照片！");
						}
					}
				}else{
					return WebPublicMehod.returnRet("error","查不到该相片！");
				}
			}else{
				String type = "2";
				map.put("timgUsingType", "2");//1代表头像
				uTeamImg = saveTeamImg(map,uTeamImg,type,uTeam,uUser);
				resultMap.put("success", "上传成功!");
			}
		}
		return resultMap;
	}

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
	@Override
	public HashMap<String, Object> getTeamGalleryList(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String,Object> hashMap = new HashMap<String, Object>();//参数
		List<HashMap<String, Object>> galleryList=new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("teamId"))) {
			UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
			if (null != uTeam) {
				hashMap.put("teamId",map.get("teamId"));
				String sql = "select i.team_img_id teamImgId,i.imgurl imgurl,i.saveurl saveurl,i.timg_using_type timgUsingType,i.user_id userId,i.img_weight imgWeight from u_team_img i" 
						+" where i.timg_using_type='2'"
						+" and i.team_id=:teamId order by i.img_weight desc ";
				//分页
				if(null != map.get("page")&& !"".equals(map.get("page"))){
					galleryList = this.getPageLimit(sql, map, hashMap);
					//最大权重值
					String maxWeight = "0";
					if (null != galleryList) {
						List<HashMap<String, Object>> list  = baseDAO.findSQLMap(sql,hashMap);
						if (null != list && list.size() > 0) {
							maxWeight = list.get(0).get("imgWeight").toString();
						}else{
							maxWeight = "0";
						}
					}
					resultMap.put("maxWeight", maxWeight);
				}
				resultMap.put("galleryList", galleryList);
			}else{
				return WebPublicMehod.returnRet("error","当前球队不存在！");
			}
		
		}else{
			return WebPublicMehod.returnRet("error","球队Id不能为空！");
		}
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
	   TODO - 根据teamId获取队徽  【2.0.0】
	   @param map
	   		teamId	球队Id
	   @return
	   		Set<UTeamImg>
	   @throws Exception
	   2016年2月29日
	   dengqiuru
	 */
	@Override
	public Set<UTeamImg> getTeamLogoByTeamId(HashMap<String, String> map) throws Exception {
		//查看球队是否存在
		Set<UTeamImg> uTeamImg = new HashSet<>();
		UTeamImg uTeamImgTemp = new UTeamImg();
		if (publicService.StringUtil(map.get("teamId"))) {
			uTeamImgTemp = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and timgUsingType='1' and imgSizeType='1' ");
			uTeamImg.add(uTeamImgTemp);
		}
		return uTeamImg;
	}

//	@Override
//	public HashMap<String, Object> deleteTeamGallery(HashMap<String, String> map) throws Exception {
//		HashMap<String, Object> resultMap = new HashMap<>();
//		HashMap<String, Object> hashMap = new HashMap<>();
//		//查看球队是否存在
//		UTeam uTeam = uTeamService.findPlayerInfoById(map);
//		UUser uUser = uUserService.getUserinfoByUserId(map);//查询用户对象
//		UTeamImg uTeamImg = null;
//		if (uTeam != null) {
//			if (null != map.get("teamImgId") && !map.get("teamImgId").equals("")) {//如果pkId不为空：删除；为空：新增
//				uTeamImg = baseDAO.get(UTeamImg.class, map.get("teamImgId"));
//				if (null != uTeamImg) {
//					//判断是否是上传者
//					if (uUser == uTeamImg.getUUser()) {
//						if (uTeamImg.getTimgUsingType() == "2" && uTeamImg.getTimgUsingType().equals("2")) {
//							hashMap.put("teamImgId", map.get("teamImgId"));
//							UpYunFileBucket.deleteFile(uTeamImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图片
//							baseDAO.executeHql("delete from UTeamImg where teamImgId=:teamImgId", hashMap);
//						}else{
//							return WebPublicMehod.returnRet("error","该图片不属于相册！");
//						}
//					}else{
//						return WebPublicMehod.returnRet("error","只能编辑自己上传的球队照片！");
//					}
//				}else{
//					return WebPublicMehod.returnRet("error","查不到该相册！");
//				}
//			}else{
//				return WebPublicMehod.returnRet("error","teamImgId不能为空！");
//			}
//		}else{
//			return WebPublicMehod.returnRet("error","查不到球队！");
//		}
//		resultMap.put("uTeamImg", uTeamImg);
//		return resultMap;
//	}
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
	@Override
	public List<HashMap<String, Object>> getGalleryListInroughly(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> galleryList = new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("teamId"))) {
			hashMap.put("teamId", map.get("teamId"));
			String sql = "select i.team_img_id teamImgId,i.imgurl imgurl,i.saveurl saveurl,i.timg_using_type timgUsingType from u_team_img i" 
					+" left join u_team t on i.team_id=t.team_id"
					+" where i.timg_using_type='2'"
					+" and i.team_id=:teamId order by i.img_weight desc ";
			galleryList = baseDAO.findSQLMap(sql, hashMap);
			
		}
		return galleryList;
	}

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
	@Override
	public UTeamImg getHeadPicNotSetByTeamId(HashMap<String, String> map) throws Exception {
		UTeamImg uTeamImg = new UTeamImg();
		if (publicService.StringUtil(map.get("teamId"))) {
			uTeamImg = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and timgUsingType='1' and imgSizeType='1' ");
		}
		return uTeamImg;
	}
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
	@Override
	public HashMap<String, Object> getGalleryListByUserId(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String,Object> hashMap = new HashMap<String, Object>();//参数
		List<HashMap<String, Object>> galleryList=new ArrayList<HashMap<String, Object>>();
		if (publicService.StringUtil(map.get("teamId"))) {
			if (publicService.StringUtil(map.get("loginUserId"))) {
				hashMap.put("teamId",map.get("teamId"));
				hashMap.put("userId",map.get("loginUserId"));
				String sql = null;
				//查找解散的用户是否有权限
				//1:踢人；2：修改球队信息；3：删除相册；4：球队解散；5：发起约战；6：发起挑战；7：赛事;8:邀请；9：招募
				map.put("type", "3");
				List<HashMap<String, Object>> playerIds = uPlayerRoleLimitService.playerIsRoleByType(map);
				if (null != playerIds && playerIds.size() > 0) {//有权限  获取全部的相册
					sql = "select i.team_img_id teamImgId,i.imgurl imgurl,i.saveurl saveurl,i.timg_using_type timgUsingType,i.user_id userId,i.img_weight imgWeight from u_team_img i" 
							+" where i.timg_using_type='2' "
							+" and i.team_id=:teamId order by i.img_weight desc ";
				}else{
					sql = "select i.team_img_id teamImgId,i.imgurl imgurl,i.saveurl saveurl,i.timg_using_type timgUsingType,i.user_id userId,i.img_weight imgWeight from u_team_img i" 
							+" where i.timg_using_type='2' and i.user_id=:userId "
							+" and i.team_id=:teamId order by i.img_weight desc ";
				}
				//分页
				if(null != map.get("page")&& !"".equals(map.get("page"))){
					galleryList = this.getPageLimit(sql, map, hashMap);
					//最大权重值
					String maxWeight = "0";
					if (null != galleryList) {
						List<HashMap<String, Object>> list  = baseDAO.findSQLMap(sql,hashMap);
						if (null != list && list.size() > 0) {
							maxWeight = list.get(0).get("imgWeight").toString();
						}else{
							maxWeight = "0";
						}
					}
					resultMap.put("maxWeight", maxWeight);
				}
			}
		}
		resultMap.put("galleryList", galleryList);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 更新队徽
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
	@Override
	public HashMap<String, Object> updateTeamLogoByEvents(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		//查看球队是否存在
		UTeam uTeam = uTeamService.findPlayerInfoById(map);
		UTeamImg uTeamImg = new UTeamImg();
		map.put("timgUsingType", "1");//1代表头像
		if (null != uTeam) {
			//查看该球队是否存在队徽
			uTeamImg = baseDAO.get(map, "from UTeamImg where UTeam.teamId=:teamId and timgUsingType='1' and imgSizeType='1' ");
			UUser uUser = uUserService.getUserinfoByUserId(map);
			if (null != uTeamImg) {//不为空，更新
//						UpYunFileBucket.deleteFile(uTeamImg.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[0]);//删除第三方图片   更新队徽不能删除队徽
				String type = "1";
				uTeamImg = updateTeamImg(map,uTeamImg,type,uTeam,uUser);
			}else{//为空，新增
				String type = "2";
				uTeamImg = saveTeamImg(map,uTeamImg,type,uTeam,uUser);
			}
		}else{
			return WebPublicMehod.returnRet("error","查不到球队信息！");
		}
		resultMap.put("success", "队徽更新成功！");
		return resultMap;
	}
}
