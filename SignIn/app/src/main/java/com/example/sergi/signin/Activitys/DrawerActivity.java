package com.example.sergi.signin.Activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sergi.signin.Class.Coordenadas;
import com.example.sergi.signin.Class.Datos;
import com.example.sergi.signin.Fragments.FragmentListViewDogLost;
import com.example.sergi.signin.Fragments.FragmentListViewDogResque;
import com.example.sergi.signin.Google.GoogleApiActivity;
import com.example.sergi.signin.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;


public class DrawerActivity extends GoogleApiActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static int numTab;
    public static Datos datos;
    String raza,nombre,color,chip;
    Spinner spinner;
    LayoutInflater inflater;
    ArrayAdapter<CharSequence> adapter;
    boolean cargarDatos=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        cargarVariables();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        datos.crearCarpeta();
        datos.cargarPerrosEncontrados();
        datos.cargarPerrosPerdidos();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DrawerActivity.this);
                View mView=inflater.inflate(R.layout.dialog_filter_dog,null);
                final TextView nombreTextView=(TextView)mView.findViewById(R.id.nombreFiltrar);
                final TextView colorTextView=(TextView)mView.findViewById(R.id.colorFiltrar);
                final RadioButton chipSi=(RadioButton) mView.findViewById(R.id.filtrarChipSi);
                final RadioButton chipNo=(RadioButton) mView.findViewById(R.id.filtrarChipNo);
                spinner=(Spinner)mView.findViewById(R.id.spinnerFiltrar);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        raza=adapter.getItem(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button botonFiltrar=(Button) mView.findViewById(R.id.botonFiltrar);
                botonFiltrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean ok = false;
                        if (chipSi.isChecked()) {
                            chip = "yes";
                            ok = true;
                        } else if (chipNo.isChecked()) {
                            chip = "no";
                            ok = true;
                        }
                        color = colorTextView.getText().toString();
                        nombre = nombreTextView.getText().toString();
                        if (ok == true) {
                            Intent i = new Intent(DrawerActivity.this, FiltrarActivity.class);
                            i.putExtra("nombre", nombre);
                            i.putExtra("chip", chip);
                            i.putExtra("raza", raza);
                            i.putExtra("color", color);
                            startActivity(i);
                        }else{
                            Toast.makeText(DrawerActivity.this, "Has de elegir si tiene chip o no", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                spinner.setAdapter(adapter);
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void cargarVariables(){
        adapter = ArrayAdapter.createFromResource(this, R.array.razas, android.R.layout.simple_spinner_item);
        inflater = this.getLayoutInflater();
        datos= new Datos(getBaseContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.newDogLost) {
            Intent i = new Intent(this,NewNoticeActivity.class);
            Coordenadas coordenadas= new Coordenadas("drawer",0.0,0.0);
            i.putExtra("activity",coordenadas);
            startActivity(i);
        } else if (id == R.id.newDogRescue) {
            Intent i = new Intent(this,NewDogRescueActivity.class);
            Coordenadas coordenadas= new Coordenadas("drawer",0.0,0.0);
            i.putExtra("activity",coordenadas);
            startActivity(i);
        } else if (id == R.id.MyNotices) {
            Intent i = new Intent(this,MyNoticeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sign_out) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:
               FragmentListViewDogLost tab1= new FragmentListViewDogLost();
                return tab1;
               case 1:
                   FragmentListViewDogResque tab2= new FragmentListViewDogResque();
                   return tab2;
               default:
                   return null;
           }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    numTab=1;
                    return "Perros Perdidos";
                case 1:
                    numTab=2;
                    return "Perros Encontrados";
            }
            return null;
        }
    }

}