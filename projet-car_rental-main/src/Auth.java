import config.DatabaseAccessor;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Auth extends JFrame {

    static int id;
    static String mail;
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

    JTextArea title1 = new JTextArea("");
    JTextArea title2 = new JTextArea("Welcome Back to Car-Rental");
    JTextField email = new JTextField();
    JPasswordField password = new JPasswordField();
    JButton signUp = new JButton("Sign up");
    JTextArea mailtext = new JTextArea("Email");
    JTextArea comment = new JTextArea("Didn't have an account?");
    JTextArea passwordtext = new JTextArea("Password");
    JTextArea design = new JTextArea();

    String selectedRole = "Customer";

    public Auth() {
        setTitle("Car-Rental");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width - 1;
        int height = screenSize.height - 1;
        setSize(width, height);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.BLACK);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(Color.BLACK);

        ImageIcon imageIcon = new ImageIcon("src/rs6.jpg");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.BLACK);

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.black);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.addActionListener(e -> System.exit(0));

        closeButton.setBounds(715, 0, 50, 50);
        rightPanel.add(closeButton);


        title1.setBounds(50, 20, 300, 60);
        title1.setFont(new Font("serif", Font.BOLD, 50));
        title1.setForeground(Color.WHITE);
        title1.setBackground(Color.BLACK);
        title1.setEditable(false);

        title2.setBounds(75, 175, 500, 50);
        title2.setFont(new Font("SansSerif", Font.ITALIC, 25));
        title2.setForeground(Color.WHITE);
        title2.setBackground(Color.BLACK);
        title2.setEditable(false);

        design.setBounds(160, 610, 350, 1);
        design.setEditable(false);
        design.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        design.setBorder(BorderFactory.createLineBorder(Color.RED, 1));


        mailtext.setBounds(100, 260, 300, 50);
        mailtext.setFont(new Font("SansSerif", Font.ITALIC, 25));
        mailtext.setForeground(Color.WHITE);
        mailtext.setBackground(Color.BLACK);
        mailtext.setEditable(false);

        passwordtext.setBounds(100, 370, 300, 50);
        passwordtext.setFont(new Font("SansSerif", Font.ITALIC, 25));
        passwordtext.setForeground(Color.WHITE);
        passwordtext.setBackground(Color.BLACK);
        passwordtext.setEditable(false);

        email.setBounds(100, 310, 450, 40);
        email.setFont(new Font("SansSerif", Font.PLAIN, 16));
        email.setForeground(Color.WHITE);
        email.setBackground(Color.DARK_GRAY);
        email.setCaretColor(Color.WHITE);
        email.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        password.setBounds(100, 420, 450, 40);
        password.setFont(new Font("SansSerif", Font.PLAIN, 16));
        password.setForeground(Color.WHITE);
        password.setBackground(Color.DARK_GRAY);
        password.setCaretColor(Color.WHITE);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));


        RoundedButton b0 = new RoundedButton("LOG IN", 20);
        b0.setBounds(175, 520, 300, 40);
        b0.setBackground(Color.WHITE);
        b0.setForeground(Color.RED);
        b0.setFont(new Font("SansSerif", Font.ITALIC, 16));
        b0.setFocusPainted(false);
        b0.setBorderPainted(false);
        b0.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b0.addActionListener(e -> {
            String emailText = email.getText();
            String passwordText = new String(password.getPassword());

            if ("Admin".equals(selectedRole)) {
                if (verifyCredentials(emailText, passwordText, selectedRole)) {
                    mail = emailText;
                    new AdminInterface().setVisible(true);
                    dispose();
                }}
                else if ("Customer".equals(selectedRole)) {
                    if (verifyCredentials(emailText, passwordText, selectedRole)) {
                        mail = emailText;
                        new clientloc().setVisible(true);
                        dispose();
                    }
                else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password for " + selectedRole + "!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Only Admin can access this page.", "Error", JOptionPane.ERROR_MESSAGE);
            }


    });

        comment.setBounds(200, 625, 300, 30);
        comment.setFont(new Font("SansSerif", Font.ITALIC, 16));
        comment.setForeground(Color.WHITE);
        comment.setBackground(Color.BLACK);
        comment.setEditable(false);

        signUp.setBounds(375, 622, 100, 30);
        signUp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        signUp.setForeground(Color.RED);
        signUp.setBackground(Color.BLACK);
        signUp.setFont(new Font("SansSerif", Font.ITALIC, 16));
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUp.setFocusPainted(false);
        signUp.setBorderPainted(false);

        signUp.addActionListener(e -> {
            new Signup();
            dispose();
        });


        JButton adminButton = new JButton("Admin");
        adminButton.setBounds(100, 700, 150, 40);
        adminButton.setBackground(Color.WHITE);
        adminButton.setForeground(Color.RED);
        adminButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        adminButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        adminButton.setFocusPainted(false);

        JButton customerButton = new JButton("Customer");
        customerButton.setBounds(400, 700, 150, 40);
        customerButton.setBackground(Color.RED);
        customerButton.setForeground(Color.BLACK);
        customerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        customerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        customerButton.setFocusPainted(false);

        adminButton.addActionListener(e -> {
            selectedRole = "Admin";
            adminButton.setBackground(Color.RED);
            adminButton.setForeground(Color.BLACK);
            customerButton.setBackground(Color.WHITE);
            customerButton.setForeground(Color.RED);
        });

        customerButton.addActionListener(e -> {
            selectedRole = "Customer";
            customerButton.setBackground(Color.RED);
            customerButton.setForeground(Color.BLACK);
            adminButton.setBackground(Color.WHITE);
            adminButton.setForeground(Color.RED);
        });

        rightPanel.add(title1);
        rightPanel.add(title2);
        rightPanel.add(email);
        rightPanel.add(password);
        rightPanel.add(b0);
        rightPanel.add(signUp);
        rightPanel.add(mailtext);
        rightPanel.add(passwordtext);
        rightPanel.add(design);
        rightPanel.add(comment);
        rightPanel.add(adminButton);
        rightPanel.add(customerButton);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private boolean verifyCredentials(String email, String password, String role) {


        try (Connection conn = DatabaseAccessor.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ? AND user_type = ?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        new Auth();
    }
}