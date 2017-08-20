package mock02.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mock02.model.Resource;
import mock02.model.Subject;
import mock02.service.TeacherCourseService;
import mock02.service.TeacherMaterialsService;

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
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 12, 2015
 */
@Controller
@SessionAttributes({ "idcourse" })
public class TeacherMaterialsController {

	@Autowired
	TeacherMaterialsService teacherMaterialsService;
	@Autowired
	TeacherCourseService teacherCourseService;

	Log logger = LogFactory.getLog(TeacherMaterialsController.class);

	@RequestMapping(value = "teacher_materials")
	String accessTeacherMaterials(ModelMap model, @ModelAttribute("idcourse") Integer idCourse) {
		if (idCourse == null)
			return "redirect:teacher_grid_home.html";
		List<Subject> listSubject = teacherMaterialsService.getListSubjectByIdCourse(idCourse);
		model.addAttribute("listsubject", listSubject);
		model.addAttribute("active", 4);
		return "teacher_materials";
	}

	@RequestMapping(value = "teacher_resources")
	String accessTeacherResources(@RequestParam(value = "idlecture", required = false) String idLectureStr,
			@ModelAttribute("idcourse") Integer idCourse, ModelMap model) {
		if (idCourse == null)
			return "redirect:teacher_grid_home.html";
		try {
			Integer idLecture = Integer.parseInt(idLectureStr);
			String lectureName = teacherMaterialsService.getLectureNameByIdLecture(idLecture);
			if (idLecture == null || lectureName == null)
				return "redirect:teacher_materials.html";
			List<Resource> listResource = teacherMaterialsService.getListResourceNameByIdLecture(idLecture);
			model.addAttribute("lectureName", lectureName);
			model.addAttribute("listResource", listResource);
			model.addAttribute("idlecture", idLecture);
			model.addAttribute("active", 4);
			return "teacher_resources";
		} catch (NumberFormatException e) {
			return "redirect:teacher_materials.html";
		}
	}

	@RequestMapping(value = ("addresourcevideo"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String addResourceVideo(@RequestParam(value = "idlecture", required = false) String idLectureStr,
			@RequestParam(value = "value", required = false) String value) throws JSONException,
			NumberFormatException {
		Integer idLecture = Integer.parseInt(idLectureStr);
		JSONObject obj = new JSONObject();
		Integer id = teacherMaterialsService.addResourceVideo(idLecture, value);
		obj.put("id", id);
		return obj.toString();
	}

	@RequestMapping(value = ("deleteresource"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String deleteResourceVideo(@RequestParam(value = "id", required = false) String idStr)
			throws NumberFormatException {
		Integer id = Integer.parseInt(idStr);
		teacherMaterialsService.deleteResource(id);
		return "done";
	}

	@RequestMapping(value = ("editresourcevideo"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String editResourceVideo(@RequestParam(value = "id", required = false) String idStr,
			@RequestParam(value = "value", required = false) String value) throws NumberFormatException {
		Integer id = Integer.parseInt(idStr);
		teacherMaterialsService.editResourceVideo(id, value);
		return "done";
	}

	@RequestMapping(value = "addresourcedoc.html", method = RequestMethod.POST)
	@ResponseBody
	String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName,
			@RequestParam("idLecture") String idLectureStr) throws Exception {
		logger.info("file is " + file.toString());
		logger.info("file size " + file.getSize());
		logger.info("file name " + fileName);
		Integer idLecture = Integer.parseInt(idLectureStr);
		byte[] value = file.getBytes();
		teacherMaterialsService.addResourceDoc(fileName, value, idLecture);
		Integer id = teacherMaterialsService.getLastIdResource();
		JSONObject obj = new JSONObject();
		obj.put("name", fileName);
		obj.put("id", id);
		return obj.toString();
	}

	@RequestMapping(value = "downloadresourcedoc.html", method = RequestMethod.POST)
	@ResponseBody
	String downloadFile(@RequestParam("id") String idResourceStr, HttpServletRequest request)
			throws NumberFormatException, IOException, SQLException, JSONException {
		Integer idResource = Integer.parseInt(idResourceStr);
		String name = teacherMaterialsService.getResourceNameById(idResource);
		Blob blob = teacherMaterialsService.getResourceDocById(idResource);

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

	@RequestMapping("createsubject")
	String createSubject(@RequestParam(value = "name", required = false) String name,
			@ModelAttribute("idcourse") Integer idCourse) {
		if (idCourse == null)
			return "redirect:teacher_grid_home.html";
		if (name == null || name.trim().length() == 0)
			return "redirect:teacher_materials.html";
		teacherMaterialsService.createSubject(name, idCourse);
		return "redirect:teacher_materials.html";
	}

	@RequestMapping("deletesubject")
	View deleteSubject(@RequestParam(value = "id", required = false) String idStr) {
		RedirectView redirectView;
		redirectView = new RedirectView("teacher_materials.html");
		redirectView.setExposeModelAttributes(false);
		try {
			Integer id = Integer.parseInt(idStr);
			teacherMaterialsService.deleteSubject(id);
			return redirectView;
		} catch (NumberFormatException e) {
			return redirectView;
		}
	}

	@RequestMapping("editsubject")
	View editSubject(@RequestParam(value = "id", required = false) String idStr,
			@RequestParam(value = "name", required = false) String name) {
		RedirectView redirectView;
		redirectView = new RedirectView("teacher_materials.html");
		redirectView.setExposeModelAttributes(false);
		try {
			Integer id = Integer.parseInt(idStr);
			if (name == null || name.trim().length() == 0)
				return redirectView;
			teacherMaterialsService.editSubject(id, name);
			return redirectView;
		} catch (NumberFormatException e) {
			return redirectView;
		}
	}

	@RequestMapping(value = ("addlecture"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String addLecture(@RequestParam(value = "id", required = false) String idSubjectStr,
			@RequestParam(value = "name", required = false) String name) throws JSONException,
			NumberFormatException {
		Integer idSubject = Integer.parseInt(idSubjectStr);
		JSONObject obj = new JSONObject();
		Integer id = teacherMaterialsService.addLecture(idSubject, name);
		obj.put("id", id);
		return obj.toString();
	}

	@RequestMapping(value = ("deletelecture"), method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	String deleteLecture(@RequestParam(value = "id", required = false) String idLectureStr)
			throws NumberFormatException {
		Integer idLecture = Integer.parseInt(idLectureStr);
		teacherMaterialsService.deleteLecture(idLecture);
		return "done";
	}
}
