package upbox.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.CornerBean;
import upbox.model.PageLimit;
import upbox.model.UTeam;
import upbox.model.UTeamDek;
import upbox.model.UTeamDuel;
import upbox.model.UTeamMatch;
import upbox.pub.Public_Cache;
import upbox.service.CornerService;
import upbox.service.RankingListService;
import upbox.service.UChallengeService;
import upbox.service.UDuelService;
import upbox.service.UTeamService;
import upbox.service.UUserService;

/**
 * 
 * @TODO 天梯service实现类
 * @author charlesbin  
 * @email charlescai@ppsports.com 
 * @phone 13120501633 
 * @date 创建时间：2016年6月1日 下午4:11:49 
 * @version 1.0
 */
@Service("rankingListService")
public class RankingListServiceImpl implements RankingListService {
	@Resource
	private OperDAOImpl baseDAO;

	@Resource
	private UUserService uuserService;

	@Resource
	private UTeamService uteamService;
	
	@Resource
	private UDuelService uduelServiceImpl;
	
	@Resource
	private UChallengeService challengeService;
	
	@Resource
	private CornerService cornerService; 

	/**
	 * 
	 * @TODO 排行榜接口
	 * @Title: getRankingList 
	 * @param map
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:09:34
	 */
	@Override
	public HashMap<String, Object> getRankingList(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		List<Object> teamIdList = new ArrayList<Object>();
		Integer pageNum =  map.get("page") == null||Integer.parseInt(map.get("page"))<=0 ? 1 : Integer.parseInt(map.get("page"));
		if(pageNum==11){
			//更新排名
			this.updateRankByList(map);
		}
		
		// 计算限制页数
		Double a = 1.0*Integer.parseInt(Public_Cache.RANKING_LIMIT)/Public_Cache.PAGE_LIMIT;
		Double b = Math.ceil(a);
		Integer pageLimit = b.intValue();
		PageLimit page = new PageLimit(pageNum,0);
		
		StringBuffer sql = new StringBuffer();
		sql = RankingListDataSql(sql);
		sql.append(" where ut.team_status='3' and ut.team_use_status='2' and ut.integral is not null ");
		if(map.get("area")!=null&&!"".equals(map.get("area"))){
			sql.append("and ut.area = :area  ");
		}
		sql.append(" order by ut.integral DESC,allMatchCount DESC,gd DESC,ut.createdate ASC,ut.createtime ASC ");

		//限制99名
		List<HashMap<String,Object>> teamResult = new ArrayList<HashMap<String,Object>>();
		if(pageNum < pageLimit){
			sql.append(" limit "+page.getLimit());
			sql.append(" offset "+page.getOffset());
			teamResult = baseDAO.findSQLMap(map, sql.toString());
		}else if(pageNum.equals(pageLimit) ){
			sql.append(" limit "+(Integer.parseInt(Public_Cache.RANKING_LIMIT)-page.getOffset()));
			sql.append(" offset "+page.getOffset());
			teamResult = baseDAO.findSQLMap(map, sql.toString());
		}

		int j = 0;
		if(pageNum>1){
			j = (pageNum-1)*10;
		}
		
		// 生成排名序号
		for (int i = 0; i < teamResult.size(); i++) {
			teamIdList.add(teamResult.get(i).get("teamId"));
			teamResult.get(i).put("rankNo", ++j);
			
			Double sumChances = 0.0;
			Integer varCount = Integer.parseInt(teamResult.get(i).get("verMatchCount").toString());
			Integer allCount = Integer.parseInt(teamResult.get(i).get("allMatchCount").toString());
			
			DecimalFormat df = new DecimalFormat("0.0");// 格式化小数，不足的补0
			if(varCount!=0&&allCount!=0){
				sumChances = 1.0*varCount/allCount;
				sumChances = sumChances*100;
				teamResult.get(i).put("chances", df.format(sumChances));
			}else{
				teamResult.get(i).put("chances", df.format(sumChances));
			}
			//更新胜率－LBS百度上传数据-----------------------------
			uteamService.editChancesBaidulbs((String)teamResult.get(i).get("teamId"), df.format(sumChances));
			
			//球队所属性质
			if(null!=teamResult.get(i).get("teamClass")){
				teamResult.get(i).put("teamClassName",Public_Cache.HASH_PARAMS("team_class").get(teamResult.get(i).get("teamClass")));
			}else{
				teamResult.get(i).put("teamClassName",null);
			}
			
		}
		
		resultMap.put("rankingList", teamResult);
		
		List<List<CornerBean>> cornerList = new ArrayList<List<CornerBean>>();
		if(0 < teamIdList.size()){ //角标
			cornerList = cornerService.getAllTeamCornerList(map, teamIdList);
		}		
		resultMap.put("cornerList", cornerList);
		
		return resultMap;
	}

