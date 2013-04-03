package de.inselhome.beermat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.BillPositionFragment;
import de.inselhome.beermat.intent.NewBillPositionIntent;

public class NewBillPosition extends Activity implements BillPositionFragment.ActionHandler {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_newbillposition);

        BillPositionFragment newBillPositionFragment = new BillPositionFragment(this);
        getFragmentManager().beginTransaction().add(R.id.newBillPositionFragment, newBillPositionFragment).commit();
    }

    @Override
    public void onOk(BillPosition billPosition) {
        Intent data = new NewBillPositionIntent(this, billPosition);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onCancel() {
        finish();
    }
}
