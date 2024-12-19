package com.example.studentenrollapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentMenuActivity extends AppCompatActivity {

    CheckBox checkBoxSubject1, checkBoxSubject2, checkBoxSubject3;
    Button buttonSubmit;
    int totalCredits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Memastikan file layout activity_enrollment_menu.xml ada dan benar
        setContentView(R.layout.activity_enrollment_menu);

        checkBoxSubject1 = findViewById(R.id.checkBoxSubject1);
        checkBoxSubject2 = findViewById(R.id.checkBoxSubject2);
        checkBoxSubject3 = findViewById(R.id.checkBoxSubject3);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCredits = 0;

                if (checkBoxSubject1.isChecked()) totalCredits += 3;
                if (checkBoxSubject2.isChecked()) totalCredits += 4;
                if (checkBoxSubject3.isChecked()) totalCredits += 3;

                if (totalCredits > 24) {
                    Toast.makeText(EnrollmentMenuActivity.this, "Credit limit exceeded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EnrollmentMenuActivity.this, "Your enrollment has been submitted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EnrollmentMenuActivity.this, EnrollmentSummaryActivity.class);
                    intent.putExtra("totalCredits", totalCredits);
                    startActivity(intent);
                }
            }
        });
    }
}
