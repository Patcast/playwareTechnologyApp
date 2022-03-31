package pat.international.playwaretwo.ass8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
/*

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<com.example.list.Games> games = new ArrayList<com.example.list.Games>();

        games.add(new Games("Normal mode"));
        games.add(new com.example.list.Games("Hard mode"));
        games.add(new com.example.list.Games("Normal time mode practice"));
        games.add(new com.example.list.Games("Hard time mode practice"));
        games.add(new com.example.list.Games("View challenges"));


        com.example.list.GamesAdapter gamesAdapter = new com.example.list.GamesAdapter(this, games);

        ListView listView = (ListView) findViewById(R.id.listview_games);
        listView.setAdapter(gamesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 4:
                        Intent viewchallenges = new Intent(MainActivity.this,ViewChallenges.class);
                        startActivity(viewchallenges);
                        break;
                    case 0:
                        Intent test = new Intent(MainActivity.this, com.example.list.Test.class);
                        startActivity(test);
                        break;
                }
            }
        });

    }

}*/
