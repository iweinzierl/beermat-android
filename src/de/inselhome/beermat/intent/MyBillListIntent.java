package de.inselhome.beermat.intent;

import android.content.Context;
import android.content.Intent;
import de.inselhome.beermat.MyBillList;
import de.inselhome.beermat.domain.Bill;

public class MyBillListIntent extends Intent {

    public static final int REQUEST_BILL = 31;

    public static final String EXTRA_BILL = "mybilllist.extra.bill";

    public MyBillListIntent(Context context) {
        super(context, MyBillList.class);
    }

    public MyBillListIntent(Intent i) {
        super(i);
    }

    public MyBillListIntent(Context context, Bill bill) {
        this(context);
        putExtra(EXTRA_BILL, bill);
    }

    public Bill getBill() {
        Object obj = getSerializableExtra(EXTRA_BILL);
        return obj instanceof Bill ? (Bill) obj : null;
    }
}
