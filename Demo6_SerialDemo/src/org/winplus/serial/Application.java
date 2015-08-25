package org.winplus.serial;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.winplus.serial.utils.SerialPort;
import org.winplus.serial.utils.SerialPortFinder;

import android.content.SharedPreferences;

public class Application extends android.app.Application {
	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
            if (mSerialPort == null) {
                    /* Read serial port parameters */
                    SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
                    String path = sp.getString("DEVICE", "");
                    int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

                    /* Check parameters */
                    if ( (path.length() == 0) || (baudrate == -1)) {
                            throw new InvalidParameterException();
                    }

                    /* Open the serial port */
                    mSerialPort = new SerialPort(new File(path), baudrate, 0);
            }
            return mSerialPort;
    }

    public void closeSerialPort() {
            if (mSerialPort != null) {
                    mSerialPort.close();
                    mSerialPort = null;
            }
    }
}
