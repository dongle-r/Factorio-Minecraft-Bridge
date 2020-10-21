package com.dongle.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import net.minecraft.item.ItemStack;

public class SocketThreadOutput implements Runnable {

	private Thread t;
	private String threadName;
	private DataOutputStream writer;
	private boolean exit = false;
	private Gson json_parser;
	ArrayList<MinecraftItem> items = new ArrayList<MinecraftItem>();
	
	public SocketThreadOutput(String name, DataOutputStream _writer){
		threadName = name;
		writer = _writer;
		json_parser = new Gson();
		items = new ArrayList<MinecraftItem>();
	}
	
	@Override
	public void run() {
		while(!exit) {
			synchronized(items) {
				while(items.isEmpty()) {
					try {
						items.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					if(items.size() > 0) {
						writer.writeUTF(json_parser.toJson(items));
						items.clear();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
	
	public void set_items(ArrayList<MinecraftItem> _items) {
		synchronized(items) {
			items.notifyAll();
			items = _items;
		}
	}

}
