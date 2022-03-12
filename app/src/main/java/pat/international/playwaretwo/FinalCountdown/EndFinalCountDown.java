package pat.international.playwaretwo.FinalCountdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pat.international.playwaretwo.R;


public class EndFinalCountDown extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_final_countdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int amount = EndFinalCountDownArgs.fromBundle(getArguments()).getGameCount();
        String timeResult = EndFinalCountDownArgs.fromBundle(getArguments()).getTimeResult();
        TextView scoreText = view.findViewById(R.id.textScore);
        TextView timeText = view.findViewById(R.id.textTime);
        scoreText.setText(String.valueOf(amount));
        timeText.setText(timeResult);
    }

}