package upbox.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.org.bean.HttpRespons;
import com.org.pub.COM_ORG_FinalArgs;
import com.org.pub.HttpUtil;
import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;
import upbox.aes.CipherHelper;
import upbox.dao.impl.OperDAOImpl;
import upbox.dao.impl.RedisOperDAOImpl;
import upbox.model.PageLimit;
import upbox.model.UBaidulbs;
import upbox.model.UEquipment;
import upbox.model.UPlayer;
import upbox.model.URegion;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.UUserImg;
import upbox.pub.Constant;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.LBSService;
import upbox.service.MessageService;
import upbox.service.PublicService;
import upbox.service.TopicService;
import upbox.service.UEquipmentService;
import upbox.service.UPlayerService;
import upbox.service.URegionService;
import upbox.service.UTouristService;
import upbox.service.UUserImgService;
import upbox.service.UUserLogService;
import upbox.service.UUserService;
import upbox.service.UWorthService;

/**
 * 前端端用户接口实现类
//		redisDao.getHSet(key, filed);//根据key，token查询缓存中的token
 *
 */
@Service("uuserService")
public class UUserServiceImpl implements UUserService {
	@Resource
	private OperDAOImpl baseDAO;
	@Resource
	private RedisOperDAOImpl redisDao;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UPlayerService uplayerService;
	
	@Resource
	private UEquipmentService uEquipmentService;
	
	@Resource
	private UTouristService utouristService;
	
	@Resource
	private UUserLogService uUserLogService;
	
	@Resource
	private UUserImgService uUserImgService;
	
	@Resource
	private URegionService uRegionService;
	
	@Resource
	private MessageService messageService;
	
	@Resource
	private LBSService lbsService;
	@Resource
	private UWorthService uworthService;
	@Resource
	private TopicService topicService;
	
	/**
	 * 
	 * 
	   TODO - 查所有的前端用户信息
	   @param map
			page  	分页
	   @return
	   		uUserList 的hashMap
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> findAll(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		if (publicService.StringUtil(map.get("page"))) {
			PageLimit page = new PageLimit(Integer.parseInt(map.get("page")), 0);
			String hql = "from UUser where user_status='1' order by createdate desc";
			List<UUser> uUserList = baseDAO.findByPage(map, hql, page.getLimit(), page.getOffset());
			resultMap.put("uUserList", uUserList);
		}else{
			return WebPublicMehod.returnRet("error", "page不能为空！");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 获取验证码接口
	   @param map
	   		phone	手机号码
	   		rt		标示
	   @return
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getVerCodeMethod(HashMap<String, String> map) throws Exception {
		//传过来的参数
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//发送短信的参数
		HashMap<String, String> sendMap1 = new HashMap<String, String>();
		//发送短信的参数
		HashMap<String, String> sendMap2 = new HashMap<String, String>();
		//发送短信返回的参数
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String verifyCode = "";//验证码
		String smsFreeSignName = null;//签名
		String smsTemplateCode = null;//模板ID
		String recNum = null;//手机
		String extend = null;//回调参数
		UUser uUser = null;
		if("changePhone".equals(map.get("rt"))){//更换手机号，根据新号码发送验证码
			if (publicService.StringUtil(map.get("phone"))) {
				if (publicService.validateMoblie(map.get("phone"))) {
					hashMap.put("phone", map.get("phone"));
					List<UUser> uUserList = baseDAO.find("from UUser where phone=:phone", hashMap);
					if (null != uUserList && uUserList.size() > 0) {//新号码不能是在库里已经存在的
						return WebPublicMehod.returnRet("error", "你输入的手机号码已存在");
					}else{
//						uUser = baseDAO.getHRedis(UUser.class,map.get("userId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
						uUser = baseDAO.get(map, "from UUser where userId=:userId");
						hashMap = this.getChangePhoneCode(uUser,map);
						smsFreeSignName = "激战联盟";
						smsTemplateCode = "SMS_7751437";
					}
				}else{
					return WebPublicMehod.returnRet("error", "手机号码格式不正确");
				}
			}else{
				return WebPublicMehod.returnRet("error", "请输入手机号码");
			}
		}else{//其他操作
			if (publicService.StringUtil(map.get("phone"))) {//首先手机号不能为空
				if (publicService.validateMoblie(map.get("phone"))) {
					hashMap.put("phone", map.get("phone"));
					List<UUser> uUserList = baseDAO.find("from UUser where phone=:phone", hashMap);
					if (null != uUserList) {//查询改手机号是否存在
						if (uUserList.size() > 0 && uUserList.size() < 2) {//存在 且只有一条
							uUser = uUserList.get(0);
							if ("forgetPwd".equals(map.get("rt"))) {//忘记密码：手机号是一定得数据库里面存在的
								if ("1".equals(uUser.getUserStatus())) {
									hashMap = this.getForgetPwdCode(uUser);
									smsFreeSignName = "激战联盟";
									smsTemplateCode = "SMS_7741287";
								}else{
									return WebPublicMehod.returnRet("error", "该电话号码还未注册");
								}
							}else if("loginAuth".equals(map.get("rt"))){//第三方登录也是允许手机号存在的
								//第三方获取验证码
								hashMap = this.getLoginAuthCode(uUser,map);
								smsFreeSignName = "激战联盟";
								smsTemplateCode = "SMS_7215109";
							}else{//其他：注册和更换手机号都是不允许手机号存在，提示用户他的手机号存在
								if(null == map.get("rt") || "".equals(map.get("rt")) || "null".equals(map.get("rt"))){//获取验证码
									if (publicService.StringUtil(uUser.getPwd())) {//注册密码存在，不能继续注册
										return WebPublicMehod.returnRet("error", "你输入的手机号码已经注册");
									}else{//密码不存在  代表未注册
										hashMap = this.getRegisterCode(uUser,map);
										smsFreeSignName = "激战联盟";
										smsTemplateCode = "SMS_7215107";
									}
								}else{
									return WebPublicMehod.returnRet("error", "您输入的手机号码已存在");
								}
							}
						}else if(uUserList.size() >= 2){//系统针对手机号去重，一个手机号出现多条记录都是提示他联系客服小激
							return WebPublicMehod.returnRet("error", "你的账户存在异常\n如有疑问拨打400-805-1719 联系客服小激");
						}else{
							if(null == map.get("rt") || "".equals(map.get("rt")) || "null".equals(map.get("rt"))){//获取验证码			
									hashMap = this.getRegisterCode(uUser,map);
									smsFreeSignName = "激战联盟";
									smsTemplateCode = "SMS_7215107";
							}else if("loginAuth".equals(map.get("rt"))){//第三方获取验证码
								hashMap = this.getLoginAuthCode(uUser,map);
								smsFreeSignName = "激战联盟";
								smsTemplateCode = "SMS_7215109";
							}else{
								return WebPublicMehod.returnRet("error", "账号不存在\n请输入正确的手机号码");
							}
						}
					}else{
						if(null == map.get("rt") || "".equals(map.get("rt")) || "null".equals(map.get("rt"))){//获取验证码
								hashMap = this.getRegisterCode(uUser,map);
								smsFreeSignName = "激战联盟";
								smsTemplateCode = "SMS_7215107";
						}else if("loginAuth".equals(map.get("rt"))){//第三方获取验证码
							hashMap = this.getLoginAuthCode(uUser,map);
							smsFreeSignName = "激战联盟";
							smsTemplateCode = "SMS_7215109";
						}else{
							return WebPublicMehod.returnRet("error", "账号不存在\n请输入正确的手机号码");
						}
					}
				}else{
					return WebPublicMehod.returnRet("error", "手机号码格式不正确");
				}
			}else{
				return WebPublicMehod.returnRet("error", "请输入手机号码");
			}
		}

		recNum = map.get("phone");
		extend = "123456";
		verifyCode =  hashMap.get("verifyCode").toString();
		
		//将验证码发送到手机上
		sendMap1.put("product", "【UPBOX激战联盟】");//参数  【UPBOX激战联盟】
		sendMap1.put("code", verifyCode);//参数
		sendMap2.put("extend", extend);//回调参数
		sendMap2.put("sms_free_sign_name", smsFreeSignName);//签名
		sendMap2.put("rec_num", recNum);//手机号
		sendMap2.put("sms_template_code", smsTemplateCode);//模板ID
		resultMap= WebPublicMehod.send(map.get("phone"), sendMap1,sendMap2);
		String msg = "success";
		if ("1".equals(resultMap.get("ret"))) {
			this.updateUserCode(uUser,verifyCode,recNum);
			 msg = "success";
		}else if("-1".equals(resultMap.get("ret"))) {
			 msg = "error";
		}
		return WebPublicMehod.returnRet(msg, resultMap.get("result"));
	}
	
	/**
	 * 
	 * 
	   TODO - 将验证码更新到用户里
	   @param uUser			用户对象
	   @param verifyCode	验证码
	   @param phone			手机号
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private void updateUserCode(UUser uUser,String verifyCode,String phone) throws Exception {
		//如果用户对象不为空，就更新用户对象，并更新缓存
		if (null != uUser) {
			uUser.setIdcode(verifyCode);
			uUser.setCodetime(new Date());
			baseDAO.update(uUser);
			this.updateRedisUuserinfo(uUser);
//			publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object",uUser.getUserId(),"Uuser"));
		}else{//如果对象为空，就添加一个用户对象
			UUser uUserTemp = new UUser();
			uUserTemp.setUserId(WebPublicMehod.getUUID());
			uUserTemp.setPhone(phone);
			uUserTemp.setIdcode(verifyCode);
			uUserTemp.setCodetime(new Date());
			uUserTemp.setOld_key_id(-1);
			baseDAO.save(uUserTemp);
		}
	}

	//获取注册验证码
	/**
	 * 
	 * 
	   TODO - 获取注册验证码
	   @param uUser    用户对象
	   @param map
	   		phone       手机号
	   @return
	   		verifyCode的hashMap
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private HashMap<String, Object> getRegisterCode(UUser uUser, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String verifyCode = "";//验证码
		if (null == uUser) {
			if (map.get("phone").length() == 11) {
				//生成验证码
				verifyCode = updatePhoneCode(uUser);
			}else{
				return WebPublicMehod.returnRet("error", "请输入正确的手机号码");
			}
		}else{
			//2:注册来源于线下预定球场；3：注册来源于后台创建；空：刚刚注册获取验证码
			if ("2".equals(uUser.getRegi_resouce()) || "3".equals(uUser.getRegi_resouce()) || "".equals(uUser.getRegi_resouce()) || "null".equals(uUser.getRegi_resouce())) {
				//以判断用户不为空，所以只做更新
				if (null != uUser.getCodetime()) {
					Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
					if (createCodeT > 1) {
						//生成验证码
						verifyCode = updatePhoneCode(uUser);
					}else{
						return WebPublicMehod.returnRet("error", "你的操作太频繁\n请在两分钟后再获取");
					}
				}else{
					verifyCode = updatePhoneCode(uUser);
				}
			}else{
				if (publicService.StringUtil(uUser.getPwd())) {
					return WebPublicMehod.returnRet("error", "你输入的手机号已经存在");
				}else{
					if (null != uUser.getCodetime()) {
						//以判断用户不为空，所以只做更新
						Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
						if (createCodeT > 1) {
							verifyCode = updatePhoneCode(uUser);
						}else{
							return WebPublicMehod.returnRet("error", "操作太频繁\n请在两分钟后再获取");
						}
					}else{
						verifyCode = updatePhoneCode(uUser);
					}
				}
			}
		}
		hashMap.put("verifyCode", verifyCode);
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 忘记密码获取验证码
	   @param uUser    对象
	   @return
	   		verifyCode的hashMap
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private HashMap<String, Object> getForgetPwdCode(UUser uUser) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String verifyCode = "";//验证码
		if (null == uUser) {//忘记密码，用户对象不能为空
			return WebPublicMehod.returnRet("error", "账号不存在\n请输入正确的手机号码");
		}else{//忘记密码、更换手机号  不用判断注册来源
			if (null != uUser.getCodetime()) {
				Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
				if (createCodeT > 1) {
					verifyCode = updatePhoneCode(uUser);
				}else{
					return WebPublicMehod.returnRet("error", "你的操作太频繁\n请在两分钟后再获取");
				}
			}else{
				verifyCode = updatePhoneCode(uUser);
			}
		}
		hashMap.put("verifyCode", verifyCode);
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 第三方登录获取验证码
	   @param uUser    对象
	   @return
	   		verifyCode的hashMap
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private HashMap<String, Object> getLoginAuthCode(UUser uUser, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		String verifyCode = "";//验证码
		if (null == uUser) {//用户为空，就说明未注册注册，需要新增数据
			verifyCode = updatePhoneCode(uUser);
		}else{//用户不为空，就说明已经注册，只用更新验证码即可
			if (null != uUser.getCodetime()) {
				Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
				if (createCodeT > 1) {
					verifyCode = updatePhoneCode(uUser);
				}else{
					return WebPublicMehod.returnRet("error", "你的操作太频繁\n请在两分钟后再获取");
				}
			}else{
				verifyCode = updatePhoneCode(uUser);
			}
		}
		hashMap.put("verifyCode", verifyCode);
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 更换手机号获取验证码
	   @param uUser    对象
	   @return
	   		verifyCode的hashMap
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private HashMap<String, Object> getChangePhoneCode(UUser uUser, HashMap<String, String> map) throws Exception {
		//传过来的参数
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (null != uUser) {
			//以判断用户不为空，所以只做更新
			if (null != uUser.getCodetime()) {
				Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
				if (createCodeT > 1) {
					String verifyCode = updatePhoneCode(uUser);
					hashMap.put("verifyCode", verifyCode);
				}else{
					return WebPublicMehod.returnRet("error", "你的操作太频繁\n请在两分钟后再获取");
				}
			}else{
				String verifyCode = updatePhoneCode(uUser);
				hashMap.put("verifyCode", verifyCode);
			}
		}else{
			return WebPublicMehod.returnRet("error", "你输入的帐号不存在");
		}
		return hashMap;
	}

	/**
	 * 
	 * 
	   TODO - 更新验证码
	   @param uUser 用户信息--userId
	   @return
	  		verifyCode     验证码
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	private String updatePhoneCode(UUser uUser) throws Exception {
		String verifyCode = getSix();
		return verifyCode;
	}	
	/**
	 * 
	 * 
	   TODO - 产生随机的六位数 
	   @return
	   2015年12月16日
	   dengqiuru
	 */
    public static String getSix(){  
        Random rad=new Random();  
        String result  = rad.nextInt(1000000) +"";  
        if(result.length()!=6){  
            return getSix();  
        }  
        return result;  
    }  
    
