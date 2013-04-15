package de.inselhome.beermat.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import de.inselhome.beermat.R;

public class BillPositionContextDialogBuilder {

    public interface Callback {
        void onDelete();
    }

    private static Callback callback;

    public static Dialog build(Context context, Callback callback) {
        BillPositionContextDialogBuilder.callback = callback;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_billpositioncontext, null, true);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        Dialog dialog = builder.create();
        registerDeleteCallback(dialog, view);

        return dialog;
    }

    private static void registerDeleteCallback(final Dialog dialog, final View view) {
        View v = view.findViewById(R.id.delete);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onDelete();
            }
        });
    }
}
