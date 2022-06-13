package com.homework.googleDrive.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "folders")
@Getter
@Setter
@ToString
public class Folder extends WorkspaceItem {
    @DBRef
    private List<Folder> folders;
    @DBRef
    private List<File> files;

    public Folder() {
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
