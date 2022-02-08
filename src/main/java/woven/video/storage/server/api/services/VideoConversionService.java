package woven.video.storage.server.api.services;

import java.io.FileNotFoundException;
import java.util.List;
import woven.video.storage.server.api.documents.VideoFile;

/**
 * @author adeeb2358
 */
public interface VideoConversionService {

    VideoFile create(String fileId) throws FileNotFoundException, InterruptedException;

    VideoFile get(String fileId) throws FileNotFoundException;

    List<VideoFile> list();

    VideoFile convertFailed(String fileId) throws FileNotFoundException, InterruptedException;
}
