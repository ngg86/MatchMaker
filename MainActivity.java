package com.ngg86.matchmaker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.health.TimerStat;
import android.provider.Settings;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity
{
    public String Vowels = "AEIOU";
    public String Consonants = "BCDFGHJKLMNPQRSTVWXYZ";
    public String Love = "LOVE";
    public final int max = 10000;
    public final int min = 2500;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText("");
        tvInstructions.setText("Typ twee voornamen in in de textbox dan klik op 'Berekenen' om te zien of ze een goed match zijn");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
        final int randomInt = new Random().nextInt((max - min) + 1) + min;
        SharedPreferences sp = getSharedPreferences("settingsMatchMaker", MODE_PRIVATE);
        final int calcType = sp.getInt("CalcType", 0);

        //Retrieve input values
        EditText NameOne = findViewById(R.id.etNameOne);
        EditText NameTwo = findViewById(R.id.etNameTwo);
        final TextView Results = findViewById(R.id.tvResult);
        final String FirstName = NameOne.getText().toString();
        final String SecondName = NameTwo.getText().toString();

        //Start animating gif
        StartAnimation();
        //ImageView anim = findViewById(R.id.imgAnim);
        //anim.setVisibility(View.VISIBLE);

        //Show loading gif for random amount of time between 2.5 - 10 sec
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                if (calcType == 0)
                {
                    BerekeningA(FirstName, SecondName, Results);

                }
                else
                {
                    BerekeningB(FirstName, SecondName, Results);
                }
            }
        }, randomInt);
//        anim = findViewById(R.id.imgAnim);
//        anim.setVisibility(View.INVISIBLE);
    }

    private void StartAnimation()
    {
        WebView animation = findViewById(R.id.animateGif);
        animation.loadUrl("file:///android_res/drawable/heart_anim_small.gif");
        //animation.loadDataWithBaseURL("file:///android_res/drawable/heart_anim_small.gif","<html><body  align='center'></body></html>",null, "text/html", null);
        animation.setVisibility(View.VISIBLE);
        animation.setBackgroundColor(Color.TRANSPARENT);
       // animation.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

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
        WebView animation = findViewById(R.id.animateGif);
        animation.setVisibility(View.INVISIBLE);
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

        WebView animation = findViewById(R.id.animateGif);
        animation.setVisibility(View.INVISIBLE);
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
