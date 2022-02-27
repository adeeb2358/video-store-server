package video.storage.server.api.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import video.storage.server.api.converters.WebmConverter;
import video.storage.server.api.repos.VideoFileRepository;
import video.storage.server.api.services.VideoConversionService;
import video.storage.server.api.services.VideoFileService;
import video.storage.server.api.services.impl.VideoConversionServiceImpl;
import video.storage.server.api.services.impl.VideoFileServiceImpl;

/**
 * The type Video file service config.
 *
 * @author adeeb2358
 */
@Configuration
public class VideoFileServiceConfig {

    @Autowired
    private VideoFileRepository repository;

    /**
     * The Video store dir.
     */
    @Value("${woven.video.storage.server.api.storage.directory}")
    String VIDEO_STORE_DIR;

    /**
     * The Thread pool size.
     */
    @Value("${thread.maxpoolsize:100}")
    int THREAD_POOL_SIZE;

    /**
     * Video file service video file service.
     *
     * @return the video file service
     */
    @Bean
    VideoFileService videoFileService() {
        return new VideoFileServiceImpl(repository, VIDEO_STORE_DIR);
    }

    @Autowired
    private WebmConverter webmConverter;

    /**
     * Video conversion service video conversion service.
     *
     * @return the video conversion service
     */
    @Bean
    VideoConversionService videoConversionService() {
        return new VideoConversionServiceImpl(repository, webmConverter, this.THREAD_POOL_SIZE);
    }
}
