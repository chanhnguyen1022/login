package mock02.service;

import java.util.List;

import mock02.dao.ParticipationDAO;
import mock02.model.Discussion;
import mock02.model.Participation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ThaiHa
 * @verision:1.0 Dec 26, 2015
 **/
@Service("participationService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ParticipationService {
    @Autowired
    ParticipationDAO participationDAO;
    //add participation
    public void addParticipation(Participation participation) {
	participationDAO.addParticipation(participation);
    }
    
    // get list participation from discussion
    public List<Participation> getListPaticipations(Discussion discussion) {
	return participationDAO.getListPaticipations(discussion);
    }
}
