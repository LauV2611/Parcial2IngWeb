package com.example.parcial.Repositories;
import com.example.parcial.Entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, UUID> {

    Page<BookEntity> findAllByTitleContaining(String title, Pageable pageable);

    @Override
    Page<BookEntity> findAll(Pageable pageable);
}
