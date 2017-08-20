package mock02.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import mock02.model.User;
import mock02.service.ManageCourseService;
import mock02.service.ManageStudentService;

/*
* TramTran(^^)
*/
/*test password OLD*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:WebContent/WEB-INF/context-servlet.xml" })
public class UTCMSUpID05 {
    @Autowired
    ManageStudentService manageStudentService;
    @Autowired
    ManageCourseService manageCourseService;
    @Before
    public void setUp() throws Exception {
    }
    @Test
    public void testupdateUserInput1() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("volan@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        u.setPassword(null);
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput2() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("volan@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        u.setPassword("");
        assertEquals(null,manageStudentService.updateUser(u, ""));
    }
    @Test
    public void testupdateUserInput3() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("volan@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        u.setPassword("123");
        assertEquals(null,manageStudentService.updateUser(u, "12345"));
    }
    @Test
    public void testupdateUserInput4() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("volan@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        u.setPassword("123");
        assertEquals(null,manageStudentService.updateUser(u, "trang@gmail.com"));
    }
}
