package mock02.dto;

import java.util.List;

public class AdminManageUser {

	private Integer idUser;
	private String userName;
	private String role;
	private Integer status;
	private List<String> listCourseName;

	public AdminManageUser(){
		
	}
	
	public AdminManageUser(Integer idUser, String userName, String role,
			Integer status) {
		this.idUser = idUser;
		this.userName = userName;
		this.role = role;
		this.status = status;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getListCourseName() {
		return listCourseName;
	}

	public void setListCourseName(List<String> listCourseName) {
		this.listCourseName = listCourseName;
	}
}
