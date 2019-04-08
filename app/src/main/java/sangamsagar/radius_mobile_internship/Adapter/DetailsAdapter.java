package sangamsagar.radius_mobile_internship.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import sangamsagar.radius_mobile_internship.Model;
import sangamsagar.radius_mobile_internship.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private List<Model> moviesList;
    Typeface face,face1,face2;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre;
        ImageView imageView;


        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            genre = view.findViewById(R.id.genre);
            imageView = view.findViewById(R.id.imageView);
        }
    }


    public DetailsAdapter(List<Model> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        Model movie = moviesList.get(position);
        holder.title.setText("Age :" + movie.getAge());
        holder.genre.setText("Name :" + movie.getName());
        Picasso.get().load(movie.getImage()).fit().centerCrop().into(holder.imageView);

        face1 = Typeface.createFromAsset(context.getAssets(), "fonts/sairasemibold.ttf");

        holder.title.setTypeface(face1);
        holder.genre.setTypeface(face1);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
