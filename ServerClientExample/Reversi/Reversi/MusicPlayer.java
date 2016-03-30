import java.io.IOException;

// import java.net.URL;
// import sun.audio.AudioData;
// import sun.audio.AudioPlayer;
// import sun.audio.AudioStream;
// import sun.audio.ContinuousAudioDataStream;

// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class MusicPlayer extends Thread
{
    final private String music;

    public MusicPlayer(String music) {
        this.music = music;
    }

    @Override
    public void run() {
        try {
            play_music();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Error: UnsupportedAudioFileException when opening music file.");
        } catch (LineUnavailableException e) {
            System.out.println("Error: LineUnavailableException when opening music file.");
        } catch (IOException e) {
            System.out.println("Error: IOException when opening music file.");
        }
    }
	public void play_music() throws LineUnavailableException,UnsupportedAudioFileException,IOException
	{
		// URL url = new File(music).toURI().toURL();
		// AudioStream as = new AudioStream (url.openStream());
		// AudioData data = as.getData();
		// ContinuousAudioDataStream cas = new ContinuousAudioDataStream (data);
		// AudioPlayer.player.start(cas);


		// Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		// Format input2 = new AudioFormat(AudioFormat.MPEG);
		// Format output = new AudioFormat(AudioFormat.LINEAR);
		// PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder",new Format[]{input1,input2},new Format[]{output},PlugInManager.CODEC);
		// try
		// {
		//     Player player = Manager.createPlayer(new MediaLocator(new File(music).toURI().toURL()));
		//     player.start();
		// }
		// catch(Exception ex)
		// {
		// 	ex.printStackTrace();
		// }


		// while (true)
		// {
		// 	MediaPlayer mediaPlayer = new MediaPlayer(new Media("file:///"+System.getProperty("user.dir")+music));
		// 	mediaPlayer.play();
		// }
		while (true)
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(this.music));
			AudioFormat audioFormat = audioInputStream.getFormat();
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) 
			{
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,audioFormat.getSampleRate(),16,audioFormat.getChannels(),audioFormat.getChannels() * 2,audioFormat.getSampleRate(),false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat,audioInputStream);
			}
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,audioFormat,AudioSystem.NOT_SPECIFIED);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			int count;
			byte tempBuffer[] = new byte[1024];
			while ((count = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) 
			{
				if (count > 0) 
				{
					sourceDataLine.write(tempBuffer, 0, count);
				}
			}
			sourceDataLine.drain();
		}
	}
}
