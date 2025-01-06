import config.DatabaseAccessor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminInterface extends JFrame {



    private CardLayout cardLayout;
    private JPanel mainPanel;

    public AdminInterface() {
        setTitle("Admin Interface");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width - 1, screenSize.height - 1);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.black);
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

        viewUsersButton.addActionListener(e -> {
            new AdminInterface3().setVisible(true);
            dispose();
        });
        viewCarsButton.addActionListener(e -> {
            new AdminInterface2().setVisible(true);
            dispose();
        });


        navPanel.add(addCarsButton);
        navPanel.add(viewCarsButton);
        navPanel.add(viewUsersButton);
        backgroundPanel.add(navPanel, BorderLayout.NORTH);
        navPanel.add(closeButton, BorderLayout.NORTH);

        AddCarsPanel addCarsPanel = new AddCarsPanel();
        backgroundPanel.add(addCarsPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 255));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 240));
                button.setForeground(Color.black);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0, 255));
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }

    public class AddCarsPanel extends JPanel {
        public AddCarsPanel() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(new Color(0, 0, 0, 150));
            formPanel.setPreferredSize(new Dimension(600, 450));
            formPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255), 2),
                    "Add a New Car", TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 24), new Color(255, 255, 255)));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 30, 20, 20);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;

            JLabel brandLabel = createLabel("Car Brand:");
            JComboBox<String> brandBox = new JComboBox<>(new String[]{"Toyota", "Honda", "BMW", "Ford","Mercedes", "Audi", "Skoda", "Hyundai"});
            brandBox.setPreferredSize(new Dimension(200, 30));

            JLabel nameLabel = createLabel("Name of Car:");
            JTextField nameField = new JTextField(20);
            nameField.setPreferredSize(new Dimension(250, 30));

            JLabel horsepowerLabel = createLabel("Horsepower:");
            JTextField horsepowerField = new JTextField(20);
            horsepowerField.setPreferredSize(new Dimension(250, 30));

            JLabel transmissionLabel = createLabel("Transmission:");
            JComboBox<String> transmissionBox = new JComboBox<>(new String[]{"Manual", "Automatic"});
            transmissionBox.setPreferredSize(new Dimension(200, 30));

            JLabel categoryLabel = createLabel("Category:");
            JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Economy", "Compact", "SUV", "Luxury","Sedan"});
            categoryBox.setPreferredSize(new Dimension(200, 30));

            JLabel imageUrlLabel = createLabel("Image URL:");
            JTextField imageUrlField = new JTextField(20);
            imageUrlField.setPreferredSize(new Dimension(250, 30));

            JButton submitButton = new JButton("Add Car");
            submitButton.setBackground(new Color(255, 255, 255, 255));
            submitButton.setForeground(Color.BLACK);
            submitButton.setFont(new Font("Arial", Font.BOLD, 16));
            submitButton.setFocusPainted(false);
            submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String brand = (String) brandBox.getSelectedItem();
                    String name = nameField.getText();
                    String horsepower = horsepowerField.getText();
                    String transmission = (String) transmissionBox.getSelectedItem();
                    String category = (String) categoryBox.getSelectedItem();
                    String imageUrl = imageUrlField.getText();

                    if (name.isEmpty() || horsepower.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        insertCarData(brand, name, horsepower, transmission, category, imageUrl);
                    }
                }
            });

            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(brandLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(brandBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(nameLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(horsepowerLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(horsepowerField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            formPanel.add(transmissionLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(transmissionBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            formPanel.add(categoryLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(categoryBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            formPanel.add(imageUrlLabel, gbc);
            gbc.gridx = 1;
            formPanel.add(imageUrlField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(submitButton, gbc);

            add(formPanel);
        }

        private void insertCarData(String brand, String name, String hp, String transmission, String category, String imageUrl) {
            try (Connection conn = DatabaseAccessor.getConnection()) {
                String sql = "INSERT INTO available_cars (brand, name, hp, transmission, category, car_statut, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, brand);
                    stmt.setString(2, name);
                    stmt.setInt(3, Integer.parseInt(hp));
                    stmt.setString(4, transmission);
                    stmt.setString(5, category);
                    stmt.setString(6, String.valueOf(0));
                    stmt.setString(7, imageUrl);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Car added successfully!");

                        new AdminInterface().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add car.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(new Color(255, 255, 255));
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
            AdminInterface frame = new AdminInterface();
            frame.setVisible(true);
        });
    }
}
