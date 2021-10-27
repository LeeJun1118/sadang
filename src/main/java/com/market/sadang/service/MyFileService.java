package com.market.sadang.service;

import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.myFile.MyFileDto;
import com.market.sadang.domain.dto.myFile.MyFileResponseDto;
import com.market.sadang.repository.MyFileRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    //썸네일 크기 조절
    public byte[] resizeImage(BufferedImage resizeImg, int Image_Width, int Image_Height) throws IOException {

        //원본 크기
        int originWidth = resizeImg.getWidth();
        int originHeight = resizeImg.getHeight();

        //원본 너비를 기준으로 썸네일의 비율로 높이 계산
        int newWidth = originWidth;
        int newHeight = (originWidth * Image_Height) / Image_Width;

        //계산된 높이가 원본보다 높으면 안되므로
        //원본 높이를 기준으로 썸네일의 비율로 너비 계산
        if (newHeight > originHeight) {
            newWidth = (originHeight * Image_Width) / Image_Height;
            newHeight = originHeight;
        }
        BufferedImage cropImg =
                Scalr.crop(resizeImg, (originWidth - newWidth) / 2, (originHeight - newHeight) / 2, newWidth, newHeight);

        BufferedImage resultImg = Scalr.resize(cropImg, Image_Width, Image_Height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImg, "png", outputStream);

        return outputStream.toByteArray();
    }

    /*public byte[] resizeImg(InputStream imageStream) throws IOException {
        Image image = ImageIO.read(imageStream);

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        int resultWidth, resultHeight;

        double ratio;

        //넓이 기준
        if (Main_Position.equals("W")){
            ratio = (double)Image_Width/(double) imageWidth;
            resultWidth = (int) (imageWidth * ratio);
            resultHeight = (int) (imageHeight * ratio);
        }
        // 높이 기준
        else if (Main_Position.equals("H")){
            ratio = (double)Image_Height/(double) imageHeight;
            resultWidth = (int) (imageWidth * ratio);
            resultHeight = (int) (imageHeight * ratio);
        }
        // 사용자 설정 기준
        else{
            resultWidth = Image_Width;
            resultHeight = Image_Height;
        }

        Image resizeImage = image.getScaledInstance(resultWidth,resultHeight,Image.SCALE_SMOOTH);

        BufferedImage outputImg = new BufferedImage(resultWidth,resultHeight,BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = outputImg.createGraphics();
        graphics2D.drawImage(resizeImage,0,0, null);
        graphics2D.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImg, "png", outputStream);
        return outputStream.toByteArray();
    }*/
}
