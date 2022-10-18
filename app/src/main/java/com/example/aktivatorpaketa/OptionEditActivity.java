package com.example.aktivatorpaketa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OptionEditActivity extends AppCompatActivity {
    public static final String OPTION_OBJECT = "OPTION_OBJECT";
    public static final String SAVE_OPTION_OBJECT = "SAVE_OPTION_OBJECT";
    Option option;

    private ArrayAdapter<String> messagesArrayAdapter;

    EditText titleEditText;
    EditText descriptionEditText;
    EditText numberEditText;
    ListView messagesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_edit);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        messagesListView = (ListView) findViewById(R.id.messageListView);

        Intent intent = getIntent();
        option = (Option) intent.getSerializableExtra(OptionEditActivity.OPTION_OBJECT);

        if (option == null) {
            option = new Option(-1l, "", "", "", new ArrayList<String>());
        }

        titleEditText.setText(option.getTitle());
        descriptionEditText.setText(option.getDescription());
        numberEditText.setText(option.getNumber());

        messagesArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        messagesArrayAdapter.addAll(option.getMessageList()); // Copy array so data dosn't get overwriten if not saved
        messagesListView.setAdapter(messagesArrayAdapter);

        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, final long id) {
                final String msg = (String) adapter.getItemAtPosition(position);

                AlertDialog.Builder alert = new AlertDialog.Builder(OptionEditActivity.this);
                final EditText msgEditText = new EditText(OptionEditActivity.this);
                msgEditText.setText(msg);
                alert.setMessage("Promjenite poruku:");
                alert.setTitle("Izmjena poruke");

                alert.setView(msgEditText);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String enteredMessage = msgEditText.getText().toString();
                        messagesArrayAdapter.remove(msg);
                        messagesArrayAdapter.insert(enteredMessage, (int) id);
                    }
                });
                alert.setNegativeButton("Obrisi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(OptionEditActivity.this)
                                .setTitle("Obrisati?")
                                .setMessage("Jeste li sigurni da zelite obrisati poruku: \"" + msg + "\"?")
                                .setPositiveButton("Da", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        messagesArrayAdapter.remove(msg);
                                    }
                                })
                                .setNegativeButton("Ne", null).show();
                    }
                });
                alert.setNeutralButton("Odustani", null);

                alert.show();
            }
        });
    }

    public void addMessage_onClick(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(OptionEditActivity.this);
        final EditText msgEditText = new EditText(OptionEditActivity.this);
        alert.setMessage("Unesite poruku:");
        alert.setTitle("Nova poruka");

        alert.setView(msgEditText);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String enteredMessage = msgEditText.getText().toString();
                messagesArrayAdapter.add(enteredMessage);
            }
        });
        alert.setNeutralButton("Odustani", null);

        alert.show();
    }

    public void save_onClick(View v) {
        String number = numberEditText.getText().toString();
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        List<String> messageList = new ArrayList<>();
        for (int i = 0; i < messagesArrayAdapter.getCount(); i++) {
            messageList.add(messagesArrayAdapter.getItem(i));
        }

        Long id = 0L;

        if (!Options.getOptionList().isEmpty()) {
            id = Options.getOptionList().get(Options.getOptionList().size() - 1).getId() + 1;
        }
        if (this.option.getId() != -1) {
            id = this.option.getId();
        }

        Option option = new Option(id, title, description, number, messageList);

        Intent saveIntent = new Intent();
        saveIntent.putExtra(OptionEditActivity.SAVE_OPTION_OBJECT, option);
        setResult(RESULT_OK, saveIntent);
        finish();
    }
}
