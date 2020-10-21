package com.dongle.utils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.dongle.FMCBridge.FMCBridge;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class SocketThreadInput implements Runnable {

	private Thread t;
	private String threadName;
	private InputStream reader;
	private Gson json_parser;
	private boolean exit = false;
	private Boolean get_data = false;
	
	public SocketThreadInput(String name, InputStream _reader){
		threadName = name;
		reader = _reader;
		json_parser = new Gson();
	}
	
	@Override
	public void run() {
		while(!exit) {
			
			synchronized(get_data) {
				while(!get_data) {
					try {
						//hold the thread until data is available to process
						get_data.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String json = Recieve(reader);
				if(json.length() > 0) {
					JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
					JsonElement rec_type = jsonObject.get("rec_type");
					JsonElement rec_objects = jsonObject.get("rec_objects");
					processCommand(rec_type.getAsString(),rec_objects);
					get_data = false;
				}
			}
		}
	}
	
	public void processCommand(String bc, JsonElement rec_objects) {
		switch (bc) {
			case "rec_item":
				break;
			case "item":
				get_items(rec_objects);
				break;
			case "rec_power":
				break;
			case "rec_fluid":
				break;
			default:
				break;
		}
	}
	
	public void get_items(JsonElement rec_objects) {
		Type listType = new TypeToken<List<MinecraftItem>>() {}.getType();
		ArrayList<MinecraftItem> items = json_parser.fromJson(rec_objects,listType);
		if(items.size() > 0) {
			FMCBridge.instance.receieveManager.receive(items);
		}
	}
	
	public void start() {
		if(t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}
	
	public void stop() {
		exit = true;
	}
	
	public void try_receive(boolean _get_data) {
		synchronized(get_data) {
			get_data.notifyAll();
			get_data = _get_data;
		}
	}
	
	public String Recieve(InputStream in) {
		String retString = "";
		try {
			byte[] recLength = new byte[4];
			in.read(recLength,0,4);
			
			int len = ByteBuffer.wrap(recLength).getInt();
			
			byte[] recBytes = new byte[len];
			in.read(recBytes, 0,len);
			retString = new String(recBytes,StandardCharsets.UTF_8);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
		return retString;
	}

}
