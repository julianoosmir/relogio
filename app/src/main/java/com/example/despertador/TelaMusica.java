package com.example.despertador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class TelaMusica extends AppCompatActivity {


    private ArrayList<Songinfo> listSong = new ArrayList<Songinfo>();
    private MediaPlayer mp = new MediaPlayer();
    private ListView listView;
    private Songinfo music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_musica);

        int b = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
        }
        else {
            loadSong();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            loadSong();
        }
    }

    private void loadSong() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor rs = getContentResolver().query(uri,null,selection,null,null);
        if(rs != null){
            while (rs.moveToNext()){
                String url = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DATA));
                String author = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String title = rs.getString(rs.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                if (url != null && author != null && title != null){
                   try{
                       music = new Songinfo(title,author,url);
                       listSong.add(music);
                   }catch (Exception e){
                       System.out.println(e);
                   }
                }


            }
        }
      //ArrayAdapter<Songinfo> adapter = new  ArrayAdapter<Songinfo>(this, android.R.layout.simple_list_item_1,listSong);
        MyAdapter myAdapter = new MyAdapter(this,listSong);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);
    }

    private class MyAdapter extends BaseAdapter {

        private ArrayList<Songinfo> mysongs;
        private Context context;
        public MyAdapter(Context context,ArrayList<Songinfo> mysongs) {
            this.context = context;
            this.mysongs = mysongs;
        }
        @Override
        public int getCount() {
            return mysongs.size();
        }

        @Override
        public Object getItem(int position) {
            return mysongs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.mylayout,null);

            Songinfo song = mysongs.get(position);
            TextView titulo = view.findViewById(R.id.titulo);
            TextView cantor = view.findViewById(R.id.cantor);
            Button button = view.findViewById(R.id.playerButton);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button.getText() == "STOP"){
                        mp.stop();
                        button.setText("PLAY");
                    }else {
                        try {
                            mp = new MediaPlayer();
                            mp.setDataSource(song.getSongURL());
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        button.setText("STOP");

                    }

                }
            });
            titulo.setText(song.getTitle());
            cantor.setText(song.getAuthor());
            return view;
        }
    }

    private class Songinfo {
        private String title;
        private String author;
        private String SongURL;

        public Songinfo(String title, String author, String songURL) {
            this.title = title;
            this.author = author;
            this.SongURL = songURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getSongURL() {
            return SongURL;
        }

        public void setSongURL(String songURL) {
            SongURL = songURL;
        }


    }
}

