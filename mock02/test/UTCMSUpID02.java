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
/*test email*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:WebContent/WEB-INF/context-servlet.xml" })
public class UTCMSUpID02 {
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
        u.setEmail(null);
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput2() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("lanvo");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput3() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("lanvo%2.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput4() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("tram@ymail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput5() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("trang@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
    @Test
    public void testupdateUserInput6() {
        User u = new User();
        u.setUserID(24);
        u.setEmail("lanvo@gmail.com");
        u.setFullName("Võ Thị Lan");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.updateUser(u, null));
    }
}
