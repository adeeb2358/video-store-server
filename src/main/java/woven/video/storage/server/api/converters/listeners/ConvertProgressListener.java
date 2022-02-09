package woven.video.storage.server.api.converters.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import woven.video.storage.server.api.converters.WebmConverter;
import woven.video.storage.server.api.documents.VideoFile;
import ws.schild.jave.EncoderProgressListener;
import ws.schild.jave.MultimediaInfo;

/**
 * The type Convert progress listener.
 *
 * @author adeeb2358
 */
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class ConvertProgressListener implements EncoderProgressListener {

    private VideoFile videofile;

    private WebmConverter webmConverter;
    /**
     * This method is called before the encoding process starts, reporting information about the
     * source stream that will be decoded and re-encoded.
     *
     * @param info Informations about the source multimedia stream.
     */
    @Override
    public void sourceInfo(MultimediaInfo info) {}

    /**
     * This method is called to notify a progress in the encoding process.
     *
     * @param permil A permil value representing the encoding process progress.
     */
    @Override
    public void progress(int permil) {
        var progress = permil/10;
        videofile.conversionInProgress((double) progress);
        webmConverter.saveProgress(videofile);
    }

    /**
     * This method is called every time the encoder need to send a message (usually, a warning).
     *
     * @param message The message sent by the encoder.
     */
    @Override
    public void message(String message) {}
}
