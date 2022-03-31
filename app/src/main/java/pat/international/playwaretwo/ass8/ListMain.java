package pat.international.playwaretwo.ass8;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import pat.international.playwaretwo.R;


public class ListMain extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Games> games = new ArrayList<Games>();

        games.add(new Games("Normal mode"));
        games.add(new Games("Hard mode"));
        games.add(new Games("Normal time mode practice"));
        games.add(new Games("Hard time mode practice"));
        games.add(new Games("View challenges"));


       /* GamesAdapter gamesAdapter = new GamesAdapter(this, games);

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
        });*/
    }
}

