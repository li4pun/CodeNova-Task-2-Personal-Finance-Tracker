
package service;

import model.Transaction;
import model.TransactionType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class FinanceService {

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException(
                "Transaction cannot be null"
            );
        }

        transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    public BigDecimal getTotalIncome() {
    BigDecimal total = BigDecimal.ZERO;

    for (Transaction transaction : transactions) {
        if (transaction.getType() == TransactionType.INCOME) {
            total = total.add(transaction.getAmount());
        }
    }

    return total;
}
public BigDecimal getTotalExpenses() {
    BigDecimal total = BigDecimal.ZERO;

    for (Transaction transaction : transactions) {
        if (transaction.getType() == TransactionType.EXPENSE) {
            total = total.add(transaction.getAmount());
        }
    }

    return total;
}
public BigDecimal getNetCashFlow() {
    return getTotalIncome().subtract(getTotalExpenses());
}
public BigDecimal getMonthlyIncome(YearMonth month) {
    BigDecimal total = BigDecimal.ZERO;

    for (Transaction transaction : transactions) {
        YearMonth transactionMonth =
                YearMonth.from(transaction.getTimestamp());

        if (transaction.getType() == TransactionType.INCOME
                && transactionMonth.equals(month)) {
            total = total.add(transaction.getAmount());
        }
    }

    return total;
}
public BigDecimal getMonthlyExpenses(YearMonth month) {
    BigDecimal total = BigDecimal.ZERO;

    for (Transaction transaction : transactions) {
        YearMonth transactionMonth =
                YearMonth.from(transaction.getTimestamp());

        if (transaction.getType() == TransactionType.EXPENSE
                && transactionMonth.equals(month)) {
            total = total.add(transaction.getAmount());
        }
    }

    return total;
}
public BigDecimal getMonthlyNetCashFlow(YearMonth month) {
    return getMonthlyIncome(month)
            .subtract(getMonthlyExpenses(month));
}
public Map<String, BigDecimal> getExpensesByCategory(YearMonth month) {
    Map<String, BigDecimal> expenses = new HashMap<>();

    for (Transaction transaction : transactions) {
        YearMonth transactionMonth =
                YearMonth.from(transaction.getTimestamp());

        if (transaction.getType() == TransactionType.EXPENSE
                && transactionMonth.equals(month)) {

            String category = transaction.getCategory();
            BigDecimal amount = transaction.getAmount();

            expenses.put(
                category,
                expenses.getOrDefault(category, BigDecimal.ZERO)
                        .add(amount)
            );
        }
    }

    return expenses;
}
public BigDecimal getCategorySpendingPercentage(
        YearMonth month, String category) {

    BigDecimal totalExpenses = getMonthlyExpenses(month);

    if (totalExpenses.compareTo(BigDecimal.ZERO) == 0) {
        return BigDecimal.ZERO;
    }

    BigDecimal categoryExpenses =
            getExpensesByCategory(month)
                    .getOrDefault(category, BigDecimal.ZERO);

    return categoryExpenses
            .multiply(BigDecimal.valueOf(100))
            .divide(totalExpenses, 2, java.math.RoundingMode.HALF_UP);
}
public List<Transaction> filterByDateRange(
        LocalDate startDate, LocalDate endDate) {

    if (startDate == null || endDate == null
            || endDate.isBefore(startDate)) {
        throw new IllegalArgumentException("Invalid date range");
    }

    return transactions.stream()
            .filter(t -> {
                LocalDate date = t.getTimestamp().toLocalDate();

                return !date.isBefore(startDate)
                        && !date.isAfter(endDate);
            })
            .collect(Collectors.toList());
}
public List<Transaction> filterByCategory(String category) {
    if (category == null || category.isBlank()) {
        throw new IllegalArgumentException(
                "Category cannot be empty");
    }

    return transactions.stream()
            .filter(t -> t.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
}
}
