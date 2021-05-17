package it.ecubit.homecheck.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import it.ecubit.pse.api.services.DeviceService;
import it.ecubit.pse.api.services.HCPathologyService;
import it.ecubit.pse.api.services.ProvinceService;
import it.ecubit.pse.api.services.UserService;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.Device;
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
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private DeviceService deviceService;

	@Value("${user.domain}")
	private String userDomain;

	UserProperties userProperties;

	
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
		
		List<Device> devices = deviceService.findAllUnassigned();
		
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
		
		model.addAttribute("devices", devices);
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
	
	@RequestMapping(path = "/remove/{id}")
	public String removeById(@PathVariable("id") String patientIdentifier, Model model) throws PSEServiceException {
		List<Device> devicesAssignedToPatient =  deviceService.findByPatientId(patientIdentifier);
		for(Device device : devicesAssignedToPatient) {
			deviceService.revokeDeviceAssignmentToPatient(device.getId(), patientIdentifier);
		}
		if(userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			aslVtPatientService.deleteById(patientIdentifier);
		}
		else if(userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			hidaPatientService.deleteById(patientIdentifier);
		}
		else if(userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			lncPatientService.deleteById(patientIdentifier);
		}
		else if(userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			salusPatientService.deleteById(patientIdentifier);
		}
		else {
			throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {userDomain});
		}
		return this.getAllAsList(model);
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
	
	
	@GetMapping(path = "/{id}")
	public String getById(@PathVariable("id") String patientIdentifier, Model model) throws PSEServiceException {
		PSEUser user = userService.getById(patientIdentifier);
		HomeCheckPatient patient;
		List <Device> devicesUnassigned = deviceService.findAllUnassigned();
		List <Device> devices = deviceService.findByPatientId(patientIdentifier);
		devices.addAll(devicesUnassigned);
		model.addAttribute("devices", devices);
		if(userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			patient = aslVtPatientService.getById(patientIdentifier);
		}
		else if(userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			patient = hidaPatientService.getById(patientIdentifier);				
		}
		else if(userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			patient = lncPatientService.getById(patientIdentifier);			
		}
		else if(userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			patient = salusPatientService.getById(patientIdentifier);			
		}
		else {
			throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {userDomain});
		}
		List<HCPathology> pathologiesTestaECollo = new ArrayList<>();
		List<HCPathology> pathologiesTestaEColloSelected = new ArrayList<>();
		List<HCPathology> pathologiesOcchi = new ArrayList<>();
		List<HCPathology> pathologiesOcchiSelected = new ArrayList<>();
		List<HCPathology> pathologiesToraceECuore = new ArrayList<>();
		List<HCPathology> pathologiesToraceECuoreSelected = new ArrayList<>();
		List<HCPathology> pathologiesSpalla = new ArrayList<>();
		List<HCPathology> pathologiesSpallaSelected = new ArrayList<>();
		List<HCPathology> pathologiesAddome = new ArrayList<>();
		List<HCPathology> pathologiesAddomeSelected = new ArrayList<>();
		List<HCPathology> pathologiesBracciaEMani = new ArrayList<>();
		List<HCPathology> pathologiesBracciaEManiSelected = new ArrayList<>();
		List<HCPathology> pathologiesZonaPelvica = new ArrayList<>();
		List<HCPathology> pathologiesZonaPelvicaSelected = new ArrayList<>();
		List<HCPathology> pathologiesGambe = new ArrayList<>();
		List<HCPathology> pathologiesGambeSelected = new ArrayList<>();
		List<HCPathology> pathologiesPiedi = new ArrayList<>();
		List<HCPathology> pathologiesPiediSelected = new ArrayList<>();
		List<HCPathology> pathologiesGenerico = new ArrayList<>();
		List<HCPathology> pathologiesGenericoSelected = new ArrayList<>();
        for (HCPathology pathology: patient.getPatologie()) {
        	if (pathology.getAmbito().name().equals(HCPathology.PathologyScope.TESTA_E_COLLO.toString()))
        	    pathologiesTestaEColloSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.OCCHI.toString()))
        		pathologiesOcchiSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.TORACE_E_CUORE.toString()))
    		    pathologiesToraceECuoreSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.SPALLA.toString()))
    		    pathologiesSpallaSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.ADDOME.toString()))
    		    pathologiesAddomeSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.BRACCIA_E_MANI.toString()))
    		    pathologiesBracciaEManiSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.ZONA_PELVICA.toString()))
    		    pathologiesZonaPelvicaSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.GAMBE.toString()))
    		    pathologiesGambeSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.PIEDI.toString()))
    		    pathologiesPiediSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.GENERICO.toString()))
    		    pathologiesGenericoSelected.add(pathology);  	    			
        }
        pathologiesTestaECollo = hcPathologyService.getByScope(HCPathology.PathologyScope.TESTA_E_COLLO.toString());
        pathologiesTestaECollo.removeAll(pathologiesTestaEColloSelected);
        pathologiesOcchi = hcPathologyService.getByScope(HCPathology.PathologyScope.OCCHI.toString());
        pathologiesOcchi.removeAll(pathologiesOcchiSelected);
        pathologiesToraceECuore = hcPathologyService.getByScope(HCPathology.PathologyScope.TORACE_E_CUORE.toString());
        pathologiesToraceECuore.removeAll(pathologiesToraceECuoreSelected);
        pathologiesSpalla = hcPathologyService.getByScope(HCPathology.PathologyScope.SPALLA.toString());
        pathologiesSpalla.removeAll(pathologiesSpallaSelected);
        pathologiesAddome = hcPathologyService.getByScope(HCPathology.PathologyScope.ADDOME.toString());
        pathologiesAddome.removeAll(pathologiesAddomeSelected);
        pathologiesBracciaEMani = hcPathologyService.getByScope(HCPathology.PathologyScope.BRACCIA_E_MANI.toString());
        pathologiesBracciaEMani.removeAll(pathologiesBracciaEManiSelected);
        pathologiesZonaPelvica = hcPathologyService.getByScope(HCPathology.PathologyScope.ZONA_PELVICA.toString());
        pathologiesZonaPelvica.removeAll(pathologiesZonaPelvicaSelected);
        pathologiesGambe = hcPathologyService.getByScope(HCPathology.PathologyScope.GAMBE.toString());
        pathologiesGambe.removeAll(pathologiesGambeSelected);
        pathologiesPiedi = hcPathologyService.getByScope(HCPathology.PathologyScope.PIEDI.toString());
        pathologiesPiedi.removeAll(pathologiesPiediSelected);
        pathologiesGenerico = hcPathologyService.getByScope(HCPathology.PathologyScope.GENERICO.toString());
        pathologiesGenerico.removeAll(pathologiesGenericoSelected);
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
        model.addAttribute("pathologiesTestaEColloSelected", pathologiesTestaEColloSelected);
		model.addAttribute("pathologiesOcchiSelected", pathologiesOcchiSelected);
		model.addAttribute("pathologiesToraceECuoreSelected", pathologiesToraceECuoreSelected);
		model.addAttribute("pathologiesSpallaSelected", pathologiesSpallaSelected);
		model.addAttribute("pathologiesAddomeSelected", pathologiesAddomeSelected);
		model.addAttribute("pathologiesBracciaEManiSelected", pathologiesBracciaEManiSelected);
		model.addAttribute("pathologiesZonaPelvicaSelected", pathologiesZonaPelvicaSelected);
		model.addAttribute("pathologiesGambeSelected", pathologiesGambeSelected);
		model.addAttribute("pathologiesPiediSelected", pathologiesPiediSelected);
		model.addAttribute("pathologiesGenericoSelected", pathologiesGenericoSelected);
		
		HomeCheckPatientUserDTO patientDTO = new HomeCheckPatientUserDTO(patient, user);
		if (patientDTO.getBirthDate() != null) {
		   String birthDateString = patientDTO.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		   patientDTO.setBirthDateString(birthDateString);
		}
		model.addAttribute("patient", patientDTO);
		List<Province> province = provinceService.getAll(Sort.by(Order.asc(Province.CODE_FIELD_NAME)));
		List<PSEUser> users = userService.getAllByRole(this.nurseId);
		model.addAttribute("province", province);
		model.addAttribute("users", users);
		return "scheda-paziente";
	}
	
	
	@GetMapping(path = "/{id}/ConfigurazioneSensori")
	public String getConfigurazioneById(@PathVariable("id") String patientIdentifier, Model model) throws PSEServiceException {
		PSEUser user = userService.getById(patientIdentifier);
		HomeCheckPatient patient;
		List <Device> devicesUnassigned = deviceService.findAllUnassigned();
		List <Device> devices = deviceService.findByPatientId(patientIdentifier);
		devices.addAll(devicesUnassigned);
		model.addAttribute("devices", devices);
		if(userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			patient = aslVtPatientService.getById(patientIdentifier);
		}
		else if(userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			patient = hidaPatientService.getById(patientIdentifier);				
		}
		else if(userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			patient = lncPatientService.getById(patientIdentifier);			
		}
		else if(userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			patient = salusPatientService.getById(patientIdentifier);			
		}
		else {
			throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {userDomain});
		}
		List<HCPathology> pathologiesTestaECollo = new ArrayList<>();
		List<HCPathology> pathologiesTestaEColloSelected = new ArrayList<>();
		List<HCPathology> pathologiesOcchi = new ArrayList<>();
		List<HCPathology> pathologiesOcchiSelected = new ArrayList<>();
		List<HCPathology> pathologiesToraceECuore = new ArrayList<>();
		List<HCPathology> pathologiesToraceECuoreSelected = new ArrayList<>();
		List<HCPathology> pathologiesSpalla = new ArrayList<>();
		List<HCPathology> pathologiesSpallaSelected = new ArrayList<>();
		List<HCPathology> pathologiesAddome = new ArrayList<>();
		List<HCPathology> pathologiesAddomeSelected = new ArrayList<>();
		List<HCPathology> pathologiesBracciaEMani = new ArrayList<>();
		List<HCPathology> pathologiesBracciaEManiSelected = new ArrayList<>();
		List<HCPathology> pathologiesZonaPelvica = new ArrayList<>();
		List<HCPathology> pathologiesZonaPelvicaSelected = new ArrayList<>();
		List<HCPathology> pathologiesGambe = new ArrayList<>();
		List<HCPathology> pathologiesGambeSelected = new ArrayList<>();
		List<HCPathology> pathologiesPiedi = new ArrayList<>();
		List<HCPathology> pathologiesPiediSelected = new ArrayList<>();
		List<HCPathology> pathologiesGenerico = new ArrayList<>();
		List<HCPathology> pathologiesGenericoSelected = new ArrayList<>();
        for (HCPathology pathology: patient.getPatologie()) {
        	if (pathology.getAmbito().name().equals(HCPathology.PathologyScope.TESTA_E_COLLO.toString()))
        	    pathologiesTestaEColloSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.OCCHI.toString()))
        		pathologiesOcchiSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.TORACE_E_CUORE.toString()))
    		    pathologiesToraceECuoreSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.SPALLA.toString()))
    		    pathologiesSpallaSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.ADDOME.toString()))
    		    pathologiesAddomeSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.BRACCIA_E_MANI.toString()))
    		    pathologiesBracciaEManiSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.ZONA_PELVICA.toString()))
    		    pathologiesZonaPelvicaSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.GAMBE.toString()))
    		    pathologiesGambeSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.PIEDI.toString()))
    		    pathologiesPiediSelected.add(pathology);
        	if  (pathology.getAmbito().name().equals(HCPathology.PathologyScope.GENERICO.toString()))
    		    pathologiesGenericoSelected.add(pathology);  	    			
        }
        pathologiesTestaECollo = hcPathologyService.getByScope(HCPathology.PathologyScope.TESTA_E_COLLO.toString());
        pathologiesTestaECollo.removeAll(pathologiesTestaEColloSelected);
        pathologiesOcchi = hcPathologyService.getByScope(HCPathology.PathologyScope.OCCHI.toString());
        pathologiesOcchi.removeAll(pathologiesOcchiSelected);
        pathologiesToraceECuore = hcPathologyService.getByScope(HCPathology.PathologyScope.TORACE_E_CUORE.toString());
        pathologiesToraceECuore.removeAll(pathologiesToraceECuoreSelected);
        pathologiesSpalla = hcPathologyService.getByScope(HCPathology.PathologyScope.SPALLA.toString());
        pathologiesSpalla.removeAll(pathologiesSpallaSelected);
        pathologiesAddome = hcPathologyService.getByScope(HCPathology.PathologyScope.ADDOME.toString());
        pathologiesAddome.removeAll(pathologiesAddomeSelected);
        pathologiesBracciaEMani = hcPathologyService.getByScope(HCPathology.PathologyScope.BRACCIA_E_MANI.toString());
        pathologiesBracciaEMani.removeAll(pathologiesBracciaEManiSelected);
        pathologiesZonaPelvica = hcPathologyService.getByScope(HCPathology.PathologyScope.ZONA_PELVICA.toString());
        pathologiesZonaPelvica.removeAll(pathologiesZonaPelvicaSelected);
        pathologiesGambe = hcPathologyService.getByScope(HCPathology.PathologyScope.GAMBE.toString());
        pathologiesGambe.removeAll(pathologiesGambeSelected);
        pathologiesPiedi = hcPathologyService.getByScope(HCPathology.PathologyScope.PIEDI.toString());
        pathologiesPiedi.removeAll(pathologiesPiediSelected);
        pathologiesGenerico = hcPathologyService.getByScope(HCPathology.PathologyScope.GENERICO.toString());
        pathologiesGenerico.removeAll(pathologiesGenericoSelected);
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
        model.addAttribute("pathologiesTestaEColloSelected", pathologiesTestaEColloSelected);
		model.addAttribute("pathologiesOcchiSelected", pathologiesOcchiSelected);
		model.addAttribute("pathologiesToraceECuoreSelected", pathologiesToraceECuoreSelected);
		model.addAttribute("pathologiesSpallaSelected", pathologiesSpallaSelected);
		model.addAttribute("pathologiesAddomeSelected", pathologiesAddomeSelected);
		model.addAttribute("pathologiesBracciaEManiSelected", pathologiesBracciaEManiSelected);
		model.addAttribute("pathologiesZonaPelvicaSelected", pathologiesZonaPelvicaSelected);
		model.addAttribute("pathologiesGambeSelected", pathologiesGambeSelected);
		model.addAttribute("pathologiesPiediSelected", pathologiesPiediSelected);
		model.addAttribute("pathologiesGenericoSelected", pathologiesGenericoSelected);
		
		HomeCheckPatientUserDTO patientDTO = new HomeCheckPatientUserDTO(patient, user);
		if (patientDTO.getBirthDate() != null) {
		   String birthDateString = patientDTO.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		   patientDTO.setBirthDateString(birthDateString);
		}
		model.addAttribute("patient", patientDTO);
		List<Province> province = provinceService.getAll(Sort.by(Order.asc(Province.CODE_FIELD_NAME)));
		List<PSEUser> users = userService.getAllByRole(this.nurseId);
		model.addAttribute("province", province);
		model.addAttribute("users", users);
		return "redirect:/patients/"+patientIdentifier+"#ConfigurazioneSensori";
	}
	
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("patient") @Valid HomeCheckPatientUserDTO patientDTO, Model model,
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
			if (!newPatient.getIdOperatoreSanitarioAssegnato().equals("") && newPatient.getNomeOperatoreSanitarioAssegnato() == null) {
				PSEUser nurseAsUser = userService.getById(newPatient.getIdOperatoreSanitarioAssegnato());
				newPatient.setNomeOperatoreSanitarioAssegnato(nurseAsUser.getNome() + " " + nurseAsUser.getCognome());
			}
			for (Device device: patientDTO.getDevices()) {
				if (device.getTermineAssegnazioneString() != null) {
				   LocalDate dateEnd = LocalDate.parse(device.getTermineAssegnazioneString(), formatter);
				   device.setTermineAssegnazione(dateEnd);
				}
			}
			if (userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				AslVtPatient patient = aslVtPatientService.save((AslVtPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), patient.getId(), Optional.of(device.getTermineAssegnazione()));
				}
				
			} else if (userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				HidaPatient newHidaPatient = hidaPatientService.save((HidaPatient) newPatient);
				model.addAttribute("patient", newHidaPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato() != null)
					   deviceService.assignDeviceToPatient(device.getId(), newHidaPatient.getId(), Optional.of(device.getTermineAssegnazione()));
				}
			} else if (userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				LncPatient newLncPatient = lncPatientService.save((LncPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), newLncPatient.getId(), Optional.of(device.getTermineAssegnazione()));
				}
			} else if (userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				SalusPatient newSalusPatient = salusPatientService.save((SalusPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), newSalusPatient.getId(), Optional.of(device.getTermineAssegnazione()));
				}
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
