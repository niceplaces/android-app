package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;

public class AreaIntroActivity extends AppCompatActivity {

    private Bundle mExtras;
    private final AreaIntroActivity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_intro);
        FirebaseAnalytics.getInstance(this);
        mExtras = getIntent().getExtras();
        getSupportActionBar().hide();
        TextView textViewAreaName = findViewById(R.id.explore_area_name);
        TextView textViewDescription = findViewById(R.id.textview_intro);
        Button button = findViewById(R.id.btn_find_places);
        if (mExtras.getString(Const.AREA_ID) != null){
            textViewAreaName.setText(mExtras.getString("AREA_NAME"));
            textViewDescription.setText(mExtras.getString("AREA_DESC"));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PlacesListActivity.class);
                    intent.putExtra(Const.AREA_ID, mExtras.getString(Const.AREA_ID));
                    intent.putExtra("AREA_NAME", mExtras.getString("AREA_NAME"));
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            textViewAreaName.setText(mExtras.getString("LIST_NAME"));
            textViewDescription.setText(mExtras.getString("LIST_DESC"));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PlacesListActivity.class);
                    intent.putExtra(Const.LIST_ID, mExtras.getString(Const.LIST_ID));
                    intent.putExtra("LIST_NAME", mExtras.getString("LIST_NAME"));
                    startActivity(intent);
                    finish();
                }
            });
            switch (mExtras.getString(Const.LIST_ID)){
                case "2":
                    LinearLayout badge;
                    badge = findViewById(R.id.badge_proloco);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Const.PROLOCO_URL));
                            startActivity(i);
                        }
                    };
                    badge.setOnClickListener(listener);
                    badge.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    badge = findViewById(R.id.badge_viasacra);
                    View.OnClickListener listener1 = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Const.VIASACRA_URL));
                            startActivity(i);
                        }
                    };
                    badge.setOnClickListener(listener1);
                    badge.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
