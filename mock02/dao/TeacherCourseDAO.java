package mock02.dao;

import mock02.model.Course;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 13, 2015
 */
@Repository("teacherCourseDAO")
@Transactional
public class TeacherCourseDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// Check idCourse, return true if exist
	public boolean isIdCourse(int idCourse) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Course.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("idCourse"));
		crit.add(Restrictions.eq("idCourse", idCourse));
		crit.setProjection(projList);
		crit.setMaxResults(1);
		if(crit.uniqueResult() != null)
			return true;
		return false;
	}
}
