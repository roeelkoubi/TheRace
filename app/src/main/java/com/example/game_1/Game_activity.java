package com.example.game_1;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class Game_activity extends AppCompatActivity {

        private static final int DELAY = 1000;
        private final int L = 0, C = 1, right = 2; // right is the R cause we already have R in the system
        private int clock = 0;
        private Timer timer;

        private int spongebob_area = C;
        private ImageButton leftArrow, rightArrow;
        private boolean hitFlag = false;
        private int hearts_counter = 2;

        private ImageView[][] jellyfish;
        private ImageView[] explosions;
        private ImageView[] spongebob;
        private ImageView[] hearts;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.game_activity);
            hideSystemUI();
            initViews();

            rightArrow.setOnClickListener(v -> {
                if (spongebob_area < right) {
                    spongebob[spongebob_area].setVisibility(View.INVISIBLE);
                    spongebob[++spongebob_area].setVisibility(View.VISIBLE);
                    HittingJellyFish();
                }
            });
            leftArrow.setOnClickListener(v -> {
                if (spongebob_area > L) {
                    spongebob[spongebob_area].setVisibility(View.INVISIBLE);
                    spongebob[--spongebob_area].setVisibility(View.VISIBLE);
                    HittingJellyFish();
                }
            });
        }

         private void updateUI() {
             startTicker();
         }

         @Override
        protected void onStart() {
             super.onStart();
             updateUI();
         }

        @Override
        protected void onResume() {
            super.onResume();
            hideSystemUI();
        }

        private void startTicker() {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.d("timeTick", "Tick: " + clock + " On Thread: " + Thread.currentThread().getName());
                    runOnUiThread(() -> {
                        Log.d("timeTick", "Tick: " + clock + " On Thread: " + Thread.currentThread().getName());
                        setJellyfish();
                    });
                }
            }, 0, DELAY);
        }

            private void setJellyfish() {
            clock++;
            hideExplosions();
            for (int i = 0; i < 3; i++) {
                if  (jellyfish[5][i].getVisibility() == View.VISIBLE) {
                    jellyfish[5][i].setVisibility(View.GONE);
                }
                for (int j = 4; j >= 0; j--) {
                    if (jellyfish[j][i].getVisibility() == View.VISIBLE) {
                        jellyfish[j][i].setVisibility(View.INVISIBLE);
                        jellyfish[j + 1][i].setVisibility(View.VISIBLE);
                    }
                }
            }
            if (clock % 2 == 0) {
                int lane = jellyfish_random_area();
                switch (lane) {
                    case L:
                        jellyfish[0][L].setVisibility(View.VISIBLE);
                        break;
                    case C:
                        jellyfish[0][C].setVisibility(View.VISIBLE);
                        break;
                    case right:
                        jellyfish[0][right].setVisibility(View.VISIBLE);
                        break;
                }
            }
                HittingJellyFish();
        }

        private void hideExplosions() {
            explosions[L].setVisibility(View.GONE);
            explosions[C].setVisibility(View.GONE);
            explosions[right].setVisibility(View.GONE);
            spongebob[spongebob_area].setVisibility(View.VISIBLE);

        }

        private void HittingJellyFish() {
            if (jellyfish[5][L].getVisibility() == View.VISIBLE
                    && spongebob[L].getVisibility() == View.VISIBLE) {
                jellyfish[5][L].setVisibility(View.GONE);
                spongebob[L].setVisibility(View.GONE);
                explosions[L].setVisibility(View.VISIBLE);
                hearts[hearts_counter--].setVisibility(View.INVISIBLE);
                toast();
                vibrate();
            } else if (jellyfish[5][C].getVisibility() == View.VISIBLE
                    && spongebob[C].getVisibility() == View.VISIBLE) {
                jellyfish[5][C].setVisibility(View.GONE);
                spongebob[C].setVisibility(View.GONE);
                explosions[C].setVisibility(View.VISIBLE);
                hearts[hearts_counter--].setVisibility(View.INVISIBLE);
                toast();
                vibrate();
            } else if (jellyfish[5][right].getVisibility() == View.VISIBLE
                    && spongebob[right].getVisibility() == View.VISIBLE) {
                jellyfish[5][right].setVisibility(View.GONE);
                spongebob[right].setVisibility(View.GONE);
                explosions[right].setVisibility(View.VISIBLE);
                hearts[hearts_counter--].setVisibility(View.INVISIBLE);
                toast();
                vibrate();
            }
        }

        private void toast() {
            switch (hearts_counter) {
                case 1:
                    Toast.makeText(this, "Be safe!", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(this, "Be safe!!", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show();
                    restartGame();
                    break;
            }
        }

        private void vibrate() {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        private void restartGame() {
            hearts_counter = 2;
            hearts[0].setVisibility(View.VISIBLE);
            hearts[1].setVisibility(View.VISIBLE);
            hearts[2].setVisibility(View.VISIBLE);
        }

        private int jellyfish_random_area() {

            return (int) (Math.random() * (right + 1 - L)) + L;
        }

        @Override
        protected void onStop() {
            super.onStop();
            stopTicker();
        }

        private void stopTicker() {
            timer.cancel();
        }

        private void initViews() {
            jellyfish = new ImageView[6][3];
            explosions = new ImageView[3];
            spongebob = new ImageView[3];
            hearts = new ImageView[3];
            jellyfish[0][L] = findViewById(R.id.jellyFishL1);
            jellyfish[1][L] = findViewById(R.id.jellyFishL2);
            jellyfish[2][L] = findViewById(R.id.jellyFishL3);
            jellyfish[3][L] = findViewById(R.id.jellyFishL4);
            jellyfish[4][L] = findViewById(R.id.jellyFishL5);
            jellyfish[5][L] = findViewById(R.id.jellyFishL6);
            jellyfish[0][C] = findViewById(R.id.jellyFishC1);
            jellyfish[1][C] = findViewById(R.id.jellyFishC2);
            jellyfish[2][C] = findViewById(R.id.jellyFishC3);
            jellyfish[3][C] = findViewById(R.id.jellyFishC4);
            jellyfish[4][C] = findViewById(R.id.jellyFishC5);
            jellyfish[5][C] = findViewById(R.id.jellyFishC6);
            jellyfish[0][right] = findViewById(R.id.jellyFishR1);
            jellyfish[1][right] = findViewById(R.id.jellyFishR2);
            jellyfish[2][right] = findViewById(R.id.jellyFishR3);
            jellyfish[3][right] = findViewById(R.id.jellyFishR4);
            jellyfish[4][right] = findViewById(R.id.jellyFishR5);
            jellyfish[5][right] = findViewById(R.id.jellyFishR6);
            explosions[L] = findViewById(R.id.explosion_l);
            explosions[C] = findViewById(R.id.explosion_c);
            explosions[right] = findViewById(R.id.explosion_r);
            spongebob[L] = findViewById(R.id.spongebob_l);
            spongebob[C] = findViewById(R.id.spongebob_c);
            spongebob[right] = findViewById(R.id.spongebob_r);
            leftArrow = findViewById(R.id.left_arrow);
            rightArrow = findViewById(R.id.right_arrow);
            hearts[0] = findViewById(R.id.heart1);
            hearts[1] = findViewById(R.id.heart2);
            hearts[2] = findViewById(R.id.heart3);
        }

        public void hideSystemUI() {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }