package com.travismadill.passgen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.StringBuilderPrinter;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public String allCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String allLower = "abcdefghijklmnopqrstuvwxyz";
    public String numbers = "0123456789";
    public String specialChars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void copy_onclick(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.copied_password), ((EditText)findViewById(R.id.box_password)).getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    public void generate_onclick(View view) {
        EditText lengthBox = (EditText)findViewById(R.id.txt_passlength);
        EditText passBox = (EditText)findViewById(R.id.box_password);
        int len = Integer.parseInt(lengthBox.getText().toString());
        if(len > 3 && len < 100){
            specialChars = ((EditText)findViewById(R.id.txt_specialChars)).getText().toString();

            StringBuilder sb = new StringBuilder();
            Random r = new Random();

            boolean hasCaps = ((CheckBox)findViewById(R.id.chk_caps)).isChecked();
            boolean hasLowers = ((CheckBox)findViewById(R.id.chk_lower)).isChecked();
            boolean hasNums = ((CheckBox)findViewById(R.id.chk_nums)).isChecked();
            boolean hasSpecials = ((CheckBox)findViewById(R.id.chk_specials)).isChecked();

            if(hasSpecials && specialChars.isEmpty())
                hasSpecials = false;

            if(!hasCaps && !hasLowers && !hasNums && !hasSpecials) {
                Toast.makeText(getApplicationContext(), R.string.no_chars_selected, Toast.LENGTH_LONG).show();
                return;
            }

            List<Integer> options = new ArrayList<Integer>();
            if(hasCaps) options.add(0);
            if(hasLowers) options.add(1);
            if(hasNums) options.add(2);
            if(hasSpecials) options.add(3);

            while(sb.length() <= len){
                int next;
                if(options.size() > 1)
                    next = options.get(r.nextInt(options.size()));
                else next = options.get(0);

                switch(next){
                    case 0:
                        sb.append(allCaps.charAt(r.nextInt(allCaps.length())));
                        break;
                    case 1:
                        sb.append(allLower.charAt(r.nextInt(allLower.length())));
                        break;
                    case 2:
                        sb.append(numbers.charAt(r.nextInt(numbers.length())));
                        break;
                    case 3:
                        sb.append(specialChars.charAt(r.nextInt(specialChars.length())));
                        break;
                }
            }

            passBox.setText(sb.toString());
        } else {
            Toast.makeText(getApplicationContext(), R.string.pass_length_invalid, Toast.LENGTH_LONG).show();
        }
    }

    public void chk_specials_onClick(View view) {
        CheckBox c = (CheckBox)view;
        if(c.isChecked()){
            ((EditText)findViewById(R.id.txt_specialChars)).setEnabled(true);
        } else {
            ((EditText)findViewById(R.id.txt_specialChars)).setEnabled(false);
        }
    }
}
