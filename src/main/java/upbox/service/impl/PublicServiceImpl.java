package upbox.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.org.pub.COM_ORG_FinalArgs;
import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;

import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.UParameterInfo;
import upbox.model.UPlayer;
import upbox.model.UTeam;
import upbox.model.UTeamActivity;
import upbox.model.UTeamBehavior;
import upbox.model.UUser;
import upbox.model.UUserBehavior;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UParameterService;
import upbox.service.UTeamBehaviorService;
import upbox.service.UTeamService;
import upbox.service.UUserBehaviorService;
import upbox.service.UUserService;

/**
 * 通用接口实现
 * 
 * @author wmq
 *
 *         15618777630
 */
@Service("publicService")
public class PublicServiceImpl implements PublicService {
	@Resource
	private OperDAOImpl baseDao;
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private RedisOperDAOImpl redisDao;

	@Resource
	private UUserService uUserService;

	@Resource
	private UTeamService uTeamService;

	@Resource
	private UUserBehaviorService uUserBehaviorService;

	@Resource
	private UTeamBehaviorService uTeamBehaviorService;

	@Resource
	private UParameterService uParameterService;

	@Override
	public void saveListObject(List<?> list) throws Exception {
		if (null != list && list.size() > 0) {
			for (Object obj : list) {
				baseDao.save(obj);
			}
		}
	}

	@Override
	public void removeHRedis(String key) throws Exception {
		redisDao.del(key);
	}

	@Override
	public void updateHRedisByKey(String key, Object val) throws Exception {
		redisDao.set(key, SerializeUtil.serialize(val), COM_ORG_FinalArgs.REDIS_CACHE_TIME);
	}

	@Override
	public void removeHRedis_HSET(String key, String filed) throws Exception {
		redisDao.delHSet(key, filed);
	}

