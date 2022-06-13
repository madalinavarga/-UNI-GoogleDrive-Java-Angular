package com.homework.googleDrive.repository;

import com.homework.googleDrive.model.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FolderRepository extends MongoRepository<Folder, String> {
}