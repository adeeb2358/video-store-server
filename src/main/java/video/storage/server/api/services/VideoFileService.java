package video.storage.server.api.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import video.storage.server.api.documents.VideoFile;
import video.storage.server.api.services.impl.VideoFileServiceImpl.InvalidFileFormatException;

/**
 * The interface Video file service.
 *
 * @author adeeb2358
 */
public interface VideoFileService {

    /**
     * Create video file.
     *
     * @param file the file
     * @return the video file
     * @throws InvalidFileFormatException the invalid file format exception
     * @throws IOException                the io exception
     */
    VideoFile create(MultipartFile file) throws InvalidFileFormatException, IOException;

    /**
     * Delete string.
     *
     * @param fileId the file id
     * @return the string
     * @throws IOException the io exception
     */
    String delete(String fileId) throws IOException;

    /**
     * List list.
     *
     * @return the list
     */
    List<VideoFile> list();

    /**
     * Get video file.
     *
     * @param fileId the file id
     * @return the video file
     * @throws FileNotFoundException the file not found exception
     */
    VideoFile get(String fileId) throws FileNotFoundException;

}
