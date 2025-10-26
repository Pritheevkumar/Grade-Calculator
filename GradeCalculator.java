import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A modern, dark-themed graphical grade calculator application using Java Swing.
 * Allows users to dynamically add and remove subject rows, enter marks,
 * and calculate both individual subject grades and an overall grade.
 *
 * Grading Scale:
 * O: 95%+
 * A+: 90%+
 * A: 85%+
 * B+: 80%+
 * B: 75%+
 * C+: 70%+
 * C: 65%+
 * F: Below 65%
 */
public class GradeCalculator extends JFrame {

    // --- Modern Dark Theme Colors ---
    private static final Color COLOR_BACKGROUND = new Color(30, 30, 45); // Dark blue-gray
    private static final Color COLOR_PANEL = new Color(45, 45, 65);      // Slightly lighter panel
    private static final Color COLOR_TEXT = new Color(230, 230, 245);    // Off-white
    private static final Color COLOR_PRIMARY_BUTTON = new Color(70, 130, 180); // Steel Blue
    private static final Color COLOR_DANGER_BUTTON = new Color(180, 80, 80);  // Soft Red
    private static final Color COLOR_GRADE_PASS = new Color(80, 180, 100);  // Soft Green
    private static final Color COLOR_GRADE_FAIL = new Color(200, 100, 100); // Soft Red (for F)

