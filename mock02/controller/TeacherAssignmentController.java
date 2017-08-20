package mock02.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import mock02.model.Assignment;
import mock02.model.Course;
import mock02.model.Score;
import mock02.service.StudentAssignmentService;
import mock02.service.TeacherAssignmentService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 26, 2015
 */
@Controller
@SessionAttributes({ "idcourse" })
public class TeacherAssignmentController {

	@Autowired
	TeacherAssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	Log logger = LogFactory.getLog(TeacherAssignmentController.class);

	@RequestMapping("teacher_assignment")
	String accessTeacherAssignment(ModelMap model, @ModelAttribute("idcourse") Integer idCourse)
			throws ParseException {
		List<Assignment> listAssignment = assignmentService.getListAssignmentByCourse(idCourse);
		model.addAttribute("listAssignment", listAssignment);
		model.addAttribute("active", 5);
		return "teacher_assignment";
	}

	@RequestMapping(value = "teacher_addassignment.html", method = RequestMethod.POST)
	@ResponseBody
	String addAssignment(@RequestParam(value = "file", required = false) MultipartFile file,
			@ModelAttribute Assignment assignment, @ModelAttribute("idcourse") Integer idCourse,
			@RequestParam(value = "openTime", required = false) String openTimeStr,
			@RequestParam("deadline") String deadlineStr) throws ParseException, SerialException,
			SQLException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
		if (openTimeStr != null) {
			Date openTime = sdf.parse(openTimeStr);
			assignment.setOpenTime(openTime);
		}
		Date deadline = sdf.parse(deadlineStr);
		assignment.setDeadline(deadline);
		if (file != null) {
			logger.info("file is " + file.toString());
			logger.info("file size " + file.getSize());
			logger.info("file name " + assignment.getAttachFileName());
			Blob blob = new SerialBlob(file.getBytes());
			assignment.setAttachFile(blob);
		}
		Course course = new Course();
		course.setIdCourse(idCourse);
		assignment.setCourse(course);
		assignment.setType("test");
		assignmentService.saveOrUpdateAssignment(assignment);
		return "done";
	}

	@RequestMapping(value = "teacher_updateassignment.html", method = RequestMethod.POST)
	@ResponseBody
	String updateAssignment(@RequestParam(value = "file", required = false) MultipartFile file,
			@ModelAttribute Assignment assignment, @ModelAttribute("idcourse") Integer idCourse,
			@RequestParam(value = "count", required = false) String countStr,
			@RequestParam(value = "openTime", required = false) String openTimeStr,
			@RequestParam("deadline") String deadlineStr) throws NumberFormatException, SerialException,
			SQLException, IOException, NotFoundException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm");
		Integer count = Integer.parseInt(countStr);
		Course course = new Course();
		course.setIdCourse(idCourse);
		assignment.setCourse(course);
		assignment.setType("test");
		if (openTimeStr != null) {
			Date openTime = sdf.parse(openTimeStr);
			assignment.setOpenTime(openTime);
		}
		Date deadline = sdf.parse(deadlineStr);
		assignment.setDeadline(deadline);
		if (countStr == null) {
			throw new NotFoundException("Count not found!");
		}
		if (file != null) {
			logger.info("file is " + file.toString());
			logger.info("file size " + file.getSize());
			logger.info("file name " + assignment.getAttachFileName());
			Blob blob = new SerialBlob(file.getBytes());
			assignment.setAttachFile(blob);
			assignmentService.saveOrUpdateAssignment(assignment);
		} else if (count == 0) {
			assignmentService.saveOrUpdateAssignment(assignment);
		} else if (count == 1)
			assignmentService.updateNotChangeFileAssignment(assignment);
		return "done";
	}

	@RequestMapping(value = "teacher_deleteassignment.html", method = RequestMethod.POST)
	@ResponseBody
	String deleteAssignment(@RequestParam("id") Integer idAssignment) {
		assignmentService.deleteAssignment(idAssignment);
		return "done";
	}

	@RequestMapping(value = "download_assignment_attachfile", method = RequestMethod.POST)
	@ResponseBody
	String downloadFile(@RequestParam("id") String idAssignmentStr, HttpServletRequest request)
			throws NumberFormatException, IOException, SQLException, JSONException {
		Integer idAssignment = Integer.parseInt(idAssignmentStr);
		String name = assignmentService.getFileNameById(idAssignment);
		Blob blob = assignmentService.getFileById(idAssignment);

		// Hai dòng code bên dưới(1 commened, 1 code line) để lấy đường dẫn của
		// file(same method)
		// String contextPath = request.getServletContext().getRealPath("/");
		String path = this.getClass().getClassLoader().getResource("").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		String pathPrject = path.substring(1, path.indexOf("WEB-INF/classes/"));
		String urlToDownload = pathPrject + name;
		// Ghi file tu database len server de download ve
		File newFile = new File(urlToDownload);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		OutputStream out = new FileOutputStream(newFile);
		out.write(blob.getBytes(1, (int) blob.length()));
		out.flush();
		out.close();
		JSONObject obj = new JSONObject();
		obj.put("dir", name);
		// async cho 10 giay sau se xoa file(xoa file tren server sau khi
		// download)
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000);
					newFile.delete();
					logger.info("deleted file");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return obj.toString();
	}

	@RequestMapping("teacher_list_answer")
	String viewListAnswer(@RequestParam(value = "id", required = false) String idAssignmentStr, ModelMap model)
			throws ParseException {
		try {
			Integer idAssignment = Integer.parseInt(idAssignmentStr);
			List<Score> listScore = assignmentService.getListAnswerByIdAssignment(idAssignment);
			Assignment assignment = assignmentService.getAssignmentNameAndDeadlineById(idAssignment);
			model.addAttribute("assignment", assignment);
			model.addAttribute("listScore", listScore);
			model.addAttribute("active", 5);
			return "teacher_list_answer";
		} catch (NumberFormatException e) {
			return "redirect:teacher_assignment.html";
		}
	}

	@RequestMapping("teacher_mark_answer")
	String viewAnswer(@RequestParam(value = "id", required = false) String idScoreStr, ModelMap model) {
		try {
			Integer idScore = Integer.parseInt(idScoreStr);
			Score score;
			score = assignmentService.getAnswerById(idScore);
			model.addAttribute("score", score);
			model.addAttribute("active", 5);
			return "teacher_mark_answer";
		} catch (NumberFormatException | ParseException e) {
			return "redirect:teacher_assignment.html";
		}
	}

	@RequestMapping("teacher_save_mark")
	String markAnswer(@RequestParam(value = "idScore", required = false) String idScoreStr,
			@RequestParam(value = "idAssignment", required = false) String idAssignmentStr,
			@RequestParam(value = "score", required = false) String pointStr,
			@RequestParam(value = "comment", required = false) String comment, ModelMap model) {
		try {
			Integer idScore = Integer.parseInt(idScoreStr);
			Integer point = Integer.parseInt(pointStr);
			Score score = new Score();
			score.setScore(point);
			score.setComment(comment);
			score.setIdScore(idScore);
			assignmentService.markAnswer(score);
			return "redirect:teacher_list_answer.html?id=" + idAssignmentStr + "";
		} catch (NumberFormatException e) {
			return "redirect:teacher_list_answer.html?id=" + idAssignmentStr + "";
		}
	}

	@RequestMapping(value = "teacher_downloadfileanswer.html", method = RequestMethod.POST)
	@ResponseBody
	String downloadFileAnswer(@RequestParam("id") String idScoreStr, HttpServletRequest request)
			throws NumberFormatException, IOException, SQLException, JSONException, NotFoundException {
		Integer idScore = Integer.parseInt(idScoreStr);
		String name = studentAssignmentService.getAnswerFileNameById(idScore);
		Blob blob = studentAssignmentService.getAnswerFileById(idScore);
		String path = this.getClass().getClassLoader().getResource("").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		String pathPrject = path.substring(1, path.indexOf("WEB-INF/classes/"));
		String urlToDownload = pathPrject + name;
		File newFile = new File(urlToDownload);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		OutputStream out = new FileOutputStream(newFile);
		out.write(blob.getBytes(1, (int) blob.length()));
		out.flush();
		out.close();
		JSONObject obj = new JSONObject();
		obj.put("dir", name);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000);
					newFile.delete();
					logger.info("deleted file");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return obj.toString();
	}
}
