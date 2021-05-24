package it.ecubit.homecheck.web.dto;

import java.util.List;

import it.ecubit.pse.api.dtos.SensorThresholdValueDTO;
import it.ecubit.pse.mongo.entities.SensorMeasurementTimeSchedule;

public class SensorMeasurementThresholdValue {
	
	List<SensorMeasurementTimeSchedule> schedules;
	List<SensorThresholdValueDTO> sensorThresholdValue;
	
	
	public SensorMeasurementThresholdValue(List<SensorThresholdValueDTO> sensorThresholdValue,
			List<SensorMeasurementTimeSchedule> schedules) {
		 this.sensorThresholdValue = sensorThresholdValue;
		 this.schedules = schedules;
	}
	public List<SensorMeasurementTimeSchedule> getSchedules() {
		return schedules;
	}
	public void setSchedules(List<SensorMeasurementTimeSchedule> schedules) {
		this.schedules = schedules;
	}
	public List<SensorThresholdValueDTO> getSensorThresholdValue() {
		return sensorThresholdValue;
	}
	public void setSensorThresholdValue(List<SensorThresholdValueDTO> sensorThresholdValue) {
		this.sensorThresholdValue = sensorThresholdValue;
	}
	
	

}
