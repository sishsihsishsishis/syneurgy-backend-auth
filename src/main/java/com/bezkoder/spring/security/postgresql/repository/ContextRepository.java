package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.Context;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ContextRepository extends PagingAndSortingRepository<Context, Long> {
    List<Context> findAllByOrderByIdAsc();
    @Override
    Optional<Context> findById(Long aLong);

    Page<Context> findAll(Pageable pageable);
}
