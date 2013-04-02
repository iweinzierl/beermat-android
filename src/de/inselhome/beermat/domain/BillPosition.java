package de.inselhome.beermat.domain;

import junit.framework.Assert;

public class BillPosition {

    private BillItem billItem;
    private int amount;


    public BillPosition(BillItem billItem) {
        Assert.assertNotNull("Bill item may not be null", billItem);
        this.billItem = billItem;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void increase() {
        this.amount++;
    }

    public void decrease() {
        if (this.amount > 0) {
            this.amount--;
        }
    }

    public double sum() {
        return billItem.getPrice() * amount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BillPosition)) {
            return false;
        }

        BillPosition other = (BillPosition) o;

        if (other.getAmount() != getAmount()) {
            return false;
        }

        if (other.getBillItem() != getBillItem()) {
            return false;
        }

        return true;
    }
}

