package it.ecubit.homecheck.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import it.ecubit.homecheck.web.dto.SensorMeasurementThresholdValue;
import it.ecubit.pse.api.dtos.HomeCheckPatientUserDTO;
import it.ecubit.pse.api.dtos.SensorMeasurementThresholdValueDTO;
import it.ecubit.pse.api.dtos.SensorThresholdValueDTO;
import it.ecubit.pse.api.exceptions.InvalidParameterFormatException;
import it.ecubit.pse.api.exceptions.PSEServiceException;
import it.ecubit.pse.api.interfaces.AslVtPatientServiceInterface;
import it.ecubit.pse.api.interfaces.DeviceSensorServiceInterface;
import it.ecubit.pse.api.interfaces.DeviceServiceInterface;
import it.ecubit.pse.api.interfaces.HCPathologyServiceInterface;
import it.ecubit.pse.api.interfaces.HidaPatientServiceInterface;
import it.ecubit.pse.api.interfaces.LncPatientServiceInterface;
import it.ecubit.pse.api.interfaces.ProvinceServiceInterface;
import it.ecubit.pse.api.interfaces.SalusPatientServiceInterface;
import it.ecubit.pse.api.interfaces.SensorMeasurementScheduleServiceInterface;
import it.ecubit.pse.api.interfaces.SensorThresholdValueServiceInterface;
import it.ecubit.pse.api.interfaces.UserServiceInterface;
import it.ecubit.pse.api.services.DeviceSensorService;
import it.ecubit.pse.api.services.DeviceService;
import it.ecubit.pse.api.services.HCPathologyService;
import it.ecubit.pse.api.services.ProvinceService;
import it.ecubit.pse.api.services.UserService;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.Device;
import it.ecubit.pse.mongo.entities.DeviceSensor;
import it.ecubit.pse.mongo.entities.DeviceType;
import it.ecubit.pse.mongo.entities.Domain;
import it.ecubit.pse.mongo.entities.HCPathology;
import it.ecubit.pse.mongo.entities.HidaPatient;
import it.ecubit.pse.mongo.entities.HomeCheckPatient;
import it.ecubit.pse.mongo.entities.LncPatient;
import it.ecubit.pse.mongo.entities.PSEUser;
import it.ecubit.pse.mongo.entities.Province;
import it.ecubit.pse.mongo.entities.SalusPatient;
import it.ecubit.pse.mongo.entities.SensorDefaultThresholdValue;
import it.ecubit.pse.mongo.entities.SensorMeasurementPeriod;
import it.ecubit.pse.mongo.entities.SensorMeasurementTimeSchedule;
import it.ecubit.pse.mongo.entities.SensorType;
import it.ecubit.pse.mongo.utils.TypedRole.RoleType;

@Controller
@RequestMapping("/patients")
public class PatientController {

	@Autowired
	private UserServiceInterface userService;

	@Autowired
	private AslVtPatientServiceInterface aslVtPatientService;

	@Autowired
	private HidaPatientServiceInterface hidaPatientService;

	@Autowired
	private LncPatientServiceInterface lncPatientService;

	@Autowired
	private SalusPatientServiceInterface salusPatientService;
	
	@Autowired
	private HCPathologyServiceInterface hcPathologyService;
	
	@Autowired
	private ProvinceServiceInterface provinceService;
	
	@Autowired
	private DeviceServiceInterface deviceService;
	
	@Autowired
	private DeviceSensorServiceInterface deviceSensorService;
	
	@Autowired
	private SensorThresholdValueServiceInterface sensorThresholdValueService;
	
	@Autowired
	private SensorMeasurementScheduleServiceInterface sensorMeasurementScheduleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
		Comparator<SensorType> sensorTypeComparator = Comparator.comparing(SensorType::getTipo);
		Comparator<DeviceSensor> deviceSensorComparator = Comparator.comparing(DeviceSensor::getTipoSensore, sensorTypeComparator);
		Set<DeviceSensor> devicesSensor = new TreeSet<>(deviceSensorComparator);
		Set<DeviceType> devicesType = new LinkedHashSet<>();
		List<SensorThresholdValueDTO> sensors = new ArrayList<>();
		List<SensorMeasurementThresholdValueDTO> outerSensors = new ArrayList<>();
		Sort defaultThresholdSort = Sort.by(Direction.ASC, SensorDefaultThresholdValue.THRESHOLD_TYPE_FIELD_NAME, SensorDefaultThresholdValue.MIN_THRESHOLD_VALUE_FIELD_NAME, SensorDefaultThresholdValue.MAX_THRESHOLD_VALUE_FIELD_NAME);
		Sort scheduleSort = Sort.by(SensorMeasurementTimeSchedule.PERIOD_LABEL_FIELD_NAME);
		List<SensorMeasurementTimeSchedule> schedules = new ArrayList<>();
		
