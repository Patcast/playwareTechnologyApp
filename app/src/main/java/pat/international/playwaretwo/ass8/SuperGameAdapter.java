package pat.international.playwaretwo.ass8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pat.international.playwaretwo.R;

public class SuperGameAdapter extends RecyclerView.Adapter<SuperGameAdapter.ViewHolder>{
    private final List<Games> gamesList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewParent = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_ass8,parent,false);
        return new SuperGameAdapter.ViewHolder(viewParent);
    }

    @Override
    public void onBindViewHolder(@NonNull SuperGameAdapter.ViewHolder holder, int position) {
        holder.gameTitle.setText(gamesList.get(position).getGamesName());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView gameTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.game_title_txt);
        }
    }

    public void seSuperGameAdapter(ArrayList <Games> gamesInput) {
        gamesList.clear();
        this.gamesList.addAll(gamesInput);
        notifyDataSetChanged();

    }
}
