package upbox.model;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.org.pub.PublicMethod;

/**
 * 球队活跃信息
 * @author xiao
 *
 */
@Entity
@Table(name = "u_team_honor")
public class UTeamHonor {
	private String keyId;
	private String teamId;
	private Date createdate;
	private String remark;
	private Integer num;
    private Date honorDate;
	
	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@Column(name="team_id")
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getHonorDate() {
		return honorDate;
	}
	public void setHonorDate(Date honorDate) {
		this.honorDate = honorDate;
	}
	
	@Transient
    public String getHonorDateEStr() throws ParseException {
        if(this.honorDate == null)
            return "";
        return PublicMethod.getDateToString(this.honorDate,"yyyy年MM月dd日");
    }
    @Transient
    public String getHonorDateCStr() throws ParseException {
        if(this.honorDate == null)
            return "";
        return PublicMethod.getDateToString(this.honorDate,"yyyy-MM-dd");
    }
}
