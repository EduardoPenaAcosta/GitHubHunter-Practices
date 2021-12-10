package com.example.githubhunter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubhunter.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchBox;
    TextView urlDisplay;
    TextView searchResults;

    public class GitHubQueryTask extends AsyncTask<URL,Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL searchURL = urls[0];
            String gitHubSearchResults = null;

            try {
                gitHubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchURL);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return gitHubSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if( s != null && !s.equals("")){
                searchResults.setText(s);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.search) {
            Log.i("MainActivity", "El usuario a pulsado search");
            Context context = MainActivity.this;
            Toast.makeText(context, R.string.search_pressed, Toast.LENGTH_LONG).show();

            URL githubUrl = NetworkUtils.buildURL(searchBox.getText().toString());
            urlDisplay.setText(githubUrl.toString());

            new GitHubQueryTask().execute(githubUrl);

            /*Esto no lo podemos hacer en el hilo principal porque si revienta*/
            /*try {
                String response = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                Log.i("MainActivity", response);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = (EditText) findViewById(R.id.search_box);
        urlDisplay = (TextView) findViewById(R.id.url_display);
        searchResults = (TextView) findViewById(R.id.github_search_result);
    }
}