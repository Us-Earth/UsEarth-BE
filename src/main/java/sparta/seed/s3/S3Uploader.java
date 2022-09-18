package sparta.seed.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Dto upload(MultipartFile multipartFile) {

        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();

        String result = amazonS3Client.getUrl(bucket, fileName).toString();

        MultipartFile resizedFile = resizeImage(multipartFile, 1000);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resizedFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = resizedFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        return new S3Dto(fileName, result);

    }


    public MultipartFile resizeImage(MultipartFile originalImage, int targetWidth) {
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            // newWidth : newHeight = originWidth : originHeight
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if (originWidth < targetWidth) {

                return originalImage;
            } else {


                MarvinImage imageMarvin = new MarvinImage(image);


                Scale scale = new Scale();
                scale.load();
                scale.setAttribute("newWidth", targetWidth);
                scale.setAttribute("newHeight", targetWidth * originHeight / originWidth);
                scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

                String fileFormatName = originalImage.getContentType().substring(originalImage.getContentType().lastIndexOf("/") + 1);
                BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(imageNoAlpha, fileFormatName, baos);
                baos.flush();

                return new MultipartFileImpl(baos.toByteArray() ,originalImage);
            }

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리사이즈에 실패했습니다.");
        }
    }


}