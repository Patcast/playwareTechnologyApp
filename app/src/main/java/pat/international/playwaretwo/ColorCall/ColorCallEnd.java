package pat.international.playwaretwo.ColorCall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pat.international.playwaretwo.ChaseTheLight.EndChaseArgs;
import pat.international.playwaretwo.R;


public class ColorCallEnd extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_chase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int amount = EndChaseArgs.fromBundle(getArguments()).getGameCount();
        TextView scoreText = view.findViewById(R.id.textScore);
        scoreText.setText(String.valueOf(amount));
    }
}
