package com.ofss.model;
import java.math.BigDecimal;
public class WithdrawRequest {
    private int fromAccountId;
    private BigDecimal withdrawAmount;
    public int getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(int fromAccountId) { this.fromAccountId = fromAccountId; }
    public BigDecimal getWithdrawAmount() { return withdrawAmount; }
    public void setWithdrawAmount(BigDecimal withdrawAmount) { this.withdrawAmount = withdrawAmount; }
}
