package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentServiceImpl studentServiceimpl;



    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository, StudentServiceImpl studentServiceimpl) {
        this.facultyRepository = facultyRepository;
        this.studentServiceimpl = studentServiceimpl;
    }
    public Set<Faculty> getByColorOrNameIgnoreCase(String param) {
        Set<Faculty> result = new HashSet<>();
        result.addAll(facultyRepository.findByColorIgnoreCase(param));
        result.addAll(facultyRepository.findByNameIgnoreCase(param));
        return result;
    }

    public List<Student> getStudentsByFacultyId(Long id) {
        return studentServiceimpl.getByFacultyId(id);
    }


    @Override
    public Faculty add(Faculty faculty) {
        return facultyRepository.save(faculty);

    }

    @Override
    public Optional<Faculty> get(Long id) {
        return facultyRepository.findById(id);

    }


    @Override
    public Faculty update(Long id, String name, String color) {
        Faculty facultyForUpdate = facultyRepository.findById(id).get();
        facultyForUpdate.setName(name);
        facultyForUpdate.setColor(color);
        return facultyRepository.save(facultyForUpdate);

    }

    @Override
    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Faculty> getByColor(String color) {
        return facultyRepository.getAllByColor(color);

    }

    @Override
    public List<Student> getByFacultyId(Long id) {
        return null;
    }
}

