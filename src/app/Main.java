

package app;

import model.Transaction;
import model.TransactionType;
import service.FinanceService;
import storage.TransactionStorage;
import storage.SummaryExporter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Path dataPath = Path.of("data", "transactions.csv");
        Path exportPath = Path.of("exports", "financial-summary.csv");

        TransactionStorage storage = new TransactionStorage(dataPath);
        SummaryExporter exporter = new SummaryExporter();
        FinanceService financeService = new FinanceService();

        try (Scanner scanner = new Scanner(System.in)) {

            // Load existing transactions
            List<Transaction> transactions =
                    storage.loadTransactions();

            for (Transaction transaction : transactions) {
                financeService.addTransaction(transaction);
            }

            System.out.println("Transactions loaded: "
                    + transactions.size());

            boolean running = true;

            while (running) {
                System.out.println("\n=== Personal Finance Tracker ===");
                System.out.println("1. Add Income");
                System.out.println("2. Add Expense");
                System.out.println("3. View Financial Summary");
                System.out.println("4. Export Monthly Summary");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {

                    case "1":
                        addTransaction(
                                scanner, financeService,
                                TransactionType.INCOME);
                        break;

                    case "2":
                        addTransaction(
                                scanner, financeService,
                                TransactionType.EXPENSE);
                        break;

                    case "3":
                        showSummary(financeService);
                        break;

                    case "4":
                        try {
                            exporter.exportMonthlySummary(
                                    financeService,
                                    YearMonth.now(),
                                    exportPath);
                            System.out.println(
                                    "Summary exported to: " + exportPath);
                        } catch (IOException e) {
                            System.out.println(
                                    "Export failed: " + e.getMessage());
                        }
                        break;

                    case "5":
                        running = false;
                        break;

                    default:
                        System.out.println(
                                "Invalid option. Choose 1 to 5.");
                }

                // Save current transactions after each menu action.
                try {
                    storage.saveTransactions(
                            financeService.getAllTransactions());
                } catch (IOException e) {
                    System.out.println(
                            "Warning: Could not save transactions: "
                                    + e.getMessage());
                }
            }

            System.out.println("Thank you for using the tracker!");

        } catch (IOException e) {
            System.out.println(
                    "Could not load transactions: " + e.getMessage());
        }
    }

    private static void addTransaction(
            Scanner scanner,
            FinanceService financeService,
            TransactionType type) {

        try {
            System.out.print("Enter amount: ");
            BigDecimal amount =
                    new BigDecimal(scanner.nextLine().trim());

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();

            System.out.print("Enter remarks: ");
            String remarks = scanner.nextLine().trim();

            Transaction transaction = new Transaction(
                    type,
                    amount,
                    category,
                    LocalDateTime.now(),
                    remarks);

            financeService.addTransaction(transaction);

            System.out.println(type + " added successfully!");

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid amount. Enter a valid number, such as 250.50.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Transaction not added: " + e.getMessage());
        }
    }

    private static void showSummary(FinanceService financeService) {

        YearMonth month = YearMonth.now();

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Month: " + month);
        System.out.println("Monthly Income: "
                + financeService.getMonthlyIncome(month));
        System.out.println("Monthly Expenses: "
                + financeService.getMonthlyExpenses(month));
        System.out.println("Net Cash Flow: "
                + financeService.getMonthlyNetCashFlow(month));
    }
}

