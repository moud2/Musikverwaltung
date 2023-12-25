import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;


import javax.swing.JFileChooser;

import static javax.swing.JOptionPane.showMessageDialog;




public class MusicManagementGUI extends JFrame implements ActionListener {

	ImageIcon iconMusic;
	JLabel background;

	static Playlist currentPlaylist;


	static MusicPlayer musicPlayer = new MusicPlayer();

	private DefaultTableModel tableModel = new DefaultTableModel();
	private JTable table = new JTable(tableModel);

	private JScrollPane tableScroller = new JScrollPane(table);

	private JButton btnPlaySong = new JButton();
	private JButton btnStop = new JButton();
	private JButton btnMakePlaylist = new JButton("new Playlist");
	private JButton btnSearch = new JButton();
	private JButton btnAddSong = new JButton();
	private JButton btnRemoveSong = new JButton();
	private JButton btnChoosePlaylist = new JButton("Choose Playlist");

	private JLabel labelCurrentName = new JLabel("");

	private JTextField textFieldPlaylistName = new JTextField();
	private JTextField textFieldSearch = new JTextField();

	static JLabel labelTotal = new JLabel("");

	public JTextField getTextFieldSearch() {
		return this.textFieldSearch;
	}

	public DefaultTableModel getTableModel() {
		return this.tableModel;
	}

	public JTable getTable() {
		return this.table;
	}

	public JLabel getLabelCurrentName() {
		return this.labelCurrentName;
	}

	public JTextField getTextFieldPlaylistName() {
		return this.textFieldPlaylistName;
	}

	public MusicManagementGUI() {
		super("Musikverwaltung");

		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(950, 400);
		setLocation(600, 700);
		setLocationRelativeTo(null);
		setResizable(false);

		// Insert Background Image
		iconMusic = new ImageIcon(Path.RES_FOLDER + "freeImage.jpg");
		background = new JLabel(iconMusic);
		background.setBounds(0, 0, 1000, 700);

		ImageIcon playIcon = new ImageIcon(Path.RES_FOLDER + "Play15.png");
		btnPlaySong.setIcon(playIcon);

		ImageIcon stopIcon = new ImageIcon(Path.RES_FOLDER + "Stop15.png");
		btnStop.setIcon(stopIcon);

		ImageIcon searchIcon = new ImageIcon(Path.RES_FOLDER + "search15.png");
		btnSearch.setIcon(searchIcon);

		ImageIcon addIcon = new ImageIcon(Path.RES_FOLDER + "Plus.png");
		btnAddSong.setIcon(addIcon);

		ImageIcon removeIcon = new ImageIcon(Path.RES_FOLDER + "remove.png");
		btnRemoveSong.setIcon(removeIcon);

		JTableHeader header = table.getTableHeader();
		header.setBackground(new Color(10, 130, 130));
		getColorModel();
		tableModel.addColumn("Artist");
		tableModel.addColumn("Titel");
		tableModel.addColumn("Genre");
		tableModel.addColumn("Album");

		// Insert Dimensions
		btnPlaySong.setBounds(250, 320, 100, 30);
		btnStop.setBounds(370, 320, 100, 30);
		tableScroller.setBounds(250, 110, 500, 200);
		labelCurrentName.setBounds(450, 65, 200, 50);
		labelCurrentName.setForeground(Color.white);
		btnChoosePlaylist.setBounds(250, 75, 180, 30);
		btnAddSong.setBounds(490, 320, 110, 30);
		btnRemoveSong.setBounds(620, 320, 115, 30);
		btnMakePlaylist.setBounds(20, 20, 115, 30);
		textFieldPlaylistName.setBounds(130, 20, 100, 30);
		textFieldSearch.setBounds(815, 20, 100, 30);
		btnSearch.setBounds(700, 20, 115, 30);
		labelTotal.setBounds(150, 250, 100, 30);

		// Insert to JFrame
		add(btnPlaySong);
		add(btnStop);
		add(tableScroller);
		add(background);
		add(textFieldPlaylistName);
		add(labelCurrentName);
		add(btnChoosePlaylist);
		add(btnAddSong);
		add(btnRemoveSong);
		add(btnMakePlaylist);
		add(textFieldSearch);
		add(btnSearch);
		add(labelTotal);
		add(background);

		btnPlaySong.addActionListener(this);
		btnStop.addActionListener(this);
		btnMakePlaylist.addActionListener(this);
		btnSearch.addActionListener(this);
		btnAddSong.addActionListener(this);
		btnRemoveSong.addActionListener(this);
		btnChoosePlaylist.addActionListener(this);

		setVisible(true);
	}

	private static void readAllSongs() throws Exception {
		Playlist allSongPlaylist = new Playlist(Path.AllSongs);
		allSongPlaylist.addSongs(MusicFileUtil.getSongsFromFolder(Path.MP3_FOLDER));
		MusicFileUtil.playListToFile(allSongPlaylist);
	}

