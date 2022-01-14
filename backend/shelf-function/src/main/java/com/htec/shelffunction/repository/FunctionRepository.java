package com.htec.shelffunction.repository;

import com.htec.shelffunction.entity.FunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {

    List<FunctionEntity> findAllByShelfIdIn(List<Long> shelfIds);
}
