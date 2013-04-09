package de.inselhome.beermat.domain;

import java.io.Serializable;

import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;

public class BillPosition implements Serializable, Comparable<BillPosition> {

    private long id;
    private BillItem billItem;
    private int amount;

    public BillPosition() { }

    public BillPosition(final BillItem billItem) {
        Assert.assertNotNull("Bill item may not be null", billItem);
        this.billItem = billItem;
    }

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(final BillItem billItem) {
        this.billItem = billItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
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
    public boolean equals(final Object o) {
        if (!(o instanceof BillPosition)) {
            return false;
        }

        BillPosition other = (BillPosition) o;

        if (other.getAmount() != getAmount()) {
            return false;
        }

        if (!other.getBillItem().equals(getBillItem())) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(final BillPosition another) {
        return getBillItem().compareTo(another.getBillItem());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static JSONObject toJson(BillPosition billPosition) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", billPosition.getId());
        obj.put("amount", billPosition.getAmount());
        obj.put("billItem", BillItem.toJson(billPosition.getBillItem()));

        return obj;
    }
}
