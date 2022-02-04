package woven.video.storage.server.api.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import woven.video.storage.server.api.repos.VideoFileRepository;
import woven.video.storage.server.api.services.VideoFileService;

/**
 * @author adeeb2358
 */
@Configuration
public class VideoFileServiceImplConfig {

    @Autowired
    private VideoFileRepository repository;

    @Value("${woven.video.storage.server.api.storage.directory}")
    String VIDEO_STORE_DIR;

    @Bean
    VideoFileService videoFileService(){
        return new VideoFileServiceImpl(repository,VIDEO_STORE_DIR);
    }
}
