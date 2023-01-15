package com.example.PersonalSpringStudy.test.base64_test;

import com.example.PersonalSpringStudy.setting.aws_s3.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class baseToImgController {

    private final S3UploadUtil s3UploadUtil;

    @PostMapping("/api/test/v1/base64")
    public void covertToImg(@RequestBody BaseRequestDto baseRequestDto) throws IOException {
        System.out.println(baseRequestDto.getBase64());
        byte[] decodedBytes = Base64.getDecoder().decode(baseRequestDto.getBase64());

        File imageFile = new File("64toImg.png");
        FileOutputStream fos = new FileOutputStream(imageFile);
        fos.write(decodedBytes);
        fos.close();
        System.out.println("이미지 확인해보세요");

        var r = s3UploadUtil.upload(imageFile, "test");
        System.out.println(r);
    }
    @PostMapping("/api/test/v1/html")
    public void covertToHtml(@RequestPart(value = "content") String content) throws IOException {

        File htmlFile = new File("StringToHtml.html");
        FileOutputStream fos = new FileOutputStream(htmlFile);
        fos.write(content.getBytes());
        fos.close();
        System.out.println("이미지 확인해보세요");
        var r = s3UploadUtil.upload(htmlFile, "html");
        System.out.println(r);

    }

}

