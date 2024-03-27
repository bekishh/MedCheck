package dao.serviceImpl;

import dao.HospitalDao;
import database.Database;
import exception.StackOverflowException;
import model.Hospital;
import model.Patient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HospitalDaoImpl implements HospitalDao {
    @Override
    public String addHospital(Hospital hospital) {
        Database.hospitals.add(hospital);
        return "Больница успешно добавлена!";
    }

    @Override
    public Hospital findHospitalById(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), id)) {
                        return hospital;
                    }
                }
                throw new StackOverflowException("Больница с id-" + id + " не найдена!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Hospital> getAllHospital() {
        try {
            if (!Database.hospitals.isEmpty()) {
                return Database.hospitals;
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return Database.hospitals;
    }

    @Override
    public List<Patient> getAllPatientFromHospital(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), id)) {
                        return hospital.getPatients();
                    }
                }
                throw new StackOverflowException("Больница с id-" + id + " не найдена!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String deleteHospitalById(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), id)) {
                        Database.hospitals.remove(hospital);
                        return "Больница с id-" + id + " успешно удалена!";
                    }
                }
                throw new StackOverflowException("Больница с id-" + id + " не найдена!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public Map<String, Hospital> getAllHospitalByAddress(String address) {
        Map<String, Hospital> hospitalsByAddress = new HashMap<>();
        boolean isHospitalExists = false;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (hospital.getAddress().equalsIgnoreCase(address)) {
                        hospitalsByAddress.put(hospital.getHospitalName(), hospital);
                        isHospitalExists = true;
                    }
                }
                if (!isHospitalExists) {
                    throw new StackOverflowException("Больница с адресом " + address + " не найдена!");
                }
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return hospitalsByAddress;
    }
}
