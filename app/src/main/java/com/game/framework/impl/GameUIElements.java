package com.game.framework.impl;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.antibot.poop.R;

public class GameUIElements {

    public Toast toast;
    View toastView;
    TextView toastTextView;
    Typeface purisaTypeFace;

    public GameUIElements(GLGame game) {
        toast = new Toast(game);
        toastView = game.getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup) game.findViewById(R.id.toast_layout_root));
        purisaTypeFace = Typeface.createFromAsset(game.getAssets(), "Purisa.ttf");

        toast.setView(toastView);
        toastTextView = (TextView) toastView.findViewById(R.id.toast_text);

        toastTextView.setTypeface(purisaTypeFace);
        toast.setDuration(Toast.LENGTH_SHORT);
    }

    public void showNotEnoughCoinsToast() {
        toastTextView.setText("not enough coins");
        toast.show();
    }

    public void showClickAgainToConfirmToast() {
        toastTextView.setText("click again to confirm");
        toast.show();
    }
}
