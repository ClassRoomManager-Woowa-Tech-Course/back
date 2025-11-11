package com.classroom.manager.classroom.domain;

import com.classroom.manager.classroom.presentation.dto.ClassroomResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomCode;

    public Classroom(String classCode) {
        this.roomCode = classCode;
    }

    public String roomCode() {
        return roomCode;
    }

    public ClassroomResponse to() {
        return ClassroomResponse.builder()
                .roomCode(this.roomCode)
                .build();
    }
}
