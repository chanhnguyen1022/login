package mock02.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mock02.model.Contract;
import mock02.model.Course;

@Repository("manageContractDAO")
@Transactional
public class ManageContractDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// them course vao db
	public void addContract(Contract contract) {
		sessionFactory.getCurrentSession().save(contract);
	}

	// xoa course
	public void deleteContract(Contract contractId) {
		sessionFactory.getCurrentSession().delete(contractId);
	}

	// sửa
	public void editContract(Contract contract) {
		sessionFactory.getCurrentSession().update(contract);
	}

	// lay contract dua vao contractId
	public Contract getContract(int contractId) {
		return (Contract) sessionFactory.getCurrentSession().get(Contract.class, contractId);
	}

	// lay contract dua vao contractId
	public List getAllContract() {
		return sessionFactory.getCurrentSession().createQuery("from Contract").list();
	}

	 
	
	public static void main(String[] args) {
//		new ManageContractDAO().createContract(new Contract("Hợp đồng 1", "Cntt"));
//		new ManageContractDAOImpl().deleteContract(1);
	}

}
