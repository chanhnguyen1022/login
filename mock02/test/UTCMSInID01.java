package mock02.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import mock02.model.Course;
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
public class UTCMSInID01 {
    @Autowired
    ManageStudentService manageStudentService;
    @Autowired
    ManageCourseService manageCourseService;
    @Before
    public void setUp() throws Exception {
    }
    @Test
    public void testinsertUserInput1() {
        Course course = manageCourseService.getCourse(20);
        User u = new User();
        u.setEmail(null);
        u.setFullName("Lê Văn A");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.insertUser(u, course));
    }
    @Test
    public void testinsertUserInput2() {
        Course course = manageCourseService.getCourse(20);
        User u = new User();
        u.setEmail("levan");
        u.setFullName("Lê Văn A");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.insertUser(u, course));
    }
    @Test
    public void testinsertUserInput3() {
        Course course = manageCourseService.getCourse(20);
        User u = new User();
        u.setEmail("levan@.com");
        u.setFullName("Lê Văn A");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.insertUser(u, course));
    }
    @Test
    public void testinsertUserInput4() {
        Course course = manageCourseService.getCourse(20);
        User u = new User();
        u.setEmail("levan@gmail.com");
        u.setFullName("Lê Văn A");
        u.setBirthDay("1994-10-10");
        assertEquals(null,manageStudentService.insertUser(u, course));
    }
    
}
