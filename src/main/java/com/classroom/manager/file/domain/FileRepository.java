package com.classroom.manager.file.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByRelatedIdAndRelatedType(Long relatedId, FileRelatedType relatedType);

    List<File> findAllByRelatedIdInAndRelatedType(List<Long> relatedId, FileRelatedType relatedType);
}
