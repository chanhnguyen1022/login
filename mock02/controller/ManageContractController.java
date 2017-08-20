package mock02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mock02.model.Contract;
import mock02.service.ManageContractService;

@Controller
@RequestMapping(value = "contract")
public class ManageContractController {

	@Autowired
	ManageContractService manageContractService;

	@RequestMapping(value = "managecontract", method = RequestMethod.GET)
	public String manageContractPage() {

		return "admin_managecontract";
	}

	@RequestMapping(value = "getallcontract", method = RequestMethod.GET)
	public String getallContract(ModelMap mm) {
		mm.put("listContract", manageContractService.getAllContract());
		return "admin_managecontract";
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String remove(@RequestParam(value = "id") int id) {

		// lấy sinh vien dựa vào mã id của đối tượng
		Contract con = manageContractService.getContract(id);
		manageContractService.deleteContract(con);;

		return "redirect:getallcontract.html";
	}

	@RequestMapping(value = "/editContract", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id") int id, Model m) {

		// lấy sinh vien dựa vào mã id của đối tượng
		Contract con = manageContractService.getContract(id);
		// thêm các thuộc tính student cái mà dc lấy theo id cho model
		m.addAttribute("con", con);

		return "admin_edit_contract";
	}
	
	 @RequestMapping(value = "/updateContract", method = RequestMethod.POST)
	    public String update(@ModelAttribute(value = "upContract") Contract con) {

	        manageContractService.editContract(con);;
	        return "redirect:getallcontract.html";
	    }
	
	
	@RequestMapping(value = "/createContract", method = RequestMethod.POST)
    public String create(@ModelAttribute(value = "contract") Contract contract) {
      
        manageContractService.addContract(contract);
        return "redirect:getallcontract.html";

    }

}
