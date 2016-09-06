package upbox.dqr.test;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;

/**
 * 测试前端球队接口 -- uTeamService
 * @author mercideng
 *
 */
@ContextConfiguration("applicationContext.xml")
public class UteamServiceTest extends AbstractJUnit4SpringContextTests {
	@Resource
	private UTeamService uTeamService;
	private HashMap<String, Object> resultMap;
	private UTeam uTeam;
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
	   TODO - 查询球队明细
	   	map:
	   		token
	   		userStatus
	   		teamId
	   @throws Exception
	   2016年1月28日
	   dengqiuru
	   http://localhost:8080/Chelsea-MIC/uteam_getHeadUteaminfo.do?token=b76b7910-b306-43ef-bded-e6e0dee78ea3&userStatus=1&teamId=6f0272bc-955b-464d-b0a9-53e382ba3ad6
	 */
//	@Test
//	public void getUteaminfoMethod() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("token", "b76b7910-b306-43ef-bded-e6e0dee78ea3");
//		hashMap.put("userStatus", "1");
//		hashMap.put("teamId", "6f0272bc-955b-464d-b0a9-53e382ba3ad6");
//		resultMap = uTeamService.findPlayerInfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 建立球队
		userId			用户Id
		token			用户登陆识别码
		userStatus		用户登陆状态
		imgSizeType		图片尺寸类型
		imgurl			图片显示地址
		imgWeight		图片权重
		saveurl			图片保存地址
		teamName		球队全名
		teamType		球队类型
		teamSimpleName	简称	
		remark			简介
	   @throws Exception
	   2016年1月28日
	   dengqiuru
	   http://localhost:8080/Chelsea-MIC/uteam_insertNewTeam.do?userStatus=1&token=c00e91de-18a3-4dd9-9fe0-2548c8f6f03d&userId=07eae762-819f-4120-a184-56043e56ff76&imgSizeType=1&imgurl=123&imgWeight=1&saveurl=1234
	 */
//	@Test
//	public void insertNewTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("token", "b76b7910-b306-43ef-bded-e6e0dee78ea3");
//		hashMap.put("userStatus", "1");
//		hashMap.put("userId", "6f0272bc-955b-464d-b0a9-53e382ba3ad6");
//		hashMap.put("imgSizeType", "1");
//		hashMap.put("imgurl", "123");
//		hashMap.put("imgWeight", "2");
//		hashMap.put("saveurl", "1234");
//		hashMap.put("teamName", "XXXX");
//		hashMap.put("teamType", "1");
//		hashMap.put("teamSimpleName", "XX");
//		hashMap.put("remark", "");
//		resultMap = uTeamService.insertNewTeam(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 战队列表
	   	userId			用户Id
		token			用户登陆识别码
		userStatus		用户登陆状态	
		page			分页
		searchStr		战队列表搜索内容
	   @throws Exception
	   2016年1月28日
	   dengqiuru
	   http://localhost:8080/Chelsea-MIC/uteam_getUteamList.do?userStatus=1&token=c00e91de-18a3-4dd9-9fe0-2548c8f6f03d&userId=07eae762-819f-4120-a184-56043e56ff76&page=0&searchStr=B
	 */
//	@Test
//	public void getUteamList() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		HashMap<String,List<Object>> mapList = new HashMap<>();
//		hashMap.put("token", "4be4f741-ed43-4adb-91cf-81be60ba6284");
//		hashMap.put("userStatus", "1");
//		hashMap.put("page", "1");
//		resultMap = uTeamService.getUteamList(hashMap,mapList);
//		printInfo("------------------------------",resultMap);
//	}
	/**
	 * 
	 * 
	   TODO - 约战更多对手
	   @throws Exception
	   2016年4月30日
	   dengqiuru
	 */
	@Test
	public void addMoreCompetitor() throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("token", "f44f0aba-0a89-402f-88d1-a861a7e7c857");
		hashMap.put("userStatus", "1");
		hashMap.put("page", "1");
		resultMap = uTeamService.addMoreCompetitor(hashMap);
		printInfo("------------------------------",resultMap);
	}
	
	/**
	 * 
	 * 
	   TODO - 我得球队信息
	   	userId			用户Id
	   @throws Exception
	   2016年2月17日
	   dengqiuru
	   http://localhost:8080/Chelsea-MIC/uteam_myTeamInfo.do?userId=07eae762-819f-4120-a184-56043e56ff76&token=79e5a9be-6a2d-41c4-81dc-29d3cc668c02&userStatus=1
	 */
