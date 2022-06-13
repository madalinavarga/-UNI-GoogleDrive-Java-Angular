package com.homework.googleDrive.controller;

import com.homework.googleDrive.model.File;
import com.homework.googleDrive.model.Folder;
import com.homework.googleDrive.model.request.CreateFileRequest;
import com.homework.googleDrive.model.request.CreateFolderRequest;
import com.homework.googleDrive.repository.FileRepository;
import com.homework.googleDrive.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("api/workspace")
public class FolderController {
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    @Autowired
    public FolderController(FileRepository fileRepository, FolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    @GetMapping("/{sessionToken}/folder/{folderId}")
    public Folder get(@PathVariable String sessionToken, @PathVariable String folderId) {
        // get folder from database
        Folder folder = folderRepository.findById(folderId).get();

        // check if user is owner of folder
        if (!folder.getOwner().getSessionToken().equals(sessionToken)) {
            throw new ResponseStatusException(FORBIDDEN, "You are not the owner");
        }

        return folder;
    }

    @PostMapping("/{sessionToken}/folder/{folderId}/file")
    public File addFile(@PathVariable String sessionToken,
                        @PathVariable String folderId,
                        CreateFileRequest fileRequest) throws IOException {
        // check if user is owner of folder
        Folder parentFolder = folderRepository.findById(folderId).get();
        if (!parentFolder.getOwner().getSessionToken().equals(sessionToken)) {
            throw new ResponseStatusException(FORBIDDEN, "You are not the owner");
        }

        // create new file
        MultipartFile inputFile = fileRequest.getFile();
        File newFile = new File();
        if (inputFile.getContentType().equals("image/png")) {
            newFile.setContent(Base64.getEncoder().encodeToString(inputFile.getBytes()));
        } else {
            newFile.setContent(new String(inputFile.getBytes()));
        }
        newFile.setName(inputFile.getOriginalFilename());
        newFile.setCreatedAt(LocalDateTime.now());
        newFile.setUpdatedAt(LocalDateTime.now());
        newFile.setOwner(parentFolder.getOwner());

        // add file to database
        File createdFile = fileRepository.insert(newFile);

        // update files of parent folder in database
        parentFolder.addFile(createdFile);
        folderRepository.save(parentFolder);

        return createdFile;
    }

    @PostMapping("/{sessionToken}/folder/{folderId}/folder")
    public Folder addFolder(@PathVariable String sessionToken,
                            @PathVariable String folderId,
                            CreateFolderRequest folderRequest) {
        // check if user is owner of folder
        Folder parentFolder = folderRepository.findById(folderId).get();
        if (!parentFolder.getOwner().getSessionToken().equals(sessionToken)) {
            throw new ResponseStatusException(FORBIDDEN, "You are not the owner");
        }

        // create new folder
        Folder newFolder = new Folder();
        newFolder.setName(folderRequest.getName());
        newFolder.setCreatedAt(LocalDateTime.now());
        newFolder.setUpdatedAt(LocalDateTime.now());
        newFolder.setOwner(parentFolder.getOwner());

        // add folder to database
        Folder createdFolder = folderRepository.insert(newFolder);

        // update folders of parent folder in database
        parentFolder.addFolder(createdFolder);
        folderRepository.save(parentFolder);

        return createdFolder;
    }
}
