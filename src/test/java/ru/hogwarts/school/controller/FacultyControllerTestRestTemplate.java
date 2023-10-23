package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }


    @Test
    void get() throws Exception {

        Faculty facultyForAdd = new Faculty("Grifferdor", "green");

        //Подготовка ожидаемого результата

        Faculty expectedFaculty = new Faculty("Grifferdor", "green");

        //Тест
        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/student", facultyForAdd, Faculty.class);
        Faculty actualFaculty = this.restTemplate.getForObject("http://localhost:" + port + "/student" + "?id=" + postedFaculty.getId(), Faculty.class);
        assertEquals(postedFaculty.getName(), actualFaculty.getName());
        assertEquals(postedFaculty.getColor(), actualFaculty.getColor());
    }

    @Test
    void add() {
        Faculty facultyForAdd = new Faculty("Grifferdor", "green");

        //Подготовка ожидаемого результата

        Faculty expectedFaculty = new Faculty("Grifferdor", "green");

        //Тест
        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForAdd, Faculty.class);
        assertThat(postedFaculty).isNotNull();
        assertEquals(expectedFaculty.getName(), postedFaculty.getName());
        assertEquals(expectedFaculty.getColor(), postedFaculty.getColor());

    }

    @Test
    void update() {
        Faculty facultyForUpdate = new Faculty("Grifferdor", "green");

        //Подготовка ожидаемого результата

        Faculty expectedSFaculty = new Faculty("Slyzerin", "red");

        Faculty postedSFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForUpdate, Faculty.class);
        Faculty actualFaculty = this.restTemplate.getForObject("http://localhost:" + port + "/faculty" + "?id=" + postedSFaculty.getId(), Faculty.class);
        assertThat(actualFaculty).isNotNull();
        assertEquals(expectedSFaculty.getName(), postedSFaculty.getName());
        assertEquals(expectedSFaculty.getColor(), postedSFaculty.getColor());
    }

    @Test
    void delete() {
        Faculty facultyForDelete = new Faculty("Grifferdor", "green");

        //Тест
        Faculty postedFaculty = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", facultyForDelete, Faculty.class);
        this.restTemplate.delete("http://localhost:" + port + "/faculty" + "?id=" + postedFaculty.getId());

        Optional<Faculty> facultyOpt = this.restTemplate.getForObject("http://localhost:" + port + "/faculty" + "?id=" + postedFaculty.getId(), Optional.class);
        assertTrue(facultyOpt.isEmpty());
    }

    @Test
    void getByColor() throws Exception {
        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/by.color + ?str=красный", String.class),
                "[{\"id\":1,\"name\":\"Гриффиндор\",\"color\":\"Красный\",\"studentList\":[]}]");
    }

    @Test
    void getByColorOrNameIgnoreCase() throws Exception {
        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/by-name-or-color?str=красный", String.class),
                "[{\"id\":1,\"name\":\"Гриффиндор\",\"color\":\"Красный\",\"studentList\":[]}]");
    }

    @Test
    void getStudentsByFacultyId() throws Exception {
        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/students-by-faculty-id?id=2", String.class),
                "[{\"id\":4,\"name\":\"Джеймс Сириус Поттер\",\"age\":3,\"faculty\":{\"id\":2,\"name\":\"Слизерин\",\"color\":\"Зеленый\",\"studentList\":[]}}]");
    }
}