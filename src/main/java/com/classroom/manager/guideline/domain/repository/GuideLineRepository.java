package com.classroom.manager.guideline.domain.repository;

import com.classroom.manager.guideline.domain.GuideLine;
import com.classroom.manager.guideline.domain.exception.NotFoundGuideLineException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideLineRepository extends JpaRepository<GuideLine, Long> {

    default GuideLine getGuideLineById(Long guideLineId) {
        return findById(guideLineId).orElseThrow(NotFoundGuideLineException::new);
    }
}
