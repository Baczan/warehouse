package com.example.resource.repositories;

import com.example.resource.entities.Return;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<Return,Integer> {

    @Query( "select r from Return r where r.id in :ids" )
    Page<Return> findByIds(@Param("ids") List<Integer> postIdsList, Pageable pageRequest);

}
