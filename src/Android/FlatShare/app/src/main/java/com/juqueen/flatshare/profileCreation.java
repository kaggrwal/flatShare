package com.juqueen.flatshare;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class profileCreation extends AppCompatActivity {


    private EditText inputName, inputEmail, inputDOB, inputContactNo;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutDOB, inputLayoutContactNo;
    private Button btnCreateProfile;
    private static Pattern dateFrmtPtrn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);


        dateFrmtPtrn = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutDOB = (TextInputLayout) findViewById(R.id.input_layout_dob);
        inputLayoutContactNo = (TextInputLayout) findViewById(R.id.input_layout_contact_no);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputDOB = (EditText) findViewById(R.id.input_dob);
        inputContactNo = (EditText) findViewById(R.id.input_contact_no);
        btnCreateProfile = (Button) findViewById(R.id.btn_create_profile);
        btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();

            }
        });
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputDOB.addTextChangedListener(new MyTextWatcher(inputDOB));
        inputContactNo.addTextChangedListener((new MyTextWatcher(inputContactNo)));

    }

    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validateDOB()) {
            return;
        }




        new xmlProfile(inputName.getText().toString(), inputEmail.getText().toString(),
                inputDOB.getText().toString(), inputContactNo.getText().toString(), this.getApplicationContext());

        Toast.makeText(getApplicationContext(), "Profile Created", Toast.LENGTH_SHORT).show();

    }

    private boolean validateName() {

        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;

    }

    private void requestFocus(View view) {

        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateDOB() {

        String dob = inputDOB.getText().toString().trim();
        if (dob.isEmpty() || !isvalidDate(dob)) {
            inputLayoutDOB.setError((getString(R.string.err_msg_dob)));
            requestFocus(inputDOB);
            return false;
        } else {
            inputLayoutDOB.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isvalidDate(String dob) {

        Matcher mtch = dateFrmtPtrn.matcher(dob);
        if (mtch.matches()) {
            return true;
        }
        return false;


    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    private boolean isValidEmail(String email) {

        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_dob:
                    validateDOB();
                    break;
                case R.id.input_contact_no:
                    validateContactNo();
                    break;
            }
        }
    }

    private boolean validateContactNo() {

        String contactNo = inputContactNo.getText().toString().trim();

        if (contactNo.isEmpty() || !isValidContactNo(contactNo)) {
            inputLayoutContactNo.setError(getString(R.string.err_msg_contact_no));
            requestFocus(inputContactNo);
            return false;
        } else {
            inputLayoutContactNo.setErrorEnabled(false);
        }

        return true;

    }

    private boolean isValidContactNo(String contactNo) {
        if (contactNo.length() == 10) {
            return true;
        }
        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_creation, menu);
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


