package com.juqueen.flatshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class flatProfile extends AppCompatActivity implements View.OnClickListener {


    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText input_flat_name, input_flat_nickname, input_flat_address;
    private TextInputLayout input_layout_flat_name, input_layout_flat_nickname, input_layout_flat_address;
    private Button btnCreateGroup;
    private ImageView coverPic;
    private URI coverPicURI = null;
    private FloatingActionButton coverPicChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_profile);


        input_layout_flat_name = (TextInputLayout) findViewById(R.id.input_layout_flat_name);
        input_layout_flat_address = (TextInputLayout) findViewById(R.id.input_layout_flat_address);
        input_layout_flat_nickname = (TextInputLayout) findViewById(R.id.input_layout_flat_nickname);
        input_flat_name = (EditText) findViewById(R.id.input_flat_name);
        input_flat_address = (EditText) findViewById(R.id.input_flat_address);
        input_flat_nickname = (EditText) findViewById(R.id.input_flat_nickname);
        btnCreateGroup = (Button) findViewById(R.id.btn_create_flat);
        btnCreateGroup.setOnClickListener(this);

        coverPicChange = (FloatingActionButton) findViewById(R.id.fabCoverPicAdd);
        coverPicChange.setOnClickListener(this);

        coverPic = (ImageView) findViewById(R.id.imageView_flat_cover_pic);
        //coverPic.setOnClickListener(this);

        input_flat_name.addTextChangedListener(new MyTextWatcher(input_flat_name));

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.fabCoverPicAdd) {
            changePic();
        } else if (id == R.id.btn_create_flat) {
            submitForm();
        }


    }

    private void submitForm() {
        String ownerName;

        localFlatDbManager.Path =  "/flatshare/data/flats/";

        if (!validateFlatName()) {
            return;
        }

        xmlProfile profile = new xmlProfile(null,null,null,null,this);

        try {
            profile.parseXMLAndStoreIt();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ownerName = profile.getFullName();
        if(ownerName == null)
        {
            ownerName ="";
        }

        flatData flatRow = new flatData(this.input_flat_name.getText().toString(),
                this.input_flat_nickname.getText().toString(), this.input_flat_address.getText().toString(), ownerName, "Fake");
   //     directoryCheck(this);

        flatMemberData memberRow = new flatMemberData(ownerName,profile.getDob(), "Owner", flatRow.getFlatId(), profile.getUn_ID() );

        localFlatDbManager.DATABASE_NAME=flatRow.getFlatId()+"_LocalFlat.db";
        localFlatDbManager.Path = localFlatDbManager.Path+flatRow.getFlatId()+"/";

        globalFlatDbManager manager = new globalFlatDbManager(this);
        manager.addRow(flatRow);
        manager.close();


        localFlatDbManager manager1 = new localFlatDbManager(this);
        manager1.addRow(memberRow);
        manager1.close();

        Toast.makeText(getApplicationContext(), "Group Created", Toast.LENGTH_SHORT).show();

        dirStructureCreation(flatRow.getFlatId());

        Intent intent = new Intent(flatProfile.this,flatList.class);
        startActivity(intent);
        finish();


    }

    private void dirStructureCreation(String flatId) {

       directoryCheck(this,"/flatshare/data/flats/"+flatId+"/members");
    }


    private File directoryCheck(Context ctx,String address) {

        File dataDirectory = new File(ctx.getFilesDir()+address);
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                Log.e("fladir ", "Problem creating direc");
                return null;
            }
        }
        return dataDirectory;


    }


    private boolean validateFlatName() {

        if (input_flat_name.getText().toString().trim().isEmpty()) {
            input_layout_flat_name.setError(getString(R.string.err_msg_name));
            requestFocus(input_flat_name);
            return false;
        } else {
            input_layout_flat_name.setErrorEnabled(false);
        }

        return true;

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void changePic() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(flatProfile.this);

        builder.setTitle("Add Cover Pic!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "tempCoverPic.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    //pic = f;

                    startActivityForResult(intent, 1);


                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                //h=0;
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("tempCoverPic.jpg")) {

                        f = temp;
                        File photo = new File(Environment.getExternalStorageDirectory(), "tempCoverPic.jpg");
                        //pic = photo;
                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);

                    coverPic.setImageBitmap(bitmap);
                    String path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default";
                    //p = path;

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        //pic=file;
                        outFile.flush();

                        outFile.close();


                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();
                // h=1;
                //imgui = selectedImage;
                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                String picturePath = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));


                //Log.w("path of image from gallery......******************.........", picturePath + "");


                coverPic.setImageBitmap(thumbnail);

            }


        }
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
                case R.id.input_flat_name:
                    validateFlatName();
                    break;
            }
        }
    }
}
