package mock02.dao;

import java.util.List;

import mock02.model.Course;
import mock02.model.Subject;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 21, 2015
 */
@Repository("subjectDAO")
@Transactional
public class SubjectDAO {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Subject> getListSubjectByCourse(Course course) {
		Session session = sessionFactory.getCurrentSession();
		List<Subject> result = (List<Subject>) session.createCriteria(Subject.class)
				.add(Restrictions.eq("course", course)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.addOrder(Order.asc("idSubject")).list();
		return result;
	}

	public void createSubject(String name, Integer idCourse) {
		Session session = sessionFactory.getCurrentSession();
		Course course = new Course();
		course.setIdCourse(idCourse);
		Subject subject = new Subject(course, name);
		session.saveOrUpdate(subject);
	}

	public void deleteSubject(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete Subject where id=:id").setInteger("id", id).executeUpdate();
	}

	public void editSubject(Integer id, String name) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("update Subject set subjectName = :name where idSubject = :id")
				.setInteger("id", id).setString("name", name).executeUpdate();
	}
}
