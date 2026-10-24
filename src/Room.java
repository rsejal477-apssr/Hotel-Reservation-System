import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int roomNumber;
    private final String category;
    private final double pricePerNight;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }

    @Override
    public String toString() {
        return String.format("Room %d | %s | ₹%.2f/night", roomNumber, category, pricePerNight);
    }
}