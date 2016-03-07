package com.juqueen.flatshare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class flatHome extends Fragment implements View.OnClickListener{

    ImageView imageView;
    ImageButton imageButton;
    LinearLayout revealView, layoutButtons;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    View v;
    SupportAnimator mAnimator;


    public flatHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v = inflater.inflate(R.layout.fragment_flat_home, container, false);


        pixelDensity = getResources().getDisplayMetrics().density;

        imageView = (ImageView) v.findViewById(R.id.imageView);
        imageButton = (ImageButton) v.findViewById(R.id.launchTwitterAnimation);

        imageButton.setOnClickListener(this);
        revealView = (LinearLayout) v.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) v.findViewById(R.id.layoutButtons);

        alphaAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim);


        return v;
    }

    public void launchTwitter(View view) {

        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */

        if (mAnimator != null && !mAnimator.isRunning()) {
            mAnimator = mAnimator.reverse();

            imageButton.setBackgroundResource(R.drawable.rounded_button);
            imageButton.setImageResource(R.drawable.fb_icon);

            mAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    revealView.setVisibility(View.GONE);
                    layoutButtons.setVisibility(View.GONE);
                    mAnimator = null;
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
        } else if (mAnimator != null) {
            mAnimator.cancel();
            return;
        } else {

            // get the center for the clipping circle
            //int cx = (myView.getLeft() + myView.getRight()) / 2;
            //int cy = (myView.getTop() + myView.getBottom()) / 2;
            int cx = imageView.getRight();
            int cy = imageView.getBottom();
            cx -= ((28 * pixelDensity) + (16 * pixelDensity));


            // get the final radius for the clipping circle

            int finalRadius = (int) Math.hypot(imageView.getWidth(), imageView.getHeight());

            FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                    revealView.getLayoutParams();
            parameters.height = imageView.getHeight();
            revealView.setLayoutParams(parameters);


            mAnimator = ViewAnimationUtils.createCircularReveal(revealView, cx, cy, 0, finalRadius);

            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
            imageButton.setImageResource(R.drawable.gplus_icon);

            mAnimator.addListener(new SupportAnimator.AnimatorListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                    layoutButtons.setVisibility(View.VISIBLE);
                    layoutButtons.startAnimation(alphaAnimation);
                }

                @Override
                public void onAnimationCancel() {

                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            revealView.setVisibility(View.VISIBLE);

        }
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(1500);
        mAnimator.start();
    }

    public static flatHome newInstance(String text) {

        flatHome f = new flatHome();
        //Bundle b = new Bundle();
        //b.putString("msg", text);

        //f.setArguments(b);

        return f;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.launchTwitterAnimation)
        {
            launchTwitter(v);
        }

    }
}
