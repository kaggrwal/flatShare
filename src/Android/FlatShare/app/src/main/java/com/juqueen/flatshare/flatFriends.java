package com.juqueen.flatshare;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class flatFriends extends Fragment {
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    FloatingActionMenu mFloatingMenu;
    Bitmap homeIcon;
    Bitmap userIcon;
    Bitmap addIcon;

    public flatFriends() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flat_friends, container, false);


        addIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.gplus_icon);
        homeIcon =  BitmapFactory.decodeResource(this.getResources(), R.drawable.fb_icon);
        userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.fb_icon);


       fillFriendsArray();


        gridArray.add(new Item(addIcon, "Add"));

        gridView = (GridView) v.findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(v.getContext(), R.layout.element_grid, gridArray);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                if (position < gridArray.size() - 1) {
                    Toast.makeText(v.getContext(), gridArray.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    mFloatingMenu.show(true);
                }
                else
                {
                    mFloatingMenu.hide(true);
                }


            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFloatingMenu.hide(true);
                return false;
            }
        });


        FloatingActionItem item1 = new FloatingActionItem.Builder(0)
                .withResId(R.drawable.fb_icon)
                .withDelay(0)
                .withPadding(10)
                .build();

        FloatingActionItem item2 = new FloatingActionItem.Builder(1)
                .withResId(R.drawable.fb_icon)
                .withDelay(50)
                .withPadding(10)
                .build();

        FloatingActionItem item3 = new FloatingActionItem.Builder(2)
                .withResId(R.drawable.fb_icon)
                .withDelay(100)
                .withPadding(10)
                .build();

        mFloatingMenu = new FloatingActionMenu
                .Builder(getActivity())
                .addItem(item1)
                .addItem(item2)
                .addItem(item3)
                .withGravity(FloatingActionMenu.Gravity.CENTER_HORIZONTAL | FloatingActionMenu.Gravity.BOTTOM)
                .withDirection(FloatingActionMenu.Direction.Vertical)
                .animationDuration(300)
                .animationInterpolator(new OvershootInterpolator())
                .visible(true)
                .build();

        mFloatingMenu.hide(true);
        mFloatingMenu.setOnItemClickListener(new FloatingActionMenu.OnItemClickListener() {
            @Override
            public void onItemClick(FloatingActionMenu floatingActionMenu, int i) {
                Toast.makeText(getContext(),"clicked",Toast.LENGTH_SHORT).show();           }
        });



        return v;


    }

    private void fillFriendsArray() {

       /* gridArray.add(new Item(homeIcon,"Home"));
        gridArray.add(new Item(userIcon,"User"));
        gridArray.add(new Item(homeIcon,"House"));
        gridArray.add(new Item(userIcon,"Friend"));
        gridArray.add(new Item(homeIcon,"Home"));
        gridArray.add(new Item(userIcon,"Personal"));
        gridArray.add(new Item(homeIcon,"Home"));
        gridArray.add(new Item(userIcon,"User"));
        gridArray.add(new Item(homeIcon,"Building"));
        gridArray.add(new Item(userIcon,"User"));
        gridArray.add(new Item(homeIcon,"Home"));
        gridArray.add(new Item(userIcon,"xyz"));*/

        // fech from xmls
    }

    public static flatFriends newInstance(String text) {

        flatFriends f = new flatFriends();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}
