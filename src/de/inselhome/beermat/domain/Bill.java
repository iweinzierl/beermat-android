package de.inselhome.beermat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class Bill implements Serializable {

    private long id;
    private String name;
    private Date date;
    private List<BillPosition> positions;

    public Bill() {
        positions = new ArrayList<BillPosition>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addBillPosition(BillPosition billPosition) {
        assertNotNull("Cannot add empty bill position", billPosition);
        positions.add(billPosition);
    }

    public void removeBillPosition(BillPosition billPosition) {
        assertNotNull("Cannot remove empty bill position", billPosition);

        if (positions.contains(billPosition)) {
            positions.remove(billPosition);
        }
    }

    public List<BillPosition> getImmutableBillPositions() {
        return Collections.unmodifiableList(positions);
    }

    public double sum() {
        double sum = 0;

        for (BillPosition billPosition: positions) {
            sum += billPosition.sum();
        }

        return sum;
    }


    @Override
    public String toString() {
        return "Bill=[" + id + ", " + name + ", " + date + "]";
    }
}
