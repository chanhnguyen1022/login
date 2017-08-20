package mock02.service;

import java.util.List;

import mock02.dao.CourseDAO;
import mock02.model.Course;
import mock02.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("courseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CourseService {
	@Autowired
	private CourseDAO courseDao;
	
	//get listcourse by lis member
	public List<Course> course_User(List<Member> member)
	{
		return courseDao.course_User(member);
	}
	
	//get course by id course
	public Course getCourse(int courseID) {
		return courseDao.getCourse(courseID);
	}
	
	public Integer getIdMemberByIdUserAndIdCourse(Integer idUser, Integer idCourse){
		return courseDao.getIdMemberByIdUserAndIdCourse(idUser, idCourse);
	}
}
