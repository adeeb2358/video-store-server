package woven.video.storage.server.api.converters.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

/** @author adeeb2358 */
@Setter
@Getter
@AllArgsConstructor
public class WebmAudioCodec {
  private static String WEBM_AUDIO_CODEC = "libopus";

  public AudioAttributes getCodec(MultimediaObject multimediaObject) throws EncoderException {
    var audioAttributes = new AudioAttributes();
    var audioInfo = multimediaObject.getInfo().getAudio();
    audioAttributes.setCodec(WEBM_AUDIO_CODEC);
    audioAttributes.setBitRate(audioInfo.getBitRate());
    audioAttributes.setChannels(audioInfo.getChannels());
    return audioAttributes;
  }
}
