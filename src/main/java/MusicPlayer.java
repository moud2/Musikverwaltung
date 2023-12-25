import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;

public class MusicPlayer {
    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;
    private File myFile = null;
    private long totalLength;
    private Player player;
    private Thread playThread;

    MusicPlayer() {
    }

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }

    public void playSong() {
        if (player != null) {
            player.close();
        }
        playThread = null;
        playThread = new Thread(runnablePlay);
        playThread.start();
    }

    public void stopSong() {
        if (player != null) {
            player.close();
        }
    }

    private Runnable runnablePlay = () -> {
        try {
            fileInputStream = new FileInputStream(myFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            player = new Player(bufferedInputStream);
            totalLength = fileInputStream.available();
            player.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}