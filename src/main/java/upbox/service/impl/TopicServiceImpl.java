package upbox.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.org.pub.PublicMethod;
import com.org.pub.SerializeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.nodes.CollectionNode;
import upbox.dao.impl.MongoOperDAOImpl;
import upbox.dao.impl.OperDAOImpl;
import upbox.model.UBrCourt;
import upbox.model.UChallenge;
import upbox.model.UCourt;
import upbox.model.UDuel;
import upbox.model.UDuelResp;
import upbox.model.UEquipment;
import upbox.model.UFollow;
import upbox.model.UMatchBs;
import upbox.model.UPlayer;
import upbox.model.UPlayerRole;
import upbox.model.UTeam;
import upbox.model.UUser;
import upbox.model.UUserImg;
import upbox.model.topic.TopicComment;
import upbox.model.topic.TopicImg;
import upbox.model.topic.UChallageTopic;
import upbox.model.topic.UCourtTopic;
import upbox.model.topic.UDuelTopic;
import upbox.model.topic.UListTopic;
import upbox.model.topic.UMatchTopic;
import upbox.model.topic.UTeamTopic;
import upbox.model.topic.UTopic;
import upbox.model.topic.UUserTopic;
import upbox.model.topic.povo.MyCommentVo;
import upbox.model.topic.povo.TopicCommentVo;
import upbox.model.topic.povo.TopicThumbsVo;
import upbox.model.topic.povo.TopicUserVo;
import upbox.model.topic.povo.TopicVo;
import upbox.pub.Public_Cache;
import upbox.pub.WebPublicMehod;
import upbox.service.MessageService;
import upbox.service.PublicPushService;
import upbox.service.TopicService;
import upbox.service.UChallengeService;
import upbox.service.UCourtService;
import upbox.service.UDuelService;
import upbox.service.UMatchService;
import upbox.service.UPlayerRoleService;
import upbox.service.UPlayerService;
import upbox.service.UTeamService;
import upbox.service.UUserImgService;
import upbox.service.UUserService;

