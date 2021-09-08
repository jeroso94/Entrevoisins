package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NeighbourActivity extends AppCompatActivity {

    @BindView(R.id.neighbourProfileAvatar) ImageView mAvatar;
    @BindView(R.id.floatingActionButton) ImageView mFloatingActionButton;
    @BindView(R.id.title) TextView mTitleText;
    @BindView(R.id.name) TextView mNameText;
    @BindView(R.id.location) TextView mLocationText;
    @BindView(R.id.phone) TextView mPhoneText;
    @BindView(R.id.socialNetwork) TextView mSocialNetworkText;
    @BindView(R.id.aboutMeDescription) TextView mAboutMeText;

    private List<Neighbour>  mNeighbours;
    private NeighbourApiService mApiService;
    private long mClickedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour);

        //Butterknife - Déclaration de ButterKnife pour cette activité
        ButterKnife.bind(this);

        // NAVIGATION ActionBar - Affiche la flêche de retour à l'écran d'accueil (dépendant de AndroidManifest.xml - option de thème ActionBar activée, par défaut + indication parentActivityName)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Connexion à l'API pour une recherche d'utilisateurs
        mApiService = DI.getNeighbourApiService();

        //Récuperation de l'index du voisin sur lequel l'utilisateur a cliqué (passé en paramètre lors de l'appel de cette activité)
        Intent mIntent = getIntent();
        mClickedId = mIntent.getLongExtra("ID",0);

        // Affichage du profil de l'utilisateur
        showNeighbourProfile();
    }

    private void showNeighbourProfile () {
        mNeighbours = mApiService.getNeighbours();
        for(Neighbour registeredNeighbour: mNeighbours){
            Log.d("NEIGHBOUR's PROFILE", mClickedId + "|" + registeredNeighbour.getId() + "|" + registeredNeighbour.getName());
            if (registeredNeighbour.getId() == mClickedId) {
                // Provision des informations de profil depuis l'API pour affichage dans le layout

                Glide.with(this).load(registeredNeighbour.getAvatarUrl()).placeholder(R.drawable.ic_account)
                        .apply(RequestOptions.centerCropTransform()).into(mAvatar);

                mTitleText.setText(registeredNeighbour.getName());
                mNameText.setText(registeredNeighbour.getName());
                mLocationText.setText(registeredNeighbour.getAddress());
                mPhoneText.setText(registeredNeighbour.getPhoneNumber());
                mSocialNetworkText.setText("https://facebook.com/" + registeredNeighbour.getName());
                mAboutMeText.setText(registeredNeighbour.getAboutMe());

                // FAVORIS - Ajustement de l'indicateur Favoris (A retenir<->Déjà retenu)
                if (registeredNeighbour.getFavoriteFlag() == Boolean.TRUE){
                    mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_24);
                }
                else{
                    mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_border_24);
                }
            }
        }
    }

    @OnClick(R.id.floatingActionButton)
    void addToFavorite() {
        //Log.d("NEIGHBOUR's PROFILE", "addToFavorite: is clicked");
        mNeighbours = mApiService.getNeighbours();
        for(Neighbour registeredNeighbour: mNeighbours) {
            //Log.d("NEIGHBOUR's PROFILE", registeredNeighbour.getFavoriteFlag().toString());
            if (registeredNeighbour.getId() == mClickedId) {
                if (!registeredNeighbour.getFavoriteFlag()) {
                    registeredNeighbour.setFavoriteFlag(Boolean.TRUE);
                    //Log.d("NEIGHBOUR's PROFILE", registeredNeighbour.getFavoriteFlag().toString());
                    mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_24);
                } else{
                    registeredNeighbour.setFavoriteFlag(Boolean.FALSE);
                    mFloatingActionButton.setImageResource(R.drawable.ic_baseline_star_border_24);
                }
            }
        }
    }
}