package pat.international.playwaretwo.PianoTiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pat.international.playwaretwo.HomeActivity;
import pat.international.playwaretwo.R;


public class PianoTilesScreen extends Fragment {

    HomeActivity act;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        act = (HomeActivity) getActivity();
        return inflater.inflate(R.layout.fragment_piano_tiles_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}