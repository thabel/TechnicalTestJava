
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Account account = new Account();
        try {
            account.depositOndate(1000, sdf.parse("10-01-2012"));
            account.depositOndate(2000, sdf.parse("13-01-2012"));
            account.withdrawOndate(500, sdf.parse("14-01-2012"));
        } catch (Exception e) {
            System.err.println("Deposisit money failed: " + e.getMessage());
        }
    
        account.printStatement();

        System.out.println("\n--- Scenario: Invalid Deposit ---");
        try {
            account.depositOndate(-100, sdf.parse("15-01-2012")); // Invalid amount
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Scenario: Invalid Withdrawal (Insufficient Funds) ---");
        try {
            account.withdrawOndate(5000, sdf.parse("16-01-2012")); // Insufficient funds
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Scenario: Invalid Date Format ---");
        try {
            account.depositOndate(100, sdf.parse("2012-01-10")); // Invalid date format
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
