package mock02.service;

import mock02.dao.LoginDAO;
import mock02.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 14, 2015
 */

@Service("loginService")
public class LoginService {

	@Autowired
	LoginDAO loginDAO;

	// Get fullName from userName
	public String getFullName(String userName) {
		return loginDAO.getFullName(userName);
	}

	public User getUser(String userName) {
		return loginDAO.getUser(userName);
	}
}
