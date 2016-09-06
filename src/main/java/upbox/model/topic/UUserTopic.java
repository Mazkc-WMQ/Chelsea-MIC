package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户动态、看台
 */
@Document(collection = "u_user_topic")
public class UUserTopic extends UTopic {

    private String cuserId;

    public String getCuserId() {
        return cuserId;
    }

    public void setCuserId(String cuserId) {
        this.cuserId = cuserId;
    }
}
