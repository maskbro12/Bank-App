package com.ofss.model;
import java.math.BigDecimal;
public class DepositRequest {
    private int toDestinationAccount;
    private BigDecimal depositAmount;
    public int getToDestinationAccount() { return toDestinationAccount; }
    public void setToDestinationAccount(int toDestinationAccount) { this.toDestinationAccount = toDestinationAccount; }
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
}