    // Font definitions
    private static final Font FONT_BODY = new Font("Arial", Font.PLAIN, 14);
    private static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 18);
    private static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);

    // List to hold all the subject row panels
    private ArrayList<SubjectEntryPanel> subjectEntries;

    // Main GUI components
    private JPanel mainPanel;
    private JPanel subjectsListPanel; // Panel to hold the dynamic subject rows
    private JScrollPane scrollPane;
    private JButton addSubjectButton;
    private JButton calculateButton;

    // Labels for displaying results
    private JLabel totalMarksLabel;
    private JLabel percentageLabel;
    private JLabel gradeLabel;

    /**
     * Constructor: Sets up the entire GUI.
     */
    public GradeCalculator() {
        // --- 1. Set up the Main Window (JFrame) ---
        setTitle("Modern Grade Calculator");
        setSize(750, 600); // Increased width for the remove button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(COLOR_BACKGROUND);

        // --- 2. Create Main Panel ---
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10)); // Gaps between components
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for the whole window
        add(mainPanel);

        // --- 3. Create Header ---
        JLabel headerLabel = new JLabel("Student Grade Calculator");
        headerLabel.setFont(FONT_HEADER);
        headerLabel.setForeground(COLOR_TEXT);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // --- 4. Create Subjects List Panel (Dynamic) ---
        subjectsListPanel = new JPanel();
        subjectsListPanel.setLayout(new BoxLayout(subjectsListPanel, BoxLayout.Y_AXIS)); // Stack rows vertically
        subjectsListPanel.setBackground(COLOR_PANEL);
        subjectEntries = new ArrayList<>();

        // Make it scrollable
        scrollPane = new JScrollPane(subjectsListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY_BUTTON, 1));
        scrollPane.getViewport().setBackground(COLOR_PANEL);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- 5. Create Bottom Panel (Buttons and Results) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Panel for the control buttons (Add, Calculate)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(COLOR_BACKGROUND);

        addSubjectButton = new JButton("Add New Subject");
        styleButton(addSubjectButton, COLOR_PRIMARY_BUTTON);
        addSubjectButton.addActionListener(e -> addSubjectRow());
        buttonPanel.add(addSubjectButton);

        calculateButton = new JButton("Calculate");
        styleButton(calculateButton, COLOR_PRIMARY_BUTTON);
        calculateButton.setFont(FONT_LABEL); // Make it stand out
        calculateButton.addActionListener(e -> calculateGrades());
        getRootPane().setDefaultButton(calculateButton); // Press Enter to calculate
        buttonPanel.add(calculateButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Panel for the results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns for results
        resultsPanel.setBackground(COLOR_BACKGROUND);
        resultsPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Top padding

        totalMarksLabel = new JLabel("Total: -- / --");
        styleResultLabel(totalMarksLabel);
        percentageLabel = new JLabel("Percentage: --%");
        styleResultLabel(percentageLabel);
        gradeLabel = new JLabel("Overall Grade: --");
        styleResultLabel(gradeLabel);

        resultsPanel.add(totalMarksLabel);
        resultsPanel.add(percentageLabel);
        resultsPanel.add(gradeLabel);
        bottomPanel.add(resultsPanel, BorderLayout.CENTER);

        // --- 6. Finalize Window ---
        addSubjectRow(); // Add one subject row to start
        setVisible(true);
    }

    /**
     * Styles a JButton with the modern dark theme.
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BODY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 15, 8, 15) // Padding
        ));
    }

    /**
     * Styles a JLabel for the results area.
     */
    private void styleResultLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Adds a new SubjectEntryPanel to the list.
     */
    private void addSubjectRow() {
        SubjectEntryPanel newEntry = new SubjectEntryPanel(this);
        subjectEntries.add(newEntry);
        subjectsListPanel.add(newEntry);
        refreshSubjectList();
    }

    /**
     * Removes a specific SubjectEntryPanel from the list.
     */
    public void removeSubjectRow(SubjectEntryPanel entry) {
        subjectEntries.remove(entry);
        subjectsListPanel.remove(entry);
        refreshSubjectList();
    }

    /**
     * Refreshes the GUI to show changes to the subject list.
     */
    private void refreshSubjectList() {
        subjectsListPanel.revalidate();
        subjectsListPanel.repaint();
        scrollPane.revalidate(); // Ensure scrollbar adjusts
    }

    /**
     * Main calculation logic.
     * Iterates over all subject entries, calculates individual and total grades.
     */
    private void calculateGrades() {
        double totalMarkGot = 0;
        double totalOutOfMark = 0;
        boolean allValid = true;

        for (SubjectEntryPanel entry : subjectEntries) {
            try {
                double mark = entry.getMark();
                double outOf = entry.getOutOf();

                if (mark < 0 || outOf <= 0 || mark > outOf) {
                    entry.setError("Invalid range");
                    allValid = false;
                } else {
                    totalMarkGot += mark;
                    totalOutOfMark += outOf;

                    // --- Calculate and set individual grade ---
                    double individualPercentage = (mark / outOf) * 100;
                    String individualGradeLetter = getGradeFromPercentage(individualPercentage);
                    entry.setIndividualGrade(individualGradeLetter);
                }
            } catch (NumberFormatException e) {
                entry.setError("Invalid number");
                allValid = false;
            }
        }

        if (!allValid || totalOutOfMark == 0) {
            // If any entry is invalid or no marks are entered, reset overall results
            totalMarksLabel.setText("Total: -- / --");
            percentageLabel.setText("Percentage: --%");
            gradeLabel.setText("Overall Grade: --");
            gradeLabel.setForeground(COLOR_TEXT); // Reset color
        } else {
            // All entries are valid, calculate and display results
            double averagePercentage = (totalMarkGot / totalOutOfMark) * 100;

            // Determine the grade
            String grade = getGradeFromPercentage(averagePercentage);

            // --- Display the Results ---
            totalMarksLabel.setText(String.format("Total: %.2f / %.2f", totalMarkGot, totalOutOfMark));
            percentageLabel.setText(String.format("Percentage: %.2f%%", averagePercentage));
            gradeLabel.setText("Overall Grade: " + grade);

            // Set grade color
            if (grade.equals("F")) {
                gradeLabel.setForeground(COLOR_GRADE_FAIL);
            } else {
                gradeLabel.setForeground(COLOR_GRADE_PASS);
            }
        }
    }

    /**
     * Helper method to convert a percentage into a letter grade string.
     * @param percentage The percentage to evaluate.
     * @return The string grade (O, A+, A... F).
     */
    private String getGradeFromPercentage(double percentage) {
        if (percentage >= 95) return "O";
        if (percentage >= 90) return "A+";
        if (percentage >= 85) return "A";
        if (percentage >= 80) return "B+";
        if (percentage >= 75) return "B";
        if (percentage >= 70) return "C+";
        if (percentage >= 65) return "C";
        return "F";
    }

    /**
     * The main method. This is the starting point of the application.
     */
    public static void main(String[] args) {
        // Set Nimbus Look and Feel first for a cleaner UI (if available)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, the default L&F will be used.
            e.printStackTrace();
        }

        // Apply custom dark theme defaults *after* setting Nimbus
        UIManager.put("control", COLOR_BACKGROUND); // General background
        UIManager.put("nimbusBase", new Color(60, 60, 80)); // Button hover/select
        UIManager.put("nimbusFocus", COLOR_PRIMARY_BUTTON);
        UIManager.put("nimbusLightBackground", COLOR_PANEL); // Text field/list background
        UIManager.put("text", COLOR_TEXT); // Default text color
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusSelectionBackground", COLOR_PRIMARY_BUTTON);
        UIManager.put("List.cellRenderer.background", COLOR_PANEL);
        UIManager.put("List.background", COLOR_PANEL);
        UIManager.put("TextField.background", new Color(50, 50, 70));
        UIManager.put("TextField.foreground", COLOR_TEXT);
        UIManager.put("TextField.caretForeground", COLOR_TEXT);
        UIManager.put("Label.foreground", COLOR_TEXT);
        UIManager.put("Panel.background", COLOR_BACKGROUND);
        UIManager.put("ScrollPane.background", COLOR_BACKGROUND);
        UIManager.put("ScrollPane.foreground", COLOR_TEXT);
        UIManager.put("ScrollBar.thumb", new Color(70, 70, 90));
        UIManager.put("ScrollBar.track", COLOR_BACKGROUND);

        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(GradeCalculator::new);
    }

    /**
     * Inner class representing a single subject row.
     * This panel contains the text fields and labels for one subject.
     */
    class SubjectEntryPanel extends JPanel {
        private JTextField subjectNameField;
        private JTextField markField;
        private JTextField outOfField;
        private JLabel individualGradeLabel;
        private JButton removeButton;
        private GradeCalculator parentCalculator; // Reference to the main app

        public SubjectEntryPanel(GradeCalculator parent) {
            this.parentCalculator = parent;

            // Use a horizontal BoxLayout to keep everything on one line
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBackground(COLOR_PANEL);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Constrain height

            // --- Components for the row ---
            JLabel subjectLabel = new JLabel("Subject:");
            stylePanelLabel(subjectLabel);
            subjectNameField = new JTextField(15);
            stylePanelTextField(subjectNameField);

            JLabel markLabel = new JLabel("Mark Got:");
            stylePanelLabel(markLabel);
            markField = new JTextField(5);
            stylePanelTextField(markField);

            JLabel outOfLabel = new JLabel("Out of:");
            stylePanelLabel(outOfLabel);
            outOfField = new JTextField(5);
            stylePanelTextField(outOfField);

            individualGradeLabel = new JLabel("Grade: --");
            stylePanelLabel(individualGradeLabel);
            individualGradeLabel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Spacing

            removeButton = new JButton("X");
            styleButton(removeButton, COLOR_DANGER_BUTTON);
            removeButton.setMargin(new Insets(2, 5, 2, 5)); // Make button smaller
            removeButton.setFont(new Font("Arial", Font.BOLD, 12));
            removeButton.addActionListener(e -> parentCalculator.removeSubjectRow(this));

            // --- Add components to the panel ---
            add(subjectLabel);
            add(Box.createHorizontalStrut(5));
            add(subjectNameField);
            add(Box.createHorizontalStrut(10)); // Spacer
            add(markLabel);
            add(Box.createHorizontalStrut(5));
            add(markField);
            add(Box.createHorizontalStrut(10)); // Spacer
            add(outOfLabel);
            add(Box.createHorizontalStrut(5));
            add(outOfField);
            add(Box.createHorizontalStrut(5));
            add(individualGradeLabel);

            add(Box.createHorizontalGlue()); // Pushes the remove button to the far right

            add(removeButton);
        }

        private void stylePanelLabel(JLabel label) {
            label.setFont(FONT_BODY);
            label.setForeground(COLOR_TEXT);
            label.setBorder(new EmptyBorder(0, 5, 0, 5)); // Spacing
        }

        private void stylePanelTextField(JTextField field) {
            field.setFont(FONT_BODY);
            field.setBackground(new Color(50, 50, 70));
            field.setForeground(COLOR_TEXT);
            field.setCaretColor(COLOR_TEXT);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 80, 100)),
                    new EmptyBorder(5, 5, 5, 5)
            ));
            // Constrain size
            field.setMaximumSize(new Dimension(field.getPreferredSize().width, 30));
        }

        // --- Getters and Setters ---

        public double getMark() throws NumberFormatException {
            return Double.parseDouble(markField.getText());
        }

        public double getOutOf() throws NumberFormatException {
            return Double.parseDouble(outOfField.getText());
        }

        public void setError(String message) {
            // Show error by changing field colors
            markField.setBackground(COLOR_DANGER_BUTTON.darker());
            outOfField.setBackground(COLOR_DANGER_BUTTON.darker());
            individualGradeLabel.setText("Grade: " + message);
            individualGradeLabel.setForeground(COLOR_DANGER_BUTTON);
        }

        public void setIndividualGrade(String grade) {
            // Reset colors
            markField.setBackground(new Color(50, 50, 70));
            outOfField.setBackground(new Color(50, 50, 70));
            
            individualGradeLabel.setText("Grade: " + grade);
            if (grade.equals("F")) {
                individualGradeLabel.setForeground(COLOR_GRADE_FAIL);
            } else {
                individualGradeLabel.setForeground(COLOR_GRADE_PASS);
            }
        }
    }
}

