import config.DatabaseAccessor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;

public class clientrented extends JFrame {

    public clientrented() {
        setTitle("View Rented Cars");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width - 1, screenSize.height - 1);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton closeButton = createCloseButton();
        BackgroundPanel backgroundPanel = new BackgroundPanel("src/img.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setOpaque(false);

        JButton profile = createNavButton("View Profile");
        JButton rent = createNavButton("Rent");

        profile.addActionListener(e -> {
            new clientprofile().setVisible(true);
            dispose();
        });
        rent.addActionListener(e -> {
            new clientloc().setVisible(true);
            dispose();
        });

        navPanel.add(profile);
        navPanel.add(rent);
        navPanel.add(closeButton);
        backgroundPanel.add(navPanel, BorderLayout.NORTH);

        ViewRentedCarsPanel viewRentedCarsPanel = new ViewRentedCarsPanel();
        backgroundPanel.add(viewRentedCarsPanel, BorderLayout.CENTER);
    }

    private JButton createCloseButton() {
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.BLACK);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.addActionListener(e -> System.exit(0));
        return closeButton;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0));
                button.setForeground(Color.white);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0));
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    public class ViewRentedCarsPanel extends JPanel {
        public ViewRentedCarsPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel(new FlowLayout());
            titlePanel.setOpaque(false);
            JLabel titleLabel = new JLabel("Rented Cars");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel);
            add(titlePanel, BorderLayout.NORTH);

            JPanel cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setOpaque(false);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JScrollPane scrollPane = new JScrollPane(cardPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);

            loadRentedCarData(cardPanel);
        }

        private void loadRentedCarData(JPanel cardPanel) {
            try (Connection conn = DatabaseAccessor.getConnection()) {
                if (conn == null || conn.isClosed()) {
                    throw new SQLException("Database connection is not available.");
                }

                String query = """
                        SELECT id, brand, name, hp, transmission, category, car_statut, image_url, 'Unavailable' AS source
                        FROM available_cars
                        WHERE car_statut = 1
                        """;

                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        JPanel carCard = createCarCard(rs, cardPanel);
                        cardPanel.add(carCard);
                        cardPanel.add(Box.createVerticalStrut(20));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading rented car data: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private JPanel createCarCard(ResultSet rs, JPanel cardPanel) throws SQLException {
            JPanel carCard = new JPanel(new BorderLayout());
            carCard.setBorder(new LineBorder(Color.WHITE, 2, true));
            carCard.setBackground(new Color(0, 0, 0));
            carCard.setMaximumSize(new Dimension(600, 250));
            carCard.setPreferredSize(new Dimension(600, 250));

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(0, 0, 0));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

            int carId = rs.getInt("id");
            String carName = rs.getString("name");
            String carStatus = rs.getString("source");

            JLabel brandLabel = createCardLabel("Brand: " + rs.getString("brand"), 20);
            JLabel nameLabel = createCardLabel("Name: " + carName, 20);
            JLabel statusLabel = createCardLabel("Status: " + carStatus, 20);
            if (rs.getString("car_statut").equals("0")) {
                statusLabel.setForeground(new Color(0, 255, 0));
            } else {
                statusLabel.setForeground(new Color(255, 0, 0));
            }

            JButton rentButton = new JButton("Unrent");
            rentButton.setFont(new Font("Arial", Font.BOLD, 18));

            rentButton.addActionListener(e -> {
                handleUnrentAction(carId, carStatus, cardPanel, carCard, rentButton);
                cardPanel.remove(carCard);
                cardPanel.revalidate();
                cardPanel.repaint();
            });

            infoPanel.add(brandLabel);
            infoPanel.add(nameLabel);
            infoPanel.add(statusLabel);
            infoPanel.add(rentButton);

            JPanel imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            String imageUrl = rs.getString("image_url");
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage();
            Image resizedImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
            imagePanel.add(imageLabel);

            carCard.add(infoPanel, BorderLayout.WEST);
            carCard.add(imagePanel, BorderLayout.EAST);

            return carCard;
        }

        private void handleUnrentAction(int carId, String carStatus, JPanel cardPanel, JPanel carCard, JButton rentButton) {
            SwingUtilities.invokeLater(() -> {
                try (Connection conn = DatabaseAccessor.getConnection()) {
                    if (conn == null || conn.isClosed()) {
                        throw new SQLException("Database connection is not available.");
                    }

                    conn.setAutoCommit(false);

                    String updateQuery = "UPDATE available_cars SET car_statut = 0 WHERE id = ?";

                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, carId);
                        updateStmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(null, "Car returned to available successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    conn.commit();

                    rentButton.setText("Rent");

                    rentButton.revalidate();
                    rentButton.repaint();

                    cardPanel.revalidate();
                    cardPanel.repaint();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private JLabel createCardLabel(String text, int fontSize) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, fontSize));
            label.setForeground(Color.WHITE);
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
            clientrented frame = new clientrented();
            frame.setVisible(true);
        });
    }
}
