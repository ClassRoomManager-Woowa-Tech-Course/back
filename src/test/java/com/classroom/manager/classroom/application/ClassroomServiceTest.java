package com.classroom.manager.classroom.application;

import com.classroom.manager.classroom.presentation.dto.ClassroomResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ClassroomServiceTest {

    @Autowired
    private ClassroomService classroomService;

    @Test
    void findAllClassroom() {
        List<ClassroomResponse> classrooms = classroomService.findClassrooms();
        assertNotNull(classrooms);
        assertThat(classrooms.size()).isEqualTo(7);
    }
}
