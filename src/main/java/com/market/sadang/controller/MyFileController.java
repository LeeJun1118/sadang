package com.market.sadang.controller;

import com.market.sadang.domain.dto.MyFileDto;
import com.market.sadang.service.MyFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@RequiredArgsConstructor
@RestController
public class MyFileController {

    private final MyFileService myFileService;

    private static final int Thumbnail_Width = 240;
    private static final int Thumbnail_Height = 240;
    private static final int Image_Width = 800;
    private static final int Image_Height = 600;

    // 처음 전송되는 리소스의 도메인과 다른 도메인으로부터 리소스가 요청될 경우 해당 리소스는
    // cross-origin HTTP 요청이 된다.
    @CrossOrigin
    //전체 조회 : 게시글 리스트에 각각의 썸네일 이미지를 뿌려줌
    @GetMapping(value = "/thumbnail/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) throws IOException {

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path;

        // 전달되어 온 이미지가 기본 썸네일이 아닐 경우
        if (id != 0) {
            MyFileDto myFileDto = myFileService.findByFileId(id);
            path = myFileDto.getFilePath();
        }
        // 기본 썸네일일 경우
        else {
            path = "files" + File.separator + "thumbnail" + File.separator + "thumbnail.png";
        }

        BufferedImage resizeThumbnail = ImageIO.read(new File(absolutePath + path));
        byte[] imageByteArray = myFileService.resizeImage(resizeThumbnail,Thumbnail_Width,Thumbnail_Height);

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }

    // 처음 전송되는 리소스의 도메인과 다른 도메인으로부터 리소스가 요청될 경우 해당 리소스는
    // cross-origin HTTP 요청이 된다.
    @CrossOrigin
    //하나의 게시글이 가지고 있는 파일 리스트
    @GetMapping(value = "/images/{id}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImageList(@PathVariable Long id) throws IOException {

        MyFileDto myFileDto = myFileService.findByFileId(id);

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String path = myFileDto.getFilePath();

//        이미지 크기 조절
//        BufferedImage resizeImage = ImageIO.read(new File(absolutePath + path));
//        byte[] imageByteArray = myFileService.resizeImage(resizeImage,Image_Width,Image_Height);

        InputStream imageStream = new FileInputStream(absolutePath + path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
    }
}
