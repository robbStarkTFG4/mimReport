package com.example.nore.turndown;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.nore.turndown.CustomListView.ExpandableCustomAdapter;
import com.example.nore.turndown.entity.dao.DaoMaster;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.fragments.AddJobFragment;
import com.example.nore.turndown.fragments.EnviadosFragment;
import com.example.nore.turndown.fragments.PendingFragment;
import com.example.nore.turndown.fragments.RegisterAccount;
import com.example.nore.turndown.util.interfaces.ReportFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ExpandableCustomAdapter.ObjectFacilitator
        , AddJobFragment.TransitionCommunicator {

    public DrawerLayout drawer;
    private int mCurrentSelectedPosition = 0;

    private CharSequence mTitle;

    // database objects
    private SQLiteDatabase db;
    private DaoMaster master;
    public DaoSession session;
    //End database objects

    // app logic objects
    public int jobIndex;
    public String imgFile;
    public boolean lostFocus = false;
    //End app logic objects

    private Job jb;
    private String photoTitle;

    private View parent;

    private ReportFragment reportFrag;

    private int fragmentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.main_content);

        mTitle = getTitle();

        initToolbar();
        setupDrawerLayout();

        try {
            DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "mimDb13", null);
            db = openHelper.getWritableDatabase();
            master = new DaoMaster(db);
            session = master.newSession();

        } catch (Exception e) {
            Log.d("d", e.getMessage());
        }

        AddJobFragment objT = new AddJobFragment();
        objT.setTrans(this);
        reportFrag = objT;

        Fragment frag = getSupportFragmentManager().findFragmentByTag("addJob");
        if (frag != null) {
            getSupportFragmentManager().beginTransaction().remove(frag);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, objT, "addJob")
                .commit();

    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(new IconDrawable(this,MaterialIcons.md_view_headline));
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });

            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();

                //Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                //menuItem.setChecked(true);
                onNavigationDrawerItemSelected(menuItem.getItemId());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //changeFragment(position);
        fragmentPosition = position;
        if (reportFrag != null) {
            reportFrag.triggerActionFromDrawer();
        } else {
            performChange();
        }

    }

    public void performChange() {
        changeFragment(fragmentPosition);
    }

    private void changeFragment(int position) {
        if (!lostFocus) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment obj = null;
            switch (position) {
                case R.id.generate_report:
                    AddJobFragment objT = new AddJobFragment();
                    objT.setTrans(this);
                    reportFrag = objT;

                    Fragment frag = fragmentManager.findFragmentByTag("addJob");
                    if (frag != null) {
                        fragmentManager.beginTransaction().remove(frag);
                    }

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, objT, "addJob")
                            .commit();
                    break;
                case R.id.pending_report:
                    obj = new PendingFragment();

                    Fragment frag1 = fragmentManager.findFragmentByTag("pending");
                    if (frag1 != null) {
                        fragmentManager.beginTransaction().remove(frag1);
                    }

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, obj, "pending")
                            .commit();
                    break;
                case R.id.delivered_report:
                    obj = new EnviadosFragment();

                    Fragment frag2 = fragmentManager.findFragmentByTag("enviados");
                    if (frag2 != null) {
                        fragmentManager.beginTransaction().remove(frag2);
                    }

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, obj, "enviados")
                            .commit();
                    break;
                case 3:
                    obj = new RegisterAccount();
                    break;
            }
        } else {
            Snackbar
                    .make(parent, "Operacion en proceso espera un momento.....", Snackbar.LENGTH_LONG)
                    .show();
            //  Toast.makeText(this, "Operacion en proceso espera un momento.....", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (reportFrag != null) {
            reportFrag.triggerAction();
        } else {
            super.onBackPressed();
        }
    }

    public void backEvent() {
        super.onBackPressed();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ExpandableCustomAdapter.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                if (jb != null) {
                    List<ImageInfo> list = jb.getImageInfo2();
                    if (list != null) {
                        ImageInfo img = new ImageInfo();
                        img.setImgRoute(photoTitle);
                        list.add(img);
                    } else {
                        list = new ArrayList<>();
                        ImageInfo img = new ImageInfo();
                        img.setImgRoute(photoTitle);
                        list.add(img);

                        if (jb.getImageInfo2() == null) {
                            jb.setImageInfo(list);
                        }
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

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

    @Override
    public void selectedJob(Job job, String title) {
        this.jb = job;
        this.photoTitle = title;
    }

    @Override
    public void allowed(boolean res) {
        lostFocus = res;
    }

    @Override
    public void processBackEvent() {
        backEvent();
    }


    public View getParentLayout() {
        return parent;
    }

    public void setReportFrag(ReportFragment reportFrag) {
        this.reportFrag = reportFrag;
    }
}
