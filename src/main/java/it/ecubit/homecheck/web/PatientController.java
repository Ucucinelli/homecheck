package it.ecubit.homecheck.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.ecubit.homecheck.UserProperties;
import it.ecubit.homecheck.web.dto.HomeCheckPatientUserDTO;
import it.ecubit.pse.api.exceptions.InvalidParameterFormatException;
import it.ecubit.pse.api.exceptions.PSEServiceException;
import it.ecubit.pse.api.interfaces.AslVtPatientServiceInterface;
import it.ecubit.pse.api.interfaces.HidaPatientServiceInterface;
import it.ecubit.pse.api.interfaces.LncPatientServiceInterface;
import it.ecubit.pse.api.interfaces.SalusPatientServiceInterface;
import it.ecubit.pse.api.services.ProvinceService;
import it.ecubit.pse.api.services.UserService;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.Domain;
import it.ecubit.pse.mongo.entities.HidaPatient;
import it.ecubit.pse.mongo.entities.HomeCheckPatient;
import it.ecubit.pse.mongo.entities.LncPatient;
import it.ecubit.pse.mongo.entities.PSEUser;
import it.ecubit.pse.mongo.entities.Province;
import it.ecubit.pse.mongo.entities.SalusPatient;
import it.ecubit.pse.mongo.utils.TypedRole.RoleType;

@Controller
@RequestMapping("/patients")
public class PatientController {

	@Autowired
	private UserService userService;

	@Autowired
	private AslVtPatientServiceInterface aslVtPatientService;

	@Autowired
	private HidaPatientServiceInterface hidaPatientService;

	@Autowired
	private LncPatientServiceInterface lncPatientService;

	@Autowired
	private SalusPatientServiceInterface salusPatientService;

	@Value("${user.domain}")
	private String userDomain;

	UserProperties userProperties;

	@Autowired
	private ProvinceService provinceService;
	
	private String nurseId = "5";

	@ModelAttribute("patient")
	public HomeCheckPatientUserDTO userRegistrationDto() {
		return new HomeCheckPatientUserDTO();
	}


	@GetMapping
	public String showRegistrationForm(Model model) {
		List<Province> province = provinceService.getAll(Sort.by(Order.asc(Province.CODE_FIELD_NAME)));
		List<PSEUser> users = userService.getAllByRuolo(this.nurseId);
		model.addAttribute("province", province);
		model.addAttribute("users", users);
		return "new-paziente";
	}
	
	
	@GetMapping(path = "list")
	public String getAllAsList(Model model) throws PSEServiceException {
		List<? extends HomeCheckPatient> patients;
		if(this.userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			patients = aslVtPatientService.getAllAsList();
		}
		else if(this.userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			patients = hidaPatientService.getAllAsList(); 
		}
		else if(this.userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			patients = lncPatientService.getAllAsList();
		}
		else if(this.userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			patients = salusPatientService.getAllAsList();
		}
		else {
			throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {this.userDomain});
		}
		model.addAttribute("patients", patients);
		return "lista-paziente";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("patient") @Valid HomeCheckPatientUserDTO patientDTO,
			BindingResult result) throws PSEServiceException {

		/*
		 * PSEUser existing = userService.findByEmail(patientDTO.getEmail()); if
		 * (existing != null) { result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 */

		if (result.hasErrors()) {
			return "new-paziente";
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date = LocalDate.parse(patientDTO.getBirthDateString(), formatter);
			patientDTO.setBirthDate(date);
			patientDTO.setDomain(userDomain);

			PSEUser newUser = patientDTO.getUser();
			newUser.addRole(userService.getRoleByName("patient"), RoleType.PRIMARY);
			String newId = userService.save(newUser);
			HomeCheckPatient newPatient = patientDTO.getPatient();
			newPatient.setId(newId);
			if (newPatient.getIdMedico() != null && newPatient.getNomeMedicoCurante() == null) {
				PSEUser clinicianAsUser = userService.getById(newPatient.getIdMedico());
				newPatient.setNomeMedicoCurante(clinicianAsUser.getNome() + " " + clinicianAsUser.getCognome());
			}
			if (userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				AslVtPatient patient = aslVtPatientService.save((AslVtPatient) newPatient);
				System.out.println("paziente : " + patient);
			} else if (userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				hidaPatientService.save((HidaPatient) newPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				lncPatientService.save((LncPatient) newPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				salusPatientService.save((SalusPatient) newPatient);
			} else {
				throw new InvalidParameterFormatException("domain.not.recognized", new Object[] { userDomain });
			}
		} catch (Exception e) {
            System.out.println("Exception : "+e);
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
