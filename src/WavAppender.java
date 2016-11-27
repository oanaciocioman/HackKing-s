import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WavAppender {

    public void appendWAV(String wavFile1, String wavFile2, String resultFile) {

        try {
            AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File(wavFile1));
            AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));
            SequenceInputStream seq = new SequenceInputStream(clip1, clip2);

            long length = clip1.getFrameLength() + clip2.getFrameLength();

            AudioInputStream appendedFiles = new AudioInputStream(seq, clip1.getFormat(), length);

            AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, new File(resultFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
