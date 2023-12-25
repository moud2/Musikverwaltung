import java.util.ArrayList;
import java.util.List;

public final class Playlist {
    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Song SongIndex(int position) {
        if (position >= 0 && position < songs.size()) {
            return songs.get(position);
        } else {
            return null;
        }
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void addSongs(List<Song> songList) {
        songs.addAll(songList);
    }

    public void deleteAllSongs() {
        songs.clear();
    }

    public boolean deleteSong(Song song) {
        return songs.remove(song);
    }

    public boolean deleteSong(int index) {
        if (index >= 0 && index < songs.size()) {
            songs.remove(index);
            return true;
        } else {
            return false;
        }
    }
}