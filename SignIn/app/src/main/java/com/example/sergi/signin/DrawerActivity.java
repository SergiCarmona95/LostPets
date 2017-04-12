package com.example.sergi.signin;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends GoogleApiActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static int numTab;
    DatabaseReference mDatabase;
    ListPerroClass classPerro;
    StorageReference pathReference;
    private StorageReference mStorageRef;
    private List<Perro> listPerro;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        cargarDatos();
        cargarPerrosFirebaseInList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("size="+classPerro.getList().size());
        /*try {
            System.out.println("size="+classPerro.getList().size());
            descargarImagenesFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    public void descargarImagenesFirebase() throws IOException {
        final File dir = new File(getBaseContext().getFilesDir().getAbsolutePath()+"imagenes");
        if (!dir.exists()) {
            Toast.makeText(this, "CREADO LA CARPETA", Toast.LENGTH_SHORT).show();
            dir.mkdirs();
        }
        System.out.println("a");
        System.out.println("size="+classPerro.getList().size());
        /*for (Perro p:listPerro) {
            String nomIma=p.getImageUri();
            mStorageRef.child("images/"+nomIma);
            final long ONE_MEGABYTE = 1024 * 1024;
            File localFile = File.createTempFile("images", "jpg");

            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }*///universalimageloader
        System.out.println("size="+classPerro.getList().size());
        for (Perro p:listPerro) {
            //System.out.println(p.toString());
            /*if (p.getNombre().equals("firulais")){
                System.out.println("imagen="+p.getImageUri());
               /* System.out.println("1");
                final String nomIma=p.getImageUri();
                StorageReference pathReference =mStorageRef.child("Photos/"+nomIma);
                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        System.out.println("2");
                        try {
                           FileOutputStream foto =new FileOutputStream(dir+nomIma);
                            DataOutputStream dataOut = new DataOutputStream(foto);
                            while (true){
                                dataOut.write(bytes);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (EOFException e){
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(DrawerActivity.this, "Imagen Descargada", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });*/
            //}
        }
    }
    public void guardarPerros(Perro perro){
        listPerro.add(perro);
    }

    public void cargarPerrosFirebaseInList(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("perro").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Perro perro = dataSnapshot.getValue(Perro.class);
                //listPerro.add(perro);
                guardarPerros(perro);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void cargarDatos(){
        classPerro= new ListPerroClass();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        listPerro=new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://signin-2913c.appspot.com");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.newDogLost) {
            Intent i = new Intent(this,NewNoticeActivity.class);
            startActivity(i);
        } else if (id == R.id.newDogRescue) {
            Intent i = new Intent(this,NewDogRescueActivity.class);
            startActivity(i);
        } else if (id == R.id.MyNotices) {
            Intent i = new Intent(this,MyNoticeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

    class ListPerroClass{
        private List<Perro> listPerro;

        public ListPerroClass() {
            this.listPerro = new ArrayList<>();
        }

        public List<Perro> getList(){
            return listPerro;
        }
    }
}