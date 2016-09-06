package upbox.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import upbox.dao.impl.OperDAOImpl;
import upbox.model.UEquipment;
import upbox.model.UUser;
import upbox.pub.WebPublicMehod;
import upbox.service.PublicService;
import upbox.service.UEquipmentService;
import upbox.service.UUserService;
/**
 * 设备表接口
 * @author mercideng
 *
 */
@Service("uEquipmentService")
public class UEquipmentServiceImpl implements UEquipmentService {
	@Resource
	private OperDAOImpl baseDAO;
	
	@Resource
	private PublicService publicService;
	
	@Resource
	private UUserService uUserService;
	/**
	 * 
	 * 
	   TODO - 根据code获取设备Id 【2.0.0】
	   @param map
	   		code    设备号
	   @return
	   		equipmentId  设备Id
	   2016年4月7日
	   dengqiuru
	 * @throws Exception 
	 */
	@Override
	public String getEquipmentIdByCode(HashMap<String, String> map) throws Exception {
		String equipmentId = null;
		if (publicService.StringUtil(map.get("code"))) {
			UEquipment equipment = baseDAO.get(map, "from UEquipment ue where code = :code ");
			if (null != equipment) {
				equipmentId = equipment.getKeyId();
			}
		}
		return equipmentId;
	}
	/**
	 * 
	 * 
	   TODO - 将设备等信息录入前端用户设备表内 【2.0.0】
	   @param uUser
	   		uUser 		实体
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @return
	   		UEquipment 对象
	   @throws Exception
	   2016年2月22日
	   dengqiuru
	 */
	@Override
	public UEquipment insertEquipmentUser(UUser uUser, HashMap<String, String> map) throws Exception {
		//根据前端传来的用户获取设备信息
		UEquipment equipment = null;
		if (publicService.StringUtil(map.get("code"))) {
			equipment = baseDAO.get(map, "from UEquipment ue where code = :code ");
			if (null == equipment) {
				this.saveEquipment(map);
			}else{
				this.updateEquipment(map,equipment);
			}
			//更新设备号ID到u_user表里面
			uUserService.updateUuserEquip(uUser,equipment);
		}
		return equipment;
	}
	
	/**
	 * 
	 * 
	   TODO - 新增设备记录
	   @param map
	   		ip
	   		system		系统信息
	   		cpu			
	   @param equipment
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void updateEquipment(HashMap<String, String> map,UEquipment equipment) throws Exception {
		if (null != map.get("ip") && !"".equals(map.get("ip")) && !"null".equals(map.get("ip")) ) {
			equipment.setIp(map.get("ip"));
		}
		if (null != map.get("system") && !"".equals(map.get("system")) && !"null".equals(map.get("system"))) {
			equipment.setSystem(map.get("system"));
		}
		if (null != map.get("cpu") && !"".equals(map.get("cpu")) && !"null".equals(map.get("cpu"))) {
			equipment.setCpu(map.get("cpu"));
		}
		baseDAO.update(equipment);
	}

	/**
	 * 
	 * 
	   TODO - 新增设备记录
	   @param map
	   		ip
	   		system		系统信息
	   		cpu			
	   @return
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private UEquipment saveEquipment(HashMap<String, String> map) throws Exception {
		UEquipment equipment =  new UEquipment();
		equipment.setKeyId(WebPublicMehod.getUUID());
		equipment.setCode(map.get("code"));
		if (null != map.get("ip") && !"".equals(map.get("ip")) && !"null".equals(map.get("ip")) ) {
			equipment.setIp(map.get("ip"));
		}
		if (null != map.get("system") && !"".equals(map.get("system")) && !"null".equals(map.get("system"))) {
			equipment.setSystem(map.get("system"));
		}
		if (null != map.get("cpu") && !"".equals(map.get("cpu")) && !"null".equals(map.get("cpu"))) {
			equipment.setCpu(map.get("cpu"));
		}
		baseDAO.save(equipment);
		return equipment;
	}
	/**
	 * 
	 * 
	   TODO - 用户没登录、没注册获取用户设备信息 【2.0.0】
	   @param map
	   		code		设备号
	   		phoneType	设备类型
	   		ip			客户端IP地址
	   @throws Exception
	   2016年3月16日
	   dengqiuru
	 */
	@Override
	public void insertEquipment(HashMap<String, String> map) throws Exception {
		UEquipment uEquipment = null;
		if (publicService.StringUtil(map.get("code"))) {
			uEquipment = baseDAO.get(map, "from UEquipment where code=:code");
			if (null != uEquipment) {
				if (null != map.get("ip") && !"".equals(map.get("ip")) && !"null".equals(map.get("ip")) ) {
					uEquipment.setIp(map.get("ip"));
				}
				if (null != map.get("system") && !"".equals(map.get("system")) && !"null".equals(map.get("system"))) {
					uEquipment.setSystem(map.get("system"));
				}
				if (null != map.get("cpu") && !"".equals(map.get("cpu")) && !"null".equals(map.get("cpu"))) {
					uEquipment.setCpu(map.get("cpu"));
				}
				baseDAO.update(uEquipment);
			}else{
				this.saveEquipment( map);
			}
		}
		this.updateUsercode(map,uEquipment);
	}

	/**
	 * 
	 * 
	   TODO - 更新用户的设备信息
	   @param map
	   @param uEquipment
	   @throws Exception
	   2016年6月2日
	   dengqiuru
	 */
	private void updateUsercode(HashMap<String, String> map, UEquipment uEquipment) throws Exception {
		if (publicService.StringUtil(map.get("token"))) {
			UUser uUser = uUserService.getUserinfoByToken(map);
			if (null != uUser) {
				if (null != uEquipment) {
					map.put("equipmentId", uEquipment.getKeyId());
					uUserService.updateUserEquipment(map);
					baseDAO.getSessionFactory().getCurrentSession().flush();
					baseDAO.getSessionFactory().getCurrentSession().clear();
					uUser.setNumberid(uEquipment.getKeyId());
					baseDAO.update(uUser);
					uUserService.updateRedisUuserinfo(uUser);
				}
			}
		}
	}
}