	public String findFileOnPc(String startingFolder) {
		JFileChooser fileChooser = new JFileChooser(startingFolder);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public void initializeProgram() throws Exception {

		// check if the file that containes all songs exicts
		File allSongsPlaylist = new File (Path.PLAYLIST_FOLDER, Path.AllSongs);
		if (!allSongsPlaylist.exists()) {
			// when it's the first start ever the need to be created
			readAllSongs();
		}
		String allSongsPathPlayList = Path.PLAYLIST_FOLDER + Path.AllSongs;
		Playlist playlist = MusicFileUtil.fileToPlaylist(allSongsPathPlayList);
		updateSongDisplay(playlist);
	}

	private  void updateSongDisplay(Playlist playlist) {
		currentPlaylist = playlist;
		this.getLabelCurrentName().setText(currentPlaylist.getName());
		System.out.println(currentPlaylist.getName());
		List<Song> songs = playlist.getSongs();
		this.getTableModel().getDataVector().removeAllElements();
		this.getTableModel().fireTableDataChanged();
		for (Song song : songs) {
			String[] songInfo = {song.getArtist(), song.getTitle(), song.getGenre(), song.getAlbum()};
			this.getTableModel().addRow(songInfo);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSearch) {
			Playlist searchResults = new Playlist("search results");
			String searchQuery = getTextFieldSearch().getText().toLowerCase();
			if (!searchQuery.isEmpty()) {
				for (Song song : currentPlaylist.getSongs()) {
					if (song.getTitle().toLowerCase().contains(searchQuery)) {
						searchResults.addSong(song);

					} else if (song.getArtist().toLowerCase().contains(searchQuery)) {
						searchResults.addSong(song);

					} else if (song.getGenre().toLowerCase().contains(searchQuery)) {
						searchResults.addSong(song);

					} else if (song.getAlbum().toLowerCase().contains(searchQuery)) {
						searchResults.addSong(song);

					}
				}
				if (searchResults.getSongs().size() <= 0) {
					String msg = "No song with that name was found!";
					showMessageDialog(null, msg);
				} else {
					currentPlaylist = searchResults;
					updateSongDisplay(currentPlaylist);
				}
			} else {
				String msg = "Enter a search query!";
				showMessageDialog(null, msg);
			}
		} else if (e.getSource() == btnPlaySong) {
			int index = getTable().getSelectedRow();
			if (index >= 0) {
				String path =  currentPlaylist.SongIndex(index).getPath();
				System.out.println(path);
				musicPlayer.setMyFile(new File(path));
				musicPlayer.playSong();
			}
		}else if (e.getSource() == btnStop){

				musicPlayer.stopSong();

		}else if(e.getSource() == btnRemoveSong){
			int selectedRow = this.getTable().getSelectedRow();
			if (selectedRow >= 0) {
				currentPlaylist.deleteSong(selectedRow);
				try {
					MusicFileUtil.playListToFile(currentPlaylist);
					updateSongDisplay(currentPlaylist);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}else if (e.getSource() == btnMakePlaylist){
			String playlistName = this.getTextFieldPlaylistName().getText();
			boolean nameExists = !playlistName.isEmpty();

			int[] selectedRows = this.getTable().getSelectedRows();
			boolean hasSelection = selectedRows.length > 0;

			if (nameExists && hasSelection) {
				Playlist newPlaylist = new Playlist(playlistName);
				for (int rowIndex : selectedRows) {
					Song song = currentPlaylist.SongIndex(rowIndex);
					newPlaylist.addSong(song);
				}
				try {
					MusicFileUtil.playListToFile(newPlaylist);
					updateSongDisplay(newPlaylist);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				String errorMsg;
				if (!hasSelection && !nameExists) {
					errorMsg = "please select a song and add a name !!!";
				} else if (!hasSelection) {
					errorMsg = "please select a song!";
				} else {
					errorMsg = "please give a name to the playlist!";
				}
				showMessageDialog(null, errorMsg);
			}
		}
		else if(e.getSource() == btnChoosePlaylist){
			File selectedFile = new File(findFileOnPc(Path.PLAYLIST_FOLDER));
			if (selectedFile == null) {
				System.out.println("no file selected !");
				return;
			}
			try {
				Playlist playlist = MusicFileUtil.fileToPlaylist(selectedFile.toPath().toString());
				updateSongDisplay(playlist);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == btnAddSong){


					File selectedFile = new File(findFileOnPc(Path.HOME_DIR));
					if (selectedFile == null) {
						System.out.println("no file selected !");
						return;
					}
					try {
						String filePath = selectedFile.toPath().toString();
						Song newSong = MusicFileUtil.getSongInfos(filePath);
						currentPlaylist.addSong(newSong);
						MusicFileUtil.playListToFile(currentPlaylist);
						updateSongDisplay(currentPlaylist);

					} catch (UnsupportedTagException e1) {
						e1.printStackTrace();
					} catch (InvalidDataException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}


		}
	}
}