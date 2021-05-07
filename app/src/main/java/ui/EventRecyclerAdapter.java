package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstrengthlog.R;

import org.w3c.dom.Text;

import java.util.List;

import models.Event;
import models.Workout;

public class EventRecyclerAdapter  extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Event> eventList;

    OnEventClickListener onEventClickListener;


    public EventRecyclerAdapter(Context homeFragmentContext, List<Event> allEventsList, OnEventClickListener onEventClickListener) {
        this.context = homeFragmentContext;
        this.eventList = allEventsList;
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_row,parent, false);
        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.row_event_name_list);
            date = itemView.findViewById(R.id.row_event_date_list);

        }
    }



    //Para los eventos
    public interface OnEventClickListener{
        void onEventClick(int position);
    }
}
