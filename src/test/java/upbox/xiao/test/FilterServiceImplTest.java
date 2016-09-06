package upbox.xiao.test;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.service.FilterService;
@ContextConfiguration("applicationContext.xml")
public class FilterServiceImplTest   extends AbstractJUnit4SpringContextTests{
	@Resource
	private FilterService filterService;
	private HashMap<String, Object> hashMap ;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 订单生成日期转换
	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	 */
	public void printInfo(String temp,Object obj){
		System.out.println(temp + JSON.toJSONString(obj));
	}
	
	@Test
	public void findFilterTeam()  throws Exception{
//		HashMap<String, String> map=new HashMap<>();
//		map.put("area", "上海");
//		//胜率
//		List<String> chancesList=new ArrayList<>();
//		chancesList.add("0-20");
//		chancesList.add("21-30");
//		
//		//排名
//		List<String> rankList=new ArrayList<>(); 
//		rankList.add("1-50");
//		rankList.add("51-100");
//		rankList.add("-1");//未进入排名的
//		//平均年龄
//		List<String> avgAgeList=new ArrayList<>();
//		avgAgeList.add("-10");
//		avgAgeList.add("10-18");
//		avgAgeList.add("19-25");
//		avgAgeList.add("40-");
//		
//		//平均身高
//		List<String> avgHeightList=new ArrayList<>();
//		avgHeightList.add("120-140");
//		avgHeightList.add("141-160");
//		//平均体重
//		List<String> avgWeightList=new ArrayList<>();
//		avgWeightList.add("31-40");
//		avgWeightList.add("41-60");
//		
//		HashMap<String, Object> hashMap=filterService.findFilterTeam(map, chancesList, rankList, avgAgeList, avgHeightList, avgWeightList);
//		//System.out.println(hashMap);
		HashMap<String, String> map=new HashMap<>();
		map.put("area", "862");
//		map.put("chances", "0-20,21-30,31-40,41-50,50-");
//		map.put("rank", "1-50,51-100,0");//0未进入
//		map.put("avgAge", "-10,10-18,19-25,40-");//-10以下  40- 以上
//		map.put("avgHeight", "120-140,141-160");
//		map.put("avgWeight", "31-40,41-60");
		map.put("page", "1");
		map.put("loginUserId", "");
//		map.put("teamCount", "0-30,-40,41-");
//		map.put("champion", "-1");
//		map.put("orderByTeam", "2");
//		map.put("orderByRank", "fduel");
		HashMap<String, Object> hashMap=filterService.findFilterTeam(map);
			
	}
	@Test
	public void findFilterPlayer()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
//		map.put("teamId", "1sdf");
		//人数
		map.put("count","1");
//		map.put("expertPosition", "1,2,3");
//		map.put("area", "4,5");
		map.put("age", "-10,10-18,19-25,40-");//-10以下  40- 以上
		map.put("age", null);//-10以下  40- 以上
//		map.put("height", "120-140,141-160");
//		map.put("weight", "31-40,41-60");
//		map.put("teamBelonging", "1");
//		map.put("teamId", "01a29dd7-6ad1-4b6b-bd06-ea62a6856551");
		map.put("type", "1");
		
		map.put("page", "1");
		map.put("loginUserId", "");
		map.put("location","121.487760518,31.3136954987");
		
		
		//平均年龄
//		List<String> ageList=new ArrayList<>();
//		ageList.add("-10");
//		ageList.add("10-18");
//		ageList.add("19-25");
//		ageList.add("40-");
//		
//		//平均身高
//		List<String> heightList=new ArrayList<>();
//		heightList.add("120-140");
//		heightList.add("141-160");
//		//平均体重
//		List<String> weightList=new ArrayList<>();
//		weightList.add("31-40");
//		weightList.add("41-60");
		
//		map.put("teamBelonging", "1");
		
		
		HashMap<String, Object> hashMap=filterService.findFilterPlayer(map);
		System.out.println(hashMap);
	}
	
	@Test
	public void findPlayer()  throws Exception{
		HashMap<String, String> map=new HashMap<>();
//		map.put("teamId", "1sdf");
//		map.put("expertPosition", "1,2,3");
//		map.put("age", "-10,10-18,19-25,40-");//-10以下  40- 以上
//		map.put("height", "120-140,141-160");
//		map.put("weight", "31-40,41-60");
//		map.put("teamBelonging", "1");
		map.put("page", "1");
		
		HashMap<String, Object> hashMap=filterService.findFilterPlayer(map);
			
	}

}
