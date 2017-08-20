package mock02.dao;

import java.util.List;

import mock02.model.Course;
import mock02.model.Discussion;
import mock02.model.Member;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 26, 2015
 **/
@Repository("discussionDAO")
@Transactional
public class DiscussionDAO {
    @Autowired
    private SessionFactory sessionFactory;

    // add discussion
    public void addDiscusion(Discussion discussion) {
	sessionFactory.getCurrentSession().saveOrUpdate(discussion);
    }

    // get a discusion
    public Discussion getDiscussion(int idDiscussion) {
	return (Discussion) sessionFactory.getCurrentSession().get(
		Discussion.class, idDiscussion);
    }

    // get list discussion
    @SuppressWarnings("unchecked")
    public List<Discussion> getListDiscussion() {
	return (List<Discussion>) sessionFactory.getCurrentSession()
		.createCriteria(Discussion.class).list();
    }

    // delete a discussion
    public void deleteDiscussion(int idDiscussion) {
	sessionFactory.getCurrentSession()
		.createQuery("DELETE FROM Discussion WHERE idDiscussion=" + idDiscussion)
		.executeUpdate();
    }

    // get discussion by member
    @SuppressWarnings("unchecked")
    public List<Discussion> getDiscussionByMember(Member member) {
	Session session = sessionFactory.getCurrentSession();
	List<Discussion> listDiscussion = (List<Discussion>) session
		.createCriteria(Discussion.class)
		.add(Restrictions.eq("member", member)).list();
	return listDiscussion;
    }

    // get discussion by course
    @SuppressWarnings("unchecked")
    public List<Discussion> getDiscussionByCourse(Course course) {
	Session session = sessionFactory.getCurrentSession();
	List<Discussion> listDiscussion = (List<Discussion>) session
		.createCriteria(Discussion.class)
		.add(Restrictions.eq("course", course)).list();
	return listDiscussion;
    }

    // get discussion by member and counts
    @SuppressWarnings("unchecked")
    public List<Discussion> getDiscussionByMemberAndCourse(Course course,
	    Member member) {
	Session session = sessionFactory.getCurrentSession();
	List<Discussion> listDiscussion = (List<Discussion>) session
		.createCriteria(Discussion.class)
		.add(Restrictions.eq("member", member))
		.add(Restrictions.eq("course", course)).list();
	return listDiscussion;
    }

}
