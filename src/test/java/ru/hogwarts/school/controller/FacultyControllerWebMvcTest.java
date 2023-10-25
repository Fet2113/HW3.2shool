package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;


    Faculty FACULTY_1 = new Faculty( "Griffendor", "green");
    Faculty FACULTY_2 = new Faculty("Slizerin", "red");


    @Test
    void get() throws Exception{
        long id = 1l;

        //Подготовка ожидаемого результат
        Faculty facultyWithId = new Faculty("Griffendor", "green");
        facultyWithId.setId(id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(facultyWithId));

        //Тест
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/faculty")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(facultyWithId.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(facultyWithId.getColor()));

        verify(facultyRepository).findById(id);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    void add() throws Exception{
        Faculty facultyForAdd = new Faculty("Griffendor", "green");

        String request = objectMapper.writeValueAsString(facultyForAdd);

        Faculty facultyWithId = new Faculty("Griffendor", "green");
        long id = 1l;
        facultyWithId.setId(id);

        //Подготовка ожидаемого результат
        when(facultyRepository.save(facultyForAdd)).thenReturn(facultyWithId);

        //Тест
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty") //send
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //receive
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(facultyForAdd.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(facultyForAdd.getColor()));

        verify(facultyRepository).save(facultyForAdd);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    void update() throws Exception {
        Faculty facultyForUpdate = new Faculty("Griffendor", "green");

        String request = objectMapper.writeValueAsString(facultyForUpdate);

        Faculty facultyWithId = new Faculty("Griffendor", "green");
        long id = 1L;
        //Подготовка ожидаемого результат
        facultyWithId.setId(id);
        facultyWithId.setName("Puffendui");
        facultyWithId.setColor("grey");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyWithId);

        //Тест
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(facultyForUpdate.getName()))
                .andExpect(jsonPath("$.color").value(facultyForUpdate.getColor()));
    }

    @Test
    void delete() throws Exception {
        long id = 1l;

        //Подготовка ожидаемого результат
        Faculty facultyWithId = new Faculty("Griffendor", "green");
        facultyWithId.setId(id);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(facultyWithId));
        doNothing().when(facultyRepository).deleteById(id);

        //НNtcn
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/faculty")
                                .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(facultyWithId.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(facultyWithId.getColor()));

        verify(facultyRepository).findById(id);
        verify(facultyRepository).deleteById(id);
        verifyNoMoreInteractions(facultyRepository);
    }

    @Test
    void getByColor() throws Exception {
        when(facultyRepository.getAllByColor("green")).thenReturn(List.of(FACULTY_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by.color?color=green")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Griffendor"))
                .andExpect(jsonPath("$.color").value("green"));
    }

    @Test
    void getByColorOrNameIgnoreCase() throws Exception {
        when(facultyRepository.findByColorIgnoreCase("red")).thenReturn(List.of(FACULTY_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/by-name-or-color?str=Slizerin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("Slizerin"))
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void getStudentsByFacultyId() throws Exception {
        List<Faculty> FACULTYLIST = new ArrayList<>(List.of(FACULTY_1,FACULTY_2));


        when(facultyRepository.findByFacultyId(1L)).thenReturn((Student) FACULTYLIST);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/students-by-faculty-id?id=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Griffendor"))
                .andExpect(jsonPath("$.color").value("green"));
    }
}
