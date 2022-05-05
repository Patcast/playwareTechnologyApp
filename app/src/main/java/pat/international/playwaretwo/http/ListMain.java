package pat.international.playwaretwo.http;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import pat.international.playwaretwo.R;
import pat.international.playwaretwo.http.GameSessionManager;
import pat.international.playwaretwo.http.GameSessionPostRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListMain extends Fragment {

    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView list;
    Button getSessionsBtn, postSessionBtn;
    GameSessionManager manager;
    int inc1 = 0;
    SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPref = getContext().getSharedPreferences("PLAYWARE_COURSE", Context.MODE_PRIVATE);

        return inflater.inflate(R.layout.fragment_list_main, container, false);



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSessionsBtn = view.findViewById(R.id.getSessionsBtn);
        postSessionBtn = view.findViewById(R.id.postSessionBtn);


        list = view.findViewById(R.id.sessionlist);
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, items);
        manager = new GameSessionManager();


        getSessionsBtn.setOnClickListener(v -> {
                    items.clear();
                    ArrayList<String> toAdd = manager.getGameSessions(7);
                    if (toAdd != null) {
                        items.addAll(toAdd);
                        adapter.notifyDataSetChanged();

                    }
                });


        postSessionBtn.setOnClickListener(v -> {
            GameSessionPostRequest req = new GameSessionPostRequest("7", "1", String.valueOf(inc1++),
                    "10", "100", "4");

            if (manager.postGameSession(req)) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Posted!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Failed to post!")
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });
    }




}
