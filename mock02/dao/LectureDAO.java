package mock02.dao;

import mock02.model.Lecture;
import mock02.model.Subject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 21, 2015
 */
@Repository("lectureDAO")
@Transactional
public class LectureDAO {

	@Autowired
	SessionFactory sessionFactory;

	public boolean isIdLecture(int idLecture) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session.createCriteria(Lecture.class)
				.add(Restrictions.eq("idLecture", idLecture))
				.setProjection(Projections.projectionList().add(Projections.property("idLecture")))
				.uniqueResult();
		if (result == null)
			return false;
		return true;
	}

	public String getLectureNameByIdLecture(int idLecture) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(Lecture.class)
				.add(Restrictions.eq("idLecture", idLecture))
				.setProjection(Projections.projectionList().add(Projections.property("lectureName")))
				.uniqueResult();
		return result;
	}

	public Integer getLastId() {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session
				.createQuery("select idLecture from Lecture ORDER BY idLecture DESC").setMaxResults(1)
				.uniqueResult();
		return result;
	}

	public void addLecture(Integer idSubject, String name) {
		Session session = sessionFactory.getCurrentSession();
		Subject subject = new Subject();
		subject.setIdSubject(idSubject);
		Lecture lecture = new Lecture(subject, name);
		session.saveOrUpdate(lecture);
	}

	public void deleteLecture(Integer idLecture) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete Lecture where idLecture = :id").setInteger("id", idLecture)
				.executeUpdate();
	}
}
