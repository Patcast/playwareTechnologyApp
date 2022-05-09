package pat.international.playwaretwo.PianoTiles.GameCode;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import java.util.ArrayList;
import java.util.List;


import pat.international.playwaretwo.R;


public class HighScoreFragment extends Fragment implements HighScoreFragmentPresenter.IHighScoreFragment {
    HighScoreFragmentPresenter highScoreFragmentPresenter;
    ListAdapter adapterList;
    ListView listView;
    ArrayList<Score> challengesList = new ArrayList<>();




    public HighScoreFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);


        this.adapterList = new ListAdapter(this.getActivity());
        this.listView = view.findViewById(R.id.lst_fragment_score);
        this.listView.setAdapter(this.adapterList);
        this.highScoreFragmentPresenter = new HighScoreFragmentPresenter(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataBasePlayWare.INSTANCE.requestData(getContext());
        targetHandler.postDelayed(targetRunnable, 100);

    }

    public Handler targetHandler = new Handler();
    public Runnable targetRunnable = new Runnable() {
        @Override
        public void run() {
            challengesList.clear();
            challengesList.addAll(DataBasePlayWare.INSTANCE.getChallengesList());
            if (challengesList.size()>0){
                highScoreFragmentPresenter.loadData(challengesList);
            }
            if (challengesList.size() ==0){
                targetHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    public void updateList(List<Score> list) {
        this.adapterList.updateList(list);
    }




}
