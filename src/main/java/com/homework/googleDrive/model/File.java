package com.homework.googleDrive.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "files")
@Getter
@Setter
@ToString
public class File extends WorkspaceItem {
    private String content;
}
