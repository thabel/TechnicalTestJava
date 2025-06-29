
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class Account implements AccountService {

    class Transaction {

        public Date date;
        public int amount;

        public Transaction(Date date, int amount) {
            this.date = date;
            this.amount = amount;

        }
    }

    private int balance;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    public Account() {
        this.balance = 0;
    }

    public Account(int balance) {
        this.balance = balance;
    }

    public void depositOndate(int amount, Date date) {

        deposit(amount);
        transactions.add(
                new Transaction(date, amount)
        );
    }

    public void withdrawOndate(int amount, Date date) {

        withdraw(amount);
        transactions.add(
                new Transaction(date, -amount)
        );

    }

    @Override
    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

    }

    @Override
    public void withdraw(int amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new  IllegalArgumentException("Insufficient balance for withdrawal.");
        }

    }

    @Override
    public void printStatement() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("===== ACCOUNT STATEMENT =====");
        System.out.printf("%-12s | %-10s | %-10s%n", "DATE", "AMOUNT", "BALANCE");
        System.out.println("----------------------------------------");

        int currentBalance = this.balance;

        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction record = transactions.get(i);
            System.out.printf("%-12s | %-10d | %-10d%n",
                    sdf.format(record.date),
                    record.amount,
                    currentBalance);
            currentBalance -= record.amount;
        }

        System.out.println();
    }

}
