package com.example.gymapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    Context context;
    Spinner spinner;
    ListView listView;
    ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_schedule);

        context = this;

        listView = findViewById(R.id.list_items);

        listAdapter = new ArrayAdapter<>(this, R.layout.list_item_layout, new ArrayList<>());
        listView.setAdapter(listAdapter);

        listView.setAdapter(listAdapter);

        spinner =findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                String noSpaceString = selectedOption.replace(" ", "");
                String extractedSubstring = noSpaceString.substring(0, 4);
                // Do something with the selected option

                listAdapter.clear();
                if (extractedSubstring.equals("Day1")) {
                    listAdapter.add("1. Bench Press");
                    listAdapter.add("2. Chest Press");
                    listAdapter.add("3. Chest Fly");
                    listAdapter.add("4. CrossOvers");
                    listAdapter.add("5. Bicep Curl");
                    listAdapter.add("6. Hammer Curl");
                    listAdapter.add("7. Reverse Curl");
                    listAdapter.add("8. Triceps Extension");
                    listAdapter.add("9. Skull Crushers");
                    listAdapter.add("10. Shoulder Press");
                } else if (extractedSubstring.equals("Day2")) {
                    listAdapter.add("1. Wide Lats");
                    listAdapter.add("2. Close Lats");
                    listAdapter.add("3. One Arm Lats");
                    listAdapter.add("4. Row");
                    listAdapter.add("5. Shrugs");
                } else if (extractedSubstring.equals("Day3")) {
                    listAdapter.add("1. Leg Press");
                    listAdapter.add("2. Squat");
                    listAdapter.add("3. RDL");
                    listAdapter.add("4. Leg Extension");
                    listAdapter.add("5. Leg Flexion");
                    listAdapter.add("6. Walk - (10min)");
                    listAdapter.add("7. Stairs - (3min)");
                } else if (extractedSubstring.equals("Day4")) {
                    listAdapter.add("1. Chest Press");
                    listAdapter.add("2. Chest Fly");
                    listAdapter.add("3. Bicep Killer (a Tony)");
                    listAdapter.add("4. Tricep");
                    listAdapter.add("5. Shoulder 3 Muscles");
                } else if (extractedSubstring.equals("Day5")) {
                    listAdapter.add("1. Row");
                    listAdapter.add("2. Wide Lats");
                    listAdapter.add("3. Rear Delts");
                    listAdapter.add("4. Front & Lat Raises");
                    listAdapter.add("5. Shrugs");
                    listAdapter.add("6. Close Lats");
                    listAdapter.add("7. One Arm Lats");
                    listAdapter.add("8. T Bar Row");
                } else if (extractedSubstring.equals("Day6")) {
                    listAdapter.add("1. Leg Press");
                    listAdapter.add("2. RDL");
                    listAdapter.add("3. Leg Extension");
                    listAdapter.add("4. StairMaster (3min)");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });
    }
}