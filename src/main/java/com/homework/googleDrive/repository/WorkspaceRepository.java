package com.homework.googleDrive.repository;

import com.homework.googleDrive.model.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
}