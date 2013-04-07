package de.inselhome.beermat.test;

import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static List<BillPosition> createBillPositionList() {
        List<BillPosition> billPositions = new ArrayList<BillPosition>();

        for (int i = 0; i < 15; i++) {
            billPositions.add(createBillPosition("Item " + i, 1.90d));
        }

        return billPositions;
    }

    public static BillPosition createBillPosition(final String description, final double price) {
        BillItem billItem = new BillItem(description, price);
        return new BillPosition(billItem);
    }
}
