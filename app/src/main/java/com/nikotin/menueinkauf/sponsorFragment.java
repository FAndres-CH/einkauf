package com.nikotin.menueinkauf;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nikotin.menueinkauf.MainActivity.BASE_URL;
import static com.nikotin.menueinkauf.MainActivity.LOG_TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sponsorFragment} factory method to
 * create an instance of this fragment.
 */
public class sponsorFragment extends Fragment implements View.OnClickListener {
    private ImageView sponsorImage;
    private TextView buttonWeiter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sponsor_fragment, container, false);
        sponsorImage = v.findViewById(R.id.sponsorImage);
        buttonWeiter = (TextView) v.findViewById(R.id.buttonWeiter);
        buttonWeiter.setOnClickListener(this);
        getSponsor();
        return v;
    }

    private void getSponsor(){
        //DV: Part of Retrofit Code
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetroMenuAPI retroMenuAPI = retrofit.create(RetroMenuAPI.class);

        Call<SponsorEntity> callSponsor = retroMenuAPI.getSponsor();
        callSponsor.enqueue(new Callback<SponsorEntity>() {
            @Override
            public void onResponse(Call<SponsorEntity> call, Response<SponsorEntity> response) {
                if (response.body()==null){
                    Toast.makeText(getActivity(), "Fehler: Keine gültige Antwort vom Server", Toast.LENGTH_LONG).show();
                    return;
                }
                setSponsorImage(response.body());
            }

            @Override
            public void onFailure(Call<SponsorEntity> call, Throwable t) {
                Toast.makeText(getActivity(), "Service nicht erreicht. Daten konnten nicht geladen werden", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSponsorImage(SponsorEntity sponsor){
        Picasso.with(getActivity()).load(sponsor.getSponsorUrl()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(this.sponsorImage);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonWeiter){
            ((MainActivity) Objects.requireNonNull(getActivity())).navigateTo(new startFragment(),true);
        }
    }
}
