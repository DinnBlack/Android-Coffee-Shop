package com.example.thefutuscoffeeversion13.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.thefutuscoffeeversion13.Domain.ToppingModel;
import com.example.thefutuscoffeeversion13.R;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.List;

public class ToppingAdapter extends ArrayAdapter<ToppingModel> {

    private Context context;
    private List<ToppingModel> toppingList;
    private String selectedToppingTitle = "";
    private OnItemCheckedListener onItemCheckedListener;

    public List<ToppingModel> getToppingList() {
        return toppingList;
    }
    public void setOnItemCheckedListener(OnItemCheckedListener listener) {
        this.onItemCheckedListener = listener;
    }
    public ToppingAdapter(Context context, List<ToppingModel> toppingList) {
        super(context, 0, toppingList);
        this.context = context;
        this.toppingList = toppingList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToppingModel item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_list_topping, parent, false);
        }

        RadioButton radioButton = convertView.findViewById(R.id.checkBoxTopping);
        TextView tvTitleTopping = convertView.findViewById(R.id.tvTitleTopping);
        TextView tvPriceTopping = convertView.findViewById(R.id.tvPriceTopping);

        radioButton.setChecked(item.isSelected());
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Uncheck other radio buttons
                uncheckOtherRadioButtons(position);
            }
            item.setSelected(isChecked);
            notifyDataSetChanged();
        });

        convertView.setOnClickListener(view -> {
            if (radioButton.isChecked()) {
                radioButton.setChecked(false);
                item.setSelected(false);
                selectedToppingTitle = "";
                notifyDataSetChanged();
            } else {
                radioButton.setChecked(true);
                item.setSelected(true);
                uncheckOtherRadioButtons(position);
                notifyDataSetChanged();
                selectedToppingTitle = item.getTitle();
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.onItemChecked(item);
                }
            }
        });

        tvTitleTopping.setText(item.getTitle());
        tvPriceTopping.setText(formatCurrency(item.getPrice()));

        return convertView;
    }

    public interface OnItemCheckedListener {
        void onItemChecked(ToppingModel item);
    }

    private void uncheckOtherRadioButtons(int position) {
        for (int i = 0; i < getCount(); i++) {
            ToppingModel item = getItem(i);
            if (item != null) {
                item.setSelected(i == position);
            }
        }
        notifyDataSetChanged();
    }

    public String getSelectedToppingTitle() {
        return selectedToppingTitle;
    }

    private static String formatCurrency(String originalNumber) {
        int length = originalNumber.length();
        StringBuilder formattedNumber = new StringBuilder(originalNumber);
        if (length >= 4) {
            formattedNumber.insert(length - 3, '.');
            if (length >= 7) {
                formattedNumber.insert(length - 6, '.');
            }
            if (length >= 10) {
                formattedNumber.insert(length - 9, '.');
            }
            return formattedNumber.toString();
        }
        return "";
    }
}
