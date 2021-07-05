package com.example.despertador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    private ImageButton music_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri audioCollection = MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Atualizar();

        music_icon = findViewById(R.id.musicbutton);

        music_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this,TelaMusica.class);
                    startActivity(intent);
                }catch (Exception e){
                    System.out.println(e);
                }

            }
        });


    }

    private void Atualizar() {
        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                String horario = String.format("%02d:%02d:%02d",calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
                TextView helo = findViewById(R.id.hello);
                helo.setText(horario);

                long agora = SystemClock.uptimeMillis();
                long proximo = agora + (1000 - (agora % 1000));

                handler.postAtTime(runnable, proximo);

            }
        };
        runnable.run();
    }
}