package pat.international.playwaretwo.PianoTiles.GameCode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pat.international.playwaretwo.R;

public class GameoverFragment extends Fragment implements GameoverFragmentPresenter.IGameoverFragment, View.OnClickListener {
    TextView score;
    Button  saveButton;
    EditText playerName;
    GameoverFragmentPresenter gameoverFragmentPresenter;
    DBHandler db;
    CustomToast toast;

    public GameoverFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameover, container, false);
        View toastView = inflater.inflate(R.layout.custom_toast, container, false);
        this.toast = new CustomToast(toastView, getActivity().getApplicationContext());
        this.score = view.findViewById(R.id.score);
        this.playerName = view.findViewById(R.id.player_name);
        this.saveButton = view.findViewById(R.id.save_name);

        this.db = new DBHandler(this.getActivity());
        this.gameoverFragmentPresenter = new GameoverFragmentPresenter(GameoverFragmentArgs.fromBundle(getArguments()).getScore(), this, this.db, this.toast);

        this.saveButton.setOnClickListener(this);

        this.gameoverFragmentPresenter.loadData();

        return view;
    }



    public static GameoverFragment newInstance(int score){
        GameoverFragment fragment = new GameoverFragment();
        Bundle args = new Bundle();
        args.putInt("score", score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setScore(int score) {
        this.score.setText("Your Score: " + score);
    }

    @Override
    public void changePage(int page) {
        //this.fragmentListener.changePage(page);
    }

    @Override
    public void onClick(View v) {
        String playerName = this.playerName.getText().toString().equals("") ? "Player" : this.playerName.getText().toString();
        if(v == this.saveButton){
            this.gameoverFragmentPresenter.saveScore(playerName);
            this.saveButton.setEnabled(false);
        }
    }
}
