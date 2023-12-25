import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.nio.file.*;

public final class MusicFileUtil {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static Playlist fileToPlaylist (String path) throws Exception {
		return jsonStringToPlaylist(readFileAsString(path));
	}

	public static void playListToFile (Playlist playlist) throws Exception {
		savePlayListFile(playlistToJsonString(playlist), playlist.getName());
	}

	// saves a playlist as json file
	private static void savePlayListFile(String jsonString, String fileName) {
		try {
				File playlistFile = new File (Path.PLAYLIST_FOLDER, fileName);
			boolean exists = playlistFile.exists();
	
			if (!exists) {
				// file need to be created first exists and need to be rewritten
				playlistFile.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(playlistFile));
			writer.write(jsonString);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static String playlistToJsonString (Playlist playlist) {
		return gson.toJson(playlist);
	}

	private static Playlist jsonStringToPlaylist (String jsonString) {
		return gson.fromJson(jsonString, Playlist.class);
	}

	private static String readFileAsString(String filePath)throws Exception
	{
	  String data = "";
	  data = new String(Files.readAllBytes(Paths.get(filePath)));
	  return data;
	}
	
	// returns all songs in a given folder
	public static ArrayList<Song> getSongsFromFolder(final String path) throws UnsupportedTagException, InvalidDataException, IOException {
		ArrayList<Song> songsList = new ArrayList<Song>();
		
		final File folder = new File (path);
		for (final File songFile : folder.listFiles()) {
			if (songFile.isDirectory()) {
				// doNothing
			} else {
				Song mp3InFolder = getSongInfos(songFile.getPath());
				if (mp3InFolder != null) {
					songsList.add(mp3InFolder);
				}
			}
		}
		return songsList;
	}


	
	// returns a song info
	public static Song getSongInfos(final String path) throws UnsupportedTagException, InvalidDataException, IOException {
		// TODO ("check the file extention")
		Mp3File mp3File = new Mp3File(path);
		
		if (mp3File.hasId3v2Tag()) {
			  ID3v2 id3v2Tag = mp3File.getId3v2Tag();
			  
			  String title, genre, artist, album;
			  title = id3v2Tag.getTitle();
			  genre = id3v2Tag.getGenreDescription();
			  artist = id3v2Tag.getArtist();
			  album = id3v2Tag.getAlbum();
			  
			  if (title == "" || title == null) {
				  title = Path.UNKNOWN_NAME;
			  }
			  if (genre == "" || genre == null) {
				  genre = Path.UNKNOWN_NAME;
			  }
			  if (artist == "" || artist == null) {
				  artist = Path.UNKNOWN_NAME;
			  }
			  if (album == "" || album == null) {
				  album = Path.UNKNOWN_NAME;
			  }			  
			  Song song = new Song(title, genre, artist, album, path);
			  return song;
	} else {
		return null;
		}
	}
}
