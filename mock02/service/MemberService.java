package mock02.service;

import java.util.List;

import mock02.dao.MemberDAO;
import mock02.model.Course;
import mock02.model.Member;
import mock02.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("memberService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MemberService {
    @Autowired
    private MemberDAO memberDao;
    //get list member by user 
    public List<Member> listMember_User(User user) {
	return memberDao.listMember_User(user);
    }
    //get member by user and cours
    public Member getMemberByUserAndCourse(User user, Course course) {
	return memberDao.getMemberByUserAndCourse(user, course);
    }
    //get mmemer by id member
    public Member getMember(Integer idMember) {
   	return memberDao.getMember(idMember);
       }
}
