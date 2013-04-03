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

import java.util.List;

public class BillPositionAdapter extends BaseAdapter {

    public interface ClickListener {
        void onDecreaseClick(BillPosition billPosition);
        void onIncreaseClick(BillPosition billPosition);
    }

    private Context context;
    private final ClickListener clickListener;
    private List<BillPosition> billPositions;

    public BillPositionAdapter(final Context context, final ClickListener clickListener,
                               final List<BillPosition> billPositions) {
        this.context = context;
        this.clickListener = clickListener;
        this.billPositions = billPositions;
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

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onDecreaseClick(bp);
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onIncreaseClick(bp);
            }
        });

        return view;
    }

    public void add(BillPosition billPosition) {
        Assert.assertNotNull(billPosition);
        billPositions.add(billPosition);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        billPositions.clear();
        notifyDataSetChanged();
    }
}
