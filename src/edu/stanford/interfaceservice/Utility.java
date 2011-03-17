package edu.stanford.interfaceservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;


public class Utility {
	public final static String TAG = "Utility";
	public static final int MSG_REPORT_INTFACE_STATE = 0; 
	public static final int MSG_UPDATE_TXTVIEW = 1; 
	
	public static ArrayList<String> readLinesFromFile(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		File file = new File(filename);
		if (!file.canRead()) {
			Log.d(TAG, filename + "can not be read");
			return lines;
		}
		BufferedReader buffer = null;
        try {
			buffer = new BufferedReader(new FileReader(file), 8192);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
		try {
			String line = null;
			while ((line = buffer.readLine()) != null) {
				lines.add(line.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
		}
		finally {
			try {
				buffer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.getMessage());
			}
		}
		return lines;
	}
	
	public static long[] readTraffic(String device) {
		long[] traffic = {0, 0};
		for (String line : Utility.readLinesFromFile("/proc/net/dev")) {
			if (!line.startsWith(device)) continue;
			String[] elems = line.split(" +");
			traffic[0] = Long.parseLong(elems[1]);
			traffic[1] = Long.parseLong(elems[9]);
		}
		return traffic;
	}
	
}