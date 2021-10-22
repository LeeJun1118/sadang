package com.market.sadang.controller;

import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.MyFileDto;
import com.market.sadang.service.MyFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RequiredArgsConstructor
@RestController
public class MyFileController {

    private final MyFileService myFileService;

    // 처음 전송되는 리소스의 도메인과 다른 도메인으로부터 리소스가 요청될 경우 해당 리소스는
    // cross-origin HTTP 요청이 된다.
    @CrossOrigin
    @GetMapping(value = "/thumbnail/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) throws IOException {

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path;

        // 전달되어 온 이미지가 기본 썸네일이 아닐 경우
        if (id != 0){
            MyFileDto myFileDto = myFileService.findByFileId(id);
            path = myFileDto.getFilePath();
        }
        // 기본 썸네일일 경우
        else{
            path = "images" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
