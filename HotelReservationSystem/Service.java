
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Service {

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Users> users = new ArrayList<>();
    ArrayList<Booking> bookings = new ArrayList<>();

    int getDuration(Date date1, Date date2) {
        final int MILLISECONDS_IN_SECOND = 1000;
        final int SECONDS_IN_MINUTE = 60;
        final int MINUTES_IN_HOUR = 60;
        final int HOURS_IN_DAY = 24;
        long millisecondsInDay = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY;
        long diffInMillis = date2.getTime() - date1.getTime();
        // Convert milliseconds to days
        long duration = diffInMillis / millisecondsInDay;

        return (int) duration;
    }

    // is room free ?
    boolean isRoomFree(int roomNumber, Date checkIn, Date checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber) {
                if (checkIn.before(booking.getCheckOut()) && checkOut.after(booking.getCheckIn())) {
                    return false;
                }
            }
        }

        return true;
    }

    Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getId() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    Users findUser(int userId) {
        for (Users user : users) {
            if (user.getUserID() == userId) {
                return user;
            }
        }
        return null;
    }

    void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {

        // create a room if it does not already exists
        // Should not impact previously createed Bookings
        Room getRoom = findRoom(roomNumber);
        Room newRoom = new Room(roomNumber, roomType, roomPricePerNight);
        if (getRoom != null) {
            if (getRoom.equals(newRoom)) {
                return; // does not create a new Room
            }
        }
        rooms.add(newRoom);
    }

    ;
    void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        // check user

        Users user = findUser(userId);

        if (user == null) {
           throw new IllegalArgumentException("User not found: " + userId);
        }
        Room userRoom = findRoom(roomNumber);
        if (userRoom == null) {
           throw new IllegalArgumentException("Room not found: " + roomNumber);
        }
        // verify order 
        if (checkOut.before(checkIn)) {
             throw new IllegalArgumentException("Invalid booking period");
            
        }
        // verify balances
        boolean isRequestedRommFree = isRoomFree(roomNumber, checkIn, checkOut);
        if (!isRequestedRommFree) {
            throw new IllegalArgumentException("Room is not available for the specified period");
         
        }
        // is balance sufficent ?

        int duration = getDuration(checkIn, checkOut);
        int price = duration * userRoom.getRoomPricePerNight();

        // check user balance ?
        if (user.getBalance() < price) {
            throw new IllegalArgumentException("Insufficient balance. Required: " + price + ", Available: " + user.getBalance());
        }

        Booking newBooking = new Booking(userId, roomNumber, checkIn, checkOut);
        user.deductBalance(price);
        bookings.add(newBooking);

    }

    ;
    void printAll() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Print Rooms
        System.out.println("===== ROOMS (Latest to Oldest) =====");
        System.out.printf("%-5s | %-12s | %-15s%n", "ID", "ROOM TYPE", "PRICE/NIGHT");
        System.out.println("--------------------------------------------");

        for (int i = rooms.size() - 1; i >= 0; i--) {
            Room room = rooms.get(i);
            System.out.printf("%-5d | %-12s | %-15d%n",
                    room.getId(), room.getRoomType(), room.getRoomPricePerNight());
        }

        System.out.println();

        // Print Bookings
        System.out.println("===== BOOKINGS (Latest to Oldest) =====");
        System.out.printf("%-8s | %-12s | %-15s | %-15s%n", "USER ID", "ROOM NUMBER", "CHECK-IN", "CHECK-OUT");
        System.out.println("------------------------------------------------------------------");

        for (int i = bookings.size() - 1; i >= 0; i--) {
            Booking booking = bookings.get(i);
            System.out.printf("%-8d | %-12d | %-15s | %-15s%n",
                    booking.getUserId(),
                    booking.getRoomNumber(),
                    sdf.format(booking.getCheckIn()),
                    sdf.format(booking.getCheckOut()));
        }

        System.out.println();
    }

    void setUser(int userId, int balance) {
        // create a user if it does not already exists
        Users getUser = findUser(userId);
        if (getUser != null) {
            return;
        }
        Users user = new Users(userId, balance);
        users.add(user);
    }

    void printAllUsers() {
        System.out.println("===== USERS (Latest to Oldest) =====");
        System.out.printf("%-10s | %-10s%n", "USER ID", "BALANCE");
        System.out.println("------------------------------");

        for (int i = users.size() - 1; i >= 0; i--) {
            Users user = users.get(i);
            System.out.printf("%-10d | %-10d%n", user.getUserID(), user.getBalance());
        }

        System.out.println();
    }

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Service service = new Service();
        // create 3 rooms
        service.setRoom(1, RoomType.STANDAR_SUITE, 1000);
        service.setRoom(2, RoomType.JUNIOR_SUITE, 2000);
        service.setRoom(3, RoomType.SUITE, 3000);

        // create 2 users
        service.setUser(1, 5000);
        service.setUser(2, 10000);

        try {
            // User 1 tries booking Room 2 from 30/06/2026 to 07/07/2026 (7nights).
            System.out.println("\n1. User 1 booking Room 2 (30/06/2026 to 07/07/2026):");
            service.bookRoom(1, 2, sdf.parse("30/06/2026"), sdf.parse(" 07/07/2026")
            );
            System.out.println("\n2. User 1 booking Room 2 (07/07/2026 to 30/06/2026):");
            // User 1 tries booking Room 2 from 07/07/2026 to 30/06/2026.
            service.bookRoom(1, 2, sdf.parse("07/07/2026"),
                    sdf.parse("30/06/2026")
            );

            // User 1 tries booking Room 1 from 07/07/2026 to 08/07/2026 (1 night).
            System.out.println("\n3. User 1 booking Room 1 (07/07/2026 to 08/07/2026):");
            service.bookRoom(1, 1, sdf.parse("07/07/2026"),
                    sdf.parse("08/07/2026")
            );

            // User 2 tries booking Room 1 from 07/07/2026 to 09/07/2026 (2nights).
            System.out.println("\n4. User 2 booking Room 1 (07/07/2026 to 09/07/2026):");
            service.bookRoom(2, 1, sdf.parse("07/07/2026"),
                    sdf.parse("09/07/2026")
            );

            // User 2 tries booking Room 3 from 07/07/2026 to 08/07/2026 (1night).
            System.out.println("\n5. User 2 booking Room 3 (07/07/2026 to 08/07/2026):");
            service.bookRoom(2, 3, sdf.parse("07/07/2026"),
                    sdf.parse("08/07/2026")
            );
            // setRoom(1, suite, 10000).
            System.out.println("\n6. Updating Room 1:");
            service.setRoom(1, RoomType.SUITE, 1000);

            service.printAll();
            service.printAllUsers();
        } catch (Exception e) {
            System.err.println("Booking failed: " + e.getMessage());
        }
    }
}
