package mock02.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import mock02.dao.ScoreDAO;
import mock02.model.Assignment;
import mock02.model.Score;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/*
 *@author: nguyenkhue
 *@version 1.0 Dec 30, 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:WebContent/WEB-INF/context-servlet.xml" })
public class ScoreDAOTest {

	@Autowired
	ScoreDAO scoreDAO;
	
	@Ignore
	public void testGetScorebyUserAndAssignment() {
		fail("Not yet implemented");
	}

	@Test
	@DirtiesContext
	public void testGetListAnswerByAssignment() throws ParseException {
		Assignment assignment = new Assignment();
		assignment.setIdAssignment(1);
		List<Score> actuals = scoreDAO.getListAnswerByAssignment(assignment);
		assertEquals("Lê Nguyên Khuê", actuals.get(0).getMember().getUser().getFullName());
	}
	
	@Test
	public void testGetAnswerById() throws ParseException{
		Score actuals = scoreDAO.getAnswerById(2);
		assertEquals("Trịnh Hồng Nhân", actuals.getMember().getUser().getFullName());
	}

}
