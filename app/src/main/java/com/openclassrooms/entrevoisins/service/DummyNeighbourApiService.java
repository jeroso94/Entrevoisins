package com.openclassrooms.entrevoisins.service;

import android.util.Log;
import android.widget.Toast;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * {@inheritDoc}
     * Selection des voisins favoris avec la classe Iterator
     */
    @Override
    public List<Neighbour> createFavoriteList() {
        List<Neighbour> favoriteNeighbours = new ArrayList<>(getNeighbours());
        for (Iterator<Neighbour> neighbourIterator = favoriteNeighbours.iterator(); neighbourIterator.hasNext(); ){
            Neighbour neighbour = neighbourIterator.next();
            if(!neighbour.getFavoriteFlag()){
                neighbourIterator.remove();
            }
        }
        return favoriteNeighbours;
    }

    /**
     *
     * @param neighbourId
     */
    @Override
    public boolean addFavorite(long neighbourId) {
        for(Neighbour registeredNeighbour: neighbours) {
            if (registeredNeighbour.getId() == neighbourId) {
                if (!registeredNeighbour.getFavoriteFlag()) {
                    registeredNeighbour.setFavoriteFlag(Boolean.TRUE);
                    return Boolean.TRUE;
                } else{
                    registeredNeighbour.setFavoriteFlag(Boolean.FALSE);
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteFavorite(Neighbour favoriteNeighbour) {
        List<Neighbour> favoriteNeighbours = new ArrayList<>(getNeighbours());
        favoriteNeighbour.setFavoriteFlag(false);
        return Boolean.TRUE;
    }

}
