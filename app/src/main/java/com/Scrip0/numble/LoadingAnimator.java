package com.Scrip0.numble;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingAnimator extends ConstraintLayout {

    private Timer timer;

    public LoadingAnimator(Context context) {
        this(context, null, 0);
    }

    public LoadingAnimator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.loading_anim, this, true);
        ArrayList<TextView> tiles = new ArrayList<>();
        tiles.add(findViewById(R.id.tile1));
        tiles.add(findViewById(R.id.tile2));
        tiles.add(findViewById(R.id.tile3));
        tiles.add(findViewById(R.id.tile4));
        tiles.add(findViewById(R.id.tile5));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (i == 0) {
                    i = 1;
                    startTileAnim(tiles.get(0), ContextCompat.getColor(context, R.color.cell_wrong), 0);
                    startTileAnim(tiles.get(1), ContextCompat.getColor(context, R.color.cell_close), 1);
                    startTileAnim(tiles.get(2), ContextCompat.getColor(context, R.color.cell_close), 2);
                    startTileAnim(tiles.get(3), ContextCompat.getColor(context, R.color.cell_right), 3);
                    startTileAnim(tiles.get(4), ContextCompat.getColor(context, R.color.cell_wrong), 4);
                } else {
                    i = 0;
                    startTileAnim(tiles.get(0), ContextCompat.getColor(context, R.color.black), 0);
                    startTileAnim(tiles.get(1), ContextCompat.getColor(context, R.color.black), 1);
                    startTileAnim(tiles.get(2), ContextCompat.getColor(context, R.color.black), 2);
                    startTileAnim(tiles.get(3), ContextCompat.getColor(context, R.color.black), 3);
                    startTileAnim(tiles.get(4), ContextCompat.getColor(context, R.color.black), 4);
                }
            }
        }, 0, 1500);
    }

    private void startTileAnim(final TextView tile, int color, int delay) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in);
        animation.setStartOffset(delay * 100L);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tile.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                Animation scaleOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_out);
                scaleOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                tile.startAnimation(scaleOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tile.startAnimation(animation);
    }

    public void cancelAnimation() {
        if (timer != null)
            timer.cancel();
    }
}