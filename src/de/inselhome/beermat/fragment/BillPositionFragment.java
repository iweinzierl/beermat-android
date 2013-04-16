package de.inselhome.beermat.fragment;

import android.app.Activity;
import android.os.Bundle;
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

public class BillPositionFragment extends SherlockFragment {

    public interface Callback {
        void onOk(BillPosition billPosition);

        void onCancel();
    }

    private Callback callback;

    private EditText description;
    private EditText price;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Activity activity = getActivity();
        if (!(activity instanceof Callback)) {
            throw new IllegalArgumentException("Parent Activity must implement Callback!");
        }

        callback = (Callback) activity;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_newbillposition, container, false);
        description = (EditText) view.findViewById(R.id.description);
        price = (EditText) view.findViewById(R.id.price);

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        setupOkButton(ok);
        setupCancelButton(cancel);

        return view;
    }

    private void setupOkButton(Button ok) {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillPosition billPosition = extractBillPosition();
                if (billPosition != null) {
                    callback.onOk(billPosition);
                }
                else {
                    callback.onCancel();
                }
            }
        });
    }

    private void setupCancelButton(Button cancel) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onCancel();
            }
        });
    }

    private BillPosition extractBillPosition() {
        String desc = description.getText().toString();
        String rawPrice = price.getText().toString();

        try {
            BillItem item = new BillItem(desc, NumberFormat.getNumberInstance().parse(rawPrice).doubleValue());
            return new BillPosition(item);
        } catch (ParseException e) {

        }

        return null;
    }
}
