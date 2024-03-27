package dao.serviceImpl;

import dao.DoctorDao;
import dao.GenericDao;
import database.Database;
import exception.StackOverflowException;
import model.Department;
import model.Doctor;
import model.Hospital;

import java.util.*;

public class DoctorDaoImpl implements DoctorDao, GenericDao<Doctor> {
    @Override
    public String add(Long hospitalId, Doctor doctor) {
        boolean isDoctorExists = false;
        int hospitalIndex = 0;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (int i = 0; i < Database.hospitals.size(); i++) {
                    if (Objects.equals(Database.hospitals.get(i).getId(), hospitalId)) {
                        hospitalIndex = i;
                        for (Doctor hospitalDoctor : Database.hospitals.get(i).getDoctors()) {
                            if (hospitalDoctor.getFirstName().equalsIgnoreCase(doctor.getFirstName()) && hospitalDoctor.getLastName().equalsIgnoreCase(doctor.getLastName())) {
                                isDoctorExists = true;
                                break;
                            }
                        }
                    }
                }
                if (!isDoctorExists) {
                    Database.hospitals.get(hospitalIndex).setDoctor(doctor);
                    return "Доктор успешно добавлен!";
                } else {
                    throw new StackOverflowException("Доктор с таким именем уже существует!");
                }
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
        boolean isDoctorExists = false;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Doctor doctor : hospital.getDoctors()) {
                        if (Objects.equals(doctor.getId(), id)) {
                            hospital.removeDoctor(doctor);
                            isDoctorExists = true;
                        }
                    }
                    for (Department department : hospital.getDepartments()) {
                        for (Doctor doctor : department.getDoctors()) {
                            if (Objects.equals(doctor.getId(), id)) {
                                department.removeDoctor(doctor);
                            }
                        }
                    }
                }
                if (!isDoctorExists) {
                    throw new StackOverflowException("Доктор с id-" + id + " не найден!");
                }
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String updateById(Long id, Doctor doctor) {
        boolean isDoctorExists = false;
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (int i = 0; i < hospital.getDoctors().size(); i++) {
                        if (Objects.equals(hospital.getDoctors().get(i).getId(), id)) {
                            hospital.getDoctors().set(i, doctor);
                            isDoctorExists = true;
                        }
                    }
                    if (isDoctorExists) {
                        for (Department department : hospital.getDepartments()) {
                            for (int i = 0; i < department.getDoctors().size(); i++) {
                                if (Objects.equals(department.getDoctors().get(i).getId(), id)) {
                                    department.getDoctors().set(i, doctor);
                                    return "Doctor successfully updated";
                                }
                            }
                        }
                    }
                }
                throw new StackOverflowException("Доктор с id-" + id + " не найден!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public Doctor findDoctorById(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Doctor doctor : hospital.getDoctors()) {
                        if (Objects.equals(doctor.getId(), id)) {
                            return doctor;
                        }
                    }
                }
                throw new StackOverflowException("Доктор с id-" + id + " не найден!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String assignDoctorToDepartment(Long departmentId, List<Long> doctorsId) {
        List<Integer> anotherDoctors = new ArrayList<>();
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Department department : hospital.getDepartments()) {
                        if (department.getId().equals(departmentId)) {
                            for (Long doctorId : doctorsId) {
                                Doctor doctor = findDoctorById(doctorId);
                                if (doctor != null) {
                                    department.getDoctors().add(doctor);
                                } else {
                                    anotherDoctors.add(doctorId.intValue());
                                }
                            }
                            if (!anotherDoctors.isEmpty()) {
                                throw new StackOverflowException(anotherDoctors.size() == 1 ? "Доктор с идентификатором " + anotherDoctors.getFirst() + " не найден" : "Доктора с идентификатороми " + anotherDoctors + " не найдены");
                            }
                            return "Докторы успешно назначены в отделение с id: " + departmentId;
                        }
                    }
                }
                throw new StackOverflowException("Отделение с id: " + departmentId + " не найдено");
            } else {
                throw new StackOverflowException("На данный момент у вас нет ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Doctor> getAllDoctorsByHospitalId(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    if (Objects.equals(hospital.getId(), id)) {
                        return hospital.getDoctors();
                    }
                }
                throw new StackOverflowException("Больница с id-" + id + " не найден!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Doctor> getAllDoctorsByDepartmentId(Long id) {
        try {
            if (!Database.hospitals.isEmpty()) {
                for (Hospital hospital : Database.hospitals) {
                    for (Department department : hospital.getDepartments()) {
                        if (Objects.equals(department.getId(), id)) {
                            return department.getDoctors();
                        }
                    }
                }
                throw new StackOverflowException("Отделение с id-" + id + " не найден!");
            } else {
                throw new StackOverflowException("На данный момент у вас нету ни одной больницы!");
            }
        } catch (StackOverflowException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
