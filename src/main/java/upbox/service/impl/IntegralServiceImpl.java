package upbox.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.IntegralBean;
import upbox.model.PlayerInfoBean;
import upbox.model.UChallengeBs;
import upbox.model.UChallengePlayerInfo;
import upbox.model.UDuelBs;
import upbox.model.UDuelPlayerinfo;
import upbox.model.UIntergralInfo;
import upbox.model.UMatchBs;
import upbox.model.UMatchPlayerInfo;
import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.service.IntegralService;

/**
 * 积分接口实现
 * @author yc
 *
 * 13611929818
 * @param <E>
 */
@Service("integralService")
public class IntegralServiceImpl implements IntegralService{

	@Resource
	private OperDAOImpl baseDAO;
	
	private HashMap<String, Object> hashMap = new HashMap<String, Object>();

	@Override
	public HashMap<String, Object> getIntegralListWithBs(HashMap<String, String> map) throws Exception {
		List<IntegralBean> interList=new ArrayList<IntegralBean>();
		List<PlayerInfoBean> fplayerInfoList=new ArrayList<PlayerInfoBean>();
		List<PlayerInfoBean> xplayerInfoList=new ArrayList<PlayerInfoBean>();
		String bsType=map.get("bsType");
		String bsId=map.get("bsId");
		hashMap.put("bsId", bsId);
		if (bsType != null && !"".equals(bsType)&&bsId != null && !"".equals(bsId)) {
			appendIntegral(bsType, interList, bsId,fplayerInfoList,xplayerInfoList);
			hashMap.clear();
			Collections.sort(interList, this.weight);//根据 权重排序
			hashMap.put("integralList", interList);
			hashMap.put("fplayerInfoList", fplayerInfoList);
			hashMap.put("xplayerInfoList", xplayerInfoList);
			return hashMap;
		} else {
			return WebPublicMehod.returnRet("error", "请求参数不完整！");
		}
	}

