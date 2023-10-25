package ru.hogwarts.school.repository;

import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;


import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    public List<Faculty> getAllByColor(String color);
    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCase(String name);

}
