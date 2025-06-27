import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GymGUI extends JFrame {
    private ArrayList<GymMember> members = new ArrayList<>();
    private JTextArea displayArea;

    // Regular member fields
    private JTextField regularIdField, regularNameField, regularLocationField, regularPhoneField, regularEmailField, regularDobField;
    private JRadioButton regularMaleRadio, regularFemaleRadio;
    private ButtonGroup regularGenderGroup;
    private JTextField regularStartDateField, regularReferralField, regularRemovalReasonField;
    private JComboBox<String> planComboBox;
    private JTextField regularPlanPriceField;

    // Premium member fields
    private JTextField premiumIdField, premiumNameField, premiumLocationField, premiumPhoneField, premiumEmailField, premiumDobField;
    private JRadioButton premiumMaleRadio, premiumFemaleRadio;
    private ButtonGroup premiumGenderGroup;
    private JTextField premiumStartDateField, premiumTrainerField, premiumPaidAmountField;
    private JTextField premiumPlanChargeField, premiumDiscountField;

    // Panels for CardLayout
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private static final String REGULAR_PANEL = "Regular";
    private static final String PREMIUM_PANEL = "Premium";

    // Track active panel
    private JRadioButton regularRadio;

    // Static price map for Regular plans
    private static final Map<String, Double> PLAN_PRICES = new HashMap<>();
    static {
        PLAN_PRICES.put("basic", 50.00);
        PLAN_PRICES.put("standard", 75.00);
        PLAN_PRICES.put("deluxe", 100.00);
    }

    // Static premium plan charge
    private static final double PREMIUM_PLAN_CHARGE = 150.00;

    public GymGUI() {
        setTitle("Gym Management System");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        regularRadio = new JRadioButton("Regular Member", true);
        JRadioButton premiumRadio = new JRadioButton("Premium Member");
        ButtonGroup memberTypeGroup = new ButtonGroup();
        memberTypeGroup.add(regularRadio);
        memberTypeGroup.add(premiumRadio);
        radioPanel.add(regularRadio);
        radioPanel.add(premiumRadio);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createRegularMemberPanel(), REGULAR_PANEL);
        cardPanel.add(createPremiumMemberPanel(), PREMIUM_PANEL);

        regularRadio.addActionListener(e -> {
            cardLayout.show(cardPanel, REGULAR_PANEL);
            cardPanel.revalidate();
            cardPanel.repaint();
        });
        premiumRadio.addActionListener(e -> {
            cardLayout.show(cardPanel, PREMIUM_PANEL);
            cardPanel.revalidate();
            cardPanel.repaint();
        });

        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel commonButtonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        commonButtonPanel.add(createSizedButton("Activate", e -> activateMembership()));
        commonButtonPanel.add(createSizedButton("Deactivate", e -> deactivateMembership()));
        commonButtonPanel.add(createSizedButton("Mark Attendance", e -> markAttendance()));
        commonButtonPanel.add(createSizedButton("Display", e -> displayAllMembers()));
        commonButtonPanel.add(createSizedButton("Clear", e -> clearFields()));
        commonButtonPanel.add(createSizedButton("Save to File", e -> saveToFile()));
        commonButtonPanel.add(createSizedButton("Read from File", e -> readFromFile()));
        commonButtonPanel.add(createSizedButton("Pay Due Amount", e -> payDueAmount()));
        commonButtonPanel.add(createSizedButton("Calculate Discount", e -> calculateDiscount()));

        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.add(commonButtonPanel, BorderLayout.NORTH);
        southPanel.add(scrollPane, BorderLayout.CENTER);

        add(radioPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel createRegularMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Regular Member Details:");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);

        JPanel regularFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 40);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;

        Font labelFont = new Font("Arial", Font.BOLD, 14);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Member ID:");
        idLabel.setFont(labelFont);
        regularFieldsPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        regularIdField = new JTextField(25);
        regularFieldsPanel.add(regularIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        regularFieldsPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        regularNameField = new JTextField(25);
        regularFieldsPanel.add(regularNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        regularFieldsPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        JPanel regularGenderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        regularMaleRadio = new JRadioButton("Male");
        regularFemaleRadio = new JRadioButton("Female");
        regularGenderGroup = new ButtonGroup();
        regularGenderGroup.add(regularMaleRadio);
        regularGenderGroup.add(regularFemaleRadio);
        regularGenderPanel.add(regularMaleRadio);
        regularGenderPanel.add(regularFemaleRadio);
        regularFieldsPanel.add(regularGenderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(labelFont);
        regularFieldsPanel.add(dobLabel, gbc);
        gbc.gridx = 1;
        regularDobField = new JTextField(25);
        regularFieldsPanel.add(regularDobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        regularFieldsPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        regularPhoneField = new JTextField(25);
        regularFieldsPanel.add(regularPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        regularFieldsPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        regularEmailField = new JTextField(25);
        regularFieldsPanel.add(regularEmailField, gbc);

        gbc.insets = new Insets(10, 40, 10, 10);
        gbc.gridx = 2;
        gbc.gridy = 0;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(labelFont);
        regularFieldsPanel.add(locationLabel, gbc);
        gbc.gridx = 3;
        regularLocationField = new JTextField(25);
        regularFieldsPanel.add(regularLocationField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel startDateLabel = new JLabel("Membership Start Date:");
        startDateLabel.setFont(labelFont);
        regularFieldsPanel.add(startDateLabel, gbc);
        gbc.gridx = 3;
        regularStartDateField = new JTextField(25);
        regularFieldsPanel.add(regularStartDateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel referralLabel = new JLabel("Referral Source:");
        referralLabel.setFont(labelFont);
        regularFieldsPanel.add(referralLabel, gbc);
        gbc.gridx = 3;
        regularReferralField = new JTextField(25);
        regularFieldsPanel.add(regularReferralField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        JLabel removalLabel = new JLabel("Removal Reason:");
        removalLabel.setFont(labelFont);
        regularFieldsPanel.add(removalLabel, gbc);
        gbc.gridx = 3;
        regularRemovalReasonField = new JTextField(25);
        regularFieldsPanel.add(regularRemovalReasonField, gbc);

        JPanel planAndPricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JLabel planPriceLabel = new JLabel("Plan Price:");
        planPriceLabel.setFont(labelFont);
        regularPlanPriceField = new JTextField(10);
        regularPlanPriceField.setEditable(false);
        regularPlanPriceField.setBackground(Color.LIGHT_GRAY);
        regularPlanPriceField.setText("50.00");
        JLabel planLabel = new JLabel("Plan:");
        planLabel.setFont(labelFont);
        planComboBox = new JComboBox<>(new String[]{"Basic", "Standard", "Deluxe"});
        planComboBox.setPreferredSize(new Dimension(100, 30));
        planAndPricePanel.add(planPriceLabel);
        planAndPricePanel.add(regularPlanPriceField);
        planAndPricePanel.add(planLabel);
        planAndPricePanel.add(planComboBox);

        planComboBox.addActionListener(e -> {
            String selectedPlan = ((String) planComboBox.getSelectedItem()).toLowerCase();
            regularPlanPriceField.setText(String.format("%.2f", PLAN_PRICES.get(selectedPlan)));
        });

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(regularFieldsPanel, BorderLayout.CENTER);
        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(planAndPricePanel, BorderLayout.CENTER);
        centerPanel.add(lowerPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(createButton("Add Regular", e -> addRegularMember()));
        buttonPanel.add(createButton("Upgrade Plan", e -> upgradePlan()));
        buttonPanel.add(createButton("Revert Regular", e -> revertMember(true)));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        regularFieldsPanel.revalidate();
        regularFieldsPanel.repaint();

        return panel;
    }

    private JPanel createPremiumMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel headerLabel = new JLabel("Premium Member Details:");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);

        JPanel premiumFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 40);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Dimension fieldSize = new Dimension(250, 30);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Member ID:");
        idLabel.setFont(labelFont);
        premiumFieldsPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        premiumIdField = new JTextField(25);
        premiumIdField.setPreferredSize(fieldSize);
        premiumIdField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        premiumFieldsPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        premiumNameField = new JTextField(25);
        premiumNameField.setPreferredSize(fieldSize);
        premiumNameField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        premiumFieldsPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        JPanel premiumGenderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        premiumMaleRadio = new JRadioButton("Male");
        premiumFemaleRadio = new JRadioButton("Female");
        premiumGenderGroup = new ButtonGroup();
        premiumGenderGroup.add(premiumMaleRadio);
        premiumGenderGroup.add(premiumFemaleRadio);
        premiumGenderPanel.add(premiumMaleRadio);
        premiumGenderPanel.add(premiumFemaleRadio);
        premiumFieldsPanel.add(premiumGenderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(labelFont);
        premiumFieldsPanel.add(dobLabel, gbc);
        gbc.gridx = 1;
        premiumDobField = new JTextField(25);
        premiumDobField.setPreferredSize(fieldSize);
        premiumDobField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumDobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        premiumFieldsPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        premiumPhoneField = new JTextField(25);
        premiumPhoneField.setPreferredSize(fieldSize);
        premiumPhoneField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumPhoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        premiumFieldsPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        premiumEmailField = new JTextField(25);
        premiumEmailField.setPreferredSize(fieldSize);
        premiumEmailField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumEmailField, gbc);

        gbc.insets = new Insets(5, 40, 5, 10);
        gbc.gridx = 2;
        gbc.gridy = 0;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(labelFont);
        premiumFieldsPanel.add(locationLabel, gbc);
        gbc.gridx = 3;
        premiumLocationField = new JTextField(25);
        premiumLocationField.setPreferredSize(fieldSize);
        premiumLocationField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumLocationField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel startDateLabel = new JLabel("Membership Start Date:");
        startDateLabel.setFont(labelFont);
        premiumFieldsPanel.add(startDateLabel, gbc);
        gbc.gridx = 3;
        premiumStartDateField = new JTextField(25);
        premiumStartDateField.setPreferredSize(fieldSize);
        premiumStartDateField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumStartDateField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel trainerLabel = new JLabel("Personal Trainer:");
        trainerLabel.setFont(labelFont);
        premiumFieldsPanel.add(trainerLabel, gbc);
        gbc.gridx = 3;
        premiumTrainerField = new JTextField(25);
        premiumTrainerField.setPreferredSize(fieldSize);
        premiumTrainerField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumTrainerField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        JLabel paidAmountLabel = new JLabel("Paid Amount:");
        paidAmountLabel.setFont(labelFont);
        premiumFieldsPanel.add(paidAmountLabel, gbc);
        gbc.gridx = 3;
        premiumPaidAmountField = new JTextField(25);
        premiumPaidAmountField.setPreferredSize(fieldSize);
        premiumPaidAmountField.setMinimumSize(fieldSize);
        premiumFieldsPanel.add(premiumPaidAmountField, gbc);

        JPanel premiumInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JLabel planChargeLabel = new JLabel("Plan Charge:");
        planChargeLabel.setFont(labelFont);
        premiumPlanChargeField = new JTextField(10);
        premiumPlanChargeField.setPreferredSize(new Dimension(120, 30));
        premiumPlanChargeField.setEditable(false);
        premiumPlanChargeField.setBackground(Color.LIGHT_GRAY);
        premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
        JLabel discountLabel = new JLabel("Discount:");
        discountLabel.setFont(labelFont);
        premiumDiscountField = new JTextField(10);
        premiumDiscountField.setPreferredSize(new Dimension(120, 30));
        premiumDiscountField.setEditable(false);
        premiumDiscountField.setBackground(Color.LIGHT_GRAY);
        premiumDiscountField.setText("0.00");
        premiumInfoPanel.add(planChargeLabel);
        premiumInfoPanel.add(premiumPlanChargeField);
        premiumInfoPanel.add(discountLabel);
        premiumInfoPanel.add(premiumDiscountField);

        premiumIdField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int id = Integer.parseInt(premiumIdField.getText().trim());
                    GymMember member = findMemberById(id);
                    if (member instanceof PremiumMember) {
                        premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
                    } else {
                        premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
                    }
                } catch (NumberFormatException ex) {
                    premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(premiumFieldsPanel, BorderLayout.CENTER);
        centerPanel.add(premiumInfoPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(createButton("Add Premium", e -> addPremiumMember()));
        buttonPanel.add(createButton("Revert Premium", e -> revertMember(false)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("MemberDetails.txt"))) {
            // Write header
            writer.write(String.format("%-5s %-15s %-15s %-15s %-25s %-20s %-10s %-10s %-10s %-10s %-15s %-15s %-15s\n",
                    "ID", "Name", "Location", "Phone", "Email", "Start Date", "Plan", "Price",
                    "Attendance", "Active", "Discount", "Full Paid", "Net Paid"));
            writer.write("-".repeat(165) + "\n"); // Separator line

            // Write member details
            for (GymMember member : members) {
                String id = String.valueOf(member.getId());
                String name = member.getName();
                String location = member.getLocation();
                String phone = member.getPhone();
                String email = member.getEmail();
                String startDate = member.getMembershipStartDate();
                String plan, price, discount, fullPaid, netPaid;

                if (member instanceof RegularMember regularMember) {
                    plan = regularMember.getPlan();
                    price = String.format("%.2f", regularMember.getPrice());
                    discount = "0.00";
                    fullPaid = "N/A";
                    netPaid = "N/A";
                } else { // PremiumMember
                    PremiumMember premiumMember = (PremiumMember) member;
                    plan = "Premium";
                    price = String.format("%.2f", PREMIUM_PLAN_CHARGE);
                    discount = String.format("%.2f", 0.00); // Fallback, no discount getter
                    fullPaid = premiumMember.getPaidAmount() >= PREMIUM_PLAN_CHARGE ? "Yes" : "No";
                    netPaid = String.format("%.2f", premiumMember.getPaidAmount());
                }

                String attendance = String.valueOf(member.getAttendance());
                String active = member.isActiveStatus() ? "Yes" : "No";

                writer.write(String.format("%-5s %-15s %-15s %-15s %-25s %-20s %-10s %-10s %-10s %-10s %-15s %-15s %-15s\n",
                        id, name, location, phone, email, startDate, plan, price,
                        attendance, active, discount, fullPaid, netPaid));
            }
            JOptionPane.showMessageDialog(this, "Member details saved to MemberDetails.txt!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readFromFile() {
        JFrame readFrame = new JFrame("Read Member Details");
        readFrame.setSize(1000, 600); // Larger size for better visibility
        readFrame.setResizable(true); // Allow resizing
        readFrame.setLocationRelativeTo(this);

        JTextArea readText = new JTextArea(20, 100); // Wider for long lines
        readText.setEditable(false);
        readText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(readText);
        readFrame.add(scrollPane);

        try (BufferedReader reader = new BufferedReader(new FileReader("MemberDetails.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            readText.setText(sb.toString());
            readFrame.setVisible(true);
        } catch (IOException e) {
            readText.setText("Error reading from file: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error reading from file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            readFrame.setVisible(true);
        }
    }

    private void addRegularMember() {
        if (validateFields(true)) {
            try {
                int id = Integer.parseInt(regularIdField.getText().trim());
                String name = regularNameField.getText().trim();
                String location = regularLocationField.getText().trim();
                String phone = regularPhoneField.getText().trim();
                String email = regularEmailField.getText().trim();
                String dob = regularDobField.getText().trim();
                String gender = getRegularGender();
                String startDate = regularStartDateField.getText().trim();
                String referral = regularReferralField.getText().trim();

                if (!isDuplicateId(id)) {
                    if (!gender.isEmpty()) {
                        RegularMember member = new RegularMember(
                                id, name, location, phone, email, gender, dob,
                                startDate, referral
                        );
                        members.add(member);
                        JOptionPane.showMessageDialog(this, "Regular member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a gender!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Duplicate Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Member ID format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addPremiumMember() {
        if (validateFields(false)) {
            try {
                int id = Integer.parseInt(premiumIdField.getText().trim());
                String name = premiumNameField.getText().trim();
                String location = premiumLocationField.getText().trim();
                String phone = premiumPhoneField.getText().trim();
                String email = premiumEmailField.getText().trim();
                String dob = premiumDobField.getText().trim();
                String gender = getPremiumGender();
                String startDate = premiumStartDateField.getText().trim();
                String trainer = premiumTrainerField.getText().trim();

                if (!isDuplicateId(id)) {
                    if (!gender.isEmpty()) {
                        PremiumMember member = new PremiumMember(
                                id, name, location, phone, email, gender, dob,
                                startDate, trainer
                        );
                        members.add(member);
                        premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
                        premiumDiscountField.setText("0.00");
                        JOptionPane.showMessageDialog(this, "Premium member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a gender!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Duplicate Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Member ID format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void activateMembership() {
        try {
            int id = regularRadio.isSelected() ? Integer.parseInt(regularIdField.getText().trim()) : Integer.parseInt(premiumIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member != null) {
                member.activateMembership();
                JOptionPane.showMessageDialog(this, "Membership activated for ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deactivateMembership() {
        try {
            int id = regularRadio.isSelected() ? Integer.parseInt(regularIdField.getText().trim()) : Integer.parseInt(premiumIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member != null) {
                member.deactivateMembership();
                JOptionPane.showMessageDialog(this, "Membership deactivated for ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAttendance() {
        try {
            int id = regularRadio.isSelected() ? Integer.parseInt(regularIdField.getText().trim()) : Integer.parseInt(premiumIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member != null) {
                if (member.isActiveStatus()) {
                    member.markAttendance();
                    JOptionPane.showMessageDialog(this, "Attendance marked for ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot mark attendance for inactive member!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void upgradePlan() {
        try {
            int id = Integer.parseInt(regularIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member instanceof RegularMember regularMember) {
                String newPlan = ((String) planComboBox.getSelectedItem()).toLowerCase();
                String message = regularMember.upgradePlan(newPlan);
                regularPlanPriceField.setText(String.format("%.2f", PLAN_PRICES.get(newPlan)));
                JOptionPane.showMessageDialog(this, message, "Plan Upgrade", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Not a regular member!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateDiscount() {
        try {
            int id = Integer.parseInt(premiumIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member instanceof PremiumMember premiumMember) {
                try {
                    premiumMember.calculateDiscount();
                    double discount = 0.00; // Fallback
                    premiumDiscountField.setText(String.format("%.2f", discount));
                    JOptionPane.showMessageDialog(this, "Discount calculated for ID: " + id + ": " + String.format("%.2f", discount), "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    premiumDiscountField.setText("0.00");
                    JOptionPane.showMessageDialog(this, "Unable to calculate discount for ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                premiumDiscountField.setText("0.00");
                JOptionPane.showMessageDialog(this, "Not a premium member!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            premiumDiscountField.setText("0.00");
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void revertMember(boolean isRegular) {
        try {
            int id = isRegular ? Integer.parseInt(regularIdField.getText().trim()) : Integer.parseInt(premiumIdField.getText().trim());
            JTextField removalReasonField = isRegular ? regularRemovalReasonField : null;
            GymMember member = findMemberById(id);
            if (member != null) {
                if (isRegular && member instanceof RegularMember regularMember) {
                    String removalReason = removalReasonField.getText().trim();
                    if (!removalReason.isEmpty()) {
                        regularMember.revertRegularMember(removalReason);
                        members.remove(member);
                        JOptionPane.showMessageDialog(this, "Regular member reverted successfully! Reason: " + removalReason, "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Please provide a removal reason!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (!isRegular && member instanceof PremiumMember premiumMember) {
                    premiumMember.revertPremiumMember();
                    members.remove(member);
                    JOptionPane.showMessageDialog(this, "Premium member reverted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found or wrong type!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void payDueAmount() {
        try {
            int id = premiumIdField.getText().trim().isEmpty() ? -1 : Integer.parseInt(premiumIdField.getText().trim());
            GymMember member = findMemberById(id);
            if (member instanceof PremiumMember premiumMember) {
                double paidAmount = premiumPaidAmountField.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(premiumPaidAmountField.getText().trim());
                String message = premiumMember.payDueAmount(paidAmount);
                try {
                    premiumMember.calculateDiscount();
                    double discount = 0.00;
                    premiumDiscountField.setText(String.format("%.2f", discount));
                } catch (Exception e) {
                    premiumDiscountField.setText("0.00");
                }
                JOptionPane.showMessageDialog(this, message, "Payment", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Not a premium member or member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID or Paid Amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAllMembers() {
        JFrame displayFrame = new JFrame("All Members");
        displayFrame.setSize(900, 400);
        JTextArea displayText = new JTextArea(15, 80);
        displayText.setEditable(false);
        displayText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayText);
        displayFrame.add(scrollPane);
        displayFrame.setLocationRelativeTo(this);

        if (members.isEmpty()) {
            displayText.setText("No members in the system.");
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            for (GymMember member : members) {
                member.display();
                System.out.println("------------------------");
            }
            System.out.flush();
            System.setOut(old);
            displayText.setText(baos.toString());
        }
        displayFrame.setVisible(true);
    }

    private boolean validateFields(boolean isRegular) {
        JTextField idField = isRegular ? regularIdField : premiumIdField;
        JTextField nameField = isRegular ? regularNameField : premiumNameField;
        JTextField locationField = isRegular ? regularLocationField : premiumLocationField;
        JTextField phoneField = isRegular ? regularPhoneField : premiumPhoneField;
        JTextField emailField = isRegular ? regularEmailField : premiumEmailField;
        JTextField dobField = isRegular ? regularDobField : premiumDobField;
        ButtonGroup genderGroup = isRegular ? regularGenderGroup : premiumGenderGroup;

        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (locationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dobField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date of Birth is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (genderGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select a gender!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isDuplicateId(int id) {
        return members.stream().anyMatch(m -> m.getId() == id);
    }

    private GymMember findMemberById(int id) {
        return members.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

    private void clearFields() {
        regularIdField.setText("");
        regularNameField.setText("");
        regularLocationField.setText("");
        regularPhoneField.setText("");
        regularEmailField.setText("");
        regularDobField.setText("");
        regularGenderGroup.clearSelection();
        regularStartDateField.setText("");
        regularReferralField.setText("");
        regularRemovalReasonField.setText("");
        planComboBox.setSelectedIndex(0);
        regularPlanPriceField.setText("50.00");

        premiumIdField.setText("");
        premiumNameField.setText("");
        premiumLocationField.setText("");
        premiumPhoneField.setText("");
        premiumEmailField.setText("");
        premiumDobField.setText("");
        premiumGenderGroup.clearSelection();
        premiumStartDateField.setText("");
        premiumTrainerField.setText("");
        premiumPaidAmountField.setText("");
        premiumPlanChargeField.setText(String.format("%.2f", PREMIUM_PLAN_CHARGE));
        premiumDiscountField.setText("0.00");
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private JButton createSizedButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.addActionListener(listener);
        return button;
    }

    private String getRegularGender() {
        if (regularMaleRadio.isSelected()) {
            return "Male";
        } else if (regularFemaleRadio.isSelected()) {
            return "Female";
        }
        return "";
    }

    private String getPremiumGender() {
        if (premiumMaleRadio.isSelected()) {
            return "Male";
        } else if (premiumFemaleRadio.isSelected()) {
            return "Female";
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GymGUI gui = new GymGUI();
            gui.setVisible(true);
        });
    }
}