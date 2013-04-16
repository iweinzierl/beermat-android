package de.inselhome.beermat;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.fragment.BillPositionFragment;
import de.inselhome.beermat.intent.NewBillPositionIntent;

public class NewBillPosition extends SherlockFragmentActivity implements BillPositionFragment.Callback {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_newbillposition);

        BillPositionFragment newBillPositionFragment = new BillPositionFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.newBillPositionFragment, newBillPositionFragment).commit();
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
