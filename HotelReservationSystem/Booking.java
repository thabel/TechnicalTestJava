
import java.util.Date;

public class Booking {
    private int  userId,  roomNumber;
    private Date checkIn, checkOut;
    // The booking data should contains all the information
    // about the room and user when the booking was done.
    public Booking(int userId, int roomNumber, Date checkIn, Date checkOut) {
            this.userId = userId;
            this.roomNumber = roomNumber;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
    }
    public int getUserId() {
        return userId;
    }
    public Date getCheckIn() {
        return checkIn;
    }
    public Date getCheckOut() {
        return checkOut;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    
    
}
