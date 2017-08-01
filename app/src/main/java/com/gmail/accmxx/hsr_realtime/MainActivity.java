package com.gmail.accmxx.hsr_realtime;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView,mSearchResultsTextView;
    TextView errorMessageTextView;
    ProgressBar progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_http_search_results_json);
        errorMessageTextView = (TextView)findViewById(R.id.tv_error_message_display);
        progressBarView = (ProgressBar)findViewById(R.id.pb_loading_indicator);
    }

    private void makeHttpSearchQuery() {
        String httpQuery = mSearchBoxEditText.getText().toString();
        URL httpSearchUrl = NetworkTool.buildUrl(httpQuery);
        mUrlDisplayTextView.setText(httpSearchUrl.toString());
        new HttpQueryTask().execute(httpSearchUrl);
    }

    private void showJsonDataView(){
        errorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        errorMessageTextView.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
    }

    public class HttpQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String httpSearchResults = null;
            try {
                httpSearchResults = NetworkTool.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpSearchResults;
        }

        @Override
        protected void onPostExecute(String httpSearchResults) {
            progressBarView.setVisibility(View.INVISIBLE);
            if (httpSearchResults != null && !httpSearchResults.equals("")) {
                showJsonDataView();
                mSearchResultsTextView.setText(httpSearchResults);
            }
            else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeHttpSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
