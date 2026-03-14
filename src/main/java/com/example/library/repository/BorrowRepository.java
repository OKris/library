package com.example.library.repository;

import com.example.library.entity.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {


    List<Borrow> findAllByPersonIdAndReturnedAtIsNull(Long personId);

    Borrow findByPersonIdAndReturnedAtIsNull(Long personId);

    boolean existsByBookIdAndPersonId(Long id, Long personId);

    List<Borrow> findAllByReturnedAtIsNull();

    Borrow findByPerson_IdAndBook_IdAndReturnedAtIsNull(Long personId, Long bookId);

    Page<Borrow> findAllByPersonId(Long personId, Pageable pageable);

    boolean existsByBookIdAndReturnedAtIsNull(Long bookId);

}
