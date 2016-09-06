package upbox.model.topic.povo;

/**
 * 个人中心评论类
 */
public class MyCommentVo {
    private TopicUserVo user;
    private String commentId;
    private String topicId;
    private String content;
    private String timeAgo;
    private String imgUrl;
    private String commentType;
    private String delType;
    private String readType;
    private String topicType;
    private String firstContentId;
    private String category;

    private String topicContent;

    public TopicUserVo getUser() {
        return user;
    }

    public void setUser(TopicUserVo user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getDelType() {
        return delType;
    }

    public void setDelType(String delType) {
        this.delType = delType;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public String getFirstContentId() {
        return firstContentId;
    }

    public void setFirstContentId(String firstContentId) {
        this.firstContentId = firstContentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }
}
