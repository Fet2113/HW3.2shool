package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Optional<Faculty> get(Long id);

    Faculty update(Long id, String name, String color);

    void delete(Long id);

    public List<Faculty> getByColor(String color);

}
