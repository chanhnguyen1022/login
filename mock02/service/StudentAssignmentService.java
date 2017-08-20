package mock02.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import mock02.dao.AssignmentDAO;
import mock02.dao.MemberDAO;
import mock02.dao.ScoreDAO;
import mock02.dto.StudentAnswerTestDTO;
import mock02.dto.StudentAssignmentDTO;
import mock02.model.Assignment;
import mock02.model.Course;
import mock02.model.Member;
import mock02.model.Score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@author: nguyenkhue
 *@version 1.0 Jan 1, 2016
 */
@Service("studentAssignmentService")
public class StudentAssignmentService {

	@Autowired
	AssignmentDAO assignmentDAO;
	@Autowired
	ScoreDAO scoreDAO;
	@Autowired
	MemberDAO memberDAO;

	public List<StudentAssignmentDTO> getListOpenAssignmentByCourseAndIdMember(Integer idCourse,
			Integer idMember) throws ParseException {
		List<StudentAssignmentDTO> listSaDTO = new ArrayList<StudentAssignmentDTO>();
		Course course = new Course();
		course.setIdCourse(idCourse);
		Member member = new Member();
		member.setIdMember(idMember);
		List<Assignment> listAssignment = assignmentDAO.getListOpenAssignmentByCourse(course);
		int i = 0;
		int size = listAssignment.size();
		List<Integer> listScore;
		StudentAssignmentDTO saDTO;
		boolean hasDone;
		Integer updateTime;
		Integer remainTime;
		while (i < size) {
			Assignment a = listAssignment.get(i);
			saDTO = new StudentAssignmentDTO(a.getIdAssignment(), a.getAssignmentName(), a.getOpenTime(),
					a.getDeadline(), a.getType());
			listScore = scoreDAO.getPointbyMemberAndAssignment(a, member);
			hasDone = scoreDAO.hasDone(a, member);
			updateTime = scoreDAO.getCountUpdate(a, member);
			if (updateTime != null)
				remainTime = a.getRedoTime() - updateTime;
			else
				remainTime = a.getRedoTime();
			double deadlineTime = a.getDeadline().getTime();
			Date now = new Date();
			double nowTime = now.getTime();
			Double dateDiff = (deadlineTime - nowTime) / (1000 * 60 * 60 * 24);
			Integer dateInt = dateDiff.intValue();
			Integer highestScore = null;
			Iterator<Integer> iter = listScore.iterator();
			while (iter.hasNext()) {
				Integer score = iter.next();
				if (score == null)
					iter.remove();
			}
			if (listScore.size() > 1)
				highestScore = (Integer) Collections.max(listScore, null);
			else if (listScore.size() == 1)
				highestScore = listScore.get(0);
			saDTO.setHighestScore(highestScore);
			saDTO.setHasDone(hasDone);
			saDTO.setRemainTime(remainTime);
			saDTO.setDateDiff(dateInt);
			listSaDTO.add(saDTO);
			i++;
		}
		return listSaDTO;
	}

	public String getOpenAssignmentTypeById(Integer idAssignment) {
		return assignmentDAO.getOpenAssignmentTypeById(idAssignment);
	}

	public StudentAnswerTestDTO getstudentAnswerTestDTO(Integer idAssignment, Integer idMember) {
		Assignment a = assignmentDAO.getAssigment(idAssignment);
		Member member = new Member();
		member.setIdMember(idMember);
		String fullName = memberDAO.getFullNameById(idMember);
		Score score = scoreDAO.getScorebyMemberAndAssignment(a, member);
		double deadlineTime = a.getDeadline().getTime();
		Date now = new Date();
		double nowTime = now.getTime();
		Double dateDiff = (deadlineTime - nowTime) / (1000 * 60 * 60 * 24);
		Integer dateInt = dateDiff.intValue();
		StudentAnswerTestDTO satDTO = new StudentAnswerTestDTO(a.getIdAssignment(), a.getContentAssignment(),
				a.getAssignmentName(), a.getAttachFileName(), a.getDeadline());
		satDTO.setStudentName(fullName);
		satDTO.setDateDiff(dateInt);
		Integer remainTime = a.getRedoTime();
		if (score != null) {
			remainTime = a.getRedoTime() - score.getCountUpdate();
			satDTO.setAnswer(score.getAnswer());
			satDTO.setScoreFileName(score.getAttachFileName());
			satDTO.setComment(score.getComment());
			satDTO.setTimeStore(score.getTimeStore());
			if (score.getScore() != null) {
				satDTO.setScore(score.getScore());
				if (score.getComment() != null)
					satDTO.setComment(score.getComment());
			}
		}
		satDTO.setRemainTime(remainTime);
		return satDTO;
	}

	public Integer getIdScoreByIdAssignmentAndIdMember(Integer idAssignment, Integer idMember) {
		return scoreDAO.getIdScoreByIdAssignmentAndIdMember(idAssignment, idMember);
	}

	public void saveAnswerTestWithFile(Assignment assignment, Member member, String answer, Blob file,
			String fileName) {
		scoreDAO.saveAnswerTestWithFile(assignment, member, answer, file, fileName);
	}

	public void saveAnswerTestWithoutFile(Assignment assignment, Member member, String answer) {
		scoreDAO.saveAnswerTestWithoutFile(assignment, member, answer);
	}

	public void updateAnswerTestNotChangeFile(String answer, Integer idScore, Integer redoTime) {
		scoreDAO.updateAnswerTestNotChangeFile(answer, idScore, redoTime);
	}

	public void updateAnswerTestChangeFile(String answer, Blob file, String fileName, Integer idScore,
			Integer redoTime) throws SQLException {
		scoreDAO.updateAnswerTestChangeFile(answer, file, fileName, idScore, redoTime);
	}

	public String getAnswerFileNameById(Integer id) {
		return scoreDAO.getAnswerFileNameById(id);
	}

	public Blob getAnswerFileById(Integer id) {
		return scoreDAO.getAnswerFileById(id);
	}

	public Integer getRedoTimeById(Integer idAssignment) {
		return assignmentDAO.getRedoTimeById(idAssignment);
	}
}
