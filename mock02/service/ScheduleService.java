package mock02.service;

import java.sql.Date;
import java.util.List;

import mock02.dao.ScheduleDAO;
import mock02.model.Course;
import mock02.model.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 9, 2015
 */
@Service("scheduleService")
public class ScheduleService {

	@Autowired
	ScheduleDAO scheduleDAO;

	// Save or update a schedule 
	public void saveOrUpdateSchedule(Schedule schedule, Course course) {
		Schedule temp = scheduleDAO.findScheduleByDateAndIdCourse(course, schedule.getDate());
		if(temp != null){
			schedule.setIdSchedule(temp.getIdSchedule());
		}
		scheduleDAO.saveOrUpdateSchedule(schedule);
	}

	// Get list schedule
	public List<Schedule> getListSchedule(Course course) {
		return scheduleDAO.getListSchedule(course);
	}

	// delete schedule by id course and date
	public void deleteSchedule(Course course, Date date) {
		scheduleDAO.deleteSchedule(course, date);
	}
	
	//get schedule by id course and date
	public Schedule findScheduleByIdCourseAndDate(Course course, Date date){
		return scheduleDAO.findScheduleByDateAndIdCourse(course, date);
	}
}
