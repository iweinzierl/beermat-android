package de.inselhome.beermat.test;

import java.util.ArrayList;
import java.util.List;

import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;

public class TestData {

    public static List<BillPosition> createBillPositionList() {
        List<BillPosition> billPositions = new ArrayList<BillPosition>();
        billPositions.add(createBillPosition("First item", 1.90d));
        billPositions.add(createBillPosition("Second item", 2.30d));
        return billPositions;
    }

    public static BillPosition createBillPosition(final String description, final double price) {
        BillItem billItem = new BillItem(description, price);
        return new BillPosition(billItem);
    }
}
