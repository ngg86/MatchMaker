package com.ngg86.matchmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.Objects;

public class settings extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            SharedPreferences sp = getSharedPreferences("settingsMatchMaker", MODE_PRIVATE);
            int CalcType = sp.getInt("CalcType", 0);

            Spinner spnrCalcType = findViewById(R.id.spCalcType);
            spnrCalcType.setSelection(CalcType);

                    spnrCalcType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                            {
                                SharedPreferences spref = getSharedPreferences("settingsMatchMaker", MODE_PRIVATE);
                                SharedPreferences.Editor sprefEd = spref.edit();
                                sprefEd.putInt("CalcType", position);
                                sprefEd.commit();
                            }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                            {

                            }
                    });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
}
