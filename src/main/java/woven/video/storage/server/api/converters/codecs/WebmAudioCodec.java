package woven.video.storage.server.api.converters.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;

/**
 * @author adeeb2358
 */
@Setter
@Getter
@AllArgsConstructor
public class WebmAudioCodec {

    private static String WEBM_AUDIO_CODEC = "libopus";
    private static int DEFAULT_BIT_RATE = 10000;

    public AudioAttributes getCodec(MultimediaObject multimediaObject) throws EncoderException {
        var audioAttributes = new AudioAttributes();
        audioAttributes.setCodec(WEBM_AUDIO_CODEC);
        audioAttributes.setChannels(2);
        audioAttributes.setBitRate(DEFAULT_BIT_RATE);
        var audioInfo = multimediaObject.getInfo().getAudio();
        if (audioInfo != null) {
            audioAttributes.setBitRate(audioInfo.getBitRate());
            audioAttributes.setChannels(audioInfo.getChannels());
        }
        return audioAttributes;
    }
}
