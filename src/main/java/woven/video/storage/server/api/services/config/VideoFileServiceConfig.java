package woven.video.storage.server.api.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import woven.video.storage.server.api.converters.WebmConverter;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoConversionService;
import woven.video.storage.server.api.services.VideoFileService;
import woven.video.storage.server.api.services.impl.VideoConversionServiceImpl;
import woven.video.storage.server.api.services.impl.VideoFileServiceImpl;

/**
 * @author adeeb2358
 */
@Configuration
public class VideoFileServiceConfig {

    @Autowired
    private VideoFileRepository repository;

    @Value("${woven.video.storage.server.api.storage.directory}")
    String VIDEO_STORE_DIR;

    @Value("${thread.maxpoolsize:100}")
    int THREAD_POOL_SIZE;

    @Bean
    VideoFileService videoFileService() {
        return new VideoFileServiceImpl(repository, VIDEO_STORE_DIR);
    }

    @Autowired
    private WebmConverter webmConverter;

    @Bean
    VideoConversionService videoConversionService() {
        return new VideoConversionServiceImpl(repository, webmConverter, this.THREAD_POOL_SIZE);
    }
}
