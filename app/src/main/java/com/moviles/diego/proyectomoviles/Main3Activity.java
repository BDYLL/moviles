package com.moviles.diego.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main3Activity extends AppCompatActivity {

    private EditText text;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        this.text=(EditText)this.findViewById(R.id.nameText);
        this.send=(Button)this.findViewById(R.id.send);

        this.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack(v);
            }
        });
    }

    public void goBack(View v){
        Intent i = new Intent();
        i.putExtra("name",this.text.getText().toString());
        this.setResult(Activity.RESULT_OK,i);
        this.finish();
    }
}
