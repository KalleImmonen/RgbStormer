package fi.immonen.kalle.rgbstormer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Set;

import fi.immonen.kalle.rgbstormer.bluetooth.BluetoothService;
import fi.immonen.kalle.rgbstormer.bluetooth.Connections;

/**
 * Created by TeZla on 8.1.2015.
 */
public class BluetoothSelectDialogFragment extends DialogFragment {
    BluetoothActions mBluetoothActions;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setInverseBackgroundForced(false);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> pairedDevices = Connections.getPairedDevices();
        final BluetoothDevice[] btDevices = pairedDevices.toArray(new BluetoothDevice[pairedDevices.size()]);

        for (BluetoothDevice device : btDevices) {
            adapter.add(device.getName() + "\n" + device.getAddress());
        }
        builder.setTitle(R.string.pickBluetooth).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemPosition) {
                Toast.makeText(getActivity().getApplicationContext(), "Clicked!" + itemPosition + adapter.getItem(itemPosition), Toast.LENGTH_SHORT).show();
                mBluetoothActions.onSelectedBluetoothDevice(btDevices[itemPosition]);
            }
        });
        return builder.create();
    }

    @Override
    public void onStart() {
        setupBluetooth();
        super.onStart();

    }

    private void setupBluetooth() {
        mBluetoothService = new BluetoothService(getActivity().getApplicationContext(), mHandler);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mBluetoothActions = (BluetoothActions) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implment OnFragmentIntecratcioListneter!");
        }
    }

    public interface BluetoothActions {
        public void onSelectedBluetoothDevice(BluetoothDevice device);
    }


}
