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

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder>{
    private final List<Challenge> gamesList = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewParent = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_ass8,parent,false);
        return new ChallengesAdapter.ViewHolder(viewParent);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHolder holder, int position) {
        holder.challengedName.setText(gamesList.get(position).getChallengedName());
        holder.challengerName.setText(gamesList.get(position).getChallengerName());
        holder.gameId.setText(String.valueOf(gamesList.get(position).getGameId()));
        holder.status.setText(String.valueOf(gamesList.get(position).getStatus()));
        holder.dateCreated.setText(gamesList.get(position).getDateCreated());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView challengerName, challengedName,gameId,status,dateCreated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challengerName = itemView.findViewById(R.id.challenger_name_txt);
            dateCreated = itemView.findViewById(R.id.date_txt);
            challengedName = itemView.findViewById(R.id.challenged_name_txt);
            gameId = itemView.findViewById(R.id.game_id_txt);
            status = itemView.findViewById(R.id.c_status_txt);
        }
    }

    public void setSuperGameAdapter(ArrayList <Challenge> gamesInput) {
        gamesList.clear();
        this.gamesList.addAll(gamesInput);
        notifyDataSetChanged();

    }
}
