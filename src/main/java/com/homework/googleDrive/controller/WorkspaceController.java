package com.homework.googleDrive.controller;

import com.homework.googleDrive.model.File;
import com.homework.googleDrive.model.Folder;
import com.homework.googleDrive.model.Workspace;
import com.homework.googleDrive.model.request.CreateFileRequest;
import com.homework.googleDrive.model.request.CreateFolderRequest;
import com.homework.googleDrive.repository.FileRepository;
import com.homework.googleDrive.repository.FolderRepository;
import com.homework.googleDrive.repository.UserRepository;
import com.homework.googleDrive.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("api/workspace")
public class WorkspaceController {
    private final WorkspaceRepository workspaceRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkspaceController(WorkspaceRepository workspaceRepository,
                               FileRepository fileRepository,
                               FolderRepository folderRepository,
                               UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{sessionToken}")
    public Optional<Workspace> getWorkspace(@PathVariable String sessionToken) {
        return workspaceRepository.findAll()
                .stream()
                .filter(workspace -> workspace.getOwner().getSessionToken().equals(sessionToken))
                .findAny();
    }

    @PostMapping("/{sessionToken}/file")
    public File addFile(@PathVariable String sessionToken, CreateFileRequest fileRequest) throws IOException {
        // check if workspace exists
        Optional<Workspace> foundWorkspace = this.getWorkspace(sessionToken);
        if (foundWorkspace.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Workspace not found!");
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
        newFile.setOwner(userRepository.getByToken(sessionToken).get(0));

        // add file to database
        File createdFile = fileRepository.insert(newFile);

        // update files of workspace in database
        Workspace workspace = workspaceRepository.findById(foundWorkspace.get().getId()).get();
        workspace.addFile(createdFile);
        workspaceRepository.save(workspace);

        return createdFile;
    }

    @PostMapping("/{sessionToken}/folder")
    public Folder addFolder(@PathVariable String sessionToken, CreateFolderRequest folderRequest) {
        // check if workspace exists
        Optional<Workspace> foundWorkspace = this.getWorkspace(sessionToken);
        if (foundWorkspace.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Workspace not found!");
        }

        // create new folder
        Folder newFolder = new Folder();
        newFolder.setName(folderRequest.getName());
        newFolder.setCreatedAt(LocalDateTime.now());
        newFolder.setUpdatedAt(LocalDateTime.now());
        newFolder.setOwner(userRepository.getByToken(sessionToken).get(0));

        // add folder to database
        Folder createdFolder = folderRepository.insert(newFolder);

        // update folders of workspace in database
        Workspace workspace = workspaceRepository.findById(foundWorkspace.get().getId()).get();
        workspace.addFolder(createdFolder);
        workspaceRepository.save(workspace);

        return createdFolder;
    }
}
