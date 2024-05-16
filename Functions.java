import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Functions {
    static Connection con;

    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        Class.forName("com.mysql.jdbc.Driver");
        String dbURL = "jdbc:mysql://localhost:3308/Project";
        String dbUser = "root";
        String dbPass = "";
        con = DriverManager.getConnection(dbURL, dbUser, dbPass);
        while (true) {

            System.out.println("\nEnter : 0 ---> To Go Back To Main Menu");
            System.out.println("Enter : 1 ---> Show Your Savings Report");
            System.out.println("Enter : 2 ---> Track Your Spending Expense");
            System.out.println("Enter : 3 ---> View Your Activity Report");
            System.out.println("Enter : 4 ---> Print Reciept Of Particular Transaction");
            System.out.println("Enter : 5 ---> Check Primary And Secondary Expense Ration");
            System.out.println("Enter : 6 ---> To See Bar Graph Of Your Expense");
            System.out.print("Enter Your Choice : ");
            int choice = s.nextInt();
            s.nextLine();
            switch (choice) {
                case 0:
                    Expense.main(null);
                    break;
                case 1:
                    System.out.print("Enter Month's Of Which You Want To See Savings : ");
                    String moString = s.nextLine();
                    double savings = 0;
                    for (ExpenseData ed : Expense.al) {
                        if (ed.month.equalsIgnoreCase(moString)) {
                            savings += Integer.parseInt(ed.amountString);
                        }
                    }
                    System.out.println("Total Money Spent In The Month - " + savings);
                    savings = Expense.salary - savings;
                    if (savings > Expense.salary) {
                        System.out.println("\nTotal Savings Of Month - " + moString + " = " + savings);
                    } else {
                        System.out.println("\nNo Savings, Total Debt : " + (-savings));
                        System.out.println("You Need To Control Your Budget");
                    }
                    break;
                case 2:
                    double primarysum = 0;
                    double secondarysum = 0;
                    for (ExpenseData ed : Expense.al) {
                        if (ed.expenseType.equalsIgnoreCase("Primary")) {
                            primarysum += Integer.parseInt(ed.amountString);
                        } else {
                            secondarysum += Integer.parseInt(ed.amountString);
                        }
                    }
                    if (primarysum > secondarysum) {
                        System.out.println();
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");
                        System.out.println("\nYour Maximum Income Spent Is On Your Primary Expense.");
                        System.out.println("Total Amount Spend On Primary Expense Is:" + primarysum);
                        System.out.println("Total Amount Spent On Secondary Expense Is:" + secondarysum);
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");
                    } else if (secondarysum > primarysum) {
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");
                        System.out.println("\nYour Maximum Income Spent Is On Your Secondary Expense.");
                        System.out.println("Total Amount Spend On Primary Expense Is:" + primarysum);
                        System.out.println("Total Amount Spent On Secondary Expense Is:" + secondarysum);
                        System.out.println(
                                "You Need To Control Your Secondary Expenses In Order To Make Savings For Your Future Betterment!!");
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");

                    } else if (primarysum == secondarysum) {
                        System.out.println();
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");
                        System.out.println(
                                "\nYou Have Spent Equal Amount Of Money On Both Primary And Secondary Expenses.");
                        System.out.println("Total Amount Spend On Primary Expense Is:" + primarysum);
                        System.out.println("Total Amount Spent On Secondary Expense Is:" + secondarysum);
                        System.out.println(
                                "You Need To Control Your Secondary Expenses In Order To Make Savings For Your Future Betterment!!");
                        System.out.println(
                                "____________________________________________________________________________________________________________________________________________________________");

                    }
                    break;
                case 3:
                    System.out.println();
                    System.out.print("Which Month Data You Want To View : ");
                    moString = s.nextLine();
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________________________________");
                    CallableStatement cst = con.prepareCall("{ call getNumber(?,?)}");
                    cst.setString(1, moString);
                    cst.execute();
                    int count = cst.getInt(2);
                    System.out.println("Total Number Of Entries Entered In The Month " + moString + " = " + count);
                    cst = con.prepareCall("{ call getMaxAmount(?,?,?)}");
                    cst.setString(1, moString);
                    cst.execute();
                    count = cst.getInt(2);
                    String date = cst.getString(3);
                    System.out.println("Your Maximum Amount Of Transaction Was Made On:" + date);
                    System.out.println("Amount Of Transaction:" + count);
                    cst = con.prepareCall("{ call getMinAmount(?,?,?)}");
                    cst.setString(1, moString);
                    cst.execute();
                    count = cst.getInt(2);
                    date = cst.getString(3);
                    System.out.println("Your Minimum Amount Of Transaction Was Made On:" + date);
                    System.out.println("Amount Of Transaction:" + count);
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________________________________");
                    break;
                case 4:
                    System.out.print("Enter Date Of Transaction : ");
                    date = s.nextLine();
                    for (ExpenseData ed : Expense.al) {
                        if (ed.date.equalsIgnoreCase(date)) {
                            System.out.println(
                                    "____________________________________________________________________________________________________________________________________________________________");
                            System.out.println("RECIEPT:");
                            System.out.println("Name Of Holder Of Directory:" + Expense.usernameString);
                            System.out.println("The Month Of Directories Entered:" + ed.month);
                            System.out.println("Date Of Transaction:" + ed.getDate());
                            System.out.println("Total Amount Of Transaction Done:" + ed.getAmountString());
                            System.out.println(
                                    "____________________________________________________________________________________________________________________________________________________________");
                            break;
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter Month Of Which You Want To See Report : ");
                    moString = s.nextLine();
                    System.out.println("EXPENSE REPORT OF MONTH : " + moString);
                    System.out.println();
                    double[] calculate = new double[12];
                    for (ExpenseData ed : Expense.al) {
                        if (ed.month.equalsIgnoreCase(moString)) {
                            if (ed.expenseType.equalsIgnoreCase("Primary")) {
                                if (ed.expenseSubType.equalsIgnoreCase("Health And Hygiene")) {
                                    calculate[0] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Transportation")) {
                                    calculate[1] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Food & Maintenance")) {
                                    calculate[2] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Housing")) {
                                    calculate[3] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Investment And Tax")) {
                                    calculate[4] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Others")) {
                                    calculate[5] += Integer.parseInt(ed.amountString);
                                }
                            } else if (ed.expenseType.equalsIgnoreCase("Secondary")) {
                                if (ed.expenseSubType.equalsIgnoreCase("Junk Food")) {
                                    calculate[6] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Entertainment")) {
                                    calculate[7] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Alcohol Or Cigarette")) {
                                    calculate[8] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Expensive Items")) {
                                    calculate[9] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Impulse Spending")) {
                                    calculate[10] += Integer.parseInt(ed.amountString);
                                } else if (ed.expenseSubType.equalsIgnoreCase("Others")) {
                                    calculate[11] += Integer.parseInt(ed.amountString);
                                }
                            }
                        }
                    }
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________________________________");
                    System.out.println("\nPRIMARY AREA SPENDINGS:");
                    System.out.println("Total Amount Spent On Health And Hygiene:" + calculate[0]);
                    System.out.println("Total Amount Spent On Transportation:" + calculate[1]);
                    System.out.println("Total Amount Spent On Food & Maintenance:" + calculate[2]);
                    System.out.println("Total Amount Spent On Housing:" + calculate[3]);
                    System.out.println("Total Amount Spent On Investment Or Tax Or Insurance:" + calculate[4]);
                    System.out.println("Total Amount Spent On Others Category" + calculate[5]);
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________________________________");
                    System.out.println("\nSECONDARY AREA SPENDINGS");
                    System.out.println("Total Amount Spent On Junk Food:" + calculate[6]);
                    System.out.println("Total Amount Spent On Entertainment:" + calculate[7]);
                    System.out.println("Total Amount Spent On Alcohol & Cigarette:" + calculate[8]);
                    System.out.println("Total Amount Spent On Expensive Items:" + calculate[9]);
                    System.out.println("Total Amount Spent On Impulse Shopping:" + calculate[10]);
                    System.out.println("Total Amount Spent On Others Category" + calculate[11]);
                    double pper = (calculate[0] + calculate[1] + calculate[2] + calculate[3] + calculate[4]
                            + calculate[5]) * (0.06);
                    double sper = (calculate[6] + calculate[7] + calculate[8] + calculate[9] + calculate[10]
                            + calculate[11]) * (0.06);
                    System.out.println();
                    System.out.println("*)Primary Expense Percentage:" + pper + "%");
                    System.out.println("*)Secondary Expense Percentage:" + sper + "%");
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________________________________");
                    break;
                case 6:
                    Map<String, Integer> data = new HashMap<>();
                    System.out.print("Enter Month's Name : ");
                    moString = s.nextLine();
                    for (ExpenseData ed : Expense.al) {
                        if (ed.month.equalsIgnoreCase(moString)) {
                            data.put(ed.date, Integer.parseInt(ed.amountString));
                        }
                    }
                    BarGraph bg = new BarGraph(data);
                    bg.main(null);
                    break;
                default:
                    System.out.println("Enter Correct Option ");
                    break;
            }
        }
    }
}
