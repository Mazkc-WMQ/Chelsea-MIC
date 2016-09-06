package upbox.model.topic.povo;

import upbox.model.UUser;
import upbox.model.topic.TopicImg;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class TopicVo {
    private String topicId;
    private String objectId;
    private String timgUrl;
    private String topicName;
    private String content;
    private String topicSource;
    private Date createTime;
    private String timeAgo;
    private Integer count;
    private Integer dcount;
    private Boolean isDelete;
    private Boolean isComment;
    private Boolean isThumbs;
    private String type; //话题类型
    private TopicUserVo user;
    private List<TopicCommentVo> comment;
    private List<TopicThumbsVo> thumbs;
    private List<TopicImg> imgUrl;
    private String category;



    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTimgUrl() {
        return timgUrl;
    }

    public void setTimgUrl(String timgUrl) {
        this.timgUrl = timgUrl;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopicSource() {
        return topicSource;
    }

    public void setTopicSource(String topicSource) {
        this.topicSource = topicSource;
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

    public Integer getDcount() {
        return dcount;
    }

    public void setDcount(Integer dcount) {
        this.dcount = dcount;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Boolean getIsComment() {
        return isComment;
    }

    public void setIsComment(Boolean isComment) {
        this.isComment = isComment;
    }

    public Boolean getIsThumbs() {
        return isThumbs;
    }

    public void setIsThumbs(Boolean isThumbs) {
        this.isThumbs = isThumbs;
    }

    public TopicUserVo getUser() {
        return user;
    }

    public void setUser(TopicUserVo user) {
        this.user = user;
    }

    public List<TopicCommentVo> getComment() {
        return comment;
    }

    public void setComment(List<TopicCommentVo> comment) {
        this.comment = comment;
    }

    public List<TopicThumbsVo> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<TopicThumbsVo> thumbs) {
        this.thumbs = thumbs;
    }

    public List<TopicImg> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<TopicImg> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
