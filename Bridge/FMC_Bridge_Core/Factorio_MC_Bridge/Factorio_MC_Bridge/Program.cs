using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Xml;
using System.IO;
using System.ComponentModel;
using System.Diagnostics;
using System.Net;
using CoreRCON;
using CoreRCON.Parsers.Standard;
using Newtonsoft.Json;
using System.Threading.Tasks;
using System.Net.Sockets;

namespace Factorio_MC_Bridge
{
	class Program {
		static bool exit_threads = false;
		static List<MinecraftItem> minecraftItemsToSend;
		static bool holdMCSend = false;
		static bool holdFSend = false;
		static bool holdFReceive = false;
		//static void Main(string[] args)
		//{
		//	/*
		//		Input and checking for settings file.
		//		This should probably not immedantly check but prompt the user with the current settings and if they would like to change them
		//	*/
		//	Console.WriteLine("Starting Up!");
		//	Console.WriteLine("To change settings, enter 1, otherwise press any key other to continue.");
		//	string choice = Console.ReadLine();
		//	Settings settings = new Settings();
		//	string startupDoc = Path.Combine(Environment.CurrentDirectory, "settings.json");
		//	if (!File.Exists(startupDoc) || choice.Equals("1"))
		//	{
		//		FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Write, FileShare.ReadWrite);
		//		StreamWriter sw = new StreamWriter(fs, Encoding.Default);
		//		Console.WriteLine("Please enter Minecraft Location (Root of the Directory): ");
		//		settings.setMcPath(Console.ReadLine());
		//		Console.WriteLine("Please enter Factorio Server Path (Root of the Directory): ");
		//		settings.setFacotrioPath(Console.ReadLine());
		//		Console.WriteLine("Please enter the IP Address of the Factorio Server: ");
		//		settings.setIpAddress(Console.ReadLine());
		//		Console.WriteLine("Please enter RCON Port Number: ");
		//		settings.setPort(Int32.Parse(Console.ReadLine()));
		//		Console.WriteLine("Please enter RCON password: ");
		//		settings.setRconPass(Console.ReadLine());
		//		string output = JsonConvert.SerializeObject(settings);
		//		sw.WriteLine(output);
		//		sw.Close();
		//	}
		//	else
		//	{
		//		FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
		//		StreamReader sr = new StreamReader(fs, Encoding.Default);
		//		string input = "";
		//		while (!sr.EndOfStream) {
		//			input += sr.ReadLine();
		//		}
		//		settings = JsonConvert.DeserializeObject<Settings>(input);
		//	}

		//	/*
		//		Load in the item mappings file.  
		//	*/
		//	DualDictionary<String, String> itemMappings = new DualDictionary<String, String>();
		//	Dictionary<String, double> minecraftRatios = new Dictionary<String, double>();
		//	Dictionary<String, double> factorioRatios = new Dictionary<String, double>();

		//	//Open up the file stream for the item mappings
		//	string itemMappingsPath = Path.Combine(Environment.CurrentDirectory, "item_mappings.txt");
		//	FileStream fileStream = new FileStream(itemMappingsPath, FileMode.OpenOrCreate, FileAccess.Read, FileShare.Read);
		//	StreamReader streamReader = new StreamReader(fileStream, Encoding.Default);

		//	//This loops needs to do a couple of things. 
		//	//The first is it needs to read in the mappings into the DualDictionary for better translation of names.
		//	//The second it needs to bind the item ratios to their respective lists.
		//	while (!streamReader.EndOfStream) {
		//		//Item Name Mappings first
		//		String readString = streamReader.ReadLine();
		//		if (readString.Contains("#") || readString.Equals("") || readString.Equals("\n")) {
		//			continue;
		//		}
		//		String[] split = readString.Split('=');
		//		itemMappings.Add(split[0], split[1]);
		//		//Split the string again to get the ratios
		//		if (split.Length > 2) {
		//			String[] ratios = split[2].Split(':');
		//			minecraftRatios.Add(split[0], Double.Parse(ratios[0]));
		//			factorioRatios.Add(split[1], Double.Parse(ratios[1]));
		//		}
		//	}
		//	streamReader.Close();
		//	fileStream.Close();