import javax.annotation.Resource;
import javax.jms.Topic;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 话题、看台 业务接口实现
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService {

    @Resource
    private OperDAOImpl baseDAO;

    @Resource
    private MongoOperDAOImpl mongoDao;

    @Resource
    private UPlayerService uPlayerService;
    @Resource
    private UTeamService uTeamService;
    @Resource
    private UDuelService uDuelService;
    @Resource
    private UChallengeService uChallengeService;
    @Resource
    private UCourtService uCourtService;


    @Resource
    private UUserImgService uUserImgService;
    @Resource
    private UUserService uUserService;
    @Resource
    private UMatchService uMatchService;
    @Resource
    private MessageService messageService;
    @Resource
    private PublicPushService publicPushService;

    @Resource
    private UPlayerRoleService uPlayerRoleService;


    private Integer pageCount = Public_Cache.PAGE_LIMIT;//话题每页显示多少条
    private Integer thumbsCount = 20;//赞显示多少个
    private Integer commentCount = Public_Cache.PAGE_LIMIT;//话题列表评论显示多少条
    private Integer second = 5;//每个用户发消息或话题的时间间隔


    /*********************以下为用户话题部分**************************/
    /**
     * TODO - 获取球员话题列表 分页
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public HashMap<String, Object> getUserTopicList(HashMap<String, String> map) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        //获取要查询的球员用户Id
        UUser fUser = this.uUserService.getUUserByPlayerId(map);
        if (fUser == null) {
            return WebPublicMehod.returnRet("error", "未找到球员信息");
        }
        map.put("fUserId", fUser.getUserId());
        retMap.put("unreadNum", 0);
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null && uUser.getUserId() != null) {
                map.put("userId", uUser.getUserId());
                //用户在该球员话题的未读回复条数
                map.put("objectId", fUser.getUserId());
                map.put("type", "1");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }

        retMap.put("isRelease", this.userTopicReleaseRole(map));

        Query query = Query.query(Criteria.where("cuserId").is(map.get("fUserId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UUserTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UUserTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        //获取话题发布来源 //获取话题发布来源
        UUser user = this.getUserByUserId(map.get("fUserId"));

        for (UUserTopic topic : list) {
            //当前话题发布人
            map.put("tUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.userTopicDeleteRole(map));
            topicVo.setIsComment(this.userTopicReleaseRole(map));
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_user_topic"));

            //获取话题发布来源
            topicVo.setTopicSource(user == null ? "" : this.getUserName(user));

            map.put("topicId", topic.get_id());
            map.put("tableName", "u_user_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));


            tv.add(topicVo);
        }
        retMap.put("topicList", tv);

        return retMap;
    }

    //通过话题Id查找话题，已列表话题的格式返回
    @Override
    public HashMap<String, Object> getUserTopicListOne(HashMap<String, String> map) throws Exception {
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null && uUser.getUserId() != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UUserTopic topic = (UUserTopic) this.mongoDao.findOne(query, UUserTopic.class);

        if (topic == null) {
            return WebPublicMehod.returnRet("error", "未找到话题对象");
        }
        map.put("fUserId", topic.getCuserId());
        map.put("tUserId", topic.getUserId());
        TopicVo topicVo = null;
        topicVo = this.formatterTopic(topic);

        //获取话题发布来源
        UUser user = this.getUserByUserId(topic.getCuserId());
        topicVo.setTopicSource(user == null ? "" : this.getUserName(user));

        topicVo.setIsDelete(this.userTopicDeleteRole(map));
        topicVo.setIsComment(this.userTopicReleaseRole(map));
        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_user_topic"));


        map.put("topicId", topic.get_id());
        map.put("tableName", "u_user_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));

        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));

        return WebPublicMehod.returnRet("topic", topicVo);
    }

    //用户话题详情
    @Override
    public HashMap<String, Object> getUserTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UUserTopic topic = this.mongoDao.findOne(query, UUserTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null && uUser.getUserId() != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        //获取用户默认球员Ids
        UPlayer player = this.getDefaultPlayer(topic.getUserId());
        if (player != null)
            tv.setObjectId(player.getPlayerId());
        map.put("tUserId", topic.getUserId());
        map.put("fUserId", topic.getCuserId());

        //获取话题发布来源
        UUser user = this.getUserByUserId(topic.getCuserId());
        tv.setTopicSource(user == null ? "" : this.getUserName(user));

        //是否可评论
        tv.setIsComment(this.userTopicReleaseRole(map));
        //是否可删除
        tv.setIsDelete(this.userTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_user_topic"));


        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);


        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("1", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }


        return retMap;
    }

    //用户话题详情页面评论 分页
    @Override
    public HashMap<String, Object> getUserTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_user_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getUserTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_user_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    //用户话题详情 评论的评论 分页
    @Override
    public HashMap<String, Object> getUserTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_user_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    //保存用户话题
    @Override
    public HashMap<String, Object> saveUserTopic(HashMap<String, String> param) throws Exception {

        //封装修改话题对象
        UUserTopic topic = (UUserTopic) this.jsonToTopic(param, UUserTopic.class);
        //获取当前登录用户即发布话题的用户
        UUser user = this.uUserService.getUserinfoByToken(param);

        if (user == null || user.getUserId() == null) {
            return WebPublicMehod.returnRet("error", "未登录");
        }

        topic.setUserId(user.getUserId());
        param.put("userId", topic.getUserId());

        param.put("type", "1");
        if (!this.isReleaseTopicTime(param)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }


        //获取话题是针对哪个用户发布的
        UUser fUser = this.uUserService.getUUserByPlayerId(param.get("fPlayerId"));
        if (fUser == null) {
            return WebPublicMehod.returnRet("error", "未找到球员用户");
        }

        topic.setCuserId(fUser.getUserId());

        param.put("fUserId", fUser.getUserId());
        if (!this.userTopicReleaseRole(param)) {
            return WebPublicMehod.returnRet("error", "当前用户不能新建该球员的动态");
        }
        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());


        //获取用户发布来源,发布来源为当前登录用户名
        topic.setSource(this.getUserName(user));
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }


        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");
        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getCuserId(), "1");


        if (!fUser.getUserId().equals(user.getUserId())){
            //发送推送
            this.sendTopicPush(fUser, this.getUserName(user), "topicUser", "t02");
            //发送消息
            this.sendTopicMsg("topic", "topicUser", this.getUserName(user), "{\"jump\":\"t04\",\"topicId\":\"" + topic.get_id() + "\"}", fUser.getUserId());
        }


        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增球员话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    //保存用户话题评论
    @Override
    public HashMap<String, Object> saveUserTopicComment(HashMap<String, String> param) throws Exception {

        param.put("tableName", "u_user_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(param, UUserTopic.class);

        return retMap;
    }

    //用户话题点赞
    @Override
    public HashMap<String, Object> saveUserTopicThumbs(HashMap<String, String> param) throws Exception {

        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_user_topic");
        return this.saveThumbsTopic(param, UUserTopic.class);


    }

    //用户话题点赞取消
    @Override
    public HashMap<String, Object> removeUserTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_user_topic");
        return this.removeThumbsTopic(param, UUserTopic.class);
    }

    //删除用户话题
    @Override
    public HashMap<String, Object> deleteUserTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_user_topic");
        return this.removeTopic(param, UUserTopic.class);
    }

    //删除用户话题评论
    @Override
    public HashMap<String, Object> deleteUserTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_user_topic");
        return this.removeTopicComment(param, UUserTopic.class);
    }

    /**
     * TODO - 判断用户是否有发布球员话题的权限
     *
     * @param param userId 登录用户
     *              teamId 球队
     * @return
     * @throws Exception
     */
    private Boolean userTopicReleaseRole(HashMap<String, String> param) throws Exception {

        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        //验证用户是否关注球员
        if (this.isTopicFollow(param.get("fUserId"), "3", param.get("userId"))) {
            return true;
        }

        return param.get("userId").equals(param.get("fUserId"));
    }

    /**
     * 用户是否有删除自己话题的权限
     *
     * @return
     */
    private Boolean userTopicDeleteRole(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        if (param.get("userId").equals(param.get("fUserId"))) {
            return true;
        }

        return param.get("userId").equals(param.get("tUserId"));
    }

    /**********************以上为用户话题部分********************************/


    /*********************以下为约战话题部分********************************/


    /**
     * TODO - 约战话题列表 分页
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public HashMap<String, Object> getDuelTopicList(HashMap<String, String> map) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(map.get("duelId"))) {
            return WebPublicMehod.returnRet("error", "约战Id为空");
        }

        retMap.put("unreadNum", 0);
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
                //用户在该约战话题的未读回复条数
                map.put("objectId", map.get("duelId"));
                map.put("type", "2");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }
        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", this.duelTopicReleaseRole(map));

        Query query = Query.query(Criteria.where("cduelId").is(map.get("duelId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UDuelTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UDuelTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        UDuel duel = this.baseDAO.get(UDuel.class, map.get("duelId"));

        for (UDuelTopic topic : list) {
            map.put("fUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.duelTopicDeleteRole(map));
            topicVo.setIsComment(this.duelTopicCommentRole(map));

            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_duel_topic"));

            map.put("topicId", topic.get_id());
            map.put("tableName", "u_duel_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));
            //获取话题发布来源
            topicVo.setTopicSource(this.getTeamName(duel.getUTeam()) + "的约战");

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);
        return retMap;
    }

    @Override
    public HashMap<String, Object> getDuelTopicListOne(HashMap<String, String> map) throws Exception {
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        UDuelTopic topic = this.mongoDao.findOne(query, UDuelTopic.class);

        if (topic == null) {
            return WebPublicMehod.returnRet("error", "未找到话题对象");
        }

        map.put("fUserId", topic.getUserId());

        TopicVo topicVo = null;


        map.put("duelId", topic.getCduelId());
        topicVo = this.formatterTopic(topic);
        topicVo.setIsDelete(this.duelTopicDeleteRole(map));
        topicVo.setIsComment(this.duelTopicCommentRole(map));

        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_duel_topic"));

        map.put("topicId", topic.get_id());
        map.put("tableName", "u_duel_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));

        //获取话题发布来源
        UDuel duel = this.baseDAO.get(UDuel.class, map.get("duelId"));
        topicVo.setTopicSource(this.getTeamName(duel.getUTeam()) + "的约战");

        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));

        return WebPublicMehod.returnRet("topic", topicVo);
    }

    /**
     * TODO - 约战话题详情
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public HashMap<String, Object> getDuelTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UDuelTopic topic = this.mongoDao.findOne(query, UDuelTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }


        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        tv.setObjectId(topic.getCduelId());

        map.put("fUserId", topic.getUserId());
        map.put("duelId", topic.getCduelId());
        //是否可评论
        tv.setIsComment(this.duelTopicCommentRole(map));
        //是否可删除
        tv.setIsDelete(this.duelTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_duel_topic"));

        //获取话题发布来源
        UDuel duel = this.baseDAO.get(UDuel.class, map.get("duelId"));
        tv.setTopicSource(this.getTeamName(duel.getUTeam()) + "的约战");


        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("2", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }

        return retMap;
    }

    //约战话题评论 - 分页
    @Override
    public HashMap<String, Object> getDuelTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_duel_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getDuelTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_duel_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    //约战话题评论的评论 - 分页
    @Override
    public HashMap<String, Object> getDuelTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_duel_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    //保存约战话题
    @Override
    public HashMap<String, Object> saveDuelTopic(HashMap<String, String> map) throws Exception {
        //封装修改话题对象
        UDuelTopic topic = (UDuelTopic) this.jsonToTopic(map, UDuelTopic.class);
        //获取当前登录用户即发布话题的用户
        if (StringUtils.isEmpty(map.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(map);
        topic.setUserId(user.getUserId());

        map.put("userId", topic.getUserId());
        map.put("type", "2");
        if (!this.isReleaseTopicTime(map)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }

        //获取话题是针对哪个约战发布的话题
        if (StringUtils.isEmpty(map.get("duelId"))) {
            return WebPublicMehod.returnRet("error", "约战Id为空");
        }
        UDuel duel = this.baseDAO.get(UDuel.class, map.get("duelId"));
        if (duel == null) {
            return WebPublicMehod.returnRet("error", "未找到约战信息");
        }
        topic.setCduelId(duel.getDuelId());

        map.put("duelId", duel.getDuelId());
        map.put("userId", topic.getUserId());
        if (!this.duelTopicReleaseRole(map)) {
            return WebPublicMehod.returnRet("error", "当前用户不能新建这场约战的动态");
        }
        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());
        //获取用户发布来源,发布来源为约占的发起球队名
        topic.setSource(this.getTeamName(duel.getUTeam()) + "的约战");
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }

        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");

        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getCduelId(), "2");

        //如果是重发调用的复制之前挑战的所有话题
        if (StringUtils.isNotEmpty(map.get("formDuelId"))) {
            this.copyDuelTopicList(map);
        }


        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增约战话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    @Override
    public void saveDuelTopic(UDuelTopic topic) throws Exception {
        topic.setCreateTime(new Date());
        topic.set_id(WebPublicMehod.getUUID());
        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }
        this.mongoDao.save(topic);
        //插入查询表数据
        this.saveTopicList(topic, topic.getCduelId(), "2");
    }

    //保存约战话题评论
    @Override
    public HashMap<String, Object> saveDuelTopicComment(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_duel_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(map, UDuelTopic.class);
        return retMap;
    }

    //约战话题点赞
    @Override
    public HashMap<String, Object> saveDuelTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }

        param.put("tableName", "u_duel_topic");
        return this.saveThumbsTopic(param, UDuelTopic.class);
    }

    //约战话题取消点赞
    @Override
    public HashMap<String, Object> removeDuelTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_duel_topic");
        return this.removeThumbsTopic(param, UDuelTopic.class);
    }

    //删除约战话题
    @Override
    public HashMap<String, Object> deleteDuelTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_duel_topic");
        return this.removeTopic(param, UDuelTopic.class);
    }

    //删除约战话题评论
    @Override
    public HashMap<String, Object> deleteDuelTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_duel_topic");
        return this.removeTopicComment(param, UDuelTopic.class);
    }

    /**
     * TODO - 判断用户是否有发布约战话题的权限
     *
     * @param param userId 登录用户 duelId约战Id
     * @return
     */
    private Boolean duelTopicReleaseRole(HashMap<String, String> param) throws Exception {
        //如果当前访问人为游客则没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        //验证用户是否关注约战
        if (this.isTopicFollow(param.get("duelId"), "4", param.get("userId"))) {
            return true;
        }

        UPlayer player = null;

        UDuel duel = this.baseDAO.get(UDuel.class, param.get("duelId"));

        if (duel == null) {
            return false;
        }

        if (duel != null) {
            param.put("teamId", duel.getUTeam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是发起球队的成员
            if (player != null) {
                return true;
            }
        }


        UDuelResp resp = this.baseDAO.get(param, "from UDuelResp where UDuel.duelId=:duelId");
        if (resp != null) {
            param.put("teamId", resp.getUTeam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应球队的成员
            if (player != null)
                return true;
        }
        return false;
    }

    /**
     * TODO - 当前登录用户是否可以回复约战话题
     *
     * @param param
     * @return
     * @throws Exception
     */
    private Boolean duelTopicCommentRole(HashMap<String, String> param) throws Exception {
        //如果当前访问人为游客则没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }

//        //当前登录用户为话题发布用户
//        if (param.get("userId").equals(param.get("fUserId"))) {
//            return true;
//        }

        //判断登录用户是否关注约战
        if (this.isTopicFollow(param.get("duelId"), "4", param.get("userId"))) {
            return true;
        }

        UPlayer player = null;

        UDuel duel = this.baseDAO.get(UDuel.class, param.get("duelId"));

        if (duel == null) {
            return false;
        }

        if (duel != null) {
            param.put("teamId", duel.getUTeam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是发起球队的成员
            if (player != null) {
                return true;
            }
        }


        UDuelResp resp = this.baseDAO.get(param, "from UDuelResp where UDuel.duelId=:duelId");
        if (resp != null) {
            param.put("teamId", resp.getUTeam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应球队的成员
            if (player != null)
                return true;
        }

        return false;
    }

    /**
     * TODO - 判断用户是否有删除约战话题权限
     *
     * @param param userId 用户 duelId 挑战id fUserId 发布话题的用户
     * @return
     */
    public Boolean duelTopicDeleteRole(HashMap<String, String> param) throws Exception {
        //如果当前访问人为游客则没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }

        //发布话题的人有删除话题的权限
        if (param.get("userId").equals(param.get("fUserId"))) {
            return true;
        }
        UDuel duel = this.baseDAO.get(UDuel.class, param.get("duelId"));
        Integer lvl = null;
        UPlayer player = null;
        if (duel != null) {
            UTeam team = duel.getUTeam();
            param.put("teamId", team.getTeamId());
            //判断登录用户是否为发起球队的成员
            if (this.isOneTeam(param)) {
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }

            }
        }
        UDuelResp resp = this.baseDAO.get(param, "from UDuelResp where UDuel.duelId=:duelId");
        if (resp != null) {
            param.put("teamId", resp.getUTeam().getTeamId());
            //判断登录用户是否为发起球队的成员
            if (this.isOneTeam(param)) {
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /***********************以上为约战话题部分****************************/


    /***********************
     * 以下为挑战话题部分
     ****************************/

    //挑战话题列表 - 分页
    @Override
    public HashMap<String, Object> getChallengeTopicList(HashMap<String, String> map) throws Exception {

        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(map.get("challengeId"))) {
            return WebPublicMehod.returnRet("error", "挑战Id为空");
        }

        retMap.put("unreadNum", 0);

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
                //用户在该挑战话题的未读回复条数
                map.put("objectId", map.get("challengeId"));
                map.put("type", "3");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }

        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", this.challengeTopicReleaseRole(map));

        Query query = Query.query(Criteria.where("challageId").is(map.get("challengeId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UChallageTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UChallageTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        UChallenge challenge = this.baseDAO.get(UChallenge.class, map.get("challengeId"));

        for (UChallageTopic topic : list) {
            map.put("fUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.challengeTopicDeleteRole(map));
            topicVo.setIsComment(this.challengeTopicCommentRole(map));
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_challage_topic"));


            map.put("topicId", topic.get_id());
            map.put("tableName", "u_challage_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));
            //获取用户发布来源,发布来源为约占的发起球队名
            topicVo.setTopicSource(this.getTeamName(challenge.getFteam()) + "的挑战");

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);
        return retMap;
    }

    @Override
    public HashMap<String, Object> getChallengeTopicListOne(HashMap<String, String> map) throws Exception {
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UChallageTopic topic = this.mongoDao.findOne(query, UChallageTopic.class);


        if (topic == null) {
            return WebPublicMehod.returnRet("error", "未找到话题对象");
        }

        TopicVo topicVo = null;
        map.put("challengeId", topic.getChallageId());
        map.put("fUserId", topic.getUserId());
        topicVo = this.formatterTopic(topic);
        topicVo.setIsDelete(this.challengeTopicDeleteRole(map));
        topicVo.setIsComment(this.challengeTopicCommentRole(map));
        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_challage_topic"));


        map.put("topicId", topic.get_id());
        map.put("tableName", "u_challage_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));
        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));

        //获取用户发布来源,发布来源为约占的发起球队名
        UChallenge challenge = this.baseDAO.get(UChallenge.class, map.get("challengeId"));
        topicVo.setTopicSource(this.getTeamName(challenge.getFteam()) + "的挑战");

        return WebPublicMehod.returnRet("topic", topicVo);
    }

    //挑战话题详情
    @Override
    public HashMap<String, Object> getChallengeTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UChallageTopic topic = this.mongoDao.findOne(query, UChallageTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }


        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        tv.setObjectId(topic.getChallageId());
        map.put("fUserId", topic.getUserId());
        map.put("challengeId", topic.getChallageId());
        //是否可评论
        tv.setIsComment(this.challengeTopicCommentRole(map));
        //是否可删除
        tv.setIsDelete(this.challengeTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_challage_topic"));

        //获取用户发布来源,发布来源为约占的发起球队名
        UChallenge challenge = this.baseDAO.get(UChallenge.class, map.get("challengeId"));
        tv.setTopicSource(this.getTeamName(challenge.getFteam()) + "的挑战");

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("3", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }

        return retMap;
    }

    //挑战话题评论 - 分页
    @Override
    public HashMap<String, Object> getChallengeTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_challage_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getChallengeTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_challage_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    //挑战话题评论的评论 - 分页
    @Override
    public HashMap<String, Object> getChallengeTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_challage_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    @Override
    public void saveChallengeTopic(UChallageTopic topic) throws Exception {
        topic.setCreateTime(new Date());
        topic.set_id(WebPublicMehod.getUUID());
        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }
        this.mongoDao.save(topic);
        //插入查询表数据
        this.saveTopicList(topic, topic.getChallageId(), "3");


    }

    //挑战话题保存
    @Override
    public HashMap<String, Object> saveChallengeTopic(HashMap<String, String> map) throws Exception {
        //封装修改话题对象
        UChallageTopic topic = (UChallageTopic) this.jsonToTopic(map, UChallageTopic.class);
        //获取当前登录用户即发布话题的用户
        if (StringUtils.isEmpty(map.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(map);
        topic.setUserId(user.getUserId());

        map.put("userId", topic.getUserId());
        map.put("type", "3");
        if (!this.isReleaseTopicTime(map)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }

        //获取话题是针对哪个约战发布的话题
        if (StringUtils.isEmpty(map.get("challengeId"))) {
            return WebPublicMehod.returnRet("error", "挑战Id为空");
        }
        UChallenge challenge = this.baseDAO.get(UChallenge.class, map.get("challengeId"));
        if (challenge == null) {
            return WebPublicMehod.returnRet("error", "未找到挑战信息");
        }
        topic.setChallageId(challenge.getChallengeId());

        map.put("challengeId", challenge.getChallengeId());
        map.put("userId", topic.getUserId());
        if (!this.challengeTopicReleaseRole(map)) {
            return WebPublicMehod.returnRet("error", "当前用户不能新建这场挑战的动态");
        }
        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());
        //获取用户发布来源,发布来源为约占的发起球队名
        topic.setSource(this.getTeamName(challenge.getFteam()) + "的挑战");
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }

        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");

        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getChallageId(), "3");

        //如果是重发调用的复制之前挑战的所有话题
        if (StringUtils.isNotEmpty(map.get("formChallengeId"))) {
            this.copyChallengeTopicList(map);
        }

        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增挑战话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    //挑战话题评论保存
    @Override
    public HashMap<String, Object> saveChallengeTopicComment(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_challage_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(map, UChallageTopic.class);
        return retMap;
    }

    //挑战话题点赞
    @Override
    public HashMap<String, Object> saveChallengeTopicThumbs(HashMap<String, String> param) throws Exception {

        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_challage_topic");
        return this.saveThumbsTopic(param, UChallageTopic.class);
    }

    //挑战话题取消点赞
    @Override
    public HashMap<String, Object> removeChallengeTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_challage_topic");
        return this.removeThumbsTopic(param, UChallageTopic.class);
    }

    //删除挑战话题
    @Override
    public HashMap<String, Object> deleteChallengeTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        return this.removeTopic(param, UChallageTopic.class);
    }

    //删除挑战话题评论
    @Override
    public HashMap<String, Object> deleteChallengeTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_challage_topic");
        return this.removeTopicComment(param, UChallageTopic.class);
    }

    /**
     * TODO - 当前用户是否可以回复挑战话题
     *
     * @param param
     * @return
     * @throws Exception
     */
    private Boolean challengeTopicCommentRole(HashMap<String, String> param) throws Exception {
        //如果当前访问人为游客则没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }

//        //当前登录用户为话题发布用户
//        if (param.get("userId").equals(param.get("fUserId"))) {
//            return true;
//        }

        //判断登录用户是否关注约战
        if (this.isTopicFollow(param.get("challengeId"), "6", param.get("userId"))) {
            return true;
        }

        UChallenge challenge = this.baseDAO.get(UChallenge.class, param.get("challengeId"));

        if (challenge == null) {
//            new RuntimeException("未找到挑战");
            return false;
        }

        param.put("teamId", challenge.getFteam().getTeamId());
        UPlayer player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是发起队伍的球员
        if (player != null) {
            return true;
        }

        if (challenge.getXteam() != null) {
            param.put("teamId", challenge.getXteam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应队伍的球员
            if (player != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * TODO - 判断用户是否有发布挑战话题的权限
     *
     * @param param userId登录用户 challengeId 挑战id
     * @return
     * @throws Exception
     */
    private Boolean challengeTopicReleaseRole(HashMap<String, String> param) throws Exception {
        //如果当前访问人为游客则没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        //验证用户是否关注挑战
        if (this.isTopicFollow(param.get("challengeId"), "6", param.get("userId"))) {
            return true;
        }

        UChallenge challenge = this.baseDAO.get(UChallenge.class, param.get("challengeId"));

        if (challenge == null) {
//            new RuntimeException("未找到挑战");
            return false;
        }

        param.put("teamId", challenge.getFteam().getTeamId());
        UPlayer player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是发起队伍的球员
        if (player != null) {
            return true;
        }

        if (challenge.getXteam() != null) {
            param.put("teamId", challenge.getXteam().getTeamId());
            player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应队伍的球员
            if (player != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * TODO - 判断用户是否有删除挑战话题权限
     *
     * @param param userId 用户 challengeId 挑战id fUserId 发布话题的用户
     * @return
     */
    public Boolean challengeTopicDeleteRole(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("userId")))
            return false;
        //发布话题的人有删除话题的权限
        if (param.get("userId").equals(param.get("fUserId"))) {
            return true;
        }

        UChallenge challenge = this.baseDAO.get(UChallenge.class, param.get("challengeId"));
        if (challenge == null) {
//            throw new RuntimeException("未找到挑战信息");
            return false;
        }

        Integer lvl = null;
        UPlayer player = null;
        //判断用户是否是发起方球队的管理角色
        UTeam team = challenge.getFteam();
        if (team != null) {
            param.put("teamId", team.getTeamId());
            if (this.isOneTeam(param)) {
                param.put("teamId", team.getTeamId());
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }
            }
        }


        //判断用户是否是响应方球队的管理角色
        team = challenge.getXteam();
        if (team != null) {
            param.put("teamId", team.getTeamId());
            if (this.isOneTeam(param)) {
                param.put("teamId", team.getTeamId());
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /***********************以上为挑战话题部分****************************/


    /***********************
     * 以上为队伍话题部分
     ****************************/

    //队伍话题列表 - 分页
    @Override
    public HashMap<String, Object> getTeamTopicList(HashMap<String, String> map) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(map.get("teamId"))) {
            return WebPublicMehod.returnRet("error", "队伍Id为空");
        }

        retMap.put("unreadNum", 0);

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
                //用户在该队伍话题的未读回复条数
                map.put("objectId", map.get("teamId"));
                map.put("type", "4");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }

        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", this.teamTopicReleaseRole(map));

        Query query = Query.query(Criteria.where("cteamId").is(map.get("teamId"))).addCriteria(Criteria.where("category").is(map.get("category")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UTeamTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UTeamTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        UTeam team = this.baseDAO.get(UTeam.class, map.get("teamId"));

        for (UTeamTopic topic : list) {
            map.put("fUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.teamTopicDeleteRole(map));
            topicVo.setIsComment(this.teamTopicCommentRole(map));
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_team_topic"));


            map.put("topicId", topic.get_id());
            map.put("tableName", "u_team_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));

            //获取用户发布来源,发布来源为约占的发起球队名
            topicVo.setTopicSource(this.getTeamName(team));

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);
        return retMap;
    }

    @Override
    public HashMap<String, Object> getTeamTopicListOne(HashMap<String, String> map) throws Exception {

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UTeamTopic topic = this.mongoDao.findOne(query, UTeamTopic.class);

        if (topic == null) {
            return WebPublicMehod.returnRet("error", "未找到话题对象");
        }

        TopicVo topicVo = null;


        map.put("teamId", topic.getCteamId());

        map.put("fUserId", topic.getUserId());
        topicVo = this.formatterTopic(topic);
        topicVo.setIsDelete(this.teamTopicDeleteRole(map));
        topicVo.setIsComment(this.teamTopicCommentRole(map));

        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_team_topic"));


        map.put("topicId", topic.get_id());
        map.put("tableName", "u_team_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));

        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));


        //获取用户发布来源,发布来源为约占的发起球队名
        UTeam team = this.baseDAO.get(UTeam.class, map.get("teamId"));
        topicVo.setTopicSource(this.getTeamName(team));


        return WebPublicMehod.returnRet("topic", topicVo);
    }

    //队伍话题详情
    @Override
    public HashMap<String, Object> getTeamTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UTeamTopic topic = this.mongoDao.findOne(query, UTeamTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }


        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        tv.setObjectId(topic.getCteamId());
        map.put("fUserId", topic.getUserId());
        map.put("teamId", topic.getCteamId());
        //是否可评论
        tv.setIsComment(this.teamTopicCommentRole(map));
        //是否可删除
        tv.setIsDelete(this.teamTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_team_topic"));

        //获取用户发布来源,发布来源为约占的发起球队名
        UTeam team = this.baseDAO.get(UTeam.class, map.get("teamId"));
        tv.setTopicSource(this.getTeamName(team));

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("4", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }

        return retMap;
    }

    //队伍话题评论 - 分页
    @Override
    public HashMap<String, Object> getTeamTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_team_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getTeamTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_team_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    //队伍话题评论的评论 - 分页
    @Override
    public HashMap<String, Object> getTeamTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_team_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    //新增球队话题
    @Override
    public HashMap<String, Object> saveTeamTopic(HashMap<String, String> map) throws Exception {
        //封装修改话题对象
        UTeamTopic topic = (UTeamTopic) this.jsonToTopic(map, UTeamTopic.class);
        //获取当前登录用户即发布话题的用户
        if (StringUtils.isEmpty(map.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(map);
        topic.setUserId(user.getUserId());

        map.put("userId", topic.getUserId());
        map.put("type", "4");
        if (!this.isReleaseTopicTime(map)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }

        //获取话题是针对哪个约战发布的话题
        if (StringUtils.isEmpty(map.get("teamId"))) {
            return WebPublicMehod.returnRet("error", "球队Id为空");
        }
        UTeam team = this.baseDAO.get(UTeam.class, map.get("teamId"));
        if (team == null) {
            return WebPublicMehod.returnRet("error", "未找到球队信息");
        }
        topic.setCteamId(team.getTeamId());

        map.put("teamId", team.getTeamId());
        map.put("userId", topic.getUserId());
        if (!this.teamTopicReleaseRole(map)) {
            return WebPublicMehod.returnRet("error", "当前用户不能新建这只球队的动态");
        }
        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());
        //获取用户发布来源,发布来源为约占的发起球队名
        topic.setSource(this.getTeamName(team));
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }

        //敏感字检测
        topic.setCheckType("1");

        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getCteamId(), "4");

        if ("1".equals(topic.getCategory()))
            this.sendTeamTopicMsg(topic, user);

        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增球队话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    //新增球队话题评论
    @Override
    public HashMap<String, Object> saveTeamTopicComment(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_team_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(map, UTeamTopic.class);
        return retMap;
    }

    //球队话题点赞
    @Override
    public HashMap<String, Object> saveTeamTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_team_topic");
        return this.saveThumbsTopic(param, UTeamTopic.class);
    }

    //球队话题点赞取消
    @Override
    public HashMap<String, Object> removeTeamTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_team_topic");
        return this.removeThumbsTopic(param, UTeamTopic.class);
    }

    //删除球队话题
    @Override
    public HashMap<String, Object> deleteTeamTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        return this.removeTopic(param, UTeamTopic.class);
    }

    //删除球队话题评论
    @Override
    public HashMap<String, Object> deleteTeamTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_team_topic");
        return this.removeTopicComment(param, UTeamTopic.class);
    }

    /**
     * TODO - 当前登录用户是否可以回复队伍话题
     *
     * @param param
     * @return
     * @throws Exception
     */
    private Boolean teamTopicCommentRole(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        //验证用户是否关注球队
        if (this.isTopicFollow(param.get("teamId"), "2", param.get("userId"))) {
            return true;
        }


        UPlayer player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应队伍的球员


//        if("2".equals(param.get("category"))){
//            if(player != null) {
//                return this.teamNewsOptionRole(player);
//            }
//        }


        if (player != null) {
            return true;
        }

        return false;
    }

    /**
     * TODO - 判断用户是否有发布球队话题的权限
     *
     * @param param userId 登录用户
     *              teamId 球队
     * @return
     * @throws Exception
     */
    private Boolean teamTopicReleaseRole(HashMap<String, String> param) throws Exception {

        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }


        UPlayer player = this.uPlayerService.getPlayerByUserAndTeam(param);//判断用户是否是相应队伍的球员


        if ("2".equals(param.get("category"))) {
            if (player != null) {
                return this.teamNewsOptionRole(player);
            }
        } else {
            if (player != null) {
                return true;
            }

            //验证用户是否关注球队
            if (this.isTopicFollow(param.get("teamId"), "2", param.get("userId"))) {
                return true;
            }
        }

        return false;
    }

    /**
     * TODO - 判断用户是否有删除球队话题的权限
     *
     * @param param userId 用户 teamId 队伍Id fUserId 发布话题的用户
     * @return
     */
    private Boolean teamTopicDeleteRole(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        UPlayer player = this.uPlayerService.getPlayerByUserAndTeam(param);
        if ("2".equals(param.get("category"))) {
            if (player != null) {
                return this.teamNewsOptionRole(player);
            }
        }

        Boolean isRole = false;
        //发布话题的人有删除话题的权限
        if (param.get("userId").equals(param.get("fUserId"))) {
            return true;
        }

        UTeam uTeam = baseDAO.get(param, "from UTeam where teamId=:teamId");
        param.put("teamId", uTeam.getTeamId());

        player = this.uPlayerService.getPlayerByUserAndTeam(param);
        if (player != null) {
            param.put("playerId", player.getPlayerId());
            //是否具有发起约战队伍最高权限
            Integer lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
            if (lvl == null)
                return false;
            if (lvl == 1) {
                return true;
            }
        }
        return isRole;
    }

    //球队发布话题时发推送和消息
    private void sendTeamTopicMsg(UTeamTopic topic, UUser user) throws Exception {
        if (user == null) return;
        //发送消息
        List<UPlayer> players = this.getTeamCaptain(topic.getCteamId());
        if (CollectionUtils.isEmpty(players))
            return;
        for (UPlayer player : players) {
            if (player != null && player.getUUser() != null) {
                UUser userTeam = player.getUUser();
                if (userTeam == null)
                    continue;
                if (userTeam.getUserId().equals(user.getUserId()))
                    continue;

                this.sendTopicMsg("topic", "topicTeam", this.getUserName(user), "{\"jump\":\"t03\",\"topicId\":\"" + topic.get_id() + "\"}", userTeam.getUserId());
                //发送推送
                this.sendTopicPush(userTeam, this.getUserName(user), "topicTeam", "t02");
            }
        }


    }

    /***********************以上为队伍话题部分****************************/

    /************************
     * 以下为赛事话题部分
     ****************************/


    @Override
    public HashMap<String, Object> getMatchTopicList(HashMap<String, String> map) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(map.get("matchId"))) {
            return WebPublicMehod.returnRet("error", "赛事Id为空");
        }

        retMap.put("unreadNum", 0);
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
                //用户在该队伍话题的未读回复条数
                map.put("objectId", map.get("matchId"));
                map.put("type", "5");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }

        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", this.matchTopicReleaseRole(map));

        Query query = Query.query(Criteria.where("cmatchId").is(map.get("matchId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UMatchTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UMatchTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        //获取用户发布来源,发布来源为约占的发起球队名
        UMatchBs match = this.baseDAO.get(UMatchBs.class, map.get("matchId"));
        UTeam fTeam = match.getBsFteam();
        UTeam xTeam = match.getBsXteam();


        for (UMatchTopic topic : list) {
            map.put("fUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.matchTopicDeleteRole(map));
            topicVo.setIsComment(this.matchTopicCommentRole(map));
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_match_topic"));

            map.put("topicId", topic.get_id());
            map.put("tableName", "u_match_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));

            //获取用户发布来源,发布来源为约占的发起球队名
            topicVo.setTopicSource(this.getTeamName(fTeam).concat(" VS ").concat(this.getTeamName(xTeam)));

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);
        return retMap;
    }

    @Override
    public HashMap<String, Object> getMatchTopicListOne(HashMap<String, String> map) throws Exception {

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UMatchTopic topic = this.mongoDao.findOne(query, UMatchTopic.class);

        TopicVo topicVo = null;
        map.put("fUserId", topic.getUserId());
        map.put("matchId", topic.getCmatchId());
        topicVo = this.formatterTopic(topic);
        topicVo.setIsDelete(this.matchTopicDeleteRole(map));
        topicVo.setIsComment(this.matchTopicCommentRole(map));
        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_match_topic"));

        map.put("topicId", topic.get_id());
        map.put("tableName", "u_match_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));

        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));

        //获取用户发布来源,发布来源为约占的发起球队名
        UMatchBs match = this.baseDAO.get(UMatchBs.class, map.get("matchId"));
        UTeam fTeam = match.getBsFteam();
        UTeam xTeam = match.getBsXteam();
        topicVo.setTopicSource(this.getTeamName(fTeam).concat(" VS ").concat(this.getTeamName(xTeam)));


        return WebPublicMehod.returnRet("topic", topicVo);
    }

    @Override
    public HashMap<String, Object> getMatchTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UMatchTopic topic = this.mongoDao.findOne(query, UMatchTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }


        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        tv.setObjectId(topic.getCmatchId());
        map.put("fUserId", topic.getUserId());
        map.put("matchId", topic.getCmatchId());
        //是否可评论
        tv.setIsComment(this.matchTopicCommentRole(map));
        //是否可删除
        tv.setIsDelete(this.matchTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_match_topic"));

        //获取用户发布来源,发布来源为约占的发起球队名
        UMatchBs match = this.baseDAO.get(UMatchBs.class, map.get("matchId"));
        UTeam fTeam = match.getBsFteam();
        UTeam xTeam = match.getBsXteam();
        tv.setTopicSource(this.getTeamName(fTeam).concat(" VS ").concat(this.getTeamName(xTeam)));

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("5", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }

        return retMap;
    }

    @Override
    public HashMap<String, Object> getMatchTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_match_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getMatchTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_match_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    @Override
    public HashMap<String, Object> getMatchTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_match_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveMatchTopic(HashMap<String, String> map) throws Exception {
        //封装修改话题对象
        UMatchTopic topic = (UMatchTopic) this.jsonToTopic(map, UMatchTopic.class);
        //获取当前登录用户即发布话题的用户
        if (StringUtils.isEmpty(map.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(map);
        topic.setUserId(user.getUserId());

        map.put("userId", topic.getUserId());
        map.put("type", "5");
        if (!this.isReleaseTopicTime(map)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }

        //获取话题是针对哪个约战发布的话题
        if (StringUtils.isEmpty(map.get("matchId"))) {
            return WebPublicMehod.returnRet("error", "赛事Id为空");
        }
        UMatchBs match = this.baseDAO.get(UMatchBs.class, map.get("matchId"));
        if (match == null) {
            return WebPublicMehod.returnRet("error", "未找到赛事信息");
        }
        topic.setCmatchId(match.getBsId());

        map.put("matchId", match.getBsId());
        if (!this.matchTopicReleaseRole(map)) {
            return WebPublicMehod.returnRet("error", "当前用户不能新建这场比赛的动态");
        }
        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());
        //获取用户发布来源,发布来源为约占的发起球队名
        UTeam fTeam = match.getBsFteam();
        UTeam xTeam = match.getBsXteam();
        topic.setSource(this.getTeamName(fTeam).concat(" VS ").concat(this.getTeamName(xTeam)));
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }

        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");

        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getCmatchId(), "5");

        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增赛事话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveMatchTopicComment(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_match_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(map, UMatchTopic.class);
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveMatchTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_match_topic");
        return this.saveThumbsTopic(param, UMatchTopic.class);
    }

    @Override
    public HashMap<String, Object> removeMatchTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_match_topic");
        return this.removeThumbsTopic(param, UMatchTopic.class);
    }

    @Override
    public HashMap<String, Object> deleteMatchTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        return this.removeTopic(param, UMatchTopic.class);
    }

    @Override
    public HashMap<String, Object> deleteMatchTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_match_topic");
        return this.removeTopicComment(param, UMatchTopic.class);
    }


    //判断用户是否有回复话题的权限
    private Boolean matchTopicCommentRole(HashMap<String, String> param) throws Exception {
        //没有的登录时，不能发表评论
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        if (StringUtils.isEmpty(param.get("matchId"))) {
            throw new RuntimeException("赛事Id的为空");
        }
        //判断发布用户是否为空
        if (StringUtils.isEmpty(param.get("fUserId"))) {
            return false;
        }

//        //判断发布用户与当前登录用户是否为一个人
//        if (param.get("fUserId").equals(param.get("userId"))) {
//            return true;
//        }

        //判断登录用户是否关注赛事
        if (this.isTopicFollow(param.get("matchId"), "", param.get("userId"))) {
            return true;
        }

        UMatchBs uMatchBs = this.uMatchService.getUMatchBs(param.get("matchId"));
        UPlayer uPlayer = null;

        //赛事为空
        if (uMatchBs == null) {
            return false;
        }
        //判断用户是否是发起球队的成员
        UTeam fTeam = uMatchBs.getBsFteam();
        if (fTeam != null) {
            param.put("teamId", fTeam.getTeamId());
            uPlayer = this.uPlayerService.getPlayerByUserAndTeam(param);
            if (uPlayer != null) {
                return true;
            }
        }

        //判断用户是否是相应球队的成员
        UTeam xTeam = uMatchBs.getBsXteam();
        if (xTeam != null) {
            param.put("teamId", xTeam.getTeamId());
            uPlayer = this.uPlayerService.getPlayerByUserAndTeam(param);
            if (uPlayer != null) {
                return true;
            }
        }
        return false;
    }

    //判断用户是否有删除赛事话题权限
    private Boolean matchTopicDeleteRole(HashMap<String, String> param) throws Exception {

        //没有的登录时，不能发表评论
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        if (StringUtils.isEmpty(param.get("matchId"))) {
            throw new RuntimeException("赛事Id的为空");
        }
        //判断发布用户是否为空
        if (StringUtils.isEmpty(param.get("fUserId"))) {
            return false;
        }

        //判断发布用户与当前登录用户是否为一个人
        if (param.get("fUserId").equals(param.get("userId"))) {
            return true;
        }
        UMatchBs uMatchBs = this.uMatchService.getUMatchBs(param.get("matchId"));

        //赛事为空
        if (uMatchBs == null) {
            return false;
        }
        Integer lvl = null;
        UPlayer player = null;
        //判断用户是不是发起球队里的管理角色
        UTeam fTeam = uMatchBs.getBsFteam();
        if (fTeam != null) {
            param.put("teamId", fTeam.getTeamId());
            if (this.isOneTeam(param)) {
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }
            }
        }

        //判断用户是否是相应球队的管理角色
        UTeam xTeam = uMatchBs.getBsXteam();
        if (xTeam != null) {
            param.put("teamId", xTeam.getTeamId());
            if (this.isOneTeam(param)) {
                player = this.uPlayerService.getPlayerByUserAndTeam(param);
                if (player != null) {
                    param.put("playerId", player.getPlayerId());
                    //是否具有发起约战队伍最高权限
                    lvl = this.uPlayerService.getPlayerIdPlayer(param, null, null);
                    if (lvl == null)
                        return false;
                    if (lvl == 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //判断用户是否有创建赛事话题的权限
    private Boolean matchTopicReleaseRole(HashMap<String, String> param) throws Exception {

        //没有的登录时，不能发表评论
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        if (StringUtils.isEmpty(param.get("matchId"))) {
            throw new RuntimeException("赛事Id的为空");
        }
        //判断登录用户是否关注赛事
        if (this.isTopicFollow(param.get("matchId"), "5", param.get("userId"))) {
            return true;
        }

        UMatchBs uMatchBs = this.uMatchService.getUMatchBs(param.get("matchId"));
        UPlayer uPlayer = null;

        //赛事为空
        if (uMatchBs == null) {
            return false;
        }
        //判断用户是否是发起球队的成员
        UTeam fTeam = uMatchBs.getBsFteam();
        if (fTeam != null) {
            param.put("teamId", fTeam.getTeamId());
            uPlayer = this.uPlayerService.getPlayerByUserAndTeam(param);
            if (uPlayer != null) {
                return true;
            }
        }

        //判断用户是否是相应球队的成员
        UTeam xTeam = uMatchBs.getBsXteam();
        if (xTeam != null) {
            param.put("teamId", xTeam.getTeamId());
            uPlayer = this.uPlayerService.getPlayerByUserAndTeam(param);
            if (uPlayer != null) {
                return true;
            }
        }


        return false;
    }


    //-----------------------------球场话题--------------------------------//

    //球场话题列表
    @Override
    public HashMap<String, Object> getCourtTopicList(HashMap<String, String> map) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(map.get("courtId"))) {
            return WebPublicMehod.returnRet("error", "球场id为空");
        }

        retMap.put("unreadNum", 0);
        UUser uUser = null;
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
                //用户在该队伍话题的未读回复条数
                map.put("objectId", map.get("courtId"));
                map.put("type", "6");
                retMap.put("unreadNum", this.getTopicUnreadListNum(map));
            }
        }

        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", uUser == null ? false : true);

        Query query = Query.query(Criteria.where("courtId").is(map.get("courtId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UCourtTopic> list = this.mongoDao.findPage(Integer.parseInt(map.get("page")), Public_Cache.PAGE_LIMIT, query, UCourtTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;

        //获取小球场
        UBrCourt court = this.baseDAO.get(UBrCourt.class, map.get("courtId"));

        for (UCourtTopic topic : list) {
            map.put("fUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(this.getCourtTopicDeleteRole(map));
            topicVo.setIsComment(uUser == null ? false : true);
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_court_topic"));

            map.put("topicId", topic.get_id());
            map.put("tableName", "u_court_topic");
            //评论相关
            topicVo.setComment(this.getNewCommentList(map));

            //点赞相关
            topicVo.setThumbs(this.getNewThumbsList(map));

            //获取发布来源
            topicVo.setTopicSource(court.getName());

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);

        return retMap;
    }

    @Override
    public HashMap<String, Object> getCourtTopicListOne(HashMap<String, String> map) throws Exception {
        //获取当前登录的用户id
        UUser uUser = null;
        if (StringUtils.isNotEmpty(map.get("token"))) {
            uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UCourtTopic topic = this.mongoDao.findOne(query, UCourtTopic.class);

        TopicVo topicVo = null;
        map.put("fUserId", topic.getUserId());
        topicVo = this.formatterTopic(topic);
        topicVo.setIsDelete(this.getCourtTopicDeleteRole(map));
        topicVo.setIsComment(uUser == null ? false : true);
        topicVo.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_court_topic"));

        map.put("topicId", topic.get_id());
        map.put("tableName", "u_court_topic");
        //评论相关
        topicVo.setComment(this.getNewCommentList(map));

        //点赞相关
        topicVo.setThumbs(this.getNewThumbsList(map));

        //获取发布来源
        UBrCourt court = this.baseDAO.get(UBrCourt.class, topic.getCourtId());
        topicVo.setTopicSource(court.getName());

        return WebPublicMehod.returnRet("topic", topicVo);
    }

    @Override
    public HashMap<String, Object> getCourtTopicDetail(HashMap<String, String> map) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UCourtTopic topic = this.mongoDao.findOne(query, UCourtTopic.class);
        if (topic == null) {
            return this.topicNotFondError();
        }

        UUser uUser = null;
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);
        tv.setObjectId(topic.getCourtId());
        map.put("fUserId", topic.getUserId());
        map.put("courtId", topic.getCourtId());
        //是否可评论
        tv.setIsComment(uUser == null ? false : true);
        //是否可删除
        tv.setIsDelete(this.getCourtTopicDeleteRole(map));
        //是否可点赞
        tv.setIsThumbs(this.isThumbs(topic.get_id(), map.get("userId"), "u_court_topic"));

        //获取发布来源
        UBrCourt court = this.baseDAO.get(UBrCourt.class, map.get("courtId"));
        tv.setTopicSource(court.getName());

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("topic", tv);

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(map.get("userId"))) {
            Boolean isMyTopic = map.get("userId").equals(topic.getUserId());
            List<Object> l = this.getTopicUnread("6", map.get("userId"), topic.get_id(), null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }

        return retMap;
    }

    @Override
    public HashMap<String, Object> getCourtTopicCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_court_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> getCourtTopicCommentOne(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_court_topic");
        TopicCommentVo commentVo = this.getTopicCommentOne(map);
        return WebPublicMehod.returnRet("comment", commentVo);
    }

    @Override
    public HashMap<String, Object> getCourtTopicCommentToCommentListPage(HashMap<String, String> map) throws Exception {
        //指定查询的表名
        map.put("tableName", "u_court_topic");
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("commentList", this.getTopicCommentToComment(map));
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveCourtTopic(HashMap<String, String> map) throws Exception {
        //封装修改话题对象
        UCourtTopic topic = (UCourtTopic) this.jsonToTopic(map, UCourtTopic.class);
        //获取当前登录用户即发布话题的用户
        if (StringUtils.isEmpty(map.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(map);
        if (user == null || user.getUserId() == null) {
            return WebPublicMehod.returnRet("error", "未找到用户信息无法发布话题");
        }

        topic.setUserId(user.getUserId());

        map.put("userId", topic.getUserId());
        map.put("type", "6");
        if (!this.isReleaseTopicTime(map)) {
            return WebPublicMehod.returnRet("error", "您发布话题过于频繁");
        }

        //获取话题是针对哪个约战发布的话题
        if (StringUtils.isEmpty(map.get("courtId"))) {
            return WebPublicMehod.returnRet("error", "球场Id为空");
        }
        UBrCourt court = this.baseDAO.get(UBrCourt.class, map.get("courtId"));
        if (court == null) {
            return WebPublicMehod.returnRet("error", "未找球场信息");
        }
        topic.setCourtId(court.getSubcourtId());

        map.put("courtId", court.getSubcourtId());

        //id
        topic.set_id(WebPublicMehod.getUUID());
        topic.setCreateTime(new Date());

        topic.setSource(court.getName());
        //放入图片Id
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            for (TopicImg img : topic.getImgUrl()) {
                img.setImgId(WebPublicMehod.getUUID());
                img.setCreatedate(new Date());
            }
        }

        //敏感字检测
        topic.setCheckType("1");
        //分类暂时默认为1
        topic.setCategory("1");

        this.mongoDao.save(topic);

        //插入查询表数据
        this.saveTopicList(topic, topic.getCourtId(), "6");

        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "新增球场话题成功");
        retMap.put("topic", this.formatterTopic(topic));
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveCourtTopicComment(HashMap<String, String> map) throws Exception {
        map.put("tableName", "u_court_topic");
        HashMap<String, Object> retMap = this.saveTopicComment(map, UCourtTopic.class);
        return retMap;
    }

    @Override
    public HashMap<String, Object> saveCourtTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_court_topic");
        return this.saveThumbsTopic(param, UCourtTopic.class);
    }

    @Override
    public HashMap<String, Object> removeCourtTopicThumbs(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_court_topic");
        return this.removeThumbsTopic(param, UCourtTopic.class);
    }

    @Override
    public HashMap<String, Object> deleteCourtTopic(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        return this.removeTopic(param, UCourtTopic.class);
    }

    @Override
    public HashMap<String, Object> deleteCourtTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("topicId"))) {
            return WebPublicMehod.returnRet("error", "没有找到话题");
        }
        param.put("tableName", "u_court_topic");
        return this.removeTopicComment(param, UCourtTopic.class);
    }

    private Boolean getCourtTopicDeleteRole(HashMap<String, String> param) {
        if (StringUtils.isEmpty(param.get("userId")) || StringUtils.isEmpty(param.get("fUserId")))
            return false;
        return param.get("fUserId").equals(param.get("userId"));
    }


    @Override
    public HashMap<String, Object> getTopicReleaseRole(HashMap<String, String> map) throws Exception {
        UUser uUser = null;
        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }
        Boolean isRelease = this.getTopicRelease(map);
        return WebPublicMehod.returnRet("isRelease", isRelease);
    }

    //获取用户对于话题的发布权限
    private Boolean getTopicRelease(HashMap<String, String> param) throws Exception {
        //用户为空没有发布权限
        if (StringUtils.isEmpty(param.get("userId"))) {
            return false;
        }
        String type = param.get("type") == null ? "" : param.get("type");

        Boolean isRelease = false;

        switch (type) {
            case "1":
                param.put("playerId", param.get("objectId"));
                //获取要查询的球员用户Id
                UUser fUser = this.uUserService.getUUserByPlayerId(param);
                if (fUser == null) {
                    throw new RuntimeException("未找到球员信息");
                }
                param.put("fUserId", fUser.getUserId());
                isRelease = this.userTopicReleaseRole(param);
                break;
            case "2":
                param.put("duelId", param.get("objectId"));
                isRelease = this.duelTopicReleaseRole(param);
                break;
            case "3":
                param.put("challengeId", param.get("objectId"));
                isRelease = this.challengeTopicReleaseRole(param);
                break;
            case "4":
                param.put("teamId", param.get("objectId"));
                isRelease = this.teamTopicReleaseRole(param);
                break;
            case "5":
                param.put("matchId", param.get("objectId"));
                isRelease = this.matchTopicReleaseRole(param);
                break;
            case "6":
                isRelease = true;
                break;
            default:
                break;
        }


        return isRelease;
    }


    /************************
     * 以上为赛事话题部分
     ****************************/


    //话题详情页面评论列表
    private List<TopicCommentVo> getTopicComment(HashMap<String, String> param) throws Exception {
        //话题评论列表
        List<TopicCommentVo> retList = new ArrayList<TopicCommentVo>();
        List<TopicCommentVo> childCommentVo = null;
        List<Object> childComment = null;

        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("comment.firstContentId", "-1");
//        queryParam.put("comment.delType", "1");
        queryParam.put("_id", param.get("topicId"));
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("col", "comment");
        param.put("$col", "$comment");
        //查询所有回复话题的评论
        List<Object> comments = this.queryInline(queryParam, param, TopicComment.class);
        for (Object cObj : comments) {

            TopicComment c = (TopicComment) cObj;
            TopicCommentVo commentVo = this.formatterTopicComment(c);

            if (commentVo.getUser() == null) {
                break;
            }
            queryParam.put("comment.firstContentId", c.getContentId());
            queryParam.put("comment.delType", "1");
            param.remove("page");
            //查询所有针对话题评论的评论
            childComment = this.queryInline(queryParam, param, TopicComment.class);
            commentVo.setCount(childComment.size());
            childCommentVo = new ArrayList<TopicCommentVo>();
            int count = 1;
            for (Object ccObj : childComment) {
                TopicCommentVo cc = this.formatterTopicComment((TopicComment) ccObj);
                if (cc.getUser() == null)
                    break;
                childCommentVo.add(cc);
                if (count == Public_Cache.PAGE_LIMIT) {
                    break;
                }
                count++;
            }
            if (CollectionUtils.isEmpty(childCommentVo) && "2".equals(c.getDelType())) {
                continue;
            }
            commentVo.setComment(childCommentVo);
            retList.add(commentVo);
        }
        return retList;
    }

    //以评论列表的形式获取单挑评论
    private TopicCommentVo getTopicCommentOne(HashMap<String, String> param) throws Exception {
        //查询评论
        TopicCommentVo commentVo = null;
        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", param.get("topicId"));
        BasicDBObject dbo2 = new BasicDBObject();
        dbo2.put("comment", new BasicDBObject("$elemMatch", new BasicDBObject("contentId", param.get("commentId"))));
        TopicComment c = (TopicComment) this.getInlineObject(param.get("tableName"), "comment", dbo1, dbo2, null, TopicComment.class);
        if (c == null) {
            throw new RuntimeException("未找到评论信息");
        }
        commentVo = this.formatterTopicComment(c);
        //查询所有评论的回复
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("comment.firstContentId", param.get("commentId"));
        queryParam.put("comment.delType", "1");
//        queryParam.put("comment.checkType", "1");
        queryParam.put("_id", param.get("topicId"));
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("col", "comment");
        param.put("$col", "$comment");

        param.remove("page");
        List<Object> childComment = this.queryInline(queryParam, param, TopicComment.class);
        List<TopicCommentVo> childCommentVo = new ArrayList<TopicCommentVo>();
        //获取一共有多少条回复
        if (CollectionUtils.isNotEmpty(childComment)) {
            commentVo.setCount(childComment.size());
            //封装指定数量的评论到评论对象中
            int count = 1;
            for (Object ccObj : childComment) {
                TopicCommentVo cc = this.formatterTopicComment((TopicComment) ccObj);
                if (cc.getUser() == null) {
                    break;
                }
                childCommentVo.add(cc);
                if (count == Public_Cache.PAGE_LIMIT) {
                    break;
                }
                count++;
            }
            commentVo.setComment(childCommentVo);
        }

        return commentVo;
    }

    /**
     * TODO - 评论的评论分页查询
     *
     * @param param contentId 评论Id page 当前页数
     * @return
     * @throws Exception
     */
    private List<TopicCommentVo> getTopicCommentToComment(HashMap<String, String> param) throws Exception {
        //添加查询条件
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("comment.firstContentId", param.get("contentId"));
        query.put("comment.delType", "1");
        query.put("_id", param.get("topicId"));
        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("col", "comment");
        param.put("$col", "$comment");
        List<TopicCommentVo> retList = new ArrayList<TopicCommentVo>();
        List<Object> list = this.queryInline(query, param, TopicComment.class);
        for (Object obj : list) {
            TopicComment comment = (TopicComment) obj;
            TopicCommentVo cc = this.formatterTopicComment(comment);
            if (cc.getUser() == null)
                break;
            retList.add(cc);
        }
        return retList;
    }


    //删除话题公共方法
    private HashMap<String, Object> removeTopic(HashMap<String, String> param, Class cls) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(param.get("topicId")));
        this.mongoDao.remove(query, cls);

        //删除查询表中的话题数据
        this.mongoDao.remove(query, UListTopic.class);
        return WebPublicMehod.returnRet("success", "删除话题成功");
    }

    //新增话题评论方法
    private HashMap<String, Object> saveTopicComment(HashMap<String, String> param, Class cls) throws Exception {

        UUser uUser = this.uUserService.getUserinfoByToken(param);

        if (uUser == null || uUser.getUserId() == null) {
            throw new RuntimeException("未找到登录信息");
        }

        param.put("userId", uUser.getUserId());
        TopicComment comment = this.getUserNewTopicComment(param);
        if (comment != null) {
            if (!this.calcReleaseTeam(comment.getCreateDate())) {
                return WebPublicMehod.returnRet("error", "您评论过于频繁");
            }
        }

        Query query = Query.query(Criteria.where("_id").is(param.get("topicId")));

        UTopic topic = this.mongoDao.findOne(query, cls);
        if (topic == null || topic.getUserId() == null) {
            return this.topicNotFondError();
        }
        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", param.get("topicId"));
        //封装话题评论对象
        comment = this.jsonToComment(param);

//        comment.setContent(param.get("content"));
        comment.setUserId(uUser.getUserId());
        comment.setCreateDate(new Date());
        comment.setContentId(WebPublicMehod.getUUID());

        //判断是否是回复评论的评论
        if (StringUtils.isNotEmpty(param.get("bCommentId"))) {
            BasicDBObject dbo2 = new BasicDBObject(); //主要条件查询
            dbo2.put("comment", new BasicDBObject("$elemMatch", new BasicDBObject("contentId", param.get("bCommentId"))));
            TopicComment c = (TopicComment) this.getInlineObject(param.get("tableName"), "comment", dbo1, dbo2, null, TopicComment.class);
            if (c == null) {
                return WebPublicMehod.returnRet("error", "未找到回复评论");
            } else if (c.getDelType() == "2") {
                return WebPublicMehod.returnRet("error", "无法回复已删除的评论");
            }
            comment.setBcontent(c);
            UUser buser = getUserByUserId(c.getUserId());
            if (buser != null && !buser.getUserId().equals(uUser.getUserId())) {
                //发送推送
                this.sendTopicPush(buser, buser.getRealname(), "commentToComment", "t01");
            }

        }
        comment.setFirstContentId(StringUtils.isEmpty(comment.getFirstContentId()) ? "-1" : comment.getFirstContentId());
        comment.setContentId(WebPublicMehod.getUUID());

        comment.setReadType("1");//评论是否已读，1为未读，2为已读，
        comment.setDelType("1"); //删除状态 1已删除，2未删除
        comment.setCommentType("1");//评论类型为回复

        //敏感字检测
        comment.setCheckType("1");

        this.mongoDao.saveAs(query, cls, "comment", comment);
//        int count = (topic.getCount() == null || topic.getCount() <= 0 ? 0 : topic.getCount()) + 1;
        int count = this.getCommentCount("1", topic.get_id(), param.get("tableName"));


        //修改话题总评论数
        this.updateCol(count, query, "count", cls);

        //修改话题查询表数据
        this.saveTopicListComment(comment, query, dbo1);
        this.updateCol(count, query, "count", UListTopic.class);

        //发送通知 回复话题通知
//        this.sendTopicMsg("topic", "topicComment", uUser.getUserId(), "{\"jump\":\"e01\",\"unreadType\":\"5\"}", topic.getUserId());

        param.put("userId", topic.getUserId());
        UUser bUser = this.uUserService.getUserByUserId(param);
        if (bUser != null && !uUser.getUserId().equals(bUser.getUserId()))
            //发送推送
            this.sendTopicPush(bUser, this.getUserName(uUser), "topicComment", "t01");

        //封装返回参数
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("success", "评论话题成功");
        retMap.put("comment", this.formatterTopicComment(comment));
        return retMap;
    }

    //获取话题的评论或者点赞总数
    private Integer getCommentCount(String type, String topicId, String table) throws Exception {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("_id", topicId);
        query.put("comment.commentType", type);
        query.put("comment.delType", "1");
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("col", "comment");
        param.put("$col", "$comment");

        param.put("tableName", table);
        List<Object> list = queryInline(query, param, null);
        if (CollectionUtils.isEmpty(list))
            return 0;
        return list.size();
    }

    //删除话题评论公用方法
    private HashMap<String, Object> removeTopicComment(HashMap<String, String> param, Class cls) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(param.get("topicId")));
        UTopic topic = this.mongoDao.findOne(query, cls);
        BasicDBObject o1 = new BasicDBObject("_id", param.get("topicId"));
        o1.put("comment.contentId", param.get("commentId"));
//        o1.put("comment.commentType", "1");
        BasicDBObject o2 = new BasicDBObject("$set", new BasicDBObject("comment.$.delType", "2"));
        //修改状态为删除
//        int updateCount = this.mongoDao.updateTag(param.get("tableName"), o1, o2, false, true);
        this.mongoDao.updateTag(param.get("tableName"), o1, o2, false, true);

//        //删除评论下的所有子评论
//        this.deleteCommentChild(param);

//        int count = (topic.getCount() == null || topic.getCount() <= 0 ? 0 : topic.getCount() - 1);
        int count = this.getCommentCount("1", topic.get_id(), param.get("tableName"));
        //修改话题总评论数
        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", param.get("topicId"));
        //修改话题总评论数
        this.updateCol(count, query, "count", cls);

        //删除话题查询
        this.deleteTopicListComment(o1, o2);


        this.updateCol(count, query, "count", UListTopic.class);


        return WebPublicMehod.returnRet("success", "删除评论成功");
    }

    //删除一级评论下的所有子评论
    private void deleteCommentChild(HashMap<String, String> param) throws Exception {
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("comment.firstContentId", param.get("commentId"));
        queryParam.put("comment.delType", "1");
        queryParam.put("_id", param.get("topicId"));
        param.put("col", "comment");
        param.put("$col", "$comment");

        param.remove("page");
        List<Object> childComment = this.queryInline(queryParam, param, TopicComment.class);


        BasicDBObject o1 = new BasicDBObject("_id", param.get("topicId"));
        BasicDBObject o2 = new BasicDBObject("$set", new BasicDBObject("comment.$.delType", "2"));
        for (Object o : childComment) {
            TopicComment co = (TopicComment) o;
            o1.put("comment.contentId", co.getContentId());
            this.mongoDao.updateTag(param.get("tableName"), o1, o2, false, true);
            this.mongoDao.updateTag("u_list_topic", o1, o2, false, true);
        }

    }


    //获取话题回复对象
    private TopicComment getTopicCommentEntity(HashMap<String, String> param) throws Exception {

        BasicDBObject detail = new BasicDBObject("userId", param.get("userId"));
        detail.put("commentType", "2");
        detail.put("delType", param.get("delType"));

        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", param.get("topicId"));
        BasicDBObject dbo2 = new BasicDBObject(); //主要条件查询
        dbo2.put("comment", new BasicDBObject("$elemMatch", detail));
        TopicComment c = (TopicComment) this.getInlineObject(param.get("tableName"), "comment", dbo1, dbo2, null, TopicComment.class);
        return c;
    }

    //话题点赞
    private HashMap<String, Object> saveThumbsTopic(HashMap<String, String> param, Class cls) throws Exception {
        UUser uUser = this.uUserService.getUserinfoByToken(param);
        if (uUser == null || uUser.getUserId() == null) {
            throw new RuntimeException("未找到登录信息");
        }
        Query query = Query.query(Criteria.where("_id").is(param.get("topicId")));
        UTopic topic = this.mongoDao.findOne(query, cls);
        if (topic == null || topic.get_id() == null) {
            return this.topicNotFondError();
        }


        param.put("userId", uUser.getUserId());
        param.put("delType", "1");
        TopicComment thumbs = this.getTopicCommentEntity(param);
        if (thumbs != null) {
            throw new RuntimeException("不能重复点赞");
        }


        BasicDBObject db1 = new BasicDBObject();
        db1.put("_id", param.get("topicId"));

        param.put("delType", "2");
        TopicComment c = this.getTopicCommentEntity(param);


        //封装点赞对象
        thumbs = c == null ? new TopicComment() : c;
        if (c == null) {
            thumbs.setContentId(WebPublicMehod.getUUID());
            thumbs.setUserId(uUser.getUserId());
            thumbs.setCreateDate(new Date());
            thumbs.setDelType("1");
            thumbs.setReadType("1");
            thumbs.setCommentType("2");
            this.mongoDao.saveAs(query, cls, "comment", thumbs);

            //修改查询表中的点赞数
            this.saveTopicListThumbs(thumbs, query, db1);

            //发送点赞消息
//        this.sendTopicMsg("topic", "topicThumbs", uUser.getUsername(), "{\"jump\":\"e01\",\"unreadType\":\"5\"}", topic.getUserId());
            param.put("userId", topic.getUserId());
            UUser bUser = this.uUserService.getUserByUserId(param);
            if(bUser != null && !uUser.getUserId().equals(bUser.getUserId()))
                //发送推送
                this.sendTopicPush(bUser, this.getUserName(uUser), "topicThumbs", "t01");

        } else {
            BasicDBObject update = new BasicDBObject();
            update.append("comment.$.delType", "1");
            update.append("comment.$.createDate", new Date());
            BasicDBObject cDb1 = new BasicDBObject();
            cDb1.append("_id", topic.get_id());
            cDb1.append("comment.contentId", c.getContentId());
            this.mongoDao.updateTag(param.get("tableName"), cDb1, new BasicDBObject("$set", update), false, true);

            this.mongoDao.updateTag("u_list_topic", cDb1, new BasicDBObject("$set", update), false, true);
        }


//        int dCount = (topic.getDcount() == null || topic.getDcount() <= 0 ? 0 : topic.getDcount() )+ 1;
        int dCount = this.getCommentCount("2", topic.get_id(), param.get("tableName"));
        //修改点赞数
        this.updateCol(dCount, query, "dcount", cls);

        //修改查询表中的点赞数
        this.updateCol(dCount, query, "dcount", UListTopic.class);


        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("userName", this.getUserName(uUser));
        retMap.put("success", "点赞成功");
        return retMap;
    }

    //删除话题点赞
    private HashMap<String, Object> removeThumbsTopic(HashMap<String, String> param, Class cls) throws Exception {
        UUser uUser = this.uUserService.getUserinfoByToken(param);

        if (uUser == null || uUser.getUserId() == null) {
            throw new RuntimeException("未找到登录信息");
        }
        Query query = Query.query(Criteria.where("_id").is(param.get("topicId")));
        UTopic topic = this.mongoDao.findOne(query, cls);
        if (topic == null || topic.get_id() == null) {
            return this.topicNotFondError();
        }
        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", param.get("topicId"));
        BasicDBObject dbo2 = new BasicDBObject(); //主要条件查询
        BasicDBObject tj = new BasicDBObject("userId", uUser.getUserId());
        tj.append("delType", "1");
        tj.append("commentType", "2");
        dbo2.put("comment", new BasicDBObject("$elemMatch", tj));
        TopicComment c = (TopicComment) this.getInlineObject(param.get("tableName"), "comment", dbo1, dbo2, null, TopicComment.class);
        if (c == null) {
            return WebPublicMehod.returnRet("error", "用户未点赞");
        }
        BasicDBObject o1 = new BasicDBObject("_id", param.get("topicId"));
        o1.append("comment.contentId", c.getContentId());
//        o1.append("comment.userId", uUser.getUserId());
//        o1.append("comment.delType", "1");
//        o1.append("comment.commentType", "2");
        BasicDBObject o2 = new BasicDBObject("$set", new BasicDBObject("comment.$.delType", "2"));
        //删除点赞
        this.mongoDao.updateTag(param.get("tableName"), o1, o2, false, true);

        //计算点赞数量
//        int dCount = (topic.getDcount() == null || topic.getDcount() <= 0 ? 0 : topic.getDcount() - 1);
        int dCount = this.getCommentCount("2", topic.get_id(), param.get("tableName"));
        //更新点赞总个数
        this.updateCol(dCount, query, "dcount", cls);
        //修改话题查询表点赞数据
        this.deleteTopicListThumbs(o1, o2);
        this.updateCol(dCount, query, "dcount", UListTopic.class);

        return WebPublicMehod.returnRet("success", "取消成功");
    }


    /**
     * TODO - 封装显示话题的对象
     *
     * @param topic
     * @return
     * @throws Exception
     */
    private TopicVo formatterTopic(UTopic topic) throws Exception {
        TopicVo t = new TopicVo();
        t.setTopicId(topic.get_id());
        t.setTimgUrl(topic.getTimgUrl());
        t.setTopicName(topic.getTopicName());
        t.setContent(topic.getContent());
        t.setCreateTime(topic.getCreateTime());
        t.setTimeAgo(this.dateButtonInfo(topic.getCreateTime(), new Date()));
        t.setCount(topic.getCount());
        t.setDcount(topic.getDcount());
//        t.setTopicSource(topic.getSource());
        //封装图片
        t.setImgUrl(topic.getImgUrl());
        //封装创建话题的用户信息
        UUser user = baseDAO.getHRedis(UUser.class, topic.getUserId(), PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));
        t.setUser(this.setTopicUser(user));
        return t;
    }

    //格式化评论回复
    private TopicCommentVo formatterTopicComment(TopicComment comment) throws Exception {
        TopicCommentVo commentVo = new TopicCommentVo();
        commentVo.setCommentId(comment.getContentId());
        //发布评论的人
        UUser user = this.baseDAO.getHRedis(UUser.class, comment.getUserId(), PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));
        ;
        commentVo.setUser(this.setTopicUser(user));
        //被回复的人
        if (StringUtils.isNotEmpty(comment.getBuserId())) {
            UUser bUser = this.baseDAO.getHRedis(UUser.class, comment.getBuserId(), PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));
            if (bUser != null) {
                commentVo.setbUser(this.setTopicUser(bUser));
            }
        }
        commentVo.setContent("2".equals(comment.getDelType()) ? "此回复已删除" : comment.getContent());
        commentVo.setDelType(comment.getDelType());
        commentVo.setCreateTime(comment.getCreateDate());
        commentVo.setTimeAgo(this.dateButtonInfo(comment.getCreateDate(), new Date()));
        commentVo.setFirstContentId(comment.getFirstContentId());
        return commentVo;
    }

    //格式化评论点赞
    private TopicThumbsVo formatterTopicThumbs(TopicComment topicThumbs) throws Exception {
        TopicThumbsVo t = new TopicThumbsVo();
        UUser uUser = this.baseDAO.getHRedis(UUser.class, topicThumbs.getUserId(), PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));

        String name = this.getUserName(uUser);
        t.setUserId(uUser.getUserId());
        t.setUserName(name);
        t.setCreateDate(topicThumbs.getCreateDate());
        //获取用户默认球员Ids
        List<UPlayer> players = this.baseDAO.find("from UPlayer where UUser.userId='" + uUser.getUserId() + "' and UTeam.teamId is null ");

        if (CollectionUtils.isNotEmpty(players) && players.get(0) != null) {
            t.setPlayerId(players.get(0).getPlayerId());
        }

        return t;
    }

    //格式化点赞数评论数
    private String formatterCount(Integer count) {
        if (count == null) {
            return "0";
        }
        Integer c = count / 10000;
        if (c > 0) {
            return c + "万+";
        } else {
            return count.toString();
        }
    }

    //格式化个人中心评论信息
    private MyCommentVo formatterMyComment(TopicComment comment) throws Exception {
        if (comment == null) {
            return null;
        }

        //发布评论的人
        UUser user = this.baseDAO.getHRedis(UUser.class, comment.getUserId(), PublicMethod.getHRedisKey(Public_Cache.PROJECT_NAME, "Object", "Uuser"));

        MyCommentVo myComment = new MyCommentVo();
        myComment.setCommentType(comment.getCommentType());
        myComment.setReadType(comment.getReadType());
        myComment.setDelType(comment.getDelType());
        myComment.setCommentId(comment.getContentId());
        myComment.setContent(comment.getContent());
        myComment.setTimeAgo(this.dateButtonInfo(comment.getCreateDate(), new Date()));
        myComment.setUser(this.setTopicUser(user));
        myComment.setFirstContentId(comment.getFirstContentId());

        return myComment;
    }

    //获取话题展现出来的图片
    private MyCommentVo setMyTopicCommentInfo(String topicId, MyCommentVo myComment) throws Exception {
        Query query = Query.query(Criteria.where("_id").is(topicId));
        UListTopic topic = this.mongoDao.findOne(query, UListTopic.class);
        if (CollectionUtils.isNotEmpty(topic.getImgUrl())) {
            myComment.setImgUrl(topic.getImgUrl().get(0).getShowUrl());
        } else {
            myComment.setImgUrl(topic.getTimgUrl());
        }
        myComment.setTopicType(topic.getTopic_obj());
        myComment.setCategory(topic.getCategory());
        return myComment;
    }

    /**
     * TODO - 封装话题里的用户信息
     *
     * @param user
     * @return
     * @throws Exception
     */
    private TopicUserVo setTopicUser(UUser user) throws Exception {
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            return null;
        }
        String name = this.getUserName(user);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", user.getUserId());
        TopicUserVo u = new TopicUserVo();
        u.setUserId(user.getUserId());
        u.setUserName(name);

        UUserImg img = this.uUserImgService.getHeadPicNotSetByuserId(param);
        if (img != null) {
            u.setImgUrl(img.getImgurl());
        }
        //获取用户默认球员Ids
        UPlayer player = this.getDefaultPlayer(param.get("userId"));
        if (player != null)
            u.setPlayerId(player.getPlayerId());
        return u;
    }

    //获取用户默认playerId
    private UPlayer getDefaultPlayer(String userId) throws Exception {
        List<UPlayer> players = this.baseDAO.find("from UPlayer where UUser.userId='" + userId + "' and UTeam.teamId is null");
        if (CollectionUtils.isNotEmpty(players))
            if (players.get(0) != null) {
                return players.get(0);
            }
        return null;
    }

    //获取用户显示名
    private String getUserName(UUser user) {
        if (user == null) return "";
        String name = user.getRealname();
        if (StringUtils.isEmpty(name)) {
            name = user.getNickname();
            if (StringUtils.isEmpty(name)) {
                name = this.getUserPhoneHide(user.getPhone());
            }
        }
        return name;
    }

    private String getTeamName(UTeam team) {
        if (team == null) {
            return "暂无";
        }
        if (StringUtils.isNotEmpty(team.getShortName())) {
            return team.getShortName();
        } else {
            return team.getName();
        }
    }

    //隐藏手机号中间4位
    private String getUserPhoneHide(String phone) {
        StringBuffer hidePhone = new StringBuffer(phone.substring(0, 3));
        hidePhone.append("****");
        hidePhone.append(phone.substring(7));
        return hidePhone.toString();
    }

    //检查用户是否点赞
    private Boolean isThumbs(String topicId, String userId, String table) throws Exception {
        if (StringUtils.isEmpty(topicId) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(table))
            return false;
        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
        dbo1.put("_id", topicId);
        BasicDBObject dbo2 = new BasicDBObject(); //主要条件查询
        BasicDBObject query = new BasicDBObject("userId", userId);
        query.put("delType", "1");
        query.put("commentType", "2");
        dbo2.put("comment", new BasicDBObject("$elemMatch", query));
        TopicComment c = (TopicComment) this.getInlineObject(table, "comment", dbo1, dbo2, null, TopicComment.class);
        return c == null ? false : true;
    }

    /**
     * TODO - 验证是否关注 挑战，约战，球队，球员
     *
     * @param objectId
     * @param type
     * @param userId
     * @return
     * @throws Exception
     */
    private Boolean isTopicFollow(String objectId, String type, String userId) throws Exception {

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("objectId", objectId);
        param.put("userFollowType", type);
        param.put("userId", userId);
        UFollow follow = this.getFollowByUserAndType(param);
        if (follow != null) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前用户与话题发布用户是否在一个球队
     *
     * @param param userId当前用户 fUserId发起用户 teamId 队伍
     * @return
     */
    private Boolean isOneTeam(HashMap<String, String> param) throws Exception {
        HashMap<String, Object> p = new HashMap<String, Object>();
        for (String key : param.keySet()) {
            p.put(key, param.get(key));
        }
        String hql = "select count(playerId) from UPlayer where ( UUser.userId=:userId and UTeam.teamId=:teamId and inTeam = '1') or ( UUser.userId=:fUserId and UTeam.teamId=:teamId and inTeam = '1') ";

        int count = this.baseDAO.count(hql, p, false);
//        List<Object> list1 = this.baseDAO.find(hql,p);

//        hql = "select count(playerId) from UPlayer where UUser.userId=:userId and and UTeam.teamId=:teamId and inTeam = '1' ";

        if (count >= 2) {
            return true;
        }
        return false;
    }

    /**
     * TODO - 判断用户是否有球队的最高权限
     *
     * @param param userId 用户 teamId 队伍Id
     * @return
     */
    private Boolean playerIsHighestRole(HashMap<String, String> param) throws Exception {
        //获取球员

        UPlayer uPlayer = this.uPlayerService.getPlayerByUserAndTeam(param);

        if (uPlayer == null) {
            return false;
        }

        String hql = "from UPlayerRole where UPlayer.playerId='" + uPlayer.getPlayerId() + "'";
        List<UPlayerRole> roles = this.baseDAO.find(hql);
        Integer lvl = this.getPlayerHighestLvl(roles);
        if (lvl == 1) {
            return true;
        }
        return false;
    }

    //球队新闻的操作权限
    private Boolean teamNewsOptionRole(UPlayer player) throws Exception {

        Boolean isTrue = false;
        String hql = "from UPlayerRole where UPlayer.playerId='" + player.getPlayerId() + "'and memberTypeUseStatus = '1'";
        List<UPlayerRole> roles = this.baseDAO.find(hql);
        if (CollectionUtils.isEmpty(roles))
            return false;
        for (UPlayerRole role : roles) {

            Integer lvl = Integer.parseInt(role.getMemberType());
            //队长 领队 总经理 主席 新闻官 拥有球员新闻的操作权限
            if (lvl == 8 || lvl == 14 || lvl == 1 || lvl == 3 || lvl == 13) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

    /**
     * TODO - 查询内嵌文档并返回 集合
     *
     * @param collection 表名
     * @param col        列名
     * @param db1        主文档的查询条件
     * @param db2        内嵌文档的查询条件
     * @param db3        排序
     * @param cls        内嵌文档的类型
     * @return
     */
    private List<Object> getInlineList(String collection, String col, BasicDBObject db1, BasicDBObject db2, BasicDBObject db3, Class cls) throws Exception {
        List<BasicDBObject> list = this.mongoDao.getListByZH(collection, db1, db2, db3);
        List<Object> retList = new ArrayList<Object>();
        for (BasicDBObject o : list) {
            List i = (List) o.get(col);
            retList.addAll(JSONArray.toCollection(JSONArray.fromObject(i), cls));
        }
        return retList;
    }

    //获取符合条件的内嵌文档的个数
    private Integer getInlineCount(String collection, String col, BasicDBObject db1, BasicDBObject db2, BasicDBObject db3) throws Exception {
        int count = 0;
        List<BasicDBObject> list = this.mongoDao.getListByZH(collection, db1, db2, db3);
        Object o = null;
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.get(0).get(col) != null) {
                count = ((List) list.get(0).get(col)).size();
            }
        }
        return count;
    }

    /**
     * TODO - 查询内嵌文档并返回 集合
     *
     * @param collection 表名
     * @param col        列名
     * @param db1        主文档的查询条件
     * @param db2        内嵌文档的查询条件
     * @param db3        排序
     * @param cls        内嵌文档的类型
     * @return
     */
    private Object getInlineObject(String collection, String col, BasicDBObject db1, BasicDBObject db2, BasicDBObject db3, Class cls) throws Exception {
        List<BasicDBObject> list = this.mongoDao.getListByZH(collection, db1, db2, db3);
        Object o = null;
        if (CollectionUtils.isNotEmpty(list)) {
            List<BasicDBObject> io = (List<BasicDBObject>) list.get(0).get(col);
            if (CollectionUtils.isNotEmpty(io)) {
                o = JSONObject.toBean(JSONObject.fromObject(io.get(0)), cls);
            }
        }
        return o;
    }

    /**
     * TODO - 获取最高权限的球员角色
     *
     * @return
     */
    private UPlayerRole getPlayerHighestRole(List<UPlayerRole> roles) {
        UPlayerRole role = null;
        int lvl = 3;
        for (UPlayerRole r : roles) {
            //如果权限没有角色类型
            if (StringUtils.isNotEmpty(r.getMemberType())) {
                int sl = this.getPlayerRoleLimitLvl(Integer.parseInt(r.getMemberType()));
                if (sl < lvl) {
                    lvl = sl;
                    role = r;
                }
            }
        }
        return role;
    }

    /**
     * TODO - 获取最高权限的球员角色
     *
     * @return
     */
    private Integer getPlayerHighestLvl(List<UPlayerRole> roles) {
        UPlayerRole role = null;
        int lvl = 3;
        for (UPlayerRole r : roles) {
            //如果权限没有角色类型
            if (StringUtils.isNotEmpty(r.getMemberType())) {
                int sl = this.getPlayerRoleLimitLvl(Integer.parseInt(r.getMemberType()));
                if (sl < lvl) {
                    lvl = sl;
                    role = r;
                }
            }
        }
        return lvl;
    }

    /**
     * TODO - 获取用户关注信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    private UFollow getFollowByUserAndType(HashMap<String, String> param) throws Exception {
        UFollow follow = this.baseDAO.get(param, " from UFollow where UUser.userId=:userId and userFollowType=:userFollowType and objectId=:objectId and followStatus='1' ");
        return follow;
    }

    /**
     * TODO 根据角色分配等级
     *
     * @param memberType xiaoying 2016年7月25日
     */
    private Integer getPlayerRoleLimitLvl(Integer memberType) {
        Integer lvl = 3;
//		1=队长、2=队员 3-总经理 4-财务后勤 5-主教练 6-助理教练 7-队医 8-新闻官 9-拉拉队员 10-赞助商 11-无角色 12-队务 13-主席 14-领队
        switch (memberType) {
            case 13:
                lvl = 1;
                break;
            case 1:
                lvl = 1;
                break;
            case 3:
                lvl = 1;
                break;
            case 14:
                lvl = 1;
                break;
            case 6:
                lvl = 2;
                break;
            case 4:
                lvl = 2;
                break;
            case 5:
                lvl = 2;
                break;
            case 10:
                lvl = 2;
                break;
            case 2:
                lvl = 3;
                break;
            case 9:
                lvl = 3;
                break;
            case 7:
                lvl = 3;
                break;
            case 8:
                lvl = 3;
                break;
            default:
                lvl = 3;
                break;
        }
        return lvl;

    }

    /**
     * TODO - 计算两个时间的时间差
     * 1. 1小时内，精确到分钟
     * 2. 超过1小时12小时内，精确到小时
     * 3. 超过12小时，显示年月日
     *
     * @param start
     * @param end
     * @return
     */
    private String dateButtonInfo(Date start, Date end) throws ParseException {
        Long ts = (end.getTime() - start.getTime()) / 1000;
        if (ts < 60) {
            return "1分钟前";
        } else if (ts < (60 * 60)) {
            return (ts / 60) + "分钟前";
        } else if (ts < (60 * 60 * 12)) {
            return ts / (60 * 60) + "小时前";
        } else {
            return PublicMethod.getDateToString(start, "yyyy-MM-dd");
        }
    }

    /**
     * 内嵌文档查询
     *
     * @param condition 查询条件 key为列 value为值
     * @param param     其他查询参数 col 需要查询的列 tableName 查询的表名 page当前页数 sort 排序列 sorts 顺序 1正序-1倒叙
     * @param cls       查询后返回的列表类型
     * @return
     */
    private List<Object> queryInline(HashMap<String, String> condition, HashMap<String, String> param, Class cls) throws Exception {


        List<Object> list = new ArrayList<Object>();

        List<DBObject> l = new ArrayList<DBObject>();
        // match匹配条件
        Map matchMap = new HashMap();
        for (String key : condition.keySet()) {
            matchMap.put(key, condition.get(key));
        }
        DBObject matchOption = new BasicDBObject("$match", matchMap);

        // project条件
        Map projectMap = new HashMap();
        projectMap.put(param.get("col"), param.get("$col"));
        BasicDBObject projectOption = new BasicDBObject("$project", projectMap);

        // $unwind条件
        BasicDBObject unwind = new BasicDBObject("$unwind", param.get("$col"));


        l.add(projectOption);
        l.add(unwind);
        l.add(matchOption);
        if (StringUtils.isNotEmpty(param.get("sort")) && StringUtils.isNotEmpty(param.get("sorts"))) {
            l.add(new BasicDBObject("$sort", new BasicDBObject(param.get("sort"), Integer.parseInt(param.get("sorts")))));
        } else {
            l.add(new BasicDBObject("$sort", new BasicDBObject("comment.createDate", -1)));
        }
        if (StringUtils.isNotEmpty(param.get("page"))) {
            int limit = StringUtils.isEmpty(param.get("limit")) ? Public_Cache.PAGE_LIMIT : Integer.parseInt(param.get("limit"));
            l.add(new BasicDBObject("$skip", Integer.parseInt(param.get("page")) * limit));
            l.add(new BasicDBObject("$limit", limit));
        }

        List ll = mongoDao.projectQuery(param.get("tableName"), l);
//        System.out.println(JSON.toJSONString(ll));
        if (cls != null) {
            for (Object o : ll) {
                BasicDBObject basicDBObject = (BasicDBObject) o;
                list.add(JSONObject.toBean(JSONObject.fromObject(basicDBObject.get(param.get("col"))), cls));
            }
            return list;
        } else {
            return ll;
        }
    }

    private List<Object> queryInlineNe(HashMap<String, Object> condition, HashMap<String, String> param, Class cls) throws Exception {


        List<Object> list = new ArrayList<Object>();

        List<DBObject> l = new ArrayList<DBObject>();
        // match匹配条件
        Map matchMap = new HashMap();
        for (String key : condition.keySet()) {
            matchMap.put(key, condition.get(key));
        }
        DBObject matchOption = new BasicDBObject("$match", matchMap);

        // project条件
        Map projectMap = new HashMap();
        projectMap.put(param.get("col"), param.get("$col"));
        BasicDBObject projectOption = new BasicDBObject("$project", projectMap);

        // $unwind条件
        BasicDBObject unwind = new BasicDBObject("$unwind", param.get("$col"));


        l.add(projectOption);
        l.add(unwind);
        l.add(matchOption);
        if (StringUtils.isNotEmpty(param.get("sort")) && StringUtils.isNotEmpty(param.get("sorts"))) {
            l.add(new BasicDBObject("$sort", new BasicDBObject(param.get("sort"), Integer.parseInt(param.get("sorts")))));
        } else {
            l.add(new BasicDBObject("$sort", new BasicDBObject("comment.createDate", -1)));
        }
        if (StringUtils.isNotEmpty(param.get("page"))) {
            l.add(new BasicDBObject("$skip", Integer.parseInt(param.get("page")) * Public_Cache.PAGE_LIMIT));
            l.add(new BasicDBObject("$limit", Public_Cache.PAGE_LIMIT));
        }

        List ll = mongoDao.projectQuery(param.get("tableName"), l);
//        System.out.println(JSON.toJSONString(ll));
        if (cls != null) {
            for (Object o : ll) {
                BasicDBObject basicDBObject = (BasicDBObject) o;
                list.add(JSONObject.toBean(JSONObject.fromObject(basicDBObject.get(param.get("col"))), cls));
            }
            return list;
        } else {
            return ll;
        }
    }


    //查询Or的查询条件
    private List<Object> queryInline(List query, HashMap<String, String> col, HashMap<String, String> param, Class cls) throws Exception {


        List<Object> list = new ArrayList<Object>();

        List<DBObject> l = new ArrayList<DBObject>();
        // match匹配条件
        DBObject matchOption =  null;
        matchOption = new BasicDBObject("$match", new BasicDBObject("$or", query));
//        if(list.size() > 1){
//            matchOption = new BasicDBObject("$match", new BasicDBObject("$or", query));
//        }else{
//            matchOption = new BasicDBObject("$match",query);
//        }


        BasicDBList ct = new BasicDBList();
        // project条件
        Map projectMap = new HashMap();
        for (String key : col.keySet()) {
            projectMap.put(key, col.get(key));
        }
        BasicDBObject projectOption = new BasicDBObject("$project", projectMap);

        // $unwind条件
        BasicDBObject unwind = new BasicDBObject("$unwind", "$comment");
        //$group条件,分组去重
        BasicDBObject colDBObject = new BasicDBObject("comment", "$comment");
        colDBObject.put("topicId", "$_id");
        BasicDBObject group = new BasicDBObject("$group", new BasicDBObject("_id", colDBObject));

//        l.add(new BasicDBObject("$sort", new BasicDBObject("comment.createDate", -1)));
        l.add(projectOption);
        l.add(unwind);
        l.add(matchOption);
//        l.add(group); //分组去重。。。感觉不用也不会重复。。。暂时不用。。如果出现问题再使用
        if (StringUtils.isNotEmpty(param.get("sort")) && StringUtils.isNotEmpty(param.get("sorts"))) {
            l.add(new BasicDBObject("$sort", new BasicDBObject(param.get("sort"), Integer.parseInt(param.get("sorts")))));
        }
        if (StringUtils.isNotEmpty(param.get("page"))) {
            l.add(new BasicDBObject("$skip", Integer.parseInt(param.get("page")) * Public_Cache.PAGE_LIMIT));
            l.add(new BasicDBObject("$limit", Public_Cache.PAGE_LIMIT));
        }

        List ll = mongoDao.projectQuery(param.get("tableName"), l);
//        System.out.println(JSON.toJSONString(ll));
        if (cls != null) {
            for (Object o : ll) {
                BasicDBObject basicDBObject = (BasicDBObject) o;
                list.add(JSONObject.toBean(JSONObject.fromObject(basicDBObject.get(param.get("col"))), cls));
            }
            return list;
        } else {
            return ll;
        }
    }

    /**
     * TODO - 格式化前台传过来的话题对象
     *
     * @param param
     * @param cls
     * @return
     */
    public Object jsonToTopic(HashMap<String, String> param, Class cls) {
        if (StringUtils.isEmpty(param.get("topic"))) {
            throw new RuntimeException("未找到话题对象");
        }
        HashMap<String, Class> clsM = new HashMap<String, Class>();
        clsM.put("imgUrl", TopicImg.class);
        Object object = SerializeUtil.unSerializeToBean(param.get("topic"), cls);
        return object;
    }

    //把JSON格式转换为TopicComment
    public TopicComment jsonToComment(HashMap<String, String> param) {
        if (StringUtils.isEmpty(param.get("comment"))) {
            throw new RuntimeException("未找到话题评论对象");
        }
        return (TopicComment) SerializeUtil.unSerializeToBean(param.get("comment"), TopicComment.class);
    }

    /**
     * TODO - 通过Token或者player获取用户对象
     *
     * @param param token playerId
     * @return
     */
    public UUser getUUserByTokenOrPlayerId(HashMap<String, String> param) throws Exception {
        UUser uUser = null;
        if (StringUtils.isNotEmpty(param.get("token"))) {
            uUser = this.uUserService.getUserinfoByToken(param);
        } else if (StringUtils.isNotEmpty(param.get("playerId"))) {
            uUser = this.uUserService.getUUserByPlayerId(param);
        }
        return uUser;
    }

    //修改文档某列的值 table 表名 countCol 获取个数的列名 col 修改的列名
    private void updateCol(Integer count, Query query, String col, Class cls) throws Exception {
        Update update = Update.update(col, count);
        this.mongoDao.update(query, update, cls);
    }

    //获取最新的评论
    private List<TopicCommentVo> getNewCommentList(HashMap<String, String> param) throws Exception {
        //添加查询条件
        HashMap<String, String> query = new HashMap<String, String>();
//        query.put("", param.get("contentId"));
        query.put("_id", param.get("topicId"));
        query.put("comment.commentType", "1");
        query.put("comment.delType", "1");
        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("page", "0");
        param.put("col", "comment");
        param.put("$col", "$comment");
        List<TopicCommentVo> retList = new ArrayList<TopicCommentVo>();
        List<Object> list = this.queryInline(query, param, TopicComment.class);
        for (Object obj : list) {
            TopicComment comment = (TopicComment) obj;
            TopicCommentVo cc = this.formatterTopicComment(comment);
            if (cc.getUser() == null)
                break;
            retList.add(cc);
        }
        return retList;
    }

    //获取最新的点赞
    private List<TopicThumbsVo> getNewThumbsList(HashMap<String, String> param) throws Exception {
        //添加查询条件
        HashMap<String, String> query = new HashMap<String, String>();
//        query.put("", param.get("contentId"));
        query.put("_id", param.get("topicId"));
        query.put("comment.commentType", "2");
        query.put("comment.delType", "1");
        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("page", "0");
        param.put("col", "comment");
        param.put("$col", "$comment");
        param.put("limit", thumbsCount.toString());
        List<TopicThumbsVo> retList = new ArrayList<TopicThumbsVo>();
        List<Object> list = this.queryInline(query, param, TopicComment.class);
        for (Object obj : list) {
            TopicComment thumbs = (TopicComment) obj;
            retList.add(this.formatterTopicThumbs(thumbs));
        }
        return retList;
    }

    //发送消息通知
    private void sendTopicMsg(String type, String mes_type, String contentName, String params, String userId) throws Exception {
        HashMap<String, String> sendMap = new HashMap<String, String>();
        sendMap.put("type", type);
        sendMap.put("mes_type", mes_type);
        sendMap.put("contentName", contentName);
        sendMap.put("params", params);
        sendMap.put("userId", userId);
        this.messageService.addTheMessageByType(sendMap);
    }

    //消息推送
    private void sendTopicPush(UUser user, String content, String type, String jump) throws Exception {
        String numberId = user.getNumberid();
        //如果设备id为空则无法发送推送
        if (StringUtils.isEmpty(numberId))
            return;
        UEquipment uEquipment = baseDAO.get("from UEquipment where keyId='" + numberId + "'");
        if (uEquipment == null)
            return;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("code", uEquipment.getCode());
        map.put("mes_type", type);
        map.put("jump", jump);
        map.put("content", content);

        this.publicPushService.publicAppPush(map);
    }


    //获取用户未读信息列表
    @Override
    public HashMap<String, Object> getTopicUnreadListPage(HashMap<String, String> map) throws Exception {

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        } else {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        if (map.get("type") == null) {
            map.put("type", "");
        }
        //为球员话题时获取用户Id
        if ("1".equals(map.get("type"))) {
            map.put("playerId", map.get("objectId"));
            UUser fUser = this.uUserService.getUUserByPlayerId(map);
            if (fUser == null) {
                return WebPublicMehod.returnRet("error", "未找到球员用户");
            }
            map.put("objectId", fUser.getUserId());
        }
        List<MyCommentVo> list = this.getTopicUnreadList(map);
        //修改未读消息状态为已读
        map.remove("page");
        this.readTopicComment(map);

        return WebPublicMehod.returnRet("commentList", list);
    }

    //获取某个用户 在 某个类型下话题的未读回复
    private List<Object> getTopicUnread(String type, String userId, String objectId, String page, String category) throws Exception {
        HashMap<String, String> param = new HashMap<String, String>();

        String tableName = this.getTopicTableNameByType(type);
//        Class cls = this.getTopicClassByType(type);
        String typeCol = this.getTopicObjectName(type);

        HashMap<String, String> query = new HashMap<String, String>();
        query.put("comment.buserId", userId);
//        query.put("comment.delType", "1");
        query.put("comment.readType", "1");
        if (StringUtils.isNotEmpty(objectId))
            query.put(typeCol, objectId);
        if (StringUtils.isNotEmpty(category))
            query.put("category", category);

        HashMap<String, Object> query1 = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(userId))
            query1.put("userId", userId);
//        query1.put("comment.delType", "1");
        query1.put("comment.readType", "1");
        query1.put("comment.userId", new BasicDBObject("$ne", userId));
        if (StringUtils.isNotEmpty(objectId))
            query1.put(typeCol, objectId);
        if (StringUtils.isNotEmpty(category))
            query1.put("category", category);

        List queryOr = new ArrayList();

        queryOr.add(query1);
        queryOr.add(query);


        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");

        HashMap<String, String> col = new HashMap<String, String>();
        col.put("comment", "$comment");
        col.put("userId", "$userId");
        if (StringUtils.isNotEmpty(category))
            col.put("category", "$category");
        col.put(typeCol, "$".concat(typeCol));
        col.put("content", "$content");

        param.put("tableName", tableName);

        if (StringUtils.isNotEmpty(page)) {
            param.put("page", page);
        }

        List<Object> list = this.queryInline(queryOr, col, param, null);
        return list;
    }

    //获取用户在某条话题下的未读回复
    private List<Object> getTopicUnread(String type, String userId, String topicId, String page, Boolean isMyTopic) throws Exception {
        HashMap<String, String> param = new HashMap<String, String>();

        String tableName = this.getTopicTableNameByType(type);

        HashMap<String, Object> query = new HashMap<String, Object>();

        //空位时为查询个人中心
        if (StringUtils.isNotEmpty(topicId))
            query.put("_id", topicId);


        if (isMyTopic) {
//            query.put("comment.delType", "1");
            query.put("comment.readType", "1");
//            if (StringUtils.isNotEmpty(type))
            query.put("comment.userId", new BasicDBObject("$ne", userId));
        } else {
            query.put("comment.buserId", userId);
//            query.put("comment.delType", "1");
            query.put("comment.readType", "1");
        }

        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
        param.put("col", "comment");
        param.put("$col", "$comment");
        if (StringUtils.isNotEmpty(page))
            param.put("page", page);


        param.put("tableName", tableName);

        List<Object> list = this.queryInlineNe(query, param, null);
        return list;
    }

    //未读消息的列表
    private List<MyCommentVo> getTopicUnreadList(HashMap<String, String> param) throws Exception {

        List<MyCommentVo> comments = new ArrayList<MyCommentVo>();
        MyCommentVo comment = null;
        List<Object> list = this.getUnreadCommentList(param);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                JSONObject jo = JSONObject.fromObject(object);
                comment = this.formatterMyComment((TopicComment) JSONObject.toBean(JSONObject.fromObject(jo.get("comment")), TopicComment.class));
                comment.setTopicId(String.valueOf(jo.get("_id")));
                comment.setTopicContent(String.valueOf(jo.get("content")));
                this.setMyTopicCommentInfo(comment.getTopicId(), comment);
                comments.add(comment);
            }
        }

        return comments;
    }

    //修改消息状态
    private void readTopicComment(HashMap<String, String> param) throws Exception {
        //获取所有未读评论
        List<Object> list = this.getUnreadCommentList(param);
        //获取所有未读评论的id
        List<String> commentIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                JSONObject jo = JSONObject.fromObject(object);
                TopicComment comment = (TopicComment) JSONObject.toBean(JSONObject.fromObject(jo.get("comment")), TopicComment.class);
                if (comment != null) {
                    commentIds.add(comment.getContentId());
                }

            }
        }
        //修改未读回复的状态
        this.updateReadStatus(param, commentIds);
    }

    //获取未读消息类型
    private List<Object> getUnreadCommentList(HashMap<String, String> param) throws Exception {

        String unreadType = param.get("unreadType") == null ? "" : param.get("unreadType");

        List<Object> list = null;
        switch (unreadType) {
            //话题列表未读
            case "1":
                list = this.getTopicUnread(param.get("type"), param.get("userId"), param.get("objectId"), param.get("page"), param.get("category"));
                break;
            case "2":
                list = this.getTopicUnread(param.get("type"), param.get("userId"), param.get("topicId"), param.get("page"), param.get("fUserId").equals(param.get("userId")));
                break;
            case "3":
                list = this.getTopicUnread("", param.get("userId"), "", param.get("page"), true);
                break;
            case "4":
                list = this.getTopicUnread("", param.get("userId"), "", param.get("page"), false);
                break;
            case "5":
                list = this.getTopicUnread("", param.get("userId"), null, null, "");
                break;
            case "6":
                list = this.getTopicUnread(param.get("type"), null, param.get("objectId"), param.get("page"), param.get("category"));
                break;
            default:
                break;
        }
        return list;
    }

    //未读消息的个数
    private Integer getTopicUnreadListNum(HashMap<String, String> param) throws Exception {
        List<Object> list = this.getTopicUnread(param.get("type"), param.get("userId"), param.get("objectId"), null, param.get("category"));
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }


    /*********************
     * 公共话题
     ************************/


    //查询公共话题列表
    @Override
    public HashMap<String, Object> getListTopicListPage(HashMap<String, String> param) throws Exception {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(param.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        //获取当前登录的用户id

        UUser uUser = this.uUserService.getUserinfoByToken(param);
        if (uUser != null) {
            param.put("userId", uUser.getUserId());
        }

        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(param.get("userId"))) {
            Boolean isMyTopic = true;
            List<Object> l = this.getTopicUnread("", param.get("userId"), "", null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }


        //判断当前登录用户是否有发起约战话题的权限
        retMap.put("isRelease", true);

        Query query = Query.query(Criteria.where("userId").is(param.get("userId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UListTopic> list = this.mongoDao.findPage(Integer.parseInt(param.get("page")), Public_Cache.PAGE_LIMIT, query, UListTopic.class);
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        List<TopicThumbsVo> thumbsList = null;
        List<TopicCommentVo> commentList = null;

        for (UListTopic topic : list) {
            topicVo = this.formatterTopic(topic);
            topicVo.setIsDelete(true);
            topicVo.setIsComment(true);
            topicVo.setObjectId(topic.getObjectId());
            topicVo.setIsThumbs(this.isThumbs(topic.get_id(), param.get("userId"), "u_list_topic"));
//            topicVo.setIsThumbs(this.isThumbs(topic.getZambia(),map.get("userId")));
            topicVo.setType(topic.getTopic_obj());
            topicVo.setCategory(topic.getCategory());
            topicVo.setTopicSource(this.getTopicSource(topic.getObjectId(), topic.getTopic_obj()));
            param.put("topicId", topic.get_id());
            param.put("tableName", "u_list_topic");
            //评论相关
//            topicVo.setComment(this.getNewCommentList(param));

            //点赞相关
//            topicVo.setThumbs(this.getNewThumbsList(param));
//            topicVo.setThumbs(thumbsList);

            tv.add(topicVo);
        }
        retMap.put("topicList", tv);
        return retMap;

    }

    //查询公共话题评论
    @Override
    public HashMap<String, Object> getListTopicCommentPage(HashMap<String, String> param) throws Exception {

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(param.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(param);
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            return WebPublicMehod.returnRet("error", "未找到用户");
        }

        param.put("userId", user.getUserId());
        //未读回复数
        retMap.put("unreadNum", "0");
        if (StringUtils.isNotEmpty(param.get("userId"))) {
            Boolean isMyTopic = false;
            List<Object> l = this.getTopicUnread("", param.get("userId"), "", null, isMyTopic);
            retMap.put("unreadNum", CollectionUtils.isEmpty(l) ? 0 : l.size());
        }


        HashMap<String, String> query = new HashMap<String, String>();
//        query.put("_id", param.get("topicId"));
        query.put("comment.userId", user.getUserId());
        query.put("comment.delType", "1");
        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");

        List<Object> queryList = new ArrayList<Object>();
        queryList.add(query);
//        param.put("page", "0");
//        param.put("col", "comment");
//        param.put("$col", "$comment");
        HashMap<String, String> colMap = new HashMap<String, String>();
        colMap.put("comment", "$comment");
        colMap.put("content", "$content");

        param.put("tableName", "u_list_topic");
        MyCommentVo comment = null;
        List<Object> list = this.queryInline(queryList, colMap, param, null);
        List<MyCommentVo> comments = new ArrayList<MyCommentVo>();
        for (Object object : list) {
            JSONObject jo = JSONObject.fromObject(object);
            comment = this.formatterMyComment((TopicComment) JSONObject.toBean(JSONObject.fromObject(jo.get("comment")), TopicComment.class));
            comment.setTopicId(String.valueOf(jo.get("_id")));
            comment.setTopicContent(String.valueOf(jo.get("content")));
            this.setMyTopicCommentInfo(comment.getTopicId(), comment);
            comments.add(comment);
        }


        retMap.put("comments", comments);
        return retMap;
    }

    //获取我的未读的消息
    private List<Object> getNewTopicInfo(HashMap<String, String> param, UUser user) throws Exception {
        HashMap<String, String> query = new HashMap<String, String>();
//        query.put("_id", param.get("topicId"));
        query.put("comment.buserId", user.getUserId());
//        query.put("userId", user.getUserId());
        query.put("comment.delType", "1");
        query.put("comment.readType", "1");

        HashMap<String, String> query1 = new HashMap<String, String>();
//        query.put("comment.buserId", user.getUserId());
        query1.put("userId", user.getUserId());
        query1.put("comment.delType", "1");
        query1.put("comment.readType", "1");

        List queryOr = new ArrayList();

        queryOr.add(query1);
        queryOr.add(query);


        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
//        param.put("page", "0");
        HashMap<String, String> col = new HashMap<String, String>();
        col.put("comment", "$comment");
        col.put("userId", "$userId");


        param.put("tableName", "u_list_topic");
        List<Object> list = this.queryInline(queryOr, col, param, null);
        return list;
    }

    //查询所有关于我未读的话题
    @Override
    public HashMap<String, Object> getNewListTopicCommentPage(HashMap<String, String> param) throws Exception {

        if (StringUtils.isEmpty(param.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(param);
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            return WebPublicMehod.returnRet("error", "未找到用户");
        }

        List<MyCommentVo> comments = new ArrayList<MyCommentVo>();
        MyCommentVo comment = null;
        List<Object> list = this.getNewTopicInfo(param, user);
        for (Object object : list) {
            JSONObject jo = JSONObject.fromObject(object);
            comment = this.formatterMyComment((TopicComment) JSONObject.toBean(JSONObject.fromObject(jo.get("comment")), TopicComment.class));
            comment.setTopicId(String.valueOf(jo.get("_id")));
            this.setMyTopicCommentInfo(comment.getTopicId(), comment);
            comments.add(comment);
        }


        HashMap<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("comments", comments);
        return retMap;
    }

    //获取关于我的未读信息个数
    @Override
    public HashMap<String, Object> getNewListTopicComment(HashMap<String, String> param) throws Exception {
        if (StringUtils.isEmpty(param.get("token"))) {
            return WebPublicMehod.returnRet("error", "token为空");
        }
        UUser user = this.uUserService.getUserinfoByToken(param);
        if (user == null || StringUtils.isEmpty(user.getUserId())) {
            return WebPublicMehod.returnRet("error", "未找到用户");
        }

        param.remove("page");

        List<Object> list = this.getNewTopicInfo(param, user);

        return WebPublicMehod.returnRet("newMsg", CollectionUtils.isEmpty(list) ? 0 : list.size());
    }

    @Override
    public HashMap<String, Object> removeTopic(HashMap<String, String> map) throws Exception {
        String type = map.get("type") == null ? "" : map.get("type");
        HashMap<String, Object> retMap = null;

        switch (type) {
            case "1":
                retMap = this.deleteUserTopic(map);
                break;
            case "2":
                retMap = this.deleteDuelTopic(map);
                break;
            case "3":
                retMap = this.deleteChallengeTopic(map);
                break;
            case "4":
                retMap = this.deleteTeamTopic(map);
                break;
            case "5":
                retMap = this.deleteMatchTopic(map);
                break;
            case "6":
                retMap = this.deleteCourtTopic(map);
                break;
            default:
                retMap = WebPublicMehod.returnRet("error", "type为空");
                break;
        }

        return retMap;
    }

    @Override
    public HashMap<String, Object> saveTopicThumbs(HashMap<String, String> map) throws Exception {
        String type = map.get("type") == null ? "" : map.get("type");
        HashMap<String, Object> retMap = null;

        switch (type) {
            case "1":
                retMap = this.saveUserTopicThumbs(map);
                break;
            case "2":
                retMap = this.saveDuelTopicThumbs(map);
                break;
            case "3":
                retMap = this.saveChallengeTopicThumbs(map);
                break;
            case "4":
                retMap = this.saveTeamTopicThumbs(map);
                break;
            case "5":
                retMap = this.saveMatchTopicThumbs(map);
                break;
            case "6":
                retMap = this.saveCourtTopicThumbs(map);
                break;
            default:
                retMap = WebPublicMehod.returnRet("error", "type为空");
                break;
        }

        return retMap;
    }

    @Override
    public HashMap<String, Object> removeTopicThumbs(HashMap<String, String> map) throws Exception {
        String type = map.get("type") == null ? "" : map.get("type");
        HashMap<String, Object> retMap = null;

        switch (type) {
            case "1":
                retMap = this.removeUserTopicThumbs(map);
                break;
            case "2":
                retMap = this.removeDuelTopicThumbs(map);
                break;
            case "3":
                retMap = this.removeChallengeTopicThumbs(map);
                break;
            case "4":
                retMap = this.removeTeamTopicThumbs(map);
                break;
            case "5":
                retMap = this.removeMatchTopicThumbs(map);
                break;
            case "6":
                retMap = this.removeCourtTopicThumbs(map);
                break;
            default:
                retMap = WebPublicMehod.returnRet("error", "type为空");
                break;
        }

        return retMap;
    }

    @Override
    public HashMap<String, Object> updateReadType(HashMap<String, String> map) throws Exception {

        map.put("type", map.get("type") == null ? "" : map.get("type"));

        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        } else {
            return WebPublicMehod.returnRet("error", "token为空");
        }

        //为球员话题时获取用户Id
        if ("1".equals(map.get("type"))) {
            map.put("playerId", map.get("objectId"));
            UUser fUser = this.uUserService.getUUserByPlayerId(map);
            if (fUser == null) {
                return WebPublicMehod.returnRet("error", "未找到球员用户");
            }
            map.put("objectId", fUser.getUserId());
        }

        this.readTopicComment(map);

        return WebPublicMehod.returnRet("success", "成功");
    }


    //修改回复的未读状态
    private void updateReadStatus(HashMap<String, String> param, List<String> commentIds) throws Exception {

        for (String commentId : commentIds) {
            BasicDBObject o1 = new BasicDBObject("comment.contentId", commentId);
            BasicDBObject update = new BasicDBObject();
//        update.put("comment.$.createDate",new Date());
            update.put("comment.$.readType", "2");
            BasicDBObject o2 = new BasicDBObject("$set", update);
            this.mongoDao.updateTag("u_list_topic", o1, o2, false, true);
            //修改5张表对应数据
            switch (param.get("type")) {
                case "1":
                    this.mongoDao.updateTag("u_user_topic", o1, o2, false, true);
                    break;
                case "2":
                    this.mongoDao.updateTag("u_duel_topic", o1, o2, false, true);
                    break;
                case "3":
                    this.mongoDao.updateTag("u_challage_topic", o1, o2, false, true);
                    break;
                case "4":
                    this.mongoDao.updateTag("u_team_topic", o1, o2, false, true);
                    break;
                case "5":
                    this.mongoDao.updateTag("u_match_topic", o1, o2, false, true);
                    break;
                case "6":
                    this.mongoDao.updateTag("u_court_topic", o1, o2, false, true);
                default:
                    this.mongoDao.updateTag("u_user_topic", o1, o2, false, true);
                    this.mongoDao.updateTag("u_duel_topic", o1, o2, false, true);
                    this.mongoDao.updateTag("u_challage_topic", o1, o2, false, true);
                    this.mongoDao.updateTag("u_team_topic", o1, o2, false, true);
                    this.mongoDao.updateTag("u_match_topic", o1, o2, false, true);
                    this.mongoDao.updateTag("u_court_topic", o1, o2, false, true);
                    break;

            }
        }

    }

    //保存话题查询表话题，object 对应的 用户，挑战，约战，球队，赛事 id，type 1用户 2挑战 3约战 4球队 5赛事
    private void saveTopicList(UTopic topic, String objectId, String type) throws Exception {
        UListTopic tList = new UListTopic(topic, objectId, type);
        this.mongoDao.save(tList);
    }

    //保存话题查询表回复
    private void saveTopicListComment(TopicComment comment, Query query, BasicDBObject dbo1) throws Exception {
        this.mongoDao.saveAs(query, UListTopic.class, "comment", comment);
    }

    //删除话题查询表话题回复
    private void deleteTopicListComment(BasicDBObject o1, BasicDBObject o2) throws Exception {
        this.mongoDao.updateTag("u_list_topic", o1, o2, false, true);
    }

    //保险话题查询表点赞评论总数
    private void saveTopicListThumbs(TopicComment thumbs, Query query, BasicDBObject dbo1) throws Exception {
        this.mongoDao.saveAs(query, UListTopic.class, "comment", thumbs);
    }

    //删除话题查询表点赞数据
    private void deleteTopicListThumbs(BasicDBObject o1, BasicDBObject o2) throws Exception {
        this.mongoDao.updateTag("u_list_topic", o1, o2, false, false);
    }


    /*************************
     * 以下为分享部分
     *******************************/
    @Override
    public HashMap<String, Object> getShareTopicList(HashMap<String, String> map) throws Exception {

        HashMap<String, Object> retMap = new HashMap<String, Object>();
        if (map.get("type") == null) {
            map.put("type", "");
        }
        String type = map.get("type");
        retMap.put("info", this.getTopicObjectInfo(map));

        map.put("type", type);
        Query query = this.getQueryObj(map);
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        List<UTopic> list = this.mongoDao.findPage(0, Public_Cache.PAGE_LIMIT, query, this.getTopicClassByType(map.get("type")));
        List<TopicVo> tv = new ArrayList<TopicVo>();
        TopicVo topicVo = null;
        for (UTopic topic : list) {
            //当前话题发布人
            map.put("tUserId", topic.getUserId());
            topicVo = this.formatterTopic(topic);
            tv.add(topicVo);
        }

        retMap.put("topicList", tv);
        return retMap;
    }

    @Override
    public HashMap<String, Object> getShareTopicDetail(HashMap<String, String> map) throws Exception {

        map.put("type", map.get("type") == null ? "" : map.get("type"));

        HashMap<String, Object> retMap = new HashMap<String, Object>();


        Query query = Query.query(Criteria.where("_id").is(map.get("topicId")));
        UTopic topic = this.mongoDao.findOne(query, this.getTopicClassByType(map.get("type")));
        if (topic == null) {
            return WebPublicMehod.returnRet("error", "未找到话题信息");
        }


        //获取当前登录的用户id
        if (StringUtils.isNotEmpty(map.get("token"))) {
            UUser uUser = this.uUserService.getUserinfoByToken(map);
            if (uUser != null) {
                map.put("userId", uUser.getUserId());
            }
        }

        TopicVo tv = this.formatterTopic(topic);

        retMap.put("topic", tv);


        //话题评论列表
        List<TopicCommentVo> retList = new ArrayList<TopicCommentVo>();
        List<TopicCommentVo> childCommentVo = null;
        List<Object> childComment = null;
        map.put("tableName", this.getTopicTableNameByType(map.get("type")));
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("comment.firstContentId", "-1");
        queryParam.put("comment.delType", "1");
        queryParam.put("_id", map.get("topicId"));
        map.put("sort", "comment.createDate");
        map.put("sorts", "-1");
        map.put("col", "comment");
        map.put("$col", "$comment");
        //查询所有回复话题的评论
        List<Object> comments = this.queryInline(queryParam, map, TopicComment.class);
        for (Object cObj : comments) {

            TopicComment c = (TopicComment) cObj;
            TopicCommentVo commentVo = this.formatterTopicComment(c);
            if (commentVo.getUser() == null)
                break;
            queryParam.put("comment.firstContentId", c.getContentId());
            map.remove("page");
            //查询所有针对话题评论的评论
            childComment = this.queryInline(queryParam, map, TopicComment.class);
            commentVo.setCount(childComment.size());
            childCommentVo = new ArrayList<TopicCommentVo>();
            int count = 1;
            for (Object ccObj : childComment) {
                TopicCommentVo cc = this.formatterTopicComment((TopicComment) ccObj);
                if (cc.getUser() == null)
                    break;
                childCommentVo.add(cc);
                if (count == Public_Cache.PAGE_LIMIT) {
                    break;
                }
                count++;
            }
            commentVo.setComment(childCommentVo);
            retList.add(commentVo);
        }

        retMap.put("commentList", retList);
        return retMap;


    }

    //获取分享话题头部信息
    private HashMap<String, Object> getTopicObjectInfo(HashMap<String, String> param) throws Exception {

        switch (param.get("type")) {
            case "1":
                param.put("playerId", param.get("objectId"));
                return uPlayerService.roughlyStateOfUPlayerHead(param);
            case "2":
                param.put("duelId", param.get("objectId"));
                return uDuelService.getDuelInfo(param);
            case "3":
                param.put("challengeId", param.get("objectId"));
                param.put("challengeType", "1");
                return uChallengeService.getChallengeAndBs(param);
            case "4":
                param.put("teamId", param.get("objectId"));
                return uTeamService.findUteaminfoHead(param);
            case "5":
                param.put("bsId", param.get("objectId"));
                return uMatchService.getUMatchBs(param);
            case "6":
                param.put("subcourt_id", param.get("objectId"));
                return uCourtService.queryCourtDetail(param);
            default:
                return null;

        }
    }

    //查询话题对应表和字段的query信息
    private Query getQueryObj(HashMap<String, String> param) throws Exception {
        String colName = "";
        switch (param.get("type")) {
            case "1":
                param.put("playerId", param.get("objectId"));
                //获取要查询的球员用户Id
                UUser fUser = this.uUserService.getUUserByPlayerId(param);
                if (fUser == null) {
                    throw new RuntimeException("未找到球员信息");
                }
                return Query.query(Criteria.where("cuserId").is(fUser.getUserId()));
            case "2":
                colName = "cduelId";
                break;
            case "3":
                colName = "challageId";
                break;
            case "4":
                colName = "cteamId";
                break;
            case "5":
                colName = "cmatchId";
                break;
            case "6":
                colName = "courtId";
                break;
            default:
                colName = "objectId";
                break;
        }


        Query query = Query.query(Criteria.where(colName).is(param.get("objectId")));
        ;

        return query;
    }

    //获取不同话题表中话题管理id的字段信息
    private String getTopicObjectName(String type) throws Exception {
        String colName = "";
        switch (type) {
            case "1":

                return "cuserId";
            case "2":
                colName = "cduelId";
                break;
            case "3":
                colName = "challageId";
                break;
            case "4":
                colName = "cteamId";
                break;
            case "5":
                colName = "cmatchId";
                break;
            case "6":
                colName = "courtId";
                break;
            default:
                colName = "objectId";
                break;
        }
        return colName;
    }


    //通过类型获取对应的实体Class对象
    private Class getTopicClassByType(String type) {
        switch (type) {
            case "1":
                return UUserTopic.class;
            case "2":
                return UDuelTopic.class;
            case "3":
                return UChallageTopic.class;
            case "4":
                return UTeamTopic.class;
            case "5":
                return UMatchTopic.class;
            case "6":
                return UCourtTopic.class;
            default:
                return UListTopic.class;

        }
    }

    //通过类型获取对应的实体表名
    private String getTopicTableNameByType(String type) {
        switch (type) {
            case "1":
                return "u_user_topic";
            case "2":
                return "u_duel_topic";
            case "3":
                return "u_challage_topic";
            case "4":
                return "u_team_topic";
            case "5":
                return "u_match_topic";
            case "6":
                return "u_court_topic";
            default:
                return "u_list_topic";

        }
    }

    //获取用户最新发布的
    private Boolean isReleaseTopicTime(HashMap<String, String> param) throws Exception {
        UTopic topic = this.getUserNewTopic(param, this.getTopicClassByType(param.get("type")));
        if (topic == null)
            return true;
        return this.calcReleaseTeam(topic.getCreateTime());
    }

    private UTopic getUserNewTopic(HashMap<String, String> param, Class cls) throws Exception {
        UTopic topic = null;
        Query query = Query.query(Criteria.where("userId").is(param.get("userId")));
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "createTime")));
        topic = (UTopic) this.mongoDao.findOne(query, cls);
        return topic;
    }

    //获取用户最新发布的话题回复
    private TopicComment getUserNewTopicComment(HashMap<String, String> param) throws Exception {

//        BasicDBObject dbo1 = new BasicDBObject(); //主要条件查询
//        BasicDBObject dbo2 = new BasicDBObject(); //内嵌分页
//        BasicDBObject dbo3 = new BasicDBObject(); //排序
//        dbo3.put("comment.createDate", -1);
////        dbo1.put("userId", "af33962c-4077-4195-b41d-e52788b10780");
//        dbo2.put("comment", new BasicDBObject("$elemMatch", new BasicDBObject("userId",param.get("userId"))));
//        //        List l = mongoDao.getListByZH("u_user_topic", dbo1, dbo2,dbo3);
//        TopicComment comm =(TopicComment) this.getInlineObject(param.get("tableName"), "comment", null, dbo2, dbo3, TopicComment.class);
//        return comm;


        HashMap<String, String> query = new HashMap<String, String>();
//        query.put("_id", param.get("topicId"));
        query.put("comment.userId", param.get("userId"));
//        query.put("comment.delType", "1");
        //添加排序条件
        param.put("sort", "comment.createDate");
        param.put("sorts", "-1");
//        param.put("page", "0");
        param.put("col", "comment");
        param.put("$col", "$comment");
        param.put("page", "0");

        param.put("tableName", param.get("tableName"));
        List<Object> list = this.queryInline(query, param, TopicComment.class);

        if (list == null || CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (TopicComment) list.get(0);

    }

    //计算最后发布的时间是否与当前时间间隔是否合法
    private Boolean calcReleaseTeam(Date lastDate) {
        return (new Date().getTime() - lastDate.getTime()) / 1000 > second;
    }


    //获取球队
    private UUser getUserInfoByUserId(String userId) throws Exception {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userId", userId);
        UUser bUser = this.uUserService.getUserinfoByUserId(param);

        if (bUser == null || bUser.getUserId() == null) {
            return null;
        }
        return bUser;
    }

    //获取球队管理角色
    private List<UPlayer> getTeamCaptain(String teamId) throws Exception {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("teamId", teamId);
        List<UPlayer> players = this.uPlayerService.getTeamManagePlayer(param);
        return players;
    }


    //当前用户是否有话题的未读消息
    @Override
    public Boolean isTopicUnreadComment(String userId) throws Exception {
        List<Object> list = getTopicUnread("", userId, null, null, "");
        return CollectionUtils.isNotEmpty(list);
    }

    @Override
    public HashMap<String, Object> copyDuelTopicList(HashMap<String, String> map) throws Exception {
        List<UDuelTopic> list = this.mongoDao.find(Query.query(Criteria.where("cduelId").is(map.get("formDuelId"))), UDuelTopic.class);
        if(CollectionUtils.isEmpty(list))return null;
        for (UDuelTopic topic : list) {
            UDuelTopic copyTopic = new UDuelTopic();
            BeanUtils.copyProperties(copyTopic, topic);
            copyTopic.setCduelId(map.get("duelId"));
            copyTopic.set_id(WebPublicMehod.getUUID());
            List<Object> oc = (List) copyTopic.getComment();
            for (Object o : oc) {
                TopicComment comment = (TopicComment) o;
                comment.setContentId(WebPublicMehod.getUUID());
            }
            this.mongoDao.save(copyTopic);
            //插入查询表数据
            this.saveTopicList(copyTopic, copyTopic.getCduelId(), "2");
        }
        //置换取消掉的评论状态
        this.updateObjectTypeUnreadType("2", map.get("objectId"));
        return null;
    }

    @Override
    public HashMap<String, Object> copyChallengeTopicList(HashMap<String, String> map) throws Exception {
        List<UChallageTopic> list = this.mongoDao.find(Query.query(Criteria.where("challageId").is("formChallengeId")), UChallageTopic.class);
        if(CollectionUtils.isEmpty(list))return null;
        for (UChallageTopic topic : list) {
            UChallageTopic copyTopic = new UChallageTopic();
            BeanUtils.copyProperties(copyTopic, topic);
            copyTopic.setChallageId(map.get("duelId"));
            copyTopic.set_id(WebPublicMehod.getUUID());
            List<Object> oc = (List) copyTopic.getComment();
            for (Object o : oc) {
                TopicComment comment = (TopicComment) o;
                comment.setContentId(WebPublicMehod.getUUID());
            }
            this.mongoDao.save(copyTopic);
            //插入查询表数据
            this.saveTopicList(copyTopic, copyTopic.getChallageId(), "3");
        }
        //置换取消掉的评论状态
        this.updateObjectTypeUnreadType("3", map.get("objectId"));
        return null;
    }


    private void updateObjectTypeUnreadType(String type, String objectId) throws Exception {
        String tableName = this.getTopicTableNameByType(type);
        List<Object> list = this.getTopicUnread(type, null, objectId, "", "");
        TopicComment comment = null;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object object : list) {
                JSONObject jo = JSONObject.fromObject(object);
                comment = (TopicComment) JSONObject.toBean(JSONObject.fromObject(jo.get("comment")), TopicComment.class);
                BasicDBObject update = new BasicDBObject();
                update.append("comment.$.readType", "2");
//                update.append("comment.$.createDate", new Date());
                BasicDBObject cDb1 = new BasicDBObject();
//                cDb1.append("_id", topic.get_id());
                cDb1.append("comment.contentId", comment.getContentId());
                this.mongoDao.updateTag(tableName, cDb1, new BasicDBObject("$set", update), false, true);
                this.mongoDao.updateTag("u_list_topic", cDb1, new BasicDBObject("$set", update), false, true);
            }
        }
    }


    private UUser getUserByUserId(String userId) throws Exception {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("userId", userId);
        return this.uUserService.getUserByUserId(hashMap);
    }


    private String getTopicSource(String objectId, String type) throws Exception {
        if (StringUtils.isEmpty(objectId) || StringUtils.isEmpty(type))
            return "";
        switch (type) {
            case "1":
                //获取话题发布来源 //获取话题发布来源
                UUser user = this.getUserByUserId(objectId);
                return this.getUserName(user);
            case "2":
                UDuel duel = this.baseDAO.get(UDuel.class, objectId);
                return duel == null ? "" : this.getTeamName(duel.getUTeam());
            case "3":
                UChallenge challenge = this.baseDAO.get(UChallenge.class, objectId);
                return challenge == null ? "" : this.getTeamName(challenge.getFteam());
            case "4":
                UTeam team = this.baseDAO.get(UTeam.class, objectId);
                return this.getTeamName(team);
            case "5":
                UMatchBs matchBs = this.baseDAO.get(UMatchBs.class, objectId);
                if (matchBs == null) return "";
                return this.getTeamName(matchBs.getBsFteam()).concat(" VS ").concat(this.getTeamName(matchBs.getBsXteam()));

            case "6":
                UBrCourt court = this.baseDAO.get(UBrCourt.class, objectId);
                return court == null ? "" : court.getName();
        }
        return "";
    }


    //未找到话题时返回
    private HashMap<String, Object> topicNotFondError() {
        HashMap<String, Object> errorMap = new HashMap<String, Object>();
        errorMap.put("errorCode", "1");
        errorMap.put("errorMsg", "未找到话题");
        return errorMap;
    }

}
