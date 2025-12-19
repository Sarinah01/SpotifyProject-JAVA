package MusicApp1;

class SignUpInfo {
    private int userId;         // UUID or auto-generated
    private String username;
    private String email;
    private String passwordHash;   // Store hashed password
    private String gender;
    private String mobileNumber;

    // Constructor
    public SignUpInfo(int userId, String username, String email, String passwordHash,
                      String gender, String mobileNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
    }

    // Getters & Setters (can be generated via IDE or manually)

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getGender() { return gender; }
    public String getMobileNumber() { return mobileNumber; }

    public String toString() {
        return "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNumber='" + mobileNumber + '\'';
    }
}
class User {
    class node{
        node next;
        node prev;
        SignUpInfo data;
        node(SignUpInfo data){
            this.data=data;
            this.next=null;
            this.prev=null;
        }
    }

    node first=null;

    void insertAtLast(SignUpInfo x){
        node n=new node(x);
        if(first==null){
            first=n;
        }
        else{
            node temp=first;
            while(temp.next!=null){
                temp=temp.next;
            }
            temp.next=n;
            n.prev=temp;
        }
    }

    void deleteAtFirst(){
        if(first==null){
            System.out.println("empty");
        }
        else if(first.next==null){
            first=null;
        }
        else {
            node del = first.prev;
            first = first.next;
            first.prev = null;
            del = null;
        }
    }

    void deleteAtLast(){
        if(first==null){
            System.out.println("empty");
        }
        else if(first.next==null){
            first=null;
        }
        else{
            node temp=first;
            while(temp.next.next!=null){
                temp=temp.next;
            }
            temp.next.prev=null;
            temp.next=null;
        }
    }

    void deleteParticularUser(int userId){
        boolean b=false;
        if(first==null){
            System.out.println("empty");
        }
        else {
            node temp = first;
            while (temp != null) {
                if (temp.data.getUserId() == userId) {
                    b = true;
                }
                temp = temp.next;
            }
            if (b) {
                if (first.data .getUserId()== userId) {
                    deleteAtFirst();
                } else {
                    node temp1 = first;
                    while (temp1.next.data.getUserId() != userId) {
                        temp1 = temp1.next;
                    }
                    node del = temp1.next;
                    if (del.next == null) {
                        deleteAtLast();
                    } else {
                        temp1.next = del.next;
                        del.next.prev = temp1;
                        del.next = null;
                        del.prev = null;
                        del = null;
                    }
                }

            }
        }
    }

    SignUpInfo findById(int userid){
        node temp=first;
        while(temp!=null){
            if(temp.data.getUserId()==userid){
                return temp.data;
            }
            temp=temp.next;
        }
        return null;
    }

    boolean containsId(int userid){
        if (findById(userid)==null) {
            return false;
        }else {
            return true;
        }
    }

    SignUpInfo findByEmail(String email){
        node temp=first;
        while(temp!=null){
            if(temp.data.getEmail().equals(email)){
                return temp.data;
            }
            temp=temp.next;
        }
        return null;
    }

    boolean containsEmail(String email){
        if (findByEmail(email)==null) {
            return false;
        }else {
            return true;
        }
    }

}
