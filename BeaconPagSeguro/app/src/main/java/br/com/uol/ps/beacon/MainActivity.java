package br.com.uol.ps.beacon;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.uol.ps.beacon.components.NavComponent;
import br.com.uol.ps.beacon.fragments.PromoListFragment;
import br.com.uol.ps.beacon.mocks.CardViewPopulateMock;
import br.com.uol.ps.beacon.services.AlarmSetup;

/**
 * @author Jean Rodrigo Dalbon Cunha
 *         <p/>
 *         MainActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Nav Component
     */
    private NavComponent navComponent;

    private void initComponents() {
        navComponent = new NavComponent(this);
        CardViewPopulateMock.initializeMockActivity(getApplicationContext());
        AlarmSetup.startAlarmIntent(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            PromoListFragment promoListFragment = new PromoListFragment();
            promoListFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, promoListFragment, PromoListFragment.TAG).commit();
        }

    }

    public NavComponent getNavComponent() {
        return navComponent;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}