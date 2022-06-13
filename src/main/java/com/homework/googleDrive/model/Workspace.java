package com.homework.googleDrive.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "workspaces")
@Getter
@Setter
@ToString
public class Workspace {
    @Id
    private String id;
    @DBRef
    private List<Folder> folders;
    @DBRef
    private List<File> files;
    @DBRef
    private User owner;

    public Workspace() {
        folders = new ArrayList<>();
        files = new ArrayList<>();
    }

    public Workspace(User user) {
        this.owner = user;
        folders = new ArrayList<>();
        files = new ArrayList<>();
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void addFolder(Folder folder) {
        folders.add(folder);
    }
}
