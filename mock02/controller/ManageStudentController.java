package mock02.controller;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mock02.model.Course;
import mock02.model.Score;
import mock02.model.User;
import mock02.service.ManageCourseService;
import mock02.service.ManageStudentService;

/*
 * TramTran(^^)
 */
@Controller
public class ManageStudentController {

    private HttpSession session;

    @Autowired
    ManageStudentService manageStudentService;
    @Autowired
    private ManageCourseService manageCourseService;

    @RequestMapping(value = "list_student.html", method = RequestMethod.GET)
    public String getListStudent(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	request.setAttribute("active", 3);

	// get course when teacher selected
	int id = (Integer) session.getAttribute("idcourse");
	Course course = manageStudentService.getCourse(id);
	session.setAttribute("Course", course);
	if (course != null) {
	    List<User> listStudent = manageStudentService
		    .getListStudent(course);

	    session.setAttribute("List_Student", listStudent);

	} else {
	    model.addAttribute("Error_Course", "Course not exist!");
	}
	// get student do teacher quan li
	User teacherCur = (User) session.getAttribute("teacher_cur");
	List<User> listStudent = manageStudentService.getListStudent(
		teacherCur, course);

	if (listStudent == null || listStudent.isEmpty()) {
	    model.addAttribute("students_teacher_error", "List Student Empty!");
	} else {
	    model.addAttribute("students_teacher", listStudent);
	}
	return "list_student";
    }

    @RequestMapping(value = "profile_student.html", method = RequestMethod.GET)
    public String profileStudent(
	    @RequestParam(value = "userID", required = true) String id,
	    HttpServletRequest request, HttpServletResponse response,
	    ModelMap model) {
	request.setAttribute("active", 3);
	session = request.getSession();

	int userID = -1;
	try {
	    userID = Integer.parseInt(id);
	} catch (Exception e) {
	    model.addAttribute("Error_Profile", "User not exist!");
	}
	//
	if (userID != -1) {
	    User studentDetail = manageStudentService.getUser(userID);
	    List<Score> listScore = new ArrayList<>();
	    if (studentDetail == null) {
		model.addAttribute("Error_Profile", "User not exist!");
	    } else {
		Course course = (Course) session.getAttribute("Course");
		listScore = manageStudentService.getListScore(studentDetail,
			course);

		model.addAttribute("Profile_Student", studentDetail);
		model.addAttribute("List_Score", listScore);
	    }

	}
	return "profile_student";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "insert_student.html", method = RequestMethod.POST)
    public @ResponseBody String insertStudent(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	// get attribute
	String email = request.getParameter("email");
	String fullName = request.getParameter("fullName");
	String birthDate = request.getParameter("birthDate");
	// create user
	User user = new User();
	;
	user.setEmail(email);
	user.setFullName(fullName);
	user.setBirthDay(birthDate);
	// get course when teacher selected
	Course course = (Course) session.getAttribute("Course");
	// insert user
	String result = manageStudentService.insertUser(user, course);
	if (result == null) {
	    List<User> listStudent = (List<User>) session
		    .getAttribute("List_Student");
	    listStudent.add(user);
	    return "1";
	} else {
	    model.addAttribute("Error_Insert", "Insert user not success!");
	    return result;
	}
    }

    @RequestMapping(value = "insert_student_course.html", method = RequestMethod.POST)
    public @ResponseBody String addStudent(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	// get course when teacher selected
	Course course = (Course) session.getAttribute("Course");
	int id = Integer.parseInt(request.getParameter("id"));
	User student = manageStudentService.getUser(id);
	boolean result = manageStudentService.insertMember(student, course);
	if (result) {
	    return "1";
	} else {
	    return "0";
	}

    }

    @RequestMapping(value = "check_username.html", method = RequestMethod.POST)
    public @ResponseBody String checkUserName(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	String userName = request.getParameter("userName");
	System.out.println(userName);
	User user = manageStudentService.getUser(userName);
	if (user == null) {
	    return "0";
	} else {
	    return "1";
	}

    }

    @RequestMapping(value = "update_user.html", method = RequestMethod.POST)
    public @ResponseBody String updateUser(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	// get attribute
	String userID = request.getParameter("userID");
	int id = -1;
	try {
	    id = Integer.parseInt(userID);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	String email = request.getParameter("email");
	String fullName = request.getParameter("fullName");
	String birthDate = request.getParameter("birthDate");
	// create user
	User user = new User();
	user.setUserID(id);
	user.setEmail(email);
	user.setFullName(fullName);
	user.setBirthDay(birthDate);
	String result = manageStudentService.updateUser(user, null);
	if (result == null) {
	    return "1";
	} else {
	    return result;
	}

    }

    @RequestMapping(value = "delete_user.html", method = RequestMethod.POST)
    public @ResponseBody String deleteUser(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	String id = request.getParameter("userID");
	int idcourse = (Integer) session.getAttribute("idcourse");

	int userID = -1;
	try {
	    userID = Integer.parseInt(id);

	} catch (Exception e) {
	    e.printStackTrace();
	    return "0";
	}
	User u = manageStudentService.getUser(userID);
	Course c = manageCourseService.getCourse(idcourse);
	if (u == null || c == null) {
	    return "0";
	} else {

	    boolean result = manageStudentService.deleteUserofCourse(u, c);
	    if (result) {
		return "1";
	    } else {
		return "0";
	    }
	}

    }

    @RequestMapping(value = "update_profile.html", method = RequestMethod.POST)
    public @ResponseBody String updateProfile(HttpServletRequest request,
	    HttpServletResponse response, ModelMap model) {
	session = request.getSession();
	String fullname = request.getParameter("fullName");
	String passNew = request.getParameter("new_pass");
	String password = request.getParameter("password");
	String birthDay = request.getParameter("birthDay");

	User user = (User) session.getAttribute("teacher_cur");
	if (password != null && !password.equals("")) {// co thay doi mat khau
	    user.setFullName(fullname);
	    user.setBirthDay(birthDay);
	    user.setPassword(passNew);
	    String result = manageStudentService.updateUser(user, password);
	    if (result == null) {
		return "1";
	    } else {
		return result;
	    }

	} else {// khong thay doi mat khau
	    user.setFullName(fullname);
	    user.setBirthDay(birthDay);
	    String result = manageStudentService.updateUser(user, null);
	    if (result == null) {
		return "1";
	    } else {
		return result;
	    }
	}
	// end
    }

}
