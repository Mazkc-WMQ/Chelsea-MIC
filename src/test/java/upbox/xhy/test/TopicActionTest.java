package upbox.xhy.test;

import com.org.pub.SerializeUtil;
import org.junit.Test;
import upbox.model.UChallenge;
import upbox.model.topic.TopicComment;
import upbox.model.topic.TopicImg;
import upbox.model.topic.UChallageTopic;
import upbox.model.topic.UDuelTopic;
import upbox.model.topic.UMatchTopic;
import upbox.model.topic.UTeamTopic;
import upbox.model.topic.UUserTopic;
import upbox.pub.URLConnectionUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class TopicActionTest {

    @Test
    public void testSaveUserTopics() throws Exception {

        UUserTopic topic = new UUserTopic();
//        topic.setCreateTime(new Date());
        topic.setContent("hahhahahhah...........");
//        topic.setTopicName("哈哈哈");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=a68d9385-217e-4308-9251-4591a0185b17&userStatus=1";
        param = param.concat("&fPlayerId=925ef6c7-00c4-4a29-b399-93dbe16ff3a7&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_savePlayerTopic.do", param);
        System.out.println(response);
    }
    @Test
    public void testSaveUserCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("呵呵哒。。。");
//        comment.setFirstContentId("2da8c03e-ea35-49f2-909b-26217f4c766e");

        String param = "token=a68d9385-217e-4308-9251-4591a0185b17&userStatus=1";
//        param += "&bCommentId=2da8c03e-ea35-49f2-909b-26217f4c766e";
        param = param.concat("&topicId=66e93eca-cb9f-4563-bd7c-99a8e52e7eba&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_savePlayerTopicComment.do", param);
        System.out.println(response);
    }

    @Test
    public void testSaveDuelTopics() throws Exception {

        UDuelTopic topic = new UDuelTopic();
        topic.setCreateTime(new Date());
        topic.setContent("哈哈哈，233333333...........");
        topic.setTopicName("哈哈哈");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=57062d38-4007-4ff8-924b-321e06f58aac&userStatus=1";
        param = param.concat("&duelId=02423ee3-d2c3-4b58-96b1-d8c0e2f3dd46&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveDuelTopic.do", param);
        System.out.println(response);
    }

    @Test
    public void testSaveDuelCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("评论、、、、、评论、、、、、评论。。。。。。");
//        comment.setFirstContentId("8d9bcdb1-881c-4833-88f8-652818aa79f8");

        String param = "token=57062d38-4007-4ff8-924b-321e06f58aac&userStatus=1";
//        param += "&bCommentId=8d9bcdb1-881c-4833-88f8-652818aa79f8";
        param = param.concat("&topicId=aad3c588-bc6e-41e1-abf3-8c0a92ae9581&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveDuelTopicComment.do", param);
        System.out.println(response);
    }
    @Test
    public void testSaveTeamTopics() throws Exception {

        UDuelTopic topic = new UDuelTopic();
        topic.setCreateTime(new Date());
        topic.setContent("哈哈哈...........");
        topic.setTopicName("哈哈哈");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=ced41324-c8d1-4766-9749-1c9d4382ac8d&userStatus=1";
        param = param.concat("&matchId=20160611match8&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveMatchTopic.do", param);
        System.out.println(response);
    }

    @Test
    public void testSaveUserTopic() throws Exception {

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

        System.out.println(SerializeUtil.serialize(topic));
    }
    //--------------------------------------------------//
    @Test
    public void saveChallengeTopic() throws Exception {
        UChallageTopic topic = new UChallageTopic();
        topic.setCreateTime(new Date());
        topic.setContent("约战话题，战话题，话题，题，y...........");
        topic.setTopicName("哈哈哈");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=c21b4ec0-2a8c-418a-bd9b-a743944a7316&userStatus=1";
        param = param.concat("&challengeId=0108c07e-8e54-4134-a754-20f8775bada8&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveChallengeTopic.do", param);
        System.out.println(response);
    }


    @Test
    public void testSaveChallengeCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("评论、、、、、评论、、、、、评论。。。。。。");
//        comment.setFirstContentId("8d9bcdb1-881c-4833-88f8-652818aa79f8");

        String param = "token=57062d38-4007-4ff8-924b-321e06f58aac&userStatus=1";
//        param += "&bCommentId=8d9bcdb1-881c-4833-88f8-652818aa79f8";
        param = param.concat("&topicId=0602e689-ca4b-4e28-8281-05dca0fac23f&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveChallengeTopicComment.do", param);
        System.out.println(response);
    }


    @Test
    public void saveMatchTopic() throws Exception {
        UMatchTopic topic = new UMatchTopic();
        topic.setCreateTime(new Date());
        topic.setContent("赛事话题，事话题，话题，题，y233...........");
        topic.setTopicName("哈哈哈");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=c21b4ec0-2a8c-418a-bd9b-a743944a7316&userStatus=1";
        param = param.concat("&matchId=00889568-059c-4a8c-bfec-4529e92ec6e7&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveMatchTopic.do", param);
        System.out.println(response);
    }


    @Test
    public void testSaveMatchCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("评论、、、、、评论、、、、、评论2312。");