	/**
	 * 
	 * @TODO 查询天梯数据通用sql
	 * @Title: RankingListDataSql 
	 * @param sql
	 * @return
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:14:48
	 */
	public StringBuffer RankingListDataSql(StringBuffer sql){
		sql.append(" select ut.team_id as teamId,ut.short_name as shortName,ut.team_class as teamClass,ut.name,ut.integral,ut.rank ");
		sql.append(" ,(select count(1) from u_champion uch where uch.team_id = ut.team_id and uch.is_champion = '1')as is_champion ");
		sql.append(" ,(select uti.imgurl from u_team_img uti where ut.team_id = uti.team_id and uti.timg_using_type = '1' and uti.img_size_type = '1') as imgUrl ");
		sql.append(" ,((case when utdk.gd is null then 0 else utdk.gd end)+(case when utdl.gd is null then 0 else utdl.gd end)+(case when utma.gd is null then 0 else utma.gd end)) as gd ");
		sql.append(" ,((case when utdk.all_match_count is null then 0 else utdk.all_match_count end)+(case when utdl.all_match_count is null then 0 else utdl.all_match_count end)+(case when utma.all_match_count is null then 0 else utma.all_match_count end)) as allMatchCount ");
		sql.append(" ,((case when utdk.ver_match_count is null then 0 else utdk.ver_match_count end)+(case when utdl.ver_match_count is null then 0 else utdl.ver_match_count end)+(case when utma.ver_match_count is null then 0 else utma.ver_match_count end)) as verMatchCount   ");
		sql.append(" ,((case when utdk.draw_match_count is null then 0 else utdk.draw_match_count end)+(case when utdl.draw_match_count is null then 0 else utdl.draw_match_count end)+(case when utma.draw_match_count is null then 0 else utma.draw_match_count end)) as drawMatchCount   ");
		sql.append(" ,((case when utdk.fail_match_count is null then 0 else utdk.fail_match_count end)+(case when utdl.fail_match_count is null then 0 else utdl.fail_match_count end)+(case when utma.fail_match_count is null then 0 else utma.fail_match_count end)) as failMatchCount   ");
		sql.append(" from u_team ut ");
		sql.append(" left join u_team_dek utdk on ut.team_id = utdk.team_id ");
		sql.append(" left join u_team_duel utdl on ut.team_id = utdl.team_id ");
		sql.append(" left join u_team_match utma on ut.team_id = utma.team_id ");
		return sql;
	}
	
	/**
	 * 
	 * @TODO 更新天梯数据通用sql
	 * @Title: updRankByTeamSql 
	 * @param sql
	 * @return
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:14:03
	 */
	public StringBuffer updRankByTeamSql(StringBuffer sql){
		sql.append(" select ut.team_id as teamId ");
		sql.append(" ,((case when utdk.gd is null then 0 else utdk.gd end)+(case when utdl.gd is null then 0 else utdl.gd end)+(case when utma.gd is null then 0 else utma.gd end)) as gd ");
		sql.append(" ,((case when utdk.all_match_count is null then 0 else utdk.all_match_count end)+(case when utdl.all_match_count is null then 0 else utdl.all_match_count end)+(case when utma.all_match_count is null then 0 else utma.all_match_count end)) as allMatchCount ");
		sql.append(" from u_team ut ");
		sql.append(" left join u_team_dek utdk on ut.team_id = utdk.team_id ");
		sql.append(" left join u_team_duel utdl on ut.team_id = utdl.team_id ");
		sql.append(" left join u_team_match utma on ut.team_id = utma.team_id ");
		return sql;
	}
	
