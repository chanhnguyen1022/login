package mock02.model;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/*
* TramTran(^^)
*/
@Entity
@Table(name = "user",
uniqueConstraints = { @UniqueConstraint(columnNames = { "iduser" }) })
public class User {
    private int userID;
    private String userName;
    private String email;
    private String password;
    private String  fullName;
    private String birthDay;
    private String role;
    private int status;
    private Set<Member> members = new HashSet<Member>(0);
    
    
    public User(){}
    public User(int userID, String userName, String email, String password, String fullName, String birthDay, String role,
            int status, Set<Member> members) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.role = role;
        this.status = status;
        this.members = members;
    }
    @Id
    @Column(name = "iduser")
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    @Column(name = "username", nullable = false)
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "fullname", nullable = false)
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @Column(name = "birthday", nullable = true)
    public String getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    @Column(name = "role", nullable = false)
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
   
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @OneToMany( mappedBy = "user")
    public Set<Member> getMembers() {
        return members;
    }
    public void setMembers(Set<Member> members) {
        this.members = members;
    }
    
    
}
