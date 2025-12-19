package MusicApp1;

import java.sql.*;

public class AdminControl {

    
    static Connection con;
    
    static ColoursCode cc = new ColoursCode();

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listener's_hub", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean adminLogin(String username, String password) {
        if (username.equals("Admin") && password.equals("Admin#123")) {
            logAdminAction("Admin logged in");
            return true;
        }
        return false;
    }

    private static void logAdminAction(String action) {
        try {
            String sql = "INSERT INTO admin_actions (action_detail, action_time) VALUES (?, NOW())";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, action);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(cc.RED+"Failed to log admin action: " + e.getMessage()+cc.RESET);
        }
    }

    public void displayAdminAction() {
        try {
            logAdminAction("Displayed admin actions");
            String sql = "{call getAdminActions()}";
            CallableStatement cst = con.prepareCall(sql);
            ResultSet rs = cst.executeQuery(sql);
            while (rs.next()) {
                System.out.println(cc.WHITE+"Action: "+rs.getString(1)+" ,Time: "+rs.getString(2)+cc.RESET);
            }
        }catch (SQLException e) {
            System.out.println(cc.RED+"Error fetching admin actions: " + e.getMessage()+cc.RESET);
        }
    }

    public void displayAllUsers() {
        try {
            String sql = "{call getAllUsers()}";
            CallableStatement cst = con.prepareCall(sql);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                System.out.println(cc.CYAN+"User ID: " + rs.getInt("userId") + ", Username: " + rs.getString("username")+cc.RESET);
            }
            logAdminAction("Displayed all users");
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error fetching users: " + e.getMessage()+cc.RESET);
        }
    }

    public void removeUser(int userId) {
        try {
            String sql = "DELETE FROM user WHERE userId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction("Removed user with ID: " + userId);
                MusicApp1.users.deleteParticularUser(userId);
                System.out.println(cc.GREEN+"User removed successfully."+cc.RESET);
            } else {
                System.out.println(cc.YELLOW+"User not found."+cc.RESET);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error removing user: " + e.getMessage()+cc.RESET);
        }
    }

    public void displayAllSongs() {
        try {
            String sql = "{call getAllSongs()}";
            CallableStatement cst = con.prepareCall(sql);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                System.out.println(cc.PURPLE+"Song ID: " + rs.getInt("song_id") + ", Title: " + rs.getString("title")+cc.RESET);
            }
            logAdminAction("Displayed all songs");
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error fetching songs: " + e.getMessage()+cc.RESET);
        }
    }

    public void addNewSong(String title, String audioPath, int artistId, int movie_id, String song_type) {
        try {
            String sql = "INSERT INTO songs (title, audio_path, artist_id, Movie_id, song_type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, audioPath);
            pst.setInt(3, artistId);
            pst.setInt(4, movie_id);
            pst.setString(5, song_type);
            pst.executeUpdate();
            logAdminAction("Added new song: " + title);
            System.out.println(cc.GREEN+"Song added successfully."+cc.RESET);
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error adding song: " + e.getMessage()+cc.RESET);
        }
    }

    public void removeSong(int songId) {
        try {
            String sql = "DELETE FROM songs WHERE song_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, songId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction("Removed song with ID: " + songId);
                System.out.println(cc.GREEN+"Song removed successfully."+cc.RESET);
            } else {
                System.out.println(cc.YELLOW+"Song not found."+cc.RESET);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error removing song: " + e.getMessage()+cc.RESET);
        }
    }

    public void displayAllArtists(){
        try {
            String sql = "{call getAllArtists()}";
            CallableStatement cst = con.prepareCall(sql);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                System.out.println(cc.PURPLE+"Artist ID: " + rs.getInt("artist_id") + ", Name: " + rs.getString("name")+cc.RESET);
            }
            logAdminAction("Displayed all artists");
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error fetching artists: " + e.getMessage()+cc.RESET);
        }
    }

    public void addNewArtist(String name) {
        try {
            if (name.isEmpty()) {
                System.out.println(cc.YELLOW+"Artist name is empty"+cc.RESET);
                return;
            }
            String sql = "INSERT INTO artist_data (name) VALUES (?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            int n=pst.executeUpdate();
            if (n>0) {
                logAdminAction("Added new artist: " + name);
                System.out.println(cc.GREEN+"Artist added successfully."+cc.RESET);
            }else {
                System.out.println(cc.BLACK+"No such artist added:"+cc.RESET);
            }
        } catch (SQLException e) {
            System.out.println(cc.RED+"Error adding artist: " + e.getMessage()+cc.RESET);
        }
    }

    public void removeArtist(int artistId) {
        try {
            String sql = "DELETE FROM artist_data WHERE artist_id = ?";
            PreparedStatement pst= con.prepareStatement(sql);
            pst.setInt(1, artistId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected>0) {
                logAdminAction("Removed artist with ID: " + artistId);
                System.out.println(cc.GREEN+"Artist removed successfully."+cc.RESET);
            }else  {
                System.out.println(cc.BLACK+"No such artist removed:"+cc.RESET);
            }
        }catch (SQLException e) {
            System.out.println(cc.RED+"Error removing artist: " + e.getMessage()+cc.RESET);
        }
    }

    public void displayMovies() {
        try {
            String sql = "{call getAllMovies()}";
            CallableStatement cst = con.prepareCall(sql);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                System.out.println(cc.PURPLE+"Movie ID: " + rs.getInt("movie_id")+", Movie Name: " + rs.getString("movie_name")+cc.RESET);
            }
            logAdminAction("Displayed all movies");
        }catch (SQLException e) {
            System.out.println(cc.RED+"Error fetching movies: " + e.getMessage()+cc.RESET);
        }
    }

    public void addNewMovie(String name,int releaseYear) {
        try {
            if (name.isEmpty()) {
                System.out.println(cc.YELLOW+"Movie name is empty"+cc.RESET);
                return;
            }
            String sql = "INSERT INTO movie_data (movie_name,release_year) VALUES (?,?)";
            PreparedStatement pst= con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, releaseYear);
            int n=pst.executeUpdate();
            if (n>0) {
                logAdminAction("Added new movie: " + name);
                System.out.println(cc.GREEN+"Movie added successfully."+cc.RESET);
            }
        }catch (SQLException e) {
            System.out.println(cc.RED+"Error to inserting movie: " + e.getMessage()+cc.RESET);
        }
    }

    public void removeMovie(int movieId) {
        try {
            String sql = "DELETE FROM movie_data WHERE movie_id = ?";
            PreparedStatement pst= con.prepareStatement(sql);
            pst.setInt(1, movieId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected>0) {
                logAdminAction("Removed movie with ID: " + movieId);
                System.out.println(cc.GREEN+"Movie removed successfully."+cc.RESET);
            }
        }catch (SQLException e) {
            System.out.println(cc.RED+"Error in deleting movie: " + e.getMessage()+cc.RESET);
        }
    }
}
