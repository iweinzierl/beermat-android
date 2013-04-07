package de.inselhome.beermat.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockFragment;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;

import java.text.NumberFormat;
import java.text.ParseException;

public class EditBillPositionFragment extends SherlockFragment {

    public interface ActionHandler {
        void onOkClicked(BillPosition billPosition);

        void onCancelClicked();
    }

    private final ActionHandler handler;
    private final BillPosition billPosition;

    private EditText description;
    private EditText price;

    public EditBillPositionFragment(final ActionHandler handler, final BillPosition billPosition) {
        this.handler = handler;
        this.billPosition = billPosition;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editbillposition, container, false);

        description = (EditText) view.findViewById(R.id.description);
        description.setText(billPosition.getBillItem().getDescription());

        price = (EditText) view.findViewById(R.id.price);
        price.setText(String.valueOf(billPosition.getBillItem().getPrice()));

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        prepareOkButton(ok);
        prepareCancelButton(cancel);

        return view;
    }

    private void prepareOkButton(final Button ok) {
        ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    BillPosition billPosition = extractBillPosition();
                    if (billPosition != null) {
                        handler.onOkClicked(billPosition);
                    } else {
                        handler.onCancelClicked();
                    }
                }
            });
    }

    private void prepareCancelButton(final Button cancel) {
        cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    handler.onCancelClicked();
                }
            });
    }

    private BillPosition extractBillPosition() {
        String rawDescription = description.getText().toString();
        String rawPrice = price.getText().toString();

        try {
            BillItem item = new BillItem(rawDescription, NumberFormat.getNumberInstance().parse(rawPrice).doubleValue());
            BillPosition newBP = new BillPosition(item);
            newBP.setAmount(billPosition.getAmount());

            return newBP;
        } catch (ParseException e) {
            Log.e("[BEERMAT]", "Unable to parse price value: " + rawPrice, e);
        }

        return null;
    }
}
