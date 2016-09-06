package upbox.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 球员角色位置信息表
 * @author mercideng
 *
 */
@Entity
@Table(name = "u_player_role_limit")
public class UPlayerRoleLimit implements Serializable{

	private static final long serialVersionUID = -7550884697041784639L;
	private String keyId;
	private String memberType;
	private Date createDate;
	private Date modifyDate;
	private String createUserId;
	private String modifyUserId;
	private String updateUteamInfo;
	private String transferMemberType;
	private String duel;
	private String challenge;
	private String matched;
	private String excludePlayer;
	private String disbandPlayer;
	private String invitePlayer;
	private String recruitPlayer;
	private String removeUteamImg;
	private String updateFightingForce;
	private String updatePlayerinfoInteam;
	private String assignMemberTypeManager;
	private String assignMemberTypeOperater;
	private String updateFormation;
	private String setTrain;
	private String announce;
	private String financeManager;
	private String applyMemberType;
	private String duelRemind;
	private String challengeRemind;
	private String matchRemind;
	private String releaseDynamic;
	private String commentDynamic;
	private String contributeUteamCost;
	private String inviteOtherUser;
	private String signUteam;
	private String signPlayer;
	private String signDuel;
	private String signChallenge;
	private String signMatch;
	private String signStands;
	private String rankRole;

