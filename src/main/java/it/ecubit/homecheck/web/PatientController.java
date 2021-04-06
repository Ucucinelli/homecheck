package it.ecubit.homecheck.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.ecubit.homecheck.service.UserService;
import it.ecubit.homecheck.web.dto.AslVtPatientUserDTO;
import it.ecubit.homecheck.web.dto.UserRegistrationDto;

import it.ecubit.pse.api.exceptions.PSEServiceException;
import it.ecubit.pse.api.interfaces.AslVtPatientServiceInterface;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.PSEUser;

@Controller
@RequestMapping("/patients")
public class PatientController {

	@Autowired
	private UserService userService;

	@Autowired
	private AslVtPatientServiceInterface aslVtPatientService;

	@ModelAttribute("patient")
	public AslVtPatientUserDTO userRegistrationDto() {
		return new AslVtPatientUserDTO();
	}

	@GetMapping
	public String showRegistrationForm(Model model) {
		return "new-paziente";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("patient") @Valid AslVtPatientUserDTO patientDTO,
			BindingResult result) throws PSEServiceException {

		/*
		 * PSEUser existing = userService.findByEmail(patientDTO.getEmail()); if
		 * (existing != null) { result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 */

		if (result.hasErrors()) {
			return "new-paziente";
		}
		/*
		 * AslVtPatient newPatient = patientDTO.getPatient();
		 * 
		 * aslVtPatientService.save(newPatient); return
		 * "redirect:/registration?success";
		 */

		return "redirect:/patients#PatologiePaziente";
	}

}
