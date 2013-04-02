package de.inselhome.beermat.domain;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    private List<BillPosition> positions;

    public Bill() {
        positions = new ArrayList<BillPosition>();
    }

    public void addBillPosition(BillPosition billPosition) {
        Assert.assertNotNull("Cannot add empty bill position", billPosition);
        positions.add(billPosition);
    }

    public void removeBillPosition(BillPosition billPosition) {
        Assert.assertNotNull("Cannot remove empty bill position", billPosition);

        if (positions.contains(billPosition)) {
            positions.remove(billPosition);
        }
    }

    public double sum() {
        double sum = 0;

        for (BillPosition billPosition: positions) {
            sum += billPosition.sum();
        }

        return sum;
    }
}
