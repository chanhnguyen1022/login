package mock02.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Member generated by hbm2java
 */
@Entity
@Table(name = "member", catalog = "hoctructuyen")
public class Member implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idMember;
	private Course course;
	private User user;
	private Set<Discussion> discussions = new HashSet<Discussion>();
	private Set<Participation> participations = new HashSet<Participation>();
	private Set<Score> scores = new HashSet<Score>();

	public Member() {
	}

	public Member(Course course, User user) {
		this.course = course;
		this.user = user;
	}

	public Member(Course course, User user, Set<Discussion> discussions, Set<Participation> participations,
			Set<Score> scores) {
		this.course = course;
		this.user = user;
		this.discussions = discussions;
		this.participations = participations;
		this.scores = scores;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idmember", unique = true, nullable = false)
	public Integer getIdMember() {
		return this.idMember;
	}

	public void setIdMember(Integer idMember) {
		this.idMember = idMember;
	}

	@ManyToOne/*(fetch = FetchType.LAZY)*/
	@JoinColumn(name = "idcourse", nullable = false)
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@ManyToOne/*(fetch = FetchType.LAZY)*/
	@JoinColumn(name = "iduser", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public Set<Discussion> getDiscussions() {
		return this.discussions;
	}

	public void setDiscussions(Set<Discussion> discussions) {
		this.discussions = discussions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public Set<Participation> getParticipations() {
		return this.participations;
	}

	public void setParticipations(Set<Participation> participations) {
		this.participations = participations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
	public Set<Score> getScores() {
		return this.scores;
	}

	public void setScores(Set<Score> scores) {
		this.scores = scores;
	}

}