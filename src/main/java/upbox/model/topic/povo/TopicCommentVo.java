package upbox.model.topic.povo;

import upbox.model.UUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class TopicCommentVo {
    private String commentId;
    private TopicUserVo bUser;
    private TopicUserVo user;
    private String content;
    private Date createTime;
    private String timeAgo;
    private Integer count;
    private String firstContentId;
    private List<TopicCommentVo> comment;
    private String delType;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public TopicUserVo getbUser() {
        return bUser;
    }

    public void setbUser(TopicUserVo bUser) {
        this.bUser = bUser;
    }

    public TopicUserVo getUser() {
        return user;
    }

    public void setUser(TopicUserVo user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<TopicCommentVo> getComment() {
        return comment;
    }

    public void setComment(List<TopicCommentVo> comment) {
        this.comment = comment;
    }

    public String getFirstContentId() {
        return firstContentId;
    }

    public void setFirstContentId(String firstContentId) {
        this.firstContentId = firstContentId;
    }

    public String getDelType() {
        return delType;
    }

    public void setDelType(String delType) {
        this.delType = delType;
    }
}
