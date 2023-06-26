import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Student {
    private String name;
    private int rollNumber;
    private String domain;
    public Student(String name, int rollNumber, String domain) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getDomain() {
        return domain;
    }
}

class Seat {
    private int seatNumber;
    private Student student;
    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.student = null;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void assignStudent(Student student) {
        this.student = student;
    }

    public void clearSeat() {
        this.student = null;
    }
}

class ExamSeatingArrangement {
    private List<Seat> seats;
    private int maxStudentsPerDomain;
    public ExamSeatingArrangement(int totalSeats, int maxStudentsPerDomain) {
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat(i));
        }
        this.maxStudentsPerDomain = maxStudentsPerDomain;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void assignSeat(Student student, int seatNumber) {
        Seat seat = getSeatByNumber(seatNumber);
        if (seat != null) {
            if (seat.getStudent() == null) {
                if (isDomainLimitReached(student.getDomain())) {
                    JOptionPane.showMessageDialog(null, "Domain limit reached. Cannot assign seat to this student.");
                } else {
                    seat.assignStudent(student);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Seat is already assigned to a student.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid seat number.");
        }
    }

    public void clearSeat(int seatNumber) {
        Seat seat = getSeatByNumber(seatNumber);
        if (seat != null) {
            seat.clearSeat();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid seat number.");
        }
    }

    private Seat getSeatByNumber(int seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        return null;
    }

    private boolean isDomainLimitReached(String domain) {
        int count = 0;
        for (Seat seat : seats) {
            Student student = seat.getStudent();
            if (student != null && student.getDomain().equals(domain)) {
                count++;
                if (count >= maxStudentsPerDomain) {
                    return true;
                }
            }
        }
        return false;
    }
}

class ExamSeatingArrangementGUI extends JFrame {
    private ExamSeatingArrangement examSeatingArrangement;
    private JTextArea seatingTextArea;

    public ExamSeatingArrangementGUI() {
        examSeatingArrangement = new ExamSeatingArrangement(50, 3); // Total seats: 50, Max students per domain: 3

        setTitle("Exam Seating Arrangement System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createComponents();
        updateSeatingTextArea();
    }

    private void createComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        seatingTextArea = new JTextArea();
        seatingTextArea.setEditable(false);
        seatingTextArea.setFont(new Font("Arial", Font.PLAIN, 12)); // Customize font size and style
        JScrollPane scrollPane = new JScrollPane(seatingTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton assignSeatButton = new JButton("Assign Seat");
        assignSeatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignSeat();
            }
        });
        buttonPanel.add(assignSeatButton);

        JButton clearSeatButton = new JButton("Clear Seat");
        clearSeatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSeat();
            }
        });
        buttonPanel.add(clearSeatButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
    }

    private void updateSeatingTextArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seating Arrangement:\n");

        List<Seat> seats = examSeatingArrangement.getSeats();
        for (Seat seat : seats) {
            sb.append("Seat Number: ").append(seat.getSeatNumber());
            if (seat.getStudent() != null) {
                sb.append(", Student Name: ").append(seat.getStudent().getName());
                sb.append(", Roll Number: ").append(seat.getStudent().getRollNumber());
                sb.append(", Domain: ").append(seat.getStudent().getDomain());
            }
            sb.append("\n");
        }

        seatingTextArea.setText(sb.toString());
    }

    private void assignSeat() {
        String name = JOptionPane.showInputDialog(null, "Enter student name:");
        String rollNumberStr = JOptionPane.showInputDialog(null, "Enter student roll number:");
        String domain = JOptionPane.showInputDialog(null, "Enter student domain:");

        if (name != null && !name.isEmpty() && rollNumberStr != null && !rollNumberStr.isEmpty() && domain != null && !domain.isEmpty()) {
            try {
                int rollNumber = Integer.parseInt(rollNumberStr);
                Student student = new Student(name, rollNumber, domain);
                String seatNumberStr = JOptionPane.showInputDialog(null, "Enter seat number:");
                if (seatNumberStr != null && !seatNumberStr.isEmpty()) {
                    try {
                        int seatNumber = Integer.parseInt(seatNumberStr);
                        examSeatingArrangement.assignSeat(student, seatNumber);
                        updateSeatingTextArea();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid seat number. Please enter a valid number.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid roll number. Please enter a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid details.");
        }
    }

    private void clearSeat() {
        String seatNumberStr = JOptionPane.showInputDialog(null, "Enter seat number to clear:");
        if (seatNumberStr != null && !seatNumberStr.isEmpty()) {
            try {
                int seatNumber = Integer.parseInt(seatNumberStr);
                examSeatingArrangement.clearSeat(seatNumber);
                updateSeatingTextArea();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid seat number. Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExamSeatingArrangementGUI app = new ExamSeatingArrangementGUI();
                app.setVisible(true);
            }
        });
    }
}





