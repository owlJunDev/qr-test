package com.example.qr.Repository;

import org.springframework.data.repository.CrudRepository;

import com.example.qr.Entity.Cell;
import java.util.List;

public interface CellRepository extends CrudRepository<Cell, Integer> {
    List<Cell> findByNameIn(Iterable<String> name);
    List<Cell> findByFormulaLike(String name);
}