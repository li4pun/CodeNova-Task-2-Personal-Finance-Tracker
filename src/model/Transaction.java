package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction
 {
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private LocalDateTime timestamp;
    private String remarks;
public Transaction(TransactionType type, BigDecimal amount,
                   String category, LocalDateTime timestamp,
                   String remarks) 
        {

    if (type == null) 
        {
        throw new IllegalArgumentException("Transaction type cannot be null");
     }

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) 
        {
        throw new IllegalArgumentException("Amount must be greater than zero");
    }

    if (category == null || category.isBlank()) 
        {
        throw new IllegalArgumentException("Category cannot be empty");
    }

    if (timestamp == null) {
        throw new IllegalArgumentException("Timestamp cannot be null");
    }

    this.type = type;
    this.amount = amount;
    this.category = category;
    this.timestamp = timestamp;
    this.remarks = remarks;
}

 public TransactionType getType()
 {
    return type;
 }
 public BigDecimal getAmount()
{
 return amount;
}
public  String getCategory()
{
    return category;
}
public LocalDateTime getTimestamp()
{
    return timestamp;
}
public String getRemarks()
{
    return remarks;
}



















}
