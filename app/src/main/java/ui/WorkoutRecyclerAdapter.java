package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartstrengthlog.R;

import java.util.List;

import models.Workout;

public class WorkoutRecyclerAdapter extends RecyclerView.Adapter<WorkoutRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Workout> workoutList;

    public WorkoutRecyclerAdapter(Context context, List<Workout> workoutList) {
        this.context = context;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.workout_row, parent, false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutRecyclerAdapter.ViewHolder holder, int position) {

        Workout workout = workoutList.get(position);

        holder.title.setText(workout.getTitle());
        holder.workoutDescription.setText(workout.getDescription());




    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title,
                workoutDescription, name;
        String userId;
        String username;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.workout_title_list);
            workoutDescription = itemView.findViewById(R.id.workout_list);

        }
    }
}
