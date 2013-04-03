package de.inselhome.beermat.domain;

import junit.framework.Assert;

import java.io.Serializable;

public class BillItem implements Serializable {

    private String description;
    private double price;

    public BillItem() {
    }

    public BillItem(String description, double price) {
        Assert.assertNotNull("Description of bill item may not be null", description);
        Assert.assertTrue("Description of bill item may not be empty", description.length() > 0);
        Assert.assertTrue("Price of bill item may not be negative", price > 0);

        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        Assert.assertNotNull("Description cannot be null", description);
        Assert.assertTrue("Description cannot be empty", description.length() > 0);
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        Assert.assertTrue("Price cannot be negative or null", price > 0);
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BillItem)) {
            return false;
        }

        BillItem other = (BillItem) o;

        if (other.getDescription() == null && getDescription() != null || other.getDescription() != null &&
                getDescription() == null) {
            return false;
        }

        if (!other.getDescription().equals(getDescription())) {
            return false;
        }

        if (other.getPrice() != getPrice()) {
            return false;
        }

        return true;
    }
}
