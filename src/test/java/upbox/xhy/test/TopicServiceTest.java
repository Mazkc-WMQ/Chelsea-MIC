package upbox.xhy.test;

import com.org.pub.PublicMethod;

import com.org.pub.SerializeUtil;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import upbox.model.topic.TopicComment;
import upbox.model.topic.TopicImg;
import upbox.model.topic.UUserTopic;
import upbox.pub.Public_Cache;
import upbox.pub.URLConnectionUtil;
import upbox.pub.WebPublicMehod;
import upbox.service.TopicService;

import javax.annotation.Resource;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContextTest.xml"})
public class TopicServiceTest {
    @Resource
    private TopicService topicService;


    @Test
    public void saveUserTopicCommentTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","ced69dbd-59f0-44db-9c5d-a165d0aad1a9");
        param.put("token","2378e975-2ef0-4007-9e98-2c727f93b575");
        param.put("bCommentId","37fa837b-fa91-4016-baf9-35eac68cac45");
        TopicComment comment = new TopicComment();
//        comment.setUserId("000015d2-7187-4b77-9e30-2552563d5855");
        comment.setBuserId("b2e9be09-d1fe-40f7-ba65-abfe2d96b834");
        comment.setContent("评论。。。评论。。。。评论。。。");
        comment.setFirstContentId("37fa837b-fa91-4016-baf9-35eac68cac45");
//        param.put("content","");
        param.put("comment",SerializeUtil.serialize(comment));

        HashMap<String,Object> retMap = this.topicService.saveUserTopicComment(param);
        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
      public void saveUserTopicTest() throws Exception {
        UUserTopic topic = new UUserTopic();
//        topic.setCreateTime(new Date());
        topic.setContent("正在前往翻车大道。。。。。。。。。.");
        topic.setTopicName("OW");
//        topic.setCuserId("68dcf160-045b-428a-b010-366f733fcea4");
//        topic.setUserId("000015d2-7187-4b77-9e30-2552563d5855");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        topic.setImgUrl(imgs);
        HashMap<String,String> param = new HashMap<String, String>();
        param.put("topic", SerializeUtil.serialize(topic));
        param.put("fPlayerId","68dcf160-045b-428a-b010-366f733fcea4");
        param.put("token","d0a3efa6-f7d0-4296-8306-96d2aa626b44");
        HashMap<String, Object> retMap = this.topicService.saveUserTopic( param);
        System.out.println(JSONObject.fromObject(retMap));
    }



    @Test
    public void saveCourtTopicTest() throws Exception {
        UUserTopic topic = new UUserTopic();
//        topic.setCreateTime(new Date());
        topic.setContent("球场话题，场话题，话题，题，y233...........");
        topic.setTopicName("OW");
//        topic.setCategory("2");
//        topic.setCuserId("68dcf160-045b-428a-b010-366f733fcea4");
//        topic.setUserId("000015d2-7187-4b77-9e30-2552563d5855");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        topic.setImgUrl(imgs);
        HashMap<String,String> param = new HashMap<String, String>();
        param.put("topic", SerializeUtil.serialize(topic));
        param.put("courtId","3ae0f836-e2d3-40d8-abd4-e2f410877bb8");
        param.put("token","58c96d8e-23d5-4613-8d82-c0635c5c5276");
        HashMap<String, Object> retMap = this.topicService.saveCourtTopic(param);
        System.out.println(JSONObject.fromObject(retMap));
    }




    @Test
    public void getUserTopicListTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("playerId","68dcf160-045b-428a-b010-366f733fcea4");
        param.put("token","894a66a1-31fc-4e7b-bbb3-616243985514");
//        param.put("userId","84aadba7-9658-427a-85e6-7f7c7fad9c15");
//        param.put("userId","000015d2-7187-4b77-9e30-2552563d5855");
//        param.put("fUserId","84aadba7-9658-427a-85e6-7f7c7fad9c15");
        param.put("page","1");

        HashMap<String,Object> retMap = this.topicService.getUserTopicList(param);

