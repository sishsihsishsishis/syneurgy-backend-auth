package com.bezkoder.spring.security.postgresql.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "email")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@Size(max = 50)
	@Column(name = "zoom_email", unique = true, nullable = true)
	private String zoomEmail;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Size(max = 20)
	private String country;

	@Size(max = 20)
	private String countryCode;

	@Size(max = 50)
	private String company;
	@Size(max = 20)
	private String position;

	private String resetPasswordToken;
	private Integer step;

	private String invitationToken;

	private String photo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdDate;

	private String answers;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<UserTeam> userTeams = new HashSet<>();
	@NotBlank
	@Size(max = 120)
	private String password;

	private int paid_status = 0; // Default value is 0

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	private int percent; // Level of Participation

	@Column(columnDefinition = "boolean default true")
	private boolean isActive;
	@Column(name = "is_email_verified", columnDefinition = "boolean default false")
	private boolean isEmailVerified;

	private String tokenForEmail;

	@Column(name = "is_seen_tutorial_home", columnDefinition = "boolean default false")
	private boolean isSeenTutorialHome;

	@Column(name = "is_seen_tutorial_meeting", columnDefinition = "boolean default false")
	private boolean isSeenTutorialMeeting;

	@Size(max = 50)
	private String zoomAccountId;

	@Size(max = 50)
	private String zoomClientId;

	@Size(max = 50)
	private String zoomClientSecret;

	public User() {
		this.isActive = true;
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.isActive = true;
	}

	public Set<UserTeam> getUserTeams() {
		return userTeams;
	}

	public void setUserTeams(Set<UserTeam> userTeams) {
		this.userTeams = userTeams;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getCountry() { return country; }
	public  void setCountry(String country) { this.country = country; }

	public Integer getStep() { return step; }
	public void setStep(Integer step) { this.step = step; }

	public String getCompany() { return company; }
	public  void setCompany(String company) { this.company = company; }

	public String getPosition() { return position; }
	public void setPosition(String position) { this.position = position; }

	public void addUserTeam(UserTeam userTeam) {
		this.userTeams.add(userTeam);
	}

	public String getInvitationToken() {
		return invitationToken;
	}

	public void setInvitationToken(String invitationToken) {
		this.invitationToken = invitationToken;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getFullName() {
		if (firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
			return firstName + " " + lastName;
		} else if (firstName != null && firstName.length() > 0) {
			return firstName;
		} else if (lastName != null && lastName.length() > 0) {
			return lastName;
		}
		return "";
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		isEmailVerified = emailVerified;
	}

	public String getTokenForEmail() {
		return tokenForEmail;
	}

	public void setTokenForEmail(String tokenForEmail) {
		this.tokenForEmail = tokenForEmail;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public int getPaid_status() {
		return paid_status;
	}

	public void setPaid_status(int paid_status) {
		this.paid_status = paid_status;
	}

	public String getZoomEmail() {
		return zoomEmail;
	}

	public void setZoomEmail(String zoomEmail) {
		this.zoomEmail = zoomEmail;
	}

	public boolean isSeenTutorialHome() {
		return isSeenTutorialHome;
	}

	public boolean isSeenTutorialMeeting() {
		return isSeenTutorialMeeting;
	}

	public void setSeenTutorialHome(boolean seenTutorialHome) {
		isSeenTutorialHome = seenTutorialHome;
	}

	public void setSeenTutorialMeeting(boolean seenTutorialMeeting) {
		isSeenTutorialMeeting = seenTutorialMeeting;
	}

	public String getZoomAccountId() {
		return zoomAccountId;
	}

	public void setZoomAccountId(String zoomAccountId) {
		this.zoomAccountId = zoomAccountId;
	}

	public String getZoomClientId() {
		return zoomClientId;
	}

	public void setZoomClientId(String zoomClientId) {
		this.zoomClientId = zoomClientId;
	}

	public String getZoomClientSecret() {
		return zoomClientSecret;
	}

	public void setZoomClientSecret(String zoomClientSecret) {
		this.zoomClientSecret = zoomClientSecret;
	}
}
