

public class Room{
    
    private  int id , roomPricePerNight;
    private RoomType roomType;
    // RoomType is enum
    public Room(int id, RoomType roomType , int roomPricePerNight) {
            this.id = id;
            this.roomType = roomType;
            this.roomPricePerNight = roomPricePerNight;
    }

    public int getId() {
        return id;
    }

    public int getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public RoomType getRoomType() {
        return roomType;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null || getClass() != o.getClass()) 
            return false;
        Room room = (Room) o;
        return this.id == room.id && this.roomPricePerNight == room.roomPricePerNight && this.roomType == room.roomType;
    }   

}