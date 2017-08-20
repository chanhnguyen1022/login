package mock02.dao;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mock02.model.Lecture;
import mock02.model.Resource;

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
@Repository("resourceDAO")
@Transactional
public class ResourceDAO {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Resource> getListResourceByLecture(Lecture lecture) {
		Session session = sessionFactory.getCurrentSession();
		List<Resource> result = session.createCriteria(Resource.class)
				.add(Restrictions.eq("lecture", lecture)).list();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getListResourceNameByLecture(Lecture lecture) {
		Session session = sessionFactory.getCurrentSession();
		List<Object> result = session
				.createCriteria(Resource.class)
				.add(Restrictions.eq("lecture", lecture))
				.setProjection(
						Projections.projectionList().add(Projections.property("idResource"))
								.add(Projections.property("type")).add(Projections.property("resourceTitle")))
				.list();
		Iterator<Object> itr = result.iterator();
		List<Resource> listResource = new ArrayList<Resource>();
		Resource resource;
		while (itr.hasNext()) {
			resource = new Resource();
			Object[] obj = (Object[]) itr.next();
			resource.setIdResource(Integer.parseInt(String.valueOf(obj[0])));
			resource.setType(String.valueOf(obj[1]));
			resource.setResourceTitle(String.valueOf(obj[2]));
			listResource.add(resource);
		}
		return listResource;
	}

	public Integer getLastId() {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session
				.createQuery("select idResource from Resource ORDER BY idResource DESC").setMaxResults(1)
				.uniqueResult();
		return result;
	}

	public void addResourceVideo(Integer idLecture, String value) {
		Lecture lecture = new Lecture();
		lecture.setIdLecture(idLecture);
		Resource resource = new Resource(lecture, "video", value);
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(resource);
		session.flush();
	}

	public void deleteResource(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Resource resource = new Resource();
		resource.setIdResource(id);
		session.createQuery("delete from Resource where idResource=:id").setInteger("id", id).executeUpdate();
		session.flush();
	}

	public void editResourceVideo(Integer id, String value) {
		Session session = sessionFactory.getCurrentSession();
		Lecture lecture = new Lecture();
		Integer idLecture = getIdLectureByIdResource(id);
		lecture.setIdLecture(idLecture);
		Resource resource = new Resource(lecture, "video", value);
		resource.setIdResource(id);
		session.saveOrUpdate(resource);
		session.flush();
	}

	public Integer getIdLectureByIdResource(Integer idResource) {
		Session session = sessionFactory.getCurrentSession();
		Lecture result = (Lecture) session.createQuery("select lecture from Resource where idResource=?")
				.setParameter(0, idResource).setMaxResults(1).uniqueResult();
		int temp = result.getIdLecture();
		return temp;
	}

	public void addResourceDoc(String fileName, Blob value, Integer idLecture) {
		Session session = sessionFactory.getCurrentSession();
		Lecture lecture = new Lecture();
		lecture.setIdLecture(idLecture);
		Resource resource = new Resource(lecture, "document", fileName, value);
		session.saveOrUpdate(resource);
		session.flush();
	}

	public Blob getResourceDocById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Blob result = (Blob) session.createCriteria(Resource.class).add(Restrictions.eq("idResource", id))
				.setProjection(Projections.projectionList().add(Projections.property("resourceContent")))
				.uniqueResult();
		return result;
	}
	
	public String getResourceNameById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(Resource.class).add(Restrictions.eq("idResource", id))
				.setProjection(Projections.projectionList().add(Projections.property("resourceTitle")))
				.uniqueResult();
		return result;
	}
}
