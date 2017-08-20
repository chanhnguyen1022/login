package mock02.dao;

import java.util.ArrayList;
import java.util.List;

import mock02.model.Course;
import mock02.model.Member;
import mock02.model.User;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("memberDao")
@Transactional
public class MemberDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	// lay member theo user
	// lay
	public List<Member> listMember_User(User user) {
		List<Member> listMember = new ArrayList<Member>();
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Member.class);
		crit.add(Restrictions.eq("user", user));
		listMember = (List<Member>) crit.list();
		return listMember;
	}

	// get member by user and course
	public Member getMemberByUserAndCourse(User user, Course course) {
		Session session = sessionFactory.getCurrentSession();
		Member member = (Member) session.createCriteria(Member.class)
				.add(Restrictions.eqOrIsNull("course", course))
				.add(Restrictions.eqOrIsNull("user", user)).uniqueResult();
		return member;
	}

	Integer getIdUserByIdMember(Integer idMember) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session.createCriteria(Member.class)
				.add(Restrictions.eq("idMember", idMember))
				.setProjection(Projections.property("user")).uniqueResult();
		return result;
	}

	public Member getMember(Integer idMember) {
		return (Member) sessionFactory.getCurrentSession().get(Member.class,
				idMember);
	}

	public String getFullNameById(Integer idMember) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(Member.class)
				.add(Restrictions.eq("idMember", idMember))
				.createAlias("user", "u")
				.setProjection(Projections.property("u.fullName"))
				.uniqueResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCourseNameByIdUser(Integer idUser) {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		user.setUserID(idUser);
		List<String> result = (List<String>) session
				.createCriteria(Member.class)
				.add(Restrictions.eq("user", user)).createAlias("course", "c")
				.setProjection(Projections.property("c.courseName")).list();
		return result;
	}
	
	
}
