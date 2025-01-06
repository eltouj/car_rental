import config.DatabaseAccessor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminInterface2 extends JFrame {
    private final JComboBox<String> sort = new JComboBox<>(new String[]{"name", "hp", "car_statut"});
    private final JComboBox<String> sortd = new JComboBox<>(new String[]{"ASC", "DESC"});
    private static String sel = "name";
    private static String ord = "ASC";
    private JScrollPane scrollPane;

    public AdminInterface2() {
        setTitle("View Cars");
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
        viewUsersButton.addActionListener(e -> {
            new AdminInterface3().setVisible(true);
            dispose();
        });

        navPanel.add(addCarsButton);
        navPanel.add(viewCarsButton);
        navPanel.add(viewUsersButton);
        navPanel.add(closeButton);

        sort.setBounds(150,190,200,30);
        sortd.setBounds(150,240,200,30);
        sort.setBackground(new Color(0, 0, 0, 180));
        sort.setForeground(Color.WHITE);
        sort.setFont(new Font("SansSerif", Font.ITALIC, 17));
        sort.setBorder(null);


        sortd.setBackground(new Color(0, 0, 0, 180));
        sortd.setForeground(Color.WHITE);
        sortd.setFont(new Font("SansSerif", Font.ITALIC, 17));
        sortd.setBorder(null);

        backgroundPanel.add(navPanel, BorderLayout.NORTH);
        backgroundPanel.add(sort, BorderLayout.CENTER);
        backgroundPanel.add(sortd, BorderLayout.CENTER);


        ViewCarsPanel viewCarsPanel = new ViewCarsPanel();
        backgroundPanel.add(viewCarsPanel, BorderLayout.CENTER);

        sort.addActionListener(e -> {
            sel = sort.getSelectedItem().toString();
            viewCarsPanel.reloadCarData();
        });

        sortd.addActionListener(e -> {
            ord = sortd.getSelectedItem().toString();
            viewCarsPanel.reloadCarData();
        });
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

    public class ViewCarsPanel extends JPanel {
        private JPanel cardPanel;

        public ViewCarsPanel() {
            setOpaque(false);
            setLayout(new BorderLayout());

            JPanel titlePanel = new JPanel(new FlowLayout());
            titlePanel.setOpaque(false);
            JLabel titleLabel = new JLabel("Available Cars");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel);

            add(titlePanel, BorderLayout.NORTH);

            cardPanel = new JPanel();
            cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
            cardPanel.setOpaque(false);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            scrollPane = new JScrollPane(cardPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            add(scrollPane, BorderLayout.CENTER);

            loadCarData();
        }

        private void loadCarData() {
            try (Connection conn = DatabaseAccessor.getConnection()) {
                String query = "SELECT * FROM available_cars ORDER BY " + sel + " " + ord;
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    cardPanel.removeAll();
                    while (rs.next()) {
                        JPanel carCard = new JPanel();
                        carCard.setLayout(new BorderLayout());
                        carCard.setBorder(new LineBorder(Color.WHITE, 2, true));
                        carCard.setBackground(new Color(0, 0, 0, 150));

                        carCard.setMaximumSize(new Dimension(600, 250));
                        carCard.setPreferredSize(new Dimension(600, 250));

                        JPanel infoPanel = new JPanel();
                        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                        infoPanel.setBackground(new Color(0, 0, 0, 150));
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

                        JLabel brandLabel = createCardLabel("Brand: " + rs.getString("brand"));
                        JLabel nameLabel = createCardLabel("Name: " + rs.getString("name"));
                        JLabel hpLabel = createCardLabel("HP: " + rs.getInt("hp"));
                        JLabel transmissionLabel = createCardLabel("Transmission: " + rs.getString("transmission"));
                        JLabel categoryLabel = createCardLabel("Category: " + rs.getString("category"));
                        JLabel statusLabel = createCardLabel(
                                "Status: " + (rs.getString("car_statut").equals("true") ? "Unvailable" : "Available"));

                        if (rs.getString("car_statut").equals("0")) {
                            statusLabel.setForeground(new Color(0, 255, 0));
                        } else {
                            statusLabel.setForeground(new Color(255, 0, 0));
                        }

                        infoPanel.add(brandLabel);
                        infoPanel.add(nameLabel);
                        infoPanel.add(hpLabel);
                        infoPanel.add(transmissionLabel);
                        infoPanel.add(categoryLabel);
                        infoPanel.add(statusLabel);

                        JPanel imagePanel = new JPanel();
                        imagePanel.setLayout(new BorderLayout());
                        imagePanel.setBackground(Color.BLACK);
                        imagePanel.setPreferredSize(new Dimension(300, 300));
                        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 10));

                        JLabel imageLabel = new JLabel();

                        String imagePath = rs.getString("image_url");
                        if (imagePath != null && !imagePath.isEmpty()) {
                            ImageIcon carImage = new ImageIcon(imagePath);
                            Image scaledImage = carImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                        } else {
                            imageLabel.setIcon(new ImageIcon(new ImageIcon("src/placeholder.jpg").getImage()
                                    .getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
                        }

                        imagePanel.add(imageLabel, BorderLayout.NORTH);

                        carCard.add(infoPanel, BorderLayout.CENTER);
                        carCard.add(imagePanel, BorderLayout.EAST);

                        cardPanel.add(carCard);
                        cardPanel.add(Box.createVerticalStrut(20));
                    }
                    cardPanel.revalidate();
                    cardPanel.repaint();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading car data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private JLabel createCardLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            return label;
        }

        public void reloadCarData() {
            loadCarData();
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
            AdminInterface2 frame = new AdminInterface2();
            frame.setVisible(true);
        });
    }
}
