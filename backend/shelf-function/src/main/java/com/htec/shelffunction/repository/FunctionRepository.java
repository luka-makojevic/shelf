package com.htec.shelffunction.repository;

import com.htec.shelffunction.entity.FunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {

    @Query("SELECT f " +
            "FROM FunctionEntity f JOIN ShelfEntity s ON (f.shelfId = s.id)" +
            "WHERE s.userId = :userId")
    List<FunctionEntity> findAllByUserId(@Param("userId") Long userId);
}
