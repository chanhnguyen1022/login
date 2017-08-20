package mock02.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mock02.model.Course;
import mock02.model.Member;
import mock02.model.Score;
import mock02.model.User;
import mock02.validator.Validate;

/*
 * TramTran(^^)
 */

@Repository("manageStudentDAO")
@Transactional
public class ManageStudentDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// Insert Student into database
	public String insertUser(User user, Course course) {
	    String error = null;
	    Session session = this.sessionFactory.getCurrentSession();
	    String valid = Validate.formatUser(user);
	    if(valid==null){
	        if(checkEmail(user.getEmail())==0){//email chua ton tai
	            try {
	                session.beginTransaction();
	                user.setUserName(user.getEmail());
	                user.setPassword(user.getEmail());
	                user.setRole("ROLE_STUDENT");
	                user.setStatus(1);
	                session.persist(user);
	                
	                User userPer = getUser(user.getUserName());
	                insertMember(userPer, course);
	                error = null;
	            } catch (Exception e) {
	                System.out.println(e.getMessage());
	                session.getTransaction().rollback();
	                error = "Insert user not success!";
	            }
	        }
	        else{//email da ton tai
	            error = "Email exist!";
	        }
	    }
	    else{
	        error = valid;
	    }
	   
		return error; 

	}

	// Insert Member into database
	public boolean insertMember(User user, Course course) {

		Session session = this.sessionFactory.getCurrentSession();
		Member member = new Member();
		member.setUser(user);
		member.setCourse(course);
		try {

			session.beginTransaction();
			
			session.persist(member);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			session.getTransaction().rollback();
			return false;
		}
	}

	// update Student
	public String updateUser(User user,String passwordOld) {
	    String error = null;
	    Session session = this.sessionFactory.getCurrentSession();
	    User u = getUser(user.getUserID());
	    String valid = Validate.formatUser(user);
	    if(u!=null){
	        if(valid ==null){
	            if(passwordOld!=null ){// co thay doi mat khau
	                if(u.getPassword().equals(passwordOld)){
	                    if(u.getEmail().equals(user.getEmail())){//không thay đổi email
	                        u.setPassword(user.getPassword());
	                        u.setFullName(user.getFullName());
	                        u.setBirthDay(user.getBirthDay());
	                        session.saveOrUpdate(u);
	                    }
	                    else{//có thay đổi email
	                        if(checkEmail(user.getEmail())==0){
	                            u.setPassword(user.getPassword());
	                             u.setEmail(user.getEmail());
	                             u.setUserName(user.getEmail());
	                             u.setFullName(user.getFullName());
	                             u.setBirthDay(user.getBirthDay());
	                             session.saveOrUpdate(u);
	                        }
	                        else{
	                            error = "Email exist!";
	                        }
	                    }
	                }
	                else{
	                    error = "Error!Password wrong!";
	                }
	            }
	            else{//khong thay doi mat khau
	                if(u.getEmail().equals(user.getEmail())){//không thay đổi email
	                    u.setFullName(user.getFullName());
	                    u.setBirthDay(user.getBirthDay());
	                    session.saveOrUpdate(u);
	                }
	                else{//có thay đổi email
	                    if(checkEmail(user.getEmail())==0){
	                         u.setEmail(user.getEmail());
	                         u.setUserName(user.getEmail());
	                         u.setFullName(user.getFullName());
	                         u.setBirthDay(user.getBirthDay());
	                         session.saveOrUpdate(u);
	                    }
	                    else{
	                        error = "Email exist!";
	                    }
	                }
	            }
	        }
	        else{
	            error = valid;
	        }
	        
	    }//
	    else{
	        error="Error:User not exist!";
	    }
	    
		return error;
	}
	// delete Student
	public boolean deleteUserofCourse(User u,Course c) {
	    Session session = sessionFactory.getCurrentSession();
	    if(u==null || c==null){
	        return false;
	    }
	    else{
	        try{
	            Member m = (Member)session.createCriteria(Member.class).
	                    add(Restrictions.and(Restrictions.eq("course", c), Restrictions.eq("user", u))).uniqueResult();
	            if(m==null){return false;}
	            session.delete(m);
	           return true;
	        }
	        catch(Exception e){
	            e.printStackTrace();
	            session.getTransaction().rollback();
	            return false;
	        }
	    }
       
	}

	// getUser of userName
	@SuppressWarnings("unchecked")
	public int checkEmail(String email) {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> users = new ArrayList<>();
		try {
			Criteria crit = session.createCriteria(User.class);
			crit.add(Restrictions.eq("email", email));
			users = crit.list();
			System.out.println(users.size());
			return users.size();
		} catch (Exception e) {
			e.printStackTrace();
			// Rollback trong trư�?ng hợp có lỗi xẩy ra.
			session.getTransaction().rollback();
			return 0;
		}

	}
	public User getUser(String username){
	    Session session = this.sessionFactory.getCurrentSession();
        try {
            User user = (User) session.createCriteria(User.class).
                    add(Restrictions.eq("userName", username)).uniqueResult();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("get user not succuess!");
            // Rollback trong trư�?ng hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
            return null;
        }

	}
    // get List student of teacher
    public List<User> getListStudent(User teacher,Course course) {
       
        List<User> listStudent = new ArrayList<>();
        List<Course> listCourse = getListCourse(teacher);
        for(Course c:listCourse){
           
            if(c.getIdCourse()!= course.getIdCourse()){
               
                List<User> listS = getListStudent(c);
                for(User u:listS){
                    if(checkUserofCouse(u, course)== null && !listStudent.contains(u)){
                        listStudent.add(u);
                    }
                   
                }
                
                
            }
        }
      
        return listStudent;
    }

    private Member checkUserofCouse(User u,Course c){
        Session session = sessionFactory.getCurrentSession();
        try{
            Member m = (Member)session.createCriteria(Member.class).
                    add(Restrictions.and(Restrictions.eq("course", c), Restrictions.eq("user", u))).uniqueResult();
           return m;
        }
        catch(Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
            return null;
        }
    }
	// get Listcourse of teacher
	@SuppressWarnings("unchecked")
	public List<Course> getListCourse(User teacher) {
		List<Course> listCourse = new ArrayList<>();

		Session session = this.sessionFactory.getCurrentSession();
		try {
			if (!session.isOpen()) {
				sessionFactory.openSession();
			}
			Criteria crit = session.createCriteria(Member.class);
			crit.add(Restrictions.eq("user", teacher));
			List<Member> members = crit.list();

			for (Member m : members) {
			    //m.getCourse().getCourseName()
				listCourse.add(m.getCourse());

			}

			return listCourse;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get user not succuess!");
			session.getTransaction().rollback();
			return null;
		}
	}

	// get Course id
	@SuppressWarnings("unchecked")
	public Course getCourse(int id) {
		Course course = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {

			Criteria crit = session.createCriteria(Course.class);
			crit.add(Restrictions.eq("idCourse", id));
			List<Course> courses = crit.list();
			for (Course c : courses) {
				course = c;
				break;
			}
			return course;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get course not succuess!");
			session.getTransaction().rollback();
			return null;
		}
	}

	// get List Student of Course
	@SuppressWarnings("unchecked")
	public List<User> getListStudent(Course course) {
		List<User> listStudent = new ArrayList<>();
		List<User> listUser = new ArrayList<>();

		Session session = this.sessionFactory.getCurrentSession();
		try {
			if (!session.isOpen()) {
				sessionFactory.openSession();
			}
			Criteria crit = session.createCriteria(Member.class);
			crit.add(Restrictions.eq("course", course));
			List<Member> members = crit.list();

			for (Member m : members) {
				listUser.add(m.getUser());
			}

			for (User u : listUser) {
				if ("ROLE_STUDENT".equals(u.getRole()) && u.getStatus() == 1) {
					listStudent.add(u);
				}
			}

			return listStudent;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get user not succuess!");
			// Rollback trong trường hợp có lỗi xảy ra.
			session.getTransaction().rollback();
			return null;
		}
	}

	// get User of UserID
	@SuppressWarnings("unchecked")
	public User getUser(int userID) {
		User user = null;
		Session session = this.sessionFactory.getCurrentSession();
		try {

			Criteria crit = session.createCriteria(User.class);
			crit.add(Restrictions.eq("userID", userID));
			List<User> users = crit.list();
			for (User u : users) {
				user = u;
				break;
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("get user not succuess!");
			// Rollback trong trư�?ng hợp có lỗi xẩy ra.
			session.getTransaction().rollback();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Score> getListScore(User student,Course course){
	    Session session = this.sessionFactory.getCurrentSession();
	    
	    Member member = getMember(student, course);
	    
	    if(member!=null){
	        
	        List<Score> scores = new ArrayList<>();
	        try{
	            Criteria crit = session.createCriteria(Score.class);
	            crit.add(Restrictions.eq("member", member));
	             scores = crit.list();
	             return scores;
	        }
	        catch (Exception e){
	            e.printStackTrace();
	            System.out.println("get list scores not succuess!");
	            // Rollback trong trư�?ng hợp có lỗi xẩy ra.
	            session.getTransaction().rollback();
	            return null;
	        }
	       
	    }
	    else{
	        return null;
	    }
	    
	}
	
	@SuppressWarnings("unchecked")
	private Member getMember(User user,Course course){
	    Member member = null;
        Session session = this.sessionFactory.getCurrentSession();
        try {

            Criteria crit = session.createCriteria(Member.class);
            crit.add(Restrictions.and(Restrictions.eq("user", user), Restrictions.eq("course", course)));
            List<Member> members = crit.list();
            for (Member m : members) {
                member = m;
                break;
            }
            return member;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("get Member of user and course not succuess!");
            // Rollback trong trư�?ng hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
            return null;
        }

	}
}
