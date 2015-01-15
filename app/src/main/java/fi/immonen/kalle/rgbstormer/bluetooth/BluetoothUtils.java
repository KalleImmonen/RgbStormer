package fi.immonen.kalle.rgbstormer.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by TeZla on 15.1.2015.
 */
public class BluetoothUtils {
    private static BluetoothAdapter mBluetoothAdapter;

    public static boolean initBluetooth() {
        boolean state = false;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            System.out.println("Bluetooth is Disable...");
            state = true;
        } else if (mBluetoothAdapter.isEnabled()) {
            String address = mBluetoothAdapter.getAddress();
            String name = mBluetoothAdapter.getName();
            System.out.println(String.format("Bluetooth name: %s. Address: %s", name, address));
            state = false;
        }
        return state;
    }

    public static Set<BluetoothDevice> getPairedDevices() {
        if (mBluetoothAdapter == null) {
            initBluetooth();
        }
        return mBluetoothAdapter.getBondedDevices();
    }

    public static BluetoothSocket createRfcommSocket(BluetoothDevice device) {

        BluetoothSocket bluetoothSocket = null;
        try {
            Class deviceClass = device.getClass();
            Class aClass[] = new Class[1];
            aClass[0] = Integer.TYPE;
            Method createSocketMethod = deviceClass.getMethod("createRfcommSocket", aClass);
            Object aObject[] = new Object[1];
            aObject[0] = Integer.valueOf(1);
            bluetoothSocket = (BluetoothSocket) createSocketMethod.invoke(device, aObject);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return bluetoothSocket;

    }
}
