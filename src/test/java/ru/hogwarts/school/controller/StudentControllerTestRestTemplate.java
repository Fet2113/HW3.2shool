package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Student;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    void get() throws Exception {

        Student studentForAdd = new Student("Harry", 20);

        //Подготовка ожидаемого результата

        Student expectedStudent = new Student("Harry", 20);

        //Тест
        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForAdd, Student.class);
        Student actualStudent = this.restTemplate.getForObject("http://localhost:" + port + "/student" + "?id=" + postedStudent.getId(), Student.class);
        assertEquals(postedStudent.getName(), actualStudent.getName());
        assertEquals(postedStudent.getAge(), actualStudent.getAge());
    }


    @Test
    void add() {

        Student studentForAdd = new Student("Harry", 20);

        //Подготовка ожидаемого результата

        Student expectedStudent = new Student("Harry", 20);

        //Тест
        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForAdd, Student.class);
        assertThat(postedStudent).isNotNull();
        assertEquals(expectedStudent.getName(), postedStudent.getName());
        assertEquals(expectedStudent.getAge(), postedStudent.getAge());
    }

    @Test
    void update() {
        Student studentForUpdate = new Student("Harry", 20);

        //Подготовка ожидаемого результата

        Student expectedStudent = new Student("Bob", 21);

        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForUpdate, Student.class);
        Student actualStudent = this.restTemplate.getForObject("http://localhost:" + port + "/student" + "?id=" + postedStudent.getId(), Student.class);
        assertThat(actualStudent).isNotNull();
        assertEquals(expectedStudent.getName(), postedStudent.getName());
        assertEquals(expectedStudent.getAge(), postedStudent.getAge());
    }

    @Test
    void delete() {

        Student studentForDelete = new Student("Harry", 20);

        //Тест
        Student postedStudent = this.restTemplate.postForObject("http://localhost:" + port + "/student", studentForDelete, Student.class);
        this.restTemplate.delete("http://localhost:" + port + "/student" + "?id=" + postedStudent.getId());

        Optional<Student> studentOpt = this.restTemplate.getForObject("http://localhost:" + port + "/student" + "?id=" + postedStudent.getId(), Optional.class);
        assertTrue(studentOpt.isEmpty());
    }

    @Test
    void getByAge() throws Exception {

        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/student + ?age=", String.class),
                "[{\"id\":4,\"name\":\"Джеймс Сириус Поттер\",\"age\":3,\"faculty\":{\"id\":2,\"name\":\"Слизерин\",\"color\":\"Зеленый\",\"studentList\":[]}}]");
    }

    @Test
    void getByAgeBetween() throws Exception {
        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/student/by-age-between", String.class),
                "[{\"id\":3,\"name\":\"Фред Уизли\",\"age\":12,\"faculty\":{\"id\":1,\"name\":\"Гриффиндор\",\"color\":\"Красный\",\"studentList\":[]}}]");
    }


    @Test
    void getFacultyByStudentId() throws Exception {
        assertEquals(this.restTemplate.getForObject("http://localhost:" + port + "/student/faculty-by-student-id?id=1", String.class),
                "{\"id\":1,\"name\":\"Гриффиндор\",\"color\":\"Красный\",\"studentList\":[]}");
    }
}