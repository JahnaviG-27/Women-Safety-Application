// EmojiMoodSlider.java
package com.darkness.sparkwomen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;

public class EmojiMoodSlider extends LinearLayout {

    private String[] emojis = {"😭", "😔", "😐", "🙂", "😊"};
    private int selectedPosition = 2; // Default neutral
    private OnMoodSelectedListener listener;

    public interface OnMoodSelectedListener {
        void onMoodSelected(int position);
    }

    public EmojiMoodSlider(Context context) {
        super(context);
        init();
    }

    public EmojiMoodSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmojiMoodSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        createEmojiViews();
    }

    private void createEmojiViews() {
        removeAllViews();

        for (int i = 0; i < emojis.length; i++) {
            final int position = i;
            TextView emojiView = new TextView(getContext());
            emojiView.setText(emojis[i]);
            emojiView.setTextSize(i == selectedPosition ? 24 : 18);
            emojiView.setPadding(dpToPx(16), 0, dpToPx(16), 0);

            emojiView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    if (listener != null) {
                        listener.onMoodSelected(position);
                    }
                    updateEmojiSizes();
                }
            });

            addView(emojiView);
        }
    }

    private void updateEmojiSizes() {
        for (int i = 0; i < getChildCount(); i++) {
            TextView child = (TextView) getChildAt(i);
            boolean isSelected = (i == selectedPosition);
            child.setTextSize(isSelected ? 24 : 18);
            child.animate()
                    .scaleX(isSelected ? 1.2f : 1f)
                    .scaleY(isSelected ? 1.2f : 1f)
                    .setDuration(200)
                    .start();
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void setOnMoodSelectedListener(OnMoodSelectedListener listener) {
        this.listener = listener;
    }
}