package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 比赛动态、看台
 */
@Document(collection = "u_match_topic")
public class UMatchTopic extends UTopic {

    private String cmatchId;

    public String getCmatchId() {
        return cmatchId;
    }

    public void setCmatchId(String cmatchId) {
        this.cmatchId = cmatchId;
    }
}
