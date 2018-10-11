package com.ngg86.matchmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public String Vowels = "AEIOU";
    public String Consonants = "BCDFGHJKLMNPQRSTVWXYZ";
    public String Love = "LOVE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //LoadSettings();

            TextView tvInstructions = findViewById(R.id.tvInstructions);
            TextView tvResult = findViewById(R.id.tvResult);
            tvResult.setText("");
            tvInstructions.setText("Typ twee voornamen in in de textbox dan klik op 'Berekenen' om te zien of ze een goed match zijn");
        }

//    private void LoadSettings()
//        {
//            SharedPreferences sPref = getSharedPreferences("settings", MODE_PRIVATE);
//            if(sPref != null)
//                {
//                    Intent intent = new Intent(this, settings.class).putExtra("sharedPrefs", sPref);
//                    Spinner sp = findViewById(R.id.spCalcType);
//                    sp.setSelection(sPref.getInt("CalcType", 0));
//                }
//        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
//            SharedPreferences sPref = getSharedPreferences("settingsMatchMaker", MODE_PRIVATE);
//            if(sPref.getInt("CalcType", 0)
//                {
//                    Intent intent = new Intent(this, settings.class)
//                                    .putExtra("sharedPrefs", sPref.getInt("CalcType", 0));
//                    startActivity(intent);
//                }
            switch (item.getItemId())
                {
                    case R.id.action_settings:
                        Intent intent = new Intent(this, settings.class);
                        startActivity(intent);
                        return true;

                    default:
                        return super.onOptionsItemSelected(item);
                }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }

    public void onClickCalculateMatch(View view)
        {
            SharedPreferences sp = getSharedPreferences("settingsMatchMaker", MODE_PRIVATE);
            int calcType = sp.getInt("CalcType", 0);

            //Retrieve input values
            EditText NameOne = findViewById(R.id.etNameOne);
            EditText NameTwo = findViewById(R.id.etNameTwo);
            TextView Results = findViewById(R.id.tvResult);
            String FirstName = NameOne.getText().toString();
            String SecondName = NameTwo.getText().toString();

            if (calcType == 0)
                {
                    BerekeningA(FirstName, SecondName, Results);
                }
            else
                {
                    BerekeningB(FirstName, SecondName, Results);
                }

        }

    public void BerekeningA(String NameOne, String NameTwo, TextView Results)
        {
            //calculate scores of Strings(ASCII values)
            int FirstNameValue = CalculateNameValue(NameOne);
            int SecondNameValue = CalculateNameValue(NameTwo);

            //calculate result of score values as a percentage
            int Percentage = CalculatePercentage(FirstNameValue, SecondNameValue);

            //convert Percentage to string and set it to TextView
            String Result = String.valueOf(Percentage) + "%";
            Results.setText(Result);


        }

    public void BerekeningB(String NameOne, String NameTwo, TextView Results)
        {
            char charNameOne = NameOne.charAt(0);
            char charNameTwo = NameTwo.charAt(0);
            int vowelCounterOne = CountVowels(NameOne);
            int vowelCounterTwo = CountVowels(NameTwo);
            int consonantCounterOne = CountConsonants(NameOne);
            int consonantCounterTwo = CountConsonants(NameTwo);
            int loveCounterOne = CountLove(NameOne) * 5;
            int loveCounterTwo = CountLove(NameTwo) * 5;
            int points = 0;


            if (NameOne.length() == NameTwo.length())
                {
                    points += 20;
                }
            if ((IsVowel(charNameOne) && IsVowel(charNameTwo)))
                {
                    points += 10;
                }
            if ((IsConsonant(charNameOne) && IsConsonant(charNameTwo)))
                {
                    points += 10;
                }
            if (vowelCounterOne == vowelCounterTwo)
                {
                    points += 20;
                }
            if (consonantCounterOne == consonantCounterTwo)
                {
                    points += 20;
                }

            points += loveCounterOne + loveCounterTwo;
            String totalPoints = String.valueOf(points);
            Results.setText("Total points: " + totalPoints);
        }

    private int CountLove(String Name)
        {
            int love = 0;
            for (int i = 0; i < Name.length(); i++)
                {
                    char c = Name.charAt(i);
                    if (IsLove(c))
                        {

                            love++;
                        }
                }
            return love;
        }

    private int CountConsonants(String Name)
        {
            int consonants = 0;
            for (int i = 0; i < Name.length(); i++)
                {
                    char c = Name.charAt(i);
                    if (IsConsonant(c))
                        {
                            consonants++;
                        }
                }
            return consonants;
        }

    private int CountVowels(String Name)
        {
            int vowels = 0;
            for (int i = 0; i < Name.length(); i++)
                {
                    char c = Name.charAt(i);
                    if (IsVowel(c))
                        {
                            vowels++;
                        }
                }
            return vowels;
        }

    private boolean IsVowel(char c)
        {
            return Vowels.indexOf(c) != -1;
        }

    private boolean IsConsonant(char c)
        {
            return Consonants.indexOf(c) != -1;
        }

    private boolean IsLove(char c)
        {
            return Love.indexOf(c) != -1;
        }

    private int CalculatePercentage(int firstNameValue, int secondNameValue)
        {
            int value = Math.abs(firstNameValue - secondNameValue);

            int percentage = 100 - value;
            return percentage;
        }

    private int CalculateNameValue(String firstName)
        {
            int value = 0;
            for (int i = 0; i < firstName.length(); i++)
                {
                    char c = firstName.charAt(i);
                    int charValue = (int) c;
                    value += charValue;
                }
            return value / 10;
        }

}
