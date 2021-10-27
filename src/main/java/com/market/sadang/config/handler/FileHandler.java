package com.market.sadang.config.handler;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.myFile.MyFileDto;
import com.market.sadang.repository.MyFileRepository;
import com.market.sadang.service.MyFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileHandler {

    private final MyFileService myFileService;
    private final MyFileRepository myFileRepository;

    public List<MyFile> parseFileInfo(Board board, List<MultipartFile> multipartFiles) throws Exception{
        //반환할 파일 리스트
        List<MyFile> fileList = new ArrayList<>();

        //전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)){
            //파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            //프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            //경로 구분자 File.separator 사용
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            //파일을 저장할 세부 경로 지정
            String path = "files" + File.separator + current_date;
            File file = new File(path);

            //디렉터리가 존재하지 않을 경우
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            for (MultipartFile multipartFile : multipartFiles){
                //파일의 확장자 추출
                String contentType = multipartFile.getContentType();
                String originalFileExtension;

                //확장자명이 존재하지 않을 경우 처리 안함
                if (ObjectUtils.isEmpty(contentType))
                    break;
                else{
                    if (contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else
                        break;
                }

                //파일명 중복을 피하기 위해 나노초 얻어 지정
                String newFileName = System.nanoTime() + originalFileExtension;

                MyFileDto myFileDto = MyFileDto.builder()
                        .originFileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + newFileName)
                        .fileSize(multipartFile.getSize())
                        .build();

                MyFile uploadFile = new MyFile(
                        myFileDto.getOriginFileName(),
                        myFileDto.getFilePath(),
                        myFileDto.getFileSize()
                );

                if (board.getId() != null)
                    uploadFile.setBoard(board);

                // 생성 후 리스트에 추가
                fileList.add(uploadFile);

                //업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + path + File.separator + newFileName);
                multipartFile.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileList;
    }
}
