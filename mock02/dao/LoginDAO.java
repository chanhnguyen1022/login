package mock02.dao;

import mock02.model.User;

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
 *@version 1.0 Dec 14, 2015
 */
@Repository("loginDAO")
@Transactional
public class LoginDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// Get fullName from userName
	public String getFullName(String userName) {
		Session session = sessionFactory.getCurrentSession();
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("fullName"));
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("userName", userName));
		crit.setProjection(projList);
		crit.setMaxResults(1);
		String name = (String) crit.uniqueResult();
		return name;
	}
	//get user from username
	public User getUser(String userName)
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("userName", userName));
		crit.setMaxResults(1);
		User user1 = (User) crit.uniqueResult();
		return user1;
	}
}
