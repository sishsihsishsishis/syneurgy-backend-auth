package com.bezkoder.spring.security.postgresql.models;

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

	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Size(max = 20)
	private String country;


	@Size(max = 50)
	private String company;
	@Size(max = 20)
	private String position;

	private Integer step;

	@OneToMany(targetEntity = Organization.class, mappedBy = "id", orphanRemoval = false, fetch = FetchType.LAZY)
	private Set<Organization> organizations;

//	@OneToMany(targetEntity = Organization.class, mappedBy = "id", orphanRemoval = false, fetch = FetchType.LAZY)
//	private Set<Team> teams;

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	@JoinTable(name = "user_teams",
			joinColumns = { @JoinColumn(name = "user_id") },
			inverseJoinColumns = { @JoinColumn(name = "team_id") })
	private Set<Team> teams = new HashSet<>();

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
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

	public Set<Organization> getOrganizations() { return  organizations; }
	public void setOrganizations(Set<Organization> organizations) { this.organizations = organizations; }

	public Set<Team> getTeams() { return  teams; }
	public void setTeams(Set<Team> teams) { this.teams = teams; }

	public Integer getStep() { return step; }
	public void setStep(Integer step) { this.step = step; }

	public String getCompany() { return company; }
	public  void setCompany(String company) { this.company = company; }

	public String getPosition() { return position; }
	public void setPosition(String position) { this.position = position; }

	public void addTeam(Team team) {
		this.teams.add(team);
		team.getUsers().add(this);
	}

	public void removeTeam(long teamId) {
		Team team = this.teams.stream().filter(t -> t.getId() == teamId).findFirst().orElse(null);
		if (team != null) {
			this.teams.remove(team);
			team.getUsers().remove(this);
		}
	}
}
