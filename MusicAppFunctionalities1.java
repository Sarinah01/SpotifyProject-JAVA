package MusicApp1;
import javazoom.jl.player.Player;
import java.io.*;
import java.sql.*;
import java.util.*;

public class MusicAppFunctionalities {
    //instance-static variables:
    static Player player;
    static Thread playThread;
    static boolean isPaused = false;
    static final Object lock = new Object();
    static PlayingList playlist = new PlayingList();
    static int currentSongIndex = 0;
    static int currentUserId ; // ✅ SET THIS DURING LOGIN IN REAL FLOW

    static ColoursCode cc = new ColoursCode();

    static Connection con;
    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listener's_hub", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //validate user input of integer type
    private static int getValidatedInt() {
        while (true) {
            try {
                String s = sc.nextLine();
                if (s.isEmpty()){
                    System.out.println(cc.RED_BOLD_BRIGHT+"Please enter a number");
                }else {
                    return Integer.parseInt(s);
                }
            } catch (NumberFormatException e) {
                System.out.print(cc.RED_BOLD_BRIGHT+"Invalid input.Characters are not allowed, Please enter a valid number: +\n"+cc.RESET);
            }
        }
    }
    //add song to table of recently played songs
    public static void addToRecentlyPlayed(int userId, int songId) {
        try {
            String sql = "INSERT INTO recently_played (userId, song_id) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error while adding to recently played: " + e.getMessage());
        }
    }


    static Scanner sc = new Scanner(System.in);

    //----------------------------------menu of functions of songs--------------------------------
    public static void showSongfunctions() {
        while (true) {
            System.out.println(cc.BLUE_BOLD_BRIGHT+"\n=====================================");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"1. Pause");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"2. Resume");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"3. Next Song");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"4. Previous Song");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"5. Shuffle Playlist");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"6. Download this song");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"7. Like / Rate / Add to Playlist");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"8. Show Liked Songs");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"9. Show Song Ratings");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"10. Exit this Menu");
            System.out.println(cc.BLUE_BOLD_BRIGHT+"====================================="+cc.RESET);

            System.out.print(cc.YELLOW+"Enter your choice: ");
            int choice = getValidatedInt();

            handleUserChoice(choice);

            if (choice == 9) {
                break;
            }
        }
    }

    public static void handleUserChoice(int choice) {
        switch (choice) {
            case 1 :
                pauseSong();
                System.out.println(cc.PURPLE+"Song Paused.");
                break;
            case 2 :
                resumeSong();
                System.out.println(cc.PURPLE+"Song Resumed.");
                break;
            case 3 :
                playNextSong();
                break;
            case 4 :
                playPreviousSong();
                break;
            case 5 :
                shufflePlaylist();
                break;
            case 6:
                downloadSong();
                break;
            case 7 :
                promptSongActions();
                break;
            case 8 :
                displayAndPlayLikedSongs();
                break;
            case 9 :
                displayAndPlaySongRatings();
                break;
            case 10 :
                exitMenu();
                break;
            default :
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice."+cc.RESET);
                break;
        }
    }

    //method of playing song by providing path
    public static void playSong(String path) {
        try {
            if (player != null) {
                player.close();
            }

            if (playThread != null) {
                playThread.interrupt();
            }

            // Reset state variables for the new song
            isPaused = false;

            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);

            playThread = new Thread(() -> {
                try {
                    System.out.println(cc.CYAN+"Playing: " + path+cc.RESET);
                    while (!player.isComplete() && !Thread.currentThread().isInterrupted()) {
                        synchronized (lock) {
                            if (isPaused) {
                                lock.wait();
                            }
                        }
                        player.play(1); // play frame by frame
                    }
                } catch (Exception e) {
//                    System.out.println(cc.RED_BOLD_BRIGHT+"Error playing song: " + e.getMessage()+cc.RESET);
                }
            });
            playThread.start();
            showSongfunctions();
        } catch (Exception e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"Error initializing player: " + e.getMessage()+cc.RESET);
        }
    }

    //pause song
    private static void pauseSong() {
        if (playThread != null && playThread.isAlive()) {
            isPaused = true;
        }
    }

    //resume song from where it stop in previous
    private static void resumeSong() {
        if (isPaused) {
            synchronized (lock) {
                isPaused = false;
                lock.notify();
            }
        }
    }

    //play next song from current playing list
    private static void playNextSong() {
        if (playlist.isEmpty()) {
            System.out.println(cc.PURPLE+"Playlist is empty."+cc.RESET);
            return;
        }
        currentSongIndex = playlist.getNextIndex(currentSongIndex);
        addToRecentlyPlayed(currentUserId, getSongIdByPath(playlist.getByIndex(currentSongIndex)));
        System.out.println(cc.GREEN+"Playing Next Song.");
        playSong(playlist.getByIndex(currentSongIndex));
    }

    //play previous song from current playing list
    private static void playPreviousSong() {
        if (playlist.isEmpty()) {
            System.out.println(cc.RED_BOLD+"Playlist is empty.");
            return;
        }
        currentSongIndex=playlist.getPrevIndex(currentSongIndex);
        addToRecentlyPlayed(currentUserId, getSongIdByPath(playlist.getByIndex(currentSongIndex)));
        System.out.println(cc.PURPLE+"Playing Previous Song.");
        playSong(playlist.getByIndex(currentSongIndex));
    }

    //shuffle current playing list
    public static void shufflePlaylist() {
        if (playlist.isEmpty()) {
            System.out.println(cc.RED_BOLD+"Playlist is empty.");
            return;
        }
        playlist.shuffleList();
        currentSongIndex = 0;
        System.out.println(cc.PURPLE+"Playlist Shuffled.");
        playSong(playlist.getByIndex(currentSongIndex));
    }

    //download current playing song
    public static void downloadSong() {
        try {
            String path = playlist.getByIndex(currentSongIndex);
            FileInputStream fis = new FileInputStream(new File(path));
            System.out.println("Enter path where you want to download.");
            String splitPath[]=path.split("\\\\");
            String dpath= sc.nextLine();
            while (dpath.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"Path must not be empty,Please enter a path.");
                dpath = sc.nextLine();
            }
            dpath+="\\"+splitPath[splitPath.length-1];
            FileOutputStream fos = new FileOutputStream(new File(dpath));
            byte[] b = new byte[(int) fis.getChannel().size()];
            fis.read(b);
            fos.write(b);
            fis.close();
            fos.close();
            System.out.println(cc.GREEN+"Song Downloaded successfully."+cc.RESET);
        }catch (Exception e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"Error downloading song: " + e.getMessage()+cc.RESET);
        }
    }

    //prompt song actions (like,rate,add to playlist)
    public static void promptSongActions() {
        String currentPath = playlist.getByIndex(currentSongIndex);
        int songId = getSongIdByPath(currentPath);

        System.out.println("\nWhat would you like to do for this song?");
        System.out.println("1. Like Song");
        System.out.println("2. Rate Song");
        System.out.println("3. Add to Playlist");
        System.out.print("Enter your choice: ");
        int action = getValidatedInt();

        switch (action) {
            case 1 :
                likeSong(currentUserId, songId);
                break;

            case 2 :
                rateSong(currentUserId, songId);
                break;

            case 3 :
                addToPlaylist(currentUserId, songId);
                break;

            default :
                System.out.println("❌ Invalid choice.");
                break;
        }
    }

    //like song current playing song
    public static void likeSong(int userId, int songId) {
        try {
            String sql="select * from liked_songs where userId = ? and song_Id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println(cc.YELLOW+"You have already liked this song."+cc.RESET);
                return;
            }
            sql = "INSERT IGNORE INTO liked_songs (userId, song_id) VALUES (?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(cc.GREEN+"🎵 Song liked successfully!"+cc.RESET);
                sql="UPDATE songs set no_likes=no_likes + 1 WHERE song_id=?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, songId);
                ps.executeUpdate();
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Something went wrong."+cc.RESET);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error while liking song: " + e.getMessage()+cc.RESET);
        }
    }

    //rate current playing song
    public static void rateSong(int userId, int songId) {
        try {
            String check="Select * from ratings where userId = ? and song_Id = ?";
            PreparedStatement check01 = con.prepareStatement(check);
            check01.setInt(1, userId);
            check01.setInt(2, songId);
            ResultSet check02 = check01.executeQuery();
            if (check02.next()) {
                System.out.println(cc.YELLOW+"You have already rated this song."+cc.RESET);
                return;
            }
            System.out.print(cc.CYAN+"Enter rating (1 to 5): "+cc.RESET);
            double rating=0;
            while (true) {
                try {
                    rating = sc.nextDouble();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(cc.RED_BOLD_BRIGHT+"Ratings must be in numerical!"+cc.RESET);
                    sc.nextLine();
                }
            }
            sc.nextLine();
            String sql = "INSERT INTO ratings (userId, song_id, rating) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.setDouble(3, rating);
            int r=ps.executeUpdate();
            if (r>0) {
                sql = "select avg(rating) from ratings where song_id=?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, songId);
                ResultSet rs = ps.executeQuery();
                double ratingAvg=0;
                if (rs.next()) {
                    ratingAvg = rs.getDouble(1);
                }
                sql="UPDATE songs set rating=? WHERE song_id=?";
                ps = con.prepareStatement(sql);
                ps.setDouble(1, ratingAvg);
                ps.setInt(2, songId);
                ps.executeUpdate();
                System.out.println(cc.GREEN+"⭐ Song rated successfully as " + rating + "/5."+cc.RESET);
            }else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Something went wrong."+cc.RESET);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error while rating song: " + e.getMessage()+cc.RESET);
        }
    }

    //add to playlist the current playing song
    public static void addToPlaylist(int userId, int songId) {
        try {
            int playlistId = -1;

            String fetchPlaylistSql = "SELECT playlist_id FROM playlist WHERE userId = ?";
            PreparedStatement ps1 = con.prepareStatement(fetchPlaylistSql);
            ps1.setInt(1, userId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                playlistId = rs.getInt("playlist_id");
            } else {
                String defaultTitle = "Default Playlist " + userId;
                String createPlaylistSql = "INSERT INTO playlist (playlist_title, userId) VALUES (?, ?)";
                PreparedStatement ps2 = con.prepareStatement(createPlaylistSql);
                ps2.setString(1, defaultTitle);
                ps2.setInt(2, userId);
                ps2.executeUpdate();

                Statement st=con.createStatement();
                ResultSet rs1=st.executeQuery("select max(playlist_id) from playlist");
                if (rs1.next()) {
                    playlistId = rs1.getInt(1);
                    System.out.println("✅ Created new playlist: " + defaultTitle + " (ID: " + playlistId + ")");
                }
            }

            String insertSongSql = "INSERT IGNORE INTO playlist_songs (playlist_id,song_id) VALUES (?,?)";
            PreparedStatement ps3 = con.prepareStatement(insertSongSql);
            ps3.setInt(1, playlistId);
            ps3.setInt(2, songId);
            int rows = ps3.executeUpdate();

            if (rows > 0) {
                System.out.println("🎶 Song added to your playlist successfully!");
            } else {
                System.out.println("ℹ️ Song already exists in your playlist.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error while adding song to playlist: " + e.getMessage());
        }
    }

    //display the liked song by current user
    public static void displayAndPlayLikedSongs() {
        try {
            String sql = """
                SELECT s.song_id,s.audio_path, s.title
                FROM liked_songs ls
                JOIN songs s ON ls.song_id = s.song_id
                WHERE ls.userId = ?
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> temp=playlist.getAll();
            playlist.clear();

            int index = 0;
            System.out.println("\n"+cc.CYAN+"💖 Liked Songs:");
            while (rs.next()) {
                index++;
                System.out.println(cc.PURPLE+index + ". " + rs.getString("title"));
                songIds.add(rs.getInt("song_id"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.YELLOW+"You haven't liked any songs yet.");
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
                return;
            }

            System.out.print(cc.CYAN+"Enter the number of the song to play, or 0 to go back: ");
            int choice = getValidatedInt();

            if (choice == 0) {
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
            } else if (choice > 0 && choice <= songIds.size()) {
                currentSongIndex=choice - 1;
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error fetching liked songs: " + e.getMessage());
        }
    }

    //display songs that rated by current user
    public static void displayAndPlaySongRatings() {
        try {
            String sql = """
                SELECT s.song_id,s.audio_path, s.title, r.rating
                FROM ratings r
                JOIN songs s ON r.song_id = s.song_id
                WHERE r.userId = ?
                ORDER BY r.rating DESC
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> temp=playlist.getAll();
            playlist.clear();

            int index = 0;
            System.out.println(cc.YELLOW+"\n⭐ Your Rated Songs:");
            while (rs.next()) {
                index++;
                System.out.println(cc.CYAN+index + ". " + rs.getString("title") + " - Rated: " + rs.getDouble("rating") + "/5");
                songIds.add(rs.getInt("song_id"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.YELLOW+"You haven't rated any songs yet.");
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
                return;
            }

            System.out.print(cc.CYAN+"Enter the number of the song to play, or 0 to go back: ");
            int choice = getValidatedInt();

            if (choice == 0) {
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
            } else if (choice > 0 && choice <= songIds.size()) {
                currentSongIndex=choice - 1;
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                playlist.clear();
                for (String path : temp) {
                    playlist.addToList(path);
                }
                showSongfunctions();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error fetching rated songs: " + e.getMessage());
        }
    }

    //exit
    private static void exitMenu() {
        System.out.println(cc.CYAN+"\nWhere would you like to go ? Enter your choice");
        System.out.println("1. Exit the Application");
        System.out.println("2. Back to Main Menu ");

        System.out.print("Enter your choice: ");
        int nextChoice = getValidatedInt();

        switch (nextChoice) {
            case 1:
                System.out.println("👋 Exiting the Music App. Thank you!");
                System.exit(0);
            case 2:
                MusicApp1.appMainMenu();
                break;
            default :
                System.out.println("Invalid choice. Staying in current menu.");
                showSongfunctions();
                break;
        }
    }

    //------------------------------------------------------------------------------------
    //--------------main methods---------------------------------------------

    //Search by type of songs
    public void searchSongByType() {
        System.out.println(cc.YELLOW+"🔎 Searching for a Song.");
        System.out.print("Enter Song Type (e.g. Pop, Romantic): ");
        String songTypeName = sc.nextLine().trim();

        try {
            String sql = "SELECT song_id, title, audio_path FROM songs WHERE song_type LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + songTypeName + "%");
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> songTitles = new ArrayList<>();
            playlist.clear();

            int index = 0;
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("title"));
                songIds.add(rs.getInt("song_id"));
                songTitles.add(rs.getString("title"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ No songs found with that Type.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.YELLOW+"Enter the number of the song you want to play (or 0 to return to Main Menu): ");
            int choice = getValidatedInt();
            currentSongIndex=choice - 1;

            if (choice == 0) {
                MusicApp1.appMainMenu();
            } else if (choice >= 1 && choice <= songIds.size()) {
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error while searching song: " + e.getMessage());
            MusicApp1.appMainMenu();
        }
    }

    //explore by singer
    public void exploreBySinger() {
        System.out.println(cc.PURPLE+"🎤 Exploring Songs by Singer.");
        System.out.print("Enter Singer Name (leave blank to show all songs): ");
        String singerName = sc.nextLine().trim();

        List<String> songTitles = new ArrayList<>();
        playlist.clear();

        try {
            String sql;
            PreparedStatement pst;

            if (singerName.isEmpty()) {
                sql = "SELECT title, audio_path FROM songs";
                pst = con.prepareStatement(sql);
            } else {
                sql = "SELECT title, audio_path " +
                        "FROM songs " +
                        "JOIN artist_data ON songs.artist_id = artist_data.artist_id " +
                        "WHERE LOWER(TRIM(name)) like '%"+singerName.toLowerCase()+"%'";
                pst = con.prepareStatement(sql);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String path = rs.getString("audio_path");
                songTitles.add(title);
                playlist.addToList(path);
            }

            // If no songs found for given singer, show all songs
            if (playlist.isEmpty() && !singerName.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ No songs found for '" + singerName + "'. Showing all songs instead.\n");

                sql = "SELECT title, audio_path FROM songs";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String title = rs.getString("title");
                    String path = rs.getString("audio_path");
                    songTitles.add(title);
                    playlist.addToList(path);
                }

                if (playlist.isEmpty()) {
                    System.out.println("⚠️ No songs available in the library.");
                    return;
                }

                System.out.println(cc.CYAN_BOLD_BRIGHT+"\n--- All Available Songs ---");
            } else {
                String heading = singerName.isEmpty() ? "All Available Songs" : "Songs by " + singerName;
                System.out.println("\n--- " +cc.CYAN+ heading + " ---");
            }

            for (int i = 0; i < songTitles.size(); i++) {
                System.out.println((i + 1) + ". " + songTitles.get(i));
            }

            System.out.print("Enter the number of the song to play: ");
            int songChoice = getValidatedInt();
            if (songChoice < 1 || songChoice > playlist.size()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ Invalid song choice.");
                MusicApp1.appMainMenu();
                return;
            } else {
                currentSongIndex=songChoice - 1;
                String selectedPath = playlist.getByIndex(currentSongIndex);
                int songId = getSongIdByPath(selectedPath);

                if (songId != -1) {
                    addToRecentlyPlayed(currentUserId, songId);  // ✅ Track as recently played
                } else {
                    System.out.println(cc.RED_BOLD_BRIGHT+"❌ Unable to find song ID for the selected song.");
                }

                playSong(selectedPath);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"⚠️ Error fetching songs: " + e.getMessage());
        }

        showSongfunctions(); // back to main menu
    }

    //songs of playlists
    public void displayAndPlayPlaylists() {
        try {
            // Step 1: Show all playlists by current user
            String sql = "SELECT playlist_id, playlist_title FROM playlist WHERE userId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            List<Integer> playlistIds = new ArrayList<>();
            System.out.println(cc.PURPLE+"\n🎵 Your Playlists:");
            int index = 0;
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("playlist_title"));
                playlistIds.add(rs.getInt("playlist_id"));
            }

            if (playlistIds.isEmpty()) {
                System.out.println(cc.YELLOW+"You have no playlists.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.CYAN+"Enter the number of the playlist to view songs, or 0 to go back: ");
            int playlistChoice = getValidatedInt();

            if (playlistChoice == 0) {
                MusicApp1.appMainMenu();
                return;
            } else if (playlistChoice < 1 || playlistChoice > playlistIds.size()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid playlist choice.");
                showSongfunctions();
                return;
            }

            int selectedPlaylistId = playlistIds.get(playlistChoice - 1);

            // Step 2: Show songs in the selected playlist
            String songSql = "SELECT songs.song_id, title, songs.audio_path "+
                "FROM playlist_songs "+
                "JOIN songs ON playlist_songs.song_id = songs.song_id "+
                "WHERE playlist_id = ? ";
            ps = con.prepareStatement(songSql);
            ps.setInt(1, selectedPlaylistId);
            rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            playlist.clear();

            System.out.println(cc.YELLOW+"\n🎶 Songs in the selected Playlist:");
            index = 0;
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("title"));
                songIds.add(rs.getInt("song_id"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.YELLOW+"No songs found in this playlist.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.PURPLE+"Enter the number of the song to play, or 0 to go back: ");
            int songChoice = getValidatedInt();

            if (songChoice == 0) {
                MusicApp1.appMainMenu();
            } else if (songChoice >= 1 && songChoice <= songIds.size()) {
                currentSongIndex=songChoice-1;
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                if (path != null) {
                    addToRecentlyPlayed(currentUserId, songId);
                    playSong(path);
                } else {
                    System.out.println(cc.RED_BOLD_BRIGHT+"Song path not found.");
                    showSongfunctions();
                }
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error displaying playlists: " + e.getMessage());
        }
    }

    //get id by entering path
    public static int getSongIdByPath(String path) {
        int songId = -1;
        try {
            String sql = "SELECT song_id FROM songs WHERE audio_path = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, path);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                songId = rs.getInt("song_id");
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ No song found for the given path.");
            }
        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error fetching song ID: " + e.getMessage());
        }
        return songId;
    }

    //display and play recently played songs
    public void displayAndPlayRecentlyPlayed() {
        try {
            String sql = """
                SELECT s.song_id,s.audio_path, s.title, COUNT(rp.song_id) AS play_count
                FROM recently_played rp
                JOIN songs s ON rp.song_id = s.song_id
                WHERE rp.userId = ?
                GROUP BY rp.song_id
                ORDER BY play_count DESC
                """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> temp=playlist.getAll();
            playlist.clear();

            System.out.println(cc.GREEN+"\n🎧 Recently Played Songs:");
            int index = 0;
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("title") + " - Played: " + rs.getInt("play_count"));
                songIds.add(rs.getInt("song_id"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.YELLOW+"No recently played songs.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.CYAN+"Enter the number of the song to play, or 0 to go back: ");
            int choice = getValidatedInt();

            if (choice == 0) {
                MusicApp1.appMainMenu();
            } else if (choice > 0 && choice <= songIds.size()) {
                currentSongIndex=choice - 1;
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error fetching recently played: " + e.getMessage());
            MusicApp1.appMainMenu();
        }
    }

    //generate list of random 10 songs
    public void playRandomSong() {
        playlist.clear();
        try {
            String sql = "SELECT song_id, title, audio_path FROM songs ORDER BY RAND() LIMIT 10";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println(cc.PURPLE+"🎵 Generated some Random Songs from All Songs: ");
            while (rs.next()) {
                System.out.println("-->  "+rs.getString("title"));
                String path = rs.getString("audio_path");
                playlist.addToList(path);
            }
            currentSongIndex=0;
            addToRecentlyPlayed(currentUserId, getSongIdByPath(playlist.getByIndex(currentSongIndex)));
            playSong(playlist.getByIndex(currentSongIndex));

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error playing random song: " + e.getMessage());
            MusicApp1.appMainMenu();
        }
    }

    //generate song by name
    public void searchSongByName() {
        System.out.println(cc.YELLOW+"🔎 Searching for a Song.");
        System.out.print("Enter Song Name (or part of it): ");
        String songName = sc.nextLine().trim();

        try {
            String sql = "SELECT song_id, title, audio_path FROM songs WHERE title LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + songName + "%");
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> songTitles = new ArrayList<>();
            playlist.clear();

            int index = 0;
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("title"));
                songIds.add(rs.getInt("song_id"));
                songTitles.add(rs.getString("title"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ No songs found with that name.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.YELLOW+"Enter the number of the song you want to play (or 0 to return to Main Menu): ");
            int choice = getValidatedInt();
            currentSongIndex=choice - 1;

            if (choice == 0) {
                MusicApp1.appMainMenu();
            } else if (choice >= 1 && choice <= songIds.size()) {
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error while searching song: " + e.getMessage());
            MusicApp1.appMainMenu();
        }
    }

    //generate list of 10 trending songs
    public void showTrendingSongs() {
        try {
            String sql = "SELECT s.song_id, s.title, s.audio_path, AVG(r.rating) AS avg_rating "+
                "FROM ratings r "+
                "JOIN songs s ON r.song_id = s.song_id "+
                "GROUP BY s.song_id "+
                "ORDER BY avg_rating DESC "+
                "LIMIT 10 ";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            playlist.clear();
            int index = 0;

            System.out.println(cc.CYAN+"\n🔥 Trending Songs Right Now (Based on Ratings):");
            while (rs.next()) {
                index++;
                System.out.println(index + ". " + rs.getString("title") + " | Average Rating: " + rs.getDouble("avg_rating"));
                songIds.add(rs.getInt("song_id"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"No trending songs available.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.YELLOW+"Enter the number of the song you want to play (or 0 to return to Main Menu): ");
            int choice = getValidatedInt();
            currentSongIndex=choice - 1;

            if (choice == 0) {
                MusicApp1.appMainMenu();
            } else if (choice >= 1 && choice <= songIds.size()) {
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error fetching trending songs: " + e.getMessage());
            MusicApp1.appMainMenu();
        }
    }

    //generate songs by movie name
    public void searchSongByMovie() {
        System.out.println(cc.PURPLE+"🔎 Searching for a Song.");
        System.out.print("Enter movie Name (or part of it): ");
        String movieName = sc.nextLine().trim();

        if (movieName.isEmpty()) {
            System.out.println(cc.RED_BOLD_BRIGHT+"No movie name found.");
            MusicApp1.appMainMenu();
            return;
        }

        try {
            String sql = "SELECT song_id, title, audio_path FROM songs join movie_data "
                    +"on songs.movie_id=movie_data.movie_id WHERE movie_name LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + movieName + "%");
            ResultSet rs = ps.executeQuery();

            List<Integer> songIds = new ArrayList<>();
            List<String> songTitles = new ArrayList<>();
            playlist.clear();

            int index = 0;
            while (rs.next()) {
                index++;
                System.out.println(cc.CYAN+index + ". " + rs.getString("title"));
                songIds.add(rs.getInt("song_id"));
                songTitles.add(rs.getString("title"));
                playlist.addToList(rs.getString("audio_path"));
            }

            if (songIds.isEmpty()) {
                System.out.println(cc.RED_BOLD_BRIGHT+"❌ No songs found with that movie name.");
                MusicApp1.appMainMenu();
                return;
            }

            System.out.print(cc.YELLOW+"Enter the number of the song you want to play (or 0 to return to Main Menu): ");
            int choice = getValidatedInt();
            currentSongIndex=choice - 1;

            if (choice == 0) {
                MusicApp1.appMainMenu();
            } else if (choice >= 1 && choice <= songIds.size()) {
                int songId = songIds.get(currentSongIndex);
                String path = playlist.getByIndex(currentSongIndex);
                addToRecentlyPlayed(currentUserId, songId);
                playSong(path);
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice.");
                MusicApp1.appMainMenu();
            }

        } catch (SQLException e) {
            System.out.println(cc.RED_BOLD_BRIGHT+"❌ Error while searching song: " + e.getMessage());
        }
    }
}

class PlayingList {
    class Node {
        String path;
        Node next;

        Node(String path) {
            this.path = path;
            this.next = null;
        }
    }

    Node first=null;

    void addToList(String path) {
        Node new_node = new Node(path);
        if (first == null) {
            first = new_node;
            first.next = first;
        } else {
            Node temp = first;
            while (temp.next != first) {
                temp = temp.next;
            }
            temp.next = new_node;
            new_node.next = first;
        }
    }
    
    void clear() {
        first=null;
    }

    boolean isEmpty() {
        if (first == null) {
            return true;
        } else {
            return false;
        }
    }

    int size() {
        if (first == null) {
            return 0;
        } else {
            int count = 0;
            Node temp = first;
            do {
                count++;
                temp = temp.next;
            } while (temp != first);
            return count;
        }
    }

    void shuffleList() {
        ArrayList<String> arr = new ArrayList<>();
        Node temp = first;
        do {
            arr.add(temp.path);
            temp = temp.next;
        }while (temp != first);
        first = null;
        Collections.shuffle(arr);
        for (String s : arr) {
            addToList(s);
        }
    }

    List<String> getAll(){
        ArrayList<String> arr = new ArrayList<>();
        if (first == null) {
            return arr;
        }else {
            Node temp = first;
            do {
                arr.add(temp.path);
                temp = temp.next;
            }while (temp != first);
            return arr;
        }
    }

    String getByIndex(int index) {
        if (first == null) {
            return null;
        } else {
            Node temp = first;
            for (int i = 0; i < index;i++) {
                temp = temp.next;
            }
            return temp.path;
        }
    }

    int getNextIndex(int currentIndex){
        return (currentIndex + 1)%size();
    }

    int getPrevIndex(int currentIndex){
        if (currentIndex == 0) {
            return size()-1;
        }else {
            return currentIndex-1;
        }
    }
}
