package de.inselhome.beermat.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillItem;
import de.inselhome.beermat.domain.BillPosition;
import de.inselhome.beermat.exception.BeermatException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class EditBillPositionFragment extends SherlockFragment {

    private static final String LOGTAG = "[beermat] EditBillPositionFragment";
    private static final String SAVED_DESCRIPTION = "bundle.description";
    private static final String SAVED_PRICE = "bundle.price";

    public interface Callback {
        BillPosition getBillPosition();

        void onOkClicked(BillPosition billPosition);

        void onCancelClicked();
    }

    private Callback callback;

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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editbillposition, container, false);

        Button ok = (Button) view.findViewById(R.id.ok);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        prepareOkButton(ok);
        prepareCancelButton(cancel);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        if (bundle != null) {
            setDescription(bundle.getString(SAVED_DESCRIPTION));
            setPrice(bundle.getDouble(SAVED_PRICE));
        } else {
            setDescription(callback.getBillPosition().getBillItem().getDescription());
            setPrice(callback.getBillPosition().getBillItem().getPrice());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(SAVED_DESCRIPTION, getDescription());

        try {
            bundle.putDouble(SAVED_PRICE, getPrice());
        } catch (BeermatException e) {
            Log.w(LOGTAG, "Unable to save price");
        }
    }

    private EditText getDescriptionField() {
        View v = getView();
        if (v != null) {
            return (EditText) v.findViewById(R.id.description);
        }

        return null;
    }

    public void setDescription(String description) {
        EditText edit = getDescriptionField();

        if (edit != null) {
            edit.setText(description);
        }
    }

    public String getDescription() {
        EditText edit = getDescriptionField();

        if (edit != null) {
            return edit.getText().toString();
        }

        return null;
    }

    private EditText getPriceField() {
        View v = getView();
        if (v != null) {
            return (EditText) v.findViewById(R.id.price);
        }

        return null;
    }

    public void setPrice(double price) {
        EditText edit = getPriceField();

        if (edit != null) {
            edit.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(price));
        }
    }

    public double getPrice() throws BeermatException {
        EditText edit = getPriceField();

        if (edit != null) {
            String text = edit.getText().toString();
            try {
                return NumberFormat.getNumberInstance(Locale.getDefault()).parse(text).doubleValue();
            } catch (ParseException e) {
                throw new BeermatException(getString(R.string.invalid_number_format), e);
            }
        }

        return -1;
    }

    private void prepareOkButton(final Button ok) {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    BillPosition billPosition = extractBillPosition();
                    if (billPosition != null) {
                        callback.onOkClicked(billPosition);
                    } else {
                        callback.onCancelClicked();
                    }
                } catch (BeermatException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void prepareCancelButton(final Button cancel) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                callback.onCancelClicked();
            }
        });
    }

    private BillPosition extractBillPosition() throws BeermatException {
        BillItem item = new BillItem(getDescription(), getPrice());
        BillPosition newBP = new BillPosition(item);
        newBP.setAmount(callback.getBillPosition().getAmount());

        return newBP;
    }
}
