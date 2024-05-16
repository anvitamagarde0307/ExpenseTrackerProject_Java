import java.util.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Expense {
    static ArrayList<ExpenseData> al = new ArrayList<>();
    public static final Object lock = new Object();
    static TransactionEntryApp tea;
    static Scanner s = new Scanner(System.in);
    public static boolean dataEntered = false;
    public static double salary;
    public static String usernameString;

    Expense(String usernameString) {
        System.out.println();
        System.out.print("Enter Your Monthly Salary / Pocket Money : ");
        salary = s.nextDouble();
        this.usernameString = usernameString;
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            System.out.println("\nEnter: 1 ---> Insert Transaction Entry In Database");
            System.out.println("Enter: 2 ---> Display All Transactions From The Database");
            System.out.println("Enter: 3 ---> Delete A Specific Record Of The Database");
            System.out.println("Enter: 4 ---> Move To Next Page Of Operations");
            System.out.println("Enter: 0 ---> Exit The System\n");
            System.out.print("Enter Choice: ");
            int choice = s.nextInt();

            switch (choice) {
                case 0:
                    PreparedStatement pst = ExpenseData.con.prepareStatement("Truncate Table entrySave");
                    pst.executeUpdate();
                    System.exit(0);
                    break;
                case 1:
                    try {
                        synchronized (lock) {
                            dataEntered = false;
                            tea = new TransactionEntryApp();
                            tea.start();
                            while (!dataEntered) {
                                lock.wait();
                            }
                            String month = TransactionEntryApp.entryField.getText();
                            String date = TransactionEntryApp.dateField.getText();
                            String expenseType = (String) TransactionEntryApp.transactionTypeComboBox.getSelectedItem();
                            String expenseSubType = (String) TransactionEntryApp.expenseTypeComboBox.getSelectedItem();
                            String amountString = TransactionEntryApp.amountField.getText();
                            ExpenseData ed = new ExpenseData(month, date, expenseType, expenseSubType, amountString);
                            al.add(ed);
                            ed.main();
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    JFrame frame = new JFrame("Display Entries");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(800, 400);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
                    int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
                    frame.setLocation(centerX, centerY);

                    String[] columnNames = { "Entry No.", "Month", "Amount", "Type of Expense", "Expense Sub Type" };
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                    JTable table = new JTable(tableModel);
                    for (ExpenseData ed : al) {
                        Object[] rowData = {
                                al.indexOf(ed) + 1, // Entry No.
                                ed.month,
                                ed.amountString,
                                ed.expenseType,
                                ed.expenseSubType
                        };
                        tableModel.addRow(rowData);

                    }

                    JScrollPane scrollPane = new JScrollPane(table);
                    frame.add(scrollPane);
                    frame.setVisible(true);
                    try {
                        Thread.sleep(6000);
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("Error : " + e.getMessage());
                    }
                    frame.dispose();
                    break;
                case 3:
                    System.out.print("Enter The Number Of Entry You Want To Delete From Database : ");
                    int num = s.nextInt();
                    pst = ExpenseData.con
                            .prepareStatement("Delete From entrySave where Entry_Id=" + num);
                    ExpenseData ed = al.remove(num);
                    if (ed == null) {
                        System.out.println("Entry Not Found In Database, Not Deleted");
                    } else {
                        System.out.println("Entry Deleted From Database");
                    }
                    break;
                case 4:
                    Functions.main(null);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}

class ExpenseData {
    String month, date, expenseType, expenseSubType, amountString;
    static Connection con;

    public void main() throws Exception {
        String driveString = "com.mysql.jdbc.Driver";
        Class.forName(driveString);
        String dbURL = "jdbc:mysql://localhost:3308/Project";
        String dbUser = "root";
        String dbPass = "";
        con = DriverManager.getConnection(dbURL, dbUser, dbPass);
        PreparedStatement pst = con.prepareStatement(
                "Insert into entrySave (Entry_Month,Entry_Date,Entry_Type,Entry_SubType,Entry_Amount) values (?,?,?,?,?)");
        pst.setString(1, month);
        pst.setString(2, date);
        pst.setString(3, expenseType);
        pst.setString(4, expenseSubType);
        pst.setInt(5, Integer.parseInt(amountString));
        pst.executeUpdate();
    }

    ExpenseData(String month, String date, String expenseType, String expenseSubType, String amountString) {
        this.month = month;
        this.date = date;
        this.expenseType = expenseType;
        this.expenseSubType = expenseSubType;
        this.amountString = amountString;
    }

    @Override
    public String toString() {
        return "ExpenseData [month=" + month + ", date=" + date + ", expenseType=" + expenseType + ", expenseSubType="
                + expenseSubType + ", amountString=" + amountString + "]";
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getExpenseSubType() {
        return expenseSubType;
    }

    public String getAmountString() {
        return amountString;
    }

}
