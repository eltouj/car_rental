import config.DatabaseAccessor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminInterface3 extends JFrame {



    public AdminInterface3() {
        setTitle("View Users");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width - 1, screenSize.height - 1);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.BLACK);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        JButton addCarsButton = createNavButton("Add Cars");
        JButton viewCarsButton = createNavButton("View Cars");
        JButton viewUsersButton = createNavButton("View Users");

        addCarsButton.addActionListener(e -> {
            new AdminInterface().setVisible(true);
            dispose();
        });
        viewCarsButton.addActionListener(e -> {
            new AdminInterface2().setVisible(true);
            dispose();
        });

        navPanel.add(addCarsButton);
        navPanel.add(viewCarsButton);
        navPanel.add(viewUsersButton);
        navPanel.add(closeButton);
        backgroundPanel.add(navPanel, BorderLayout.NORTH);

        ViewUsersPanel viewUsersPanel = new ViewUsersPanel();
        backgroundPanel.add(viewUsersPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 255));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 240));
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0, 255));
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    public class ViewUsersPanel extends JPanel {
        private JTextField imageUrlField;
        private JButton updateButton;


        public ViewUsersPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());
            JPanel titlePanel = new JPanel(new FlowLayout());
            titlePanel.setOpaque(false);
            JLabel titleLabel = new JLabel("Registered Users");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
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
                String query = "SELECT * FROM users";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        JPanel userCard = new JPanel();
                        userCard.setLayout(new BorderLayout());
                        userCard.setBorder(new LineBorder(Color.WHITE, 2, true));
                        userCard.setBackground(new Color(0, 0, 0, 150));
                        userCard.setMaximumSize(new Dimension(500, 250));
                        userCard.setPreferredSize(new Dimension(500, 250));

                        JPanel infoPanel = new JPanel();
                        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
                        infoPanel.setBackground(new Color(0, 0, 0, 150));
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

                        JLabel usernameLabel = createCardLabel("Username: " + rs.getString("name"));
                        JLabel emailLabel = createCardLabel("Email: " + rs.getString("email"));
                        infoPanel.add(usernameLabel);
                        infoPanel.add(emailLabel);

                        if (rs.getString("email").equals(Auth.mail)) {
                            JLabel statusLabel = createCardLabel("Status: Active");
                            statusLabel.setForeground(new Color(0, 255, 0));
                            infoPanel.add(statusLabel);

                            addImageUrlField(infoPanel, rs.getString("image_url"), rs.getString("email"));
                        } else {
                            JLabel statusLabel = createCardLabel("Status: Inactive");
                            statusLabel.setForeground(new Color(255, 0, 0));
                            infoPanel.add(statusLabel);
                        }

                        JPanel imagePanel = new JPanel();
                        imagePanel.setLayout(new BorderLayout());
                        imagePanel.setBackground(Color.BLACK);
                        imagePanel.setPreferredSize(new Dimension(200, 200));
                        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

                        JLabel imageLabel = new JLabel();

                        String imagePath = rs.getString("image_url");
                        if (imagePath != null && !imagePath.isEmpty()) {
                            ImageIcon userImage = new ImageIcon(imagePath);
                            Image scaledImage = userImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                        } else {
                            imageLabel.setIcon(new ImageIcon(new ImageIcon("src/placeholder.jpg").getImage()
                                    .getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
                        }

                        imagePanel.add(imageLabel, BorderLayout.NORTH);

                        userCard.add(infoPanel, BorderLayout.CENTER);
                        userCard.add(imagePanel, BorderLayout.EAST);

                        cardPanel.add(userCard);
                        cardPanel.add(Box.createVerticalStrut(20));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void addImageUrlField(JPanel infoPanel, String currentImageUrl, String email) {
            JLabel imageUrlLabel = new JLabel("Image URL:");
            imageUrlLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            imageUrlLabel.setForeground(Color.WHITE);
            infoPanel.add(imageUrlLabel);

            imageUrlField = new JTextField(10);
            imageUrlField.setText(currentImageUrl);
            infoPanel.add(imageUrlField);

            imageUrlField.setPreferredSize(new Dimension(200, 30));
            updateButton = new JButton("Update Image");
            updateButton.setFont(new Font("Arial", Font.BOLD, 16));
            updateButton.setBackground(new Color(0, 0, 0, 255));
            updateButton.setForeground(Color.WHITE);
            updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            updateButton.setFocusPainted(false);
            updateButton.addActionListener(e -> updateImageUrl(email));
            infoPanel.add(updateButton);
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
                            JOptionPane.showMessageDialog(this, "Image updated successfully!");
                            loadUserData((JPanel) getComponent(1));
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update image.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error updating image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid image URL.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private JLabel createCardLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            return label;
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                e.printStackTrace();
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
        SwingUtilities.invokeLater(() -> {
            AdminInterface3 frame = new AdminInterface3();
            frame.setVisible(true);
        });
    }
}
