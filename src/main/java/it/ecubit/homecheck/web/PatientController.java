package it.ecubit.homecheck.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import it.ecubit.pse.api.dtos.HomeCheckPatientUserDTO;
import it.ecubit.pse.api.exceptions.InvalidParameterFormatException;
import it.ecubit.pse.api.exceptions.PSEServiceException;
import it.ecubit.pse.api.interfaces.AslVtPatientServiceInterface;
import it.ecubit.pse.api.interfaces.HidaPatientServiceInterface;
import it.ecubit.pse.api.interfaces.LncPatientServiceInterface;
import it.ecubit.pse.api.interfaces.SalusPatientServiceInterface;
import it.ecubit.pse.api.services.HCPathologyService;
import it.ecubit.pse.api.services.ProvinceService;
import it.ecubit.pse.api.services.UserService;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.Domain;
import it.ecubit.pse.mongo.entities.HCPathology;
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
	
	@Autowired
	private HCPathologyService hcPathologyService;

	@Value("${user.domain}")
	private String userDomain;

	UserProperties userProperties;

	@Autowired
	private ProvinceService provinceService;
	
	private String nurseId = "5";

	@ModelAttribute("patient")
	public HomeCheckPatientUserDTO userRegistrationDto(Model model) {
		List<HCPathology> pathologiesTestaECollo = new ArrayList<>();
		List<HCPathology> pathologiesOcchi = new ArrayList<>();
		List<HCPathology> pathologiesToraceECuore = new ArrayList<>();
		List<HCPathology> pathologiesSpalla = new ArrayList<>();
		List<HCPathology> pathologiesAddome = new ArrayList<>();
		List<HCPathology> pathologiesBracciaEMani = new ArrayList<>();
		List<HCPathology> pathologiesZonaPelvica = new ArrayList<>();
		List<HCPathology> pathologiesGambe = new ArrayList<>();
		List<HCPathology> pathologiesPiedi = new ArrayList<>();
		List<HCPathology> pathologiesGenerico = new ArrayList<>();
		pathologiesTestaECollo = hcPathologyService.getByScope(HCPathology.PathologyScope.TESTA_E_COLLO.toString());
		pathologiesOcchi = hcPathologyService.getByScope(HCPathology.PathologyScope.OCCHI.toString());
		pathologiesToraceECuore = hcPathologyService.getByScope(HCPathology.PathologyScope.TORACE_E_CUORE.toString());
		pathologiesSpalla = hcPathologyService.getByScope(HCPathology.PathologyScope.SPALLA.toString());
		pathologiesAddome = hcPathologyService.getByScope(HCPathology.PathologyScope.ADDOME.toString());
		pathologiesBracciaEMani = hcPathologyService.getByScope(HCPathology.PathologyScope.BRACCIA_E_MANI.toString());
		pathologiesZonaPelvica = hcPathologyService.getByScope(HCPathology.PathologyScope.ZONA_PELVICA.toString());
		pathologiesGambe = hcPathologyService.getByScope(HCPathology.PathologyScope.GAMBE.toString());
		pathologiesPiedi = hcPathologyService.getByScope(HCPathology.PathologyScope.PIEDI.toString());
		pathologiesGenerico = hcPathologyService.getByScope(HCPathology.PathologyScope.GENERICO.toString());
		
		model.addAttribute("pathologiesTestaECollo", pathologiesTestaECollo);
		model.addAttribute("pathologiesOcchi", pathologiesOcchi);
		model.addAttribute("pathologiesToraceECuore", pathologiesToraceECuore);
		model.addAttribute("pathologiesSpalla", pathologiesSpalla);
		model.addAttribute("pathologiesAddome", pathologiesAddome);
		model.addAttribute("pathologiesBracciaEMani", pathologiesBracciaEMani);
		model.addAttribute("pathologiesZonaPelvica", pathologiesZonaPelvica);
		model.addAttribute("pathologiesGambe", pathologiesGambe);
		model.addAttribute("pathologiesPiedi", pathologiesPiedi);
		model.addAttribute("pathologiesGenerico", pathologiesGenerico);
		return new HomeCheckPatientUserDTO();
	}


	@GetMapping
	public String showRegistrationForm(Model model) {
		List<Province> province = provinceService.getAll(Sort.by(Order.asc(Province.CODE_FIELD_NAME)));
		List<PSEUser> users = userService.getAllByRole(this.nurseId);
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
			if (newPatient.getIdOperatoreSanitarioAssegnato() != null && newPatient.getNomeOperatoreSanitarioAssegnato() == null) {
				PSEUser nurseAsUser = userService.getById(newPatient.getIdOperatoreSanitarioAssegnato());
				newPatient.setNomeOperatoreSanitarioAssegnato(nurseAsUser.getNome() + " " + nurseAsUser.getCognome());
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
