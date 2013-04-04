package de.inselhome.beermat.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;
import junit.framework.Assert;

import java.util.Collections;
import java.util.List;

public class BillPositionAdapter extends BaseAdapter {

    public interface ActionHandler {
        void onDecreaseClick(BillPosition billPosition);

        void onIncreaseClick(BillPosition billPosition);

        void onDetailClick(BillPosition billPosition);
    }

    private Context context;
    private final ActionHandler actionHandler;
    private List<BillPosition> billPositions;

    public BillPositionAdapter(final Context context, final ActionHandler actionHandler, final List<BillPosition> billPositions) {
        this.context = context;
        this.actionHandler = actionHandler;
        setBillPositions(billPositions);
    }

    @Override
    public int getCount() {
        return billPositions.size();
    }

    @Override
    public Object getItem(final int position) {
        if (position < getCount()) {
            return billPositions.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view;

        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_billposition, null);
        } else {
            view = convertView;
        }

        final BillPosition bp = (BillPosition) getItem(position);

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(bp.getBillItem().getDescription());

        TextView amount = (TextView) view.findViewById(R.id.amount);
        amount.setText(String.valueOf(bp.getAmount()));

        Button decrease = (Button) view.findViewById(R.id.decrease);
        Button increase = (Button) view.findViewById(R.id.increase);

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                actionHandler.onDetailClick(bp);
            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                actionHandler.onDetailClick(bp);
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                actionHandler.onDecreaseClick(bp);
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                actionHandler.onIncreaseClick(bp);
            }
        });

        return view;
    }

    private void setBillPositions(final List<BillPosition> billPositions) {
        Collections.sort(billPositions);
        this.billPositions = billPositions;
    }

    public void add(final BillPosition billPosition) {
        Assert.assertNotNull(billPosition);
        billPositions.add(billPosition);
        setBillPositions(billPositions);
        notifyDataSetChanged();
    }

    public void remove(final BillPosition billPosition) {
        Assert.assertNotNull(billPosition);
        if (billPositions.contains(billPosition)) {
            billPositions.remove(billPosition);
            setBillPositions(billPositions);
        }
    }

    public void removeAllItems() {
        billPositions.clear();
        notifyDataSetChanged();
    }

    public void resetAmounts() {
        for (BillPosition billPosition : billPositions) {
            billPosition.setAmount(0);
        }

        notifyDataSetChanged();
    }
}
