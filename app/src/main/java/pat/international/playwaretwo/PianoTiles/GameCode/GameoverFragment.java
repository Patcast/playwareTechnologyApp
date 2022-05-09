package pat.international.playwaretwo.PianoTiles.GameCode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pat.international.playwaretwo.R;

public class GameoverFragment extends Fragment implements GameoverFragmentPresenter.IGameoverFragment, View.OnClickListener {
    TextView scoreTextView;
    Button  saveButton;
    EditText playerName;
    GameoverFragmentPresenter gameoverFragmentPresenter;
    DBHandler db;
    CustomToast toast;
    int score;
    boolean saved;

    public GameoverFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gameover, container, false);
        View toastView = inflater.inflate(R.layout.custom_toast, container, false);

        this.toast = new CustomToast(toastView, getActivity().getApplicationContext());
        this.scoreTextView = view.findViewById(R.id.score);
        this.playerName = view.findViewById(R.id.player_name);
        this.saveButton = view.findViewById(R.id.save_name);

        this.db = new DBHandler(this.getActivity());
        this.gameoverFragmentPresenter = new GameoverFragmentPresenter(GameoverFragmentArgs.fromBundle(getArguments()).getScore(), this, this.db, this.toast);

        this.saveButton.setOnClickListener(this);

        this.gameoverFragmentPresenter.loadData();
        saveButton.setOnClickListener(v->saveScore());
        saved= false;

        return view;
    }



    private void saveScore() {
        if(!saved){
            String name = playerName.getText().toString();
            if(name.length()>0) {
                DataBasePlayWare.INSTANCE.postGameChallenge(score, name);
                saved = true;
            }
            else Toast.makeText(getContext(),"write your naame before saving",Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void setScore(int score) {

        this.scoreTextView.setText("Your Score: " + score);
        this.score = score;
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
