import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseTracker {
    private static final String USER_DATA_FILE = "users.dat";
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        loadUsers();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    saveUsers();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("Registration successful!");
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful!");
                userMenu(scanner, user);
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }

    private static void userMenu(Scanner scanner, User user) {
        while (true) {
            System.out.println("1. Add Expense");
            System.out.println("2. List Expenses");
            System.out.println("3. Get Category Total");
            System.out.println("4. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    addExpense(scanner, user);
                    break;
                case 2:
                    user.listExpenses();
                    break;
                case 3:
                    System.out.println("Enter category:");
                    String category = scanner.nextLine();
                    double total = user.getCategoryTotal(category);
                    System.out.println("Total for " + category + ": " + total);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addExpense(Scanner scanner, User user) {
        try {
            System.out.println("Enter date (yyyy-MM-dd):");
            String dateString = scanner.nextLine();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            System.out.println("Enter category:");
            String category = scanner.nextLine();
            System.out.println("Enter amount:");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            Expense expense = new Expense(date, category, amount);
            user.addExpense(expense);
            System.out.println("Expense added successfully!");
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
        }
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_DATA_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, start with an empty user list
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }
}
