package com.homework.googleDrive.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class WorkspaceItem {
    @Id
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @DBRef
    private User owner;
}
