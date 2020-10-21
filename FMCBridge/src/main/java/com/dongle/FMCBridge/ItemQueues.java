package com.dongle.FMCBridge;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import com.dongle.utils.BridgeCommand;
import com.dongle.utils.SocketThreadInput;
import com.dongle.utils.SocketThreadOutput;
import com.google.gson.Gson;

public class ItemQueues {

	public Socket networkQueue;
	InputStream input;
	DataOutputStream output;
	boolean logged_in = false;
	boolean connected = false;
	public SocketThreadInput threadInput;
	public SocketThreadOutput threadOutput;
	
	public ItemQueues() {
		try {
			if(FMCBridge.instance.queues_logged_in) {
				return;
			}
			
			networkQueue = 	new Socket("127.0.0.1", 25575);
			connected = networkQueue.isConnected();
			
			if(connected) {
				input = networkQueue.getInputStream();
				output = new DataOutputStream(networkQueue.getOutputStream());
				logged_in = login(input, output);
				if(logged_in) {
					FMCBridge.instance.queues_logged_in = true;
					System.out.println("Logged into ");
					StartThreads();
				}
			}

		}
		catch (Exception e) {
			
		}
	}
	
	public <T, K> boolean login(InputStream input, DataOutputStream output) {

		boolean _logged_in = false;
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put("username", Config.username);
		params.put("password", Config.password);;
		BridgeCommand bc = new BridgeCommand("login", params);
		
		Gson json_parser = new Gson();
		try {
			output.writeUTF(json_parser.toJson(bc));
			while(true) {
				String data = Recieve(input);
				BridgeCommand result = new BridgeCommand();
				result = json_parser.fromJson(data, result.getClass());
				if(result.command_type.equals("login")) {
					_logged_in = Boolean.parseBoolean((String)result.command_body.get("result"));
					return _logged_in;
				}
				break;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return _logged_in;
	}
	
	public void StartThreads() {
		threadInput = new SocketThreadInput("InputThread", input);
		threadOutput = new SocketThreadOutput("OutputThread", output);
		threadInput.start();
		threadOutput.start();
		try {
			//Start both threads and have them wait.
			threadInput.wait();
			threadOutput.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
