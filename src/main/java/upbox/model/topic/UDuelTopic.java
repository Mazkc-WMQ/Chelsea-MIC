package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 约战动动态、看台
 */
@Document(collection = "u_duel_topic")
public class UDuelTopic extends UTopic {

    private String cduelId;

    public String getCduelId() {
        return cduelId;
    }

    public void setCduelId(String cduelId) {
        this.cduelId = cduelId;
    }
}
