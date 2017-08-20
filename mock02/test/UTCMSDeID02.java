package mock02.test;

import static org.junit.Assert.assertEquals;

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
public class UTCMSDeID02 {
    @Autowired
    ManageStudentService manageStudentService;
    @Autowired
    ManageCourseService manageCourseService;
    @Before
    public void setUp() throws Exception {
    }
    @Test
    public void testdeleteUserInput1() {
        Course course = null;
        User u = manageStudentService.getUser(24);
        assertEquals(true,manageStudentService.deleteUserofCourse(u, course));
    }
    @Test
    public void testdeleteUserInput2() {
        Course course = manageCourseService.getCourse(24);
        User u = manageStudentService.getUser(24);
        assertEquals(true,manageStudentService.deleteUserofCourse(u, course));
    }
    @Test
    public void testdeleteUserInput3() {
        Course course = manageCourseService.getCourse(22);
        User u = manageStudentService.getUser(25);
        assertEquals(true,manageStudentService.deleteUserofCourse(u, course));
    }
}
