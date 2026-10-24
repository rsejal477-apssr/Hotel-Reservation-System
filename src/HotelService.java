import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelService {
    private final List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private final File dataFile;

    public HotelService() {
        this("data/bookings.dat");
    }

    public HotelService(String storagePath) {
        dataFile = new File(storagePath);
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        rooms.add(new Room(101, "Standard", 1800));
        rooms.add(new Room(102, "Standard", 1800));
        rooms.add(new Room(201, "Deluxe", 2800));
        rooms.add(new Room(202, "Deluxe", 2800));
        rooms.add(new Room(301, "Suite", 4500));
        rooms.add(new Room(302, "Suite", 4500));
        loadBookings();
    }

    public List<Room> getRooms() { return new ArrayList<>(rooms); }
    public List<Reservation> getAllBookings() { return new ArrayList<>(reservations); }

    public List<Room> searchAvailableRooms(String category, LocalDate checkIn, LocalDate checkOut) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (!"All".equalsIgnoreCase(category) &&
                    !room.getCategory().equalsIgnoreCase(category)) continue;
            boolean occupied = false;
            for (Reservation reservation : reservations) {
                if (reservation.getRoomNumber() == room.getRoomNumber()
                        && reservation.overlaps(checkIn, checkOut)) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) available.add(room);
        }
        return available;
    }

    public Reservation bookRoom(String name, String email, int roomNumber,
                                LocalDate checkIn, LocalDate checkOut) {
        if (name == null || name.trim().isEmpty() || email == null ||
                !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
                checkIn == null || checkOut == null ||
                !checkIn.isBefore(checkOut) || checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Enter valid guest details and future dates.");
        }

        Room selected = null;
        for (Room room : rooms) if (room.getRoomNumber() == roomNumber) selected = room;
        if (selected == null) throw new IllegalArgumentException("Room not found.");

        for (Reservation reservation : reservations) {
            if (reservation.getRoomNumber() == roomNumber &&
                    reservation.overlaps(checkIn, checkOut)) {
                return null;
            }
        }

        String id = "HTL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reservation reservation = new Reservation(id, name.trim(), email.trim(),
                selected, checkIn, checkOut);
        reservations.add(reservation);
        saveBookings();
        return reservation;
    }

    public Reservation findBooking(String bookingId) {
        for (Reservation reservation : reservations)
            if (reservation.getBookingId().equalsIgnoreCase(bookingId.trim())) return reservation;
        return null;
    }

    public boolean cancelBooking(String bookingId, String email) {
        Reservation reservation = findBooking(bookingId);
        if (reservation == null || !reservation.getEmail().equalsIgnoreCase(email.trim())
                || !"CONFIRMED".equals(reservation.getStatus())) return false;
        reservation.cancel();
        saveBookings();
        return true;
    }

    public boolean processPayment(String bookingId, String email) {
        Reservation reservation = findBooking(bookingId);
        if (reservation == null || !reservation.getEmail().equalsIgnoreCase(email.trim())
                || !"CONFIRMED".equals(reservation.getStatus())
                || !"PENDING".equals(reservation.getPaymentStatus())) return false;
        reservation.markPaid();
        saveBookings();
        return true;
    }

    private void saveBookings() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(reservations);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save bookings: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBookings() {
        if (!dataFile.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object object = in.readObject();
            if (object instanceof List<?>) reservations = (List<Reservation>) object;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new IllegalStateException("Could not load saved bookings: " + e.getMessage(), e);
        }
    }
}