import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ExpenseTracker {
    static HashMap<String, String> hm = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        // Welcome Frame
        JFrame welcomeFrame = new JFrame("Welcome to Expense Tracker");
        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        Font largerfont = new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 24);
        welcomeLabel.setFont(largerfont); // Set the larger font

        welcomeFrame.add(welcomeLabel);
        welcomeFrame.setSize(600, 400);
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        welcomeFrame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - 600) / 2;
        int y = (screenSize.height - 400) / 2;
        welcomeFrame.setLocation(x, y);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        welcomeFrame.dispose();
        BufferedReader br = new BufferedReader(new FileReader("UserNamePassword.txt"));
        String line = "";

        // Login_Registration Module
        while ((line = br.readLine()) != null) {
            String[] str = line.trim().split("=");
            hm.put(str[0], str[1]);
        }
        br.close(); // Closing the BufferedReader

        System.out.println("\nNew User ?   \"Click-1\" for Sign-Up");
        Thread.sleep(1000);
        System.out.println("\nAlready Registered User ?   \"Click-2\" for Sign-In");
        Thread.sleep(1000);
        System.out.println("\nWant To Exit System ?   \"Click-3\" to exit the system");
        System.out.print("Choose the option to select: ");
        int key = s.nextInt();
        s.nextLine(); // Consume the newline

        switch (key) {
            case 1:
                SignUp signup = new SignUp();
                signup.register(); // Calling SignUp Module
                break;
            case 2:
                SignIn signin = new SignIn();
                signin.signInYourAccount(); // Calling Sign-in Module
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }
}

class SignUp {
    Scanner s = new Scanner(System.in);
    static String name, bday, email, gender, phoneNumber, username, password;
    int age;

    public SignUp() {
    }

    public void register() throws Exception {
        while (true) {
            System.out.println(
                    "                                                  --> (USER'S REGISTRATION) <--                                                              ");
            System.out.print("\nPlease enter your full name (First Last): ");
            name = s.nextLine();

            String namePattern = "^[A-Z][a-z]+\\s[A-Z][a-z]+$";
            Pattern regex = Pattern.compile(namePattern);
            Matcher matcher = regex.matcher(name);

            if (matcher.matches()) {
                break;
            } else {
                System.out.println("Invalid name format. There Should be a capital letter in First Name.\nPlease enter your full name.");
            }
        }

        System.out.print("Please enter your date of birth (YYYY-MM-DD): ");
        bday = s.nextLine();

        boolean check = true;
        while (check) {
            System.out.print("Enter your Email address: ");
            email = s.nextLine();
            String e = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (email.matches(e)) {
                check = false;
            } else {
                System.out.println("Invalid email address.");
                System.out.print("Enter again: ");
            }
        }

        System.out.print("Please enter your gender: ");
        gender = s.nextLine();

        boolean validPhone = false;
        while (!validPhone) {
            System.out.print("Please enter your phone number: ");
            phoneNumber = s.nextLine();
            if (phoneNumber.matches("^[789]\\d{9}$")) {
                validPhone = true;
            } else {
                System.out.println("Invalid phone number. It must start with 7, 8, or 9 and be 10 digits long.");
            }
        }

        // OTP Based Registration Frame
        check = true;
        while (check) {
            JFrame jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setVisible(true);
            int num = (int) (Math.random() * 9000) + 1000;
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = (int) ((screenSize.getWidth() - jFrame.getWidth()) / 2);
            int centerY = (int) ((screenSize.getHeight() - jFrame.getHeight()) / 2);
            jFrame.setLocation(centerX, centerY);
            JOptionPane.showMessageDialog(jFrame, "Your OTP: " + num);
            System.out.print("Enter OTP received on your number: ");
            int otp = s.nextInt();
            s.nextLine(); // Consume the newline
            if (otp == num) {
                System.out.println("Entered OTP is correct");
                jFrame.dispose();
                String number = Integer.toString((int) (Math.random() * (30 - 11 + 1)) + 11);
                int index = name.indexOf(' ');
                String autoString = name.substring(0, index).concat(number);
                System.out.println("\nThe system's automatically generated username: " + autoString);
                System.out.println("\"Click 1\" to select automated username");
                System.out.println("\"Click 2\" for manually entering the username");
                System.out.print("Enter your choice: ");
                BufferedWriter bw = new BufferedWriter(new FileWriter("UserNamePassword.txt", true));
                int choice = s.nextInt();
                s.nextLine();
                if (choice == 1) {
                    username = autoString;
                    System.out.println("Automated username selected");
                } else if (choice == 2) {
                    boolean validUsername = false;
                    while (!validUsername) {
                        System.out.print("\nEnter username manually: ");
                        username = s.nextLine();
                        if (ExpenseTracker.hm.containsKey(username)) {
                            System.out.println("Entered username is already taken. Enter again: ");
                        } else {
                            validUsername = true;
                        }
                    }
                }
                System.out.print("Please enter your password: ");
                password = s.nextLine();
                bw.write(username + "=" + password);
                bw.newLine();
                bw.flush();
                bw.close();
                check = false;
                int count = 5;
                System.out.println("\nSigning-up into the system! Please wait.");
                while (count > 0) {
                    System.out.print(count + " .... ");
                    Thread.sleep(1000);
                    count--;
                }
                System.out.println("\nSign-up successful, welcome to the system!");
                Expense e = new Expense(username);
                e.main(null);
                break;
            } else {
                System.out.println("Entered OTP is incorrect!");
                jFrame.dispose();
            }
        }
    }
}

class SignIn {
    public void signInYourAccount() throws Exception {
        Scanner s = new Scanner(System.in);
        boolean userFound = false;
        System.out.println(
                "                                                  --> (USER'S LOGIN) <--                                                              ");
        while (!userFound) {
            System.out.print("Enter Your Username: ");
            String enteredUsername = s.nextLine();

            BufferedReader br = new BufferedReader(new FileReader("UserNamePassword.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.trim().split("=");
                String usernameFromFile = arr[0];
                String passwordFromFile = arr[1];

                if (usernameFromFile.equals(enteredUsername)) {
                    System.out.print("Enter Your Password: ");
                    String enteredPassword = s.nextLine();

                    if (passwordFromFile.equals(enteredPassword)) {
                        int count = 5;
                        System.out.println("\nSigning-in into the system! Please wait.");
                        while (count > 0) {
                            System.out.print(count + " .... ");
                            Thread.sleep(1000);
                            count--;
                        }
                        System.out.println("\nSign-in successful, welcome to the system!");
                        userFound = true;
                        Expense e = new Expense(usernameFromFile);
                        e.main(null);
                        break;
                    } else {
                        System.out.println("\"WARNING\" :: Password Is Incorrect");
                    }
                }
            }

            br.close();

            if (!userFound) {
                System.out.println("\"WARNING\" :: Username Not Found");
                System.out.println("Please try again.");
            }
        }
    }
}


