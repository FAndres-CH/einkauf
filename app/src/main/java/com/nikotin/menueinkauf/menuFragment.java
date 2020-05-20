package com.nikotin.menueinkauf;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.nikotin.menueinkauf.MainActivity.LOG_TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link menuFragment} factory method to
 * create an instance of this fragment.
 */
public class menuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imgViewMenu;
    private TextView txtViewMenuTitle;
    private TextView txtViewMenuKueche;
    private TextView txtViewMenuArt;
    private TextView txtViewMenuZutaten;
    private TextView txtViewMenuZubereitung;
    private TextView buttonMenuMerken;
    private MenuNormal mn;

    public menuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.menu_fragment, container, false);
        imgViewMenu=(ImageView) v.findViewById(R.id.image_view_menu);
        imgViewMenu.setOnClickListener(this);
        txtViewMenuTitle=(TextView) v.findViewById(R.id.txtViewMenueTitle);
        txtViewMenuKueche=(TextView) v.findViewById(R.id.txtViewMenueKueche);
        txtViewMenuArt=(TextView) v.findViewById(R.id.txtViewMenueArt);
        txtViewMenuZutaten=(TextView) v.findViewById(R.id.txtViewMenueZutaten);
        txtViewMenuZubereitung = (TextView)v.findViewById(R.id.txtViewMenuZubereitung);
        buttonMenuMerken = (TextView)v.findViewById(R.id.buttonMenuMerken);
        buttonMenuMerken.setOnClickListener(this);
        mn=((MainActivity) getActivity()).getSelectedMenu();
        fillMenu();
        return v;
    }

    //DV: View will be filled with information stored in the selectedMenu Object from MainActivity
    public void fillMenu(){
        Picasso.with(getContext()).load(mn.getBildUrl()).into(imgViewMenu);
        txtViewMenuTitle.setText(mn.getName());
        txtViewMenuKueche.setText("Küche: "+mn.getKueche());
        txtViewMenuArt.setText("Art: "+mn.getArt());
        txtViewMenuZutaten.setText("Zutaten für "+mn.getAnzPersonen().toString()+" Personen: \n"
                +mn.getZutaten());
        txtViewMenuZubereitung.setText(mn.getZubereitung());
    }

    @Override
    public void onClick(View v) {
        if(v == buttonMenuMerken){
            SharedPreferences sp = getActivity().getPreferences(Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("menuId_gemerkt", mn.getMenuId());
            editor.apply();

            buttonMenuMerken.setBackgroundColor(Color.rgb(10,140, 60));

            Toast.makeText(getContext(), "Menu wurde gemerkt", Toast.LENGTH_LONG).show();
        }
    }
}
