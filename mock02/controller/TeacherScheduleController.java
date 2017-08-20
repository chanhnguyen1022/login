package mock02.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Date;

import mock02.model.Course;
import mock02.model.Schedule;
import mock02.service.ScheduleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 9, 2015
 */
@Controller
@SessionAttributes({ "idcourse", "fullname" })
public class TeacherScheduleController {

	@Autowired
	ScheduleService scheduleService;

	// Save a schedule
	@RequestMapping(value = "saveschedule", method = RequestMethod.POST)
	@ResponseBody
	String saveSchedule(@RequestParam(value = "date") String dateStr,
			@RequestParam(value = "title") String title, @RequestParam(value = "body") String body,
			@RequestParam(value = "badge") String badge, @ModelAttribute("fullname") String fullName,
			@ModelAttribute("idcourse") int idCourse) throws ParseException {
		boolean important = false;
		String footer = "From " + fullName;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date((sdf.parse(dateStr).getTime()));
		if ("1".compareTo(badge) == 0) {
			important = true;
		}
		Course course = new Course();
		course.setIdCourse(idCourse);
		Schedule schedule = new Schedule(course, title, body, footer, date, important);
		scheduleService.saveOrUpdateSchedule(schedule, course);
		return "done";
	}

	// Get list schedule for a course by id course
	@RequestMapping(value = ("schedule"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String getListSchedule(@ModelAttribute("idcourse") int idCourse) throws JSONException {
		Course course = new Course();
		course.setIdCourse(idCourse);
		List<Schedule> listSchedule = scheduleService.getListSchedule(course);
		JSONArray array = new JSONArray();
		JSONObject obj;
		int i = 0;
		while (i < listSchedule.size()) {
			obj = new JSONObject();
			obj.put("body", listSchedule.get(i).getContent());
			obj.put("badge", listSchedule.get(i).getImportant());
			obj.put("date", listSchedule.get(i).getDate());
			obj.put("title", listSchedule.get(i).getTitle());
			obj.put("footer", listSchedule.get(i).getFooter());
			array.put(obj);
			i++;
		}
		return array.toString();
	}

	// Delete schedule by id course and date
	@RequestMapping(value = ("deleteschedule"), method = RequestMethod.GET)
	@ResponseBody
	String deleteSchedule(@ModelAttribute("idcourse") int idCourse, @RequestParam("date") String dateStr)
			throws ParseException {
		Course course = new Course();
		course.setIdCourse(idCourse);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date((sdf.parse(dateStr).getTime()));
		scheduleService.deleteSchedule(course, date);
		return "done";
	}

	// Get schedule for a course by id course and date
	@RequestMapping(value = ("getschedule"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String getSchedule(@ModelAttribute("idcourse") int idCourse, @RequestParam("date") String dateStr)
			throws JSONException, ParseException {
		Course course = new Course();
		course.setIdCourse(idCourse);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date((sdf.parse(dateStr).getTime()));
		Schedule schedule = scheduleService.findScheduleByIdCourseAndDate(course, date);
		JSONArray array = new JSONArray();
		JSONObject obj;
		obj = new JSONObject();
		if (schedule != null) {
			obj.put("body", schedule.getContent());
			obj.put("badge", schedule.getImportant());
			obj.put("title", schedule.getTitle());
			obj.put("footer", schedule.getFooter());
		} else {
			obj.put("body", "");
			obj.put("badge", false);
			obj.put("title", "");
			obj.put("footer", "");
		}
		array.put(obj);
		return array.toString();
	}
}
