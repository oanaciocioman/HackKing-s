
/*
DEVELOPING GAME IN JAVA

Caracteristiques

Editeur : NEW RIDERS
Auteur : BRACKEEN
Parution : 09 2003
Pages : 972
Isbn : 1-59273-005-1
Reliure : Paperback
Disponibilite : Disponible a la librairie
*/


import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * An example of playing a sound with an echo filter.
 *
 * @see EchoFilter
 * @see SimpleSoundPlayer
 */
public class EchoFilterTest {

   // public static void main(String[] args) {
    public static void gain() {
        // load the sound
        SimpleSoundPlayer sound = new SimpleSoundPlayer("../sounds/voice.wav", 7200, "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\amy2.wav");

        // create the sound stream
        InputStream is = new ByteArrayInputStream(sound.getSamples());

        // create an echo with a 11025-sample buffer
        // (1/4 sec for 44100Hz sound) and a 60% decay
        EchoFilter filter = new EchoFilter(11025, .6f);

        // create the filtered sound stream
        is = new FilteredSoundStream(is, filter);

        // play_silent the sound
        sound.play_silent(is);

        // due to bug in Java Sound, explicitly exit the VM.
        System.exit(0);
    }

}
