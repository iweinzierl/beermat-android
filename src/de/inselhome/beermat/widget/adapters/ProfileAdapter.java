package de.inselhome.beermat.widget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.inselhome.beermat.R;
import de.inselhome.beermat.domain.Bill;

public class ProfileAdapter extends BillAdapter {

    public ProfileAdapter() {
        super();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listitem_profile, parent, false);

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView amount = (TextView) view.findViewById(R.id.item_amount);

        Bill bill = (Bill) getItem(position);
        if (bill == null) {
            return view;
        }

        date.setText(formatDate(bill.getDate()));
        name.setText(bill.getName());
        amount.setText(String.valueOf(bill.getImmutableBillPositions().size()));

        return view;
    }
}
