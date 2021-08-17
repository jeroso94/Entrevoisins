package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;


public class FavoriteFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;

    public static FavoriteFragment newInstance() { return (new FavoriteFragment()); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private void initList() {
        mNeighbours = mApiService.getNeighbours();
        // boucle for (mNeighbours) avec ajout champs FlagFavorite
        // Si FlagFavorite = true
        mRecyclerView.setAdapter(new MyFavoriteRecyclerViewAdapter(mNeighbours));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }
}