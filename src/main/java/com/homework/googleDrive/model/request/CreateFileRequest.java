package com.homework.googleDrive.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateFileRequest {
    private MultipartFile file;
}
