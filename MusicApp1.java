package MusicApp1;
import java.io.File;
import java.sql.*;
import java.util.*;

public class MusicApp1 {
    //establishing connection + instance Collections:
    static Connection con;
    static User users = new User();
    static Scanner sc = new Scanner(System.in);
    private static int useridCounter;

    static ColoursCode cc=new ColoursCode();
    static MusicAppFunctionalities mf=new MusicAppFunctionalities();

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listener's_hub", "root", "");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   //validate user input as integer
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

    //generating users from database:
    static void InsertUserfromDB() {
        try {
            String uplodData = "{call getAllUsers()}";
            CallableStatement ud = con.prepareCall(uplodData);
            ResultSet rs = ud.executeQuery();
            while (rs.next()) {
                users.insertAtLast( new SignUpInfo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //generate max user id from Database to continue order of increment
    static {
        try {
            String q1 = "{call getMaxId()}";
            CallableStatement cst = con.prepareCall(q1);
            ResultSet rs = cst.executeQuery();
            if (rs.next()) {
                useridCounter = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //generating user-id:
    public static int generateUserid() {
        return useridCounter + 1;
    }
    //add user in linked list:
    public static void addUser(String username, String email, String password, String gender, String mobileno) {
        try {
            int userIDS = generateUserid();
            SignUpInfo newUser = new SignUpInfo(userIDS, username, email, password, gender, mobileno);

            // Insert into DB
            insertUserToDB(newUser);

            //Insertion in HashMap
            users.insertAtLast(newUser);
            System.out.println("User addded: " + users.findById(userIDS).getUsername() + " And Your user-Id for Login is: " + userIDS);

        } catch (Exception e) {
            System.out.println("User Sign-Up Failed! Try Again!");
        }
    }

    //insert user to db:
    public static void insertUserToDB(SignUpInfo user) {
        try {
            String sql = "INSERT INTO user (userId, username, email, passwordHash, gender, mobileNumber) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getUserId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getMobileNumber());
            //executing:
            ps.executeUpdate();
            System.out.println("User inserted into DataBase successfully!");
            ps.close();
        } catch (Exception e) {
            System.out.println("DataBase Insertion Error: " + e.getMessage());
        }
    }

    //------------------------login- methods---------------------------------
    private static boolean loginUser() {
        String password = "";
        boolean check = false;      //for check login successful or not
        boolean loop=true;
        while (loop) {
            //giving options for login either with email or UserId
            System.out.println(cc.YELLOW+"How would you like to login?");
            System.out.println("Press 1 to login with userId:");
            System.out.println("Press 2 to login with email:");
            int login=getValidatedInt();
            //
            if (login==1) {
                System.out.print(cc.YELLOW+"Enter User ID: ");
                int userId = getValidatedInt();

                if (users.containsId(userId)) {
                    System.out.print("Enter Password: ");
                    password = sc.nextLine();
                    if (users.findById(userId).getPasswordHash().equals(password)) {
                        MusicAppFunctionalities.currentUserId=userId;
                        System.out.println(cc.PURPLE_BOLD_BRIGHT+"Welcome to the Listener's Hub, " + users.findById(userId).getUsername() + "!");
                        check = true;
                    } else {
                        System.out.println(cc.RED_BOLD_BRIGHT+"Incorrect password.");
                    }
                } else {
                    System.out.println(cc.RED_BOLD_BRIGHT+"User ID not found.");
                }
                loop = false;
            }
            //
            else if (login==2) {
                System.out.println(cc.YELLOW+"Enter Email:");
                String email = sc.nextLine();
                boolean emailfound = false;
                if (users.containsEmail(email)) {
                    emailfound = true;
                    System.out.println("Enter Password:");
                    password = sc.nextLine();
                    if (users.findByEmail(email).getPasswordHash().equals(password)) {
                        MusicAppFunctionalities.currentUserId=users.findByEmail(email).getUserId();
                        System.out.println("User Found.");
                        System.out.println(cc.PURPLE_BOLD_BRIGHT+"Welcome to the Listener's Hub, "+ users.findByEmail(email).getUsername() + "!");
                        check = true;
                    }else {
                        System.out.println(cc.RED_BOLD_BRIGHT+"Wrong Password! Please Try another one!");
                    }
                }
                if (!emailfound) {
                    System.out.println(cc.RED_BOLD_BRIGHT+"No such User Found! Please try another option:!");
                }
                loop = false;
            } else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid Choice! Enter 1 or 2 only!");
            }
        }
        return check;
    }
//-------------------------------------------------------------------------------------------
    //--------------------------sign up methods-----------------------------------------------
    private static void signUpUser() {
        System.out.print(cc.YELLOW+"Enter Username: ");
        String username = sc.nextLine();
        String email;
        while (true) {
            System.out.print("Enter Gmail: ");
            email = sc.nextLine().trim();
            if (MusicApp1.isValidGmail(email))
                break;
            else
                System.out.println(cc.RED_BOLD_BRIGHT+"Invalid Gmail. Format should be username@gmail.com");
        }
        String password;
        System.out.println("Create Password which follows below conditions: ");
        System.out.println("- At least 1 uppercase letter");
        System.out.println("- At least 1 lowercase letter");
        System.out.println("- At least 1 number");
        System.out.println("- At least 1 special character");
        System.out.println("- Minimum length of 8 characters");
        while (true) {
            password = sc.nextLine();
            if (MusicApp1.isValidPassword(password))
                break;
            else {
                System.out.println(cc.RED_BOLD_BRIGHT+"Weak password! Ensure it has:");
                System.out.println("- At least 1 uppercase letter");
                System.out.println("- At least 1 lowercase letter");
                System.out.println("- At least 1 number");
                System.out.println("- At least 1 special character");
                System.out.println("- Minimum length of 8 characters");
            }
        }
        System.out.print(cc.YELLOW+"Enter Gender: ");
        String gender = sc.nextLine();
        System.out.print("Enter Mobile Number: ");
        String mobile;
        while (true) {
            mobile=sc.nextLine();
            if (isValidMobileNo(mobile)){
                break;
            }
        }
        MusicApp1.addUser(username, email, password, gender, mobile);
    }

    //gmail validation!
    public static boolean isValidGmail(String email) {
        if (email == null || email.isEmpty())
            return false;
        int atPosition = email.indexOf('@');
        if (atPosition <= 0 || atPosition == email.length() - 1 ) {
            return false;  // '@' at start or end or not found
        }
        String username = email.substring(0, atPosition);
        String domain = email.substring(atPosition + 1,email.length());
        if (!username.isEmpty() && domain.equals("gmail.com")) {
            return true;
        }else {
            return false;
        }
    }

    //password Validation:
    public static boolean isValidPassword(String password) {
        if (password.length() < 8)
            return false;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (int i=0; i<password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch))
                hasUpper = true;
            else if (Character.isLowerCase(ch))
                hasLower = true;
            else if (Character.isDigit(ch))
                hasDigit = true;
            else
                hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    //mobile number validation
    public static boolean isValidMobileNo(String mobileNo) {
        if (mobileNo.length() != 10) {
            System.out.println("Mobile no must have 10 digits!");
            return false;
        }
        boolean validMobileNo = true;
        for (int i=0; i<mobileNo.length(); i++) {
            char ch = mobileNo.charAt(i);
            if (!Character.isDigit(ch)){
                System.out.println("Characters not allowed in mobile no,please enter only digits:");
                validMobileNo = false;
                break;
            }
            if ((i==0) && (ch != '6' && ch != '7' && ch != '8' && ch != '9')) {
                System.out.println("First digit of Mobile number is must be 6,7,8 or 9 in India");
                validMobileNo = false;
                break;
            }
        }
        return validMobileNo;
    }
    //-------------------------------------------------sign up methods ends-------------------------------

    //--------------------------------------admin panel methods--------------------------------------
    private static void adminPanel() {
        AdminControl ac = new AdminControl();
        System.out.print("Enter Admin Username: ");
        String adminUser = sc.nextLine();
        System.out.print("Enter Admin Password: ");
        String adminPass = sc.nextLine();

        if (ac.adminLogin(adminUser, adminPass)) {
            boolean adminActive = true;
            while (adminActive) {
                System.out.println(cc.CYAN_BOLD_BRIGHT+"\n---- Admin Panel ----");
                System.out.println("1. Display All Users");
                System.out.println("2. Remove User");
                System.out.println("3. Display All Songs");
                System.out.println("4. Add New Song");
                System.out.println("5. Remove Song");
                System.out.println("6. Display All Artists");
                System.out.println("7. Add New Artist");
                System.out.println("8. Remove Artist");
                System.out.println("9. Display All Movies");
                System.out.println("10. Add Movie");
                System.out.println("11. Remove Movie");
                System.out.println("12. Display Admin Actions");
                System.out.println("13. Logout As admin");
                System.out.println("14. Exit the program"+cc.RESET);


                System.out.print(cc.YELLOW+"Enter your choice: ");
                int adminChoice = getValidatedInt();

                switch (adminChoice) {
                    case 1:
                        ac.displayAllUsers();
                        break;
                    case 2:
                        System.out.print(cc.YELLOW+"Enter User ID to remove: ");
                        ac.removeUser(getValidatedInt());
                        break;
                    case 3:
                        ac.displayAllSongs();
                        break;
                    case 4:
                        System.out.println(cc.PURPLE+"Enter title");
                        String title = sc.nextLine();
                        System.out.println("Enter song type");
                        String songType = sc.nextLine();
                        System.out.println("Enter audio path");
                        String audioPath = sc.nextLine();
                        File f=new File(audioPath);
                        if (!f.exists()) {
                            System.out.println(cc.RED_BOLD_BRIGHT+"Any File does not exist at this location");
                            break;
                        }
                        if (!audioPath.toLowerCase().endsWith(".mp3")){
                            System.out.println(cc.RED_BOLD_BRIGHT+"This file path is not of audio file");
                            break;
                        }
                        System.out.println("Enter movie id:");
                        int movie = getValidatedInt();
                        System.out.println("Enter artist id");
                        int artistId = getValidatedInt();
                        ac.addNewSong(title, audioPath, artistId, movie, songType);
                        break;
                    case 5:
                        System.out.print(cc.YELLOW+"Enter Song ID to remove: ");
                        ac.removeSong(getValidatedInt());
                        break;
                    case 6:
                        ac.displayAllArtists();
                        break;
                    case 7:
                        System.out.println(cc.YELLOW+"Enter name of artist: ");
                        ac.addNewArtist(sc.nextLine());
                        break;
                    case 8:
                        System.out.println(cc.YELLOW+"Enter artist ID to remove: ");
                        ac.removeArtist(getValidatedInt());
                        break;
                    case 9:
                        ac.displayMovies();
                        break;
                    case 10:
                        System.out.println(cc.YELLOW+"Enter name of movie: ");
                        String movieName = sc.nextLine();
                        System.out.println("Enter release year");
                        int releaseYear = getValidatedInt();
                        ac.addNewMovie(movieName, releaseYear);
                        break;
                    case 11:
                        System.out.println(cc.YELLOW+"Enter movie ID to remove: ");
                        ac.removeMovie(getValidatedInt());
                        break;

                    case 12:
                        ac.displayAdminAction();
                        break;
                    case 13:
                        System.out.println(cc.GREEN+"Admin Logged Out.");
                        adminActive = false;
                        break;
                    case 14:
                        System.out.println(cc.CYAN_BOLD_BRIGHT+"Exiting Application");
                        System.exit(0);
                    default:
                        System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice. Try again.");
                }
            }
        } else {
            System.out.println(cc.RED_BOLD_BRIGHT+"Invalid Admin Credentials.");
        }
    }
//---------------------------admin panel ends------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) throws Exception{
        System.out.println(cc.GREEN_BOLD_BRIGHT+"======================================================");
        System.out.println("🎵🎶  Welcome to Listener's Hub - Your Music World 🎶🎵");
        System.out.println("======================================================");
        System.out.println();
        System.out.println("🔥 Stream your favorite tracks anytime, anywhere!");
        System.out.println("🎧 Discover new artists, genres & playlists curated for you.");
        System.out.println("🎵 Create, customize, and enjoy your own playlists.");
        System.out.println();
        System.out.println("👉 Let's get started and feel the vibe!");
        System.out.println("======================================================"+cc.RESET);

        //Backup All data from Database
        InsertUserfromDB();

        //---------------------------------------------start logging in -------------------------------------------
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println(cc.YELLOW+"\n1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Admin Login");
            System.out.print("Enter your choice: ");
            int choice = getValidatedInt();
            try {
                switch (choice) {
                    case 1:
                        isLoggedIn = loginUser();
                        break;
                    case 2:
                        signUpUser();
                        break;
                    case 3:
                        adminPanel();
                        break;
                    default:
                        System.out.println(cc.RED_BOLD_BRIGHT+"Invalid choice. Try again!");
                }
            }catch (Exception e){
                System.out.println(cc.RED_BOLD_BRIGHT+"Some unexpected Error occured!");
            }
        }

        //---------------------after succesfull login-------------------------
        //calling method of handling main menu
        appMainMenu();

    }

    public static void appMainMenu(){
        System.out.println(cc.PURPLE_BOLD_BRIGHT+"=====================================");
        System.out.println("🎵 What would you like to do?");
        System.out.println("1. Search Songs by Type");
        System.out.println("2. Explore by Singer");
        System.out.println("3. Play Your Playlist");
        System.out.println("4. Recently Played");
        System.out.println("5. Play a Random Song");
        System.out.println("6. Search Song by Name");
        System.out.println("7. View Trending Songs");
        System.out.println("8. Search Song By Movie name");
        System.out.println("9. Exit the App");
        System.out.println("====================================="+cc.RESET);

        System.out.print(cc.YELLOW+"Enter your choice: ");
        int choice = getValidatedInt();

        switch (choice) {
            case 1:
                mf.searchSongByType();
                break;

            case 2:
                mf.exploreBySinger();
                break;

            case 3:
                System.out.println(cc.YELLOW+"🎧 Playing Your Playlist.");
                mf.displayAndPlayPlaylists();
                break;

            case 4:
                System.out.println(cc.YELLOW+"⏮️ Showing Recently Played Songs.");
                mf.displayAndPlayRecentlyPlayed();
                break;

            case 5:
                System.out.println("🎲 Playing a Random Song.");
                mf.playRandomSong();
                break;

            case 6:
                mf.searchSongByName();
                break;

            case 7:
                mf.showTrendingSongs();
                break;

            case 8:
                mf.searchSongByMovie();
                break;

            case 9:
                System.out.println(cc.CYAN+"👋 Exiting the Music App. Thank you!");
                System.exit(0);
                break;

            default:
                System.out.println(cc.RED_BOLD_BRIGHT+"❗ Invalid choice. Please select from 1 to 8.");
                appMainMenu();
                break;
        }
    }

}
