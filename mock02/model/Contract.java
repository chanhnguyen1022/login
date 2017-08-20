package mock02.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contract", catalog = "hoctructuyen")
public class Contract implements java.io.Serializable {

	private Integer contractId;
	private String contractName;
	private Date start_date;
	private Date end_date;
	private String description;
	private String status;
	private String department;

	public Contract() {
	}

	public Contract(String contractName, String department) {
		super();

		this.contractName = contractName;
		this.department = department;
	}

	public Contract(Integer contractId, String contractName, Date start_date, Date end_date, String description,
			String status, String department) {
		super();
		this.contractId = contractId;
		this.contractName = contractName;
		this.start_date = start_date;
		this.end_date = end_date;
		this.description = description;
		this.status = status;
		this.department = department;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "contractId", unique = true, nullable = false)
	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	@Column(name = "contractName", nullable = false, length = 100)
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
