package com.classroom.manager.classroom.application;

import com.classroom.manager.classroom.config.ClassroomProperties;
import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.repository.ClassroomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomInitializer {

    private final ClassroomRepository classroomRepository;
    private final ClassroomProperties classroomProperties;

    @PostConstruct
    public void init(){
        classroomProperties.getCode().forEach(classCode -> {
            if (!classroomRepository.existsByRoomCode((classCode))) {
                classroomRepository.save(new Classroom(classCode));
            }
        });
    }
}
