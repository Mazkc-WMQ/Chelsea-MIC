package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 挑战动态、看台
 */
@Document(collection = "u_challage_topic")
public class UChallageTopic extends UTopic {

    private String challageId;

    public String getChallageId() {
        return challageId;
    }

    public void setChallageId(String challageId) {
        this.challageId = challageId;
    }
}
