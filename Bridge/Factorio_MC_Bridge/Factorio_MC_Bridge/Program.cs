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

namespace Factorio_MC_Bridge
{
	class Program
	{
		static void Main(string[] args)
		{
			/*
				Input and checking for settings file.
				This should probably not immedantly check but prompt the user with the current settings and if they would like to change them
			*/
			Console.WriteLine("Starting Up!");
			Console.WriteLine("To change settings, enter 1, otherwise press any key other to continue.");
			string choice = Console.ReadLine();
			Settings settings = new Settings();
			string startupDoc = Path.Combine(Environment.CurrentDirectory, "settings.json");
			if (!File.Exists(startupDoc) || choice.Equals("1"))
			{
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Write, FileShare.ReadWrite);
				StreamWriter sw = new StreamWriter(fs, Encoding.Default);
				Console.WriteLine("Please enter Minecraft Location (Root of the Directory): ");
				settings.setMcPath(Console.ReadLine());
				Console.WriteLine("Please enter Factorio Server Path (Root of the Directory): ");
				settings.setFacotrioPath(Console.ReadLine());
				Console.WriteLine("Please enter the IP Address of the Factorio Server: ");
				settings.setIpAddress(Console.ReadLine());
				Console.WriteLine("Please enter RCON Port Number: ");
				settings.setPort(Int32.Parse(Console.ReadLine()));
				Console.WriteLine("Please enter RCON password: ");
				settings.setRconPass(Console.ReadLine());
				string output = JsonConvert.SerializeObject(settings);
				sw.WriteLine(output);
				sw.Close();
			}
			else
			{
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
				StreamReader sr = new StreamReader(fs, Encoding.Default);
				string input = "";
				while (!sr.EndOfStream) {
					input += sr.ReadLine();
				}
				settings = JsonConvert.DeserializeObject<Settings>(input);
			}

			/*
				Load in the item mappings file.  
			*/
			DualDictionary<String, String> itemMappings = new DualDictionary<String, String>();
			Dictionary<String, double> minecraftRatios = new Dictionary<String, double>();
			Dictionary<String, double> factorioRatios = new Dictionary<String, double>();

			//Open up the file stream for the item mappings
			string itemMappingsPath = Path.Combine(Environment.CurrentDirectory, "item_mappings.txt");
			FileStream fileStream = new FileStream(itemMappingsPath, FileMode.OpenOrCreate, FileAccess.Read, FileShare.Read);
			StreamReader streamReader = new StreamReader(fileStream, Encoding.Default);

			//This loops needs to do a couple of things. 
			//The first is it needs to read in the mappings into the DualDictionary for better translation of names.
			//The second it needs to bind the item ratios to their respective lists.
			while (!streamReader.EndOfStream) {
				//Item Name Mappings first
				String readString = streamReader.ReadLine();
				if (readString.Contains("#") || readString.Equals("") || readString.Equals("\n")) {
					continue;
				}
				String[] split = readString.Split('=');
				itemMappings.Add(split[0], split[1]);
				//Split the string again to get the ratios
				if (split.Length > 2) {
					String[] ratios = split[2].Split(':');
					minecraftRatios.Add(split[0], Double.Parse(ratios[0]));
					factorioRatios.Add(split[1], Double.Parse(ratios[1]));
				}
			}
			streamReader.Close();
			fileStream.Close();
			
			/*
				Open RCON Connection to factorio server
				Parse the files and send the data to the approiate game.
			*/
			Console.WriteLine("Found settings. Beginning transfer.");
			var rcon = new RCON(IPAddress.Parse(settings.getIPAddress()), (ushort)settings.getPort(), settings.getRconPass() );
			while (true)
			{
				try
				{
					List<ItemPair> factorioItems = parseFactrio(settings, itemMappings, factorioRatios);
					List<ItemPair> minecraftItems = parseMinecraft(settings, itemMappings, minecraftRatios);
					sendToFactorio(minecraftItems, rcon);
					sendToMinecraft(factorioItems, settings);
					Thread.Sleep(1000);
				}
				catch (Exception e) {
					Console.WriteLine("Something went wrong. Moving past error.");
					Console.WriteLine(e.Message);
					continue;
				}
			}
		}

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
			String fullPath = Path.Combine(settings.getFactorioPath(), "script-output\\toMC.dat");
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

		/// MINECRAFT
		/// Reading and Parsing for minecraft 
		/// MINECRAFT

		public static List<ItemPair> parseMinecraft(Settings settings, DualDictionary<String,String> mappings, Dictionary<String, double> ratios)
		{
			List<ItemPair> items = new List<ItemPair>();
			String fullPath = Path.Combine(settings.getMcPath(), "toFactorio.dat");
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
			String fullPath = Path.Combine(settings.getMcPath(), "fromFactorio.dat");

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
	}
}
