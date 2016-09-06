package upbox.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.struts2.json.annotations.JSON;

/**
 * 球场评分记录表
 * @author wmq
 *
 * 15618777630
 */
@Entity
@Table(name = "u_courtsorce")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)  
public class UCourtsorce implements Serializable{
	private static final long serialVersionUID = 5911166741842194966L;
	private String scoreId;
	private String score;
	private Date date;
	private Date time;
	private String userId;
	private String courtId;
	private String subcourtId;
	
	@Id
	@Column(name = "sorce_id", unique = true, nullable = false, length = 60)
	public String getScoreId()
	{
		return scoreId;
	}
	public void setScoreId(String scoreId)
	{
		this.scoreId = scoreId;
	}
	@Column(name = "sorce", length = 20)
	public String getScore()
	{
		return score;
	}
	public void setScore(String score)
	{
		this.score = score;
	}
	@Temporal(TemporalType.DATE)  @JSON(format="yyyy-MM-dd")
	@Column(name = "date", length = 13)
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	@Column(name = "time", length = 15)   @JSON(format="HH:mm:ss")
	public Date getTime()
	{
		return time;
	}
	public void setTime(Date time)
	{
		this.time = time;
	}
	@Column(name = "user_id", length = 60)
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	@Column(name = "court_id", length = 60)
	public String getCourtId()
	{
		return courtId;
	}
	public void setCourtId(String courtId)
	{
		this.courtId = courtId;
	}
	@Column(name = "subcourt_id", length = 60)
	public String getSubcourtId()
	{
		return subcourtId;
	}
	public void setSubcourtId(String subcourtId)
	{
		this.subcourtId = subcourtId;
	}
}
