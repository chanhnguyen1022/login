package mock02.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import mock02.dto.StudentAnswerTestDTO;
import mock02.dto.StudentAssignmentDTO;
import mock02.model.Assignment;
import mock02.model.Member;
import mock02.service.StudentAssignmentService;

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

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 25, 2015
 **/
@Controller
@SessionAttributes({ "idcourse", "idmember" })
public class StudentAssignmentController {
	@Autowired
	StudentAssignmentService assignmentService;

	Log logger = LogFactory.getLog(StudentAssignmentController.class);

	@RequestMapping(value = "student_assignments")
	String getAssignmentListbyCourse(ModelMap model, @ModelAttribute("idcourse") int idCourse,
			@ModelAttribute("idmember") int idMember) throws ParseException {
		List<StudentAssignmentDTO> listAssignmentDTO = assignmentService
				.getListOpenAssignmentByCourseAndIdMember(idCourse, idMember);
		model.addAttribute("listAssignmentDTO", listAssignmentDTO);
		model.addAttribute("active", 4);
		return "student_assignment";
	}

	@RequestMapping(value = "student_answer")
	String accessStudentAnswerAssignment(ModelMap model,
			@RequestParam(value = "id", required = false) String idAssignmentStr,
			@ModelAttribute("idmember") int idMember) {
		try {
			Integer idAssignment = Integer.parseInt(idAssignmentStr);
			String type = assignmentService.getOpenAssignmentTypeById(idAssignment);
			model.addAttribute("active", 4);
			if (type == null)
				return "redirect:student_assignments.html";
			if (type.equals("test")) {
				StudentAnswerTestDTO answerTestDTO = assignmentService.getstudentAnswerTestDTO(idAssignment,
						idMember);
				model.addAttribute("answerTestDTO", answerTestDTO);
				return "student_answer_test";
			} else {
				return "student_answer_quiz";
			}
		} catch (NumberFormatException e) {
			return "redirect:student_assignments.html";
		}
	}

	@RequestMapping(value = "student_saveorupdateanswer")
	@ResponseBody
	String saveOrUpdateAnswerTest(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "attachFileName", required = false) String fileName,
			@RequestParam(value = "idAssignment", required = false) String idAssignmentStr,
			@RequestParam(value = "answer", required = false) String answer,
			@RequestParam(value = "count", required = false) String countStr,
			@ModelAttribute("idmember") Integer idMember) throws NumberFormatException, SerialException,
			SQLException, IOException, NotFoundException {
		Integer idAssignment = Integer.parseInt(idAssignmentStr);
		Integer count = Integer.parseInt(countStr);
		Integer idScore = assignmentService.getIdScoreByIdAssignmentAndIdMember(idAssignment, idMember);
		// Check fake idAssignment
		String type = assignmentService.getOpenAssignmentTypeById(idAssignment);
		if (type == null || !type.equals("test"))
			throw new NotFoundException("Assignment not found.");
		Assignment assignment = new Assignment();
		assignment.setIdAssignment(idAssignment);
		Member member = new Member();
		member.setIdMember(idMember);
		if (idScore == null) {
			if (file != null) {
				Blob blob = new SerialBlob(file.getBytes());
				assignmentService.saveAnswerTestWithFile(assignment, member, answer, blob, fileName);
			} else {
				assignmentService.saveAnswerTestWithoutFile(assignment, member, answer);
			}
		} else {
			Integer redoTime = assignmentService.getRedoTimeById(idAssignment);
			if (file != null) {
				Blob blob = new SerialBlob(file.getBytes());
				assignmentService.updateAnswerTestChangeFile(answer, blob, fileName, idScore, redoTime);
			} else {
				if (count == 1)
					assignmentService.updateAnswerTestNotChangeFile(answer, idScore, redoTime);
				else if (count == 0)
					assignmentService.updateAnswerTestChangeFile(answer, null, null, idScore, redoTime);
				else
					throw new NotFoundException("flag for control file not found");
			}
		}
		return "done";
	}

	@RequestMapping(value = "downloadfileanswer.html", method = RequestMethod.POST)
	@ResponseBody
	String downloadFile(@RequestParam("id") String idAssignmentStr,
			@ModelAttribute("idmember") Integer idMember, HttpServletRequest request)
			throws NumberFormatException, IOException, SQLException, JSONException, NotFoundException {
		Integer idAssignment = Integer.parseInt(idAssignmentStr);
		Integer idScore = assignmentService.getIdScoreByIdAssignmentAndIdMember(idAssignment, idMember);
		// Check idScore by idAssignment and idMember, prevent one member can
		// download all file in score table
		if (idScore == null)
			throw new NotFoundException("File not found.");
		String name = assignmentService.getAnswerFileNameById(idScore);
		Blob blob = assignmentService.getAnswerFileById(idScore);

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
}
