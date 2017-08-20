package mock02.dto;

import java.util.Date;

/*
 *@author: nguyenkhue
 *@version 1.0 Jan 2, 2016
 */
public class StudentAnswerTestDTO {
	private Integer idAssignment;
	private String contentAsisgnment;
	private String assignmentName;
	private String assignmentFileName;
	private Date timeStore;
	private Date deadline;
	private Integer dateDiff;
	private Integer score;
	private String scoreFileName;
	private String comment;
	private String answer;
	private Integer remainTime;
	private String studentName;

	public StudentAnswerTestDTO() {

	}

	public StudentAnswerTestDTO(Integer idAssignment, String contentAsisgnment, String assignmentName,
			String assignmentFileName, Date deadline) {
		super();
		this.idAssignment = idAssignment;
		this.contentAsisgnment = contentAsisgnment;
		this.assignmentName = assignmentName;
		this.assignmentFileName = assignmentFileName;
		this.deadline = deadline;
	}

	public Integer getIdAssignment() {
		return idAssignment;
	}

	public void setIdAssignment(Integer idAssignment) {
		this.idAssignment = idAssignment;
	}

	public String getContentAsisgnment() {
		return contentAsisgnment;
	}

	public void setContentAsisgnment(String contentAsisgnment) {
		this.contentAsisgnment = contentAsisgnment;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getAssignmentFileName() {
		return assignmentFileName;
	}

	public void setAssignmentFileName(String assignmentFileName) {
		this.assignmentFileName = assignmentFileName;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getScoreFileName() {
		return scoreFileName;
	}

	public void setScoreFileName(String scoreFileName) {
		this.scoreFileName = scoreFileName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(Integer remainTime) {
		this.remainTime = remainTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getDateDiff() {
		return dateDiff;
	}

	public void setDateDiff(Integer dateDiff) {
		this.dateDiff = dateDiff;
	}

	public Date getTimeStore() {
		return timeStore;
	}

	public void setTimeStore(Date timeStore) {
		this.timeStore = timeStore;
	}
}
