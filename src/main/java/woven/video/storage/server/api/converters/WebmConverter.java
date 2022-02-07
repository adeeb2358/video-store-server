package woven.video.storage.server.api.converters;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
/** @author adeeb2358 */
public class WebmConverter {

  private final Encoder encoder;
  private final VideoFileRepository repository;

  public void convert(VideoFile videoFile) {
    try {
      var multimediaObject = new MultimediaObject(videoFile.getBinaryContent());
      var encodingAttributes = new EncodingAttributes();
      var targetFile = new File(videoFile.getConvertedFilePath());
      encodingAttributes.setAudioAttributes(new WebmAudioCodec().getCodec(multimediaObject));
      encodingAttributes.setVideoAttributes(new WebmVideoCodec().getCodec(multimediaObject));
      encoder.encode(
          multimediaObject,
          targetFile,
          encodingAttributes,
          new ConvertProgressListener(videoFile, repository));
      videoFile.conversionStarted();
      repository.save(videoFile);

    } catch (Exception e) {
      videoFile.conversionFailed(e.getMessage());
      repository.save(videoFile);
    }
  }
}
