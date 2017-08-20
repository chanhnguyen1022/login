package mock02.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mock02.model.Course;
import mock02.model.Member;
import mock02.model.User;
import mock02.validator.Validate;
import mock02.validator.Uploadfile;

/*
* TramTran(^^)
*/
@Repository("manageCourseDAO")
@Transactional
public class ManageCourseDAO {
    @Autowired
    private SessionFactory sessionFactory;
    
    // doc file
    @SuppressWarnings({ "rawtypes", "resource" })
    public List<User> readFile(Uploadfile ufile) {
        List<User> listStudent = new ArrayList<>();
        try {
            
            Workbook workbook = new XSSFWorkbook(ufile.getFile().getInputStream());
            int numberOfSheets = workbook.getNumberOfSheets();// get Sheet in
                                                              // Excel
            
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator rowIterator = sheet.iterator();// Lấy các rows của 1
                                                        // sheet
                rowIterator.next();// bỏ row header của sheet
                while (rowIterator.hasNext()) {
                    
                    Row row = (Row) rowIterator.next();// chạy từng row của 1
                                                       // trang
                    User student = new User();// Với mỗi dòng tạo một đối tượng
                                              // student
                    
                    Iterator cellIterator = row.cellIterator();// lấy cells của
                                                               // row
                    
                    int flag = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = (Cell) cellIterator.next();// chạy từng cell
                        flag = flag + 1;
                        if (cell.getCellType() == 1) {// là kiểu String
                            if (flag == 1) {
                                student.setEmail(cell.getStringCellValue());
                            } else {
                                student.setFullName(cell.getStringCellValue());
                            }
                        } else {// là kiểu Date
                            String dateFormat=null;
                            try{
                                Date date = cell.getDateCellValue();
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                 dateFormat = Validate.formatDate(cal.get(Calendar.YEAR) + "-"
                                        + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE));
                            }
                            catch(Exception e){
                                dateFormat=null;
                            }
                            
                            student.setBirthDay(dateFormat);
                        }
                    }
                    // đọc xong một đối tượng student
                    listStudent.add(student);
                }
            }
            return listStudent;
            
        } catch (IOException e) {
            
            e.printStackTrace();
            return null;
        }
    }
    
    //
    public boolean createCourse(Course c) {
        Session session = this.sessionFactory.getCurrentSession();
        try {
            
            session.beginTransaction();
            session.persist(c);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            return false;
        }
    }
    
    public Course getCourse(String coursename) {
        Session session = sessionFactory.getCurrentSession();
        try {
   
            Course c = (Course) session.createCriteria(Course.class).add(Restrictions.eq("courseName", coursename))
                    .uniqueResult();
                    
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public int checkCourseUpdate(String courseName){
        Session session = sessionFactory.getCurrentSession();
        try{
            Criteria crit = session.createCriteria(Course.class);
            crit.add(Restrictions.eq("courseName", courseName));
            List<Course> listCourse = crit.list();
            return listCourse.size();
        }catch(Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
            return 0;
        }
        
    }
    
    public Course getCourse(int id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Course c = (Course) session.createCriteria(Course.class).add(Restrictions.eq("idCourse", id))
                    .uniqueResult();
                    
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return null;
        }
    }
    
    public Member checkUserofCouse(User u, Course c) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Member m = (Member) session.createCriteria(Member.class)
                    .add(Restrictions.and(Restrictions.eq("course", c), Restrictions.eq("user", u))).uniqueResult();
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return null;
        }
    }
    
    // delete member
    @SuppressWarnings("unchecked")
	private boolean deleteMemberofCourse(Course c) {
        Session session = sessionFactory.getCurrentSession();
        try {
            Criteria crit = session.createCriteria(Member.class);
            crit.add(Restrictions.eq("course", c));
            List<Member> listMember = crit.list();
            for (Member m : listMember) {
                session.delete(m);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
    }
    
    // delete Course
    public boolean deleteCourse(int courseID){
        Session session = sessionFactory.getCurrentSession();
       
            Course c = getCourse(courseID);
            if(c!=null){
                boolean result = deleteMemberofCourse(c);
                if(result){
                    try{
                        Course course= (Course) session.createCriteria(Course.class)
                                .add(Restrictions.eq("idCourse",courseID)).uniqueResult();
                        session.delete(course);
                        return true;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        session.getTransaction().rollback();
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    //update course
    public String updateCourse(Course c){
        Session session = sessionFactory.getCurrentSession();
        Course course = getCourse(c.getIdCourse());
        String error = null;
        if(course.getCourseName().equals(c.getCourseName())){//khong thay doi name course
            try{
                course.setDescription(c.getDescription());
                session.beginTransaction();
                session.saveOrUpdate(course);
                
            } catch (Exception e) {
                e.printStackTrace();
                session.getTransaction().rollback();
                error= "Update not success!";
            }
        }
        else{// co thay doi name course
              if(checkCourseUpdate(c.getCourseName())==0){
                  try{
                      course.setCourseName(c.getCourseName());
                      course.setDescription(c.getDescription());
                      session.beginTransaction();
                      session.saveOrUpdate(course);
                      
                  } catch (Exception e) {
                      e.printStackTrace();
                      session.getTransaction().rollback();
                      error= "Update not success!";
                  }
              }
              else{
                  error = "Course exist!";
              }
        }
        return error;
    }
}

