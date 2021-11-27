package dk.au.mad21fall.appproject.group3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BarAdapter extends RecyclerView.Adapter<BarAdapter.BarViewHolder> {


    public interface IBarItemClickedListener{
        void onBarClicked(int index);
    }

    //callback interface for user actions on each item
    private IBarItemClickedListener listener;

    //data in the adapter
    private List<Bar> barList;

    public List<Bar> Bars(){
        return barList;
    }

    //constructor
    public BarAdapter(IBarItemClickedListener listener){
        this.listener = listener;
    }

    //a method for updating the list - causes the adapter/recyclerview to update
    public void updateBarList(List<Bar> lists){
        barList = lists;
        notifyDataSetChanged();
    }

    //override this method to create the viewholder object the first time they are requested
    //use the inflater from parent (Activity's viewgroup) to get the view and then use view holders constructor
    @NonNull
    @Override
    public BarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_item, parent, false);
        BarViewHolder vh = new BarViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BarViewHolder holder, int position) {

        //TODO: make this display items

        //Set the text and picture.
        holder.txtName.setText(barList.get(position).getName());
        holder.txtRating.setText("5.0");//""+ movieList.get(position).getUserRating());
        //I wanted to make this check for internext, and post the standard genre if not present, via
        //via the function i made on the Constants file.
        //I couldn't get the context to work properly though, so I didn't implement it.
        //BarGlide.with(holder.imgIcon.getContext()).load(barList.get(position).getPoster()).into(holder.imgIcon);
        //holder.imgIcon.setImageResource(movieList.get(position).drawable(movieList.get(position).getGenre()));
    }

    //override this to return size of list
    @Override
    public int getItemCount() {
        if(barList != null){
            return barList.size();}
        else{
            return 0;
        }
    }

    //The ViewHolder class for holding information about each list item in the RecyclerView
    public class BarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Set up the widgets for the UI view.
        TextView txtName;
        TextView txtRating;
        ImageView imgIcon;
        ImageView imgColor;

        //custom callback interface for user actions done the view holder item
        IBarItemClickedListener listener;

        //constructor
        public BarViewHolder(@NonNull View itemView, IBarItemClickedListener barItemClickedListener) {
            super(itemView);

            //get references from the layout file

            //TODO: Fix the picture (if internet, picture, if not, standard genre icon).
            imgIcon = itemView.findViewById(R.id.imgIcon);
            imgColor = itemView.findViewById(R.id.imgColor);
            txtName = itemView.findViewById(R.id.txtName);
            txtRating = itemView.findViewById(R.id.txtRating);

            listener = barItemClickedListener;

            //set click listener for whole list item
            itemView.setOnClickListener(this);

            //set click listener specifically for the invite button
        }

        //react to user clicking the listitem (implements OnClickListener)
        @Override
        public void onClick(View view) {
            listener.onBarClicked(getAdapterPosition());
        }
    }
}

