package upbox.action;

import com.opensymphony.xwork2.ModelDriven;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import upbox.model.topic.TopicComment;
import upbox.model.topic.UTopic;
import upbox.model.topic.UUserTopic;
import upbox.service.TopicService;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 话题、看台 action
 * @author xhy
 */
@Controller("topicAction")
@Scope("prototype")
public class UTopicAction extends OperAction implements ModelDriven<UTopic> {

    @Resource
    private TopicService topicService;

    private HashMap<String,Object> hashMap;

    /**
     * TODO - 获取球员话题列表 分页
     * @return
     */
    public String getPlayerTopicListMethod(){
        try {
            hashMap = topicService.getUserTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取一条的用户话题列表信息
     * @return
     */
    public String getPlayerTopicListOneMethod(){
        try {
            hashMap = topicService.getUserTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球员话题详情信息
     * @return
     */
    public String getPlayerTopicDetailMethod(){
        try {
            hashMap = topicService.getUserTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球员话题评论 分页
     * @return
     */
    public String getPlayerTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getUserTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getPlayerTopicCommentOneMethod(){
        try {
            hashMap = topicService.getUserTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球员话题评论的评论 分页
     * @return
     */
    public String getPlayerTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getUserTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存球员话题
     * @return
     */
    public String savePlayerTopicMethod(){
        try {
            hashMap = topicService.saveUserTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除球员话题
     * @return
     */
    public String deletePlayerTopicMethod(){
        try {
            hashMap = topicService.deleteUserTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存球员话题回复
     * @return
     */
    public String savePlayerTopicCommentMethod(){
        try {
            hashMap = topicService.saveUserTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除球员话题回复
     * @return
     */
    public String deletePlayerTopicCommentMethod(){
        try {
            hashMap = topicService.deleteUserTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 球员话题点赞
     * @return
     */
    public String savePlayerTopicThumbsMethod(){
        try {
            hashMap = topicService.saveUserTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消球员话题点赞
     * @return
     */
    public String removePlayerTopicThumbsMethod(){
        try {
            hashMap = topicService.removeUserTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }




    /**
     * TODO - 获取约战话题列表 分页
     * @return
     */
    public String getDuelTopicListMethod(){
        try {
            hashMap = topicService.getDuelTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取一条的约战话题列表信息
     * @return
     */
    public String getDuelTopicListOneMethod(){
        try {
            hashMap = topicService.getDuelTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }


    /**
     * TODO - 获取约战话题详情信息
     * @return
     */
    public String getDuelTopicDetailMethod(){
        try {
            hashMap = topicService.getDuelTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取约战话题评论 分页
     * @return
     */
    public String getDuelTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getDuelTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getDuelTopicCommentOneMethod(){
        try {
            hashMap = topicService.getDuelTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }


    /**
     * TODO - 获取约战话题评论的评论 分页
     * @return
     */
    public String getDuelTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getDuelTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存约战话题
     * @return
     */
    public String saveDuelTopicMethod(){
        try {
            hashMap = topicService.saveDuelTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存约战话题回复
     * @return
     */
    public String saveDuelTopicCommentMethod(){
        try {
            hashMap = topicService.saveDuelTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除约战话题
     * @return
     */
    public String deleteDuelTopicMethod(){
        try {
            hashMap = topicService.deleteDuelTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除约战话题回复
     * @return
     */
    public String deleteDuelTopicCommentMethod(){
        try {
            hashMap = topicService.deleteDuelTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 约战话题点赞
     * @return
     */
    public String saveDuelTopicThumbsMethod(){
        try {
            hashMap = topicService.saveDuelTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消约战话题点赞
     * @return
     */
    public String removeDuelTopicThumbsMethod(){
        try {
            hashMap = topicService.removeDuelTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }






    /**
     * TODO - 获取挑战话题列表 分页
     * @return
     */
    public String getChallengeTopicListMethod(){
        try {
            hashMap = topicService.getChallengeTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }


    /**
     * TODO - 获取一条的挑战话题列表信息
     * @return
     */
    public String getChallengeTopicListOneMethod(){
        try {
            hashMap = topicService.getChallengeTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取挑战话题详情信息
     * @return
     */
    public String getChallengeTopicDetailMethod(){
        try {
            hashMap = topicService.getChallengeTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取挑战话题评论 分页
     * @return
     */
    public String getChallengeTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getChallengeTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getChallengeTopicCommentOneMethod(){
        try {
            hashMap = topicService.getChallengeTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取挑战话题评论的评论 分页
     * @return
     */
    public String getChallengeTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getChallengeTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存挑战话题
     * @return
     */
    public String saveChallengeTopicMethod(){
        try {
            hashMap = topicService.saveChallengeTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存挑战话题回复
     * @return
     */
    public String saveChallengeTopicCommentMethod(){
        try {
            hashMap = topicService.saveChallengeTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除挑战话题
     * @return
     */
    public String deleteChallengeTopicMethod(){
        try {
            hashMap = topicService.deleteChallengeTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除挑战话题回复
     * @return
     */
    public String deleteChallengeTopicCommentMethod(){
        try {
            hashMap = topicService.deleteChallengeTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 挑战话题点赞
     * @return
     */
    public String saveChallengeTopicThumbsMethod(){
        try {
            hashMap = topicService.saveChallengeTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消约战话题点赞
     * @return
     */
    public String removeChallengeTopicThumbsMethod(){
        try {
            hashMap = topicService.removeChallengeTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }



    /**
     * TODO - 获取队伍话题列表 分页
     * @return
     */
    public String getTeamTopicListMethod(){
        try {
            hashMap = topicService.getTeamTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取一条的球队话题列表信息
     * @return
     */
    public String getTeamTopicListOneMethod(){
        try {
            hashMap = topicService.getTeamTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取队伍话题详情信息
     * @return
     */
    public String getTeamTopicDetailMethod(){
        try {
            hashMap = topicService.getTeamTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取队伍话题评论 分页
     * @return
     */
    public String getTeamTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getTeamTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getTeamTopicCommentOneMethod(){
        try {
            hashMap = topicService.getTeamTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }


    /**
     * TODO - 获取队伍话题评论的评论 分页
     * @return
     */
    public String getTeamTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getTeamTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存队伍话题
     * @return
     */
    public String saveTeamTopicMethod(){
        try {
            hashMap = topicService.saveTeamTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存队伍话题回复
     * @return
     */
    public String saveTeamTopicCommentMethod(){
        try {
            hashMap = topicService.saveTeamTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除队伍话题
     * @return
     */
    public String deleteTeamTopicMethod(){
        try {
            hashMap = topicService.deleteTeamTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除队伍话题回复
     * @return
     */
    public String deleteTeamTopicCommentMethod(){
        try {
            hashMap = topicService.deleteTeamTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 队伍话题点赞
     * @return
     */
    public String saveTeamTopicThumbsMethod(){
        try {
            hashMap = topicService.saveTeamTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消队伍话题点赞
     * @return
     */
    public String removeTeamTopicThumbsMethod(){
        try {
            hashMap = topicService.removeTeamTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }



    /**
     * TODO - 获取赛事话题列表 分页
     * @return
     */
    public String getMatchTopicListMethod(){
        try {
            hashMap = topicService.getMatchTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取一条的赛事话题列表信息
     * @return
     */
    public String getMatchTopicListOneMethod(){
        try {
            hashMap = topicService.getMatchTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取赛事话题详情信息
     * @return
     */
    public String getMatchTopicDetailMethod(){
        try {
            hashMap = topicService.getMatchTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取赛事话题评论 分页
     * @return
     */
    public String getMatchTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getMatchTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getMatchTopicCommentOneMethod(){
        try {
            hashMap = topicService.getMatchTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取赛事话题评论的评论 分页
     * @return
     */
    public String getMatchTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getMatchTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存赛事话题
     * @return
     */
    public String saveMatchTopicMethod(){
        try {
            hashMap = topicService.saveMatchTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存赛事话题回复
     * @return
     */
    public String saveMatchTopicCommentMethod(){
        try {
            hashMap = topicService.saveMatchTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除赛事话题
     * @return
     */
    public String deleteMatchTopicMethod(){
        try {
            hashMap = topicService.deleteMatchTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除赛事话题回复
     * @return
     */
    public String deleteMatchTopicCommentMethod(){
        try {
            hashMap = topicService.deleteMatchTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 赛事话题点赞
     * @return
     */
    public String saveMatchTopicThumbsMethod(){
        try {
            hashMap = topicService.saveMatchTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消赛事话题点赞
     * @return
     */
    public String removeMatchTopicThumbsMethod(){
        try {
            hashMap = topicService.removeMatchTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }




    /**
     * TODO - 获取球场话题列表 分页
     * @return
     */
    public String getCourtTopicListMethod(){
        try {
            hashMap = topicService.getCourtTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取一条的球场话题列表信息
     * @return
     */
    public String getCourtTopicListOneMethod(){
        try {
            hashMap = topicService.getCourtTopicListOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球场话题详情信息
     * @return
     */
    public String getCourtTopicDetailMethod(){
        try {
            hashMap = topicService.getCourtTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球场话题评论 分页
     * @return
     */
    public String getCourtTopicCommentListPageMethod(){
        try {
            hashMap = topicService.getCourtTopicCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 以评论列表的数据格式获取单条评论
     * @return
     */
    public String getCourtTopicCommentOneMethod(){
        try {
            hashMap = topicService.getCourtTopicCommentOne(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取球场话题评论的评论 分页
     * @return
     */
    public String getCourtTopicCommentToCommentListPageMethod(){
        try {
            hashMap = topicService.getCourtTopicCommentToCommentListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存球场话题
     * @return
     */
    public String saveCourtTopicMethod(){
        try {
            hashMap = topicService.saveCourtTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 保存球场话题回复
     * @return
     */
    public String saveCourtTopicCommentMethod(){
        try {
            hashMap = topicService.saveCourtTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除球场话题
     * @return
     */
    public String deleteCourtTopicMethod(){
        try {
            hashMap = topicService.deleteCourtTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除球场话题回复
     * @return
     */
    public String deleteCourtTopicCommentMethod(){
        try {
            hashMap = topicService.deleteCourtTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 球场话题点赞
     * @return
     */
    public String saveCourtTopicThumbsMethod(){
        try {
            hashMap = topicService.saveCourtTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消球场话题点赞
     * @return
     */
    public String removeCourtTopicThumbsMethod(){
        try {
            hashMap = topicService.removeCourtTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取用户发布话题的权限
     * @return
     */
    public String getTopicReleaseRoleMethod(){
        try {
            hashMap = topicService.getTopicReleaseRole(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 删除话题
     * @return
     */
    public String removeTopicMethod(){
        try {
            hashMap = topicService.removeTopic(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 话题点赞
     * @return
     */
    public String saveTopicThumbsMethod(){
        try {
            hashMap = topicService.saveTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 取消话题点赞
     * @return
     */
    public String removeTopicThumbsMethod(){
        try {
            hashMap = topicService.removeTopicThumbs(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }




    /**
     * TODO- 获取用户所发的话题列表 - 分页
     * @return
     */
    public String getListTopicListPageMethod(){
        try {
            hashMap = this.topicService.getListTopicListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取我所发布的评论 - 分页
     * @return
     */
    public String getListTopicCommentListPageMethod(){
        try {
            hashMap = this.topicService.getListTopicCommentPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

//    /**
//     * TODO - 获取关于我的所有未读评论 - 分页
//     * @return
//     */
//    public String getNewListTopicCommentPageMethod(){
//        try {
//            hashMap = this.topicService.getNewListTopicCommentPage(super.getParams());
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("UTopicAction:" + e.getMessage());
//            return returnRet(null,e);
//        }
//        return returnRet(hashMap, null,"success");
//    }

    /**
     * TODO - 某类话题未读回复列表
     * @return
     */
    public String getTopicUnreadListPageMethod(){
        try {
            hashMap = this.topicService.getTopicUnreadListPage(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 获取关于我的所有未读评论的总数
     * @return
     */
    public String getNewListTopicCommentMethod(){
        try {
            hashMap = this.topicService.getNewListTopicComment(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }


    /**
     * TODO - 更新未读消息的状态为已读
     * @return
     */
    public String updateUnreadCommentMethod(){
        try {
            hashMap = this.topicService.updateReadType(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }



    /**
     * TODO - 分享话题列表接口
     * @return
     */
    public String getShareTopicListMethod(){
        try {
            hashMap = this.topicService.getShareTopicList(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }

    /**
     * TODO - 分享话题详情接口
     * @return
     */
    public String getShareTopicDetailMethod(){
        try {
            hashMap = this.topicService.getShareTopicDetail(super.getParams());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("UTopicAction:" + e.getMessage());
            return returnRet(null,e);
        }
        return returnRet(hashMap, null,"success");
    }




    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }



    @Override
    public UTopic getModel() {
        return null;
    }
}
