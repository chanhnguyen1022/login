package mock02.dao;

import java.util.List;

import mock02.model.Discussion;
import mock02.model.Participation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 26, 2015
 **/
@Repository("paticipationDAO")
@Transactional
public class ParticipationDAO {
    @Autowired
    private SessionFactory sessionFactory;

    // add participation
    public void addParticipation(Participation participation) {
	sessionFactory.getCurrentSession().saveOrUpdate(participation);
    }

    // get list participation from discussion
    @SuppressWarnings("unchecked")
    public List<Participation> getListPaticipations(Discussion discussion) {
	Session session = sessionFactory.getCurrentSession();
	List<Participation> listParticipation = (List<Participation>) session
		.createCriteria(Participation.class)
		.add(Restrictions.eq("discussion", discussion)).list();
	return listParticipation;
    }
}
