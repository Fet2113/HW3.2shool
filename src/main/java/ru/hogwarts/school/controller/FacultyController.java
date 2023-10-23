package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    public final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> get(@PathVariable("id") Long id) {
        Optional<Faculty> faculty = facultyService.get(id);
        return faculty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return facultyService.add(faculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> update(@PathVariable("id") Long id, @RequestBody Faculty faculty) {
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> delete(@PathVariable("id") Long id) {
        facultyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by.color")
    public ResponseEntity<List<Faculty>> getByColor(@RequestParam("by.color") String color) {
        return ResponseEntity.ok(facultyService.getByColor(color));
    }
    @GetMapping("/by-name-or-color")
    public Set<Faculty> getByColorOrNameIgnoreCase(@RequestParam String param) {
        return facultyService.getByColorOrNameIgnoreCase(param);
    }

    @GetMapping("/students-by-faculty-id")
    public List<Student> getStudentsByFacultyId(@RequestParam Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }


}
