package upbox.service.impl;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UEquipment;
import upbox.model.UTourist;
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UTouristService;
/**
 * 前端游客接口实现类
 * @author mercideng
 *
 */
@Service("utouristService")
public class UTouristServiceImpl implements UTouristService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	/**
	 * 
	 * 
	   TODO - 游客登录
	   @param map
	   		code 设备号
	   		phoneType 设备类型
	   		ip	  
	   @return
	   		uTourist的HashMap<String, Object>
	   @throws Exception
	   2015年12月28日
	   dengqiuru
	 */
	@Override
	public HashMap<String, Object> touristLogin(HashMap<String, String> map) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<>();
		String codeId = "";//设备ID
		String curType = "2";//游客事件类型  1:关联 2:访问
		String log = "";//游客事件类型
		UTourist uTourist = null;
		//根据code，查询设备号是否存在
		if (publicService.StringUtil(map.get("code"))) {
			UEquipment uEquipment = baseDAO.get(map, "from UEquipment where code=:code ");
			if (null == uEquipment) {//不存在，新增
				codeId = this.createUEquiment(map);
			}else{//存在，获取主键ID
				codeId = uEquipment.getKeyId();
			}
			map.put("keyId", codeId);
			uTourist = baseDAO.get(map,"from UTourist where UEquipment.keyId=:keyId ");
			if (null == uTourist) {
				log = "游客登录访问系统";
				uTourist = creatUTourist(codeId,curType,log);
			}
			//将设备主键Id保存到游客用户表
			if (null == uTourist) {
				return WebPublicMehod.returnRet("error", "游客信息没有保存成功！");
			}
		}else{
			return WebPublicMehod.returnRet("error", "游客信息没有保存成功！");
		}
		uTourist.setUseStatus("-1");
		resultMap.put("uTourist", uTourist);
		return resultMap;
	}
	
	/**
	 * 
	 * 
	   TODO - 保存游客信息
	   @param codeId	设备表主键
	   @param curType	类型
	   @param log		日志
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private UTourist creatUTourist(String codeId,String curType,String log) throws Exception {
		UTourist uTourist = new UTourist();
		UEquipment uEquipment = baseDAO.get(UEquipment.class,codeId);//查询设备对象
		uTourist.setPikeyId(WebPublicMehod.getUUID());
		uTourist.setUEquipment(uEquipment);//设备信息
		uTourist.setCurType(curType);
		uTourist.setLog(log);
		uTourist.setCreatedate(new Date());
		baseDAO.save(uTourist);
		return uTourist;
	}

	/**
	 * 
	 * 
	   TODO -  新建设备表
	   @param map
	   		code		极光推送code
	   		ip			ip地址
	   		phoneType	手机类型
	   @return
	   @throws Exception
	   2016年6月1日
	   dengqiuru
	 */
	private String createUEquiment(HashMap<String, String> map) throws Exception {
		UEquipment equipment = new UEquipment();
		equipment.setKeyId(WebPublicMehod.getUUID());
		if (publicService.StringUtil(map.get("code"))) {
			equipment.setCode(map.get("code"));
		}
		equipment.setCpu(map.get("ip"));
		equipment.setSystem(map.get("phoneType"));
		baseDAO.save(equipment);
		return equipment.getKeyId();
	}

	/**
	 * 
	 * 
	   TODO - 用户登录 注册  关联时，进行记录[用户表与设备表关联的操作日志]
	   @param uUserTemp
	   		userId  用户Id
	   @param equipment
	   		设备表实体
	   @param type
	   		状态：1--注册  2--登录  3--第三方登录   4--第三方关联
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	@Override
	public UTourist inTourist(UUser uUserTemp, UEquipment equipment, String type) throws Exception {
		String log = "";
		if ("1".equals(type)) {
			log = uUserTemp.getUserId() +"与" + equipment.getCode() +"设备号进行注册！";
		}else if ("2".equals(type)) {
			log = uUserTemp.getUserId() +"与" + equipment.getCode() +"设备号进行登录！";
		}else if ("3".equals(type)) {
			log = uUserTemp.getUserId() +"与" + equipment.getCode() +"设备号进行第三方登录！";
		}else if ("4".equals(type)) {
			log = uUserTemp.getUserId() +"与" + equipment.getCode() +"设备号进行第三方登录关联！";
		}
		UTourist tourist = new UTourist();
		tourist.setPikeyId(WebPublicMehod.getUUID());
		tourist.setUEquipment(equipment);
		tourist.setCurType("1");
		tourist.setLog(log);
		tourist.setCreatedate(new Date());
		baseDAO.save(tourist);		
		return tourist;
	}
	
}
