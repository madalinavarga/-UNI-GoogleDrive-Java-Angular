package com.homework.googleDrive.repository;

import com.homework.googleDrive.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {
}