	/**
	 * 
	 * @TODO 通用胜率数据接口
	 * @Title: getPublicRankingInfo 
	 * @param teamId 球队id
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:11:14
	 */
	@Override
	public HashMap<String, Object> getPublicRankingInfo(String teamId) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合
		baseDAO.getSessionFactory().getCurrentSession().clear();
		baseDAO.getSessionFactory().getCurrentSession().flush();
		
		HashMap<String, String> hqlMap = new HashMap<String, String>();
		hqlMap.put("teamId", teamId);
		
		// 1.挑战信息
		UTeamDek uTeamDek = baseDAO.get(hqlMap," from UTeamDek where UTeam.teamId = :teamId ");
		
		// 2.约战信息
		UTeamDuel uTeamDuel = baseDAO.get(hqlMap," from UTeamDuel where UTeam.teamId = :teamId ");
		
		// 3.赛事信息
		UTeamMatch uTeamMatch = baseDAO.get(hqlMap,"  from UTeamMatch where UTeam.teamId = :teamId ");
		
		//总场次
		Integer allMatchCount = 0;
		//总胜场
		Integer verMatchCount = 0;
		//总平场
		Integer drawMatchCount = 0;
		//总负场
		Integer failMatchCount = 0;
		
		//进球数
		Integer allGoalCount = 0;
		
		//失球数
		Integer allFumbleCount = 0;
		
		//总胜率
		Double sumChances = 0.0;
		
		if(null!=uTeamDek){
			allMatchCount = allMatchCount + uTeamDek.getAllMatchCount();
			verMatchCount = verMatchCount + uTeamDek.getVerMatchCount();
			drawMatchCount = drawMatchCount + uTeamDek.getDrawMatchCount();
			failMatchCount = failMatchCount + uTeamDek.getFailMatchCount();
			allGoalCount = allGoalCount + uTeamDek.getAllGoal();
			allFumbleCount = allFumbleCount + uTeamDek.getAllFumble();
		}
		if(null!=uTeamDuel){
			allMatchCount = allMatchCount + uTeamDuel.getAllMatchCount();
			verMatchCount = verMatchCount + uTeamDuel.getVerMatchCount();
			drawMatchCount = drawMatchCount + uTeamDuel.getDrawMatchCount();
			failMatchCount = failMatchCount + uTeamDuel.getFailMatchCount();
			allGoalCount = allGoalCount + uTeamDuel.getAllGoal();
			allFumbleCount = allFumbleCount + uTeamDuel.getAllFumble();
		}
		if(null!=uTeamMatch){
			allMatchCount = allMatchCount + uTeamMatch.getAllMatchCount();
			verMatchCount = verMatchCount + uTeamMatch.getVerMatchCount();
			drawMatchCount = drawMatchCount + uTeamMatch.getDrawMatchCount();
			failMatchCount = failMatchCount + uTeamMatch.getFailMatchCount();
			allGoalCount = allGoalCount + uTeamMatch.getAllGoal();
			allFumbleCount = allFumbleCount + uTeamMatch.getAllFumble();
		}
		
		DecimalFormat df = new DecimalFormat("0.0");// 格式化小数，不足的补0
		if(verMatchCount!=0&&allMatchCount!=0){
			sumChances = 1.0*verMatchCount/allMatchCount;
			sumChances = sumChances*100;
			resultMap.put("chances", df.format(sumChances));
			
		}else{
			resultMap.put("chances", df.format(sumChances));
		}
		
