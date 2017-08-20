package mock02.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import mock02.dao.LectureDAO;
import mock02.dao.ResourceDAO;
import mock02.dao.SubjectDAO;
import mock02.model.Course;
import mock02.model.Lecture;
import mock02.model.Resource;
import mock02.model.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 21, 2015
 */
@Service("teacherMaterialsService")
public class TeacherMaterialsService {

	@Autowired
	SubjectDAO subjectDAO;
	@Autowired
	LectureDAO lectureDAO;
	@Autowired
	ResourceDAO resourceDAO;
	
	Log logger = LogFactory.getLog(TeacherMaterialsService.class);

	public List<Subject> getListSubjectByIdCourse(int idCourse) {
		Course course = new Course();
		course.setIdCourse(idCourse);
		return subjectDAO.getListSubjectByCourse(course);
	}

	public boolean isIdLecture(int idLecture) {
		return lectureDAO.isIdLecture(idLecture);
	}

	public String getLectureNameByIdLecture(Integer idLecture) {
		return lectureDAO.getLectureNameByIdLecture(idLecture);
	}

	public List<Resource> getListResourceNameByIdLecture(int idLecture) {
		Lecture lecture = new Lecture();
		lecture.setIdLecture(idLecture);
		return resourceDAO.getListResourceNameByLecture(lecture);
	}

	public Integer addResourceVideo(Integer idLecture, String value) {
		resourceDAO.addResourceVideo(idLecture, value);
		return resourceDAO.getLastId();
	}

	public void deleteResource(Integer id) {
		resourceDAO.deleteResource(id);
	}

	public void editResourceVideo(Integer id, String value) {
		resourceDAO.editResourceVideo(id, value);
	}

	public void addResourceDoc(String fileName, byte[] value, Integer idLecture) throws SerialException,
			SQLException {
		Blob blob = new SerialBlob(value);
		resourceDAO.addResourceDoc(fileName, blob, idLecture);
	}

	public Blob getResourceDocById(Integer id) {
		return resourceDAO.getResourceDocById(id);
	}
	
	public String getResourceNameById(Integer id) {
		return resourceDAO.getResourceNameById(id);
	}
	
	public Integer getLastIdResource(){
		return resourceDAO.getLastId();
	}
	
	public void createSubject(String name, Integer idCourse){
		subjectDAO.createSubject(name, idCourse);
	}
	
	public void deleteSubject(Integer id){
		subjectDAO.deleteSubject(id);
	}
	
	public void editSubject(Integer id, String name){
		subjectDAO.editSubject(id, name);
	}
	
	public Integer addLecture(Integer idSubject, String name){
		if(name == null || name.trim().length() == 0)
			return null;
		lectureDAO.addLecture(idSubject, name);
		return lectureDAO.getLastId();
	}
	
	public void deleteLecture(Integer idLecture){
		lectureDAO.deleteLecture(idLecture);
	}
}
