package com.example.demoapplication.utility;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import com.example.demoapplication.R;

public class DialogHelper {

    public static class DialogCallback {
        public void onPositiveButtonClick(View view) {}

        public void onNegativeButtonClick(View view){}
    }


    public static void showGenericDialog(final Activity context, boolean isCancellable, String title, String message,
                                         String positiveBtnText, String negativeBtnText, final DialogCallback dialogCallback) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(isCancellable);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveBtnText, (dialogInterface, i) -> {
                if (dialogCallback != null){
                    dialogCallback.onPositiveButtonClick(new View(context));
                }
            });
            builder.setNegativeButton(negativeBtnText, (dialogInterface, i) -> {
                if (dialogCallback != null) {
                    dialogCallback.onNegativeButtonClick(new View(context));
                }
            });
            Dialog dialog = builder.create();
            Window window = dialog.getWindow();
            if(window!=null) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
            }

            if (!context.isFinishing()) {
                dialog.show();
            }
        } catch (Exception ignore) {}
    }


    public static void showOkDialog(Activity context, boolean isCancellable, String title, String message,
                                    final DialogCallback dialogCallback) {
        showGenericDialog(context, isCancellable, title, message,context.getString(R.string.dialog_btn_ok_text), null, dialogCallback);
    }


}
