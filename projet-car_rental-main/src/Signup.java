import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Signup extends JFrame {
    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(Color.BLACK);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private final JButton signin = new JButton("Sign in");
    private final JTextField comment = new JTextField("You have an account ?");
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField passwordVerifyField = new JPasswordField();
    private final JTextField addressField = new JTextField();
    private final JComboBox<String> userTypeComboBox = new JComboBox<>(new String[]{"Admin", "Customer"});


    public Signup() {
        setTitle("Car-Rental");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width-1 ;
        int height = screenSize.height-1 ;
        setSize(width, height);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.BLACK);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);

        ImageIcon imageIcon = new ImageIcon("src/amg.jpg");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.BLACK);

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.BLACK);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(715, 0, 50, 50);
        closeButton.addActionListener(e -> System.exit(0));
        rightPanel.add(closeButton);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));



        JTextArea title = new JTextArea("SIGN UP TO Discover our offers!");
        title.setBounds(75, 25, 500, 70);
        title.setFont(new Font("SansSerif", Font.ITALIC, 25));
        title.setForeground(Color.WHITE);
        title.setBackground(Color.BLACK);
        title.setEditable(false);
        rightPanel.add(title);


        addFieldWithLabel("Name", nameField, rightPanel, 85);


        addFieldWithLabel("Email", emailField, rightPanel, 185);


        addFieldWithLabel("Password", passwordField, rightPanel, 285);


        addFieldWithLabel("Verify Password", passwordVerifyField, rightPanel, 385);


        addFieldWithLabel("Address", addressField, rightPanel, 485);


        RoundedButton signupButton = new RoundedButton("SIGN UP", 20);
        signupButton.setBounds(175, 710, 300, 40);
        signupButton.setBackground(Color.WHITE);
        signupButton.setForeground(Color.RED);
        signupButton.setFont(new Font("SansSerif", Font.ITALIC, 16));
        signupButton.addActionListener(this::handleSignup);
        signupButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupButton.setBorderPainted(false);
        signupButton.setFocusPainted(false);
        rightPanel.add(signupButton);

        addFieldWithLabel("User Type", userTypeComboBox, rightPanel, 585);


        userTypeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userTypeComboBox.setForeground(Color.WHITE);
        userTypeComboBox.setBackground(Color.DARK_GRAY);
        userTypeComboBox.setBorder(null);
        userTypeComboBox.setBorder(BorderFactory.createEmptyBorder());



        signin.setBounds(375, 750, 100, 40);
        signin.setBackground(Color.black);
        signin.setForeground(Color.RED);
        signin.setFont(new Font("SansSerif", Font.ITALIC, 16));
        signin.addActionListener(this::handleSignup);
        signin.setBorderPainted(false);
        signin.setFocusPainted(false);
        signin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rightPanel.add(signupButton);

        signin.addActionListener(e -> {
            new Auth();
            dispose();
        });

        comment.setBounds(185, 750, 200, 40);
        comment.setFont(new Font("SansSerif", Font.ITALIC, 16));
        comment.setForeground(Color.white);
        comment.setBackground(Color.black);
        comment.setEditable(false);
        comment.setBorder(null);
        comment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        rightPanel.add(comment);
        rightPanel.add(signin);

        mainPanel.add(rightPanel);
        mainPanel.add(leftPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void addFieldWithLabel(String labelText, JComponent field, JPanel panel, int yPosition) {
        JTextArea label = new JTextArea(labelText);
        label.setBounds(100, yPosition, 300, 50);
        label.setFont(new Font("SansSerif", Font.ITALIC, 25));
        label.setForeground(Color.WHITE);
        label.setBackground(Color.BLACK);
        label.setEditable(false);
        panel.add(label);

        field.setBounds(100, yPosition + 50, 450, 40);
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setForeground(Color.WHITE);
        field.setBackground(Color.DARK_GRAY);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.add(field);
    }

    private void handleSignup(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String passwordVerify = new String(passwordVerifyField.getPassword());
        String address = addressField.getText();
        String userType = (String) userTypeComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || userType == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!");
            return;
        }

        if (!password.equals(passwordVerify)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {

            String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkEmailQuery)) {
                checkStatement.setString(1, email);
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "This email is already in use. Please use a different email.");
                    return;
                }
            }

            String query = "INSERT INTO users (name, email, password, address, user_type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.setString(4, address);
                statement.setString(5, userType);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Signup successful!");
                    new Auth();
                    dispose();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    public class DatabaseConnection {
        private static final String DB_URL = "jdbc:mysql://localhost:3306/car_rental";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "";

        static {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load the database driver", e);
            }
        }

        public static Connection getConnection() throws SQLException {

            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
