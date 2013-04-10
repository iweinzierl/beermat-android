package de.inselhome.beermat.test;

import de.inselhome.beermat.domain.Bill;
import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestData {

    public static List<Bill> createBillList() {
        List<Bill> bills = new ArrayList<Bill>();

        for (int i = 0; i < 10; i++) {
            Bill bill = new Bill();
            bill.setDate(new Date());
            bill.setId(i);
            bill.setName("Bill " + i);
            bill.addBillPosition(createBillPosition("Bill Pos " + i, 1.90));
            bills.add(bill);
        }

        return bills;
    }

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
