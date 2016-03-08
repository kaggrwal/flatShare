package com.juqueen.flatshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class flatList extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton createBtn;
    private FloatingActionButton joinBtn;
    private TextView emptyFlats;
    private globalFlatDbManager manager;
    private List<flatData> flatDatas;
    private ArrayList<String> itemName;


    ListView list;


    /*String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };*/

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
        emptyFlats = (TextView) findViewById(R.id.emptyFlat);


        createBtn.setOnClickListener(this);

        manager = new globalFlatDbManager(this);

        flatDatas = manager.getAllRows();


        if(flatDatas.size() != 0)
        {

            emptyFlats.setVisibility(View.INVISIBLE);
            flatRetrieval();
        }
        else{


            emptyFlats.setVisibility(View.VISIBLE);
            emptyFlats.setText("Currently there are no flats, Create One");
        }

    }


    public void flatRetrieval(){


        itemName = new ArrayList<String>();


        for(flatData flatData : flatDatas)
        {
            String nickName = flatData.getNickName();
            if(null != nickName && !nickName.isEmpty())
            {
                itemName.add(flatData.getName()+"("+nickName+")");
            }
            else
            {
                itemName.add(flatData.getName());
            }


        }

        customFlatListAdapter adapter=new customFlatListAdapter(this, itemName, imgid);
        list=(ListView)findViewById(R.id.flatList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                flatData selectedflat = flatDatas.get(position);
                Toast.makeText(getApplicationContext(), selectedflat.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(flatList.this,flatActivity.class);
                //Bundle mBundle = new Bundle();
                //mBundle.putParcelable("selectedflat", selectedflat);
                intent.putExtra("selectedflat",selectedflat);
                startActivity(intent);



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




