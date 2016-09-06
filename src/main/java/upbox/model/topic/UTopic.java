package upbox.model.topic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.alibaba.fastjson.JSON;

/**
 * 动态、看台 基类
 */
public class UTopic implements Serializable {
	@Id
    private String _id;
    private String topicName;
    private String userId;
    private Date createTime;
    private Integer count; //评论数
    private Integer dcount;//点赞数
    private String timgUrl;//动态图标
    private String content;
    private String topicIsDelete;
    private Integer num;//话题序号
    private Object comment;//评论和点赞信息
    private List<TopicImg> imgUrl;// 动态图片
//    private List<UTopicThumbs> zambia;//点赞 --目前不使用
    private String source;//话题来源

    private String category;

    private String checkType;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getTimgUrl() {
        return timgUrl;
    }

    public void setTimgUrl(String timgUrl) {
        this.timgUrl = timgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopicIsDelete() {
        return topicIsDelete;
    }

    public void setTopicIsDelete(String topicIsDelete) {
        this.topicIsDelete = topicIsDelete;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
    	if(comment instanceof String){
    		this.comment = JSON.parseArray((String)comment, TopicComment.class);
    	}else if (comment instanceof List){
    		this.comment = (List<TopicComment>) comment;
    	}
    }

    public List<TopicImg> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<TopicImg> imgUrl) {
        this.imgUrl = imgUrl;
    }

//
//    public List<UTopicThumbs> getZambia() {
//        return zambia;
//    }
//
//    public void setZambia(List<UTopicThumbs> zambia) {
//        this.zambia = zambia;
//    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
