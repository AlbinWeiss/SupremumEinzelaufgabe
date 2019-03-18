package com.example.supremumeinzelaufgabe;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // send matriculation number to server
        Button btn_send = findViewById(R.id.ID_btn_send);
        // search primes in the matriculation number
        Button btn_primes = findViewById(R.id.ID_btn_primes);
        final EditText input_matriculationNumber = findViewById(R.id.ID_input_matriculationNumber);
        final TextView output_response = findViewById(R.id.ID_ouput_response);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // Log.i("TAG", "Matrikelnummer: " + input_matriculationNumber.getText().toString());

                String request = input_matriculationNumber.getText().toString();
                SocketClientThread socketClientThread = new SocketClientThread(request);
                Thread thread = new Thread(socketClientThread, "socketClientThread1");
                thread.start();

                String response = "";
                try {
                    thread.join();
                    response = socketClientThread.getResponse();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Log.i("TAG", "response" + response);
                output_response.setText(response);
            }
        });

        btn_primes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String request = input_matriculationNumber.getText().toString();

                String primesOutput = "";
                // check matriculation number
                if (request.length() == 8) {
                    String primes = searchPrimes(request);
                    primesOutput = "Alle Primzahlziffern Ihrer Matrikelnummer: " + primes;
                }

                // Log.i("TAG", "primes" + primesOutput);
                output_response.setText(primesOutput);
            }
        });
    }

    /**
     * Searches for all prime numbers in the matriculation number
     *
     * @param matriculationNumber
     * @return
     */
    private String searchPrimes(String matriculationNumber) {
        char[] charArray = matriculationNumber.toCharArray();
        String primes = "";
        for (char digit : charArray) {
            if (digit == '2' || digit == '3' || digit == '5' || digit == '7') {
                primes = primes + digit;
            }
        }
        return primes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
