package upbox.service;

import java.sql.Statement;
import java.util.List;

import net.sf.json.JSONArray;
import upbox.movetest.bean.UOrder;
import upbox.movetest.bean.UPlayer;
import upbox.movetest.bean.UTeam;
import upbox.movetest.bean.UUeqnumber;
import upbox.movetest.bean.UUser;
import upbox.movetest.bean.UUserAndTeamAlbum;

/**
 * 数据迁移service
 * @author wmq
 *
 * 15618777630
 */
public interface MoveDBTestService {
	/**
	 * 
	 * 
	   TODO - 用户模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveUser(List<UUser> li,Statement stmt)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 设备号模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveEnumber(List<UUeqnumber> li)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户头像模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveUserHead(JSONArray li)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球队相册迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void TeamXCMoveDB(List<UUserAndTeamAlbum> li)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户头像模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveUserHeadXC(List<UUserAndTeamAlbum> li) throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 用户球员模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void movPlayer(List<UPlayer> li,Statement stmt)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 订单模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveOrder(List<UOrder> li,Statement stmt)throws Exception;
	
	/**
	 * 
	 * 
	   TODO - 球队模块迁移
	   @throws Exception
	   2015年12月18日
	   mazkc
	 */
	public void moveTeam(List<UTeam> li,Statement stmt)throws Exception;
}