		//	/*
		//		Open RCON Connection to factorio server
		//		Parse the files and send the data to the approiate game.
		//	*/
		//	Console.WriteLine("Found settings. Beginning transfer.");
		//	var rcon = new RCON(IPAddress.Parse(settings.getIPAddress()), (ushort)settings.getPort(), settings.getRconPass() );
		//	while (true)
		//	{
		//		try
		//		{
		//			List<ItemPair> factorioItems = parseFactrio(settings, itemMappings, factorioRatios);
		//			List<ItemPair> minecraftItems = parseMinecraft(settings, itemMappings, minecraftRatios);
		//			sendToFactorio(minecraftItems, rcon);
		//			sendToMinecraft(factorioItems, settings);
		//			Thread.Sleep(1000);
		//		}
		//		catch (Exception e) {
		//			Console.WriteLine("Something went wrong. Moving past error.");
		//			Console.WriteLine(e.Message);
		//			continue;
		//		}
		//	}
		//}
		static async Task Main(string[] args) {

			Thread minecraftThread = new Thread(MinecraftThread);
			Thread factorioThread = new Thread(FactorioThread);
			minecraftThread.Start();
			factorioThread.Start();
			
			while (true) {
				//Hold for the rest of the application
				string line = Console.ReadLine();
				if (line.Equals("quit")) {
					exit_threads = true;
					break;
				}
				switch (line) {
					case "send_mc":
						holdMCSend = true;
						minecraftItemsToSend.Add(new MinecraftItem("minecraft:stone", 64, 0, null));
						minecraftItemsToSend.Add(new MinecraftItem("minecraft:wool", 15, 3, null));
						holdMCSend = false;
						break;
					case "send_f":
						holdFSend = true;
						break;
					case "receive_r":
						holdFReceive = true;
						break;
					default:
						break;
				}				
			}
			minecraftThread.Join();
			factorioThread.Join();
			Console.WriteLine("Stopping.");

			#region "minecraft main"
			//bool minecraft_loggedin = false;
			//minecraftItemsToSend = new List<MinecraftItem>();

			//Console.WriteLine("Connected");
			//IPEndPoint endPoint = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 25575);
			//Socket s = new Socket(endPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
			//try {
			//	s.Bind(endPoint);
			//	s.Listen(10);
			//	Socket handler = s.Accept();
			//	//Get data
			//	string json = Receive(handler);
			//	Console.WriteLine(json);

			//	//Send response
			//	Dictionary<object, object> command_body = new Dictionary<object, object>();
			//	command_body.Add("result", "true");
			//	BridgeCommand bc = new BridgeCommand("login", command_body);
			//	Send(handler, bc);
			//	minecraft_loggedin = true;
			//	if (handler.Connected && minecraft_loggedin) {
			//		Thread outputThread = new Thread(OutputThread);
			//		Thread inputThread = new Thread(InputThread);
			//		outputThread.Start(handler);
			//		inputThread.Start(handler);
			//	}
			//	while (true) {
			//		//Hold for the rest of the application
			//		string line = Console.ReadLine();
			//		if (line.Equals("quit")) {
			//			break;
			//		}

			//		if (line.Equals("send")) {
			//			holdMCSend = true;
			//			minecraftItemsToSend.Add(new MinecraftItem("minecraft:stone", 64, 0, null));
			//			minecraftItemsToSend.Add(new MinecraftItem("minecraft:wool", 15, 3, null));
			//			holdMCSend = false;
			//		}
			//	}
			//}
			//catch (Exception e) {
			//	Console.WriteLine(e);
			//}
			#endregion

			#region "factorio main"
			//RCON rcon = new RCON(IPAddress.Parse("127.0.0.1"), 25555, "password");
			//await rcon.ConnectAsync();
			//bool connected = true;

			//string getItems = await rcon.SendCommandAsync(@"/silent-command remote.call(""testRCON"",""testRCON"")");
			//Console.WriteLine(getItems);

			//List<factorioItem> factorioItems = new List<factorioItem>();
			//factorioItems = JsonConvert.DeserializeObject<List<factorioItem>>(getItems);
			//StringBuilder strBuilder = new StringBuilder();


			//foreach (factorioItem f in factorioItems) {
			//	Console.WriteLine(f.name + " " + f.count);
			//	strBuilder.Append("{\"" + f.name + "\":" + f.count + "},");
			//}

			//strBuilder.Remove(strBuilder.Length - 1, 1);

			//List<factorioItem> factorioItems = new List<factorioItem>();
			//var factorioItem1 = new factorioItem("transport-belt", 10);
			//var factorioItem2 = new factorioItem("wooden-chest", 100);
			//Dictionary<string, int> factorioItems = new Dictionary<string, int>();
			//factorioItems.Add(factorioItem1.name, factorioItem1.count);
			//factorioItems.Add(factorioItem2.name, factorioItem2.count);

			//string s = JsonConvert.SerializeObject(factorioItems);
			//Console.WriteLine(s);
			//string sendItems = await rcon.SendCommandAsync(string.Format(@"/command remote.call(""receiveItems"",""inputItems"",'{0}')", s));
			#endregion
		}