	@Id
	@Column(name = "key_id", unique = true, nullable = false, length = 60)
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	@Column(name = "member_type", length = 20)
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	@Column(name = "create_date", length = 20)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "modify_date", length = 20)
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	@Column(name = "create_user_id", length = 60)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "modify_user_id", length = 60)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name = "update_uteam_info", length = 20)
	public String getUpdateUteamInfo() {
		return updateUteamInfo;
	}
	public void setUpdateUteamInfo(String updateUteamInfo) {
		this.updateUteamInfo = updateUteamInfo;
	}
	@Column(name = "transfer_member_type", length = 20)
	public String getTransferMemberType() {
		return transferMemberType;
	}
	public void setTransferMemberType(String transferMemberType) {
		this.transferMemberType = transferMemberType;
	}
	@Column(name = "duel", length = 20)
	public String getDuel() {
		return duel;
	}
	public void setDuel(String duel) {
		this.duel = duel;
	}
	@Column(name = "challenge", length = 20)
	public String getChallenge() {
		return challenge;
	}
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	
	@Column(name = "matched", length = 20)
	public String getMatched() {
		return matched;
	}
	public void setMatched(String matched) {
		this.matched = matched;
	}
	
	@Column(name = "exclude_player", length = 20)
	public String getExcludePlayer() {
		return excludePlayer;
	}
	public void setExcludePlayer(String excludePlayer) {
		this.excludePlayer = excludePlayer;
	}

	@Column(name = "disband_player", length = 20)
	public String getDisbandPlayer() {
		return disbandPlayer;
	}
	public void setDisbandPlayer(String disbandPlayer) {
		this.disbandPlayer = disbandPlayer;
	}

	@Column(name = "invite_player", length = 20)
	public String getInvitePlayer() {
		return invitePlayer;
	}
	public void setInvitePlayer(String invitePlayer) {
		this.invitePlayer = invitePlayer;
	}

	@Column(name = "recruit_player", length = 20)
	public String getRecruitPlayer() {
		return recruitPlayer;
	}
	public void setRecruitPlayer(String recruitPlayer) {
		this.recruitPlayer = recruitPlayer;
	}

	@Column(name = "remove_uteam_img", length = 20)
	public String getRemoveUteamImg() {
		return removeUteamImg;
	}
	public void setRemoveUteamImg(String removeUteamImg) {
		this.removeUteamImg = removeUteamImg;
	}

	@Column(name = "update_fighting_force", length = 20)
	public String getUpdateFightingForce() {
		return updateFightingForce;
	}
	public void setUpdateFightingForce(String updateFightingForce) {
		this.updateFightingForce = updateFightingForce;
	}

	@Column(name = "update_playerinfo_inteam", length = 20)
	public String getUpdatePlayerinfoInteam() {
		return updatePlayerinfoInteam;
	}
	public void setUpdatePlayerinfoInteam(String updatePlayerinfoInteam) {
		this.updatePlayerinfoInteam = updatePlayerinfoInteam;
	}

	@Column(name = "assign_member_type_manager", length = 20)
	public String getAssignMemberTypeManager() {
		return assignMemberTypeManager;
	}
	public void setAssignMemberTypeManager(String assignMemberTypeManager) {
		this.assignMemberTypeManager = assignMemberTypeManager;
	}

	@Column(name = "assign_member_type_operater", length = 20)
	public String getAssignMemberTypeOperater() {
		return assignMemberTypeOperater;
	}
	public void setAssignMemberTypeOperater(String assignMemberTypeOperater) {
		this.assignMemberTypeOperater = assignMemberTypeOperater;
	}

	@Column(name = "update_formation", length = 20)
	public String getUpdateFormation() {
		return updateFormation;
	}
	public void setUpdateFormation(String updateFormation) {
		this.updateFormation = updateFormation;
	}

	@Column(name = "set_train", length = 20)
	public String getSetTrain() {
		return setTrain;
	}
	public void setSetTrain(String setTrain) {
		this.setTrain = setTrain;
	}

	@Column(name = "announce", length = 20)
	public String getAnnounce() {
		return announce;
	}
	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	@Column(name = "finance_manager", length = 20)
	public String getFinanceManager() {
		return financeManager;
	}
	public void setFinanceManager(String financeManager) {
		this.financeManager = financeManager;
	}

	@Column(name = "apply_member_type", length = 20)
	public String getApplyMemberType() {
		return applyMemberType;
	}
	public void setApplyMemberType(String applyMemberType) {
		this.applyMemberType = applyMemberType;
	}

	@Column(name = "duel_remind", length = 20)
	public String getDuelRemind() {
		return duelRemind;
	}
	public void setDuelRemind(String duelRemind) {
		this.duelRemind = duelRemind;
	}

	@Column(name = "challenge_remind", length = 20)
	public String getChallengeRemind() {
		return challengeRemind;
	}
	public void setChallengeRemind(String challengeRemind) {
		this.challengeRemind = challengeRemind;
	}

	@Column(name = "match_remind", length = 20)
	public String getMatchRemind() {
		return matchRemind;
	}
	public void setMatchRemind(String matchRemind) {
		this.matchRemind = matchRemind;
	}

	@Column(name = "release_dynamic", length = 20)
	public String getReleaseDynamic() {
		return releaseDynamic;
	}
	public void setReleaseDynamic(String releaseDynamic) {
		this.releaseDynamic = releaseDynamic;
	}

	@Column(name = "comment_dynamic", length = 20)
	public String getCommentDynamic() {
		return commentDynamic;
	}
	public void setCommentDynamic(String commentDynamic) {
		this.commentDynamic = commentDynamic;
	}

	@Column(name = "contribute_uteam_cost", length = 20)
	public String getContributeUteamCost() {
		return contributeUteamCost;
	}
	public void setContributeUteamCost(String contributeUteamCost) {
		this.contributeUteamCost = contributeUteamCost;
	}

	@Column(name = "invite_other_user", length = 20)
	public String getInviteOtherUser() {
		return inviteOtherUser;
	}
	public void setInviteOtherUser(String inviteOtherUser) {
		this.inviteOtherUser = inviteOtherUser;
	}

	@Column(name = "sign_uteam", length = 20)
	public String getSignUteam() {
		return signUteam;
	}
	public void setSignUteam(String signUteam) {
		this.signUteam = signUteam;
	}

	@Column(name = "sign_player", length = 20)
	public String getSignPlayer() {
		return signPlayer;
	}
	public void setSignPlayer(String signPlayer) {
		this.signPlayer = signPlayer;
	}

	@Column(name = "sign_duel", length = 20)
	public String getSignDuel() {
		return signDuel;
	}
	public void setSignDuel(String signDuel) {
		this.signDuel = signDuel;
	}

	@Column(name = "sign_challenge", length = 20)
	public String getSignChallenge() {
		return signChallenge;
	}
	public void setSignChallenge(String signChallenge) {
		this.signChallenge = signChallenge;
	}

	@Column(name = "sign_match", length = 20)
	public String getSignMatch() {
		return signMatch;
	}
	public void setSignMatch(String signMatch) {
		this.signMatch = signMatch;
	}

	@Column(name = "sign_stands", length = 20)
	public String getSignStands() {
		return signStands;
	}
	public void setSignStands(String signStands) {
		this.signStands = signStands;
	}
	@Column(name = "rank_role", length = 20)
	public String getRankRole() {
		return rankRole;
	}
	public void setRankRole(String rankRole) {
		this.rankRole = rankRole;
	}
	
}
