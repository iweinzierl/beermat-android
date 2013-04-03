package de.inselhome.beermat.intent;

import android.content.Context;
import android.content.Intent;
import de.inselhome.beermat.NewBillPosition;
import de.inselhome.beermat.domain.BillPosition;

public class NewBillPositionIntent extends Intent {

    public static final int REQUEST_BILLPOSITION = 1;

    public static final String EXTRA_BILLPOSITION = "extra.billposition";

    public NewBillPositionIntent(Context context) {
        super(context, NewBillPosition.class);
    }

    public NewBillPositionIntent(Intent intent) {
        super(intent);
    }

    public NewBillPositionIntent(Context context, BillPosition billPosition) {
        this(context);
        putExtra(EXTRA_BILLPOSITION, billPosition);
    }

    public BillPosition getBillPosition() {
        Object obj = getSerializableExtra(EXTRA_BILLPOSITION);
        return obj instanceof BillPosition ? (BillPosition) obj : null;
    }
}
