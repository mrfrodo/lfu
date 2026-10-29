package com.frodo.lfu.domain.models;

import java.time.Instant;
import java.util.Objects;

/**
 * Aggregate root
 */
public final class StoredFile {
    private final FileId fileId;
    private final FileMetadata fileMetadata;
    private final String uploadedBy;
    private final Instant createdAt;
    private FileStatus uploadStatus;
    private Instant completedAt;

    // Private constructor for internal creation and reconstitution
    private StoredFile(FileId fileId, FileMetadata fileMetadata, FileStatus uploadStatus, String uploadedBy, Instant createdAt) {
        this.fileId = Objects.requireNonNull(fileId, "fileId cannot be null");
        this.fileMetadata = Objects.requireNonNull(fileMetadata, "fileMetadata cannot be null");
        this.uploadStatus = Objects.requireNonNull(uploadStatus, "uploadStatus cannot be null");
        this.uploadedBy = Objects.requireNonNull(uploadedBy, "uploadedBy cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
        this.completedAt = null;
    }

    /**
     * Static factory method to create a brand new StoredFile.
     */
    public static StoredFile initiate(FileId fileId, FileMetadata fileMetadata, String uploadedBy, Instant createdAt) {
        return new StoredFile(
                fileId,
                fileMetadata,
                FileStatus.INITIATED,
                uploadedBy,
                createdAt
        );
    }

    //reconstitute from database
    public StoredFile reconstitute() {
        //create from db
        return null;
    }

    public void markUploading()  {
        if (uploadStatus != FileStatus.INITIATED) {
            throw new IllegalStateException("Cannot start uploading when status is " + uploadStatus);
        }
        this.uploadStatus = FileStatus.UPLOADING;
    }

    public void markCompleted(Instant completedAt)  {
        if (uploadStatus != FileStatus.UPLOADING) {
            throw new IllegalStateException("Cannot complete when status is " + uploadStatus);
        }
        this.completedAt = completedAt;
        this.uploadStatus = FileStatus.COMPLETED;
    }

    public void markFailed() {
        if (uploadStatus == FileStatus.INITIATED || uploadStatus == FileStatus.UPLOADING) {
            this.uploadStatus = FileStatus.FAILED;
        } else {
            throw new IllegalStateException("Cannot mark failed when status is " + uploadStatus);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoredFile other)) return false;
        return fileId.equals(other.fileId);
    }

    @Override
    public int hashCode() {
        return fileId.hashCode();
    }

    public FileId getFileId() {
        return fileId;
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public FileStatus getUploadStatus() {
        return uploadStatus;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
