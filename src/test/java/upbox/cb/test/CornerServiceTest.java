package upbox.cb.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


import upbox.model.CornerBean;
import upbox.service.CornerService;


/**
 * 角标test -- cornerService
 * @author charlescai
 *
 */
@ContextConfiguration("file:src/main/resources/applicationContext.xml")
public class CornerServiceTest extends AbstractJUnit4SpringContextTests {
	
	@Resource
	private CornerService cornerService;
	
	private HashMap<String, Object> resultMap;
	
	
	
	@Ignore
	/**
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	 */
	public void printInfo(String temp,Object obj) throws JSONException{
		System.out.println(temp + JSONUtil.serialize(obj));
	}
	
	/**
	 * test
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception{
		resultMap = new HashMap<String, Object>();
//		List<CornerBean> resultList = new ArrayList<CornerBean>();
		List<List<CornerBean>> resultLists = new ArrayList<List<CornerBean>>();
		List<Object> teamIdList = new ArrayList<Object>();
		//推荐挑战 + 挑战中 
//		teamIdList.add("4756a9ef-150e-4fe7-80f0-16542cc2c74e");
		//我的球队+ 挑战中 + 约战中
//		teamIdList.add("b050eb5c-4da7-4431-a1c3-314ebbf0b1c9");
//		teamIdList.add("0462db79-84b1-47c6-88ed-837ac1a4c345");
		//上海崇明哈特虎足球队
//		teamIdList.add("1c03c913-33dc-4490-b661-1747af47e467");
		teamIdList.add("d0e44c44-f924-4544-8d7e-1dd230b3d4ed");
		
		//我签约的队伍+ 挑战中  + 约战中
//		teamIdList.add("4de15558-5912-4eb4-a2dd-de08a8a599bd");
		//推荐约战 + 约战中
//		teamIdList.add("1bc6e91f-04fe-454b-a40e-da2a5661c64a");
//		//挑战中
//		teamIdList.add("5f674c26-7521-428f-8a86-4ed358a8893b");
//		//约战中
//		teamIdList.add("c005");
		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("loginUserId","b19d47c0-dd83-4184-b6f0-6dfcb136f42f");
		hashMap.put("loginUserId","57aedeb6-16bf-45e6-9f6a-0d28e4adbd48");
//		hashMap.put("type","status");
//		hashMap.put("type","relation");
//		hashMap.put("type","recommend");
		//约战中队伍
//		hashMap.put("teamId","4756a9ef-150e-4fe7-80f0-16542cc2c74e");
		//我创建的队伍
//		hashMap.put("teamId","2b9f9dde-e531-4044-a2e1-f4a4d4b610d7");
		//我签约的队伍
//		hashMap.put("teamId","909cb087-10f2-4da4-b7fe-49fbea06ad8c");
		
//		hashMap.put("teamId","1bc6e91f-04fe-454b-a40e-da2a5661c64a");
		
		//单独角标
//		resultList = cornerService.getAloneTeamCornerByType(hashMap);
//		resultLists = cornerService.getAloneTeamCornerListByType(hashMap,teamIdList);
		resultLists = cornerService.getAllTeamCornerList(hashMap,teamIdList);
		//所有角标
//		resultLists = cornerService.getPlayerCorner(hashMap, teamIdList);
		
		if(0<resultLists.size()){
			//System.out.println("------------------->list is not null and size="+resultLists.size());
			resultMap.put("corner", resultLists);
		}else{
			//System.out.println("----------------------->list is null and size="+resultLists.size());
		}
		
		printInfo("-------------------------------->",resultMap);
	}
	

}
