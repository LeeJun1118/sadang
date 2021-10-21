package com.market.sadang.repository;


import com.market.sadang.domain.Board;
import com.market.sadang.domain.MyFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyFileRepository extends JpaRepository<MyFile,Long> {
    List<MyFile> findAllByBoard(Board board);
}
