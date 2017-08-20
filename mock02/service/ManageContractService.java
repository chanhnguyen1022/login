package mock02.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mock02.dao.ManageContractDAO;
import mock02.model.Contract;

@Service("manageContractService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ManageContractService {

	@Autowired
	ManageContractDAO manageContractDAO;

	public void addContract(Contract contract) {
		manageContractDAO.addContract(contract);

	}

	public void deleteContract(Contract contractId) {
		manageContractDAO.deleteContract(contractId);

	}

	public void editContract(Contract contract) {
		manageContractDAO.editContract(contract);

	}

	public Contract getContract(int contractId) {
		return manageContractDAO.getContract(contractId);
	}

	public List getAllContract() {
		return manageContractDAO.getAllContract();
	}

}
