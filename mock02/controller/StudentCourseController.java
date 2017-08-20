package mock02.controller;

import javax.servlet.http.HttpServletRequest;

import mock02.model.Course;
import mock02.service.CourseService;
import mock02.service.ManageStudentService;
import mock02.service.TeacherCourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 24, 2015
 **/
@Controller
@SessionAttributes({ "idcourse", "course", "iduser", "idmember" })
public class StudentCourseController {
	@Autowired
	TeacherCourseService teacherCourseService;
	@Autowired
	ManageStudentService manageStudentService;
	@Autowired
	CourseService courseService;

	// Handle access teacher course if id course if illegal then go back to home
	// page
	@RequestMapping(value = "student_course")
	View accessCourseTeacher(@RequestParam(value = "id", required = false) String idStr,
			@RequestParam(value = "mode", required = false) String mode,
			@ModelAttribute("iduser") Integer idUser, ModelMap model, HttpServletRequest request) {
		RedirectView redirectView;
		// send request menu active
		request.setAttribute("active", 2);
		//

		if (idStr == null || idStr.trim().length() == 0 || !teacherCourseService.isIdCourse(idStr)) {

			if (mode != null) {
				if (mode.equals("list")) {
					redirectView = new RedirectView("teacher_list_home.html");
					redirectView.setExposeModelAttributes(false);
					return redirectView;
				} else {
					redirectView = new RedirectView("teacher_grid_home.html");
					redirectView.setExposeModelAttributes(false);
					return redirectView;
				}
			} else {

				redirectView = new RedirectView("teacher_grid_home.html");
				redirectView.setExposeModelAttributes(false);
				return redirectView;
			}
		} else {
			int id = Integer.parseInt(idStr);
			Course course = courseService.getCourse(id);
			Integer idMember = courseService.getIdMemberByIdUserAndIdCourse(idUser, id);
			model.addAttribute("idmember", idMember);
			model.addAttribute("idcourse", id);
			model.addAttribute("course", course);
			request.getSession().removeAttribute("iduser");
			return new ModelAndView("teacher_course").getView();
		}
	}
}
