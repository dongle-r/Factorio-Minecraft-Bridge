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

namespace Factorio_MC_Bridge
{
	class Program
	{
		static void Main(string[] args)
		{
			Console.WriteLine("Starting Up!");
			string startupDoc = Path.Combine(Environment.CurrentDirectory, "settings.txt");
			String mcPath = "";
			String facPath = "";
			if (!File.Exists(startupDoc))
			{
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Write, FileShare.ReadWrite);
				StreamWriter sw = new StreamWriter(fs, Encoding.Default);
				Console.WriteLine("Please enter Minecraft Location: ");
				mcPath = Console.ReadLine();
				sw.WriteLine(mcPath);
				Console.WriteLine("Please enter Factorio Server Path: ");
				facPath = Console.ReadLine();
				sw.WriteLine(facPath);
				sw.Close();
			}
			else
			{
				Console.WriteLine("Found paths. Beginning transfer");
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
				StreamReader sr = new StreamReader(fs, Encoding.Default);
				mcPath = sr.ReadLine();
				facPath = sr.ReadLine();
			}

			ItemMappings<String, String> itemMappings = new ItemMappings<String, String>();
			string itemMappingsPath = Path.Combine(Environment.CurrentDirectory, "item_mappings.txt");
			FileStream fileStream = new FileStream(itemMappingsPath, FileMode.OpenOrCreate, FileAccess.Read, FileShare.Read);
			StreamReader streamReader = new StreamReader(fileStream, Encoding.Default);
			while (!streamReader.EndOfStream) {
				String readString = streamReader.ReadLine();
				String[] split = readString.Split('=');
				itemMappings.Add(split[0], split[1]);
			}

			var rcon = new RCON(IPAddress.Parse("172.28.65.243"), 25525, "test");
			while (true)
			{
				try
				{
					List<ItemPair> factorioItems = parseFactrio(facPath, itemMappings);
					List<ItemPair> minecraftItems = parseMinecraft(mcPath, itemMappings);
					sendToFactorio(minecraftItems, rcon);
					sendToMinecraft(factorioItems, mcPath);
					Thread.Sleep(1000);
				}
				catch (Exception e) {
					Console.WriteLine("Something went wrong. Moving past error.");
					Console.WriteLine(e.Message);
					continue;
				}
			}
		}


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

		public static List<ItemPair> parseMinecraft(String path, ItemMappings<String,String> mappings)
		{
			List<ItemPair> items = new List<ItemPair>();
			String fullPath = Path.Combine(path, "toFactorio.dat");
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
				items.Add(new ItemPair(temp[0], Int32.Parse(temp[1])));
			}
			sr.Close();
			File.WriteAllText(fullPath, string.Empty);

			//Remap Items to the opposing item
			for (int i = 0; i < items.Count; i++){
				items[i].name = mappings.minecraft[items[i].name];
	
			}
			return items;
		}

		public static List<ItemPair> parseFactrio(String path, ItemMappings<String, String> mappings) {
			List<ItemPair> items = new List<ItemPair>();
			String fullPath = Path.Combine(path, "script-output\\toMC.dat");
			FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.ReadWrite, FileShare.ReadWrite);
			StreamReader sr = new StreamReader(fs, Encoding.Default);
			while (!sr.EndOfStream) {
				string proc = sr.ReadLine();
				string[] temp = proc.Split(':');
				int containsTest = pairContains(items, temp[0]);
				if (containsTest != -1){
					items[containsTest].count += Int32.Parse(temp[1]);
				}
				else {
					items.Add(new ItemPair(temp[0], Int32.Parse(temp[1])));
				}
				StreamWriter sw = new StreamWriter(fs);
				sw.WriteLine("");
			}

			//Remap Items to the opposing item
			for (int i = 0; i < items.Count; i++){
				items[i].name = mappings.facotrio[items[i].name];
			}
			return items;
		}


		public static void sendToMinecraft(List<ItemPair> items, String path) {
			String fullPath = Path.Combine(path, "fromFactorio.dat");

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
