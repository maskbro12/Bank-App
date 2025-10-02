package com.ofss.model;
import java.math.BigDecimal;
public class TransferRequest {
    private int fromSourceAccount;
    private int toTargetAccount;
    private BigDecimal transferAmount;
    public int getFromSourceAccount() { return fromSourceAccount; }
    public void setFromSourceAccount(int fromSourceAccount) { this.fromSourceAccount = fromSourceAccount; }
    public int getToTargetAccount() { return toTargetAccount; }
    public void setToTargetAccount(int toTargetAccount) { this.toTargetAccount = toTargetAccount; }
    public BigDecimal getTransferAmount() { return transferAmount; }
    public void setTransferAmount(BigDecimal transferAmount) { this.transferAmount = transferAmount; }
}