    /**
     * 
     * 
       TODO - 注册
       @param map
       		source			登录来源：1、微信；2、微博；3、QQ
       		logintoken		第三方登录token
       		nickName		昵称
	   		phone			手机
	   		phonecode		验证码
	   		code			设备号
	   		phoneType		设备类型
	   		nickname		昵称
	   		loginpassword	密码
	   		ip				客户端IP地址
       @return
       @throws Exception
       2015年12月16日
       dengqiuru
     */
	@Override
	public HashMap<String, Object> registerMethod(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//获取前端用户信息
		if (publicService.StringUtil(map.get("phone"))) {
			if (publicService.validateMoblie(map.get("phone"))) {
				hashMap.put("phone", map.get("phone"));
				UUser uUser = baseDAO.get("from UUser where phone=:phone",hashMap);
				//判断是直接注册还是第三方关联
				if (publicService.StringUtil(map.get("logintoken"))) {//logintoken不为空，就是第三方关联
					loginAuthTiedMethod(map,uUser);
				}else{//为空，就代表是用户注册功能
					String type = "4";//1代表注册
					if (null != uUser) {
						if (null == uUser.getPwd() || "".equals(uUser.getPwd())) {
							if (uUser.getIdcode().equals(map.get("phonecode"))) {
								Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
								if (createCodeT <= 1) {
									//查询该设备号在用户中是否已经存在  如果存在，就把其他用户的设备号设置为null
									if (publicService.StringUtil(map.get("code"))) {
										this.updateUserEquipment(map);
									}
									this.updateUuserinfoByRegi(uUser,map,type);
									uUser.setRegiStatus("1");//设置用户注册状态 1：注册接口   未走关联直接注册 2：关联接口  没有关联已经注册 3：关联接口   没有关联没有注册
									uEquipmentService.insertEquipmentUser(uUser,map);
									//将注册记录存入用户操作记录表
									String regiResource = "1";//前端用户自己注册的
									uUserLogService.saveUserLog(uUser,regiResource);
								}else{
									return WebPublicMehod.returnRet("error", "验证码已过期\n请重新获取验证码");
								}
							}else{
								return WebPublicMehod.returnRet("error", "你输入的验证码有误");
							}
						}else{
							return WebPublicMehod.returnRet("error", "你输入的手机号码已存在");
						}
					}else{
						return WebPublicMehod.returnRet("error", "你的验证码和手机号不匹配\n请重新获取验证码");
					}
				}
				this.updateToken(uUser, map);
				this.setUuserBehavior(map,uUser);//设置时间轴
				//添加初始球员信息
				uplayerService.createUplayer(map, uUser, null);
				resultMap.put("uUser", uUser);
			}else{
				return WebPublicMehod.returnRet("error", "手机号格式有误");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请输入手机号");
		}
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 用户注册时间轴
	   @param map		
		   	type  			1:用户 2：球队
			behaviorType	事件类型
			userId			用户id
			teamId			球队Id
			objectId		对应事件的Id
			createDate		时间（如果事件不是当前时间，就得传这个参数 【例如：球队成立时间】）
	   @param uUser   对象
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private void setUuserBehavior(HashMap<String, String> map, UUser uUser) throws Exception {
		//用户首次注册
		//设置时间轴
		map.put("type", "1");
		map.put("behaviorType", "1");
		map.put("objectType", null);
		map.put("userId", uUser.getUserId());
		map.put("objectId", uUser.getUserId());
		publicService.updateBehavior(map);
	}

	/**
	 * 
	 * 
	   TODO - 用户注册时，更新用户信息
	   @param uUser   对象
	   @param map
	   		loginpassword 		登录密码
	   		realName			真实名称
	   		phone				手机号码
       		source				登录来源：1、微信；2、微博；3、QQ
       		logintoken			第三方登录token
       		openid				微信openid
	   @param type				注册来源
	   @throws Exception
	   2016年3月14日
	   dengqiuru
	 */
	private void updateUuserinfoByRegi(UUser uUser, HashMap<String, String> map,String type) throws Exception {
		if (publicService.StringUtil(map.get("loginpassword"))) {
			uUser.setPwd(CipherHelper.md5(map.get("loginpassword")));
		}
		if (publicService.StringUtil(map.get("realName"))) {
			uUser.setRealname(map.get("realName"));
		}
		uUser.setPhone(map.get("phone"));
		if (null == uUser.getUserStatus() || "".equals(uUser.getUserStatus())) {//默认待关联  关联时判断状态是否为空，为空就设置待关联，不为空就不设置
			uUser.setUserStatus("1");
		}
		if (null == uUser.getUserType() || "".equals(uUser.getUserType())) {//默认待关联  关联时判断状态是否为空，为空就设置待关联，不为空就不设置
			uUser.setUserType("1");
		}
		if (null == uUser.getCreatedate() || "".equals(uUser.getUserType())) {
			uUser.setCreatedate(new Date());
		}
		uUser.setRegi_resouce(type);
		if (publicService.StringUtil(map.get("source"))) {
			if ("1".equals(map.get("source"))) {// 微信
				uUser.setWechatlogintoken(map.get("logintoken"));
				uUser.setOpenid(map.get("openid"));
			}else if ("2".equals(map.get("source"))) {
				uUser.setWeibologintoken(map.get("logintoken"));
			}else if ("3".equals(map.get("source"))) {
				uUser.setQqlogintoken(map.get("logintoken"));
			}
		}
		if (publicService.StringUtil(map.get("appCode"))) {
			int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));
			uUser.setVersion(intAppCode+"");
		}
		baseDAO.update(uUser);
		//更新缓存
		this.updateRedisUuserinfo(uUser);
	}

