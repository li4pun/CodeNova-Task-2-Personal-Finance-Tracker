

package storage;

import model.Transaction;
import model.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionStorage {

    private final Path filePath;

    public TransactionStorage(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException(
                    "File path cannot be null");
        }

        this.filePath = filePath;
    }

    public void saveTransactions(List<Transaction> transactions)
            throws IOException {

        List<String> lines = new ArrayList<>();

        lines.add("type,amount,category,timestamp,remarks");

        for (Transaction transaction : transactions) {
            lines.add(
                    transaction.getType() + ","
                    + transaction.getAmount().toPlainString() + ","
                    + escapeCsv(transaction.getCategory()) + ","
                    + transaction.getTimestamp() + ","
                    + escapeCsv(transaction.getRemarks() == null
                            ? "" : transaction.getRemarks())
            );
        }

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(
                filePath,
                lines,
                StandardCharsets.UTF_8
        );
    }

    public List<Transaction> loadTransactions()
            throws IOException {

        List<Transaction> transactions = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return transactions;
        }

        List<String> lines = Files.readAllLines(
                filePath, StandardCharsets.UTF_8);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            
            if (line.isBlank()) {
                continue;
            }

            List<String> fields = parseCsvLine(line);

            if (fields.size() != 5) {
                throw new IOException(
                        "Invalid CSV format at line " + (i + 1));
            }

            try {
                TransactionType type =
                        TransactionType.valueOf(fields.get(0));

                BigDecimal amount =
                        new BigDecimal(fields.get(1));

                String category = fields.get(2);

                LocalDateTime timestamp =
                        LocalDateTime.parse(fields.get(3));

                String remarks = fields.get(4);

                Transaction transaction = new Transaction(
                        type, amount, category, timestamp, remarks);

                transactions.add(transaction);

            } catch (RuntimeException e) {
    throw new IOException(
            "Invalid transaction at line " + (i + 1)
            + ": " + e.getMessage(), e);
}
        }

        return transactions;
    }

    private String escapeCsv(String value) {
        if (value == null) {
            value = "";
        }

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    private List<String> parseCsvLine(String line)
            throws IOException {

        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (ch == ',' && !insideQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }

        if (insideQuotes) {
            throw new IOException("Unclosed quote in CSV record");
        }

        fields.add(field.toString());
        return fields;
    }
}
