package mock02.service;

import java.util.List;

import mock02.dao.DiscussionDAO;
import mock02.model.Course;
import mock02.model.Discussion;
import mock02.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 26, 2015
 **/
@Service("discussionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DiscussionService {
    @Autowired
    DiscussionDAO discussionDAO;

    public void addDiscusion(Discussion discussion) {
	discussionDAO.addDiscusion(discussion);
    }

    // get a discusion
    public Discussion getDiscussion(int idDiscussion) {
	return discussionDAO.getDiscussion(idDiscussion);
    }

    // get list discussion
    public List<Discussion> getListDiscussion() {
	return discussionDAO.getListDiscussion();
    }

    // delete a discussion
    public void deleteDiscussion(int idDiscussion) {
	discussionDAO.deleteDiscussion(idDiscussion);
    }

    // get discussion by member
    public List<Discussion> getDiscussionByMember(Member member) {
	return discussionDAO.getDiscussionByMember(member);
    }

    // get discussion by course

    public List<Discussion> getDiscussionByCourse(Course course) {
	return discussionDAO.getDiscussionByCourse(course);
    }

    // get discussion by member and counts
    public List<Discussion> getDiscussionByMemberAndCourse(Course course,
	    Member member) {

	return discussionDAO.getDiscussionByMemberAndCourse(course, member);
    }
}
