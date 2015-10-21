/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jaspersoft.android.sdk.ui.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A view that extends Spinner and allows to choose multiple items. Selected values are displayed on the spinner
 * divided by comma.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class MultiSelectSpinner<T> extends Spinner implements DialogInterface.OnMultiChoiceClickListener {

    private static final String TEXT_SEPARATOR = ", ";
    private static final String TEXT_ELLIPSIS = "\u2026";
    private static final int TEXT_MAX_LENGTH = 100;

    private List<T> items;
    CharSequence[] stringItems;
    private boolean[] checkedItems;
    private boolean[] defaultCheckedItems;
    /**
     * the text that displays on the spinner if there is nothing selected.
     */
    private String defaultText;
    /**
     * The listener that receives notifications when items are selected.
     */
    private OnItemsSelectedListener onItemsSelectedListener;

    public MultiSelectSpinner(Context context) {
        super(context);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiSelectSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * This method will be invoked when an item in the dialog is clicked.
     *
     * @param dialog    The dialog where the selection was made.
     * @param which     The position of the item in the list that was clicked.
     * @param isChecked True if the click checked the item, else false.
     */
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        checkedItems[which] = isChecked;
        updateState(false);
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // The prompt to display when the dialog is shown
        if (getPrompt() != null) {
            builder.setTitle(getPrompt());
        }
        defaultCheckedItems = Arrays.copyOf(checkedItems, checkedItems.length);
        builder.setMultiChoiceItems(stringItems, checkedItems, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateState();
                    }
                });
        builder.setNegativeButton(R.string.mss_btn_check_all, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getListView().setAdapter(initCustomAdapter(dialog));
                Button button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setText(ifAnyChecked() ? R.string.mss_btn_uncheck_all : R.string.mss_btn_check_all);

                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Button button = (Button) view;
                                boolean ifAnyChecked = ifAnyChecked();
                                button.setText(ifAnyChecked ? R.string.mss_btn_check_all : R.string.mss_btn_uncheck_all);

                                if (ifAnyChecked) {
                                    unselectAll();
                                } else {
                                    selectAll();
                                }

                                dialog.getListView().setAdapter(initCustomAdapter(dialog));
                                updateState(false);
                            }
                        });
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                checkedItems = defaultCheckedItems;
                updateState(false);
            }
        });
        dialog.show();
        return true;
    }

    private boolean ifAnyChecked() {
        boolean ifAnyCheckedMask = false;
        for (boolean checkedItem : checkedItems) {
            ifAnyCheckedMask = ifAnyCheckedMask || checkedItem;
        }
        return ifAnyCheckedMask;
    }

    private BaseAdapter initCustomAdapter(final AlertDialog dialog) {
        return new ArrayAdapter<CharSequence>(
                getContext(), R.layout.select_dialog_multichoice, android.R.id.text1, stringItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (checkedItems != null) {
                    boolean isItemChecked = checkedItems[position];
                    if (isItemChecked) {
                        dialog.getListView().setItemChecked(position, true);
                    }
                }
                return view;
            }
        };
    }

    /**
     * Gets a list of items displayed in the spinner.
     *
     * @return list of items
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * Sets a list of items to be displayed in the spinner.
     *
     * @param items list of items
     */
    public void setItemsList(List<T> items) {
        setItemsList(items, null);
    }

    /**
     * Sets a list of items to be displayed in the spinner.
     *
     * @param items       list of items
     * @param defaultText the text that displays on the spinner if there is nothing selected.
     */
    public void setItemsList(List<T> items, String defaultText) {
        this.defaultText = (defaultText != null) ? defaultText : "";
        this.items = items;

        int size = items.size();

        // init the strings list
        stringItems = new CharSequence[size];
        checkedItems = new boolean[size];

        for (int i = 0; i < size; i++) {
            stringItems[i] = items.get(i).toString();
            checkedItems[i] = false;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{defaultText});
        setAdapter(adapter);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of
     * @return The position of the specified item, or -1 if this list does not contain the element
     */
    public int getItemPosition(T item) {
        return items.indexOf(item);
    }

    /**
     * Returns positions of the currently selected items.
     *
     * @return List of Integers (starting at 0).
     */
    public List<Integer> getSelectedItemsPositions() {
        List<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < items.size(); i++) {
            if (checkedItems[i]) {
                positions.add(i);
            }
        }
        return positions;
    }

    /**
     * Sets the currently selected items.
     *
     * @param positions List of indexes (starting at 0) of the data items to be selected.
     */
    public void setSelection(List<Integer> positions) {
        unselectAll();
        for (int position : positions) {
            if (position < checkedItems.length) {
                checkedItems[position] = true;
            }
        }
        updateState();
    }

    /**
     * Returns the list of the currently selected items.
     *
     * @return The list that contains all of the currently selected items, or
     * empty list if there is nothing selected.
     */
    public List<T> getSelectedItems() {
        List<T> selectedItems = new ArrayList<T>();
        for (Integer position : getSelectedItemsPositions()) {
            selectedItems.add(items.get(position));
        }
        return selectedItems;
    }

    public void setOnItemsSelectedListener(OnItemsSelectedListener listener) {
        this.onItemsSelectedListener = listener;
    }

    public interface OnItemsSelectedListener {
        public void onItemsSelected(List selectedItems);
    }

    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException();
    }

    public void setSelection(int position, boolean animate) {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private void updateState() {
        updateState(true);
    }

    private void updateState(boolean notifyListener) {
        // refresh text on spinner
        StringBuilder spinnerBuilder = new StringBuilder();
        for (int i = 0; i < stringItems.length; i++) {
            if (checkedItems[i]) {
                if (spinnerBuilder.length() > 0) {
                    spinnerBuilder.append(TEXT_SEPARATOR);
                }
                if (spinnerBuilder.length() > TEXT_MAX_LENGTH) {
                    spinnerBuilder.append(TEXT_ELLIPSIS);
                    break;
                }
                spinnerBuilder.append(stringItems[i]);
            }
        }

        String spinnerText = (spinnerBuilder.length() > 0) ? spinnerBuilder.toString() : defaultText;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{spinnerText});
        setAdapter(adapter);

        if (onItemsSelectedListener != null && notifyListener) {
            onItemsSelectedListener.onItemsSelected(getSelectedItems());
        }
    }

    private void selectAll() {
        setSelection(true);
    }

    private void unselectAll() {
        setSelection(false);
    }

    private void setSelection(boolean isSelected) {
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = isSelected;
        }
    }
}
