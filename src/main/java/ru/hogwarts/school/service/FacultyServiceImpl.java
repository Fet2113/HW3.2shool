package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long currentId = 0L;


    @Override
    public Faculty add(Faculty faculty) {
        Long id = ++currentId;
        faculty.setId(id);
        faculties.put(id, faculty);

        return faculties.get(id);
    }

    @Override
    public Faculty get(Long id) {
       return faculties.get(id);
    }

    @Override
    public Faculty update(Long id, Faculty faculty) {
        if (faculties.containsKey(id)) {
            Faculty facultyById = faculties.get(id);
            facultyById = faculties.get(id);
            facultyById.setName(faculty.getName());
            facultyById.setColor(faculty.getColor());
            return faculties.get(id);
        } else {
            return null;
        }

    }

    @Override
    public void delete(Long id) {
        faculties.remove(id);
    }

    @Override
    public List<Faculty> getByColor(String color) {
        return faculties.values()
                .stream()
                .filter(it -> it.getColor().equals(color))
                .collect(Collectors.toList());
    }
}