//	@Test
//	public void myTeamInfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		resultMap = uTeamService.myTeamInfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	

	/**
	 * 
	 * 
	   TODO - 队长踢人
	   @param map
	   	userId 踢人的用户Id
	   	playerId  被踢人的球员Id
	   	teamId	踢人的队伍id
	   @return
	   @throws Exception
	   2016年2月18日
	   dengqiuru
	 */
//	@Test
//	public void excludeByTeamLeader() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", "fa94637d-c4e8-405c-adb7-934eb97cd0eb");
//		hashMap.put("playerId", "f6693488-ad8e-48af-98a5-a86eb3f121fb");
//		hashMap.put("teamId", "3a77be66-77e2-4180-a830-30b5023203d1");
//		resultMap = uTeamService.excludeByTeamLeader(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	
//	/**
//	 * 
//	 * 
//	   TODO - 退出队伍
//	   @throws Exception
//	   2016年2月18日
//	   dengqiuru
//	 */
//	@Test
//	public void exitTeamByUserId() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", "fa94637d-c4e8-405c-adb7-934eb97cd0eb");
//		hashMap.put("teamId", "3a77be66-77e2-4180-a830-30b5023203d1");
//		resultMap = uTeamService.exitTeamByUserId(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
//	@Test
//	public void findUteaminfoListHead() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", "fa94637d-c4e8-405c-adb7-934eb97cd0eb");
//		resultMap = uTeamService.findUteaminfoListHead(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
//	@Test
//	public void findUteaminfoListHead() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		resultMap = uTeamService.findUteaminfoListHead(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 加入球队
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void joinInTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "5d538205-57f3-4e87-8531-2dc1f1ad5548");
//		hashMap.put("userId", "07eae762-819f-4120-a184-56043e56ff76");
//		resultMap = uTeamService.joinInTeam(hashMap);
//		printInfo("------------------------------",resultMap);
//		
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 战队详情--概况
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void roughlyStateOfUteam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "5d538205-57f3-4e87-8531-2dc1f1ad5548");
//		hashMap.put("page", "0");
//		resultMap = uTeamService.roughlyStateOfUteam(hashMap);
//		printInfo("------------------------------",resultMap);
//		
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 战队详情--概况
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void findUteaminfoHead() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "5d538205-57f3-4e87-8531-2dc1f1ad5548");
//		hashMap.put("page", "0");
//		resultMap = uTeamService.findUteaminfoHead(hashMap);
//		printInfo("------------------------------",resultMap);
//		
//	}
	
//	/**
//	 * 
//	 * 
//	   TODO - 战队详情--概况
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void getUteaminfoByUserId() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("loginUserId", "2230ed71-ae11-47b2-b897-7ff0b0f87b72");
//		resultMap = uTeamService.getUteaminfoByUserId(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	
//	/**
//	 * 
//	 * 
//	   TODO - 球队关注
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void followUTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "2230ed71-ae11-47b2-b897-7ff0b0f87b72");
//		hashMap.put("type", "1");
//		resultMap = uTeamService.followUTeam(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 我关注的球队
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void myFollowUTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("page", "0");
//		resultMap = uTeamService.myFollowUTeam(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
//	/**
//	 * 
//	 * 
//	   TODO - 我关注的球队
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void getBrCourtlist() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "5d538205-57f3-4e87-8531-2dc1f1ad5548");
//		resultMap = uTeamService.getBrCourtlist(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	
	/**
	 * 
	 * 
	   TODO - 我关注的球队
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
//	@Test
//	public void updateUTeaminfo() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "37245c90-94ec-4439-9dd9-dfce929f34c7");
//		hashMap.put("teamName", "长江蓝狮");
//		resultMap = uTeamService.updateUTeaminfo(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
//	/**
//	 * 
//	 * 
//	   TODO - 我关注的球队
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void invitePlayer() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		hashMap.put("teamId", "909cb087-10f2-4da4-b7fe-49fbea06ad8c");
//		hashMap.put("loginUserId", "7fa516c6-7b58-45db-8d41-73f1dab98848");
//		hashMap.put("playerId", "c3716858-e7a3-49b7-8a61-29f3586eb2f0");
//		resultMap = uTeamService.invitePlayer(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
	
	
//	/**
//	 * 
//	 * 
//	   TODO - 推荐的对手
//	   @throws Exception
//	   2016年3月2日
//	   dengqiuru
//	 */
//	@Test
//	public void getrecommendTeam() throws Exception{
//		HashMap<String, String> hashMap = new HashMap<>();
//		resultMap = uTeamService.getrecommendTeam(hashMap);
//		printInfo("------------------------------",resultMap);
//	}
}
