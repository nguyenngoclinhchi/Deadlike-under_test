package androidexample.com.deadlike;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import androidexample.com.deadlike.internet.InternetActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openInputScreen(View view){
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }

    public  void openOutputScreen(View view){
        Intent intent = new Intent(this, OutputActivity.class);
        startActivity(intent);
    }

    public void openInternetScreen(View v){
        Intent intent = new Intent(this, InternetActivity.class);
        startActivity(intent);


    }



}
