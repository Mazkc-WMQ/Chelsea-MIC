package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Administrator on 2016/8/23.
 */
@Document(collection = "u_court_topic")
public class UCourtTopic extends UTopic  {

    private String courtId;

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }
}
