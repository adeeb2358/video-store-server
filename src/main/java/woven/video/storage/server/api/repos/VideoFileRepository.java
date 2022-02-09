package woven.video.storage.server.api.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.documents.VideoFile.Status;

/**
 * The interface Video file repository.
 *
 * @author adeeb2358
 */
@Repository
public interface VideoFileRepository extends MongoRepository<VideoFile, String> {

    /**
     * Find by check sum optional.
     *
     * @param checkSum the check sum
     * @return the optional
     */
    Optional<VideoFile> findByCheckSum(String checkSum);

    /**
     * Find by id and status not optional.
     *
     * @param id     the id
     * @param status the status
     * @return the optional
     */
    Optional<VideoFile> findByIdAndStatusNot(String id, Status status);

    /**
     * Find by status not list.
     *
     * @param status the status
     * @return the list
     */
    List<VideoFile> findByStatusNot(Status status);

}


