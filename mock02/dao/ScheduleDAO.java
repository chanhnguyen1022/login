package mock02.dao;

import java.sql.Date;
import java.util.List;

import mock02.model.Course;
import mock02.model.Schedule;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 9, 2015
 */
@Repository("scheduleDAO")
@Transactional
public class ScheduleDAO {
	@Autowired
	private SessionFactory sessionFactory;

	// Save a schedule
	public void saveOrUpdateSchedule(Schedule schedule) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(schedule);
	}

	// Get list schedule by id course
	@SuppressWarnings("unchecked")
	public List<Schedule> getListSchedule(Course course) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Schedule.class);
		crit.add(Restrictions.eq("course", course));
		List<Schedule> results = (List<Schedule>) crit.list();
		return results;
	}

	// Delete schedule by id course and date
	public void deleteSchedule(Course course, Date date) {
		Session session = sessionFactory.getCurrentSession();
		Schedule schedule = findScheduleByDateAndIdCourse(course, date);
		session.delete(schedule);
	}

	// get schedule by id course and date
	public Schedule findScheduleByDateAndIdCourse(Course course, Date date) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Schedule.class);
		crit.add(Restrictions.eq("course", course));
		crit.add(Restrictions.eq("date", date));
		crit.setMaxResults(1);
		Schedule result = (Schedule) crit.uniqueResult();
		return result;
	}
	
	
}
