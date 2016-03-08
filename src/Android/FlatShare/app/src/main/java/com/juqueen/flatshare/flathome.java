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
import android.widget.TextView;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class flatHome extends Fragment implements View.OnClickListener {

    ImageView imageView;
    ImageButton imageButton;
    LinearLayout revealView, layoutButtons;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    View v;
    SupportAnimator mAnimator;
    TextView name, nickName, address, flatId, owner;
    flatData selectedFlatData;


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

        name = (TextView) v.findViewById(R.id.tvName);
        nickName = (TextView) v.findViewById(R.id.tvnickName);
        flatId = (TextView) v.findViewById(R.id.tvFlaId);
        address = (TextView) v.findViewById(R.id.tvAddress);
        owner = (TextView) v.findViewById(R.id.tvOwner);
        selectedFlatData = getArguments().getParcelable("selectedflat");

        fillDetails(selectedFlatData);


        return v;
    }

    public void fillDetails(flatData selectedFlatData) {
        if (!selectedFlatData.getName().isEmpty() ) {
            name.setText(name.getText() + selectedFlatData.getName());
        } else {
            name.setText(name.getText() + "not provided");
        }
        if (!selectedFlatData.getNickName().isEmpty()) {
            nickName.setText(nickName.getText() + selectedFlatData.getNickName());
        } else {
            nickName.setText(nickName.getText() + "not provided");
        }
        if (!selectedFlatData.getFlatId().isEmpty()) {
            flatId.setText(flatId.getText() + selectedFlatData.getFlatId());
        } else {
            flatId.setText(flatId.getText() + "not provided");
        }
        if (!selectedFlatData.getAddress().isEmpty() ) {
            address.setText(address.getText() + selectedFlatData.getAddress());
        } else {
            address.setText(address.getText() + "not provided");
        }
        if (!selectedFlatData.getOwnerName().isEmpty()) {
            owner.setText(owner.getText() + selectedFlatData.getOwnerName());
        } else {
            owner.setText(owner.getText() + "not provided");
        }

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

    public static flatHome newInstance(flatData flatData) {

        flatHome f = new flatHome();
        Bundle b = new Bundle();
        b.putParcelable("selectedflat", flatData);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.launchTwitterAnimation) {
            launchTwitter(v);
        }

    }
}