		List<Device> deviceAssigned = deviceService.findByPatientId(patientIdentifier);
		for (Device device: deviceAssigned)  {
			devicesType.add(device.getTipologia());
		}
		for (DeviceType deviceType: devicesType)  {
			 devicesSensor.addAll(deviceSensorService.findByDeviceType(deviceType));
		}
		
		
		for (DeviceSensor deviceSensor:devicesSensor)  {
		   String orarioMattina="";
		   String orarioPomeriggio="";
		   String orarioSera="";
		   String orarioNotte="";
		   sensors = sensorThresholdValueService.getThresholdForPatientAndSensorTypeSorted(patientIdentifier, deviceSensor.getTipoSensore(), defaultThresholdSort);
		   schedules = sensorMeasurementScheduleService.getDefinedSchedulesForPatientAndSensorTypeSorted(patientIdentifier, deviceSensor.getTipoSensore(), scheduleSort);
		   if (!schedules.isEmpty())
			   for (SensorMeasurementTimeSchedule schedule: schedules)  {
				   if (schedule.getEtichettaPeriodo().equals("Mattina"))
					   orarioMattina = schedule.getOrarioMisurazione();
		           if (schedule.getEtichettaPeriodo().equals("Pomeriggio"))
			           orarioPomeriggio = schedule.getOrarioMisurazione();
		           if (schedule.getEtichettaPeriodo().equals("Sera"))
			           orarioSera = schedule.getOrarioMisurazione();
		           if (schedule.getEtichettaPeriodo().equals("Notte"))
			           orarioNotte = schedule.getOrarioMisurazione();
			   }
		   if (sensors != null && sensors.size() > 0)   {
		   SensorMeasurementThresholdValueDTO sensorMeasuerementThresholdValue = new SensorMeasurementThresholdValueDTO(sensors, schedules, deviceSensor.getTipoSensore(), orarioMattina, orarioPomeriggio, orarioSera, orarioNotte);
		   
		   outerSensors.add(sensorMeasuerementThresholdValue);  
		   }
		}
		model.addAttribute("outerSensors", outerSensors);
		
