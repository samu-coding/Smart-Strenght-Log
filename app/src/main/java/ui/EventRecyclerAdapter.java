package ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstrengthlog.MainMenu;
import com.example.smartstrengthlog.R;

import org.w3c.dom.Text;

import java.util.List;

import models.Event;
import models.Workout;
import util.SmartStrengthLogAPI;

public class EventRecyclerAdapter  extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private OnEventClickListener onEventClickListener;
    private Context context;
    private List<Event> eventList;

    OnEventClickListener eventClickListener;


    public EventRecyclerAdapter(Context homeFragmentContext, List<Event> allEventsList, OnEventClickListener onEventClickListener) {
        this.context = homeFragmentContext;
        this.eventList = allEventsList;
        this.eventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_row,parent, false);
        return new ViewHolder(view, eventClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.name.setText(event.getNameEvent());
        holder.date.setText(event.getDateEvent());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnEventClickListener onEventClickListener;
        public TextView name;
        public TextView date;
        public ViewHolder(@NonNull View itemView, OnEventClickListener onEventClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.row_event_name_list);
            date = itemView.findViewById(R.id.row_event_date_list);
            this.onEventClickListener = onEventClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onEventClickListener.onEventClick(getAdapterPosition());
        }
    }



    //Para los eventos
    public interface OnEventClickListener{
        void onEventClick(int position);
    }

}
