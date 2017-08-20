package mock02.service;

import mock02.dao.UserDAO;
import mock02.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {
	@Autowired
	private UserDAO userDao;
	//lay user dua vao password, username
		public User getUse_ByUserNamePasword(User user) {
			return userDao.getUse_ByUserNamePasword(user);
		}
}
