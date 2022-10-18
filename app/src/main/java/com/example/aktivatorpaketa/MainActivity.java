package com.example.aktivatorpaketa;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aktivatorpaketa.sms.SmsHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int OPTION_CREATE_REQUEST_CODE = 1;
    private static final int OPTION_EDIT_REQUEST_CODE = 2;

    public static MainActivity mainActivity;

    private OptionListViewAdapter optionListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Options.load(getFilesDir().getPath());

        ListView listView = (ListView) findViewById(R.id.optionsListView);
        optionListViewAdapter = new OptionListViewAdapter(this, R.layout.option_row, Options.getOptionList());
        listView.setAdapter(optionListViewAdapter);
        listView.setScrollContainer(false);

        mainActivity = this;
    }

    public void activateOption(final Option option) {
        if (!isSmsPermissionGranted()) {
            requestReadAndSendSmsPermission();
        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Aktivirati?")
                .setMessage("Jeste li sigurni da zelite aktivirati opciju: \"" + option.getTitle() + "\"?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.i("", "Aktivacija opcije: " + option.getTitle());
                        SmsHelper.send(option);
                    }
                })
                .setNegativeButton("Ne", null).show();
    }

    public void createOption() {
        Intent intent = new Intent(this.getBaseContext(), OptionEditActivity.class);
        startActivityForResult(intent, OPTION_CREATE_REQUEST_CODE);
    }

    public void editOption(Option option) {
        Intent intent = new Intent(this.getBaseContext(), OptionEditActivity.class);
        intent.putExtra(OptionEditActivity.OPTION_OBJECT, option);
        startActivityForResult(intent, OPTION_EDIT_REQUEST_CODE);
    }

    public void deleteOption(final Option option) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Obrisati?")
                .setMessage("Jeste li sigurni da zelite obrisati opciju: \"" + option.getTitle() + "\"?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Options.getOptionList().remove(option);
                        optionListViewAdapter.notifyDataSetChanged();
                        Options.save(getFilesDir().getPath());
                    }
                })
                .setNegativeButton("Ne", null).show();
    }

    public void addOption_onClick(View v) {
        createOption();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        Option option = (Option) data.getSerializableExtra(OptionEditActivity.SAVE_OPTION_OBJECT);

        switch (requestCode) {
            case OPTION_CREATE_REQUEST_CODE:
                Options.getOptionList().add(option);
                break;
            case OPTION_EDIT_REQUEST_CODE:
                int index = -1;
                for (Option o : Options.getOptionList()) {
                    if (o.getId().equals(option.getId())) {
                        index = Options.getOptionList().indexOf(o);
                    }
                }

                if (index != -1) {
                    Options.getOptionList().set(index, option);
                } else {
                    Options.getOptionList().add(option);
                }
                break;
        }

        optionListViewAdapter.notifyDataSetChanged();
        Options.save(getFilesDir().getPath());
    }

    public void receiveSms(String number, String message) {
        if (number.equals("T-HT") | number.equals("13636")) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(number)
                    .setMessage(message)
                    .setNeutralButton("OK", null)
                    .show();
        }
    }


    /**
     * Check if we have SMS permission
     */
    public boolean isSmsPermissionGranted() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, 111);
    }
}
