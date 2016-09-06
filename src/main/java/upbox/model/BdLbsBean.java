package upbox.model;

import java.util.List;

public class BdLbsBean {
	private String uid; 					//数据id string 
	private String geotable_id;				//geotable_id string 
	private String title; 					//poi名称 string 
	private String address; 				//poi地址 string 
	private String province; 				//poi所属省 string(20) 
	private String city; 					//poi所属城市 string(20) 
	private String district; 				//poi所属区 string(20) 
	private String coord_type; 				//坐标系定义 int32 可选 3代表百度经纬度坐标系统 4代表百度墨卡托系统
	private List<Object> location; 				//经纬度 array 
	private String tags; 					//poi的标签 string 
	private String distance; 				//距离，单位为米 int32 
	private String weight; 					//权重 int32 
	private String object_id;
	private String params_type;
	private String create_time;
	private String direction;
	private String type;
	private String longitude;				//经度
	private String latitude;				//维度
	private String date;					//时间
	private String challegeid;				//挑战id
	private String duelid;					//约战id
	private String teamid;					//球队id
	private String key_id;					//球员id
	private String court_id;				//球场id
	private String subcourt_id;				//下属球场id
	private int object_intid;				//主键id数值类型
	private int duel_intid;
	private int challege_intid;
	private int user_intid;
	private int team_intid;
	private int court_intid;
	private int subcourt_intid;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getGeotable_id() {
		return geotable_id;
	}
	public void setGeotable_id(String geotable_id) {
		this.geotable_id = geotable_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCoord_type() {
		return coord_type;
	}
	public void setCoord_type(String coord_type) {
		this.coord_type = coord_type;
	}
	
	public List<Object> getLocation() {
		return location;
	}
	public void setLocation(List<Object> location) {
		this.location = location;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	public String getParams_type() {
		return params_type;
	}
	public void setParams_type(String params_type) {
		this.params_type = params_type;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

//	public String getDuel_id() {
//		return duel_id;
//	}
//	public void setDuel_id(String duel_id) {
//		this.duel_id = duel_id;
//	}
	public String getDuelid() {
		return duelid;
	}
	public void setDuelid(String duelid) {
		this.duelid = duelid;
	}
	public int getObject_intid() {
		return object_intid;
	}
	public void setObject_intid(int object_intid) {
		this.object_intid = object_intid;
	}
	public int getDuel_intid() {
		return duel_intid;
	}
	public void setDuel_intid(int duel_intid) {
		this.duel_intid = duel_intid;
	}
	public int getChallege_intid() {
		return challege_intid;
	}
	public void setChallege_intid(int challege_intid) {
		this.challege_intid = challege_intid;
	}
	public int getUser_intid() {
		return user_intid;
	}
	public void setUser_intid(int user_intid) {
		this.user_intid = user_intid;
	}
	public int getTeam_intid() {
		return team_intid;
	}
	public void setTeam_intid(int team_intid) {
		this.team_intid = team_intid;
	}
	public int getCourt_intid() {
		return court_intid;
	}
	public void setCourt_intid(int court_intid) {
		this.court_intid = court_intid;
	}
	public int getSubcourt_intid() {
		return subcourt_intid;
	}
	public void setSubcourt_intid(int subcourt_intid) {
		this.subcourt_intid = subcourt_intid;
	}
	public String getChallegeid() {
		return challegeid;
	}
	public void setChallegeid(String challegeid) {
		this.challegeid = challegeid;
	}
	public String getTeamid() {
		return teamid;
	}
	public void setTeamid(String teamid) {
		this.teamid = teamid;
	}
	public String getKey_id() {
		return key_id;
	}
	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}
	public String getCourt_id() {
		return court_id;
	}
	public void setCourt_id(String court_id) {
		this.court_id = court_id;
	}
	public String getSubcourt_id() {
		return subcourt_id;
	}
	public void setSubcourt_id(String subcourt_id) {
		this.subcourt_id = subcourt_id;
	}
	
	
}
