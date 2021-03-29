package it.ecubit.homecheck.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.ecubit.pse.api.interfaces.AslVtPatientServiceInterface;

@Controller
public class MainController {
	@Autowired
	private AslVtPatientServiceInterface aslVtPatientService;

    @GetMapping("/")
    public String root() {
    	aslVtPatientService.getAllAsList();
        return "dashboard";
    }
    
    @GetMapping("{value}")
    public String page(@PathVariable("value") String value) {
        return value;
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
}
