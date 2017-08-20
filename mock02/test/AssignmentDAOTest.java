package mock02.test;

import static org.junit.Assert.*;
import mock02.dao.AssignmentDAO;
import mock02.model.Assignment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 31, 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:WebContent/WEB-INF/context-servlet.xml" })
public class AssignmentDAOTest {

	@Autowired
	AssignmentDAO assignmentDAO;
	
	@Test
	public void testGetAssignmentNameAndDeadlineById() {
		Assignment assignment = assignmentDAO.getAssignmentNameAndDeadlineById(1);
		assertEquals("Bài 1 : Tìm hiểu và cài đặt Laravel 5.x", assignment.getAssignmentName());
	}

}