	@Override
	public String updateBehavior(HashMap<String, String> map) throws Exception {
		String result = "null";
		if (this.StringUtil(map.get("type"))) {
			if ("1".equals(map.get("type"))) {// 用户
				if ("8".equals(map.get("behaviorType")) || "9".equals(map.get("behaviorType")) || "11".equals(map.get("behaviorType"))|| "12".equals(map.get("behaviorType"))) {
					if (this.StringUtil(map.get("teamId"))) {
						List<UPlayer> uPlayers = baseDAO.find(map, "from UPlayer where UTeam.teamId=:teamId and inTeam='1'");
						if (null != uPlayers) {
							for (UPlayer uPlayer : uPlayers) {
								if (null != uPlayer.getUUser()) {
									map.put("userId", uPlayer.getUUser().getUserId());
									this.setinsertUserBehavior(map);
								}
							}
						}
					}
				}else{
					this.setinsertUserBehavior(map);
				}
				//设置首次参与约战或首次参与挑战
				this.setOtherUUserBehavior(map);
			} else if ("2".equals(map.get("type"))) {// 球队 步骤同上
				if (null != map.get("teamId") && !"".equals(map.get("teamId")) && !"null".equals(map.get("teamId"))) {
					UTeamBehavior uTeamBehavior = (UTeamBehavior) this.getBehavior(map);
					if (null == uTeamBehavior) {
						result = this.insertUTeamBehavior(map, uTeamBehavior);
					} else {// 如果不为空，查询是否为球队成立事件
						result = this.updateUTeamBehavior(map, uTeamBehavior);
					}
					//设置首次参与约战或首次参与挑战
					this.setOtherTeamBehavior(map);
				}
			} else {
				result = "fail";
			}
			
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 设置首次参与约战或首次参与挑战
	   @param map
	   @return
	   @throws Exception
	   2016年8月10日
	   dengqiuru
	 */
	private String setOtherTeamBehavior(HashMap<String, String> map) throws Exception {
		String result = null;
		if (this.StringUtil(map.get("behaviorType"))) {
			if ("4".equals(map.get("behaviorType")) || "5".equals(map.get("behaviorType")) ||  "6".equals(map.get("behaviorType")) || "7".equals(map.get("behaviorType"))) {
				map.put("behaviorType", "12");
				UTeamBehavior uTeamBehavior = (UTeamBehavior) this.getBehavior(map);
				if (null == uTeamBehavior) {
					result = this.insertUTeamBehavior(map, uTeamBehavior);
				} 
			}else if("8".equals(map.get("behaviorType")) || "9".equals(map.get("behaviorType")) || "10".equals(map.get("behaviorType"))){
				map.put("behaviorType", "13");
				UTeamBehavior uTeamBehavior = (UTeamBehavior) this.getBehavior(map);
				if (null == uTeamBehavior) {
					result = this.insertUTeamBehavior(map, uTeamBehavior);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 设置首次参与约战或首次参与挑战
	   @param map
	   2016年8月10日
	   dengqiuru
	 * @throws Exception 
	 */
	private String setOtherUUserBehavior(HashMap<String, String> map) throws Exception {
		String result = null;
		if (this.StringUtil(map.get("teamId"))) {
			List<UPlayer> uPlayers = baseDAO.find(map, "from UPlayer where UTeam.teamId=:teamId and inTeam='1'");
			if (null != uPlayers) {
				for (UPlayer uPlayer : uPlayers) {
					if (null != uPlayer.getUUser()) {
						if ("8".equals(map.get("behaviorType")) || "9".equals(map.get("behaviorType"))) {
							map.put("behaviorType", "7");
							UUserBehavior uUserBehavior = (UUserBehavior) this.getBehavior(map);
							if (null == uUserBehavior) {
								result = this.insertUserBehavior(map);
							}
						}else if("11".equals(map.get("behaviorType")) || "12".equals(map.get("behaviorType"))){
							map.put("behaviorType", "10");
							UUserBehavior uUserBehavior = (UUserBehavior) this.getBehavior(map);
							if (null == uUserBehavior) {
								result = this.insertUserBehavior(map);
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 添加用户里程碑事件
	   @param map
	   2016年8月10日
	   dengqiuru
	 * @throws Exception 
	 */
	private String setinsertUserBehavior(HashMap<String, String> map) throws Exception {
		String result = null;
		if (this.StringUtil(map.get("userId"))) {
			if (null != map.get("userId") && !"".equals(map.get("userId"))
					&& !"null".equals(map.get("userId"))) {
				UUserBehavior uUserBehavior = (UUserBehavior) this.getBehavior(map);
				if (null == uUserBehavior) {
					result = this.insertUserBehavior(map);
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 修改球队的事件
	   @param map
	   @param uTeamBehavior
	   @return
	   @throws Exception
	   2016年8月10日
	   dengqiuru
	 */
	private String updateUTeamBehavior(HashMap<String, String> map, UTeamBehavior uTeamBehavior) throws Exception {
		HashMap<String, String> paramsMap = new HashMap<>();//查询参数的集合
		String result = "null";
		if (this.StringUtil(uTeamBehavior.getTeamBehaviorType())) {
			if ("1".equals(uTeamBehavior.getTeamBehaviorType())) {// 球队成立日期可修改，修改后先删除缓存，再修改数据库，最后存入缓存
				if (this.StringUtil(map.get("behaviorType"))) {
					UTeam uTeam = baseDAO.get(map, "from UTeam where teamId=:teamId");
					if (null != uTeam) {
						redisDao.del(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UTeamBehavior",
								map.get("teamId")));
						uTeamBehaviorService.updateDate(uTeamBehavior, map, uTeam);
						if (null != uTeamBehavior) {
							int index = 0;
							// 查询数据库当前球队的时间轴
							List<UTeamBehavior> uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviors(map);
							if (null != uTeamBehaviorlist && uTeamBehaviorlist.size() > 0) {// 数据库存在，
								for (UTeamBehavior uTeamBehaviorTemp : uTeamBehaviorlist) {
									paramsMap.put("name", "team_behavior_type");
									paramsMap.put("params", map.get("behaviorType"));
									//查询该里程碑是否显示
									UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(paramsMap);
									if (null != uParameterInfo) {
										if (!"-1".equals(uParameterInfo.getParamsIsShow())) {
											redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
													"UTeamBehavior", map.get("teamId")), index,
													SerializeUtil.serialize(uTeamBehaviorTemp));
										}
									}else{
										redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
												"UTeamBehavior", map.get("teamId")), index,
												SerializeUtil.serialize(uTeamBehaviorTemp));
									}
									index++;
								}
							}
							result = "success";
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 新建球队的事件
	   @param map
	   @param uTeamBehavior
	   @return
	   @throws Exception
	   2016年8月10日
	   dengqiuru
	 */
	private String insertUTeamBehavior(HashMap<String, String> map, UTeamBehavior uTeamBehavior) throws Exception {
		HashMap<String, String> paramsMap = new HashMap<>();//查询参数的集合
		String result = "null";
		if (this.StringUtil(map.get("behaviorType"))) {
			UTeam uTeam = baseDao.get(UTeam.class, map.get("teamId"));
			if (null != uTeam) {
				uTeamBehavior = uTeamBehaviorService.insertuTeamBehavior(map, uTeam);
				if (null != uTeamBehavior) {
					redisDao.del(
							PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UTeamBehavior", map.get("teamId")));
					if (null != uTeamBehavior) {
						int index = 0;
						// 查询数据库当前球队的时间轴
						List<UTeamBehavior> uTeamBehaviorlist = uTeamBehaviorService.getuTeamBehaviors(map);
						if (null != uTeamBehaviorlist && uTeamBehaviorlist.size() > 0) {// 数据库存在，
							for (UTeamBehavior uTeamBehaviorTemp : uTeamBehaviorlist) {
								paramsMap.put("name", "team_behavior_type");
								paramsMap.put("params", uTeamBehaviorTemp.getTeamBehaviorType());
								//查询该里程碑是否显示
								UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(paramsMap);
								if (null != uParameterInfo) {
									if (!"-1".equals(uParameterInfo.getParamsIsShow())) {
										redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
												"UTeamBehavior", map.get("teamId")), index,
												SerializeUtil.serialize(uTeamBehaviorTemp));
									}
								}else{
									redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,
											"UTeamBehavior", map.get("teamId")), index,
											SerializeUtil.serialize(uTeamBehaviorTemp));
								}
								index++;
							}
						}
						result = "success";
					}
				} else {
					result = "fail";
				}
			} else {
				result = "error";
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 修改用户的事件
	   @param map
	   @return
	   @throws Exception
	   2016年8月10日
	   dengqiuru
	 */
	private String insertUserBehavior(HashMap<String, String> map) throws Exception {
		HashMap<String, String> paramsMap = new HashMap<>();//查询参数的集合
		String result = "null";
		// 查询缓存是否存在
		if (this.StringUtil(map.get("userId"))) {
			UUser uUserTemp = baseDao.get(UUser.class, map.get("userId"));
			if (null != uUserTemp) {
				// 先删除缓存里面对应球员的事件
				redisDao.del(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UUserBehavior", map.get("userId")));
				UUserBehavior uUserBehavior = uUserBehaviorService.insertuUserBehavior(map, uUserTemp);// 先存入数据库
				if (null != uUserBehavior) {
					int index = 0;
					// 查询数据库当前球队的时间轴
					List<UUserBehavior> uUserBehaviors = uUserBehaviorService.getuUserBehaviors(map);
					if (null != uUserBehaviors && uUserBehaviors.size() > 0) {// 数据库存在，
						for (UUserBehavior uUserBehaviorTemp : uUserBehaviors) {
							paramsMap.put("name", "user_behavior_type");
							paramsMap.put("params", map.get("behaviorType"));
							//查询该里程碑是否显示
							UParameterInfo uParameterInfo = uParameterService.getMemberTypeByTeamId202(paramsMap);
							if (null != uParameterInfo) {
								if (!"-1".equals(uParameterInfo.getParamsIsShow())) {
									redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UUserBehavior",
											map.get("userId")), index, SerializeUtil.serialize(uUserBehaviorTemp));
								}
							}else{
								redisDao.setSortedSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UUserBehavior",
										map.get("userId")), index, SerializeUtil.serialize(uUserBehaviorTemp));
							}
							index++;
						}
					}
					result = "success";
				}
			} else {
				result = "error";
			}
		}
		return result;
	}

	@Override
	public Boolean StringUtil(String param) throws Exception {
		boolean isNull = false;
		if (null != param && !"".equals(param) && !"null".equals(param)) {
			isNull = true;
		}
		return isNull;
	}

	@Override
	public Object getBehavior(HashMap<String, String> map) throws Exception {
		Object object = null;
		if ("1".equals(map.get("type"))) {// 用户
			if (this.StringUtil(map.get("behaviorType"))) {
				if (null == object || "".equals(object.toString()) || "null".equals(object.toString())) {
					UUserBehavior uUserBehavior = uUserBehaviorService.getuUserBehavior(map);
					object = uUserBehavior;
				}
			}
		} else if ("2".equals(map.get("type"))) {// 球队 步骤同上
			if (this.StringUtil(map.get("behaviorType"))) {
				UTeamBehavior tt = uTeamBehaviorService.getuTeamBehavior(map);
				object = tt;
			}
		}
		return object;
	}

	@Override
	public List<Object> getBehaviorList(HashMap<String, String> map) throws Exception {
		Set<Serializable> set = null;
		List<Object> array = new ArrayList<Object>();
		if ("1".equals(map.get("type"))) {// 用户
			set = redisDao.getSoredSet(
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UUserBehavior", map.get("userId")), 0, 30,
					false);
			if (null != set && set.size() > 0) {
				Iterator<Serializable> it = set.iterator();
				while (it.hasNext()) {
					array.add(SerializeUtil.unSerializeToBean(it.next().toString(), UUserBehavior.class));
				}
			}
		} else if ("2".equals(map.get("type"))) {// 球队 步骤同上
			set = redisDao.getSoredSet(
					PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "UTeamBehavior", map.get("teamId")), 0, 30,
					false);
			if (null != set && set.size() > 0) {
				Iterator<Serializable> it = set.iterator();
				while (it.hasNext()) {
					array.add(SerializeUtil.unSerializeToBean(it.next().toString(), UTeamBehavior.class));
				}
			}
		}
		return array;
	}

	@Override
	public void addTeamActivity(String activity_status, String team_id) throws Exception {
		UTeamActivity uta = new UTeamActivity();
		uta.setKeyId(WebPublicMehod.getUUID());
		uta.setActivityStatus(activity_status);
		uta.setCreatetime(new Date());
		uta.setTeamId(team_id);
		baseDao.save(uta);
	}

	@Override
	public int getIntKeyId(int type, HashMap<String, String> map) {
		try {
			List<HashMap<String, Object>> list = null;
			String sql = "";
			switch (type) {
			case 1:
				sql = "select user_id_int as intid from u_user where user_id = :id";
				break;
			case 2:
				sql = "select team_id_int as intid from u_team where team_id = :id";
				break;
			case 3:
				sql = "select court_id_int as intid from u_court where court_id = :id";
				break;
			case 4:
				sql = "select subcourt_id_int as intid from u_br_court where subcourt_id = :id";
				break;
			case 5:
				sql = "select duel_id_int as intid from u_duel where duel_id = :id";
				break;
			case 6:
				sql = "select challege_id_int as intid from u_challenge where challenge_id = :id";
				break;
			default:
				break;
			}
			list = baseDao.findSQLMap(map, sql);
			System.out.println(list.get(0).get("intid"));
			return (int) list.get(0).get("intid");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return -1;
	}
	
	/** 
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty 
     *  
     * @param obj 
     * @return 
     */  
	@Override
    public boolean isNullOrEmpty(Object obj) {  
        if (obj == null)  
            return true;  
  
        if (obj instanceof CharSequence)  
            return ((CharSequence) obj).length() == 0;  
  
        if (obj instanceof Collection)  
            return ((Collection) obj).isEmpty();  
  
        if (obj instanceof Map)  
            return ((Map) obj).isEmpty();  
  
        if (obj instanceof Object[]) {  
            Object[] object = (Object[]) obj;  
            if (object.length == 0) {  
                return true;  
            }  
            boolean empty = true;  
            for (int i = 0; i < object.length; i++) {  
                if (!isNullOrEmpty(object[i])) {  
                    empty = false;  
                    break;  
                }  
            }  
            return empty;  
        }  
        return false;  
    }  
	private static URL url;  
	private static HttpURLConnection con;  
	private static int state = -1; 
	@Override
	public synchronized String isConnect(String urlStr) throws IOException {
		String msg = null;
		int counts = 0;  
		while (counts < 5) {  
//			try {
			url = new URL(urlStr);  
			con = (HttpURLConnection) url.openConnection();  
			state = con.getResponseCode();  
			if (state == 200) {  
				msg = "ok";
			}  
			break;  
//			}catch (Exception ex) {  
//				counts++;   
////				System.out.println("URL不可用，连接第 "+counts+" 次");  
//				urlStr = null;  
//				continue;  
//			}  
		}  
		return msg;  
	}
	
	
	  /**
     * 判断手机号码
     *
     * @param phone
     * @return
     */
	@Override
    public boolean validateMoblie(String phone) {
        boolean rs = false;
        if (matchingText("^(13[0-9]|15[0-9]|18[0-9]|14[5|7]|17[0-9])\\d{4,8}$", phone)) {
            rs = true;
        } else {
            rs = false;
        }
        return rs;
    }

    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        boolean b = m.matches();
        return b;
    }
}
