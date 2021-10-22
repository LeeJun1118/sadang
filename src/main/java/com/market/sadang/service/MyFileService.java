package com.market.sadang.service;

import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.MyFileDto;
import com.market.sadang.domain.dto.MyFileResponseDto;
import com.market.sadang.repository.MyFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyFileService {

    private final MyFileRepository fileRepository;

    @Transactional
    public List<MyFileResponseDto> findAllByBoard(Long boardId) {
        List<MyFile> myFiles = fileRepository.findAllByBoardId(boardId);

        return myFiles.stream()
                .map(MyFileResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    public MyFileDto findByFileId(Long id) {
        MyFile myFile = fileRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
        MyFileDto myFileDto = MyFileDto.builder()
                .originFileName(myFile.getOriginFileName())
                .filePath(myFile.getFilePath())
                .fileSize(myFile.getFileSize())
                .build();
        return myFileDto;
    }
}
