package woven.video.storage.server.api.converters.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import woven.video.storage.server.api.documents.VideoFile;
import woven.video.storage.server.api.repos.VideoFileRepository;
import ws.schild.jave.EncoderProgressListener;
import ws.schild.jave.MultimediaInfo;

/** @author adeeb2358 */
@AllArgsConstructor
@Getter
@Setter
public class ConvertProgressListener implements EncoderProgressListener {

  private VideoFile videoFile;
  private VideoFileRepository repository;
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
    double progress = permil / 10;
    videoFile.conversionInProgress(progress);
    repository.save(videoFile);
  }

  /**
   * This method is called every time the encoder need to send a message (usually, a warning).
   *
   * @param message The message sent by the encoder.
   */
  @Override
  public void message(String message) {}
}