		resultMap.put("allMatchCount", allMatchCount);
		resultMap.put("verMatchCount", verMatchCount);
		resultMap.put("drawMatchCount", drawMatchCount);
		resultMap.put("failMatchCount", failMatchCount);
		resultMap.put("allGoalCount", allGoalCount);
		resultMap.put("allFumbleCount", allFumbleCount);
		
		return resultMap;
	}

	/**
	 * 
	 * @TODO 天梯搜索接口
	 * @Title: getRankingSearch 
	 * @param map
	 * 		searchStr 搜索条件
	 * @return
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:10:36
	 */
	@Override
	public HashMap<String, Object> getRankingSearch(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> resultMap = new HashMap<>();// 返回集合

		List<HashMap<String, Object>> teamResult = new ArrayList<HashMap<String, Object>>();// 查询集合
		
		Integer pageNum =  map.get("page") == null||Integer.parseInt(map.get("page"))<=0 ? 1 : Integer.parseInt(map.get("page"));
		//PageLimit page = new PageLimit(0 >= Integer.parseInt(map.get("page")) ? 0 : Integer.parseInt(map.get("page")),0);

		StringBuffer sql = new StringBuffer();
		sql = RankingListDataSql(sql);
		sql.append(" where ut.team_status='3' and ut.team_use_status='2' ");
		if (null != map.get("searchStr")) {
			map.put("searchStr","%"+map.get("searchStr").toString()+"%");
			sql.append(" and ut.name like :searchStr ");
			sql.append(" order by ut.integral DESC,allMatchCount DESC,gd DESC,ut.createdate ASC,ut.createtime ASC ");
			sql.append((pageNum - 1) * Public_Cache.PAGE_LIMIT);
			sql.append(" limit "+Public_Cache.PAGE_LIMIT);

			teamResult = baseDAO.findSQLMap(map, sql.toString());
			
		}
		

		int j = 0;
		// 生成排名序号
		for (int i = 0; i < teamResult.size(); i++) {
			teamResult.get(i).put("rankNo", ++j);
		}
		resultMap.put("rankingList", teamResult);
		// 用户头部信息
		resultMap.put("userHeadInfo", uuserService.getUserinfoByUserId(map));
		// 球队头部信息
		resultMap.put("teamHeadInfo", uteamService.findUteaminfoListHead(map));

		return resultMap;
	}

	/**
	 * 
	 * @TODO 更新排行榜排名
	 * @Title: updateRankByList 
	 * @param map
	 * @throws Exception
	 * @author charlesbin
	 * @date 2016年6月1日 下午4:10:02
	 */
	@Override
	public void updateRankByList(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("coming update ranking");
		List<HashMap<String,Object>> teamResult = new ArrayList<HashMap<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql = updRankByTeamSql(sql);
		sql.append(" where ut.team_status='3' and ut.team_use_status='2' and ut.integral is not null  ");
		sql.append(" order by ut.integral DESC,allMatchCount DESC,gd DESC,ut.createdate ASC,ut.createtime ASC ");
		teamResult = baseDAO.findSQLMap(map, sql.toString());

		if(teamResult.size()>0){
			// 生成排名序号
			for (int i = 0; i < teamResult.size(); i++) {
				UTeam ut = baseDAO.get(UTeam.class,teamResult.get(i).get("teamId").toString());
				if(null!=ut){
//					System.out.println("-队名-----------------"+ut.getName());
//					System.out.println("-修改前排名-----------------"+ut.getRank());
					ut.setRank(i+1);
//					System.out.println("-分-----------------"+ut.getIntegral());
					
					baseDAO.save(ut);
					//同步数据库
					baseDAO.getSessionFactory().getCurrentSession().flush();
					//上传或修改LBS百度数据-更新排名
					if (ut.getuRegion()!=null&&"".equals(ut.getuRegion().get_id())) {
						map.put("area", ut.getuRegion().get_id());
						uteamService.setAreaToBaiduLBSCreateTeam(ut, map);//上传或修改LBS百度数据
					}
				}
			}
		}
	}
}