//        comment.setFirstContentId("8d9bcdb1-881c-4833-88f8-652818aa79f8");

        String param = "token=57062d38-4007-4ff8-924b-321e06f58aac&userStatus=1";
//        param += "&bCommentId=8d9bcdb1-881c-4833-88f8-652818aa79f8";
        param = param.concat("&topicId=8745ecf6-41ad-41dd-87cc-42152c87231c&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveTeamTopicComment.do", param);
        System.out.println(response);

    }


    @Test
    public void testReadComment() throws Exception{
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_updateUnreadComment.do", "userStatus=1&token=c21b4ec0-2a8c-418a-bd9b-a743944a7316");
        System.out.println(response);
    }



    //--------------------------------------------------//
    @Test
    public void saveTeamTopic() throws Exception {
        UTeamTopic topic = new UTeamTopic();
        topic.setCreateTime(new Date());
        topic.setContent("赛事话题，事话题，话题，题，y233...........");
        topic.setTopicName("哈哈哈");
        topic.setCategory("2");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=40127c5f-4f9d-424b-b045-5882ecf0be4c&userStatus=1";
        param = param.concat("&teamId=73ddee71-7a82-40dd-ae8c-dbdedca45e0a&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveTeamTopic.do", param);
        System.out.println(response);
    }


    @Test
    public void testSaveTeamCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("评论、、、、、评论、、、、、评论123333333333。");
//        comment.setFirstContentId("8d9bcdb1-881c-4833-88f8-652818aa79f8");

        String param = "token=e051ccfd-d5d1-41e0-89bc-8ddd2341e627&userStatus=1";
//        param += "&bCommentId=8d9bcdb1-881c-4833-88f8-652818aa79f8";
        param = param.concat("&topicId=956d18ba-9fe1-495d-904c-c9364401dd04&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveTeamTopicComment.do", param);
        System.out.println(response);

    }


    @Test
    public void saveCourtTopic() throws Exception {
        UTeamTopic topic = new UTeamTopic();
        topic.setCreateTime(new Date());
        topic.setContent("球场话题，场话题，话题，题，y233...........");
        topic.setTopicName("哈哈哈");
        topic.setCategory("2");
        List<TopicImg> imgs = new ArrayList<TopicImg>();
        TopicImg img = new TopicImg();
        img.setCreatedate(new Date());
        img.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img);
        TopicImg img2 = new TopicImg();
        img2.setCreatedate(new Date());
        img2.setShowUrl("http://img.upbox.com.cn/launchOverflow/2016/7/28/1469674775070.jpg");
        imgs.add(img2);
        topic.setImgUrl(imgs);

        String param = "token=40127c5f-4f9d-424b-b045-5882ecf0be4c&userStatus=1";
        param = param.concat("&courtId=3ae0f836-e2d3-40d8-abd4-e2f410877bb8&topic=").concat(URLEncoder.encode(SerializeUtil.serialize(topic), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveCourtTopic.do", param);
        System.out.println(response);
    }

    @Test
    public void testSaveCourtCommentTopics() throws Exception {

//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("topicId","93d07a25-d95a-4f12-b6ce-11cb40b89f2f");
//        param.put("token","f6035485-38b2-4d41-853c-33bf3a922461");
//        param.put("bCommentId","c1553928-ab77-40c5-8567-510e039694b4");
        TopicComment comment = new TopicComment();
//        comment.setUserId("ffc45d2c-4258-4931-8c76-7be3886865da");
//        comment.setBuserId("12c70551-068d-4fab-98b5-87e721a2c22f");
        comment.setContent("评论、、、、、评论、、、、、评论123333333333。");
//        comment.setFirstContentId("8d9bcdb1-881c-4833-88f8-652818aa79f8");

        String param = "token=e051ccfd-d5d1-41e0-89bc-8ddd2341e627&userStatus=1";
//        param += "&bCommentId=8d9bcdb1-881c-4833-88f8-652818aa79f8";
        param = param.concat("&topicId=8b491f09-9638-4f57-8d3b-da5fdac04e32&comment=").concat(URLEncoder.encode(SerializeUtil.serialize(comment), "utf-8"));
        String response = URLConnectionUtil.sendPost("http://localhost:8089/Chelsea-MIC/topic_saveCourtTopicComment.do", param);
        System.out.println(response);

    }


    @Test
    public void formatter(){
        int i = 12000;
        int c = i/10000;
        if(c > 0){
            System.out.println(c+"万+");
        }else{
            System.out.println(i);
        }

    }


}