	/**
	 * 
	 * 
	   TODO - 注册后录入基本信息  【2.0.0】
	   @param map
	   		userId			用户Id
	   		nickName		昵称
	   		birthday		出生年月
	   		height			身高
	   		weight			体重
	   		sex				性别
	   		position		球队中位置
	   		number			球队中背号
	   		memberType		球队中身份
	   		canPosition		可踢位置
	   		practiceFoot	惯用脚
	   		expertPosition	擅长位置
	   		imgSizeType		图片尺寸类型
	   		imgurl			图片显示地址
	   		imgWeight		图片权重
	   		saveurl		   	图片存储地址
	   @return
	   		uUser 对象的hashMap<String,Object>
	   @throws Exception
	   2016年2月25日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> insertUserinfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();//输出
		if (publicService.StringUtil(map.get("userId"))) {
			UUser uUser = baseDAO.getHRedis(UUser.class,map.get("userId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			if (null != uUser) {
				if (publicService.StringUtil(map.get("imgurl"))) {
					//将头像上传到用户图片表中
					String msg = publicService.isConnect(map.get("imgurl"));
					if (null != msg && "ok".equals(msg)) {
						uUserImgService.uploadHeadPic(map);
					}else{
						return WebPublicMehod.returnRet("error", "非有效url");
					}
				}
				if (publicService.StringUtil(map.get("nickName"))) {
					uUser.setNickname(map.get("nickName"));//真实姓名
				}if (publicService.StringUtil(map.get("birthday"))) {
					Date birthday = PublicMethod.getStringToDate(map.get("birthday"), "yyyy-MM-dd");
					uUser.setBirthday(birthday);//出生日期
					uUser.setAge(getAgeByBirthday(birthday));//年龄
				}if (publicService.StringUtil(map.get("height"))) {
					uUser.setHeight(map.get("height"));//身高
				}if (publicService.StringUtil(map.get("weight"))) {
					uUser.setWeight(map.get("weight"));//体重
				}if (publicService.StringUtil(map.get("sex"))) {
					uUser.setSex(map.get("sex"));//性别
				}
				baseDAO.getSessionFactory().getCurrentSession().flush();
				baseDAO.getSessionFactory().getCurrentSession().clear();
				baseDAO.update(uUser);
				//更新年龄lbs数据
				this.setAreaToBaiduLBS(uUser);
				//更新缓存
				this.updateRedisUuserinfo(uUser);
				//将可踢位置、擅长位置、惯用脚插入到球员表中
				UPlayer player=uplayerService.createUplayer(map, uUser, null);
				//保存首次加入球队身价信息
				if(publicService.StringUtil(uUser.getRealname())&&publicService.StringUtil(uUser.getNickname())&&publicService.StringUtil(uUser.getSex())&&uUser.getBirthday()!=null&&publicService.StringUtil(uUser.getWeight())&&publicService.StringUtil(uUser.getHeight())){
					if(player!=null&&publicService.StringUtil(player.getPracticeFoot())&&publicService.StringUtil(player.getExpertPosition())&&publicService.StringUtil(player.getCanPosition())){
						map.put("taskBehavior", "2");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
						resultMap.putAll(uworthService.saveTaskInfo(map));	
					}
				}

				resultMap.put("uUser", uUser);
			}else{
				return WebPublicMehod.returnRet("error", "用户不能为空！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "userId不能为空！");
		}
		return resultMap;
	}

	
	/**
	 * 
	 * 
	   TODO - 更新设备号ID到u_user表里面
	   @param uUser
	   		uUser对象
	   @param equipment
	   		equipment对象
	   2015年12月18日
	   dengqiuru
	 * @throws Exception 
	 */
	public void updateUuserEquip(UUser uUser,UEquipment equipment) throws Exception{
//		UUser uUserTemp = baseDAO.getHRedis(UUser.class,uUser.getUserId(),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
		if (null != equipment) {
			uUser.setNumberid(equipment.getKeyId());
			baseDAO.update(uUser);
//			//在游客表里进行关联操作
//			utouristService.inTourist(uUserTemp,equipment,type);
			this.updateRedisUuserinfo(uUser);
//			publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object",uUser.getUserId()));
		}
	}

	/**
	 * 
	 * 
	   TODO - 忘记密码	
	   @param map
	   		ip				客户端IP地址
	   		code			设备号
	   		phoneType		设备类型
	   		cpu				内核
	   		phone			手机号
	   		phonecode		验证码
	   		loginpassword	登录密码
	   2015年12月14日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public HashMap<String, Object> getPasswordMethod(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//获取前端用户信息
		if (publicService.StringUtil(map.get("phone"))) {
			if (publicService.validateMoblie(map.get("phone"))) {
				hashMap.put("phone", map.get("phone"));
				UUser uUser = baseDAO.get("from UUser where phone=:phone",hashMap);
				if (null != uUser) {//用户存在
					if (publicService.StringUtil(uUser.getIdcode())) {
						if (map.get("phonecode").equals(uUser.getIdcode())) {//验证码输入正确
							Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
							if (createCodeT >= 0 && createCodeT <= 1) {//在获取验证码2分钟内注册
								uUser.setPwd(CipherHelper.md5(map.get("loginpassword")));
								baseDAO.update(uUser);
								if (publicService.StringUtil(map.get("code"))) {//code不为空，更新设备信息
									this.updateUserEquipment(map);
									uEquipmentService.insertEquipmentUser(uUser,map);
								}
								//更新缓存
								this.updateRedisUuserinfo(uUser);
							}else{
								return WebPublicMehod.returnRet("error", "验证码已过期\n请重新获取验证码");
							}
						}else{
							return WebPublicMehod.returnRet("error", "你的验证码和手机号不匹配\n请重新获取验证码");
						}
					}else{
						return WebPublicMehod.returnRet("error", "请先获取验证码");
					}
				}else{
					return WebPublicMehod.returnRet("error", "账号不存在\n请输入正确的手机号码");
				}
				this.updateToken(uUser,map);
				resultMap.put("uUser", uUser);
			}else{
				return WebPublicMehod.returnRet("error", "手机号格式有误");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请输入手机号");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 设置token和用户状态
	   @param uUser
	   @param map
	   @throws Exception
	   2016年3月15日
	   dengqiuru
	 */
	private void updateToken(UUser uUser, HashMap<String, String> map) throws Exception {
		//登录时，更新用户操作记录表
		uUserLogService.updateUuserLog(uUser);
		//登录后更新Token
		map.put("token", WebPublicMehod.getUUID());
		updateUuserToken(uUser,map);
		//设置用户状态
		uUser.setUseStatus("1");
	}

