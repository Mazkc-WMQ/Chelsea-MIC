package upbox.model.topic;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 球队动态、看台
 */
@Document(collection = "u_team_topic")
public class UTeamTopic extends UTopic {

    private String cteamId;

    public String getCteamId() {
        return cteamId;
    }

    public void setCteamId(String cteamId) {
        this.cteamId = cteamId;
    }

}
