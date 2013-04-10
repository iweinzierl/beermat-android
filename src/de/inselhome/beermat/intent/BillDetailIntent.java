package de.inselhome.beermat.intent;

import android.content.Context;
import android.content.Intent;
import de.inselhome.beermat.BillDetail;
import de.inselhome.beermat.domain.Bill;

public class BillDetailIntent extends Intent {

    public static final String EXTRA_BILL = "extra.billdetail.bill";

    public BillDetailIntent(Intent i) {
        super(i);
    }

    public BillDetailIntent(Context context, Bill bill) {
        super(context, BillDetail.class);
        putExtra(EXTRA_BILL, bill);
    }

    public Bill getBill() {
        Object obj = getSerializableExtra(EXTRA_BILL);
        return obj instanceof Bill ? (Bill) obj : null;
    }
}
