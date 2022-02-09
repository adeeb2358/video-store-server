package woven.video.storage.server.api.services;

import java.io.FileNotFoundException;
import java.util.List;
import woven.video.storage.server.api.documents.VideoFile;

/**
 * The interface Video conversion service.
 *
 * @author adeeb2358
 */
public interface VideoConversionService {

    /**
     * Create video file.
     *
     * @param fileId the file id
     * @return the video file
     * @throws FileNotFoundException the file not found exception
     * @throws InterruptedException  the interrupted exception
     */
    VideoFile create(String fileId) throws FileNotFoundException, InterruptedException;

    /**
     * Get video file.
     *
     * @param fileId the file id
     * @return the video file
     * @throws FileNotFoundException the file not found exception
     */
    VideoFile get(String fileId) throws FileNotFoundException;

    /**
     * List list.
     *
     * @return the list
     */
    List<VideoFile> list();

    /**
     * Convert failed video file.
     *
     * @param fileId the file id
     * @return the video file
     * @throws FileNotFoundException the file not found exception
     * @throws InterruptedException  the interrupted exception
     */
    VideoFile convertFailed(String fileId) throws FileNotFoundException, InterruptedException;
}
