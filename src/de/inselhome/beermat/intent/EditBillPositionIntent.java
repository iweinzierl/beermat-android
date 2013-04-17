package de.inselhome.beermat.intent;

import android.content.Context;
import android.content.Intent;

import de.inselhome.beermat.EditBillPosition;
import de.inselhome.beermat.domain.BillPosition;

public class EditBillPositionIntent extends Intent {

    public static final int REQUEST_BILLPOSITION = 21;

    public static final String EXTRA_BILLPOSITION_ORIG = "extra.billposition.orig";
    public static final String EXTRA_BILLPOSITION_NEW = "extra.billposition.new";

    public EditBillPositionIntent(final Context context, final BillPosition billPosition) {
        super(context, EditBillPosition.class);
        putExtra(EXTRA_BILLPOSITION_ORIG, billPosition);
    }

    public EditBillPositionIntent(final Context context, final BillPosition oldBP, final BillPosition newBP) {
        super(context, EditBillPosition.class);
        putExtra(EXTRA_BILLPOSITION_ORIG, oldBP);
        putExtra(EXTRA_BILLPOSITION_NEW, newBP);
    }

    public EditBillPositionIntent(final Intent intent) {
        super(intent);
    }

    public EditBillPositionIntent(final Context context) {
        super(context, EditBillPosition.class);
    }

    public BillPosition getOldBillPosition() {
        Object obj = getSerializableExtra(EXTRA_BILLPOSITION_ORIG);
        return obj instanceof BillPosition ? (BillPosition) obj : null;
    }

    public BillPosition getNewBillPosition() {
        Object obj = getSerializableExtra(EXTRA_BILLPOSITION_NEW);
        return obj instanceof BillPosition ? (BillPosition) obj : null;
    }
}
