package com.w3epic.day9externalstorage;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText etFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileContent = findViewById(R.id.etFileContent);

        String content = getFIleContents("test_directory/File.txt");
        Log.e("debug", content);
    }

    public void btnSaveOnClickHandler(View view) {
        final EditText etDialogText = new EditText(this);
        etDialogText.setHint("Enter filename without extension");

        final String message = etFileContent.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save to a location");
        builder.setView(etDialogText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /* https://stackoverflow.com/questions/5453708/android-how-to-use-environment-getexternalstoragedirectory
                * Environment.getExternalStorageDirectory().getAbsolutePath()
                * Gives you the full path the SDCard.
                *
                * Environment.getExternalStorageDirectory()
                * Gives file hander with location of SDCard
                * */

                //File file = Environment.getExternalStorageDirectory();
                String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
                File dir = new File(fileLocation, "test_directory");
                Log.e("debug", dir.getAbsolutePath());

                if (!dir.exists()) {
                    dir.mkdir();
                } else {
                    Toast.makeText(MainActivity.this, "Directory already exists", Toast.LENGTH_SHORT).show();
                }

                File file =  new File(dir, etDialogText.getText().toString() + ".txt");

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.append(message);
                    fileWriter.flush();
                    fileWriter.close();
                    Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }

    String getFIleContents(String filename) {
        String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(fileLocation, filename);
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return sb.toString();
    }
}