		#region "Threads"
		public static void MinecraftThread() {
			bool minecraft_loggedin = false;
			minecraftItemsToSend = new List<MinecraftItem>();

			IPEndPoint endPoint = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 25575);
			Socket s = new Socket(endPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
			try {
				s.Bind(endPoint);
				s.Listen(10);
				Socket handler = s.Accept();
				//Get data
				string json = Receive(handler);
				Console.WriteLine(json);

				//Send response
				Dictionary<object, object> command_body = new Dictionary<object, object>();
				command_body.Add("result", "true");
				BridgeCommand bc = new BridgeCommand("login", command_body);
				Send(handler, bc);
				minecraft_loggedin = true;
				if (handler.Connected && minecraft_loggedin) {
					Thread outputThread = new Thread(OutputThread);
					Thread inputThread = new Thread(InputThread);
					outputThread.Start(handler);
					inputThread.Start(handler);
				}
			}
			catch (Exception e) {
				Console.WriteLine(e);
			}
		}

		public static async void FactorioThread() {
			RCON rcon = new RCON(IPAddress.Parse("127.0.0.1"), 25555, "password");
			while (true) {
				try {
					await rcon.ConnectAsync();
					break;
				}
				catch {
					Thread.Sleep(1000);
				}
			}

			bool connected = true;

			while (true){
				if (holdFSend) {
					holdFSend = false;
					string getdata = await rcon.SendCommandAsync(@"/silent-command remote.call(""send_items"",""send_items"")");
					Dictionary<string, List<factorioItem>> pairs = new Dictionary<string, List<factorioItem>>();
					pairs = JsonConvert.DeserializeObject<Dictionary<string, List<factorioItem>>>(getdata);
					Console.WriteLine(getdata);
				}
				if (holdFReceive) {
					holdFReceive = false;
					var factorioItem1 = new factorioItem("transport-belt", 10);
					var factorioItem2 = new factorioItem("wooden-chest", 100);
					var factorioFluid1 = new factorioItem("water", 15000);
					var factorioFluid2 = new factorioItem("steam", 25000);
					var factorioEnergy1 = new factorioItem("energy", 111111);

					Dictionary<string, int> factorioItems = new Dictionary<string, int>();
					factorioItems.Add(factorioItem1.name, factorioItem1.count);
					factorioItems.Add(factorioItem2.name, factorioItem2.count);

					Dictionary<string, int> factorioFluids = new Dictionary<string, int>();
					factorioFluids.Add(factorioFluid1.name, factorioFluid1.count);
					factorioFluids.Add(factorioFluid2.name, factorioFluid2.count);

					Dictionary<string, int> factorioEnergy = new Dictionary<string, int>();
					factorioEnergy.Add(factorioEnergy1.name, factorioEnergy1.count);

					string items = JsonConvert.SerializeObject(factorioItems);
					string fluids = JsonConvert.SerializeObject(factorioFluids);
					string energy = JsonConvert.SerializeObject(factorioEnergy);

					Console.WriteLine(items);
					Console.WriteLine(fluids);
					Console.WriteLine(energy);

					string sendItems = await rcon.SendCommandAsync(string.Format(@"/command remote.call(""receive_items"",""inputItems"",'{0}')", items));
					string sendFluids = await rcon.SendCommandAsync(string.Format(@"/silent-command remote.call(""receive_fluids"",""inputFluids"",'{0}')", fluids));
					string sendEnergy = await rcon.SendCommandAsync(string.Format(@"/silent-command remote.call(""receive_energy"",""inputEnergy"",'{0}')", energy));
				}
			}
		}

		public static void OutputThread(Object socket) {
			while (!exit_threads) {
				if (minecraftItemsToSend.Count > 0 && !holdMCSend) {
					Socket s = (Socket)socket;
					string command = "get_items";
					string rec_type = "item";
					List<Object> item_list = new List<Object>();
					item_list.AddRange(minecraftItemsToSend);
					MinecraftObject mco = new MinecraftObject(command, rec_type, item_list);
					Send(s, mco);
					minecraftItemsToSend.Clear();
				}
			}
		}

