package mock02.service;

import java.util.ArrayList;
import java.util.List;

import mock02.dao.ManageUserDAO;
import mock02.dao.MemberDAO;
import mock02.dao.UserDAO;
import mock02.dto.AdminManageUser;
import mock02.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("manageUserService")
public class ManageUserService {
	
	@Autowired
	private ManageUserDAO manageUserDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private MemberDAO memberDAO;

	public String insertUser(User user) {
		return manageUserDAO.insertUser(user);
	}

	public String updateUser(User user) {
		return manageUserDAO.updateUser(user);
	}

	public boolean deleteUser(User userID) {
		return manageUserDAO.deleteUser(userID);
	}

	public int checkUser(String userName) {
		return manageUserDAO.checkUser(userName);
	}

	public User getUser(int userID) {
		return manageUserDAO.getUser(userID);
	}
	
	public List<AdminManageUser> getListUser() {
		List<User>listUser = userDAO.getListUser();
		List<AdminManageUser> laMU = new ArrayList<AdminManageUser>();
		int i = 0;
		AdminManageUser aMU;
		System.out.println(listUser.size());
		while(i < listUser.size()){
			aMU = new AdminManageUser(listUser.get(i).getUserID(), listUser.get(i).getUserName(), listUser.get(i).getRole(), listUser.get(i).getStatus());
			List<String> listCourseName =memberDAO.getCourseNameByIdUser(listUser.get(i).getUserID());
			aMU.setListCourseName(listCourseName);
			laMU.add(aMU);
			i++;
		}
		return laMU;
	}
}
