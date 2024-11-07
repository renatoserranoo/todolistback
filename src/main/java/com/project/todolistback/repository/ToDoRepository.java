package com.project.todolistback.repository;

import com.project.todolistback.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query("SELECT COALESCE(MAX(t.ordem), 0) FROM ToDo t")
    Integer findMaxOrder();

    Optional<ToDo> findByNome(String nome);

    List<ToDo> findByOrdemBetween(Integer startOrder, Integer endOrder);
}
