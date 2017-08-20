package mock02.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mock02.validator.Validate;
import mock02.model.User;

@Repository("manageUserDAO")
@Transactional
public class ManageUserDAO {
	@Autowired
	private SessionFactory sessionFactory;

	// Insert user to database
	public String insertUser(User user) {
		String error = null;
		Session session = this.sessionFactory.getCurrentSession();
		String valid = Validate.formatUser(user);
		if (valid == null) {
			if (checkUser(user.getUserName()) == 0) {// user name chua co trong database
				try {
					session.save(user);
					session.flush();
				} catch (Exception e) {
					e.printStackTrace();
					error = "Insert user not success!";
				}
			} else { // Username da co trong database
				error = "Username exist!";
			}
		} else {
			error = valid;
		}
		return error;
	}

	public String updateUser(User user) {
		String error = null;
		Session session = this.sessionFactory.getCurrentSession();
		User u = getUser(user.getUserID());
		String valid = Validate.formatUser(user);
		if (u != null) {
			if (valid == null) {
				u.setFullName(user.getFullName());
				u.setBirthDay(user.getBirthDay());
				u.setRole(user.getRole());
				session.saveOrUpdate(u);

			} else {
				error = "Update fail";

			}
		}
		return error;

	}

	public boolean deleteUser(User userID) {
		Session session = sessionFactory.getCurrentSession();
		if (userID == null) {
			return false;
		} else
			try {
				User u = (User) session.createCriteria(User.class).add(Restrictions.eq("userID", userID))
						.uniqueResult();
				session.delete(u);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				session.getTransaction().rollback();
				return false;
			}

	}

	@SuppressWarnings("unchecked")
	public int checkUser(String userName) {
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Criteria crit = session.createCriteria(User.class);
			crit.add(Restrictions.eq("userName", userName));
			List<Object> lst = crit.list();
			return lst.size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

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
			// Rollback trong khi loi xay ra
			session.getTransaction().rollback();
			return null;
		}
	}
}
