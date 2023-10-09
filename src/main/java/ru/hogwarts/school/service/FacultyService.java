package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Optional<Faculty> get(Long id);

    Faculty update(Long id, String name, String color);

    void delete(Long id);

    public List<Faculty> getByColor(String color);
    List<Student> getByFacultyId(Long id);
    public Set<Faculty> getByColorOrNameIgnoreCase(String param);
    public List<Student> getStudentsByFacultyId(Long id);

}
