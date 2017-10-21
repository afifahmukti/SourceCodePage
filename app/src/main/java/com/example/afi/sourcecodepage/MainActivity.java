package com.example.afi.sourcecodepage;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    EditText myText;
    String Url;
    Spinner spin;
    ProgressBar progress;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myText = (EditText) findViewById(R.id.edit);
        progress = (ProgressBar) findViewById(R.id.progress);
        text = (TextView) findViewById(R.id.txtHasil);
        spin = (Spinner)findViewById(R.id.spin);
        ArrayAdapter<CharSequence> array = ArrayAdapter.createFromResource(this,R.array.list,android.R.layout.simple_spinner_item);
        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(array);
        progress.setVisibility(View.GONE);
    }

    public void doSomething(View view){
        Url = spin.getSelectedItem()+myText.getText().toString();
        boolean valid = Patterns.WEB_URL.matcher(Url).matches();

        if(valid){
            getSupportLoaderManager().restartLoader(0,null,this);
            progress.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
        }else{
            Loader loader = getSupportLoaderManager().getLoader(0);
            if(loader != null){
                loader.cancelLoad();
            }
            text.setText("URL TIDAK VALID");
            progress.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new ConnectInternet(this,Url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        text.setText(data);
        progress.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
