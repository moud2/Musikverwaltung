public final class Song {
    private String title;
    private String genre;
    private String artist;
    private String album;
    private String path;

    public Song(String title, String genre, String artist, String album, String path) {
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.album = album;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }
}