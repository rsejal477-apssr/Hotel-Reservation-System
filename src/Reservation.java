import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String bookingId;
    private final String guestName;
    private final String email;
    private final int roomNumber;
    private final String roomCategory;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalAmount;
    private String status;
    private String paymentStatus;

    public Reservation(String bookingId, String guestName, String email, Room room,
                       LocalDate checkIn, LocalDate checkOut) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.email = email;
        this.roomNumber = room.getRoomNumber();
        this.roomCategory = room.getCategory();
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = ChronoUnit.DAYS.between(checkIn, checkOut) * room.getPricePerNight();
        this.status = "CONFIRMED";
        this.paymentStatus = "PENDING";
    }

    public String getBookingId() { return bookingId; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public int getRoomNumber() { return roomNumber; }
    public String getRoomCategory() { return roomCategory; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void cancel() { status = "CANCELLED"; }
    public void markPaid() { paymentStatus = "PAID (SIMULATED)"; }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return "CONFIRMED".equals(status) && checkIn.isBefore(end) && start.isBefore(checkOut);
    }

    public String toDisplayString() {
        return String.format("%s | %s | Room %d (%s) | %s to %s | ₹%.2f | %s | %s",
                bookingId, guestName, roomNumber, roomCategory, checkIn, checkOut,
                totalAmount, status, paymentStatus);
    }

    @Override
    public String toString() { return toDisplayString(); }
}