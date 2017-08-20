package mock02.service;

import mock02.dao.ManageStudentDAO;
import mock02.dao.TeacherCourseDAO;
import mock02.model.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 13, 2015
 */
@Service("teacherCourseService")
public class TeacherCourseService {
	
	@Autowired
	TeacherCourseDAO techerCourseDAO;
	@Autowired
	ManageStudentDAO manageStudentDAO;
	
	public boolean isIdCourse(String idStr){
		try {
			int id = Integer.parseInt(idStr);
			return techerCourseDAO.isIdCourse(id);
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	//get course from idcourse
	public Course getCourse(Integer idCourse){
		return manageStudentDAO.getCourse(idCourse);
	}
}
