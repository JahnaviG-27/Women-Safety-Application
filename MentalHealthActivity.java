package com.darkness.sparkwomen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MentalHealthActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final String[] breathingCycle = {"Inhale...", "Hold...", "Exhale..."};
    private int breathIndex = 0;
    private boolean isBreathing = false;
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = false;
    private int completedTasks = 0;
    private int totalTasks = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health);

        setupMoodSlider();
        setupBreathingExercise();
        setupMusicPlayer();
        setupTasks();
        setupJournal();
        setupHelpline();
    }

    private void setupMoodSlider() {
        EmojiMoodSlider moodSlider = findViewById(R.id.moodSlider);
        TextView moodMessage = findViewById(R.id.moodMessage);

        moodSlider.setOnMoodSelectedListener(position -> {
            String[] messages = {
                    "It's okay to not be okay. 💛",
                    "Every small step counts. 🌱",
                    "You're glowing with positivity. ✨"
            };
            moodMessage.setText(messages[Math.min(position, messages.length - 1)]);
        });
    }

    private void setupBreathingExercise() {
        MaterialButton breatheButton = findViewById(R.id.breatheButton);
        ImageView breathCircle = findViewById(R.id.breathCircle);
        TextView breathText = findViewById(R.id.breathText);

        breatheButton.setOnClickListener(v -> {
            if (!isBreathing) {
                breathCircle.setVisibility(View.VISIBLE);
                breathText.setVisibility(View.VISIBLE);
                breatheButton.setText(R.string.stop_breathing);
                handler.post(breathRunnable);
                isBreathing = true;
            } else {
                breathCircle.setVisibility(View.GONE);
                breathText.setVisibility(View.GONE);
                breatheButton.setText(R.string.start_breathing_exercise);
                handler.removeCallbacks(breathRunnable);
                breathCircle.clearAnimation();
                isBreathing = false;
            }
        });
    }

    private final Runnable breathRunnable = new Runnable() {
        @Override
        public void run() {
            TextView breathText = findViewById(R.id.breathText);
            ImageView breathCircle = findViewById(R.id.breathCircle);
            breathText.setText(breathingCycle[breathIndex]);

            ScaleAnimation animation;
            switch (breathIndex) {
                case 0: animation = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, 0.5f, 0.5f); animation.setDuration(4000); break;
                case 1: animation = new ScaleAnimation(1.4f, 1.4f, 1.4f, 1.4f, 0.5f, 0.5f); animation.setDuration(2000); break;
                default: animation = new ScaleAnimation(1.4f, 1f, 1.4f, 1f, 0.5f, 0.5f); animation.setDuration(4000); break;
            }

            animation.setFillAfter(true);
            breathCircle.startAnimation(animation);
            breathIndex = (breathIndex + 1) % breathingCycle.length;
            handler.postDelayed(this, (breathIndex == 1) ? 2000 : 4000);
        }
    };

    private void setupMusicPlayer() {
        MaterialButton musicButton = findViewById(R.id.playMusicButton);
        musicButton.setOnClickListener(v -> {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.calm_music);
                if (mediaPlayer == null) {
                    Toast.makeText(this, "Missing music file", Toast.LENGTH_SHORT).show();
                    return;
                }
                mediaPlayer.setLooping(true);
            }

            if (!isMusicPlaying) {
                mediaPlayer.start();
                musicButton.setText(R.string.stop_music);
                isMusicPlaying = true;
            } else {
                mediaPlayer.pause();
                musicButton.setText(R.string.play_calm_music);
                isMusicPlaying = false;
            }
        });
    }

    private void setupTasks() {
        MaterialButton walkTask = findViewById(R.id.walkTask);
        MaterialButton waterTask = findViewById(R.id.waterTask);
        MaterialButton breathTask = findViewById(R.id.breathTask);
        EditText taskInput = findViewById(R.id.taskInput);
        LinearLayout taskContainer = findViewById(R.id.taskContainer);
        MaterialButton addButton = findViewById(R.id.addTaskButton);

        setToggleTask(walkTask);
        setToggleTask(waterTask);
        setToggleTask(breathTask);

        addButton.setOnClickListener(v -> {
            String taskText = taskInput.getText().toString().trim();
            if (!taskText.isEmpty()) {
                MaterialButton newTask = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
                newTask.setText(taskText);
                newTask.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                setToggleTask(newTask);
                taskContainer.addView(newTask);
                totalTasks++;
                taskInput.setText("");
            } else {
                Toast.makeText(this, "Enter a task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToggleTask(MaterialButton taskButton) {
        taskButton.setCheckable(true);
        taskButton.setOnClickListener(v -> {
            boolean checked = taskButton.isChecked();
            if (checked) completedTasks++;
            else completedTasks--;
            checkCompletion();
        });
    }

    private void checkCompletion() {
        TextView message = findViewById(R.id.completionMessage);
        if (completedTasks == totalTasks) {
            message.setText("Great job completing all tasks! 🌟");
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.GONE);
        }
    }

    private void setupJournal() {
        MaterialButton saveButton = findViewById(R.id.saveNoteButton);
        EditText noteInput = findViewById(R.id.noteInput);

        saveButton.setOnClickListener(v -> {
            String note = noteInput.getText().toString().trim();
            if (!note.isEmpty()) {
                // Replace with Firebase/SQLite saving logic
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
                noteInput.setText("");
            } else {
                Toast.makeText(this, "Please write something first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupHelpline() {
        MaterialButton helplineButton = findViewById(R.id.helplineButton);
        helplineButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18005990019"));
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(breathRunnable);
    }
}
