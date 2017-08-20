package mock02.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mock02.model.Course;
import mock02.model.Discussion;
import mock02.model.Member;
import mock02.model.Participation;
import mock02.model.User;
import mock02.service.CourseService;
import mock02.service.DiscussionService;
import mock02.service.MemberService;
import mock02.service.ParticipationService;
import mock02.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author: ThaiHa
 * @verision:1.0 Jan 3, 2016
 **/
@Controller
@SessionAttributes({ "idcourse" })
public class TeacherDiscussionController {
    @Autowired
    DiscussionService discussionService;

    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    MemberService memberService;
    @Autowired
    ParticipationService participationService;

    @RequestMapping(value = "teacher_discussions")
    String getListDiscussions(ModelMap model,
	    @ModelAttribute("idcourse") Integer courseID, HttpSession session,
	    User user, HttpServletRequest request) {
	if (courseID == null)
	    return "redirect:student_grid_home.html";
	session = request.getSession();
	// get user by attribute
	user = (User) session.getAttribute("teacher_cur");
	// get course từ id course
	Course course = courseService.getCourse(courseID);
	// get member by course and user
	Member thisMember = new Member();
	thisMember = memberService.getMemberByUserAndCourse(user, course);
	if (thisMember == null)
	    return "redirect:student_grid_home.html";
	List<Discussion> listDiscussion = discussionService
		.getDiscussionByCourse(course);
	// get list user names create discussion
	List<String> listUserCreateDiscussion = new ArrayList<String>();
	// get list number Answers discussion
	List<Integer> listNumberAnswers = new ArrayList<Integer>();
	for (int i = 0; i < listDiscussion.size(); i++) {
	    Member member = new Member();
	    member = listDiscussion.get(i).getMember();
	    Member member1 = new Member();
	    member1 = memberService.getMember(member.getIdMember());
	    User userCreateDiscussion = new User();
	    userCreateDiscussion = member1.getUser();
	    listUserCreateDiscussion.add(userCreateDiscussion.getFullName());
	    List<Participation> listParticipation = new ArrayList<Participation>();
	    listParticipation = participationService
		    .getListPaticipations(listDiscussion.get(i));
	    Integer numberAnswer = listParticipation.size();
	    listNumberAnswers.add(numberAnswer);
	}
	// get list number Answers discussion
	model.addAttribute("idMember", thisMember.getIdMember());
	model.addAttribute("fullNameCreateDiscussion", user.getFullName());
	model.addAttribute("listNumberAnswers", listNumberAnswers);
	model.addAttribute("listUserCreateDiscussion", listUserCreateDiscussion);
	model.addAttribute("listDiscussion", listDiscussion);
	model.addAttribute("active", 6);
	model.addAttribute("course", course);
	return "teacher_discussions";
    }

    // vao trong chi tiet discussion
    @RequestMapping(value = "teacher_discussion")
    String getListDiscussion(
	    ModelMap model,
	    @ModelAttribute("idcourse") int courseID,
	    HttpSession session,
	    User user,
	    HttpServletRequest request,
	    @RequestParam(value = "idDiscussion", required = false) int idDiscussion) {
	session = request.getSession();
	// get user by attribute
	user = (User) session.getAttribute("teacher_cur");
	// get course từ id course
	Course course = courseService.getCourse(courseID);
	// get member by course and user
	Member thisMember = new Member();
	thisMember = memberService.getMemberByUserAndCourse(user, course);
	// get discussion bằng id discussion
	Discussion currentDiscussion = discussionService
		.getDiscussion(idDiscussion);
	// get member create this discussion
	Member memberCreatDiscussion = currentDiscussion.getMember();
	// get member by session
	Member member1 = memberService.getMember(memberCreatDiscussion
		.getIdMember());
	// get user create this discussion
	User userCreateDiscussion = member1.getUser();
	// get list Participation from current discussion
	List<Participation> listParticipation = participationService
		.getListPaticipations(currentDiscussion);
	// get list user participation in this discussion
	List<Member> listMemberCreatParticipation = new ArrayList<Member>();
	for (int i = 0; i < listParticipation.size(); i++) {
	    Member member = new Member();
	    member = listParticipation.get(i).getMember();
	    listMemberCreatParticipation.add(member);
	}
	// get listUser create participation on this discussion
	List<User> listUserCreatParticipation = new ArrayList<User>();
	for (int i = 0; i < listParticipation.size(); i++) {
	    Member member = new Member();
	    member = memberService.getMember(listMemberCreatParticipation
		    .get(i).getIdMember());
	    User userCreatParticipation = new User();

	    userCreatParticipation = member.getUser();
	    listUserCreatParticipation.add(userCreatParticipation);
	}
	model.addAttribute("fullNameComment", user.getFullName());
	// user
	model.addAttribute("listUserCreatParticipation",
		listUserCreatParticipation);
	// user
	model.addAttribute("userCreateDiscussion", userCreateDiscussion);
	// member
	model.addAttribute("idMember", thisMember.getIdMember());
	// id discussion
	model.addAttribute("idDiscussion", idDiscussion);
	// atctive
	model.addAttribute("active", 6);
	// participation
	model.addAttribute("listParticipation", listParticipation);
	// discussion
	model.addAttribute("currentDiscussion", currentDiscussion);
	// course
	model.addAttribute("course", course);
	return "teacher_discussion";
    }

    @RequestMapping("deleteDiscussion")
    View deleteSubject(
	    @RequestParam(value = "idDiscussion", required = false) String idDiscussionStr) {
	RedirectView redirectView;
	redirectView = new RedirectView("teacher_discussions.html");
	redirectView.setExposeModelAttributes(false);
	try {
	    Integer idDiscussion = Integer.parseInt(idDiscussionStr);
	    discussionService.deleteDiscussion(idDiscussion);
	    return redirectView;
	} catch (NumberFormatException e) {
	    return redirectView;
	}
    }
}
