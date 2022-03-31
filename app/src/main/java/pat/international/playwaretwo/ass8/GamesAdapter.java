package pat.international.playwaretwo.ass8;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class GamesAdapter extends ArrayAdapter<Games> {

    private static final String LOG_TAG = GamesAdapter.class.getSimpleName();


    public GamesAdapter(Activity context, ArrayList<Games> games) {

        super(context, 0, games);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Games currentGames = getItem(position);


        TextView nameTextView = (TextView) listItemView.findViewById(R.id.games_name);

        nameTextView.setText(currentGames.getGamesName());


        return listItemView;
    }
}