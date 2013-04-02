package de.inselhome.beermat.widget.adapters;

import java.util.List;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.BillPosition;

public class BillPositionAdapter extends BaseAdapter {

    private Context context;
    private List<BillPosition> billPositions;

    public BillPositionAdapter(final Context context, final List<BillPosition> billPositions) {
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

        return view;
    }
}
