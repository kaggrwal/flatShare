package com.juqueen.flatshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;


public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup buttonsContainer;
    private Button activeButton = null;
    private final int MAX_BUTTONS = 4;
    private Button volatileGroup = null;
    private Button flat_Group = null;
    private TextView optionSelected = null;
    private TranslateAnimation trans1 = null;
    private TranslateAnimation trans2 = null;
    private int countFlatGrp = 0;
    private int countVolatileGrp = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        volatileGroup = (Button) findViewById(R.id.btnVolatile);
        flat_Group = (Button) findViewById(R.id.btnFlatGrp);
        optionSelected = (TextView) findViewById(R.id.tvOptionSelected);

        trans1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -30, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        trans1.setDuration(1000);


        trans2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 30, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        trans2.setDuration(1500);




        flat_Group.setOnClickListener(this);
        volatileGroup.setOnClickListener(this);



        this.buttonsContainer = (ViewGroup) findViewById(R.id.buttonsContainer);

       // MaterialRippleLayout.on(this.buttonsContainer).rippleColor(Color.BLACK).create();

        int buttonsSpacing = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int buttonSize = (int) getResources().getDimension(R.dimen.button_size);

        buttonsSpacing = dpToPixel(buttonsSpacing,getApplicationContext())-8;
        buttonSize = dpToPixel(buttonSize,getApplicationContext());

        for (int i = 0; i < MAX_BUTTONS; i++) {

            Button button = (Button) getLayoutInflater().inflate(R.layout.circular_button_layout, buttonsContainer,false);
            //button.setMinHeight(20);
            //button.setMinWidth(20);
//            MaterialRippleLayout.on(button).rippleColor(Color.BLACK).create();

            button.setOnClickListener(this);
            buttonsContainer.addView(button);

            //Add margin between buttons manually
            if (i != MAX_BUTTONS - 1) {
                buttonsContainer.addView(new Space(this), new ViewGroup.LayoutParams(buttonsSpacing, buttonSize));
            }
        }

        //selectButton((Button) buttonsContainer.getChildAt(0));
    }




    private static Float scale;
    public static int dpToPixel(int dp, Context context) {
        if (scale == null)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) dp * scale);
    }



    public void onClick(View view) {
        //selectButton((Button) view);

        if(view.getId() == R.id.btnFlatGrp)
        {
            this.buttonsContainer.setVisibility(View.VISIBLE);

            if(countFlatGrp ==0) {
                optionSelected.startAnimation(trans1);
                optionSelected.setVisibility(View.VISIBLE);
                optionSelected.setText("This option let's you to visit the flats or groups you have created/can create for full time duration,\nyou can manage bills, share files with your friends and much more.");
            }
            else if(countFlatGrp<countVolatileGrp)
            {
                optionSelected.startAnimation(trans2);
                optionSelected.setVisibility(View.INVISIBLE);
                optionSelected.setText("This option let's you to visit the flats or groups you have created/can create for full time duration,\nyou can manage bills, share files with your friends and much more.");
                optionSelected.startAnimation(trans1);
                optionSelected.setVisibility(View.VISIBLE);

            }
            else if(countFlatGrp >= countVolatileGrp)
            {
                optionSelected.setText("This option let's you to visit the flats or groups you have created/can create for full time duration,\nyou can manage bills, share files with your friends and much more.");
                optionSelected.setVisibility(View.VISIBLE);
            }

        }
        else if(view.getId()== R.id.btnVolatile)
        {

            this.buttonsContainer.setVisibility(View.VISIBLE);
            if(countVolatileGrp ==0) {
                optionSelected.setText("This option let's you to visit the groups you have created/can create for shorter duration,\n" +
                        "you can manage bills with the friends that are not part of Flatshare community.");
                optionSelected.startAnimation(trans1);
                optionSelected.setVisibility(View.VISIBLE);
            }
            else if(countVolatileGrp < countFlatGrp)
            {
                optionSelected.startAnimation(trans2);
                optionSelected.setVisibility(View.INVISIBLE);
                optionSelected.setText("This option let's you to visit the groups you have created/can create for shorter duration,\n" +
                        "you can manage bills with the friends that are not part of Flatshare community.");
                optionSelected.startAnimation(trans1);
                optionSelected.setVisibility(View.VISIBLE);

            }
            else if(countVolatileGrp >=countFlatGrp)
            {
                optionSelected.setText("This option let's you to visit the groups you have created/can create for shorter duration,\n" +
                        "you can manage bills with the friends that are not part of Flatshare community.");
                optionSelected.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            selectButton((Button) view);
        }



    }

    private void selectButton(Button button) {
        if (activeButton != null) {
            activeButton.setSelected(false);
            activeButton = null;
        }

        activeButton = button;
        button.setSelected(true);

        if(button == buttonsContainer.getChildAt(0))
        {
            Intent intent = new Intent(homeActivity.this, flatList.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
