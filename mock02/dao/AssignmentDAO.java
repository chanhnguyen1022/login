package mock02.dao;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mock02.model.Assignment;
import mock02.model.Course;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 25, 2015
 **/
@Repository("assignmentDAO")
@Transactional
public class AssignmentDAO {
	@Autowired
	SessionFactory sessionFactory;

	Log logger = LogFactory.getLog(AssignmentDAO.class);

	// get assignment by id assignment
	public Assignment getAssigment(int idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		Assignment result = (Assignment) session
				.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", idAssignment))
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("idAssignment").as("idAssignment"))
								.add(Projections.property("assignmentName").as("assignmentName"))
								.add(Projections.property("deadline").as("deadline"))
								.add(Projections.property("redoTime").as("redoTime"))
								.add(Projections.property("contentAssignment").as("contentAssignment"))
								.add(Projections.property("attachFileName").as("attachFileName")))
				.setResultTransformer(Transformers.aliasToBean(Assignment.class)).uniqueResult();
		return result;
	}

	public String getOpenAssignmentTypeById(int idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		String type = (String) session.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", idAssignment)).add(Restrictions.isNotNull("openTime"))
				.setProjection(Projections.property("type")).uniqueResult();
		return type;
	}

	public Assignment getAssignmentNameAndDeadlineById(Integer idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		Assignment result = (Assignment) session
				.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", idAssignment))
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("assignmentName").as("assignmentName"))
								.add(Projections.property("deadline").as("deadline")))
				.setResultTransformer(Transformers.aliasToBean(Assignment.class)).uniqueResult();
		return result;
	}

	// get list assignment by course
	// Chỉ get các field cần thiết không get hết tất cả (commented by khuê)
	@SuppressWarnings("unchecked")
	public List<Assignment> getListAssignmentByCourse(Course course) throws ParseException {
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = (List<Object>) session
				.createCriteria(Assignment.class)
				.add(Restrictions.eq("course", course))
				.setProjection(
						Projections.projectionList().add(Projections.property("idAssignment"))
								.add(Projections.property("assignmentName"))
								.add(Projections.property("openTime")).add(Projections.property("deadline"))
								.add(Projections.property("redoTime"))
								.add(Projections.property("contentAssignment"))
								.add(Projections.property("attachFileName"))
								.add(Projections.property("type"))).list();
		Iterator<Object> itr = result.iterator();
		List<Assignment> listAssignment = new ArrayList<Assignment>();
		Assignment assignment;
		while (itr.hasNext()) {
			assignment = new Assignment();
			Object[] obj = (Object[]) itr.next();
			assignment.setIdAssignment(Integer.parseInt(String.valueOf(obj[0])));
			assignment.setAssignmentName(String.valueOf(obj[1]));
			String timeOpen = String.valueOf(obj[2]);
			if ("null".compareTo(timeOpen) != 0)
				assignment.setOpenTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(timeOpen));
			assignment.setDeadline(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(String.valueOf(obj[3])));
			assignment.setRedoTime(Integer.parseInt(String.valueOf(obj[4])));
			assignment.setContentAssignment(String.valueOf(obj[5]));
			if ("null".compareTo(String.valueOf(obj[6])) != 0)
				assignment.setAttachFileName(String.valueOf(obj[6]));
			assignment.setType(String.valueOf(obj[7]));
			listAssignment.add(assignment);
		}
		return listAssignment;
	}

	@SuppressWarnings("unchecked")
	public List<Assignment> getListOpenAssignmentByCourse(Course course) throws ParseException {
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = (List<Object>) session
				.createCriteria(Assignment.class)
				.add(Restrictions.eq("course", course))
				.add(Restrictions.isNotNull("openTime"))
				.setProjection(
						Projections.projectionList().add(Projections.property("idAssignment"))
								.add(Projections.property("assignmentName"))
								.add(Projections.property("openTime")).add(Projections.property("deadline"))
								.add(Projections.property("redoTime"))
								.add(Projections.property("contentAssignment"))
								.add(Projections.property("attachFileName"))
								.add(Projections.property("type"))).list();
		Iterator<Object> itr = result.iterator();
		List<Assignment> listAssignment = new ArrayList<Assignment>();
		Assignment assignment;
		while (itr.hasNext()) {
			assignment = new Assignment();
			Object[] obj = (Object[]) itr.next();
			assignment.setIdAssignment(Integer.parseInt(String.valueOf(obj[0])));
			assignment.setAssignmentName(String.valueOf(obj[1]));
			String timeOpen = String.valueOf(obj[2]);
			if ("null".compareTo(timeOpen) != 0)
				assignment.setOpenTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(timeOpen));
			assignment.setDeadline(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(String.valueOf(obj[3])));
			assignment.setRedoTime(Integer.parseInt(String.valueOf(obj[4])));
			assignment.setContentAssignment(String.valueOf(obj[5]));
			if ("null".compareTo(String.valueOf(obj[6])) != 0)
				assignment.setAttachFileName(String.valueOf(obj[6]));
			assignment.setType(String.valueOf(obj[7]));
			listAssignment.add(assignment);
		}
		return listAssignment;
	}

	public void saveOrUpdateAssignment(Assignment assignment) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(assignment);
		session.flush();
	}

	public void updateNotChangeFileAssignment(Assignment assignment) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(
				"update Assignment set course=:course, assignmentName=:name, openTime=:openTime, deadline=:deadline,"
						+ "redoTime=:redo, contentAssignment=:content, type=:type where idAssignment=:id")
				.setInteger("course", assignment.getCourse().getIdCourse())
				.setString("name", assignment.getAssignmentName())
				.setTimestamp("openTime", assignment.getOpenTime())
				.setTimestamp("deadline", assignment.getDeadline())
				.setInteger("redo", assignment.getRedoTime()).setString("type", assignment.getType())
				.setString("content", assignment.getContentAssignment())
				.setInteger("id", assignment.getIdAssignment()).executeUpdate();
		session.flush();
	}

	public void deleteAssignment(Integer idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("delete Assignment where idAssignment=:id").setInteger("id", idAssignment)
				.executeUpdate();
		session.flush();
	}

	public Blob getAttachFileById(Integer idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		Blob blob = (Blob) session.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", idAssignment))
				.setProjection(Projections.projectionList().add(Projections.property("attachFile")))
				.uniqueResult();
		return blob;
	}

	public Blob getFileById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Blob result = (Blob) session.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", id))
				.setProjection(Projections.projectionList().add(Projections.property("attachFile")))
				.uniqueResult();
		return result;
	}

	public String getFileNameById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", id))
				.setProjection(Projections.projectionList().add(Projections.property("attachFileName")))
				.uniqueResult();
		return result;
	}

	public Integer getRedoTimeById(Integer idAssignment) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session.createCriteria(Assignment.class)
				.add(Restrictions.eq("idAssignment", idAssignment))
				.setProjection(Projections.projectionList().add(Projections.property("redoTime")))
				.uniqueResult();
		return result;
	}
}
