package com.androidprojects.pablo.healthscanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

/*
        ImageView devImg = (ImageView)findViewById(R.id.device_icon);
        devImg.setImageResource(R.drawable.fitbit);

        TextView devText = (TextView)findViewById(R.id.device_text);
        devText.setText("Fitbit");

        LinearLayout newDevice = (LinearLayout)findViewById(R.id.new_device_layout);
        LinearLayout devices= (LinearLayout)findViewById(R.id.connected_devices_layout);
        devices.addView(newDevice);
*/
//        devImg.setImageResource(R.drawable.balloon);
//        devText.setText("Balloon");
//        devices.addView(newDevice);

  /*      LinearLayout newDev = new LinearLayout(this);


        newDev.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,30);
        newDev.setLayoutParams(LLParams);

        LayoutInflater inflater;
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.device_template ,null);
        layout.getViewB

        ImageButton delBut = new ImageButton(this);
        ViewGroup.MarginLayoutParams IBParams = new ViewGroup.MarginLayoutParams(20,20);
        IBParams.topMargin=5;


        ImageButton<ImageButton
        android:layout_marginTop="5dp"
        android:layout_width="20dp"
        android:layout_height="20dp"
        android:layout_alignParentLeft="true"
        android:adjustViewBounds="true"
        android:padding="1dp"
        android:scaleType="fitXY"
        android:src="@drawable/delete_button" />
*/


  ImageButton options = (ImageButton) findViewById(R.id.ap_optionsButton);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getBaseContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup, popup.getMenu());
                popup.show();


                Toast.makeText(getBaseContext(), "Clicked",Toast.LENGTH_LONG ).show();
                //       /** Instantiating PopupMenu class */
//                PopupMenu popup = new PopupMenu(getBaseContext(), view);
//
                /** Adding menu items to the popumenu */
//                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });
    }

    public void onOptionsClick(View v)
    {
        /** Instantiating PopupMenu class */
        PopupMenu popup = new PopupMenu(getBaseContext(), v);

        /** Adding menu items to the popumenu */
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        /** Defining menu item click listener for the popup menu */
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
