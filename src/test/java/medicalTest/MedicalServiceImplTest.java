package medicalTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    void testCheckBloodPressure() {

        PatientInfo patientInfo = new PatientInfo("id-0001","Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.0"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(120, 100));

        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }

    @Test
    void testCheckTemperature() {

        PatientInfo patientInfo = new PatientInfo("id-0001","Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.0"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("34"));

        Mockito.verify(alertService).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }

    @Test
    void testNotMessage() {

        PatientInfo patientInfo = new PatientInfo("id-0001","Иван", "Петров",
                LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.0"), new BloodPressure(120, 80)));

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
        medicalService.checkBloodPressure(patientInfo.getId(), new BloodPressure(120, 80));
        medicalService.checkTemperature(patientInfo.getId(), new BigDecimal("36"));

        Mockito.verify(alertService, Mockito.times(0)).send(message);
    }
}
