package upbox.model.topic;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2016/8/8.
 */
@Document(collection = "u_list_topic")
public class UListTopic  extends UTopic{
    private String objectId;
    private String topic_obj;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTopic_obj() {
        return topic_obj;
    }

    public void setTopic_obj(String topic_obj) {
        this.topic_obj = topic_obj;
    }

    public UListTopic(){}
    public UListTopic(UTopic topic,String objectId,String type) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(this,topic);
        this.objectId = objectId;
        this.topic_obj = type;
    }

}
