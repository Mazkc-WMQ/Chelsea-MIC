package upbox.model.topic.povo;

import upbox.model.UUser;

import java.util.Date;

/**
 * 点赞
 */
public class TopicThumbsVo {
    private String userId;
    private String userName;
    private String playerId;
    private Date createDate;//点赞时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
