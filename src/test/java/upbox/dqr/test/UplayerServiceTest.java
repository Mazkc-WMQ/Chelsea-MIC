package upbox.dqr.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.model.UPlayer;
import upbox.model.UUser;
import upbox.service.UPlayerService;

/**
 * 测试前端用户接口 -- uUserService
 * @author mercideng
 *
 */
@ContextConfiguration("applicationContext.xml")
public class UplayerServiceTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private UPlayerService uPlayerService;
	private HashMap<String, Object> resultMap;
	
	private UUser uUser;
	
	private UPlayer uPlayer;
	//UpYunFileBucket.deleteFile(ubi.getSaveurl().split(UpYunFileBucket.BUCKET_NAME)[1]);
	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	   dengqiuru
	 */
	public void printInfo(String temp,Object obj){
		//System.out.println(temp + JSON.toJSONString(obj));
	}
	
	/**
	 * 
	 * 
	   TODO - 编辑球员信息
	   @param map
	   		userId		用户ID
	   		playerId	球员ID
	   @param uUser	
	   		height 		身高
	   		weight 		体重
	   @param uPlayer
	   		number		背号
	   		memberType	球员用户身份   必传
	   		position	位置（1-前锋 2-后卫 3-守门员 ）
	   		nickname	外号
	   		remark		简介
	   		teamId		球队信息
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	   2.03
	   http://localhost:8080/ssh/uuser_editPlayerInfo.do?userId=a08dbfc4-f745-4375-b276-b74de068f914&playerId=null&height=203&weight=240&number=23&nickname=xiaozanzan&remark=123&teamId=null&memberType=null&position=
	 */
//	@Test
//	public void editPlayerInfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		uUser = new UUser();
//		uPlayer = new UPlayer();
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		hashMap.put("playerId", "");
//		hashMap.put("teamId", "");
//		//用户编辑信息
//		uUser.setHeight("111");
//		uUser.setWeight("222");
//		//球员编辑信息
//		uPlayer.setNumber(23);
//		uPlayer.setNickname("小詹詹222111111");
//		uPlayer.setRemark(null);
//		uPlayer.setMemberType("1");
//		uPlayer.setPosition("1");
//		resultMap = uPlayerService.editPlayerInfo(hashMap, uUser, uPlayer);
//		printInfo("------------------------------",resultMap);
//	}
	
//	/**
//	 * 
//	 * 
//	   TODO - 根据球员Id获取球员信息，以及他所在的球队列表
//	   	hashMap
//	   		token
//	   		userStatus
//	   		playerId
//	   @throws Exception
//	   2016年1月29日
//	   dengqiuru
//	   http://localhost:8080/Chelsea-MIC/uplayer_getUPlayerinfoByPlayerId.do?userStatus=1&token=0db3b257-6eef-451c-97c4-0a35463732ab&playerId=81c2832c-0ba7-4c09-a3ed-b525fdab4bce
//	 */
//	@Test
//	public void getUTeamListByPlayerId() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("page", "0");
//		hashMap.put("playerId", "89ce2a5c-6506-4bba-b111-9f166c15f428");
//		resultMap = uPlayerService.getUTeamListByPlayerId(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	
	/**
	 * 
	 * 
	   TODO - 战队详情   队员列表
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	 */
//	@Test
//	public void listPlayerInfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "6f0272bc-955b-464d-b0a9-53e382ba3ad6");
//		resultMap = uPlayerService.listPlayerInfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}

	/**
	 * 
	 * 
	   TODO - 球员接口   球员列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
//	@Test
//	public void getUplayerList() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("page", "0");
//		resultMap = uPlayerService.getUPlayerList(hashMap);
//		printInfo("------------------------------",resultMap);
//	}

	

	/**
	 * 
	 * 
	   TODO - 球员详情  概况
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
//	@Test
//	public void getUplayerinfoByPlayerId() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("playerId", "003cb4cd-1910-4ba6-b475-e2fc21113054");
//		resultMap = uPlayerService.getUplayerinfoByPlayerId(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 球员详情  相册列表
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
//	@Test
//	public void getUplayerGalleryList() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("playerId", "003cb4cd-1910-4ba6-b475-e2fc21113054");
//		resultMap = uPlayerService.getUplayerGalleryList(hashMap);
//		printInfo("------------------------------",resultMap);
//	}

	/**
	 * 
	 * 
	   TODO - 球员详情  编辑相册
	   @param map
	   @return
	   @throws Exception
	   2016年2月19日
	   dengqiuru
	 */