        System.out.println(JSONObject.fromObject(retMap));
    }

    @Ignore
    public void deleteUserTopicTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","ac097ae1-b17d-4ac2-841e-27d86790534d");
        HashMap<String,Object> retMap = this.topicService.deleteUserTopic(param);
        System.out.println(JSONObject.fromObject(retMap));

    }

    @Test
    public void deleteUserTopicCommentTest() throws Exception{
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","01d5a640-6290-4c7f-b6c9-4eeee6b588af");
        param.put("commentId","bdaa0ae7-50e9-490b-a97d-af43f7ba1dba");

        HashMap<String,Object> retMap = this.topicService.deleteUserTopicComment(param);
        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void saveUserTopicThumbsTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","b195bcea-6cf5-41b4-8ca9-f82ee36ad398");
        param.put("token","894a66a1-31fc-4e7b-bbb3-616243985514");

        HashMap<String,Object> retMap = this.topicService.saveUserTopicThumbs(param);

        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void removeUserTopicThumbsTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","b195bcea-6cf5-41b4-8ca9-f82ee36ad398");
        param.put("token","894a66a1-31fc-4e7b-bbb3-616243985514");

        HashMap<String,Object> retMap = this.topicService.removeUserTopicThumbs(param);

        System.out.println(JSONObject.fromObject(retMap));
}

    @Ignore
    public void getUserTopicDetailTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","a2af5db1-8c74-4d24-a38b-468dbb642ead");
        HashMap<String,Object> retMap = this.topicService.getUserTopicDetail(param);
        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void getUserTopicCommentListPageTest() throws Exception{
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","a2af5db1-8c74-4d24-a38b-468dbb642ead");
        param.put("page","0");
//        param.put("","");
        HashMap<String,Object> retMap = this.topicService.getUserTopicCommentListPage(param);
        System.out.println(retMap);
    }

    @Test
    public void getUserTopicCommentToCommentListPageTest() throws Exception{
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","a2af5db1-8c74-4d24-a38b-468dbb642ead");
        param.put("contentId","b77436f2-a68e-491f-885b-b74219131975");
        param.put("page","0");
//        param.put("","");
        HashMap<String,Object> retMap = this.topicService.getListTopicCommentPage(param);
        System.out.println(JSONObject.fromObject(retMap));
    }



    @Test
    public void getListTopicCommentPageTest() throws Exception {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("token","a68d9385-217e-4308-9251-4591a0185b17");
        hashMap.put("page","0");

        HashMap<String,Object> retMap =this.topicService.getListTopicCommentPage(hashMap);
        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void getNewListTopicCommentPageTest() throws Exception {
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("token","3586ae93-824b-49b8-be44-ac18c1b48551");
        hashMap.put("page","0");

        HashMap<String,Object> retMap =this.topicService.getNewListTopicCommentPage(hashMap);
        System.out.println(JSONObject.fromObject(retMap));
    }







    @Test
    public void saveTeamTopicTest() throws Exception {
        UUserTopic topic = new UUserTopic();
//        topic.setCreateTime(new Date());
        topic.setContent("正在前往翻车大道。。。。。。。。。.");
        topic.setTopicName("OW");
//        topic.setCuserId("68dcf160-045b-428a-b010-366f733fcea4");
//        topic.setUserId("000015d2-7187-4b77-9e30-2552563d5855");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        topic.setImgUrl(imgs);
        HashMap<String,String> param = new HashMap<String, String>();
        param.put("topic", SerializeUtil.serialize(topic));
        param.put("teamId","df2621d4-7615-4485-a818-b8488498b4ee");
        param.put("token","c21b4ec0-2a8c-418a-bd9b-a743944a7316");
        HashMap<String, Object> retMap = this.topicService.saveTeamTopic( param);
        System.out.println(JSONObject.fromObject(retMap));
    }
    @Test
    public void saveTeamTopicCommentTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId", "82353eab-c7f1-4c4b-bd85-bf363783fd97");
        param.put("token", "a68d9385-217e-4308-9251-4591a0185b17");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("000015d2-7187-4b77-9e30-2552563d5855");
        comment.setBuserId("b2e9be09-d1fe-40f7-ba65-abfe2d96b834");
        comment.setContent("评论。。。评论。。。。评论。。。");
        comment.setFirstContentId("9dcf2c9e-cb2f-4d1f-a196-9766deeced1c");
        param.put("content","");
        param.put("comment",SerializeUtil.serialize(comment));

        HashMap<String,Object> retMap = this.topicService.saveTeamTopicComment(param);
        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
        public void saveTeamTopicThumbsTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","83fd5c60-c6d7-4c15-87f7-1bf315ee99f7");
        param.put("token","894a66a1-31fc-4e7b-bbb3-616243985514");

        HashMap<String,Object> retMap = this.topicService.saveTeamTopicThumbs(param);

        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void removeTeamTopicThumbsTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","83fd5c60-c6d7-4c15-87f7-1bf315ee99f7");
        param.put("token","894a66a1-31fc-4e7b-bbb3-616243985514");

        HashMap<String,Object> retMap = this.topicService.removeTeamTopicThumbs(param);

        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void removeTeamTopicCommentTest() throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("topicId","56343275-4cf6-4d90-824f-36a9c1609955");
        param.put("token","2dc85d75-e418-487a-983c-5a7ff860f2b3");
        param.put("commentId","a613c01e-4d04-4af3-9564-da71a64bd8fd");

        HashMap<String,Object> retMap = this.topicService.deleteTeamTopicComment(param);

        System.out.println(JSONObject.fromObject(retMap));
    }

    @Test
    public void updateReadType()throws Exception{
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("token","token=c21b4ec0-2a8c-418a-bd9b-a743944a7316");
        HashMap<String,Object> retMap = this.topicService.updateReadType(param);
        System.out.println(JSONObject.fromObject(retMap));
    }








}
