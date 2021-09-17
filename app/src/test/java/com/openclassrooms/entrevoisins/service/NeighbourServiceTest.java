package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourActivity;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() { service = DI.getNewInstanceApiService(); }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    // TESTED - S'assurer que la méthode createFavoriteList se construit qu'avec des items marqués comme favori
    @Test
    public void createFavoriteListWithSuccess(){
        //Préparation du jeu de test
        Neighbour defaultFavorite = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(6);
        List<Neighbour> favoriteNeighbours = service.createFavoriteList();

        //Simulation de l'exécution de la méthode & évalutation du résultat
        assertEquals(1, service.createFavoriteList().size());
        assertEquals(defaultFavorite, favoriteNeighbours.get(0));
    }

    // TESTED - S'assurer que la methode addFavorite modifie le champs FavoriteFlag à TRUE pour le voisin concerné
    @Test
    public void addFavoriteWithSuccess(){
        //Préparation du jeu de test
        service.getNeighbours().get(0).setFavoriteFlag(Boolean.TRUE);

        // Simulation de l'exécution de la méthode
        service.addFavorite(service.getNeighbours().get(0).getId());

        //Evaluation du résultat
        assertFalse(service.getNeighbours().get(0).getFavoriteFlag());
    }

    // TESTED - S'assurer que la methode deleteFavorite modifie le champs FavoriteFlag à FALSE pour le favori concerné
    @Test
    public void deleteFavoriteWithSuccess() {
        Neighbour favoriteToDelete = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(6);
        service.deleteFavorite(favoriteToDelete);
        assertFalse(service.getNeighbours().get(6).getFavoriteFlag());
    }
}
