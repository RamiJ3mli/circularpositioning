package tn.ramijemli.circularpositioning;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.constraint_layout)
    ConstraintLayout mConstraintLayout;

    private ConstraintSet constraintSet;
    private ValueAnimator hoursAnimation, minutesAnimation, secondsAnimation, msAnimation;
    private int hoursRadius, minutesRadius, secondsRadius, msRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        constraintSet = new ConstraintSet();
        constraintSet.clone(mConstraintLayout);
        initAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hoursAnimation != null) {
            if (hoursAnimation.isPaused()) {
                hoursAnimation.resume();
                minutesAnimation.resume();
                secondsAnimation.resume();
                msAnimation.resume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hoursAnimation.isRunning()) {
            hoursAnimation.pause();
            minutesAnimation.pause();
            secondsAnimation.pause();
            msAnimation.pause();
        }
    }


    @Override
    protected void onDestroy() {
        constraintSet = null;
        if (hoursAnimation.isRunning()) {
            hoursAnimation.cancel();
            minutesAnimation.cancel();
            secondsAnimation.cancel();
            msAnimation.cancel();

            hoursAnimation.removeAllUpdateListeners();
            minutesAnimation.removeAllUpdateListeners();
            secondsAnimation.removeAllUpdateListeners();
            msAnimation.removeAllUpdateListeners();
        }
        hoursAnimation = minutesAnimation = secondsAnimation = msAnimation = null;
        super.onDestroy();
    }

    private void initAnimations() {
        hoursRadius = ScreenUtil.convertDIPToPixels(getApplicationContext(), 40);
        minutesRadius = ScreenUtil.convertDIPToPixels(getApplicationContext(), 51);
        secondsRadius = ScreenUtil.convertDIPToPixels(getApplicationContext(), 51);
        msRadius = ScreenUtil.convertDIPToPixels(getApplicationContext(), 7);

        //HOURS HAND ANIMATION
        hoursAnimation = animate(TimeUnit.SECONDS.toMillis(40));
        hoursAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int degrees = (Integer) valueAnimator.getAnimatedValue();
                constraintSet.constrainCircle(R.id.hour_hand, R.id.clock_background, hoursRadius, degrees);
                constraintSet.setRotation(R.id.hour_hand, degrees);
                constraintSet.applyTo(mConstraintLayout);
            }
        });

        //MINUTES HAND ANIMATION
        minutesAnimation = animate(TimeUnit.SECONDS.toMillis(10));
        minutesAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int degrees = (Integer) valueAnimator.getAnimatedValue();
                constraintSet.constrainCircle(R.id.minute_hand, R.id.clock_background, minutesRadius, degrees);
                constraintSet.setRotation(R.id.minute_hand, degrees);
                constraintSet.applyTo(mConstraintLayout);
            }
        });

        //SECONDS HAND ANIMATION
        secondsAnimation = animate(TimeUnit.SECONDS.toMillis(3));
        secondsAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int degrees = (Integer) valueAnimator.getAnimatedValue();
                constraintSet.constrainCircle(R.id.second_hand, R.id.clock_background, secondsRadius, degrees);
                constraintSet.setRotation(R.id.second_hand, degrees);
                constraintSet.applyTo(mConstraintLayout);
            }
        });

        //SECONDS HAND ANIMATION
        msAnimation = animate(200);
        msAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int degrees = (Integer) valueAnimator.getAnimatedValue();
                constraintSet.constrainCircle(R.id.ticker, R.id.second_hand, msRadius, degrees);
                constraintSet.setRotation(R.id.ticker, degrees);
                constraintSet.applyTo(mConstraintLayout);
            }
        });
    }

    private ValueAnimator animate(long orbitDuration) {
        ValueAnimator anim = ValueAnimator.ofInt(0, 359);
        anim.setDuration(orbitDuration);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        return anim;
    }

    @OnClick(R.id.start)
    public void startAction() {
        if (hoursAnimation.isPaused()) {
            hoursAnimation.resume();
            minutesAnimation.resume();
            secondsAnimation.resume();
            msAnimation.resume();
        } else if (hoursAnimation.isRunning()) {
            hoursAnimation.pause();
            minutesAnimation.pause();
            secondsAnimation.pause();
            msAnimation.pause();
        } else {
            hoursAnimation.start();
            minutesAnimation.start();
            secondsAnimation.start();
            msAnimation.start();
        }
    }
}
