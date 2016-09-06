package upbox.wmq.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.alibaba.fastjson.JSON;

import upbox.model.UDuelChallengeImg;
import upbox.service.UDuelService;

/**
 * 约战测试
 * @author wmq
 *
 * 15618777630
 */
@ContextConfiguration("applicationContext.xml")
public class DuelServiceTest extends AbstractJUnit4SpringContextTests{
	@Resource
	private UDuelService uduelServiceImpl;
	private List<String> duelTeamList = new ArrayList<String>();
	private List<UDuelChallengeImg> duelImgList = new ArrayList<UDuelChallengeImg>();
	UDuelChallengeImg uci = new UDuelChallengeImg();
	
	@Ignore
	public HashMap<String,String> returnHashMap() throws SQLException {
		uci.setDuelChallUsingType("1");
		uci.setImgSaveType("1");
		uci.setImgurl("---->");
		HashMap<String,String> hash = new HashMap<String, String>();
		hash.put("userId", "111111111111");
		hash.put("loginUserId", "111111111111");
		hash.put("teamId", "111111111111");
		hash.put("token", "111111111111");
		hash.put("userStatus", "1");
		hash.put("stdate", "2015-01-03");
		hash.put("sttime", "19:22:11");
		hash.put("teamBehaviorType", "3");
		hash.put("subCourtId", "4b853743-651e-4bb3-aa22-1c713e99d842");
		hash.put("page", "1");
		hash.put("duelPayType", "1");  
		hash.put("payProportion", "1");
		hash.put("remark", "12342342");
		hash.put("orderId", "111111111111");
		hash.put("duelId", "ece4ab2d-6a3f-40af-a44d-c3dea3cb46f1");
		duelTeamList.add("a1215979-f302-4d09-95df-350264fabeb5");
		duelImgList.add(uci);
		return hash;
	}
	
	@Test
	public void testtest() throws Exception{
		uduelServiceImpl.updateDuelByTimeOut();
	}
	
	@Ignore
	/**
	 * 
	 * 
	   TODO - 打印
	   @param temp
	   @param obj
	   2015年12月1日
	   mazkc
	 */
	public void printInfo(String temp, Object obj) {
		//System.out.println(temp + "---------------:" + JSON.toJSONString(obj));
	}

	/**
	 * 
	 * 
	   TODO - 测试是否处于约战
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void checkTeamDuel(){
		try{
			printInfo("checkTeamDuel()",uduelServiceImpl.checkTeamDuel(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 查询球队首次约战相关记录
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void duelFristDate(){
		try{
			printInfo("duelFristDate()",uduelServiceImpl.duelFrist(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 查看我的约战列表
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void findMyDuelList(){
		try{
			printInfo("findMyDuelList()",uduelServiceImpl.findMyDuelList(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 查看战队战绩列表
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void findTeamDuelList(){
		try{
			printInfo("findTeamDuelList()",uduelServiceImpl.findTeamDuelList(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * 
	   TODO -  查看球员战绩列表
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void findPlayerDuelList(){
		try{
			printInfo("findPlayerDuelList()",uduelServiceImpl.findPlayerDuelList(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO -  查看约战列表
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void findAllDuelList(){
		try{
			printInfo("findAllDuelList()",uduelServiceImpl.findAllDuelList(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO -  发起约战-走一个
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void addDuelOrder(){
		try{
			printInfo("addDuelOrder()",uduelServiceImpl.addDuelOrder(returnHashMap(),duelTeamList,duelImgList));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	   TODO -  关注约战
	   @throws Exception
	   2016年2月29日
	   mazkc
	 */
	@Ignore
	public void followDuel(){
		try{
			printInfo("followDuel()",uduelServiceImpl.followDuel(returnHashMap()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
