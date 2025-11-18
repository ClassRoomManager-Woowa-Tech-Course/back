package com.classroom.manager.classroom.domain.repository;

import com.classroom.manager.classroom.domain.Classroom;
import com.classroom.manager.classroom.domain.exception.NotFoundClassroomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    default Classroom getByRoomCode(String roomCode) {
        return findByRoomCode(roomCode).orElseThrow(NotFoundClassroomException::new);
    }

    Optional<Classroom> findByRoomCode(String roomCode);

    boolean existsByRoomCode(String roomCode);
}
