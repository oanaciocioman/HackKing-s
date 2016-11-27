import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 * The SimpleSoundPlayer encapsulates a sound that can be opened from the file
 * system and later played.
 */

class SimpleSoundPlayer {


    public static void main(String[] args) {
        // load a sound
        SimpleSoundPlayer sound = new SimpleSoundPlayer("C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\amy.wav", 72000, "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\amy2.wav");

        // create the stream to play_silent
        InputStream stream = new ByteArrayInputStream(sound.getSamples());

        // play_silent the sound
        sound.play_silent(stream);

        // exit
        System.exit(0);
    }

    private AudioFormat format;

    private byte[] samples;

    private byte[] full_bfr;
    private AudioInputStream inputStream;
    private File out;
    private File in;


    /**
     * Opens a sound from a file.
     */
    public SimpleSoundPlayer(String filename, int speed, String outputFilePath) {
        try {
            out = new File(outputFilePath);
            in = new File(filename);
            // open the audio input stream
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));

            format = stream.getFormat();
            format = new AudioFormat(PCM_SIGNED, speed, 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), format.isBigEndian()); ////

            // get the audio samples
            samples = getSamples(stream);

            inputStream = stream;
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public SimpleSoundPlayer(String filename) {
        try {
            // open the audio input stream
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));

            format = stream.getFormat();

            // get the audio samples
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the samples of this sound as a byte array.
     */
    public byte[] getSamples() {
        return samples;
    }

    /**
     * Gets the samples from an AudioInputStream as an array of bytes.
     */
    private byte[] getSamples(AudioInputStream audioStream) {
        // get the number of bytes to read
        int length = (int) (audioStream.getFrameLength() * format
                .getFrameSize());

        // read the entire stream
        byte[] samples = new byte[length];
        DataInputStream is = new DataInputStream(audioStream);
        try {
            is.readFully(samples);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // return the samples
        return samples;
    }

    /**
     * Plays a stream. This method blocks (doesn't return) until the sound is
     * finished playing.
     */
    public void play_silent(InputStream source) {

        // use a short, 100ms (1/10th sec) buffer for real-time
        // change to the sound stream
        int bufferSize = format.getFrameSize()
                * Math.round(format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        // create a line to play_silent to
        SourceDataLine line;
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufferSize);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }

        // start the line
        line.start();
        List<Byte> full_buffer = new ArrayList<>();

        // copy data to the line
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                numBytesRead = source.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                    //line.write(buffer, 0, numBytesRead);
                    for (int i = 0; i < buffer.length; i++) {
                        full_buffer.add(buffer[i]);
                    }
                }
            }

            byte[] full_buff = new byte[full_buffer.size()];
            for (int i = 0; i < full_buffer.size(); i++) {
                full_buff[i] = full_buffer.get(i);
            }

            // wait until all data is played, then close the line
            line.drain();
            line.close();

            full_bfr = full_buff;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void write() {

        AudioInputStream clip2 = null;

        try {
            clip2 = AudioSystem.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long length = clip2.getFrameLength();

        AudioInputStream appendedFiles = new AudioInputStream(clip2, format, length);

        try {
            AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(InputStream source) {

        // use a short, 100ms (1/10th sec) buffer for real-time
        // change to the sound stream
        int bufferSize = format.getFrameSize()
                * Math.round(format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        // create a line to play to
        SourceDataLine line;
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufferSize);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }

        // start the line
        line.start();

        // copy data to the line
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                numBytesRead = source.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                    line.write(buffer, 0, numBytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // wait until all data is played, then close the line
        line.drain();
        line.close();

    }

}