package mock02.dto;

import java.util.Date;

/*
 *@author: nguyenkhue
 *@version 1.0 Jan 1, 2016
 */
public class StudentAssignmentDTO {

	private Integer idAssignment;
	private String assignmentName;
	private Integer remainTime;
	private Date openTime;
	private Date deadline;
	private Integer dateDiff;
	private String type;
	private Integer highestScore;
	private boolean hasDone;

	public StudentAssignmentDTO() {

	}

	public StudentAssignmentDTO(Integer idAssignment, String assignmentName, Date openTime, Date deadline,
			String type) {
		super();
		this.idAssignment = idAssignment;
		this.assignmentName = assignmentName;
		this.openTime = openTime;
		this.deadline = deadline;
		this.type = type;
	}

	public Integer getIdAssignment() {
		return idAssignment;
	}

	public void setIdAssignment(Integer idAssignment) {
		this.idAssignment = idAssignment;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public Integer getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(Integer remainTime) {
		this.remainTime = remainTime;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(Integer highestScore) {
		this.highestScore = highestScore;
	}

	public boolean isHasDone() {
		return hasDone;
	}

	public void setHasDone(boolean hasDone) {
		this.hasDone = hasDone;
	}

	public Integer getDateDiff() {
		return dateDiff;
	}

	public void setDateDiff(Integer dateDiff) {
		this.dateDiff = dateDiff;
	}
}
