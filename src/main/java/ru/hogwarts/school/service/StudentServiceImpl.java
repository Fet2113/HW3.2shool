package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> get(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student update(Long id, String name, int age) {
        Student studentForUpdate = studentRepository.findById(id).get();
        studentForUpdate.setName(name);
        studentForUpdate.setAge(age);
        return studentRepository.save(studentForUpdate);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);

    }

    @Override
    public List<Student> getByAge(int age) {
        return studentRepository.getAllByAge(age);
    }

    public List<Student> getByAgeBetween(int min, int max) {
        return studentRepository.findAllByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(Long id) {
        return studentRepository.findById(id).get().getFaculty();
    }

    public List<Student> getByFacultyId(Long facultyId) {

        return studentRepository.findByFacultyId(facultyId);
    }
    public Integer getCount() {
        return studentRepository.getCount();
    }

    public Double getAvgAge() {
        return studentRepository.getAvgAge();
    }

    public List<Student> getLastFive() {
        return studentRepository.getLastFive();
    }

}
