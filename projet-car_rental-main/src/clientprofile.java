import config.DatabaseAccessor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class clientprofile extends JFrame {

    public clientprofile() {
        setTitle("Client Profile");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width - 1, screenSize.height - 1);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.black);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.addActionListener(e -> System.exit(0));

        BackgroundPanel backgroundPanel = new BackgroundPanel("src/img.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton clientloc = createNavButton("Rent a Car");
        clientloc.addActionListener(e -> {
            new clientloc().setVisible(true);
            dispose();
        });

        navPanel.add(clientloc);
        navPanel.add(closeButton);

        backgroundPanel.add(navPanel, BorderLayout.NORTH);

        ViewUsersPanel viewUsersPanel = new ViewUsersPanel();
        backgroundPanel.add(viewUsersPanel, BorderLayout.CENTER);

    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 180));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    public class ViewUsersPanel extends JPanel {
        private JTextField imageUrlField;
        private JButton updateButton;

        public ViewUsersPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setOpaque(false);
            JLabel titleLabel = new JLabel("Your Profile");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel);
            add(titlePanel, BorderLayout.NORTH);

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setOpaque(false);

            JScrollPane scrollPane = new JScrollPane(cardPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            add(scrollPane, BorderLayout.CENTER);

            loadUserData(cardPanel);
        }

        private void loadUserData(JPanel cardPanel) {
            try (Connection conn = DatabaseAccessor.getConnection()) {
                String query = "SELECT * FROM users WHERE email = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, Auth.mail);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            JPanel userCard = new JPanel();
                            userCard.setLayout(new BorderLayout());
                            userCard.setBorder(new LineBorder(Color.WHITE, 2, true));
                            userCard.setBackground(new Color(0, 0, 0, 180));
                            userCard.setMaximumSize(new Dimension(700, 400));
                            userCard.setPreferredSize(new Dimension(700, 400));
                            userCard.setAlignmentX(Component.CENTER_ALIGNMENT);

                            JPanel infoPanel = new JPanel();
                            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                            infoPanel.setBackground(new Color(0, 0, 0, 0));
                            infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                            JLabel usernameLabel = createCardLabel("Username: " + rs.getString("name"), "src/icons/user.png");
                            JLabel emailLabel = createCardLabel("Email: " + rs.getString("email"), "src/icons/email.png");
                            JLabel passwordLabel = createCardLabel("Password: " + rs.getString("password"), "src/icons/lock.png");
                            JLabel addressLabel = createCardLabel("Address: " + rs.getString("address"), "src/icons/address.png");
                            JLabel statusLabel = createCardLabel("Status: Active", "src/icons/status.png");
                            statusLabel.setForeground(new Color(0, 255, 0));

                            infoPanel.add(usernameLabel);
                            infoPanel.add(Box.createVerticalStrut(10));
                            infoPanel.add(emailLabel);
                            infoPanel.add(Box.createVerticalStrut(10));
                            infoPanel.add(passwordLabel);
                            infoPanel.add(Box.createVerticalStrut(10));
                            infoPanel.add(addressLabel);
                            infoPanel.add(Box.createVerticalStrut(10));
                            infoPanel.add(statusLabel);
                            infoPanel.add(Box.createVerticalStrut(20));

                            addImageUrlField(infoPanel, rs.getString("image_url"), rs.getString("email"));

                            JPanel imagePanel = new JPanel();
                            imagePanel.setLayout(new BorderLayout());
                            imagePanel.setBackground(Color.BLACK);
                            imagePanel.setPreferredSize(new Dimension(250, 250));
                            imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

                            JLabel imageLabel = new JLabel();
                            String imagePath = rs.getString("image_url");
                            if (imagePath != null && !imagePath.isEmpty()) {
                                ImageIcon userImage = new ImageIcon(imagePath);
                                Image scaledImage = userImage.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                                imageLabel.setIcon(new ImageIcon(scaledImage));
                            } else {
                                imageLabel.setIcon(new ImageIcon(new ImageIcon("src/placeholder.jpg").getImage()
                                        .getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
                            }

                            imagePanel.add(imageLabel, BorderLayout.CENTER);

                            userCard.add(infoPanel, BorderLayout.CENTER);
                            userCard.add(imagePanel, BorderLayout.EAST);

                            cardPanel.add(userCard);
                        } else {
                            JLabel noUserLabel = new JLabel("No user found for the logged-in email.");
                            noUserLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                            noUserLabel.setForeground(Color.WHITE);
                            noUserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            cardPanel.add(noUserLabel);
                        }
                    }
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        private JLabel createCardLabel(String text, String iconPath) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            label.setIcon(new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            return label;
        }

        private void addImageUrlField(JPanel infoPanel, String currentImageUrl, String email) {
            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

            JLabel imageUrlLabel = new JLabel("Image URL:");
            imageUrlLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            imageUrlLabel.setForeground(Color.WHITE);
            imagePanel.add(imageUrlLabel);

            imageUrlField = new JTextField(currentImageUrl);
            imageUrlField.setPreferredSize(new Dimension(300, 30));
            imageUrlField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1, true),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            imageUrlField.setBackground(new Color(255, 255, 255, 200));
            imagePanel.add(imageUrlField);

            updateButton = new JButton("Update Image");
            updateButton.setMargin(new Insets(0, 160, 0, 0));
            updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            updateButton.setPreferredSize(new Dimension(150, 30));
            updateButton.setBackground(new Color(0, 0, 0));
            updateButton.setForeground(Color.WHITE);
            updateButton.setFocusPainted(false);
            updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            updateButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
            updateButton.addActionListener(e -> updateImageUrl(email));

            imagePanel.add(updateButton);

            infoPanel.add(imagePanel);
        }


        private void updateImageUrl(String email) {
            String newImageUrl = imageUrlField.getText();
            if (newImageUrl != null && !newImageUrl.trim().isEmpty()) {
                try (Connection conn = DatabaseAccessor.getConnection()) {
                    String updateQuery = "UPDATE users SET image_url = ? WHERE email = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                        stmt.setString(1, newImageUrl);
                        stmt.setString(2, email);
                        int rowsUpdated = stmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(this, "Image URL updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "No user found for the given email.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating image URL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Image URL cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = Toolkit.getDefaultToolkit().getImage(imagePath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading background image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new clientprofile().setVisible(true));
    }
}
