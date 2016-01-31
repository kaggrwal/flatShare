package com.juqueen.flatshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class flatList extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton createBtn;
    private FloatingActionButton joinBtn;

    ListView list;
    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    Integer[] imgid={
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
            R.drawable.fb_icon,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_list);

        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.menuFLatList);
        createBtn = (FloatingActionButton) findViewById(R.id.fabFlatToCreate);
        joinBtn = (FloatingActionButton) findViewById(R.id.fabFlatToJoin);

        createBtn.setOnClickListener(this);

        customFlatListAdapter adapter=new customFlatListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.flatList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();


        if(id == R.id.fabFlatToCreate);
        {
            Intent intent = new Intent(flatList.this, flatProfile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }


    }
}




