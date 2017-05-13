// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

public class BalanceData {
    private String cardNumber;
    private String ownerName;
    private String ownerEmail;
    private double cardBalance;

    public BalanceData() {
        super();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public double getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(double cardBalance) {
        this.cardBalance = cardBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceData that = (BalanceData) o;

        if (Double.compare(that.cardBalance, cardBalance) != 0) return false;
        if (!cardNumber.equals(that.cardNumber)) return false;
        if (ownerName != null ? !ownerName.equals(that.ownerName) : that.ownerName != null)
            return false;
        return ownerEmail != null ? ownerEmail.equals(that.ownerEmail) : that.ownerEmail == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cardNumber.hashCode();
        result = 31 * result + (ownerName != null ? ownerName.hashCode() : 0);
        result = 31 * result + (ownerEmail != null ? ownerEmail.hashCode() : 0);
        temp = Double.doubleToLongBits(cardBalance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
