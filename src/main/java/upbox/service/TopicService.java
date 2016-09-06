package upbox.service;

import upbox.model.UDuel;
import upbox.model.UUser;
import upbox.model.topic.TopicComment;
import upbox.model.topic.UChallageTopic;
import upbox.model.topic.UDuelTopic;
import upbox.model.topic.UUserTopic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 话题、看台业务实现接口
 * @author xhy
 */
public interface TopicService {

    /**
     * TODO - 查询球员话题列表 分页
     * @param map
     * @return
     */
    public HashMap<String,Object> getUserTopicList(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取一条的用户话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getUserTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询球员话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getUserTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询球员话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getUserTopicCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getUserTopicCommentOne(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询球员话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getUserTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 保存球员话题对象
     * @param topic 用户话题对象
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveUserTopic(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 新增球员话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveUserTopicComment(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 话题点赞
     * @param param topicId 话题Id zUserId 点赞用户Id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveUserTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 话题点赞取消
     * @param param topicId 话题Id zUserId 点赞用户Id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeUserTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除球员话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteUserTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除球员话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteUserTopicComment(HashMap<String, String> param) throws Exception;



    /**
     * TODO - 查询约战话题列表 分页
     * @param map
     * @return
     */
    public HashMap<String,Object> getDuelTopicList(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取一条的约战话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDuelTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询约战话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDuelTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询约战话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDuelTopicCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDuelTopicCommentOne(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询约战话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDuelTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;


    /**
     * TODO - 新增约战话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveDuelTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 新增约战话题 新增约战时调用
     * @param topic
     *          cduelId - 约战Id
     *          userId-创建约战的用户Id
     *          content - 话题内容
     *          imgUrl.showUrl 图片地址
     *          source -发起方球队名
     * @return
     * @throws Exception
     */
    public void saveDuelTopic(UDuelTopic topic) throws Exception;

    /**
     * TODO - 新增约战话题评论
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveDuelTopicComment(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 约战话题点赞
     * @param param topicId 话题Id zUserId 点赞用户Id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveDuelTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 约战话题点赞取消
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeDuelTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除约战话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteDuelTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除约战话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteDuelTopicComment(HashMap<String, String> param) throws Exception;



    /**
     * TODO - 查询挑战话题列表 分页
     * @param map
     * @return
     */
    public HashMap<String,Object> getChallengeTopicList(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取一条的挑战话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getChallengeTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询挑战话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getChallengeTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询挑战话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getChallengeTopicCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getChallengeTopicCommentOne(HashMap<String,String> map) throws Exception;
    /**
     * TODO - 查询挑战话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getChallengeTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;


    /**
     * TODO - 新增挑战话题 新增挑战时调用
     * @param topic
     *          challageId - 约战Id
     *          userId-创建约战的用户Id
     *          content - 话题内容
     *          imgUrl.showUrl 图片地址
     *          source -发起方球队名
     * @return
     * @throws Exception
     */
    public void saveChallengeTopic(UChallageTopic topic) throws Exception;

    /**
     * TODO - 新增挑战话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveChallengeTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 新增挑战话题评论
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveChallengeTopicComment(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 挑战话题点赞
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveChallengeTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 挑战话题点赞取消
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeChallengeTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除约战话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteChallengeTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除约战话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteChallengeTopicComment(HashMap<String, String> param) throws Exception;



    /**
     * TODO - 查询队伍话题列表 分页
     * @param map
     * @return
     */
    public HashMap<String,Object> getTeamTopicList(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取一条的队伍话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTeamTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询队伍话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTeamTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询队伍话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTeamTopicCommentListPage(HashMap<String,String> map) throws Exception;
    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTeamTopicCommentOne(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询队伍话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTeamTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;


    /**
     * TODO - 新增队伍话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveTeamTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 新增队伍话题评论
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveTeamTopicComment(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 队伍话题点赞
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveTeamTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 队伍话题点赞取消
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeTeamTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除队伍话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteTeamTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除队伍话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteTeamTopicComment(HashMap<String, String> param) throws Exception;


    /**
     * TODO - 查询赛事话题列表 - 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getMatchTopicList(HashMap<String, String> map) throws Exception;

    /**
     * TODO - 获取一条的赛事话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getMatchTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询队伍话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getMatchTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询赛事话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getMatchTopicCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getMatchTopicCommentOne(HashMap<String,String> map) throws Exception;
    /**
     * TODO - 查询赛事话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getMatchTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;



    /**
     * TODO - 新增赛事话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveMatchTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 新增赛事话题评论
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveMatchTopicComment(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 赛事话题点赞
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveMatchTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 赛事话题点赞取消
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeMatchTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除赛事话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteMatchTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除赛事话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteMatchTopicComment(HashMap<String, String> param) throws Exception;



    /**
     * TODO - 查询球场话题列表 - 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getCourtTopicList(HashMap<String, String> map) throws Exception;

    /**
     * TODO - 获取一条的球场话题列表信息
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getCourtTopicListOne(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询球场话题详情
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getCourtTopicDetail(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询球场话题评论分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getCourtTopicCommentListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 获取单条评论以评论列表的格式获取
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getCourtTopicCommentOne(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询球场话题评论的评论 分页
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getCourtTopicCommentToCommentListPage(HashMap<String,String> map) throws Exception;



    /**
     * TODO - 新增球场话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveCourtTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 新增球场话题评论
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveCourtTopicComment(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 球场话题点赞
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveCourtTopicThumbs(HashMap<String,String> param) throws  Exception;

    /**
     * TODO - 球场话题点赞取消
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeCourtTopicThumbs(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 删除球场话题
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteCourtTopic(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 删除球场话题评论
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteCourtTopicComment(HashMap<String, String> param) throws Exception;


    /**
     * TODO - 获取某个类型的话题发布权限
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTopicReleaseRole(HashMap<String,String> param) throws Exception;


    /**
     * TODO - 话题未读回复列表 分页
     * @param map type 类型 1用户 2挑战 3约战 4球队 5赛事 如果为个人中心则为空
     *              unreadType 未读类型 1.话题列表未读 2.话题详情页未读 3.个人中心 - 我的话题未读 4.个人中心 - 我的评论未读 5.用户所有未读
     *              objectId 用户 or 挑战 or 约战 or 球队 or 赛事  Id 如果为个人中心则为空
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTopicUnreadListPage(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 查询用户发布的所有话题 - 分页
     * @param param token
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getListTopicListPage(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 查询用户发布的所有评论 - 分页
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getListTopicCommentPage(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 查询与用户相关的未读信息- 分页
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getNewListTopicCommentPage(HashMap<String, String> param) throws Exception;

    /**
     * TODO - 获取与用户相关的未读回复信息的个数
     * @param param
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getNewListTopicComment(HashMap<String,String> param)throws Exception;


    /**
     * TODO - 删除话题
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeTopic(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 话题点赞
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> saveTopicThumbs(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 话题取消点赞
     * @param map
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> removeTopicThumbs(HashMap<String,String> map) throws Exception;


    /**
     * TODO -  修改未读状态
     * @param param type 类型 1用户 2挑战 3约战 4球队 5赛事 如果为个人中心则为空
     *              objectId 用户 or 挑战 or 约战 or 球队 or 赛事  Id 如果为个人中心则为空
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> updateReadType(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 获取话题分享列表
      * @param param type 1用户 2挑战 3约战 4球队 5赛事
     *                 objectId 用户 or 挑战 or 约战 or 球队 or 赛事  Id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getShareTopicList(HashMap<String,String> param) throws Exception;

    /**
     * TODO - 获取话题明细分享
     * @param param type 1用户 2挑战 3约战 4球队 5赛事
     *                topicId 话题id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getShareTopicDetail(HashMap<String,String> param) throws Exception;


    /**
     * TODO - 当前用户是否有未读的话题消息
     * @param userId
     * @return
     * @throws Exception
     */
    public Boolean isTopicUnreadComment(String userId) throws Exception;


    /**
     * TODO - 复制约战下的话题
     * @param map objectId 事件原本的话题 toObjectId 新的事件id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> copyDuelTopicList(HashMap<String,String> map) throws Exception;

    /**
     * TODO - 复制挑战下的话题
     * @param map objectId 事件原本的话题 toObjectId 新的事件id
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> copyChallengeTopicList(HashMap<String,String> map) throws Exception;

}
