package it.ecubit.homecheck.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import it.ecubit.pse.mongo.entities.Patient.Sex;

public class AslVtPatientUserDTO  {

	private static final long serialVersionUID = 5772100208566866788L;




	private String id;


	private String alias;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String surname;

    @NotNull
	private Sex sex;
	

	private LocalDate birthDate;
	

	private String clinicianId;
	

	private String email;


	private float weight; // in Kg


	private float height; // in cm


	private String clinicianFullName;


	private String organization;
	

	private String password;
	

	private LocalDateTime creationDate;
	

	private LocalDateTime lastModifiedDate;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public Sex getSex() {
		return sex;
	}


	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public LocalDate getBirthDate() {
		return birthDate;
	}

    
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}


	public String getClinicianId() {
		return clinicianId;
	}


	public void setClinicianId(String clinicianId) {
		this.clinicianId = clinicianId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public String getClinicianFullName() {
		return clinicianFullName;
	}


	public void setClinicianFullName(String clinicianFullName) {
		this.clinicianFullName = clinicianFullName;
	}


	public String getOrganization() {
		return organization;
	}


	public void setOrganization(String organization) {
		this.organization = organization;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public LocalDateTime getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}


	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}

