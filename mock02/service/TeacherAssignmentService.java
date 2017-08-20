package mock02.service;

import java.sql.Blob;
import java.text.ParseException;
import java.util.List;

import mock02.dao.AssignmentDAO;
import mock02.dao.MemberDAO;
import mock02.dao.ScoreDAO;
import mock02.dao.UserDAO;
import mock02.model.Assignment;
import mock02.model.Course;
import mock02.model.Score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 25, 2015
 **/
@Service("techerAssignmentService")
public class TeacherAssignmentService {

	@Autowired
	AssignmentDAO assignmentDAO;
	@Autowired
	ScoreDAO scoreDAO;
	@Autowired
	MemberDAO memberDAO;
	@Autowired
	UserDAO userDAO;

	// get list assignment by id course
	public List<Assignment> getListAssignmentByCourse(int idCourse) throws ParseException {
		Course course = new Course();
		course.setIdCourse(idCourse);
		return assignmentDAO.getListAssignmentByCourse(course);
	}

	// get assignment by id assignment
	public Assignment getAssigment(int idAssignment) {
		return assignmentDAO.getAssigment(idAssignment);
	}

	public void saveOrUpdateAssignment(Assignment assignment) {
		assignmentDAO.saveOrUpdateAssignment(assignment);
	}

	public Assignment getAssignmentNameAndDeadlineById(Integer idAssignment) {
		return assignmentDAO.getAssignmentNameAndDeadlineById(idAssignment);
	}

	public void updateNotChangeFileAssignment(Assignment assignment) {
		assignmentDAO.updateNotChangeFileAssignment(assignment);
	}

	public void deleteAssignment(Integer idAssignment) {
		assignmentDAO.deleteAssignment(idAssignment);
	}

	public Blob getAttachFileById(Integer idAssignment) {
		return assignmentDAO.getAttachFileById(idAssignment);
	}

	public Blob getFileById(Integer id) {
		return assignmentDAO.getFileById(id);
	}

	public String getFileNameById(Integer id) {
		return assignmentDAO.getFileNameById(id);
	}

	public List<Score> getListAnswerByIdAssignment(Integer idAssignment) throws ParseException {
		Assignment assignment = new Assignment();
		assignment.setIdAssignment(idAssignment);
		return scoreDAO.getListAnswerByAssignment(assignment);
	}

	public Score getAnswerById(Integer idScore) throws ParseException {
		return scoreDAO.getAnswerById(idScore);
	}
	
	public void markAnswer(Score score){
		scoreDAO.markAnswer(score);
	}
}
