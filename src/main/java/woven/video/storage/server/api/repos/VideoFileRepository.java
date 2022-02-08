package woven.video.storage.server.api.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.documents.VideoFile.Status;

/**
 * @author adeeb2358
 */
@Repository
public interface VideoFileRepository extends MongoRepository<VideoFile, String> {

    Optional<VideoFile> findByCheckSum(String checkSum);

    Optional<VideoFile> findByIdAndStatusNot(String id, Status status);

    List<VideoFile> findByStatusNot(Status status);

}


