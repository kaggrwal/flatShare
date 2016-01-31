package com.juqueen.flatshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;


public class loadingScreen extends ActionBarActivity {

    private boolean IS_CONNECTED_INTERNET = false;
    private boolean IS_LOGGED_IN = false;
    private ImageView flatshareText;
    private Button fbLogin;
    private CallbackManager callbackManager;

    private Button gSignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_loading_screen);

        flatshareText = (ImageView) findViewById(R.id.imageView_flatshare_text);
        fbLogin = (Button) findViewById(R.id.btnFb);
        gSignin = (Button) findViewById(R.id.btnGplus);

        gSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loadingScreen.this, homeActivity.class);
                startActivity(intent);


            }
        });


        connectionDetector Detector = new connectionDetector(getApplicationContext());

        IS_CONNECTED_INTERNET = Detector.checkInternet();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                if (IS_CONNECTED_INTERNET) {

                    if (!IS_LOGGED_IN) {
                        translateFlatshareText();
                        fbintialize();
                    }


                } else {

                    showAlertDialog(loadingScreen.this, "No Internet Connection",
                            "You don't have internet connection" + "\n" + "Application will exit now", false);
                    System.exit(0);
                }


            }


        }, 2200);


    }


    private void fbintialize() {

        final Collection<String> permissons = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");

        callbackManager = CallbackManager.Factory.create();
        //fbLogin.setReadPermissions(Arrays.asList("public_profile", "user_friends", "user_status"));
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(loadingScreen.this, permissons);
            }
        });
        fbloginProcess();


    }

    private void fbloginProcess() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(loadingScreen.this, "logging", Toast.LENGTH_SHORT).show();
                final String[] name = {null};
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                //Log.v("LoginActivity", response.toString());
                                try {
                                    name[0] = object.getString("name");
                                    Toast.makeText(loadingScreen.this, name[0], Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

                Log.e("dd", "facebook login canceled");

            }

            @Override
            public void onError(FacebookException e) {

                Log.e("dd", "facebook login failed error");

            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void translateFlatshareText() {


        //////////////   ///////////////////////////////////////////////////////////////////////////////
        TranslateAnimation trans1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        trans1.setDuration(1000);
        trans1.setFillAfter(true);
        trans1.setFillEnabled(true);
        flatshareText.setAnimation(trans1);
        flatshareText.startAnimation(trans1);

        LinearLayout btnLayout = (LinearLayout) findViewById(R.id.btnLinearLayout);

        btnLayout.setVisibility(View.VISIBLE);


        // gSignin.setWidth(flatshareText.getWidth()-20);
        //gSignin.setHeight(flatshareText.getHeight()-20);

        /*gSignin.setStyle(SignInButton.SIZE_STANDARD,SignInButton.COLOR_DARK);
        fbLogin.setWidth(500);
        fbLogin.setHeight(gSignin.getHeight());
        fbLogin.setVisibility(View.VISIBLE);
        gSignin.setVisibility(View.VISIBLE);
*/
        /*AlphaAnimation alphaAnimation = new AlphaAnimation(30,100);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setFillEnabled(true);
        fbLogin.setAnimation(alphaAnimation);*/


    }


    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_screen, menu);
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
