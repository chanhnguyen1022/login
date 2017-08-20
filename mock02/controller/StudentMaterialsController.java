package mock02.controller;

import java.util.List;

import mock02.model.Course;
import mock02.model.Resource;
import mock02.model.Subject;
import mock02.service.CourseService;
import mock02.service.TeacherMaterialsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 24, 2015
 **/
@Controller
@SessionAttributes({ "idcourse" })
public class StudentMaterialsController {
    @Autowired
    TeacherMaterialsService teacherMaterialsService;
    @Autowired
    CourseService courseService;

    @RequestMapping(value = "student_materials")
    String accessTeacherMaterials(ModelMap model,
	    @ModelAttribute("idcourse") int idCourse) {
	List<Subject> listSubject = teacherMaterialsService
		.getListSubjectByIdCourse(idCourse);
	model.addAttribute("listsubject", listSubject);
	model.addAttribute("active", 3);
	Course course = courseService.getCourse(idCourse);
	model.addAttribute("course", course);
	return "student_materials";
    }

    @RequestMapping(value = "student_resources")
    String accessTeacherResources(
	    @RequestParam(value = "idlecture", required = false) String idLectureStr,
	    @ModelAttribute("idcourse") int idCourse, ModelMap model) {
	try {
	    Integer idLecture = Integer.parseInt(idLectureStr);
	    String lectureName = teacherMaterialsService
		    .getLectureNameByIdLecture(idLecture);
	    if (idLecture == null || lectureName == null)
		return "redirect:student_materials.html";
	    List<Resource> listResource = teacherMaterialsService
		    .getListResourceNameByIdLecture(idLecture);
	    model.addAttribute("lectureName", lectureName);
	    model.addAttribute("listResource", listResource);
	    model.addAttribute("idlecture", idLecture);
	    model.addAttribute("active", 3);
	    return "student_resources";
	} catch (NumberFormatException e) {
	    return "redirect:student_materials.html";
	}
    }
}