//	@Test
//	public void updateUplayerGallery() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("playerId", "003cb4cd-1910-4ba6-b475-e2fc21113054");
//		hashMap.put("userId", "66c7ddcf-cdee-4cee-90c5-4dcc37771ac0");
//		hashMap.put("pkId", "ab094bb8-3add-4ae4-bc8b-5214bcc10ecc");
//		hashMap.put("imgSizeType", "1");
//		hashMap.put("imgurl", "22222");
//		hashMap.put("weight", "123");
//		hashMap.put("saveurl", "222222222");
//		resultMap = uPlayerService.updateUplayerGallery(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
//	
//	/**
//	 * 
//	 * 
//	   TODO - 有/无球队的球员查询
//	   @param map
//	   @return
//	   @throws Exception
//	   2016年2月19日
//	   dengqiuru
//	 */
//	@Test
//	public void getUPlayerinfoInUteam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("playerId", "95fed6f2-ebe4-4301-9da1-24ef854aa694");
//		resultMap = uPlayerService.getUPlayerinfoInUteam(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	
//	/**
//	 * 
//	 * 
//	   TODO - 球员详情 头部
//	   @param map
//	   @return
//	   @throws Exception
//	   2016年2月19日
//	   dengqiuru
//	 */
//	@Test
//	public void roughlyStateOfUPlayerHead() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("playerId", "79dc7f7c-b261-4c94-a442-89bce2642887");
//		resultMap = uPlayerService.roughlyStateOfUPlayerHead(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
//	/**
//	 * 
//	 * 
//	   TODO - 球员详情 详情
//	   @param map
//	   @return
//	   @throws Exception
//	   2016年2月19日
//	   dengqiuru
//	 */
//	@Test
//	public void roughlyStateOfUPlayer() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("page", "0");
//		hashMap.put("playerId", "79dc7f7c-b261-4c94-a442-89bce2642887");
//		resultMap = uPlayerService.roughlyStateOfUPlayer(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 球员详情 详情
//	   @param map
//	   @return
//	   @throws Exception
//	   2016年2月19日
//	   dengqiuru
//	 */
//	@Test
//	public void getUplayerByTeamIds() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("page", "0");
//		hashMap.put("teamIds", "5d538205-57f3-4e87-8531-2dc1f1ad5548,ef8dc981-f0ba-4d27-9267-1b9bb64718af");
//		resultMap = uPlayerService.getUplayerByTeamIds(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 关注/取消关注球员
//	   @throws Exception
//	   2016年1月26日
//	   dengqiuru
//	 */
//	@Test
//	public void followUPlayer() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("playerId", "c106090d-303c-4e5f-a21c-0d2bbab1f233");
//		hashMap.put("type", "0");
//		resultMap = uPlayerService.followUPlayer(hashMap);
//		printInfo("------------------------------",uUser);
//	}	
//	/**
//	 * 
//	 * 
//	   TODO - 关注/取消关注球员
//	   @throws Exception
//	   2016年1月26日
//	   dengqiuru
//	 */
//	@Test
//	public void myFollowUPlayer() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("page", "0");
//		resultMap = uPlayerService.myFollowUPlayer(hashMap);
//		printInfo("------------------------------",uUser);
//	}
	/**
	 * 
	 * 
	   TODO - 关注/取消关注球员
	   @throws Exception
	   2016年1月26日
	   dengqiuru
//	 */
//	@Test
//	public void transferPlayerList202() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("page", "0");
//		hashMap.put("teamId", "ff66c814-d864-4add-8ae0-91812a668682");
//		hashMap.put("loginUserId", "460b1ebb-a5d5-43f6-8e96-b04a136a8ae1");
//		resultMap = uPlayerService.transferPlayerList202(hashMap);
//		printInfo("------------------------------",uUser);
//	}

	@Test
	public void recruitPlayer202() throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
		hashMap1.put("phone", "15618038746");
		hashMap1.put("name", "1");
		List<Map<String, Object>> addressBooks = new ArrayList<>();
		addressBooks.add(hashMap1);
		hashMap.put("code", "0");
		hashMap.put("teamId", "ff66c814-d864-4add-8ae0-91812a668682");
		hashMap.put("loginUserId", "460b1ebb-a5d5-43f6-8e96-b04a136a8ae1");
		resultMap = uPlayerService.recruitPlayerList202(hashMap, addressBooks);
		printInfo("------------------------------",uUser);
	}
}
