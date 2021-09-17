package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Boolean.FALSE;


/**
 * Created by JeroSo94 on 16/08/2021.
 */
public class MyFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder>{

    private final List<Neighbour> mNeighbours;
    private NeighbourApiService mApiService;

    public MyFavoriteRecyclerViewAdapter(List<Neighbour> items) {
        mNeighbours = items;
    }

    @Override
    public MyFavoriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favorite, parent, false);

        // Connexion à l'API pour une recherche d'utilisateurs
        mApiService = DI.getNeighbourApiService();

        return new MyFavoriteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyFavoriteRecyclerViewAdapter.ViewHolder holder, int position) {
        Neighbour favoriteNeighbour = mNeighbours.get(position);
        holder.mNeighbourNameFavorite.setText(favoriteNeighbour.getName());
        Glide.with(holder.mNeighbourAvatarFavorite.getContext())
                .load(favoriteNeighbour.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mNeighbourAvatarFavorite);

        holder.mDeleteButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = FALSE;
                result = mApiService.deleteFavorite(favoriteNeighbour);
                if (result) {
                    mNeighbours.remove(favoriteNeighbour);
                    MyFavoriteRecyclerViewAdapter.this.notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "Successfully removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNeighbours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_list_avatar_favorite)
        public ImageView mNeighbourAvatarFavorite;
        @BindView(R.id.item_list_name_favorite)
        public TextView mNeighbourNameFavorite;
        @BindView(R.id.item_list_delete_button_favorite)
        public ImageButton mDeleteButtonFavorite;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
