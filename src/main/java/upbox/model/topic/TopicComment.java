package upbox.model.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态看台评论
 */
public class TopicComment implements Serializable {
    private String contentId;//评论id
    private Date createDate;
    private String userId;
    private String content;
    private String buserId;
    private String commentIsDelete;
    private String readType;//是否查看状态
    private String delType;//删除状态
    private String commentType;
    private TopicComment bcontent; //回复的评论
    private String firstContentId;//基于那条一级评论的回复
    private String checkType;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBuserId() {
        return buserId;
    }

    public void setBuserId(String buserId) {
        this.buserId = buserId;
    }

    public String getCommentIsDelete() {
        return commentIsDelete;
    }

    public void setCommentIsDelete(String commentIsDelete) {
        this.commentIsDelete = commentIsDelete;
    }

    public TopicComment getBcontent() {
        return bcontent;
    }

    public void setBcontent(TopicComment bcontent) {
        this.bcontent = bcontent;
    }

    public String getFirstContentId() {
        return firstContentId;
    }

    public void setFirstContentId(String firstContentId) {
        this.firstContentId = firstContentId;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public String getDelType() {
        return delType;
    }

    public void setDelType(String delType) {
        this.delType = delType;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
}
