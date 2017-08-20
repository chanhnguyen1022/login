package mock02.dao;

import java.util.List;

import mock02.model.User;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userDao")
@Transactional
public class UserDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public User getUser(int userID) {
		return (User) sessionFactory.getCurrentSession()
				.get(User.class, userID);
	}

	// lay user dua vao password, username
	public User getUse_ByUserNamePasword(User user) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("userName", user.getUserName()));
		crit.add(Restrictions.eq("password", user.getPassword()));
		crit.setMaxResults(1);
		User user1 = (User) crit.uniqueResult();
		return user1;
	}

	public String getFullNameById(Integer idUser) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(User.class)
				.add(Restrictions.eq("userID", idUser))
				.setProjection(Projections.property("fullName")).uniqueResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<User> getListUser() {
		Session session = sessionFactory.getCurrentSession();
		List<User> results = session
				.createCriteria(User.class)
				.add(Restrictions.ne("role", "ROLE_ADMIN"))
				.setProjection(
						Projections.distinct(Projections
								.projectionList()
								.add(Projections.property("userID")
										.as("userID"))
								.add(Projections.property("userName").as(
										"userName"))
								.add(Projections.property("role").as("role"))
								.add(Projections.property("status")
										.as("status"))))
				.setResultTransformer(Transformers.aliasToBean(User.class))
				.list();
		return results;
	}
}
