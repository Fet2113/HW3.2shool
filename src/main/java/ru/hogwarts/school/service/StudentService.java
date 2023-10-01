package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student add(Student student);
    Optional<Student> get(Long id);
    Student update(Long id, String name, int age);
    void delete(Long id);
    public List<Student> getByAge(int age);
}