	/**
	 * 
	 * 
	   TODO - 登录
	   @param map
	   		phone			手机号码
	   		loginpassword	密码
	   		token			用户登陆识别码
	   		ip				客户端IP地址
	   		code			设备号
	   		phoneType		设备类型
	   @return
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> loginMethod(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//查询该设备号在用户中是否已经存在  如果存在，就把其他用户的设备号设置为null
		//获取前端用户信息
		hashMap.put("phone", map.get("phone"));
		hashMap.put("pwd", CipherHelper.md5(map.get("loginpassword")));
		List<UUser> uUserList = baseDAO.find("from UUser where phone=:phone",hashMap);
		//存在当前用户
		if (null != uUserList && uUserList.size() > 0) {
			uUserList = baseDAO.find("from UUser where phone=:phone and pwd=:pwd",hashMap);
			if (null != uUserList) {
				//存在当前用户
				if (uUserList.size() > 0 && uUserList.size() < 2) {
					this.updateUserVerson(map,uUserList.get(0));
					//存在当前用户，且只有一个用户，更新设备信息
					if (publicService.StringUtil(map.get("code"))) {
//						this.updateUserVerson(map);
						this.updateUserEquipment(map);
						uEquipmentService.insertEquipmentUser(uUserList.get(0),map);
						updateToken(uUserList.get(0), map);
						resultMap.put("uUser", uUserList.get(0));
					}else{
						updateToken(uUserList.get(0), map);
						resultMap.put("uUser", uUserList.get(0));
					}
				}else if(uUserList.size() > 1){
					return WebPublicMehod.returnRet("error", "你的账户存在异常\n请拨打400-805-1719 联系客服小激");
				}else{
					return WebPublicMehod.returnRet("error", "你输入的手机号码或密码错误\n请重新输入");
				}
			}else{
				return WebPublicMehod.returnRet("error", "你输入的手机号码或密码错误\n请重新输入");
			}
		}else{
			return WebPublicMehod.returnRet("error", "你输入的帐号不存在");
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 更新登录人的版本号
	   @param map
	   @param uUser
	   @throws Exception
	   2016年9月5日
	   dengqiuru
	 */
	private void updateUserVerson(HashMap<String, String> map, UUser uUser) throws Exception {
		if (null != uUser) {
			if (publicService.StringUtil(map.get("appCode"))) {
				int intAppCode=WebPublicMehod.intAppCode(map.get("appCode"));//int值的版本号
				uUser.setVersion(intAppCode+"");
				baseDAO.save(uUser);
			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 当前用户用的设备号，清除其他用户同样设备信息
	   @param map
	   		code			设备号
	   @return
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	public void updateUserEquipment(HashMap<String, String> map) throws Exception {
		//根据设备号获取设备号Id
		String equipmentId = uEquipmentService.getEquipmentIdByCode(map);
		if (publicService.StringUtil(equipmentId)) {
			map.put("equipmentId", equipmentId);
			//获取当前设备关联的用户
			List<UUser> uUserList = baseDAO.find(map, "from UUser where numberid=:equipmentId");
			if (null != uUserList && uUserList.size() > 0) {
				//存在，则清空用户的设备信息
				for (UUser uUser : uUserList) {
					uUser.setNumberid(null);
					baseDAO.update(uUser);
					//更新缓存
					this.updateRedisUuserinfo(uUser);
				}
			}
		}
		
	}

	/**
	 * 
	 * 
	   TODO - 登录后更新Token
	   @param uUser
	   		uUser 对象
	   2016年1月4日
	   dengqiuru
	 * @throws Exception 
	 */
	private void updateUuserToken(UUser uUser,HashMap<String, String> map) throws Exception {
		if (null != uUser) {//删除缓存这个用户的token
			if (publicService.StringUtil(uUser.getToken())) {
				redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"),uUser.getToken());					
				redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token","Version"),uUser.getToken());// 登录时token不一样删除缓存里的版本号
			}
			redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"),map.get("token"));// 登录时token不一样删除缓存里的token
			baseDAO.getSessionFactory().getCurrentSession().flush();
			uUser.setToken(map.get("token"));
			baseDAO.update(uUser);
			redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"),uUser.getUserId());
			redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"),map.get("token"), uUser.getUserId());//新建token （key,userId）
			redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"), uUser.getUserId(), SerializeUtil.serialize(uUser));
			redisDao.expire(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"), COM_ORG_FinalArgs.REDIS_CACHE_TIME);//给token设置时间
		}
	}	


	/**
	 * 
	 	TODO - 个人信息
	   @param map
	   		loginUserId		当前用户Id
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUserInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();//输出
		if (publicService.StringUtil(map.get("loginUserId"))) {
			UUser uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			if (null != uUser) {
				map.put("userId", uUser.getUserId());
				Set<UUserImg> uUserImg = uUserImgService.getHeadPicByuserId(map);//获取每个球员的头像
				uUser.setUUserImg(uUserImg);//先将头像set到user对象中
				if (null != uUser.getuRegion()) {//如果区域不为空，就在整体set进去
					setURegion(uUser,map);
				}
				resultMap.put("uUser", uUser);
			}else{
				return WebPublicMehod.returnRet("error", "请先注册！");
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 将区域set到用户中
	   @param uUser
	   		uUser对象
	   @param map
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private void setURegion(UUser uUser,HashMap<String, String> map) throws Exception {
		//将区域的Idput到map里面
		map.put("keyId", uUser.getuRegion().get_id());
		//根据keyId查询相对应的区域
		Set<URegion> uRegions = uRegionService.getURegionSet(map);
		uUser.setuRegionSet(uRegions);
	}

	/**
	 * 
	 * 
	   TODO - 修改个人信息
	   @param map
	   		loginUserId		当前用户Id
	   		phone			手机号
	   		nickname		姓名
	   		userId			登录人的id
	   		area			区域Id
	   		sex				性别
	   		remark			简介
	   		email			邮箱
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> editUserInfo(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();//输出
		UUser uUser = new UUser();
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			if (null != uUser) {
				// 修改用户表
				this.updateUserinfo(uUser,map);
				//更新百度LBS
				this.setAreaToBaiduLBS(uUser);
				//更新缓存
				this.updateRedisUuserinfo(uUser);
//				publicService.removeHRedis(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object",uUser.getUserId()));
			}else{
				return WebPublicMehod.returnRet("error", "不存在该用户！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请使用正常用户执行此操作");
		}
		resultMap.put("uUser", uUser);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 修改用户信息
	   @param uUser
	   		uUser对象
	   @param map
	   		nickname		昵称
	   		remark			简介
	   		phone			手机号
	   		phonecode		更换手机号时的验证码
	   		email			邮箱
	   		area			区域，第三级Id
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private HashMap<String, Object> updateUserinfo(UUser uUser, HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();//输出
		if (publicService.StringUtil(map.get("nickname"))) {
			uUser.setNickname(map.get("nickname"));//昵称
		}
		if (publicService.StringUtil(map.get("remark"))) {
			uUser.setRemark(map.get("remark"));//简介
		}else if("null".equals(map.get("remark"))){
			uUser.setRemark(null);//简介
		}else{
			uUser.setRemark(uUser.getRemark());//简介
		}
		if (publicService.StringUtil(map.get("phone"))) {
			//获取前端用户信息
			if (publicService.validateMoblie(map.get("phone"))) {
				UUser uUserTemp = baseDAO.get(map, "from UUser where phone=:phone");
				if (null == uUserTemp) {
					if (publicService.StringUtil(uUser.getIdcode())) {
						if (map.get("phonecode").equals(uUser.getIdcode())) {//验证码输入正确
							Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
							if (createCodeT >= 0 && createCodeT <= 2) {//在获取验证码2分钟内注册
								uUser.setPhone(map.get("phone"));//手机号
							}else{
								return WebPublicMehod.returnRet("error", "验证码已过期\n请重新获取验证码");
							}
						}else{
							return WebPublicMehod.returnRet("error", "你输入的验证码有误");
						}
					}else{
						return WebPublicMehod.returnRet("error", "你输入的验证码有误");
					}
				}else{
					return WebPublicMehod.returnRet("error", "你输入的手机号已经存在");
				}
			}else{
				return WebPublicMehod.returnRet("error", "手机号格式有误");
			}
		}
		if (publicService.StringUtil(map.get("email"))) {
			uUser.setEmail(map.get("email"));//邮箱
		}
		if (publicService.StringUtil(map.get("area"))) {
			URegion uRegion = uRegionService.getURegionInfo(map);
			uUser.setuRegion(uRegion);//区域
		}
		baseDAO.update(uUser);
		resultMap.put("uUser", uUser);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 修改密码
	   @param map
	   		phone				手机号码
	   		oldloginpassword	原始密码
	   		newloginpassword	新密码
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2015年12月15日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> updatePasswordMethod(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		//获取前端用户信息
		if (publicService.StringUtil(map.get("phone"))) {
			if (publicService.validateMoblie(map.get("phone"))) {
				hashMap.put("phone", map.get("phone"));
				hashMap.put("oldloginpassword",CipherHelper.md5(map.get("oldloginpassword")));
				//根据手机号和原始密码查询用户是否存在
				UUser uUser = baseDAO.get("from UUser where phone=:phone and pwd=:oldloginpassword ",hashMap);
				if (null != uUser) {
					//用户存在，做修改密码操作
					if (publicService.StringUtil(map.get("newloginpassword"))) {
						//判断原始密码和新密码是否一致，一致则不能修改
						if (!map.get("oldloginpassword").equals(map.get("newloginpassword"))) {
							uUser.setPwd(CipherHelper.md5(map.get("newloginpassword")));
							baseDAO.update(uUser);
							//更新缓存
							this.updateRedisUuserinfo(uUser);
						}else{
							return WebPublicMehod.returnRet("error", "原始密码和新密码不能一致！");
						}
					}else{
						return WebPublicMehod.returnRet("error", "新密码密码不能为空！");
					}
				}else{
					return WebPublicMehod.returnRet("error", "你输入的手机号不存在，请重新输入");
				}
				resultMap.put("uUser", uUser);
			}else{
				return WebPublicMehod.returnRet("error", "手机号格式有误");
			}
		}else{
			return WebPublicMehod.returnRet("error", "请输入手机号");
		}
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 第三方登录
	   @param map
	   		source		来源：1=微信；2=微博；3=QQ
	   		logintoken	第三方登录userId
	   		code		设备号
	   		phoneType	设备类型
	   		ip			
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2015年12月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> loginAuthMethod(HashMap<String, String> map) throws Exception {
		HashMap<String,Object> resultMap = new HashMap<String, Object>();//输出
		UUser uUser = null;
		if ("1".equals(map.get("source"))) {//微信
			uUser = baseDAO.get(map, "from UUser where wechatlogintoken = :logintoken");
		}else if ("2".equals(map.get("source"))) {//微博
			uUser = baseDAO.get(map, "from UUser where weibologintoken = :logintoken");
		}else if ("3".equals(map.get("source"))) {//QQ
			uUser = baseDAO.get(map, "from UUser where qqlogintoken = :logintoken");
		}
		// 设备绑定用户表
		if (null != uUser) {
			//查询该设备号在用户中是否已经存在  如果存在，就把其他用户的设备号设置为null
			if (publicService.StringUtil(map.get("code"))) {
				this.updateUserEquipment(map);
			}
			//更新token
			updateToken(uUser, map);
		}
		resultMap.put("uUser", uUser);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 第三方登录关联【该功能再无联调】
	   @param map
	   		source			来源：1=微信；2=微博；3=QQ
	   		logintoken		第三方登录userId
	   		phone			手机号
	   		loginpassword	密码
	   		code
	   		phoneType
	   		ip
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2015年12月21日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> loginAuthTiedMethod(HashMap<String, String> map,UUser uUser) throws Exception {
		HashMap<String,Object> 	resultMap = new HashMap<String, Object>();//输出
		//不能判断uUser对象，获取验证码对象已经生成数据了
		//只能根据uUser密码进行验证
		//判断输入的验证码是否正确
		if (uUser.getIdcode().equals(map.get("phonecode"))) {
			Long createCodeT = (System.currentTimeMillis() - uUser.getCodetime().getTime()) / (1000 * 60);
			//首先判断验证码是否保持在两分钟之内
			if (createCodeT <= 2) {
				//判断用户密码是是否为空
				String regiType = "1";//该状态代表注册来源，1：代表第三
				if (publicService.StringUtil(uUser.getPwd())) {//不为空，说明已经注册过
					updateUuserinfoByRegi(uUser, map, regiType);
					uUser.setRegiStatus("2");//设置用户注册状态 1：注册接口   未走关联直接注册 2：关联接口  没有关联已经注册 3：关联接口   没有关联没有注册
				}else{//为空，说明还没有注册过
					//判断密码是否传参过来，如果没有就提示请输入密码
					if (publicService.StringUtil(map.get("loginpassword"))) {
						updateUuserinfoByRegi(uUser, map, regiType);
						uUser.setRegiStatus("3");//设置用户注册状态 1：注册接口   未走关联直接注册 2：关联接口  没有关联已经注册 3：关联接口   没有关联没有注册
					}else{
						return WebPublicMehod.returnRet("error", "请输入密码\n以确保你的账号安全！");
					}
				}
			}else{
				return WebPublicMehod.returnRet("error", "验证码已过期\n请重新获取验证码");
			}
		}else{
			return WebPublicMehod.returnRet("error", "你输入的验证码有误");
		}
		if (publicService.StringUtil(map.get("code"))) {
			this.updateUserEquipment(map);
			uEquipmentService.insertEquipmentUser(uUser,map);
		}
		String regiResource = "2";//第三方
		uUserLogService.saveUserLog(uUser,regiResource);//操作记录
		//登录后更新Token
		updateUuserToken(uUser,map);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 用户头部信息
	   @param map
	   		loginUserId  当前用户Id
	   @return
	   		uUser的resultMap
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> uuserHeadinfo(HashMap<String, String> map) throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		HashMap<String, Object> hashMap = new HashMap<>();
		List<HashMap<String, Object>> uUser=new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> list=new ArrayList<>();
		resultMap.put("worthList",list);
		//将loginUserIdput带hashMap中
		if (publicService.StringUtil(map.get("loginUserId"))) {
			hashMap.put("userId", map.get("loginUserId"));
			String sql = "select u.user_id userId,u.realname realname,u.nickname nickname,u.username username,i.imgurl imgurl from u_user u "
					+" left join u_user_img i on i.user_id=u.user_id and i.uimg_using_type='1' and i.img_size_type='1'"
					+" where u.user_id=:userId";
			uUser = baseDAO.findSQLMap(sql,hashMap);
			if (uUser != null && uUser.size() > 0) {
				for (HashMap<String, Object> hashMap2 : uUser) {
					//根据这个用户查询这个用户是否存在队长信息，如果存在带出球队信息
					this.displayData(hashMap2,map);
				}
			}
			//保存入驻UPBOX平台身价信息
			map.put("taskBehavior", "1");//1、入驻UPBOX平台 2、成为UPBOX球员 3、首次建立球队 4、首次加入球队
			resultMap.putAll(uworthService.saveTaskInfo(map));
		}
		resultMap.put("uUser", uUser);
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 未读通知 【2.0.0】
	   @param map
	   		loginUserId    当前用户Id
	   @return
	   		playerRed                           --球员用户信息是否为空状态  1:有 2:无
		    messageRed                           --用户未读通知状态  1:有 2:无
		    isNoRead                           --用户未读通知状态/或者未填写的信息  1:有 2:无 3：第三方登录
		    playerinfoRed                           --用户球员信息有无为空    1:有 2:无
		    userinfoRed                           --用户地区有无为空    1:有 2:无
		    topicRed							--话题红点 1：有，2-无
		    worthRed							--身价红点 1：有，2-无
	   @throws Exception
	   2016年5月4日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setIsRed(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String isNoRead = "2";//用户未读通知状态/或者未填写的信息  1:有 2:无 3：第三方登录
		String playerRed = "2";//球员用户信息是否为空状态  1:有 2:无
		String messageRed = "2";//用户未读通知状态  1:有 2:无
		String playerinfoRed = "2";//用户球员信息有无为空    1:有 2:无
		String userinfoRed = "2";//用户地区有无为空    1:有 2:无
		String topicRed = "2";//话题红点    1:有 2:无
		String worthRed = "2";//身价红点    1:有 2:无
		if (publicService.StringUtil(map.get("loginUserId"))){
			//球员信息是否为空
			UPlayer uPlayer = uplayerService.getUplayerByUserIdInJoinTeam(map);
			if (null != uPlayer) {
				if (publicService.isNullOrEmpty(uPlayer.getUUser().getRealname())) {//真实姓名
					isNoRead = "1";
					playerRed = "1";
					playerinfoRed = "1";
				}else if (publicService.isNullOrEmpty(uPlayer.getUUser().getBirthday())) {//生日
					isNoRead = "1";
					playerRed = "1";
					playerinfoRed = "1";
				}else if (publicService.isNullOrEmpty(uPlayer.getUUser().getHeight())) {//身高
					isNoRead = "1";
					playerRed = "1";
					playerinfoRed = "1";
				}else if (publicService.isNullOrEmpty(uPlayer.getUUser().getWeight())) {//体重
					isNoRead = "1";
					playerRed = "1";
					playerinfoRed = "1";
				}else if (publicService.isNullOrEmpty(uPlayer.getUUser().getuRegion())) {//地区
					isNoRead = "1";
					playerRed = "1";
					userinfoRed = "1";
				}
			}else {
				UUser uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
				if (null != uUser) {
					if (publicService.isNullOrEmpty(uUser.getuRegion())) {
						userinfoRed = "1";
					}
				}else{
					userinfoRed = "1";
				}
				isNoRead = "1";
				playerRed = "1";
				playerinfoRed = "1";
			}
			//消息通知信息是否未读
			HashMap<String, Object> hashMap = messageService.getUnreadCount(map);
			if (null != hashMap) {
				if (null != hashMap.get("count")) {
					if (Integer.parseInt(hashMap.get("count").toString()) > 0) {
						isNoRead = "1";
						messageRed = "1";
					}
				}
			}
			//身价
			HashMap<String, Object>  worthMap=uworthService.findUserIdTaskInfo(map);
			if (null != worthMap) {
				if (null != worthMap.get("isShow")) {
					if ("2".equals(worthMap.get("isShow").toString())) {
						isNoRead = "1";
						worthRed = "1";//身价红点    1:有 2:无
					}
				}
			}
			//话题
			Boolean isTopic = topicService.isTopicUnreadComment(map.get("loginUserId"));
			if (true == isTopic) {
				topicRed = "1";
				isNoRead = "1";
			}
		}else{
			isNoRead = "3";//游客登录
		}
		resultMap.put("playerinfoRed", playerinfoRed);
		resultMap.put("userinfoRed", userinfoRed);
		resultMap.put("isNoRead", isNoRead);
		resultMap.put("playerRed", playerRed);
		resultMap.put("messageRed", messageRed);
		resultMap.put("topicRed", topicRed);
		resultMap.put("worthRed", worthRed);
		return resultMap;
	}


	/**
	 * 
	 * 
	   TODO - 填充这个用户的队长信息
	   @param hashMap2
	   		userId  用户Id 
	   @param map
	   @throws Exception
	   2016年5月30日
	   dengqiuru
	 */
	private void displayData(HashMap<String, Object> hashMap2,HashMap<String, String> map) throws Exception {
		String teamId = null;
		String isTeamLeader = "3";//3：没有球队的球员
		String teamName = null;
		if (null != hashMap2) {
			if (null != hashMap2.get("userId")) {
				//根据用户查询该用户是否存在队长信息
				teamId = uplayerService.getUteamIdByuserId(hashMap2);
				if (publicService.StringUtil(teamId)) {
					isTeamLeader = "1";//为队长
					map.put("teamId", teamId);
					UTeam uTeam = baseDAO.get(map,"from UTeam where teamId=:teamId");
					if (null != uTeam) {
						//球队为正常状态
						if ("2".equals(uTeam.getTeamUseStatus())) {
							//查出球队名和球队Id
							teamId = uTeam.getTeamId();
							teamName = uTeam.getName();
						}
					}
				}else{//不位队长，则查询为球员的球队Id和名称
					List<UPlayer> uPlayerList = uplayerService.getUPlayerIsNotTeamLeader(map);
					if (null != uPlayerList && uPlayerList.size() > 0) {
						isTeamLeader = "2";
						if (null != uPlayerList.get(0).getUTeam()) {
							if ("2".equals(uPlayerList.get(0).getUTeam().getTeamUseStatus())) {
								teamId = uPlayerList.get(0).getUTeam().getTeamId();
								teamName = uPlayerList.get(0).getUTeam().getName();
							}
						}
					}
				}
			}
		}
		//填充球队信息
		hashMap2.put("teamId", teamId);
		hashMap2.put("teamName", teamName);
		hashMap2.put("isTeamLeader", isTeamLeader);
		
	}

	/**
	 * 
	 * 
	   TODO - 订单中，预定人手机号为新号，库内无数据，那么新增此人信息 【2.0.0】
	   @param map
	   		phone		手机号码
	   		realName	真实姓名
	   @return
	   		uUser 对象
	   @throws Exception
	   2016年1月26日
	   dengqiuru
	 */
	@Override
	public UUser insertOrderNewUser(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();//参数集合
		hashMap.put("phone", map.get("phone"));
		UUser uUser = baseDAO.get("from UUser where phone=:phone", hashMap);
		if (null == uUser) { // 如果预定人手机号为新号，库内无数据，那么新增此人信息
			uUser = new UUser();
			uUser.setUserId(WebPublicMehod.getUUID());
			uUser.setPhone(map.get("phone"));
			uUser.setPwd(CipherHelper.md5("123456"));
			uUser.setRealname(map.get("realName"));
			uUser.setUserStatus("2");
			uUser.setUserType("1");
			baseDAO.save(uUser);
		} else {
			uUser.setRealname(map.get("realName"));
			baseDAO.update(uUser);
			//更新后删除缓存
			this.updateRedisUuserinfo(uUser);
		}
		//添加初始球员信息
		uplayerService.createUplayer(map, uUser, null);
		return uUser;
	}

	/**
	 * 每个列表页面头部信息
	 */
	@Override
	public UUser getUserinfoByUserId(HashMap<String, String> map) throws Exception { 
		//查询用户信息
		UUser uUser = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
		}
		return uUser;
	}

	/**
	 * TODO - 通过userId 获取用户对象
	 * @param map userId 用户Id
	 * @return
	 * @throws Exception
	 */
	@Override
	public UUser getUserByUserId(HashMap<String, String> map) throws Exception {
		//查询用户信息
		UUser uUser = null;
		if (publicService.StringUtil(map.get("userId"))) {
			uUser = baseDAO.getHRedis(UUser.class,map.get("userId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
		}
		return uUser;
	}
	/**
	 * 每个列表页面头部信息,并更新用户token
	 */
	@Override
	public String getUserinfoByUserIdCreatetoken(HashMap<String, String> map) throws Exception {
		//查询用户信息
		UUser uUser = null;
		String token = null;
		if (publicService.StringUtil(map.get("loginUserId"))) {
			uUser = baseDAO.getHRedis(UUser.class,map.get("loginUserId"),PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			if (null != uUser) {
				updateToken(uUser, map);
				token = uUser.getToken();
			}
		}
		return token;
	}

	/**
	 * 
	 * 
	   TODO - 编辑球员信息时更新个人信息
	   @param map	loginUserId  个人ID
	   @param uUser		身高、体重
	   		birthday		生日
	   		userType		用户类型
	   		imgSizeType		图片尺寸类型
	   		imgurl			图片显示地址
	   		weight			图片权重
	   		saveurl 		图片存储地址
	   @throws Exception
	   2015年12月16日
	   dengqiuru
	 */
	@Override
	public void inPlayerUpdateUUser(HashMap<String, String> map, UUser uUser) throws Exception {
		//头像
		if (publicService.StringUtil(map.get("imgurl"))) {
			uUserImgService.uploadHeadPic(map);
		}
		if (publicService.StringUtil(map.get("realname"))) {
			uUser.setRealname(map.get("realname"));//真实姓名
		}
		if (publicService.StringUtil(map.get("nickname"))) {
			uUser.setNickname(map.get("nickname"));//真实姓名
		}
		if (publicService.StringUtil(map.get("sex"))) {
			uUser.setSex(map.get("sex"));//性别
		}
		if (publicService.StringUtil(map.get("userType"))) {
			uUser.setUserType(map.get("userType"));
		}
		if (publicService.StringUtil(map.get("height"))) {
			uUser.setHeight(map.get("height"));
			//体重 身高 年龄修改后再计算球队平均值
		}
		if (publicService.StringUtil(map.get("weight"))) {
			uUser.setWeight(map.get("weight"));
			//体重 身高 年龄修改后再计算球队平均值
		}
		if (publicService.StringUtil(map.get("birthday"))) {
			SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
			Date birthday = sim.parse(map.get("birthday"));
			uUser.setBirthday(birthday);
			uUser.setAge(getAgeByBirthday(birthday));
		}if (publicService.StringUtil(uUser.getUserStatus())) {//状态-从待关联到正常  1：正常
			if ("2".equals(uUser.getUserStatus())) {
				uUser.setUserStatus("1");
			}
		}
		baseDAO.getSessionFactory().getCurrentSession().flush();
		baseDAO.getSessionFactory().getCurrentSession().clear();
		baseDAO.update(uUser);
		//更新年龄lbs数据
		this.setAreaToBaiduLBS(uUser);
		//更新缓存
		this.updateRedisUuserinfo(uUser);
		//修改年龄、身高、体重之后、更新球队平均体重
		uplayerService.updateTeamAvg(uUser);
	}
	@Override
	public void updateUserInfo(HashMap<String, String> map, UUser uUser) throws Exception {
		
		if (publicService.StringUtil(map.get("realname"))) {
			uUser.setRealname(map.get("realname"));//真实姓名
		}
		if (publicService.StringUtil(map.get("nickname"))) {
			uUser.setNickname(map.get("nickname"));//真实姓名
		}
		if (publicService.StringUtil(map.get("height"))) {
			uUser.setHeight(map.get("height"));
			//体重 身高 年龄修改后再计算球队平均值
		}
		if (publicService.StringUtil(map.get("weight"))) {
			uUser.setWeight(map.get("weight"));
			//体重 身高 年龄修改后再计算球队平均值
		}
		if (publicService.StringUtil(map.get("birthday"))) {
			SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
			Date birthday = sim.parse(map.get("birthday"));
			uUser.setBirthday(birthday);
			uUser.setAge(getAgeByBirthday(birthday));
		}
		if (publicService.StringUtil(uUser.getUserStatus())) {//状态-从待关联到正常  1：正常
			if ("2".equals(uUser.getUserStatus())) {
				uUser.setUserStatus("1");
			}
		}
		baseDAO.getSessionFactory().getCurrentSession().flush();
		baseDAO.getSessionFactory().getCurrentSession().clear();
		baseDAO.update(uUser);
		//更新年龄lbs数据
		this.setAreaToBaiduLBS(uUser);
		//更新缓存
		this.updateRedisUuserinfo(uUser);
		//修改年龄、身高、体重之后、更新球队平均体重
		uplayerService.updateTeamAvg(uUser);
	}

	/**
	 * 
	 * 
	   TODO - 根据出生日期计算年龄
	   @param birthday	出生日期
	   @return
	   2015年12月16日
	   dengqiuru
	 */
	public Integer getAgeByBirthday(Date birthday) {
		Integer age;
		if (null == birthday) {
			age = null;
		}else{
			Calendar cal = Calendar.getInstance();

			if (cal.before(birthday)) {
				throw new IllegalArgumentException(
						"The birthDay is before Now.It's unbelievable!");
			}

			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

			cal.setTime(birthday);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

			age = yearNow - yearBirth;

			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					// monthNow==monthBirth 
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					// monthNow>monthBirth 
					age--;
				}
			}
		}
		
		return age;
	}

	/**
	 * 
	 * 
	   TODO - 邀请球员时，用户列表 【2.0.0】
	   @param map
	   		loginUserId    用户Id
	   @return
	   		uUserlist 的hashMap<String,Object>
	   @throws Exception
	   2016年3月1日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> getUserlistOfInvite(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		List<UUser> uUserlist = baseDAO.find(map, "from UUser where userId <> :loginUserId and user_status='1' and user_type='1' order by createdate desc");
		resultMap.put("uUserlist", uUserlist);
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 根据token获取uuser对象 【2.0.0】
	   @param map
	   		token
	   @return
	   		UUser 对象
	   @throws Exception
	   2016年3月2日
	   dengqiuru
	 */
	@Override
	public UUser getUserinfoByToken(HashMap<String, String> map) throws Exception {
		UUser uUser = new UUser();
		if (publicService.StringUtil(map.get("token"))) {
			//根据token查询userId
			String userId = String.valueOf( redisDao.getHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"),map.get("token")));
			if (publicService.StringUtil(userId)) {
				//userId不为空，根据userId查询用户对象
				uUser = baseDAO.getHRedis(UUser.class,userId,PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"));
			}else{
				//为空，根据token查询数据库里的用户对象
				uUser = baseDAO.get(map, "from UUser where token=:token");
				if (null != uUser) {
					redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"),map.get("token"), uUser.getUserId());//新建token （key,userId）
					redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"), uUser.getUserId(), SerializeUtil.serialize(uUser));
					redisDao.expire(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Token"), COM_ORG_FinalArgs.REDIS_CACHE_TIME);//给token设置时间
					this.updateRedisUuserinfo(uUser);
				}
			}
		}
		return uUser;
	}

	/**
	 * 
	 * 
	   TODO - 建队时，判断队长信息是否填写完整 【2.0.0】
	   @param uUser
	   		uUser 对象
	   @return
	   		String  提示消息
	   2016年3月11日
	   dengqiuru
	 */
	@Override
	public String isPlayerinfoComplete(UUser uUser) throws Exception {
		String result = null;
		if (uUser != null) {
			if (null == uUser.getBirthday()) {
				result = "生日";
			}else if (null == uUser.getWeight() ||  "".equals(uUser.getWeight()) || "null".equals(uUser.getWeight())) {
				result = "体重";
			}else if (null == uUser.getHeight() ||  "".equals(uUser.getHeight()) || "null".equals(uUser.getHeight())) {
				result = "身高";
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	   TODO - 查询别人的用户对象 【2.0.0】
	   @param map
	   		userId		用户Id
	   @return
	   @throws Exception
	   2016年4月18日
	   dengqiuru
	 */
	@Override
	public HashMap<String, String> getUserinfoByOtherUserId(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("userId", map.get("userId"));
		String sql = "select u.nickname nickname,u.realname username,i.imgurl userimg from u_user u "
				+" left join u_user_img i on i.user_id=u.user_id and i.uimg_using_type='1' and i.img_size_type='1'"
				+" where u.user_id=:userId";
		List<HashMap<String, String>> uUser = baseDAO.findSQLMap(sql,hashMap);
		if(uUser!=null&&uUser.size()>0){
			return uUser.get(0);
		}else return null;
	}
	/**
	 * 
	 * 
	   TODO - 将用户对象在缓存删除后在更新到缓存中去 【2.0.0】
	   @param uUser
	   		uUser对象
	   @throws Exception
	   2016年4月27日
	   dengqiuru
	 */
	@Override
	public void updateRedisUuserinfo(UUser uUser) throws Exception{
		if (null != uUser) {
			//先根据这个用户的id  删除缓存中该用户的信息
			redisDao.delHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"),uUser.getUserId());
			//然后再讲用户最新信息插入到缓存
			redisDao.setHSet(PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME,"Object","Uuser"),uUser.getUserId(), SerializeUtil.serialize(uUser));
		}
	}

	/**
	 * 
	 * @TODO 新增用户百度云LBS数据
	 * @Title: insUserBaiduLBSData 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年6月29日 下午2:10:48
	 */
	@Override
	public void insUserBaiduLBSData(HashMap<String, String> map)throws Exception{
		if(null!=map.get("address")&&null!=map.get("userId")){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("address", map.get("address"));
			hashMap.put("url",Public_Cache.LBS_LOCATION);
			
//			try {
				//获得经纬度
				String lbsApiStr = lbsService.addressToApi(hashMap);
				JSONObject json = new JSONObject(lbsApiStr);
				json.getString("result");
				
				Document document = DocumentHelper.parseText(json.getString("result"));
				Element element = document.getRootElement();
				Element result = element.element("result");
				Element location = result.element("location");
				
				// 创建位置数据表数据
				hashMap.put("title","用户数据");
				hashMap.put("tags","user");
				hashMap.put("coord_type",3);
				hashMap.put("geotable_id",Constant.BAIDU_TABLE_ID);
				hashMap.put("latitude", Double.parseDouble(location.elementText("lat")));
				hashMap.put("longitude", Double.parseDouble(location.elementText("lng")));
				
				hashMap.put("object_id",map.get("userId"));
				hashMap.put("date",new Date());
				map.put("id", map.get("userId"));
				hashMap.put("user_intid",publicService.getIntKeyId(1, map));
				hashMap.put("params_type","1");
				
				//获取位置数据表数据 id
				String lbsDataStr = lbsService.createGeodata(hashMap);
				json = new JSONObject(lbsDataStr);
				json.getString("result");
				json = new JSONObject(json.getString("result"));
				
				//记录入库 [百度对照表]
				hashMap.put("objectId", map.get("userId"));
				hashMap.put("id",json.getInt("id"));
				hashMap.put("geotableId", Constant.BAIDU_TABLE_ID);
//				lbsService.createUbaidulbsData(hashMap);

//				Map<String,Object> retMap = PublicMethod.parseJSON2Map(map.get("result").toString());
//				String	bdLbsId=(String) retMap.get("id");
//				lbsService.createUbaidulbsData(map.get("userId"), "1", bdLbsId);
				lbsService.createUbaidulbsData(map.get("userId"),"1",json.getString("id"));
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
			
		}
	}

	/**
	 * 
	 * @TODO 修改用户百度云LBS数据
	 * @Title: updUserBaiduLBSData 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年6月29日 下午2:10:51
	 */
	@Override
	public void updUserBaiduLBSData(HashMap<String, String> map)throws Exception{
		if(null!=map.get("address")&&null!=map.get("userId")){
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("address", map.get("address"));
			hashMap.put("url",Public_Cache.LBS_LOCATION);
			
//			try {
				//获得经纬度
				String lbsApiStr = lbsService.addressToApi(hashMap);
				JSONObject json = new JSONObject(lbsApiStr);
				json.getString("result");
				
				Document document = DocumentHelper.parseText(json.getString("result"));
				Element element = document.getRootElement();
				Element result = element.element("result");
				Element location = result.element("location");
				
				//获得用户对应百度LBS数据表ID
				UBaidulbs uBaidulbs = null;
				uBaidulbs = baseDAO.get(map,"from UBaidulbs where objectId = :userId ");
				if(null!=uBaidulbs){
					// 修改位置数据表数据
					hashMap.put("id",uBaidulbs.getLbsData());
					hashMap.put("title","用户数据");
					hashMap.put("tags","user");
					hashMap.put("coord_type",3);
					hashMap.put("geotable_id",Constant.BAIDU_TABLE_ID);
					hashMap.put("latitude", Double.parseDouble(location.elementText("lat")));
					hashMap.put("longitude", Double.parseDouble(location.elementText("lng")));
					
					hashMap.put("object_id",map.get("userId"));
					hashMap.put("date",new Date());
					map.put("id", map.get("userId"));
					hashMap.put("user_intid",publicService.getIntKeyId(1, map));
					hashMap.put("params_type","1");
					
					//获取位置数据表数据 id
					lbsService.updateGeodata(hashMap);
//					String lbsUpdStr = lbsService.updateGeodata(hashMap);
//					System.out.println("updateGeodata="+lbsUpdStr);
//					json = new JSONObject(lbsUpdStr);
//					json.getString("result");
//					System.out.println("result="+result);
//					json = new JSONObject(json.getString("result"));
				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
		}
	}

//	@Override
//	public HashMap<String, Object> getBaidulbs(HashMap<String, String> map) throws Exception {
//		HashMap<String, Object> hashMap = new HashMap<>();
//		HttpUtil http = new HttpUtil();
//		List<UUser> uUserList = baseDAO.find(map, "from UUser where userId=:userId");
//		if (null != uUserList && uUserList.size() > 0) {
//			for (UUser uUser : uUserList) {
//				if (null != uUser.getuRegion()) {
//					OutUbCourtMap ubCourtMap = this.setUbCourtMap1(uUser.getuRegion(), map);
//					hashMap.put("output", "json");
//					hashMap.put("ak", "E4805d16520de693a3fe707cdc962045");
//					hashMap.put("callback", null);
//					hashMap.put("address", ubCourtMap.getNameStr());
//					hashMap.put("city", null);
//					HttpRespons response = http.sendPost("http://campaign.upbox.com.cn:8800/Position/baidulbs_addressToApi.do", hashMap);
//					System.out.println(response.getContent());
//				}
//			}
//		}
//		return null;
//	}
	
	
//	/**
//	 * 
//	 * 
//	   TODO - 球队概况 --球场轴--区域
//	   @param getuRegion  区域对象
//	   @param map
//	   		keyId		区域主键Id
//	   @param uTeam			对象
//	   @return
//	   @throws Exception
//	   2016年6月1日
//	   dengqiuru
//	 */
//	private OutUbCourtMap setUbCourtMap1(URegion getuRegion,HashMap<String, String> map) throws Exception {
//		OutUbCourtMap ubCourtMap1 = new OutUbCourtMap();
//		map.put("keyId", getuRegion.get_id());
//		ubCourtMap1.setIdStr("区域");
//		Set<URegion> uRegions = uRegionService.getURegionSet(map);
//		String province = null;
//		String city = null;
//		String county = null;
//		for (URegion uRegion : uRegions) {
//			if ("1".equals(uRegion.getType())) {
//				province = uRegion.getName();
//			}
//			if ("2".equals(uRegion.getType())) {
//				city = uRegion.getName();
//			}
//			if ("3".equals(uRegion.getType())) {
//				county = uRegion.getName();
//			}
//		}
//		if ("市".equals(province.substring(province.length()-1, province.length()))) {
//			ubCourtMap1.setNameStr(province+county);//将省市区拼接
//		}else{
//			ubCourtMap1.setNameStr(province+city+county);//将省市区拼接
//		}
//		return ubCourtMap1;
//	}
	
	/**
	 * 
	 * 
	   TODO - 更新百度LBS
	   @param uTeam
	   @param map
	 * 	userId 用户主键
	 * 	address 地区
	 *  user_id_int 用户数值类型ID
	   @throws Exception
	   2016年7月2日
	   dengqiuru
	 */
	@Override
	public void setAreaToBaiduLBS(UUser uUser) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<>();
		if (null != uUser) {
			if (null != uUser.getuRegion()) {
				hashMap.put("area", uUser.getuRegion().get_id());
				String address = uRegionService.getURegionInfoByArea(hashMap);
				hashMap.put("address", address);
				hashMap.put("userId", uUser.getUserId());
				hashMap.put("user_id_int", uUser.getUser_id_int()+"");
				this.saveOrUpdateUserLbs(uUser,hashMap);
			}
//			if (null != uUser.getuRegion()) {
//				this.saveOrUpdateUserLbs(uUser,map);
//			}else{
//				this.saveOrUpdateUserLbs(uUser,map);
//			}
		}
	}

	/**
	 * 
	 * 
	   TODO - 更新或新增球员lbs
	   @param uUser
	   @param map
	   		address			地址
	   @throws Exception
	   2016年7月22日
	   dengqiuru
	 */
	private void saveOrUpdateUserLbs(UUser uUser, HashMap<String, Object> map) throws Exception {
		Map<String, Object> lbsMap = null;
		//根据区域获取路径
		if (null != uUser) {
			//用户Id
			String userId = uUser.getUserId();
			//定义一个字段，判断新增，没有区域，自定义字段不为空，就不做操作
			String str = "1";
			try {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
//				hashMap.put("url",Public_Cache.LBS_LOCATION);
				//位置集合
				//根据地区获取经纬度
				hashMap.put("address", map.get("address"));
				hashMap.put("url",Public_Cache.LBS_LOCATION);
				hashMap.put("output", "json");//数据的返回格式
				hashMap.put("object_id",map.get("userId"));
				Map<String, Object> positionMap = this.getLocalhost(hashMap);
				if (null == positionMap) {
					//获取经纬度失败
					lbsService.createUbaidulbsDataError((String)map.get("userId"), "2", "APP根据地址:"+map.get("address")+"获取不到经纬度");
				}else{
					int clIntId= this.getEntityIntId("select user_id_int as intId from u_user where user_id = '"+uUser.getUserId()+"'");
					//年龄
					int age = 0;
					if (publicService.isNullOrEmpty(uUser.getAge())) {
						age =  uUser.getAge();
					}
					//擅长位置、加队数
					HashMap<String, String> playerMap = uplayerService.getLBSUplayerinfo(userId);
					//查询LBS是否存在当前数据
					UBaidulbs baidulbs=lbsService.getBaidulbs("1",userId);
					//区域Id
					String areaid = uUser.getuRegion().get_id();
					if (null != baidulbs) {//存在，修改
						//上传LBS参数
						HashMap<String,Object> data=this.setUserLbsMap(areaid,age,playerMap,map,positionMap, clIntId, userId, "用户数据");
						data.put("id", baidulbs.getLbsData());
						lbsMap = PublicMethod.parseJSON2Map(lbsService.updateGeodata(data));
					}else{//不存在  新增
						str = "2";
						//获取位置数据表数据 id
						lbsMap = PublicMethod.parseJSON2Map(lbsService.createGeodata(this.setUserLbsMap(areaid,age,playerMap,map,positionMap, clIntId, userId, "用户数据")));
						
					}
					
					this.updateUbaidulbsDataError(lbsMap,userId,str);
					if(lbsMap!=null){
						Map<String,Object> retMap = PublicMethod.parseJSON2Map(lbsMap.get("result").toString());
						if(retMap.get("status")!=null&&"0".equals(retMap.get("status").toString())){//0代表 百度lbs端成功记录
							String bdLbsId=retMap.get("id").toString();
							lbsService.createUbaidulbsData(userId, "1", bdLbsId);
						}else{
							lbsService.createUbaidulbsDataError(userId, "1", "APP更新球员lbs数据异常");
						}
					}else{
						if ("1".equals(str)) {
							lbsService.createUbaidulbsDataError(userId, "1", "APP新增球员lbs数据异常");
						}
					}
				}
			} catch (Exception e) {
				this.updateUbaidulbsDataError(lbsMap, userId, str);
			}
		}
	}
	
	/**
	 * 
	 * 
	   TODO - 添加异常数据lbs到本地日志
	   @param lbsMap
	   @param userId
	   @param str
	   @throws Exception
	   2016年8月1日
	   dengqiuru
	 */
	private void updateUbaidulbsDataError(Map<String, Object> lbsMap, String userId, String str) throws Exception {
		if("1".equals(str)){
			lbsService.createUbaidulbsDataError(userId, "1", "APP更新球员lbs数据异常");
		}else if ("2".equals(str)) {
			lbsService.createUbaidulbsDataError(userId, "1", "APP新增球员lbs数据异常");
		}
	}

	/**
	 * 
	 * TODO 根据地区获取经纬度
	 * @param hashMap output－json数据返回，url－球场数据的地址，address-地址
	 * @return localhostHashMap lat－纬度，lng－经度
	 * @throws Exception
	 * xiaoying 2016年6月30日
	 */
	private HashMap<String, Object> getLocalhost(HashMap<String, Object> hashMap)  throws Exception{
		HashMap<String, Object> hashMap2=new HashMap<>();
		String location=lbsService.addressToApi(hashMap);//经纬度获取 json格式的
		if(!"".equals(location)){
			return lbsService.packLbsDateAddress(location);//处理过的经纬度
		}else{
			//获取经纬度失败
			lbsService.createUbaidulbsDataError((String)hashMap.get("object_id"), "2", "APP根据地址:"+hashMap.get("address")+"获取不到经纬度");
		}
		return hashMap2;
	}
	/**
	 * 
	 * 
	   TODO - lbs设置参数
	   @param age
	   @param playerMap		球员信息集合
	   @param map
	   @param positionMap	经纬度集合
	   @param clIntId
	   @param userId
	   @param title
	   @return
	   @throws Exception
	   2016年7月22日
	   dengqiuru
	 */
	private HashMap<String,Object> setUserLbsMap(String areaid,int age, HashMap<String, String> playerMap, HashMap<String, Object> map,Map<String, Object> positionMap,int clIntId,String userId,String title) throws Exception  {
		HashMap<String,Object> hashMap = new HashMap<String,Object>();
		HashMap<String,String> hashMap1 = new HashMap<String,String>();
		hashMap.put("address",URLEncoder.encode(map.get("address").toString(),"UTF-8"));
		hashMap.put("url",Public_Cache.LBS_LOCATION);
		hashMap.put("title", URLEncoder.encode(title, "UTF-8"));
		hashMap.put("latitude",positionMap.get("lat"));
		hashMap.put("longitude",positionMap.get("lng"));
		hashMap.put("coord_type",3);
		hashMap.put("geotable_id", Constant.BAIDU_PLAYER_TABLE_ID);
		hashMap.put("areaid",areaid);
		hashMap1.put("id", userId);
		hashMap.put("player_intid",publicService.getIntKeyId(1, hashMap1));
		hashMap.put("tags","user");
		hashMap.put("params_type","1");
		hashMap.put("keyid",userId);
		hashMap.put("age",age);
		if (null != playerMap) {
			if (publicService.StringUtil(playerMap.get("expertPosition"))) {
				hashMap.put("position",playerMap.get("expertPosition"));
			}
			if (publicService.StringUtil(playerMap.get("addteam"))) {
				hashMap.put("addteam",playerMap.get("addteam"));
			}
		}
//		hashMap.put("date",PublicMethod.getDateToString(new Date(),"yyyy-MM-dd"));
		return hashMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 获取表中对应的int ID字段,sql中对应 intId
	   @param sql
	   @return
	   @throws Exception
	   2016年7月21日
	   dengqiuru
	 */
	private int getEntityIntId(String sql) throws Exception {
		List<Map<String,Object>> list = this.baseDAO.findSQLMap(sql);
		if(CollectionUtils.isNotEmpty(list)){
			return Integer.parseInt(list.get(0).get("intId").toString());
		}
		return 0;
	}
	/**
	 * 
	 * @TODO 用户登录记录
	 * @Title: insLoginLog 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月4日 下午5:38:06
	 */
	@Override
	public void insLoginLog(HashMap<String, String> map) throws Exception {
		if(null!=map.get("token") && !"".equals(map.get("token"))&&null!=map.get("resource")&& !"".equals(map.get("resource"))&&null!=map.get("appCode")&&!"".equals(map.get("appCode"))){
			HttpUtil http = new HttpUtil();
			UUser user = this.getUserinfoByToken(map);
			map.put("userId", user.getUserId());
			System.out.println(Public_Cache.UPBOX_LOG_URL+"/uuLoginLog_insLoginLog.do");
			HttpRespons response = http.sendPost(Public_Cache.UPBOX_LOG_URL+"/uuLoginLog_insLoginLog.do",map);
			@SuppressWarnings("unused")
			HashMap<String, Object> map1 = (HashMap<String, Object>) PublicMethod.parseJSON2Map(response.getContent());
			
		}
	}

	/**
	 * 
	 * 
	   TODO - 根据手机号查询用户信息,若没有则新增
	   @param map
	   		phone		手机号
	   		realName	真实姓名
	   		nickName	昵称
	   		sex			性别
	   		birthday	出生年月
	   		height		身高
	   		weight		体重
	   		area		区域
	   @return
	   2016年7月8日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public UUser getUserinfoByPhone(HashMap<String, String> map) throws Exception {
		UUser uUser = baseDAO.get(map, "from UUser where phone=:phone");
		if (null != uUser) {
			//真实姓名
			if (publicService.StringUtil(map.get("realName"))) {
				uUser.setRealname(map.get("realName"));
			}
			//昵称
			if (publicService.StringUtil(map.get("nickName"))) {
				uUser.setNickname(map.get("nickName"));
			}
			//性别
			if (publicService.StringUtil(map.get("sex"))) {
				uUser.setSex(map.get("sex"));
			}
			//出生年月
			if (publicService.StringUtil(map.get("birthday"))) {
				Date birthday = PublicMethod.getStringToDate(map.get("birthday"), "yyyy-MM-dd");
				uUser.setBirthday(birthday);//出生日期
				uUser.setAge(getAgeByBirthday(birthday));//年龄
			}
			//身高
			if (publicService.StringUtil(map.get("height"))) {
				uUser.setHeight(map.get("height"));
			}
			//体重
			if (publicService.StringUtil(map.get("weight"))) {
				uUser.setWeight(map.get("weight"));
			}
			//区域
			if (publicService.StringUtil(map.get("area"))) {
				URegion uRegion = uRegionService.getURegionInfo(map);
				uUser.setuRegion(uRegion);//区域
			}
			baseDAO.update(uUser);
		}else{
			uUser = new UUser();
			uUser.setUserId(WebPublicMehod.getUUID());
			uUser.setPhone(map.get("phone"));
			//真实姓名
			if (publicService.StringUtil(map.get("realName"))) {
				uUser.setRealname(map.get("realName"));
			}
			//昵称
			if (publicService.StringUtil(map.get("nickName"))) {
				uUser.setNickname(map.get("nickName"));
			}
			//性别
			if (publicService.StringUtil(map.get("sex"))) {
				uUser.setSex(map.get("sex"));
			}
			//出生年月
			if (publicService.StringUtil(map.get("birthday"))) {
				Date birthday = PublicMethod.getStringToDate(map.get("birthday"), "yyyy-MM-dd");
				uUser.setBirthday(birthday);//出生日期
				uUser.setAge(getAgeByBirthday(birthday));//年龄
			}
			//身高
			if (publicService.StringUtil(map.get("height"))) {
				uUser.setHeight(map.get("height"));
			}
			//体重
			if (publicService.StringUtil(map.get("weight"))) {
				uUser.setWeight(map.get("weight"));
			}
			//区域
			if (publicService.StringUtil(map.get("area"))) {
				URegion uRegion = uRegionService.getURegionInfo(map);
				uUser.setuRegion(uRegion);//区域
			}
			uUser.setUserStatus("1");
			uUser.setUserType("1");
			uUser.setOld_key_id(-1);
			baseDAO.save(uUser);
		}
		//更新lbs
		this.setAreaToBaiduLBS(uUser);
		//添加初始球员信息
		uplayerService.createUplayer(map, uUser, null);
		return uUser;
	}

	/**
	 * 
	 * 
	   TODO - 修改年龄为空的数据
	   @param map
	   @return
	   @throws Exception
	   2016年7月14日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> setAge(HashMap<String, String> map) throws Exception{
		 HashMap<String, Object> resultMap = new HashMap<>();
		 //查询生日不为空的用户数据
		List<UUser> uUsers = baseDAO.find("from UUser where birthday is not null");
		if (null != uUsers && uUsers.size() > 0) {
			for (UUser uUser : uUsers) {
				if (publicService.isNullOrEmpty(uUser.getAge())) {
					//查询年龄是否为空，为空则根据出生年月计算年龄
					uUser.setAge(getAgeByBirthday(uUser.getBirthday()));
					baseDAO.update(uUser);
					//更新lbs
					this.setAreaToBaiduLBS(uUser);
				}
			}
		}
		resultMap.put("success", "修改成功");
		return resultMap;
	}

	/**
	 * 
	 * @TODO 用户数据更新至百度云【跑批】
	 * @Title: pubUpdAllUserLBS 
	 * @param map
	 * @throws Exception 
	 * @author charlesbin
	 * @date 2016年7月27日 下午5:56:32
	 */
	@Override
	public void pubUpdAllUserLBS(HashMap<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		List<UUser> userList = new ArrayList<UUser>();
		userList = baseDAO.find(" from UUser where UUser.uRegion is not null ");
		
		if(0<userList.size()){
//			int teamSize = teamsList.size();
//			System.out.println("总数="+teamSize);
				// 计算限制页数
//				Double a = 1.0*teamSize/300;
//				Double b = Math.ceil(a);
//				Integer pageNum = b.intValue();
			for(UUser uuser : userList){
				//更新球队
				this.setAreaToBaiduLBS(uuser);
			}
		}
	}

	@Override
	public UUser getUUserByPlayerId(HashMap<String, String> map) throws Exception {
		String hql = "select p.UUser from UPlayer p where p.playerId =:playerId";
		UUser user = this.baseDAO.get(map,hql);
		return user;
	}

	@Override
	public UUser getUUserByPlayerId(String playerId) throws Exception {
		String hql = "select p.UUser from UPlayer p where p.playerId ='"+playerId+"'";
		UUser user = this.baseDAO.get(hql);
		return user;
	}

	@Override
	public HashMap<String, Object> setPlayer(HashMap<String, String> map)throws Exception{
		HashMap<String, Object> resultMap = new HashMap<>();
		StringBuffer hql = new StringBuffer("select u.user_id userId,p.player_id playerId from u_user u "
				 +" LEFT JOIN u_player p on u.user_id=p.user_id  "
				 +" where team_id is null and p.player_id is null "
				 +" GROUP BY u.user_id");
		List<HashMap<String, Object>> hashMaps = baseDAO.findSQLMap(hql.toString());
		if (null != hashMaps && hashMaps.size() > 0) {
			for (HashMap<String, Object> hashMap : hashMaps) {
				UUser uUser = baseDAO.get("from UUser where userId=:userId", hashMap);
				if (null != uUser) {
					this.addPlayerinfo(uUser);
				}
				
			}
		}
		resultMap.put("success", "修改成功");
		return resultMap;
	}

	/**
	 * 
	 * 
	   TODO - 更新球员信息
	   @param user
	   2016年8月23日
	   dengqiuru
	 * @throws Exception 
	 */
	private void addPlayerinfo(UUser user) throws Exception {
		HashMap<String, String> map = new HashMap<>();
		if (null != user) {
			map.put("userId", user.getUserId());
			UPlayer uPlayer = baseDAO.get(map, "from UPlayer where UUser.userId=:userId and UTeam.teamId is null");
			if (null == uPlayer) {
				uPlayer = new UPlayer();
				uPlayer.setPlayerId(WebPublicMehod.getUUID());
				uPlayer.setUUser(user);
				uPlayer.setTeamBelonging("2");
				uPlayer.setInTeam("2");
				uPlayer.setAddqd("管理后台");
				baseDAO.save(uPlayer);
			}
		}
	}

}
