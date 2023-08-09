package com.example.homework1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ...
    Button btncreate;
    ListView listview;



    @Override
    protected void onResume() {
        super.onResume();
        // When the activity is resumed, fetch data and update ListView
        new Thread(fetchDataRunnable).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewElement();
        new Thread(fetchDataRunnable).start();

        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent it = new Intent(MainActivity.this, CreatingRecord.class);
                    startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                String[] parts = selectedItem.split(", BMR: ");
                String[] part2 = parts[0].split(", Name: ");
                String ID = part2[0].replace("ID: ", "");


                Intent intent = new Intent(MainActivity.this, ModifyingRecord.class);

                // Put the selected item as an extra in the Intent
                intent.putExtra("id", ID);

                //Log.d(name, "test");
                //Log.d(bmr, "test");
                // Start the new activity
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                String[] parts = selectedItem.split(", BMR: ");
                String[] part2 = parts[0].split(", Name: ");
                String ID = part2[0].replace("ID: ", "");

                // Call a method to delete the record
                showConfirmationDialog(ID);

                return true; // Return true to consume the long click event
            }
        });
    }



    private Runnable fetchDataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://140.115.200.131/myapp/read_record.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line);
                    }

                    List<String> dataList = parseJSONData(result.toString());

                    Log.d("Connection", "Connected to XAMPP server and got response.");

                    inputStream.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
                            listview.setAdapter(adapter);
                        }
                    });
                }
                else{
                    Log.d("Connection", "Failed to connect to XAMPP server. Response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private List<String> parseJSONData(String jsonData) {
        List<String> dataList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int ID = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String bmr = jsonObject.getString("bmr");
                String dataItem = "ID: " + ID + ", Name: " + name + ", BMR: " + bmr;
                dataList.add(dataItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void deleteRecord(String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://140.115.200.131/myapp/delete_record.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.connect();

                    // Prepare the data to be sent
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

                    //Log.d(id, "test");
                    //Log.d(data, "test");


                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Record deleted successfully
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI if needed
                            }
                        });
                        // Fetch updated data from the server
                        new Thread(fetchDataRunnable).start();
                    } else {
                        // Error deleting record
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Handle error if needed
                            }
                        });
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showConfirmationDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Record");
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call the deleteRecord method when user confirms deletion
                deleteRecord(id);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // ...
    private void initViewElement(){
        btncreate = (Button) findViewById(R.id.btncreate);
        listview = (ListView) findViewById(R.id.listview);
    }
}

