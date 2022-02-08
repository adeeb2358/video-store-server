package woven.video.storage.server.api.converters;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import woven.video.storage.server.api.converters.codecs.WebmAudioCodec;
import woven.video.storage.server.api.converters.codecs.WebmVideoCodec;
import woven.video.storage.server.api.converters.listeners.ConvertProgressListener;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.repos.VideoFileRepository;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

@Component
@Getter
@Setter
@AllArgsConstructor
@EnableAsync
/** @author adeeb2358 */
public class WebmConverter {

    private final String OUTPUT_FORMAT = "webm";
    private final Encoder encoder;
    private final VideoFileRepository repository;

    public VideoFile convert(VideoFile videoFile) {
        try {
            var multimediaObject = new MultimediaObject(videoFile.getBinaryContent());
            var encodingAttributes = new EncodingAttributes();
            var targetFile = new File(videoFile.getConvertedFilePath());
            encodingAttributes.setAudioAttributes(new WebmAudioCodec().getCodec(multimediaObject));
            encodingAttributes.setVideoAttributes(new WebmVideoCodec().getCodec(multimediaObject));
            encodingAttributes.setFormat(OUTPUT_FORMAT);
            encoder.encode(
                    multimediaObject,
                    targetFile,
                    encodingAttributes,
                    new ConvertProgressListener(videoFile, this));
            videoFile.conversionFinished();
            repository.save(videoFile);
        } catch (Exception e) {
            videoFile.conversionFailed(e.getMessage());
            repository.save(videoFile);
        }
        return videoFile;
    }

    public void saveProgress(VideoFile videoFile) {
        repository.save(videoFile);
    }
}
