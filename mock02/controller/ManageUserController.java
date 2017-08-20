package mock02.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mock02.dto.AdminManageUser;
import mock02.model.User;
import mock02.service.ManageUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ManageUserController {
	@SuppressWarnings("unused")
	private HttpSession session;
	@Autowired
	ManageUserService manageUserService;

	
	@RequestMapping(value = "admin_add_user", method = RequestMethod.GET)
	public String accessAddUserPage(ModelMap model) {
		model.addAttribute("active", 2);
		return "admin_add_user";
	}
	
	@RequestMapping(value = "admin_confirm_add_user", method = RequestMethod.POST)
	public String insertStudent(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, @ModelAttribute User user) throws Exception {
		session = request.getSession();
		user.setPassword(user.getEmail());
		user.setUserName(user.getEmail());
		String result = manageUserService.insertUser(user);
		if (result == null)
			return "redirect:admin_manageuser.html";
		else {
			throw new Exception(result);
		}
	}
	
	@RequestMapping(value = "admin_manageuser", method = RequestMethod.GET)
	public String accessManageUserPage(ModelMap model) {
		model.addAttribute("active", 2);
		List<AdminManageUser> listUser = manageUserService.getListUser();
		model.addAttribute("listUser", listUser);
		return "admin_manageuser";
	}

	@RequestMapping(value = "admin_edit_user", method = RequestMethod.POST)
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
		String role = request.getParameter("role");
		// create user
		User user = new User();
		user.setUserID(id);
		user.setEmail(email);
		user.setFullName(fullName);
		user.setBirthDay(birthDate);
		user.setRole(role);
		String result = manageUserService.updateUser(user);
		if (result == null) {
			return "1";
		} else {
			return result;
		}

	}

	@RequestMapping(value = "admin_delete_user", method = RequestMethod.POST)
	public @ResponseBody String deleteUser(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		session = request.getSession();
		String userid = request.getParameter("userID");

		int id = -1;
		try {
			id = Integer.parseInt(userid);

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		User u = manageUserService.getUser(id);

		if (u == null) {
			return "0";
		} else {
			boolean result = manageUserService.deleteUser(u);
			if (result) {
				return "1";
			} else {
				return "0";
			}
		}
	}

}
