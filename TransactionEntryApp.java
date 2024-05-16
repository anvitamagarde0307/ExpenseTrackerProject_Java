import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionEntryApp extends Thread {
    static JTextField entryField;
    static JTextField dateField;
    static JComboBox<String> transactionTypeComboBox;
    static JComboBox<String> expenseTypeComboBox;
    static JTextField amountField;
    static JTextArea expenseCategoriesArea;

    public synchronized static void MainMethod() {
        JFrame frame = new JFrame("Transaction Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerx = (int) ((screensize.getWidth() - frame.getWidth()) / 2);
        int centery = (int) ((screensize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(centerx, centery);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Month Of Transaction Entry:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        entryField = new JTextField(20);
        panel.add(entryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Date (Format \"YYYY-MM-DD\"): "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dateField = new JTextField(10);
        panel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Transaction Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        String[] transactionTypes = { "Primary", "Secondary" };
        transactionTypeComboBox = new JComboBox<>(transactionTypes);
        panel.add(transactionTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Type of Expense:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        expenseTypeComboBox = new JComboBox<>();
        panel.add(expenseTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        amountField = new JTextField(10);
        panel.add(amountField, gbc);

        expenseTypeComboBox.setEnabled(false);
        expenseTypeComboBox.setVisible(false);

        transactionTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) transactionTypeComboBox.getSelectedItem();
                if ("Primary".equals(selectedType)) {
                    String[] primaryExpenseTypes = { "Health And Hygiene", "Transportation", "Food & Maintenance",
                            "Housing", "Investment And Tax", "Others" };
                    expenseTypeComboBox.setModel(new DefaultComboBoxModel<>(primaryExpenseTypes));
                } else if ("Secondary".equals(selectedType)) {
                    String[] secondaryExpenseTypes = { "Junk Food", "Entertainment", "Alcohol Or Cigarette",
                            "Expensive Items", "Impulse Spending", "Others" };
                    expenseTypeComboBox.setModel(new DefaultComboBoxModel<>(secondaryExpenseTypes));
                }
                expenseTypeComboBox.setEnabled(true);
                expenseTypeComboBox.setVisible(true);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(51, 153, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entry = entryField.getText();
                String dateInput = dateField.getText();
                String transactionType = (String) transactionTypeComboBox.getSelectedItem();
                String expenseType = (String) expenseTypeComboBox.getSelectedItem();
                String amount = amountField.getText(); // Get the entered amount

                if (!isValidDate(dateInput, entry)) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid date in the Format 'YYYY-MM-DD' (eg: 2023-01-01)", "Invalid Date",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                synchronized (Expense.lock) {
                    Expense.dataEntered = true;
                    Expense.lock.notify();
                }

                frame.dispose();
            }
        });

        panel.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        JButton showCategoriesButton = new JButton("Show Expense Categories");
        showCategoriesButton.setBackground(new Color(51, 153, 51));
        showCategoriesButton.setForeground(Color.WHITE);
        showCategoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String transactionType = (String) transactionTypeComboBox.getSelectedItem();
                String expenseType = (String) expenseTypeComboBox.getSelectedItem();
                displayExpenseCategory(transactionType, expenseType);
            }
        });
        panel.add(showCategoriesButton, gbc);

        // Expense Categories TextArea
        gbc.gridx = 0;
        gbc.gridy = 7; // Update the grid position
        gbc.gridwidth = 3;
        expenseCategoriesArea = new JTextArea(5, 25);
        expenseCategoriesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(expenseCategoriesArea);
        panel.add(scrollPane, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void displayExpenseCategory(String transactionType, String expenseType) {
        expenseCategoriesArea.setText("EXPENSE CATEGORY: \n\n");

        if ("Primary".equals(transactionType)) {
            expenseCategoriesArea.append("PRIMARY EXPENSE INCLUDES*\n");
            if (expenseType != null) {
                expenseCategoriesArea.append("*" + expenseType + "\n");
            }
        } else if ("Secondary".equals(transactionType)) {
            expenseCategoriesArea.append("SECONDARY EXPENSE INCLUDES*\n");
            if (expenseType != null) {
                expenseCategoriesArea.append("*" + expenseType + "\n");
            }
        }
    }

    public static boolean isValidDate(String dateInput, String entry) {

        String[] str = dateInput.trim().split("-");


        try{
            if (entry.equalsIgnoreCase("January")) {
                if (str[1].equalsIgnoreCase("01")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("February")) {
                if (str[1].equalsIgnoreCase("02")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("March")) {
                if (str[1].equalsIgnoreCase("03")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("April")) {
                if (str[1].equalsIgnoreCase("04")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("May")) {
                if (str[1].equalsIgnoreCase("05")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("June")) {
                if (str[1].equalsIgnoreCase("06")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("July")) {
                if (str[1].equalsIgnoreCase("07")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("August")) {
                if (str[1].equalsIgnoreCase("08")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("September")) {
                if (str[1].equalsIgnoreCase("09")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("October")) {
                if (str[1].equalsIgnoreCase("10")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("November")) {
                if (str[1].equalsIgnoreCase("11")) {
                    return true;
                }
            } else if (entry.equalsIgnoreCase("December")) {
                if (str[1].equalsIgnoreCase("12")) {
                    return true;
                }
            }
            else{
                System.out.println("please enter correct name of month");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public void run() {
        MainMethod();
    }
}
