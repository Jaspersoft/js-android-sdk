package com.jaspersoft.android.sdk.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ParamsDialog  {
    private static final String KEY = "params";
    private final Context context;
    private final String content;
    private final Bundle savedState;

    private EditText paramsContent;
    private OnResult callback = NullCallback.INSTANCE;
    private AlertDialog dialog;

    private ParamsDialog(Builder builder) {
        this.context = builder.context;
        this.savedState = builder.savedState;
        this.content = builder.params;
    }

    public static class Builder {
        private final Context context;
        private final Bundle savedState;
        private String params;

        public Builder(Context context, Bundle savedState) {
            this.context = context;
            this.savedState = savedState;
        }

        public Builder params(String data) {
            this.params = data;
            return this;
        }

        public ParamsDialog build() {
            if (params == null) {
                params = "{}";
            }
            return new ParamsDialog(this);
        }
    }

    public void show() {
        onCreate(savedState);
        dialog.show();
    }

    public Bundle saveInstanceState() {
        Bundle dialogState = dialog.onSaveInstanceState();
        dialogState.putString(KEY, getParams());
        return dialogState;
    }

    public void removeCallbacks() {
        callback = NullCallback.INSTANCE;
    }

    public void setCallback(OnResult callback) {
        this.callback = callback;
    }

    protected void onCreate(Bundle savedInstanceState) {
        ViewGroup customView = createView();

        String savedContent = savedInstanceState == null ? content : savedInstanceState.getString(KEY);
        savedContent = savedContent == null ? content : savedContent;

        paramsContent.setText(savedContent);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Apply params");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<ReportParameter> parameters = ParamsMapper.INSTANCE.fromJson(getParams());
                callback.onParamsResult(parameters);
            }
        });
        builder.setCancelable(true);
        builder.setView(customView);

        dialog = builder.create();
        if (savedInstanceState != null) {
            dialog.onRestoreInstanceState(savedInstanceState);
        }
    }

    @NonNull
    private String getParams() {
        return paramsContent.getText().toString();
    }

    private ViewGroup createView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.params_dialog, null);
        paramsContent = (EditText) root.findViewById(R.id.paramsContent);
        return root;
    }

    public interface OnResult {
        void onParamsResult(List<ReportParameter> params);
    }

    private static class NullCallback implements OnResult {
        private static OnResult INSTANCE = new NullCallback();

        private NullCallback() {
        }

        @Override
        public void onParamsResult(List<ReportParameter> params) {
        }
    }
}
