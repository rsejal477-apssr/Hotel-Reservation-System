import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main extends JFrame {
    private final HotelService hotel = new HotelService();
    private final Color navy = new Color(20, 35, 60);
    private final Color blue = new Color(35, 75, 125);
    private final Color blueHover = new Color(25, 58, 100);
    private final Color teal = new Color(0, 112, 100);
    private final Color tealHover = new Color(0, 86, 78);
    private final Color page = new Color(240, 244, 249);
    private final Color ink = new Color(38, 49, 65);
    private JTextField nameField, emailField, checkInField, checkOutField, bookingIdField, bookingEmailField;
    private JComboBox<String> categoryBox;
    private JComboBox<Integer> roomBox;
    private JTextArea resultsArea;
    private JTable bookingTable;
    private DefaultTableModel bookingModel;

    public Main() {
        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 760);
        setMinimumSize(new Dimension(940, 660));
        setLocationRelativeTo(null);
        getContentPane().setBackground(page);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabs.setBackground(page);
        tabs.setForeground(ink);
        tabs.addTab("  Search & Book  ", createBookingPanel());
        tabs.addTab("  Manage Booking  ", createManagePanel());
        tabs.addTab("  All Bookings  ", createBookingsPanel());
        add(tabs, BorderLayout.CENTER);
        refreshBookingsTable();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(navy);
        header.setBorder(new EmptyBorder(22, 28, 22, 28));
        JLabel title = new JLabel("HOTEL RESERVATION SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        JLabel subtitle = new JLabel("A comfortable stay starts here  •  Search rooms  •  Reserve  •  Manage bookings");
        subtitle.setForeground(new Color(210, 224, 242));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel texts = new JPanel(new GridLayout(2, 1, 0, 7));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(subtitle);
        header.add(texts, BorderLayout.WEST);
        return header;
    }

    private JPanel createBookingPanel() {
        JPanel outer = new JPanel(new BorderLayout(16, 16));
        outer.setBackground(page);
        outer.setBorder(new EmptyBorder(20, 22, 18, 22));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 224, 235)),
                new EmptyBorder(18, 18, 18, 18)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        nameField = new JTextField(18);
        emailField = new JTextField(18);
        checkInField = new JTextField(LocalDate.now().plusDays(1).toString(), 12);
        checkOutField = new JTextField(LocalDate.now().plusDays(2).toString(), 12);
        categoryBox = new JComboBox<>(new String[]{"All", "Standard", "Deluxe", "Suite"});
        roomBox = new JComboBox<>();

        addField(form, c, 0, 0, "Guest name", nameField);
        addField(form, c, 0, 2, "Email address", emailField);
        addField(form, c, 1, 0, "Check-in (YYYY-MM-DD)", checkInField);
        addField(form, c, 1, 2, "Check-out (YYYY-MM-DD)", checkOutField);
        addField(form, c, 2, 0, "Room category", categoryBox);
        addField(form, c, 2, 2, "Available room", roomBox);

        JButton searchButton = button("Search Availability", blue, blueHover);
        JButton bookButton = button("Confirm Booking", teal, tealHover);
        searchButton.addActionListener(e -> searchRooms());
        bookButton.addActionListener(e -> bookRoom());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        actions.setOpaque(false);
        actions.add(searchButton);
        actions.add(bookButton);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 4; c.weightx = 1;
        form.add(actions, c);

        resultsArea = new JTextArea(9, 50);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultsArea.setForeground(ink);
        resultsArea.setBackground(new Color(250, 252, 255));
        resultsArea.setMargin(new Insets(12, 12, 12, 12));
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        JScrollPane resultsScroll = new JScrollPane(resultsArea);
        resultsScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(215, 224, 235)),
                "Availability / booking result"));

        JLabel note = new JLabel("Sample rates: Standard ₹1,800  •  Deluxe ₹2,800  •  Suite ₹4,500 per night");
        note.setForeground(new Color(85, 98, 115));
        note.setFont(new Font("SansSerif", Font.PLAIN, 12));
        outer.add(form, BorderLayout.NORTH);
        outer.add(resultsScroll, BorderLayout.CENTER);
        outer.add(note, BorderLayout.SOUTH);
        return outer;
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(page);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 224, 235)),
                new EmptyBorder(20, 20, 20, 20)));
        bookingIdField = new JTextField(22);
        bookingEmailField = new JTextField(22);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 9, 9, 9);
        c.fill = GridBagConstraints.HORIZONTAL;
        addField(form, c, 0, 0, "Booking ID", bookingIdField);
        addField(form, c, 1, 0, "Booking email", bookingEmailField);

        JButton viewButton = button("View Details", blue, blueHover);
        JButton payButton = button("Simulate Payment", teal, tealHover);
        JButton cancelButton = button("Cancel Reservation", new Color(155, 45, 55), new Color(120, 30, 40));
        viewButton.addActionListener(e -> viewBooking());
        payButton.addActionListener(e -> makePayment());
        cancelButton.addActionListener(e -> cancelBooking());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        actions.setOpaque(false);
        actions.add(viewButton);
        actions.add(payButton);
        actions.add(cancelButton);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2;
        form.add(actions, c);

        JTextArea help = new JTextArea(
                "HOW TO MANAGE A RESERVATION\n\n" +
                "1. Enter the booking ID shown after confirming a reservation.\n" +
                "2. Enter the email address used when making the booking.\n" +
                "3. View details, record a simulated payment, or cancel the reservation.\n\n" +
                "Note: Payment is simulated. No real money is charged.");
        help.setEditable(false);
        help.setOpaque(false);
        help.setForeground(ink);
        help.setFont(new Font("SansSerif", Font.PLAIN, 15));
        help.setMargin(new Insets(18, 8, 8, 8));
        help.setLineWrap(true);
        help.setWrapStyleWord(true);

        panel.add(form, BorderLayout.NORTH);
        panel.add(help, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(page);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        String[] columns = {"Booking ID", "Guest", "Email", "Room", "Category", "Check-in", "Check-out", "Total", "Status", "Payment"};
        bookingModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookingTable = new JTable(bookingModel);
        bookingTable.setRowHeight(28);
        bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bookingTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        bookingTable.getTableHeader().setBackground(navy);
        bookingTable.getTableHeader().setForeground(Color.WHITE);
        bookingTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        bookingTable.setSelectionBackground(new Color(213, 229, 247));
        bookingTable.setGridColor(new Color(225, 231, 239));
        JButton refreshButton = button("Refresh Bookings", blue, blueHover);
        refreshButton.addActionListener(e -> refreshBookingsTable());
        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints c, int row, int col, String label, JComponent input) {
        c.gridy = row; c.gridx = col; c.gridwidth = 1; c.weightx = 0;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        jLabel.setForeground(ink);
        panel.add(jLabel, c);
        c.gridx = col + 1; c.weightx = 1;
        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        input.setPreferredSize(new Dimension(190, 32));
        panel.add(input, c);
    }

    private JButton button(String text, Color color, Color hover) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(12, 18, 12, 18));
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(hover); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(color); }
        });
        return b;
    }

    private Dates readDates() {
        try {
            LocalDate start = LocalDate.parse(checkInField.getText().trim());
            LocalDate end = LocalDate.parse(checkOutField.getText().trim());
            if (start.isBefore(LocalDate.now()) || !start.isBefore(end)) {
                showError("Check-in must be today or a future date, and check-out must be after check-in.");
                return null;
            }
            return new Dates(start, end);
        } catch (DateTimeParseException ex) {
            showError("Enter dates in YYYY-MM-DD format, for example 2026-12-15.");
            return null;
        }
    }

    private void searchRooms() {
        Dates dates = readDates();
        if (dates == null) return;
        List<Room> rooms = hotel.searchAvailableRooms((String) categoryBox.getSelectedItem(), dates.start, dates.end);
        roomBox.removeAllItems();
        StringBuilder text = new StringBuilder();
        text.append("Stay: ").append(dates.start).append(" to ").append(dates.end)
                .append(" (").append(ChronoUnit.DAYS.between(dates.start, dates.end)).append(" night(s))\n\n");
        if (rooms.isEmpty()) {
            text.append("No rooms are available for the selected category and dates.");
        } else {
            text.append("AVAILABLE ROOMS\n");
            for (Room room : rooms) {
                roomBox.addItem(room.getRoomNumber());
                text.append(String.format("Room %d | %-8s | ₹%.2f/night | Estimated total ₹%.2f%n",
                        room.getRoomNumber(), room.getCategory(), room.getPricePerNight(),
                        room.getPricePerNight() * ChronoUnit.DAYS.between(dates.start, dates.end)));
            }
        }
        resultsArea.setText(text.toString());
    }

    private void bookRoom() {
        Dates dates = readDates();
        if (dates == null) return;
        if (roomBox.getSelectedItem() == null) {
            showError("Search availability and select an available room first.");
            return;
        }
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        try {
            Reservation reservation = hotel.bookRoom(name, email, (Integer) roomBox.getSelectedItem(), dates.start, dates.end);
            if (reservation == null) {
                showError("That room is no longer available for the selected dates. Search again.");
                searchRooms();
                return;
            }
            resultsArea.setText("BOOKING CONFIRMED!\n\n" + reservation.toDisplayString()
                    + "\n\nSave your booking ID: " + reservation.getBookingId()
                    + "\nUse the Manage Booking tab to view, pay, or cancel.");
            bookingIdField.setText(reservation.getBookingId());
            bookingEmailField.setText(reservation.getEmail());
            refreshBookingsTable();
            JOptionPane.showMessageDialog(this, "Booking created!\nBooking ID: " + reservation.getBookingId(),
                    "Reservation successful", JOptionPane.INFORMATION_MESSAGE);
            searchRooms();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(ex.getMessage());
        }
    }

    private void viewBooking() {
        Reservation r = hotel.findBooking(bookingIdField.getText().trim());
        if (r == null) { showError("Booking not found. Check the booking ID."); return; }
        JOptionPane.showMessageDialog(this, r.toDisplayString().replace(" | ", "\n"),
                "Booking Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void makePayment() {
        Reservation r = hotel.findBooking(bookingIdField.getText().trim());
        if (r == null) { showError("Booking not found. Check the booking ID."); return; }
        if (!r.getEmail().equalsIgnoreCase(bookingEmailField.getText().trim())) {
            showError("The email does not match this booking."); return;
        }
        if (!"PENDING".equals(r.getPaymentStatus())) {
            showError("Payment is not pending for this booking."); return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                String.format("Simulate payment of ₹%.2f? No real money will be charged.", r.getTotalAmount()),
                "Confirm simulated payment", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                if (hotel.processPayment(r.getBookingId(), bookingEmailField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Simulated payment recorded successfully.");
                    refreshBookingsTable();
                } else showError("Payment could not be processed.");
            } catch (IllegalStateException ex) { showError(ex.getMessage()); }
        }
    }

    private void cancelBooking() {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this reservation?",
                "Confirm cancellation", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            boolean cancelled = hotel.cancelBooking(bookingIdField.getText().trim(), bookingEmailField.getText().trim());
            if (cancelled) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled. The room is available again for those dates.");
                refreshBookingsTable();
            } else showError("Could not cancel. Check the booking ID, email, and booking status.");
        } catch (IllegalStateException ex) { showError(ex.getMessage()); }
    }

    private void refreshBookingsTable() {
        if (bookingModel == null) return;
        bookingModel.setRowCount(0);
        for (Reservation r : hotel.getAllBookings()) {
            bookingModel.addRow(new Object[]{r.getBookingId(), r.getGuestName(), r.getEmail(),
                    r.getRoomNumber(), r.getRoomCategory(), r.getCheckIn(), r.getCheckOut(),
                    String.format("₹%.2f", r.getTotalAmount()), r.getStatus(), r.getPaymentStatus()});
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Please check", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { }
            try { new Main().setVisible(true); }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not start the application:\n" + ex.getMessage(),
                        "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static class Dates {
        final LocalDate start, end;
        Dates(LocalDate start, LocalDate end) { this.start = start; this.end = end; }
    }
}
