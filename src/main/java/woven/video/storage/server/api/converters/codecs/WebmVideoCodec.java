package woven.video.storage.server.api.converters.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;

/**
 * The type Webm video codec.
 *
 * @author adeeb2358
 */
@Getter
@Setter
@AllArgsConstructor
public class WebmVideoCodec {
  private static final String WEBM_VIDEO_CODEC = "libvpx-vp9";

    /**
     * Gets codec.
     *
     * @param multimediaObject the multimedia object
     * @return the codec
     * @throws EncoderException the encoder exception
     */
    public VideoAttributes getCodec(MultimediaObject multimediaObject) throws EncoderException {
    var videoAttributes = new VideoAttributes();
    var videoInfo = multimediaObject.getInfo().getVideo();
    videoAttributes.setCodec(WEBM_VIDEO_CODEC);
    videoAttributes.setBitRate(videoInfo.getBitRate());
    videoAttributes.setFrameRate(Math.round(videoInfo.getFrameRate()));
    videoAttributes.setSize(videoInfo.getSize());
    return videoAttributes;
  }
}