	/**
	 * 对list的时间进行排序 重写list date排序方法
	 */
	public  final Comparator<IntegralBean> weight = new Comparator<IntegralBean>(){          
        public int compare(IntegralBean o1, IntegralBean o2) {                                          
             Integer weight1 = o1.getWeight();
             Integer weight2 = o2.getWeight();
             return  weight1.compareTo(weight2);                                        
        }  
    };
    
    
	/**
	 * 处理各表的激数
	 * @param bsType 比赛类型
	 * @param interList 激数集合
	 * @param bsId 小场次id
	 * @throws Exception
	 */
	private void appendIntegral(String bsType,List<IntegralBean> interList,String bsId,List<PlayerInfoBean> fplayerInfoList,List<PlayerInfoBean> xplayerInfoList)throws Exception{
		
		List<UIntergralInfo> finfoList=null;
		List<UIntergralInfo> xinfoList=null;
		List<UDuelPlayerinfo> fduList=null;
		List<UDuelPlayerinfo> xduList=null;
		List<UChallengePlayerInfo> fchList=null;
		List<UChallengePlayerInfo> xchList=null;
		List<UMatchPlayerInfo> fmaList=null;
		List<UMatchPlayerInfo> xmaList=null;
		
		if(bsType.equals("duel")){
			UDuelBs dbs=baseDAO.get(UDuelBs.class,bsId);
			if(dbs!=null&&dbs.getUTeam()!=null&&dbs.getXUTeam()!=null){
				hashMap.put("cfteamId", dbs.getUTeam().getTeamId());
				hashMap.put("cxteamId", dbs.getXUTeam().getTeamId());
				finfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cfteamId and UDuelBs.bsId=:bsId",hashMap);
				xinfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cxteamId and UDuelBs.bsId=:bsId",hashMap);
				fduList=baseDAO.find("from UDuelPlayerinfo where UTeam.teamId=:cfteamId and UDuelBs.bsId=:bsId order by cast(time as integer)",hashMap);
				xduList=baseDAO.find("from UDuelPlayerinfo where UTeam.teamId=:cxteamId and UDuelBs.bsId=:bsId order by cast(time as integer)",hashMap);
				interList.add(assembleIntegralBean("合计激数", dbs.getFfj(), dbs.getKfj(), "", "",null));
				assembleIntegralBeanList(finfoList, xinfoList, interList);
			}
		}else if(bsType.equals("challenge")){
			UChallengeBs cbs=baseDAO.get(UChallengeBs.class,bsId);
			if(cbs!=null&&cbs.getBsFteam()!=null&&cbs.getBsXteam()!=null){
				hashMap.put("cfteamId", cbs.getBsFteam().getTeamId());
				hashMap.put("cxteamId", cbs.getBsXteam().getTeamId());
				finfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cfteamId and UChallengeBs.bsId=:bsId",hashMap);
				xinfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cxteamId and UChallengeBs.bsId=:bsId",hashMap);
				fchList=baseDAO.find("from UChallengePlayerInfo where uteam.teamId=:cfteamId and UChallengeBs.bsId=:bsId order by cast(time as integer)",hashMap);
				xchList=baseDAO.find("from UChallengePlayerInfo where uteam.teamId=:cxteamId and UChallengeBs.bsId=:bsId order by cast(time as integer)",hashMap);
				interList.add(assembleIntegralBean("合计激数", cbs.getFqFj(), cbs.getXyFj(), "", "",null));
				assembleIntegralBeanList(finfoList, xinfoList, interList);
			}
		}else if(bsType.equals("match")){
			UMatchBs cbs=baseDAO.get(UMatchBs.class,bsId);
			if(cbs!=null&&cbs.getBsFteam()!=null&&cbs.getBsXteam()!=null){
				hashMap.put("cfteamId", cbs.getBsFteam().getTeamId());
				hashMap.put("cxteamId", cbs.getBsXteam().getTeamId());
				finfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cfteamId and UMatchBs.bsId=:bsId",hashMap);
				xinfoList=baseDAO.find("from UIntergralInfo where uteam.teamId=:cxteamId and UMatchBs.bsId=:bsId",hashMap);
				fmaList=baseDAO.find("from UMatchPlayerInfo where uteam.teamId=:cfteamId and UMatchBs.bsId=:bsId order by cast(time as integer)",hashMap);
				xmaList=baseDAO.find("from UMatchPlayerInfo where uteam.teamId=:cxteamId and UMatchBs.bsId=:bsId order by cast(time as integer)",hashMap);
				interList.add(assembleIntegralBean("合计激数", cbs.getFqFj(), cbs.getXyFj(), "", "",null));
				assembleIntegralBeanList(finfoList, xinfoList, interList);
			}
		}
		
		
		appendPlayer(fplayerInfoList, fduList, fchList, fmaList);//拼发起应对象
		appendPlayer(xplayerInfoList, xduList, xchList, xmaList);//拼接响应对象
	}
	
	/**
	 * 处理各表的激数
	 * @param plBeanList add的球员事件bean
	 * @param duList 处理约战
	 * @param chList 处理挑战
	 * @param maList 处理赛事
	 * @throws Exception
	 */
	private void appendPlayer(List<PlayerInfoBean> plBeanList,List<UDuelPlayerinfo> duList,List<UChallengePlayerInfo> chList,List<UMatchPlayerInfo> maList)throws Exception{
		if(duList!=null&&duList.size()>0){
			for(UDuelPlayerinfo a:duList){
				PlayerInfoBean plbean=new PlayerInfoBean();
				plbean.setShirtsNum(getShirtNum(a.getUTeam(), a.getUUser()));
				if(a.getUUser()!=null){
					plbean.setRealName(a.getUUser().getRealname());
					plbean.setNickName(a.getUUser().getNickname());
				}
				plbean.setTime(a.getTime());
				plbean.setEventType(a.getDuelBsType());
				plBeanList.add(plbean);
			}
		}else if(chList!=null&&chList.size()>0){
			for(UChallengePlayerInfo b:chList){
				PlayerInfoBean plbean=new PlayerInfoBean();
				plbean.setShirtsNum(getShirtNum(b.getUteam(), b.getUser()));
				if(b.getUser()!=null){
					plbean.setRealName(b.getUser().getRealname());
					plbean.setNickName(b.getUser().getNickname());
				}
				plbean.setTime(b.getTime());
				plbean.setEventType(b.getChallengeType());
				plBeanList.add(plbean);
			}
		}else if(maList!=null&&maList.size()>0){
			for(UMatchPlayerInfo c:maList){
				PlayerInfoBean plbean=new PlayerInfoBean();
				plbean.setShirtsNum(getShirtNum(c.getUteam(), c.getUser()));
				if(c.getUser()!=null){
					plbean.setRealName(c.getUser().getRealname());
					plbean.setNickName(c.getUser().getNickname());
				}
				plbean.setTime(c.getTime());
				plbean.setEventType(c.getMatchPlayerType());
				plBeanList.add(plbean);
			}
		}
	}
	
