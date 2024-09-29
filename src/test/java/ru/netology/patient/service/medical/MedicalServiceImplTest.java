package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.never;

class MedicalServiceImplTest {
    @Test
    void checkBloodPressure() {
        PatientInfoRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, alertService);
        Mockito.when(patientInfoFileRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure("1", currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(String.format("Warning, patient with id: null, need help"), argumentCaptor.getValue());
    }

    @Test
    void checkTemperature() {
        PatientInfoRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, alertService);
        Mockito.when(patientInfoFileRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        BigDecimal currentTemperature = new BigDecimal("35.0");
        medicalService.checkTemperature("1", currentTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(String.format("Warning, patient with id: null, need help"), argumentCaptor.getValue());
    }

    @Test
    void checkNormal() {
        PatientInfoRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepository, alertService);
        Mockito.when(patientInfoFileRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure("1", currentPressure);
        BigDecimal currentTemperature = new BigDecimal("36.65");
        medicalService.checkTemperature("1", currentTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService, never()).send(argumentCaptor.capture());
    }
}