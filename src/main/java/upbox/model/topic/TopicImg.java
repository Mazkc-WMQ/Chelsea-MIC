package upbox.model.topic;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态看台图片
 */
public class TopicImg implements Serializable {
    private String imgId;
    private Date createdate;
    private String showUrl;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

   

    /**
	 * @return the createdate
	 */
	public Date getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate the createdate to set
	 */
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