		public static void InputThread(Object socket) {
			while (!exit_threads) {
				Socket s = (Socket)socket;
				string json = Receive(s);
				List<MinecraftItem> items = JsonConvert.DeserializeObject<List<MinecraftItem>>(json);
				for (int i = 0; i < items.Count; i++) {
					Console.WriteLine(items[i].itemName + ":" + items[i].metadata + " " + items[i].count);
				}
			}
		}

		#endregion
		#region " Socket Functions "
		public static string Receive(Socket socket) {
			int length = 0;
			byte[] sizeBuffer = new byte[2];
			socket.Receive(sizeBuffer);
			for (int i = 0; i < sizeBuffer.Length; i++) {
				length = length << 8;
				length += sizeBuffer[i];
			}
			byte[] stringBuffer = new byte[length];
			int received_amount = socket.Receive(stringBuffer);
			string json = "";
			if (stringBuffer.Length == received_amount) {
				json = Encoding.UTF8.GetString(stringBuffer, 0, stringBuffer.Length);
			}
			return json;
		}

		public static void Send(Socket socket, Object json_object ) {
			string send_string = JsonConvert.SerializeObject(json_object);
			int send_data_len = Encoding.UTF8.GetByteCount(send_string);
			byte[] send_data = Encoding.UTF8.GetBytes(send_string);
			byte[] send_data_length = BitConverter.GetBytes(send_data_len);

			if (BitConverter.IsLittleEndian) {
				Array.Reverse(send_data_length);
			}

			socket.Send(send_data_length);
			socket.Send(send_data);
		}


		#endregion

		#region " Factorio "
		/// FACTORIO
		/// Reading and parsing for factorio 
		/// FACTORIO
		public static async void sendToFactorio(List<ItemPair> items, RCON rcon) {
			if (items.Count > 0) {
				for (int i = 0; i < items.Count; i++) {
					StringBuilder str = new StringBuilder();
					String cmd = "";
					if (items[i].count > 100) {
						while (items[i].count > 100) {
							str.Append(@"/silent-command remote.call(""receiveItems"",""inputItems"",");
							str.Append(@"""");
							str.Append(items[i].name);
							str.Append(@""",");
							str.Append(100);
							str.Append(")");
							cmd = await rcon.SendCommandAsync(str.ToString());
							items[i].count -= 100;
							str.Clear();
						}
					}
					str.Append(@"/silent-command remote.call(""receiveItems"",""inputItems"",");
					str.Append(@"""");
					str.Append(items[i].name);
					str.Append(@""",");
					str.Append(items[i].count.ToString());
					str.Append(")");
					cmd = await rcon.SendCommandAsync(str.ToString());
					str.Clear();
				}
			}
		}

		public static List<ItemPair> parseFactrio(Settings settings, DualDictionary<String, String> mappings, Dictionary<String, double> ratios)
		{
			List<ItemPair> items = new List<ItemPair>();
			String fullPath = "";
			FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.ReadWrite, FileShare.ReadWrite);
			StreamReader sr = new StreamReader(fs, Encoding.Default);
			while (!sr.EndOfStream){
				string proc = sr.ReadLine();
				string[] temp = proc.Split(':');
				int count = 0;
				if (ratios.Count > 0) {
					double readNum = Double.Parse(temp[1]) * ratios[temp[0]]; ;
					count = (int)Math.Round(readNum, MidpointRounding.AwayFromZero);
				}
				else {
					count = (int)Double.Parse(temp[1]);
				}
				int containsTest = pairContains(items, temp[0]);

				if (containsTest != -1){
					if (items[containsTest].count < 64) {
						int remainder = 64 - items[containsTest].count;
						if (remainder > 0) {
							items[containsTest].count += remainder;
							count -= remainder;
						}
					}
					if ((64 - count) < 0) {
						items.Add(new ItemPair(temp[0], 64));
						int remain = Math.Abs(64 - count);
						while (remain > 0) {
							if (remain > 64) {
								items.Add(new ItemPair(temp[0], 64));
								remain -= 64;
							}
							else {
								items.Add(new ItemPair(temp[0], remain));
								remain -= 64;
							}
						}
					}
					else {
						items.Add(new ItemPair(temp[0], Int32.Parse(temp[1])));
					}
				}
				else
				{
					if ((64 - count) < 0) {
						items.Add(new ItemPair(temp[0], 64));
						int remain = Math.Abs(64 - count);
						while (remain > 0) {
							if (remain > 64) {
								items.Add(new ItemPair(temp[0], 64));
								remain -= 64;
							}
							else {
								items.Add(new ItemPair(temp[0], remain));
								remain -= 64;
							}
						}
					}
					else {
						items.Add(new ItemPair(temp[0], Int32.Parse(temp[1])));
					}
					//items.Add(new ItemPair(temp[0], Int32.Parse(temp[1])));
				}
				StreamWriter sw = new StreamWriter(fs);
				sw.WriteLine("");
			}


			//Remap Items to the opposing item
			for (int i = 0; i < items.Count; i++)
			{
				items[i].name = mappings.facotrio[items[i].name];
			}
			return items;
		}
		#endregion

		#region " Minecraft "
		/// MINECRAFT
		/// Reading and Parsing for minecraft 
		/// MINECRAFT

		public static List<ItemPair> parseMinecraft(Settings settings, DualDictionary<String,String> mappings, Dictionary<String, double> ratios)
		{
			List<ItemPair> items = new List<ItemPair>();
			String fullPath = "";
			while (true)
			{
				try
				{
					using (FileStream Fs = new FileStream(fullPath, FileMode.Open, FileAccess.ReadWrite, FileShare.None, 100))
					{
						break;
					}
				}
				catch (IOException)
				{
					Thread.Sleep(100);
				}
			}
			FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.ReadWrite, FileShare.ReadWrite);
			StreamReader sr = new StreamReader(fs, Encoding.Default);
			while (!sr.EndOfStream)
			{
				string proc = sr.ReadLine();
				string[] temp = proc.Split('~');
				int count = 0;
				if (ratios.Count > 0) {
					double readNum = Double.Parse(temp[1]) * ratios[temp[0]]; ;
					count = (int)Math.Round(readNum, MidpointRounding.AwayFromZero);
				}
				else {
					count = (int)Double.Parse(temp[1]);
				}
				items.Add(new ItemPair(temp[0], count));
			}
			sr.Close();
			File.WriteAllText(fullPath, string.Empty);

			//Remap Items to the opposing item
			for (int i = 0; i < items.Count; i++) {
				items[i].name = mappings.minecraft[items[i].name];
			}
			return items;
		}
		
