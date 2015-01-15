package fi.immonen.kalle.rgbstormer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import fi.immonen.kalle.rgbstormer.bluetooth.BluetoothConnector;
import fi.immonen.kalle.rgbstormer.bluetooth.BluetoothUtils;
import fi.immonen.kalle.rgbstormer.rgb.ByteUtil;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        Fragments.PlaceholderFragment.OnFragmentInteractionListener,
        BluetoothSelectDialogFragment.BluetoothActions {

    private static final int REQUEST_ENABLE_BT = 12;
    public static final byte SET_STEPS = (byte) 0xFC;
    public static final byte SET_DELAY = (byte) 0xFB;
    public static final byte SET_FADE = (byte) 0xFA;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private BluetoothConnector mBluetoothConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        bluetoothPopUpIfNotOn();

    }

    private void bluetoothPopUpIfNotOn() {
        if (BluetoothUtils.initBluetooth()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            default:
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container, Fragments.PlaceholderFragment.newInstance(position + 1))
                        .commit();

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_control);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onColorSelected(int color) {
        System.out.println("Color found" + color);
        if (mBluetoothConnector != null) {
            byte[] bytes = ByteUtil.hexStringToByteArray(Integer.toHexString(color));

            mBluetoothConnector.write(ByteUtil.invertAllButFirstByte(bytes));
        }
    }

    @Override
    public void buttonClicked() {
        showBluetoothConnectionDialog();
    }

    @Override
    public void onModeActivated(int mode) {
        if (mBluetoothConnector != null) {
            byte[] bytes = {(byte) mode};
            mBluetoothConnector.write(bytes);
        }
    }

    @Override
    public void onFadeSet(int fade) {
        if (mBluetoothConnector != null) {
            byte[] bytes = {SET_FADE, (byte) fade, (byte) fade};
            mBluetoothConnector.write(bytes);
        }
    }

    @Override
    public void onDelaySet(int delay) {
        if (mBluetoothConnector != null) {
            byte[] bytes = {SET_DELAY, (byte) delay, (byte) delay};
            mBluetoothConnector.write(bytes);
        }
    }

    @Override
    public void onStepsSet(int steps) {
        if (mBluetoothConnector != null) {
            byte[] bytes = {SET_STEPS, (byte) steps, (byte) steps};
            mBluetoothConnector.write(bytes);
        }
    }

    public void showBluetoothConnectionDialog() {
        // Create an instance of the dialog fragment and show it
        BluetoothSelectDialogFragment bluetoothDialog = new BluetoothSelectDialogFragment();
        bluetoothDialog.show(getFragmentManager(), "BluetoothSelectDialogFragment");
    }

    @Override
    public void onSelectedBluetoothDevice(BluetoothDevice device) {
        mBluetoothConnector = new BluetoothConnector(device, mHandler);
        mBluetoothConnector.connect();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_TOAST:
                    break;

                case Constants.MESSAGE_READ:
                    break;

                case Constants.MESSAGE_WRITE:
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    break;

                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Constants.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        case Constants.STATE_CONNECTING:
                            Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_SHORT).show();
                        case Constants.STATE_DISCONNECTED:
                            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
