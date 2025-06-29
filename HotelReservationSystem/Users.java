public class  Users {

    private int balance;
    private int userID;

    public Users(int userID){
        this.balance = 0;
        this.userID = userID;
    };
    public Users(int userID,int balance  ){
            this.balance = balance;
            this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public int getBalance() {
        return balance;
    }
    public void deductBalance(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient balance.");
        }
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }
    

    // can book a room for a specific period ( from A to B)
    // Room need to be free
    // and has to have enough  balance
}
