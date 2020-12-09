package org.big.pojo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * <p><b>UserEntity</b></p>
 * <p> </p>
 * <p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 *
 * @Author NY
 * @Date: 2020/12/9 15:11
 * @Version V1.0
 * @since JDK 1.8.0_162
 */
@Entity
@Table(name = "user", schema = "dot", catalog = "")
public class UserEntity {
    private String id;
    private String realName;
    private String nickname;
    private String role;
    private String email;
    private String pwd;
    private String username;
    private String countryCode;
    private String lastSignInTime;
    private String mobile;
    private String profilePicture;
    private String signUpTime;
    private Integer status;
    private String token;
    private String verificationCode;
    private Timestamp verificationCodeExpiryTime;
    private Integer tokenNum;
    private String umtId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Basic
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "pwd")
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "country_code")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "last_sign_in_time")
    public String getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(String lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "profile_picture")
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Basic
    @Column(name = "sign_up_time")
    public String getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(String signUpTime) {
        this.signUpTime = signUpTime;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "verification_code")
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Basic
    @Column(name = "verification_code_expiry_time")
    public Timestamp getVerificationCodeExpiryTime() {
        return verificationCodeExpiryTime;
    }

    public void setVerificationCodeExpiryTime(Timestamp verificationCodeExpiryTime) {
        this.verificationCodeExpiryTime = verificationCodeExpiryTime;
    }

    @Basic
    @Column(name = "token_num")
    public Integer getTokenNum() {
        return tokenNum;
    }

    public void setTokenNum(Integer tokenNum) {
        this.tokenNum = tokenNum;
    }

    @Basic
    @Column(name = "umt_id")
    public String getUmtId() {
        return umtId;
    }

    public void setUmtId(String umtId) {
        this.umtId = umtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(realName, that.realName) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(role, that.role) &&
                Objects.equals(email, that.email) &&
                Objects.equals(pwd, that.pwd) &&
                Objects.equals(username, that.username) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(lastSignInTime, that.lastSignInTime) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(profilePicture, that.profilePicture) &&
                Objects.equals(signUpTime, that.signUpTime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(token, that.token) &&
                Objects.equals(verificationCode, that.verificationCode) &&
                Objects.equals(verificationCodeExpiryTime, that.verificationCodeExpiryTime) &&
                Objects.equals(tokenNum, that.tokenNum) &&
                Objects.equals(umtId, that.umtId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, realName, nickname, role, email, pwd, username, countryCode, lastSignInTime, mobile, profilePicture, signUpTime, status, token, verificationCode, verificationCodeExpiryTime, tokenNum, umtId);
    }
}
