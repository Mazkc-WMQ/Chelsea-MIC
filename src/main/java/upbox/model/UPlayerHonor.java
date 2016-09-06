package upbox.model;

import com.org.pub.PublicMethod;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * 球员荣誉
 */
@Entity
@Table(name = "u_player_honor")
public class UPlayerHonor implements Serializable{
    private String keyId;
    private UUser user;
    private String remark;
    private Date createDate;
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

    @NotFound(action= NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public UUser getUser() {
        return user;
    }

    public void setUser(UUser user) {
        this.user = user;
    }

    @Column(name = "remark", length = 150)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Temporal(TemporalType.TIMESTAMP)  @JSON(format="yyyy-MM-dd hh:mm:ss")
    @Column(name = "createDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "num")
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Temporal(TemporalType.DATE)
    @JSON(format="yyyy-MM-dd")
    @Column(name = "honorDate")
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
