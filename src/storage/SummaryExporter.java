package storage;

import service.FinanceService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SummaryExporter {

    public void exportMonthlySummary(
            FinanceService financeService,
            YearMonth month,
            Path outputPath) throws IOException {

        List<String> lines = new ArrayList<>();

        // Monthly financial totals
        lines.add("Metric,Amount");
        lines.add("Month," + month);
        lines.add("Monthly Income,"
                + financeService.getMonthlyIncome(month).toPlainString());
        lines.add("Monthly Expenses,"
                + financeService.getMonthlyExpenses(month).toPlainString());
        lines.add("Net Cash Flow,"
                + financeService.getMonthlyNetCashFlow(month).toPlainString());

        // Category-wise expense breakdown
        lines.add("");
        lines.add("Category,Amount,Percentage");

        Map<String, BigDecimal> expenses =
                financeService.getExpensesByCategory(month);

        for (Map.Entry<String, BigDecimal> entry
                : expenses.entrySet()) {

            BigDecimal percentage =
                    financeService.getCategorySpendingPercentage(
                            month, entry.getKey());

            lines.add(entry.getKey() + ","
                    + entry.getValue().toPlainString() + ","
                    + percentage.toPlainString() + "%");
        }

        Path parent = outputPath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(outputPath, lines, StandardCharsets.UTF_8);
    }
}
