package com.nikotin.menueinkauf;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import static com.nikotin.menueinkauf.MainActivity.BASE_URL;
import static com.nikotin.menueinkauf.MainActivity.LOG_TAG;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link startFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class startFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView imgViewLogo;
    private ImageView imgViewMenu;
    private TextView txtViewMenuTitle;
    private TextView txtViewMenuInfo;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public startFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment startFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static startFragment newInstance(String param1, String param2) {
        startFragment fragment = new startFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mymenu_menu, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.itemOpenSavedMenu){
            SharedPreferences sp = getActivity().getPreferences(Activity.MODE_PRIVATE);
            int menuId = sp.getInt("menuId_gemerkt", -1);
            if(menuId == -1){
                Toast.makeText(getContext(), "Kein gemerktes Menu gefunden", Toast.LENGTH_LONG).show();
                return false;
            }
            doSavedMenuCall(menuId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.start_fragment, container, false);
        imgViewLogo=(ImageView) v.findViewById(R.id.imageViewLogo);
        imgViewLogo.setOnClickListener(this); //DV: clicking reference to current view
        imgViewMenu=(ImageView) v.findViewById(R.id.image_view_menu);
        imgViewMenu.setOnClickListener(this);
        txtViewMenuTitle=(TextView) v.findViewById(R.id.txtViewMenueTitle);
        txtViewMenuInfo=(TextView) v.findViewById(R.id.txtViewMenueInfo);
        if (((MainActivity) getActivity()).getSelectedMenu()==null){
            doRandMenueCall();
        }
        else{
            fillMenu();
        }
        return v;
    }

    private void doSavedMenuCall(int menuId) {
        //DV: Part of Retrofit Code
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetroMenuAPI retroMenuAPI = retrofit.create(RetroMenuAPI.class);

        Call<MenuNormal> callMenu = retroMenuAPI.loadMenu(menuId);
        callMenu.enqueue(new Callback<MenuNormal>() {
            @Override
            public void onResponse(Call<MenuNormal> call, Response<MenuNormal> response) {
                //DV: The response Object is in the Body.
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "Fehler: Keine gültige Antwort vom Server", Toast.LENGTH_LONG).show();
                    return;
                }
                ((MainActivity) getActivity()).setSelectedMenu(response.body());
                fillMenu();
            }

            @Override
            public void onFailure(Call<MenuNormal> call, Throwable t) {
                Toast.makeText(getActivity(), "Service nicht erreicht. Daten konnten nicht geladen werden", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doRandMenueCall(){
        //DV: Part of Retrofit Code
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetroMenuAPI retroMenuAPI = retrofit.create(RetroMenuAPI.class);

        Call<MenuNormal> callRandMenu=retroMenuAPI.loadRandomMenu();
        callRandMenu.enqueue(new Callback<MenuNormal>() {
            @Override
            public void onResponse(Call<MenuNormal> call, Response<MenuNormal> response) {
                //DV: The response Object is in the Body.
                if (response.body()==null){
                    Toast.makeText(getActivity(), "Fehler: Keine gültige Antwort vom Server", Toast.LENGTH_LONG).show();
                    return;
                }
                ((MainActivity) getActivity()).setSelectedMenu(response.body());
                fillMenu();
            }

            @Override
            public void onFailure(Call<MenuNormal> call, Throwable t) {
                Toast.makeText(getActivity(), "Service nicht erreicht. Daten konnten nicht geladen werden", Toast.LENGTH_LONG).show();
            }
        });

        /**
        //DV: The Following part of Code shows how a List Response would be handeld with JSON
        //Example is comming from github.com
        Call<List<RetroMenuNormal>> call = retroMenuAPI.loadRandMenue("status:open");
        call.enqueue(new Callback<List<RetroMenuNormal>>() {
            @Override
            public void onResponse(Call<List<RetroMenuNormal>> call, Response<List<RetroMenuNormal>> response) {
                //DV: The response Object is in the Body.
                if (response.body()==null){
                    Toast.makeText(getActivity(), "Fehler: Keine gültige Antwort vom Server", Toast.LENGTH_LONG).show();
                    return;
                }
                Iterator<RetroMenuNormal> iter=response.body().iterator();

                while(iter.hasNext()){
                    RetroMenuNormal retroResp=iter.next();
                    Log.d(LOG_TAG,retroResp.subject+"....::Retrofit Response::...."+retroResp.project);//DV: Log zum prüfen
                    txtViewMenuTitle.setText(retroResp.project);
                    txtViewMenuInfo.setText(retroResp.subject);
                }
            }

            @Override
            public void onFailure(Call<List<RetroMenuNormal>> call, Throwable t) {
                Toast.makeText(getActivity(), "Service nicht erreicht. Daten konnten nicht geladen werden", Toast.LENGTH_LONG).show();
            }
        }); //führt den call aus
        **/
    }
    /**
     * DV: This Method Fills the UI Elements with MenuData
     * */
    public void fillMenu(){
        MenuNormal mn=((MainActivity) getActivity()).getSelectedMenu();
        txtViewMenuTitle.setText(mn.getName());
        txtViewMenuInfo.setText(mn.getArt()+" | "+mn.getKueche());
        Picasso.with(getContext()).load(mn.getBildUrl()).into(imgViewMenu);

    }

    @Override
    public void onClick(View v) {
        if (v==imgViewLogo) {
            //todo: DV-->put here the Code to react when sombody clicks the Menu Logo
            //Toast.makeText(getActivity(), "bubu...bubu....", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "ImageLogo wurde geklickt");
            doRandMenueCall();
        }
        //DV: todo: wenn auf das Bild vom Menu geklickt wird nevigieren wir auf ein neues Fragment e.g. MenuDetail
        if (v==imgViewMenu){
            ((MainActivity) getActivity()).navigateTo(new menuFragment(),true);
        }

    }

}
