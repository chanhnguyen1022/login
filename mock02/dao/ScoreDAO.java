package mock02.dao;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mock02.model.Assignment;
import mock02.model.Member;
import mock02.model.Score;
import mock02.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 26, 2015
 **/
@Repository("scoreDAO")
@Transactional
public class ScoreDAO {
	@Autowired
	SessionFactory sessionFactory;

	// get score from by user and assignemnt
	@SuppressWarnings("unchecked")
	public List<Integer> getPointbyMemberAndAssignment(Assignment assignment, Member member) {
		Session session = sessionFactory.getCurrentSession();
		List<Integer> result = (List<Integer>) session.createCriteria(Score.class)
				.add(Restrictions.eq("member", member)).add(Restrictions.eq("assignment", assignment))
				.setProjection(Projections.property("score")).list();
		return result;
	}

	public Score getScorebyMemberAndAssignment(Assignment assignment, Member member) {
		Session session = sessionFactory.getCurrentSession();
		Score result = (Score) session
				.createCriteria(Score.class)
				.add(Restrictions.eq("member", member))
				.add(Restrictions.eq("assignment", assignment))
				.setProjection(
						Projections.projectionList().add(Projections.property("score").as("score"))
								.add(Projections.property("answer").as("answer"))
								.add(Projections.property("attachFileName").as("attachFileName"))
								.add(Projections.property("comment").as("comment"))
								.add(Projections.property("timeStore").as("timeStore"))
								.add(Projections.property("countUpdate").as("countUpdate")))
				.setResultTransformer(Transformers.aliasToBean(Score.class)).uniqueResult();
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Score> getListAnswerByAssignment(Assignment assignment) throws ParseException {
		Session session = sessionFactory.getCurrentSession();
		List<Object[]> result = (List<Object[]>) session
				.createCriteria(Score.class)
				.add(Restrictions.eq("assignment", assignment))
				.createAlias("member", "mem")
				.createAlias("member.user", "u")
				.setProjection(
						Projections.distinct(Projections.projectionList()
								.add(Projections.property("idScore")).add(Projections.property("score"))
								.add(Projections.property("timeStore"))
								.add(Projections.property("u.fullName")))).list();
		List<Score> listScore = new ArrayList<Score>();
		Score score;
		Assignment assign;
		Member member;
		User user;
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-M-dd HH:mm:ss");
		for (Object[] obj : result) {
			score = new Score();
			assign = new Assignment();
			member = new Member();
			user = new User();
			score.setIdScore(Integer.parseInt(String.valueOf(obj[0])));
			if ("null".compareTo(String.valueOf(obj[1])) != 0)
				score.setScore(Integer.parseInt(String.valueOf(obj[1])));
			score.setTimeStore(sdf.parse(String.valueOf(obj[2])));
			user.setFullName(String.valueOf(obj[3]));
			member.setUser(user);
			score.setMember(member);
			score.setAssignment(assign);
			listScore.add(score);
		}
		return listScore;
	}

	public Score getAnswerById(Integer idScore) throws ParseException {
		Session session = sessionFactory.getCurrentSession();
		Object[] result = (Object[]) session
				.createCriteria(Score.class)
				.add(Restrictions.eq("idScore", idScore))
				.createAlias("member", "mem")
				.createAlias("member.user", "u")
				.createAlias("assignment", "a")
				.setProjection(
						Projections.distinct(Projections.projectionList()
								.add(Projections.property("idScore")).add(Projections.property("score"))
								.add(Projections.property("timeStore"))
								.add(Projections.property("u.fullName")).add(Projections.property("comment"))
								.add(Projections.property("attachFileName"))
								.add(Projections.property("a.idAssignment"))
								.add(Projections.property("a.deadline")).add(Projections.property("answer"))
								.add(Projections.property("a.assignmentName")))).uniqueResult();
		Score score = new Score();
		Member member = new Member();
		User user = new User();
		Assignment assignment = new Assignment();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
		member.setUser(user);
		score.setMember(member);
		score.setAssignment(assignment);
		score.setIdScore(Integer.parseInt(String.valueOf(result[0])));
		if ("null".compareTo(String.valueOf(result[1])) != 0)
			score.setScore(Integer.parseInt(String.valueOf(result[1])));
		score.setTimeStore(sdf.parse(String.valueOf(result[2])));
		user.setFullName(String.valueOf(result[3]));
		if ("null".compareTo(String.valueOf(result[4])) != 0)
			score.setComment(String.valueOf(result[4]));
		if ("null".compareTo(String.valueOf(result[5])) != 0)
			score.setAttachFileName(String.valueOf(result[5]));
		if ("null".compareTo(String.valueOf(result[6])) != 0)
			assignment.setIdAssignment(Integer.parseInt(String.valueOf(result[6])));
		assignment.setDeadline(sdf.parse(String.valueOf(result[7])));
		score.setAnswer(String.valueOf(result[8]));
		assignment.setAssignmentName(String.valueOf(result[9]));
		return score;
	}

	public void markAnswer(Score score) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery("update Score set score = :score, comment = :comment where idScore = :id")
				.setInteger("score", score.getScore()).setString("comment", score.getComment())
				.setInteger("id", score.getIdScore()).executeUpdate();
	}

