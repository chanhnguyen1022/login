package mock02.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mock02.dao.ManageCourseDAO;
import mock02.model.Course;
import mock02.model.Member;
import mock02.model.User;
import mock02.validator.Uploadfile;

/*
* TramTran(^^)
*/
@Service("manageCourseService")
public class ManageCourseService {

    @Autowired
    private ManageCourseDAO manageCourseDAO;
    public List<User> readFile( Uploadfile ufile){
        return manageCourseDAO.readFile(ufile);
    }
    public boolean createCourse(Course c){
       return manageCourseDAO.createCourse(c);
    }
    public Course getCourse(String c){
        return manageCourseDAO.getCourse(c);
    }
    public Course getCourse(int id){
        return manageCourseDAO.getCourse(id);
    }
    
    public Member checkUserofCouse(User u,Course c){
        return manageCourseDAO.checkUserofCouse(u, c);
    }
    public boolean deleteCourse(int courseID){
        return manageCourseDAO.deleteCourse(courseID);
    }
    public int checkCourseUpdate(String courseName){
        return manageCourseDAO.checkCourseUpdate(courseName);
    }
    public String updateCourse(Course c){
        return manageCourseDAO.updateCourse(c);
    }
}