		public static void sendToMinecraft(List<ItemPair> items, Settings settings) {
			String fullPath = "";

			while (true)
			{
				try
				{
					using (FileStream Fs = new FileStream(fullPath, FileMode.Open, FileAccess.ReadWrite, FileShare.None, 100))
					{
						break;
					}
				}
				catch (IOException)
				{
					Thread.Sleep(100);
				}
			}
			StreamWriter sw = new StreamWriter(fullPath, true);
			for (int i = 0; i < items.Count; i++) {
				String itemToSend = items[i].name + "~" + items[i].count;
				sw.WriteLine(itemToSend);
			}
			sw.Close();
		}

		#endregion

		#region " Utility Functions "

		/// 
		/// The Pair Contains function used for parsing
		/// 
		public static int pairContains(List<ItemPair> list, string itemName) {
			for (int i = 0; i < list.Count(); i++) {
				if (list[i].name.Equals(itemName)) {
					return i;
				}
			}
			return -1;
		}

		public struct factorioItem {
			public factorioItem(string _name, int _count) {
				this.name = _name;
				this.count = _count;
			}
			public string name;
			public int count;
		}

		public struct BridgeCommand {
			public string command_type;
			public Dictionary<object, object> command_body;
			public BridgeCommand(string command, Dictionary<object, object> body) {
				command_type = command;
				command_body = body;
			}
		}

		//public struct MinecraftObject <T> {
		//	string command_name;
		//	string rec_type;
		//	List<T> rec_objects;

		//	public MinecraftObject(string _command_name, string _rec_type, List<T> objects) {
		//		command_name = _command_name;
		//		rec_type = _rec_type;
		//		rec_objects = objects;
		//	}
		//}

		#endregion
	}
}


/*
 * 
 * 			MinecraftItem m = new MinecraftItem("test", 5, 0, null);
			string command = "get_items";
			string rec_type = "item";
			List<Object> item_list = new List<Object>();
			item_list.Add(m);

			MinecraftObject mco = new MinecraftObject(command, rec_type, item_list);

			string json = JsonConvert.SerializeObject(mco);
			Console.WriteLine(json);
*/