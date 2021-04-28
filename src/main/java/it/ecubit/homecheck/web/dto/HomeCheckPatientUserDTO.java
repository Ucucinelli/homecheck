package it.ecubit.homecheck.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.ecubit.homecheck.constraint.FieldMatch;
import it.ecubit.pse.mongo.entities.AslVtPatient;
import it.ecubit.pse.mongo.entities.Domain;
import it.ecubit.pse.mongo.entities.HCPathology;
import it.ecubit.pse.mongo.entities.HidaPatient;
import it.ecubit.pse.mongo.entities.HomeCheckPatient;
import it.ecubit.pse.mongo.entities.LncPatient;
import it.ecubit.pse.mongo.entities.PSEUser;
import it.ecubit.pse.mongo.entities.Patient.Sex;
import it.ecubit.pse.mongo.entities.SalusPatient;
import it.ecubit.pse.mongo.interfaces.IndexedHomeCheckPatient;
import it.ecubit.pse.mongo.interfaces.IndexedPatient;
import it.ecubit.pse.mongo.interfaces.IndexedUser;
import it.ecubit.pse.mongo.json.converters.ByteArrayDeserializer;
import it.ecubit.pse.mongo.json.converters.ByteArraySerializer;
import it.ecubit.pse.mongo.json.converters.LocalDateDeserializer;
import it.ecubit.pse.mongo.json.converters.LocalDateSerializer;
import it.ecubit.pse.mongo.json.converters.LocalDateTimeDeserializer;
import it.ecubit.pse.mongo.json.converters.LocalDateTimeSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@ToString(includeFieldNames=true)
@JsonInclude(Include.NON_EMPTY)
@JsonRootName("paziente-homecheck")
public class HomeCheckPatientUserDTO implements IndexedHomeCheckPatient, IndexedUser {

	private static final long serialVersionUID = 5772100208566866788L;

	public HomeCheckPatientUserDTO(@NonNull HomeCheckPatient patient, @NonNull PSEUser user) {
		this.id = patient.getId();
		this.domain = patient.getDomain();
		this.alias = patient.getAlias();
		this.name = patient.getNome();
		this.surname = patient.getCognome();
		this.sex = patient.getSesso();
		this.birthDate = patient.getDataDiNascita();
		this.birthPlace = patient.getLuogoDiNascita();
		this.birthProvince = patient.getProvinciaDiNascita();
		this.clinicianId = patient.getIdMedico();
		this.clinicianFullName = patient.getNomeMedicoCurante();
		this.photo = patient.getFoto();
		this.weight = patient.getWeight();
		this.height = patient.getHeight();
		this.address = patient.getIndirizzo();
		this.city = patient.getCitta();
		this.province = patient.getProvincia();
		this.country = patient.getStato();
		this.fiscalCode = patient.getCodiceFiscale();
		this.nationality = patient.getCittadinanza();
		this.fixedPhone = patient.getTelefonoFisso();
		this.cellPhone = patient.getTelefonoCellulare();
		this.nurseId = patient.getIdOperatoreSanitarioAssegnato();
		this.pathologies = patient.getPatologie();
		this.organization = user.getOrganizzazione();
		this.email = patient.getEmail();
		this.password = user.getPassword();
		this.creationDate = user.getDataCreazione();
		this.lastModifiedDate = user.getDataUltimaModifica();
	}

	@JsonIgnore
	public HomeCheckPatient getPatient() {
		HomeCheckPatient patient = null;
		if(this.getDomain().equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			patient = new AslVtPatient();
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			patient = new HidaPatient();
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			patient = new LncPatient();
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			patient = new SalusPatient();
		}
		patient.setId(this.id);
		patient.setAlias(this.alias);
		patient.setNome(this.name);
		patient.setCognome(this.surname);
		patient.setSesso(this.sex);
		patient.setDataDiNascita(this.birthDate);
		patient.setLuogoDiNascita(this.birthPlace);
		patient.setProvinciaDiNascita(this.birthProvince);
		patient.setIdMedico(this.clinicianId);
		patient.setNomeMedicoCurante(this.clinicianFullName);
		patient.setFoto(this.photo);
		patient.setWeight(this.weight);
		patient.setHeight(this.height);
		patient.setIndirizzo(this.address);
		patient.setCitta(this.city);
		patient.setProvincia(this.province);
		patient.setStato(this.country);
		patient.setCodiceFiscale(this.fiscalCode);
		patient.setCittadinanza(this.nationality);
		patient.setTelefonoFisso(this.fixedPhone);
		patient.setTelefonoCellulare(this.cellPhone);
		patient.setIdOperatoreSanitarioAssegnato(this.nurseId);
		patient.setPatologie(this.pathologies);
		patient.setEmail(this.email);
		return patient;
	}

