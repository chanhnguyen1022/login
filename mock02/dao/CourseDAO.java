package mock02.dao;

import java.util.ArrayList;
import java.util.List;

import mock02.model.Course;
import mock02.model.Member;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("courseDao")
@Transactional
public class CourseDAO {
	@Autowired
	private SessionFactory sessionFactory;

	// them course vao db
	public void addCourse(Course course) {
		sessionFactory.getCurrentSession().saveOrUpdate(course);
	}

	// lay danh sach course
	@SuppressWarnings("unchecked")
	public List<Course> listCoursess() {
		return (List<Course>) sessionFactory.getCurrentSession().createCriteria(Course.class).list();
	}

	// lay course dua vao idcourse
	public Course getCourse(int courseID) {
		return (Course) sessionFactory.getCurrentSession().get(Course.class, courseID);
	}

	// xoa course
	public void deleteCourse(Course course) {

		sessionFactory.getCurrentSession()
				.createQuery("DELETE FROM Course WHERE CourseId ='" + course.getIdCourse() + "'")
				.executeUpdate();
	}

	// lay danh sach course dua vao id
	public List<Course> course_User(List<Member> member) {
		List<Course> courseList = new ArrayList<Course>();
		for (int i = 0; i < member.size(); i++) {
			Course course = new Course();
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(Course.class);
			crit.add(Restrictions.eq("idCourse", member.get(i).getCourse().getIdCourse()));
			course = (Course) crit.uniqueResult();
			// course=member.get(i).getCourse();
			courseList.add(course);
		}
		return courseList;
	}

	public Integer getIdMemberByIdUserAndIdCourse(Integer idUser, Integer idCourse) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session
				.createQuery("select idMember from Member where course =:idCourse and user =:idUser")
				.setInteger("idCourse", idCourse).setInteger("idUser", idUser).uniqueResult();
		return result;
	}
}
