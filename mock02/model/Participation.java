package mock02.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Participation generated by hbm2java
 */
@Entity
@Table(name = "participation", catalog = "hoctructuyen")
public class Participation implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer idParticipation;
	private Member member;
	private Discussion discussion;
	private String participation;

	public Participation() {
	}

	public Participation(Member member, Discussion discussion, String participation) {
		this.member = member;
		this.discussion = discussion;
		this.participation = participation;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idparticipation", unique = true, nullable = false)
	public Integer getIdParticipation() {
		return this.idParticipation;
	}

	public void setIdParticipation(Integer idParticipation) {
		this.idParticipation = idParticipation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmember", nullable = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "iddiscussion", nullable = false)
	public Discussion getDiscussion() {
		return this.discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	@Column(name = "participation", nullable = false, length = 5000)
	public String getParticipation() {
		return this.participation;
	}

	public void setParticipation(String participation) {
		this.participation = participation;
	}

}
