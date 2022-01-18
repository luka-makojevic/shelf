package com.htec.shelffunction.repository;

import com.htec.shelffunction.entity.FunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {

    List<FunctionEntity> findAllByShelfIdIn(List<Long> shelfIds);

    List<FunctionEntity> findAllByIdIn(List<Long> functionIds);

    List<FunctionEntity> findAllByShelfIdAndEventId(Long shelfId, Long eventId);
}