	@JsonIgnore
	public PSEUser getUser() {
		PSEUser user = new PSEUser();
		user.setId(this.id);
		user.setAlias(this.alias);
		user.setFoto(this.photo);
		user.setNome(this.name);
		user.setCognome(this.surname);
		if(this.sex == Sex.MALE)
			user.setSesso(PSEUser.Sex.MALE);
		else if(this.sex == Sex.FEMALE)
			user.setSesso(PSEUser.Sex.FEMALE);
		else
			user.setSesso(PSEUser.Sex.UNSPECIFIED);
		user.setDataDiNascita(this.birthDate);
		user.setLuogoDiNascita(this.birthPlace);
		user.setProvinciaDiNascita(this.birthProvince);
		user.setCittadinanza(this.nationality);
		user.setIndirizzo(this.address);
		user.setCitta(this.city);
		user.setProvincia(this.province);
		user.setStato(this.country);
		user.setCodiceFiscale(this.fiscalCode);
		user.setTelefonoFisso(this.fixedPhone);
		user.setTelefonoCellulare(this.cellPhone);
		user.setOrganizzazione(this.organization);
		user.setEmail(this.email);
		user.setPassword(this.password);
		if(this.getDomain().equalsIgnoreCase(Domain.ASL_VT_DOMAIN.getNome())) {
			user.setDomain(Domain.ASL_VT_DOMAIN);
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.HIDA_DOMAIN.getNome())) {
			user.setDomain(Domain.HIDA_DOMAIN);
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.LN_CONSULTING_DOMAIN.getNome())) {
			user.setDomain(Domain.LN_CONSULTING_DOMAIN);
		}
		else if(this.getDomain().equalsIgnoreCase(Domain.SALUS_DOMAIN.getNome())) {
			user.setDomain(Domain.SALUS_DOMAIN);
		}
		user.setDataCreazione(this.creationDate);
		if(user.getDataCreazione() == null) {
			user.setDataCreazione(LocalDateTime.now());
		}
		user.setDataUltimaModifica(this.lastModifiedDate);
		if(user.getDataUltimaModifica() == null) {
			user.setDataUltimaModifica(LocalDateTime.now());
		}
		return user;
	}

	@JsonProperty(IndexedPatient.ID_JSON_ATTR_NAME)
	private String id;

	@JsonProperty(IndexedHomeCheckPatient.DOMAIN_JSON_ATTR_NAME)
	private String domain;
	
	@JsonProperty(IndexedPatient.ALIAS_JSON_ATTR_NAME)
	private String alias;
	
	@NotEmpty(message="{name.not.empty}")
	@JsonProperty(IndexedPatient.NAME_JSON_ATTR_NAME)
	private String name;
	
	@NotEmpty(message="{surname.not.empty}")
	@JsonProperty(IndexedPatient.SURNAME_JSON_ATTR_NAME)
	private String surname;

	@NotNull(message="{sexField.not.empty}")
	@JsonProperty(IndexedPatient.SEX_JSON_ATTR_NAME)
	private Sex sex;
	
	@JsonProperty(IndexedPatient.BIRTH_DATE_JSON_ATTR_NAME)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate birthDate;
	
	@NotNull(message="{dateOfBirth.not.empty}")
	@JsonProperty(IndexedPatient.BIRTH_DATE_JSON_ATTR_NAME)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private String birthDateString;
	
	@JsonProperty(IndexedHomeCheckPatient.BIRTH_PLACE_JSON_ATTR_NAME)
	private String birthPlace;

	@JsonProperty(IndexedHomeCheckPatient.BIRTH_PROVINCE_JSON_ATTR_NAME)
	private String birthProvince;

	@JsonProperty(ID_CLINICIAN_JSON_ATTR_NAME)
	private String clinicianId;
	
	@JsonProperty(CLINICIAN_FULL_NAME_JSON_ATTR_NAME)
	private String clinicianFullName;

	@JsonProperty(IndexedHomeCheckPatient.PHOTO_JSON_ATTR_NAME)
	@JsonSerialize(using = ByteArraySerializer.class)
	@JsonDeserialize(using = ByteArrayDeserializer.class)
	private byte[] photo;

	@JsonProperty(WEIGHT_JSON_ATTR_NAME)
	@Min(1)
	private float weight; // in Kg

	@JsonProperty(HEIGHT_JSON_ATTR_NAME)
	@Min(1)
	private float height; // in cm

	@JsonProperty(IndexedHomeCheckPatient.ADDRESS_JSON_ATTR_NAME)
	private String address;

	@JsonProperty(IndexedHomeCheckPatient.CITY_JSON_ATTR_NAME)
	private String city;

	@JsonProperty(IndexedHomeCheckPatient.PROVINCE_JSON_ATTR_NAME)
	private String province;

	@JsonProperty(IndexedHomeCheckPatient.COUNTRY_JSON_ATTR_NAME)
	private String country;

	@JsonProperty(IndexedHomeCheckPatient.FISCAL_CODE_JSON_ATTR_NAME)
	private String fiscalCode;

	@JsonProperty(IndexedHomeCheckPatient.NATIONALITY_JSON_ATTR_NAME)
	private String nationality;

	@JsonProperty(IndexedHomeCheckPatient.FIXED_PHONE_JSON_ATTR_NAME)
	private String fixedPhone;

	@JsonProperty(IndexedHomeCheckPatient.CELL_PHONE_JSON_ATTR_NAME)
	private String cellPhone;

	@JsonProperty(ID_NURSE_JSON_ATTR_NAME)
	private String nurseId;

	@JsonProperty(HC_PATHOLOGIES_JSON_ATTR_NAME)
	private Set<HCPathology> pathologies = new HashSet<>();

	@JsonProperty(ORGANIZATION_JSON_ATTR_NAME)
	private String organization;
	
	@NotEmpty(message="user.email.not.empty}")
	@JsonProperty(IndexedPatient.EMAIL_FIELD_NAME)
	private String email;

	@NotEmpty(message="{user.password.not.empty}")
	@JsonProperty(PASSWORD_JSON_ATTR_NAME)
	private String password;
	
	@NotEmpty(message="{user.confirmPassword.not.empty}")
	@JsonProperty(PASSWORD_JSON_ATTR_NAME)
	private String confirmPassword;
	
	@JsonProperty(CREATION_DATE_JSON_ATTR_NAME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime creationDate;
	
	@JsonProperty(LAST_MODIFIED_DATE_JSON_ATTR_NAME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastModifiedDate;

	public void resetPathologies() {
		this.pathologies.clear();
	}

	public void addPathology(@NonNull HCPathology pathology) {
		this.pathologies.add(pathology);
	}

	public boolean removePathology(@NonNull HCPathology pathology) {
		return this.pathologies.remove(pathology);
	}

}
