package woven.video.storage.server.api.repos;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import woven.video.storage.server.api.documents.VideoFile;

/** @author adeeb2358 */
@Repository
public interface VideoFileRepository extends MongoRepository<VideoFile, String> {
  Optional<VideoFile> findByCheckSum(String checkSum);
}
