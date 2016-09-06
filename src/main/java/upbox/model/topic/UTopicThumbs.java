package upbox.model.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * 话题点赞记录表 --目前废弃
 */
public class UTopicThumbs implements Serializable {
    private String userId;
    private Date createDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
