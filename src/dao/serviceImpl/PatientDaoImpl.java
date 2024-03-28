package dao.serviceImpl;

import dao.GenericDao;
import dao.PatientDao;
import database.Database;
import exception.StackOverflowException;
import model.Hospital;
import model.Patient;

import java.util.*;

public class PatientDaoImpl implements PatientDao, GenericDao<Patient> {
    @Override
    public String add(Long hospitalId, Patient patient) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), hospitalId)) {
                        hospital.setPatient(patient);
                        return "Пациент успешно добавлен!";
                    }
                }
                throw new StackOverflowException("Больницы с таким id не существует!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void removeById(Long id) {
        boolean isRemoved = false;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    isRemoved = hospital.getPatients().removeIf(x -> x.getId().equals(id));
                }
                if (isRemoved) {
                    System.out.println("Пациент успешно удалён!");
                } else {
                    throw new StackOverflowException("Больницы с таким id не существует!");
                }
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String updateById(Long id, Patient patient) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (int i = 0; i < hospital.getPatients().size(); i++) {
                        if (Objects.equals(hospital.getPatients().get(i).getId(), id)) {
                            hospital.getPatients().set(i, patient);
                            return "Пациент успешно обновлен!";
                        }
                    }
                }
                throw new StackOverflowException("Больницы с таким id не существует!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String addPatientsToHospital(Long id, List<Patient> patients) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), id)) {
                        hospital.getPatients().addAll(patients);
                        return "Пациенты успешно добавлены!";
                    }
                }
                throw new StackOverflowException("Больницы с таким id не существует!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public Patient getPatientById(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Patient patient : hospital.getPatients()) {
                        if (Objects.equals(patient.getId(), id)) {
                            return patient;
                        }
                    }
                }
                throw new StackOverflowException("Пациента с таким id не существует!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Map<Integer, Patient> getPatientByAge(int age) {
        Map<Integer, Patient> patientsByAge = new HashMap<>();
        boolean isPatientsExists = false;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Patient patient : hospital.getPatients()) {
                        if (patient.getAge() == age) {
                            patientsByAge.put(patient.getId().intValue(), patient);
                            isPatientsExists = true;
                        }
                    }
                }
                if (!isPatientsExists) {
                    throw new StackOverflowException("Пациенты с таким возрастом не существуют!");
                }
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return patientsByAge;
    }

    @Override
    public List<Patient> sortPatientsByAge(String ascOrDesc) {
        List<Patient> sortedPatients = new ArrayList<>();
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    sortedPatients.addAll(hospital.getPatients());
                }
                Comparator<Patient> ageComparator = Comparator.comparing(Patient::getAge);

                if (ascOrDesc.equalsIgnoreCase("desc")) {
                    ageComparator = ageComparator.reversed();
                }

                sortedPatients.sort(ageComparator);
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return sortedPatients;
    }
}