		List<SensorMeasurementPeriod> measurementsPeriod = sensorMeasurementScheduleService.getAllSensorMeasurementPeriods();
		model.addAttribute("measurementsPeriod", measurementsPeriod);
		
		
		return "scheda-paziente";
	}
	
	
	@GetMapping(path = "/{id}/ConfigurazioneSensori")
	public String getConfigurazioneById(@PathVariable("id") String patientIdentifier, Model model) throws PSEServiceException {
		return "redirect:/patients/"+patientIdentifier+"#ConfigurazioneSensori";
	}
	
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("patient") @Valid HomeCheckPatientUserDTO patientDTO,
			BindingResult result, Model model) throws PSEServiceException {
		 

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
			/*
			 * if (newPatient.getIdMedico() != null && newPatient.getNomeMedicoCurante() ==
			 * null) { PSEUser clinicianAsUser =
			 * userService.getById(newPatient.getIdMedico());
			 * newPatient.setNomeMedicoCurante(clinicianAsUser.getNome() + " " +
			 * clinicianAsUser.getCognome()); } if
			 * (!newPatient.getIdOperatoreSanitarioAssegnato().equals("") &&
			 * newPatient.getNomeOperatoreSanitarioAssegnato() == null) { PSEUser
			 * nurseAsUser =
			 * userService.getById(newPatient.getIdOperatoreSanitarioAssegnato());
			 * newPatient.setNomeOperatoreSanitarioAssegnato(nurseAsUser.getNome() + " " +
			 * nurseAsUser.getCognome()); }
			 */
			for (Device device: patientDTO.getDevices()) {
				if (device.getSelezionato() != null && device.getSelezionato() == true) {
				   if(!device.getTermineAssegnazioneString().equals("")) {
				   LocalDate dateEnd = LocalDate.parse(device.getTermineAssegnazioneString(), formatter);
				   device.setTermineAssegnazione(dateEnd);
				   }
				}
			}
			if (userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				AslVtPatient patient = aslVtPatientService.save((AslVtPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), patient.getId(), Optional.ofNullable(device.getTermineAssegnazione()));
				}
				
			} else if (userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				HidaPatient newHidaPatient = hidaPatientService.save((HidaPatient) newPatient);
				model.addAttribute("patient", newHidaPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato() != null)
					   deviceService.assignDeviceToPatient(device.getId(), newHidaPatient.getId(), Optional.ofNullable(device.getTermineAssegnazione()));
				}
			} else if (userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				LncPatient newLncPatient = lncPatientService.save((LncPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), newLncPatient.getId(), Optional.ofNullable(device.getTermineAssegnazione()));
				}
			} else if (userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				SalusPatient newSalusPatient = salusPatientService.save((SalusPatient) newPatient);
				for (Device device: patientDTO.getDevices()) {
					if (device.getSelezionato())
					   deviceService.assignDeviceToPatient(device.getId(), newSalusPatient.getId(), Optional.ofNullable(device.getTermineAssegnazione()));
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

		return "redirect:/patients#AnagraficaPaziente";
	}
	
	
	@PostMapping(path = "/anagrafica")
	public String updateUserRegistry(@ModelAttribute("patient") @Valid HomeCheckPatientUserDTO patientDTO,
			BindingResult result, Model model) throws PSEServiceException {

		/*
		 * PSEUser existing = userService.findByEmail(patientDTO.getEmail()); if
		 * (existing != null) { result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 */

		if (result.hasErrors()) {
			return "scheda-paziente";
		}
		try {
			patientDTO.setDomain(userDomain);
            PSEUser existingUser = userService.getById(patientDTO.getId());
            existingUser.setIndirizzo(patientDTO.getAddress());
            existingUser.setCitta(patientDTO.getCity());
            existingUser.setProvincia(patientDTO.getProvince());
            existingUser.setStato(patientDTO.getCountry());
            existingUser.setTelefonoFisso(patientDTO.getFixedPhone());
            existingUser.setTelefonoCellulare(patientDTO.getCellPhone());
            userService.update(existingUser, false);
        
			HomeCheckPatient existingPatient;
			if(userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				existingPatient = aslVtPatientService.getById(patientDTO.getId());
			}
			else if(userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				existingPatient = hidaPatientService.getById(patientDTO.getId());				
			}
			else if(userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				existingPatient = lncPatientService.getById(patientDTO.getId());			
			}
			else if(userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				existingPatient = salusPatientService.getById(patientDTO.getId());			
			}
			else {
				throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {userDomain});
			}
			existingPatient.setIndirizzo(patientDTO.getAddress());
			existingPatient.setCitta(patientDTO.getCity());
			existingPatient.setProvincia(patientDTO.getProvince());
			existingPatient.setStato(patientDTO.getCountry());
			existingPatient.setTelefonoFisso(patientDTO.getFixedPhone());
			existingPatient.setTelefonoCellulare(patientDTO.getCellPhone());
			existingPatient.setNomeMedicoCurante(patientDTO.getClinicianFullName());
			existingPatient.setHeight(patientDTO.getHeight());
			existingPatient.setWeight(patientDTO.getWeight());
			existingPatient.setIdOperatoreSanitarioAssegnato(patientDTO.getNurseId());
			
			if (userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				aslVtPatientService.update((AslVtPatient) existingPatient);				
			} else if (userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				hidaPatientService.update((HidaPatient) existingPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				lncPatientService.update((LncPatient) existingPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				salusPatientService.update((SalusPatient) existingPatient);
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

		return  "redirect:/patients/"+patientDTO.getId()+"#AnagraficaPaziente";
	}
	
	
	@PostMapping(path = "/dispositivi")
	public String updateUserDevices(@ModelAttribute("patient") HomeCheckPatientUserDTO patientDTO,
			BindingResult result, Model model) throws PSEServiceException {
		
		if (result.hasErrors()) {
			return "scheda-paziente";
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String idPatient = patientDTO.getId();
			List<Device> devices = deviceService.findByPatientId(idPatient);
			for (Device device : devices)  {
				deviceService.revokeDeviceAssignmentToPatient(device.getId(), idPatient);
		    }
			
			for (Device device: patientDTO.getDevices()) {
				if ( device.getSelezionato() == true) {
				   if(!device.getTermineAssegnazioneString().equals("")) {
				      LocalDate dateEnd = LocalDate.parse(device.getTermineAssegnazioneString(), formatter);
				      device.setTermineAssegnazione(dateEnd);
				   }
				   deviceService.assignDeviceToPatient(device.getId(), idPatient, Optional.ofNullable(device.getTermineAssegnazione()));
				}
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

		return  "redirect:/patients/"+patientDTO.getId()+"#DispositiviAssociati";
	}
	
	
	@PostMapping(path = "/patologie")
	public String updateUserPathologies(@ModelAttribute("patient") HomeCheckPatientUserDTO patientDTO,
			BindingResult result, Model model) throws PSEServiceException {

		/*
		 * PSEUser existing = userService.findByEmail(patientDTO.getEmail()); if
		 * (existing != null) { result.rejectValue("email", null,
		 * "There is already an account registered with that email"); }
		 */

		if (result.hasErrors()) {
			return "scheda-paziente";
		}
		try {
		  
			HomeCheckPatient existingPatient;
			if(userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				existingPatient = aslVtPatientService.getById(patientDTO.getId());
			}
			else if(userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				existingPatient = hidaPatientService.getById(patientDTO.getId());				
			}
			else if(userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				existingPatient = lncPatientService.getById(patientDTO.getId());			
			}
			else if(userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				existingPatient = salusPatientService.getById(patientDTO.getId());			
			}
			else {
				throw new InvalidParameterFormatException("domain.not.recognized", new Object[] {userDomain});
			}
			existingPatient.setPatologie(patientDTO.getPathologies());
			
			if (userDomain.equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
				aslVtPatientService.update((AslVtPatient) existingPatient);				
			} else if (userDomain.equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
				hidaPatientService.update((HidaPatient) existingPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
				lncPatientService.update((LncPatient) existingPatient);
			} else if (userDomain.equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
				salusPatientService.update((SalusPatient) existingPatient);
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

		return  "redirect:/patients/"+patientDTO.getId()+"#PatologiePaziente";
	}
	
	
	@PostMapping(path = "/sensori")
	public String updateUserSensors(@ModelAttribute("patient") HomeCheckPatientUserDTO patientDTO,
			BindingResult result, Model model) throws PSEServiceException {
		
		if (result.hasErrors()) {
			return "scheda-paziente";
		}
		try {
			List<SensorMeasurementTimeSchedule> schedules = new ArrayList<>();
			for (SensorMeasurementThresholdValueDTO sensorMeasurement: patientDTO.getSensorMeasurements() )  {
			    sensorThresholdValueService.setCustomThresholdValuesForPatientAndSensorType(patientDTO.getId(), sensorMeasurement.getSensorType(), sensorMeasurement.getSensorThresholdValue());
			    if (!sensorMeasurement.getOrarioMattina().equals("")) {
			    	SensorMeasurementTimeSchedule scheduleMattina = new SensorMeasurementTimeSchedule(null, patientDTO.getId(), sensorMeasurement.getSensorType(), "Mattina", sensorMeasurement.getOrarioMattina());
			    	schedules.add(scheduleMattina);
			    }
			    if (!sensorMeasurement.getOrarioPomeriggio().equals(""))  {
				    SensorMeasurementTimeSchedule schedulePomeriggio = new SensorMeasurementTimeSchedule(null, patientDTO.getId(), sensorMeasurement.getSensorType(), "Pomeriggio", sensorMeasurement.getOrarioPomeriggio());
				    schedules.add(schedulePomeriggio);
			    }
				if (!sensorMeasurement.getOrarioPomeriggio().equals(""))  {
					SensorMeasurementTimeSchedule scheduleSera = new SensorMeasurementTimeSchedule(null, patientDTO.getId(), sensorMeasurement.getSensorType(), "Sera", sensorMeasurement.getOrarioSera());
					schedules.add(scheduleSera);
				}
				if (!sensorMeasurement.getOrarioPomeriggio().equals(""))  {
					SensorMeasurementTimeSchedule scheduleNotte = new SensorMeasurementTimeSchedule(null, patientDTO.getId(), sensorMeasurement.getSensorType(), "Notte", sensorMeasurement.getOrarioNotte());
					schedules.add(scheduleNotte);
				}
			    sensorMeasurementScheduleService.setSchedulesForPatientAndSensorType(patientDTO.getId(), sensorMeasurement.getSensorType(), schedules);
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

		return  "redirect:/patients/"+patientDTO.getId()+"#ConfigurazioneSensori";
	}
	
	
	

}
