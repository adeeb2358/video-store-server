package woven.video.storage.server.api.services.impl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import woven.video.storage.server.api.converters.WebmConverter;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.documents.VideoFile.Status;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoConversionService;

/**
 * The type Video conversion service.
 *
 * @author adeeb2358
 */
@RequiredArgsConstructor
public class VideoConversionServiceImpl implements VideoConversionService {

    private final VideoFileRepository repository;
    private final WebmConverter converter;
    private final int NO_OF_THREADS;

    @Override
    public VideoFile create(String fileId) throws FileNotFoundException, InterruptedException {
        var videoFile =
                repository.findById(fileId)
                        .orElseThrow(FileNotFoundException::new);
        if (videoFile.getStatus().equals(Status.NOT_CONVERTED)) {
            videoFile.conversionQueued();
            repository.save(videoFile);
            addToConversionThread(videoFile);
        }
        return videoFile;
    }

    @Override
    public VideoFile get(String fileId) throws FileNotFoundException {
        return repository.findByIdAndStatusNot(fileId, Status.NOT_CONVERTED)
                .orElseThrow(FileNotFoundException::new);
    }

    @Override
    public List<VideoFile> list() {
        return repository.findByStatusNot(Status.NOT_CONVERTED);
    }

    @Override
    public VideoFile convertFailed(String fileId) throws FileNotFoundException,
            InterruptedException {
        var videoFile = repository.findById(fileId).orElseThrow(FileNotFoundException::new);
        if (videoFile.getStatus().equals(Status.FAILED)) {
            addToConversionThread(videoFile);
        }
        return videoFile;
    }

    /**
     * Add to conversion thread.
     *
     * @param videoFile the video file
     * @throws InterruptedException the interrupted exception
     */
    public void addToConversionThread(VideoFile videoFile) throws InterruptedException {
        videoFile.conversionQueued();
        repository.save(videoFile);
        var exec = Executors.newFixedThreadPool(NO_OF_THREADS);
        var task = taskFor(videoFile);
        exec.submit(task);
    }

    /**
     * Task for callable.
     *
     * @param videoFile the video file
     * @return the callable
     */
    public Callable<Boolean> taskFor(VideoFile videoFile) {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    converter.convert(videoFile);
                } catch (Exception e) {
                    videoFile.conversionFailed(e.getMessage());
                    repository.save(videoFile);
                    return false;
                }
                return true;
            }
        };
    }

}
