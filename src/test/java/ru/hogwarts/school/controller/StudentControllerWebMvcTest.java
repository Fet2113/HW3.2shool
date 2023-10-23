package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    Student STUDENT_1 = new Student("Harry", 20);
    Student STUDENT_2 = new Student( "Bob", 25);

    @Test
    void get() throws Exception{
        long id = 1L;

        //Подготовка ожидаемого результат
        Student studentWithId = new Student("Harry", 20);
        studentWithId.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentWithId));

        //Тест
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/student")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(studentWithId.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(studentWithId.getAge()));

        verify(studentRepository).findById(id);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void add() throws Exception{
        Student studentForAdd = new Student("Harry", 20);

        String request = objectMapper.writeValueAsString(studentForAdd);

        Student studentWithId = new Student("Harry", 20);
        long id = 1l;
        studentWithId.setId(id);

        //Подготовка ожидаемого результат
        when(studentRepository.save(studentForAdd)).thenReturn(studentWithId);

        //Тест
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student") //send
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(studentForAdd.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(studentForAdd.getAge()));

        verify(studentRepository).save(studentForAdd);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void update() throws Exception {
        Student studentForUpdate = new Student("Harry", 20);

        String request = objectMapper.writeValueAsString(studentForUpdate);

        Student studentWithId = new Student("Harry", 20);
        long id = 1L;

        //Подготовка ожидаемого результат
        studentWithId.setId(id);
        studentWithId.setName("Draco");
        studentWithId.setAge(23);
        when(studentRepository.save(any(Student.class))).thenReturn(studentWithId);

        //Тест
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(studentForUpdate.getName()))
                .andExpect(jsonPath("$.color").value(studentForUpdate.getAge()));
    }

    @Test
    void delete() throws Exception {
        long id = 1L;

        //Подготовка ожидаемого результат
        Student studentWithId = new Student("Harry", 20);
        studentWithId.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentWithId));
        doNothing().when(studentRepository).deleteById(id);

        //Тест
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/student")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(studentWithId.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(studentWithId.getAge()));

        verify(studentRepository).findById(id);
        verify(studentRepository).deleteById(id);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void getByAge() throws Exception {
        when(studentRepository.getAllByAge(20)).thenReturn(List.of(STUDENT_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by.age/?age=20")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(20));

    }

    @Test
    void getByAgeBetween() throws Exception {
        when(studentRepository.findAllByAgeBetween(10, 500)).thenReturn(List.of(STUDENT_1, STUDENT_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/by-age-between?min=10&max=500")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getFacultyByStudentId() throws Exception {
        List<Student> STUDENTLIST = new ArrayList<>(List.of(STUDENT_1,STUDENT_2));
        when(studentRepository.getFacultyByStudentId(1L)).thenReturn((Faculty) STUDENTLIST);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/get-student-by-faculty-id?id=3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(20));
    }
}