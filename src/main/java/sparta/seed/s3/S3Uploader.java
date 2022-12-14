package sparta.seed.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public S3Dto upload(MultipartFile multipartFile) throws IOException {

    String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
    String fileFormatName = multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/") + 1);
    MultipartFile resize = resize(fileName, fileFormatName, multipartFile);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(resize.getSize());
    objectMetadata.setContentType(resize.getContentType());
    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, resize.getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
    String result = amazonS3Client.getUrl(bucket, fileName).toString();
//    removeNewFile(new File(Objects.requireNonNull(resize.getOriginalFilename())));
    return new S3Dto(fileName, result);
  }

  private MultipartFile resize(String fileName, String fileFormatName, MultipartFile originalImage) throws IOException {

    // ?????? ?????? ????????? ?????? BufferedImage ????????? ???????????????.
    BufferedImage srcImg = ImageIO.read(originalImage.getInputStream());

    int demandWidth = 1920;

    // ?????? ???????????? ????????? ?????? ?????????.
    int originWidth = srcImg.getWidth();
    int originHeight = srcImg.getHeight();

    // ?????? ????????? ???????????? ?????? ???????????? ????????? ????????? ???????????????.
    int newWidth;
    int newHeight;

    // ?????? ????????? ??? ???????????? ???????????? ??????.
    if (demandWidth >= originWidth) {
      newWidth = originWidth;
      newHeight = originHeight;
    } else {
      newWidth = demandWidth;
      newHeight = (demandWidth * originHeight) / originWidth;
    }

    BufferedImage destImg = Scalr.resize(srcImg, newWidth, newHeight);


    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(destImg, fileFormatName.toLowerCase(), byteArrayOutputStream);
    byteArrayOutputStream.flush();
    destImg.flush();
    return new MockMultipartFile(fileName, byteArrayOutputStream.toByteArray());
  }

  private void removeNewFile(File targetFile) {
    if (targetFile.delete()) {
      log.info("????????? ?????????????????????.");
    } else {
      log.info("????????? ???????????? ???????????????.");
    }
  }
}