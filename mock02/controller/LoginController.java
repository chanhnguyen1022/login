package mock02.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mock02.model.Course;
import mock02.model.User;
import mock02.service.CourseService;
import mock02.service.HandleRedirection;
import mock02.service.LoginService;
import mock02.service.ManageStudentService;
import mock02.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 12, 2015
 */
@Controller
@SessionAttributes({ "fullname", "iduser" })
public class LoginController {

	@Autowired
	HandleRedirection myAuthentication;
	@Autowired
	LoginService loginService;
	@Autowired
	ManageStudentService manageStudentService;
	@Autowired
	MemberService memberService;
	@Autowired
	CourseService courseService;

	// Create session attribute(fullname) at the fist time(if not exist)
	@ModelAttribute("fullname")
	String initAttribute() {
		return new String();
	}

	@RequestMapping(value = "login")
	String handleLogin() {
		return "login";
	}

	@RequestMapping(value = "loginfail")
	String handleLoginFail(ModelMap model) {
		model.addAttribute("error", "Username or password is invalid");
		return "login";
	}

	@RequestMapping(value = "teacher_grid_home")
	String accessTeacherGridHome(ModelMap model, Authentication authentication,
			HttpServletRequest request,
			@ModelAttribute("fullname") String fullName) {
		// get teacher when user login success and list teacher'course
		User teacher = manageStudentService.getUser(authentication.getName());
		request.getSession().setAttribute("teacher_cur", teacher);
		List<Course> listcourse = manageStudentService.getListCourse(teacher);
		request.setAttribute("List_Course", listcourse);

		if (fullName.trim().length() == 0) {
			fullName = loginService.getFullName(authentication.getName());
			model.addAttribute("fullname", fullName);
		}
		return "teacher_grid_home";
	}

	@RequestMapping(value = "teacher_list_home")
	String accessTeacherListHome(ModelMap model, Authentication authentication,
			HttpServletRequest request,
			@ModelAttribute("fullname") String fullName) {
		// get teacher when user login success and teacher'course
		User teacher = manageStudentService.getUser(authentication.getName());
		request.getSession().setAttribute("teacher_cur", teacher);
		List<Course> listcourse = manageStudentService.getListCourse(teacher);
		request.setAttribute("List_Course", listcourse);

		if (fullName.trim().length() == 0) {
			fullName = loginService.getFullName(authentication.getName());
			model.addAttribute("fullname", fullName);
		}
		return "teacher_list_home";
	}

	@RequestMapping(value = "student_grid_home")
	String accessStudentGridHome(ModelMap model, Authentication authentication,
			HttpServletRequest request,
			@ModelAttribute("fullname") String fullName) {
		// get teacher when user login success and list teacher'course
		User teacher = manageStudentService.getUser(authentication.getName());
		model.addAttribute("iduser", teacher.getUserID());
		request.getSession().setAttribute("teacher_cur", teacher);
		List<Course> listcourse = manageStudentService.getListCourse(teacher);
		request.setAttribute("List_Course", listcourse);

		if (fullName.trim().length() == 0) {
			fullName = loginService.getFullName(authentication.getName());
			model.addAttribute("fullname", fullName);
		}
		return "student_grid_home";
	}

	@RequestMapping(value = "student_list_home")
	String accessStudentListHome(ModelMap model, Authentication authentication,
			HttpServletRequest request,
			@ModelAttribute("fullname") String fullName) {
		// get teacher when user login success and list teacher'course
		User teacher = manageStudentService.getUser(authentication.getName());
		model.addAttribute("iduser", teacher.getUserID());
		request.getSession().setAttribute("teacher_cur", teacher);
		List<Course> listcourse = manageStudentService.getListCourse(teacher);
		request.setAttribute("List_Course", listcourse);

		if (fullName.trim().length() == 0) {
			fullName = loginService.getFullName(authentication.getName());
			model.addAttribute("fullname", fullName);
		}
		return "student_list_home";
	}

	@RequestMapping(value = "admin_home")
	String accessAdminHome(ModelMap model, Authentication authentication,
			HttpServletRequest request, @ModelAttribute("fullname") String fullName) {
		if (fullName.trim().length() == 0) {
			User user = manageStudentService.getUser(authentication
					.getName());
			model.addAttribute("iduser", user.getUserID());
			request.getSession().setAttribute("teacher_cur", user);
			model.addAttribute("active", 1);
			fullName = loginService.getFullName(authentication.getName());
			model.addAttribute("fullname", fullName);
		}
		return "admin_home";
	}

	// Handle request 403
	@RequestMapping(value = "forbidden")
	View forbiddenHandle(HttpServletRequest request,
			Authentication authentication) {
		RedirectView redirectView;
		String path = (String) request
				.getAttribute("javax.servlet.forward.request_uri");
		if (path != null && (path.endsWith("/") || path.endsWith("login.html"))) {
			String url = myAuthentication.determineTargetUrl(authentication);
			redirectView = new RedirectView(url);
			redirectView.setExposeModelAttributes(false);
			return redirectView;
		}
		redirectView = new RedirectView("rforb.html");
		redirectView.setExposeModelAttributes(false);
		return redirectView;
	}

	// Handle real 403
	@RequestMapping(value = "rforb")
	String realForbiddenHandle() {
		return "common/forbidden";
	}

	// Handle request 404
	@RequestMapping(value = "opps.html")
	String resourceNotFound() {
		return "common/oops";
	}

	// Handle request 405
	@RequestMapping(value = "illegalaccess.html")
	String illegalAccess() {
		return "common/illegalaccess";
	}

	// Handle request 500
	@RequestMapping(value = "error.html")
	String serverErrorPage() {
		return "common/error";
	}
}