	/**
	 * 获取背号
	 * @param teamId 球队id
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	private String getShirtNum(UTeam team,UUser user) throws Exception{
		HashMap<String, Object> hash=new HashMap<String, Object>();
		if(team!=null&&user!=null){
			hash.put("userId", user.getUserId());
			hash.put("teamId", team.getTeamId());
			UPlayer pl=baseDAO.get("from UPlayer where UUser.userId=:userId and UTeam.teamId=:teamId",hash);
			if(pl!=null&&pl.getNumber()!=null){
				return pl.getNumber().toString();
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	/**
	 * 自动组装前端需要的对象格式
	 * @param body_name 中间展示文字
	 * @param f_Integral 发起球队获得激数
	 * @param x_Integral 响应球队获得激数
	 * @param f_body_num 发起球队获得数字事件
	 * @param x_body_num 响应球队获得数字事件
	 * @return
	 * 		inter 组装完成的对象
	 */
	private IntegralBean assembleIntegralBean(String body_name,String f_Integral,String x_Integral,String f_body_num,String x_body_num,String weight){
		IntegralBean inter=new IntegralBean();
		if((f_Integral!=null&&f_Integral.equals("null"))||f_Integral==null){
			f_Integral="";
		}
		if((x_Integral!=null&&x_Integral.equals("null"))||x_Integral==null){
			x_Integral="";
		}
		if((f_body_num!=null&&f_body_num.equals("null"))||f_body_num==null){
			f_body_num="";
		}
		if((x_body_num!=null&&x_body_num.equals("null"))||x_body_num==null){
			x_body_num="";
		}
		inter.setBody_name(body_name);
		inter.setF_body_num(f_body_num);
		inter.setF_Integral(f_Integral);
		inter.setX_body_num(x_body_num);
		inter.setX_Integral(x_Integral);
		if(weight!=null&&!"".equals(weight)){
			inter.setWeight(Integer.parseInt(weight));
		}else{
			inter.setWeight(0);
		}
		
		return inter;
	}
	
	/**
	 * 拼接前端app需要的list合集
	 * @param finfoList 发起球队的积分事件
	 * @param xinfoList 响应球队的积分事件
	 * @param interList 拼接的bean
	 */
	private void assembleIntegralBeanList(List<UIntergralInfo> finfoList,List<UIntergralInfo> xinfoList,List<IntegralBean> interList){
		for(UIntergralInfo f:finfoList){
			for(UIntergralInfo x:xinfoList){
				if(f.getURuleParameter().getRuleId().equals(x.getURuleParameter().getRuleId())){
					hashMap.put("ruleId", f.getURuleParameter().getRuleId());
					interList.add(assembleIntegralBean(f.getURuleParameter().getAppName(), f.getInfo(), x.getInfo(), f.getAppShow(), x.getAppShow(),x.getURuleParameter().getRuleWeight()));
				}
			}
		}
	}
	
	
}