	public boolean hasDone(Assignment assignment, Member member) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session.createCriteria(Score.class)
				.add(Restrictions.eq("assignment", assignment))
				.add(Restrictions.eqOrIsNull("member", member))
				.setProjection(Projections.property("idScore")).setMaxResults(1).uniqueResult();
		if (result != null)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public Integer getCountUpdate(Assignment assignment, Member member) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = null;
		if (assignment.getType().equals("test")) {
			result = (Integer) session.createCriteria(Score.class)
					.add(Restrictions.eq("assignment", assignment))
					.add(Restrictions.eqOrIsNull("member", member))
					.setProjection(Projections.property("countUpdate")).setMaxResults(1).uniqueResult();
		} else if (assignment.getType().equals("quiz")) {
			List<Integer> listScore = (List<Integer>) session.createCriteria(Score.class)
					.add(Restrictions.eq("assignment", assignment))
					.add(Restrictions.eqOrIsNull("member", member))
					.setProjection(Projections.property("idScore")).list();
			result = listScore.size();
		}
		return result;
	}

	public Integer getIdScoreByIdAssignmentAndIdMember(Integer idAssignment, Integer idMember) {
		Session session = sessionFactory.getCurrentSession();
		Integer result = (Integer) session
				.createQuery(
						"select idScore from Score where idAssignment =:idAssignment and idMember =:idMember")
				.setInteger("idMember", idMember).setInteger("idAssignment", idAssignment).setMaxResults(1)
				.uniqueResult();
		return result;
	}

	public void saveAnswerTestWithFile(Assignment assignment, Member member, String answer, Blob file,
			String fileName) {
		Session session = sessionFactory.getCurrentSession();
		Date now = new Date();
		Score score = new Score(assignment, member, now, answer, fileName, file);
		score.setCountUpdate(1);
		session.save(score);
		session.flush();
	}

	public void saveAnswerTestWithoutFile(Assignment assignment, Member member, String answer) {
		Session session = sessionFactory.getCurrentSession();
		Date now = new Date();
		Score score = new Score(assignment, member, now, answer);
		score.setCountUpdate(1);
		session.save(score);
		session.flush();
	}

	public void updateAnswerTestNotChangeFile(String answer, Integer idScore, Integer redoTime) {
		Session session = sessionFactory.getCurrentSession();
		Integer countUpdate = (Integer) session
				.createQuery("select countUpdate from Score where idScore =:id").setInteger("id", idScore)
				.uniqueResult();
		//prevent cheat update
		if(redoTime == countUpdate)
			return;
		countUpdate = countUpdate + 1;
		Date now = new Date();
		session.createQuery(
				"update Score set answer =:answer, timeStore =:timeStore, countUpdate =:count where idScore =:idScore")
				.setString("answer", answer).setTimestamp("timeStore", now).setInteger("idScore", idScore)
				.setInteger("count", countUpdate).executeUpdate();
	}

	public void updateAnswerTestChangeFile(String answer, Blob file, String fileName, Integer idScore,
			Integer redoTime) throws SQLException {
		Session session = sessionFactory.getCurrentSession();
		Integer countUpdate = (Integer) session
				.createQuery("select countUpdate from Score where idScore =:id").setInteger("id", idScore)
				.uniqueResult();
		//prevent cheat update
		if(redoTime == countUpdate)
			return;
		countUpdate = countUpdate + 1;
		Date now = new Date();
		if (file != null)
			session.createQuery(
					"update Score set answer =:answer, timeStore =:timeStore, attachFile =:file, attachFileName =:fileName, countUpdate =:count "
							+ "where idScore =:idScore").setString("answer", answer)
					.setTimestamp("timeStore", now).setInteger("idScore", idScore)
					.setInteger("count", countUpdate)
					.setBinary("file", file.getBytes(1, (int) file.length())).setString("fileName", fileName)
					.executeUpdate();
		else
			session.createQuery(
					"update Score set answer =:answer, timeStore =:timeStore, attachFile =:file, attachFileName =:fileName, countUpdate =:count "
							+ "where idScore =:idScore").setString("answer", answer)
					.setTimestamp("timeStore", now).setInteger("idScore", idScore)
					.setInteger("count", countUpdate).setBinary("file", null).setString("fileName", null)
					.executeUpdate();
		session.flush();
	}

	public String getAnswerFileNameById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		String result = (String) session.createCriteria(Score.class).add(Restrictions.eq("idScore", id))
				.setProjection(Projections.projectionList().add(Projections.property("attachFileName")))
				.uniqueResult();
		return result;
	}

	public Blob getAnswerFileById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Blob result = (Blob) session.createCriteria(Score.class).add(Restrictions.eq("idScore", id))
				.setProjection(Projections.projectionList().add(Projections.property("attachFile")))
				.uniqueResult();
		return result;
	}
}
