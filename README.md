
# Personal Finance Tracker

## 1. Project Overview

The Personal Finance Tracker is a Java-based console application that helps users record income and expenses, calculate financial summaries, filter transactions, and export monthly reports to CSV files.

The project uses Java's `BigDecimal` for accurate monetary calculations and CSV files for persistent transaction storage.

## 2. Features

- Add income and expense transactions.
- Record transaction amount, category, timestamp, and remarks.
- Store and reload transactions using a CSV file.
- Calculate total income, total expenses, and net cash flow.
- Generate monthly financial summaries.
- Calculate category-wise expenses and spending percentages.
- Filter transactions by date range and category.
- Export monthly financial summaries to CSV.
- Validate transaction amounts and required fields.

## 3. Technologies Used

- Java 11 or later
- Java Collections Framework
- `BigDecimal` for monetary calculations
- `java.time` for dates and timestamps
- Java NIO.2 for file handling
- CSV for data storage and report export

## 4. Project Structure

## Project Structure


Task-2-Personal-Finance-Tracker/
│
├── src/
│   ├── app/
│   │   └── Main.java
│   │
│   ├── model/
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   │
│   ├── service/
│   │   └── FinanceService.java
│   │
│   └── storage/
│       ├── SummaryExporter.java
│       └── TransactionStorage.java
│
├── data/
│   └── transactions.csv
│
├── exports/
│   └── financial-summary.csv
│
├── screenshots/
│   ├── 1-Main-menu.png
│   ├── 2-Financial-Summary.png
│   └── 3-exported-csv.png
│
├── out/      # Compiled Java classes
│   ├── app/
│   ├── model/
│   ├── service/
│   └── storage/
│
├── .gitignore
└── README.md

## 5. How to Run

Open a terminal in the project root directory.

Compile the source files:

```powershell
javac -d out src/model/*.java src/service/*.java src/storage/*.java src/app/*.java
```

Run the application:

```powershell
java -cp out app.Main
```

Follow the console menu to add income, add expenses, view the financial summary, export a monthly report, or exit.

## 6. Mathematical Approach

### Total Income

The sum of all income transactions within the selected period.

### Total Expenses

The sum of all expense transactions within the selected period.

### Net Cash Flow

Net Cash Flow = Total Income − Total Expenses

A positive value indicates that income exceeds expenses for the selected period. A negative value indicates that expenses exceed income.

### Category-wise Spending Percentage

Category Spending Percentage = (Expenses in a Category ÷ Total Expenses) × 100

The application rounds the percentage to two decimal places using `RoundingMode.HALF_UP`. If total expenses are zero, the percentage is returned as zero to avoid division by zero.

## 7. Sample Results

Using the included sample transactions for October 2026:

| Metric       | Result |
|----------    |---:      |
| Total Income | 28000.00 |
| Total Expenses | 3500.00 |
| Net Cash Flow | 24500.00 |
| Food Expenses | 2500.00 |
| Food Spending Percentage | 71.43% |
| Travel Expenses | 1000.00 |
| Travel Spending Percentage | 28.57% |

## 8. Data Storage and Export

Transactions are stored in `data/transactions.csv`. The monthly financial report is exported to `exports/financial-summary.csv`.

The CSV files can be opened using spreadsheet applications such as Microsoft Excel or LibreOffice Calc.

## 9. Validation and Testing

The project has been tested for:

- Loading sample transactions from CSV.
- Saving and reloading transactions.
- Calculating income, expenses, and net cash flow.
- Generating monthly financial summaries.
- Calculating category-wise spending percentages.
- Filtering by date range and category.
- Exporting the monthly financial summary.
- Adding income and expense transactions through the console menu.

## 10. Limitations

- The application uses local CSV files rather than a database.
- The interface is console-based.
- Monthly reports use the month selected by the application.

## 11. Future Improvements

- Add transaction editing and deletion.
- Improve CSV validation and error reporting.
- Add transaction search and sorting.
- Support customizable reporting periods.
- Add a graphical user interface.

## 12. Author

Developed as a Java project for the CodeNova internship.
