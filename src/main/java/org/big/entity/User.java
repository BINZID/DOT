package org.big.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *<p><b>User的Entity类</b></p>
 *<p> User的Entity类</p>
 * @author BIN
 *<p>Created date: 2020/11/30 09:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Entity
@Table(name = "user", schema = "biodata")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_name")
    private String userName;
	@Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "role")
    private String role;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "adddate")
    private Date adddate;
    // 补充字段
	@Column(name = "avatar")
    private String avatar;	
	@Column(name = "dtime")
    private Timestamp dtime;
	@Column(name = "idnum")
    private Integer idnum;
	@Column(name = "level")
    private Integer level;	
	@Column(name = "mark")
    private String mark;	
	@Column(name = "mobile")
    private String mobile;	
	@Column(name = "score")
    private Integer score;	
	@Column(name = "status")
    private Byte status; 		// 用户是否激活
	@Column(name = "uploadnum")
    private Integer uploadnum;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "resettime")
    private Date resettime;
    
	@Column(name = "resetmark")
    private String resetmark;
    
	@Column(name = "profile_picture")
    private String profilePicture;
    
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/** 保留默认无参构造 */
    public User() {
    }
}