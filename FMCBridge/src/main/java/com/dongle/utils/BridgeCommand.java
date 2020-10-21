package com.dongle.utils;

import java.util.HashMap;

public class BridgeCommand {
	public BridgeCommand() {}
	public BridgeCommand(String command, HashMap<Object,Object> params) {
		command_type = command;
		command_body = params;
	}
	public String command_type;
	public HashMap<Object,Object> command_body;
}
