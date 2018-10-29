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
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
				StreamReader sr = new StreamReader(fs, Encoding.Default);
				mcPath = sr.ReadLine();
				facPath = sr.ReadLine();
			}

			var rcon = new RCON(IPAddress.Parse("172.28.65.243"), 25525, "test");
			while (true)
			{
				List<ItemPair> factorioItems = parseFactrio(facPath);
				List<ItemPair> minecraftItems = parseMinecraft(mcPath);
				sendToFactorio(minecraftItems, rcon);
				sendToMinecraft(factorioItems, mcPath);
				Thread.Sleep(1000);
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

		public static List<ItemPair> parseMinecraft(String path)
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

			//items = remapMC(items);
			for (int i = 0; i < items.Count; i++)
			{
				if (items[i].name.Equals("minecraft:diamond"))
				{
					items[i].name = "science-pack-2";
				}
				if (items[i].name.Equals("minecraft:diamond_block"))
				{
					items[i].name = "science-pack-3";
				}
				if (items[i].name.Equals("minecraft:stonebrick"))
				{
					items[i].name = "concrete";
				}
			}
			return items;
		}

		public static List<ItemPair> remapMC(List<ItemPair> items) {
			List<ItemPair> tempList = items;
			for (int i = 0; i < items.Count; i++)
			{
				if (items[i].name.Equals("minecraft:diamond"))
				{
					items[i].name = "science-pack-2";
				}
				if (items[i].name.Equals("minecraft:diamond_block"))
				{
					items[i].name = "science-pack-3";
				}
				if (items[i].name.Equals("minecraft:stonebrick"))
				{
					items[i].name = "concrete";
				}
			}
			return tempList;
		}


		public static List<ItemPair> parseFactrio(String path) {
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
			//List<ItemPair> tempList = items;
			for (int i = 0; i < items.Count; i++)
			{
				if (items[i].name.Equals("iron-plate"))
				{
					items[i].name = "minecraft:iron_ingot";
				}
				if (items[i].name.Equals("coal"))
				{
					items[i].name = "minecraft:coal";
				}
			}
			return items;
		}


		public static void sendToMinecraft(List<ItemPair> items, String path) {
			String fullPath = Path.Combine(path, "fromFactorio.dat");
			//FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.ReadWrite, FileShare.ReadWrite);
			//StreamReader sr = new StreamReader(fs, Encoding.Default);
			StreamWriter sw = new StreamWriter(fullPath, true);
			for (int i = 0; i < items.Count; i++) {
				String itemToSend = items[i].name + "~" + items[i].count;
				sw.WriteLine(itemToSend);
			}
			sw.Close();
			//sr.Close();
			//File.WriteAllText(fullPath, string.Empty);
		}


		public static List<ItemPair> remapFactorio(List<ItemPair> items)
		{
			List<ItemPair> tempList = items;
			for (int i = 0; i < items.Count; i++)
			{
				if (items[i].name.Equals("iron-plate"))
				{
					items[i].name = "minecraft:iron_ingot";
				}
				if (items[i].name.Equals("coal"))
				{
					items[i].name = "minecraft:coal";
				}
			}
			return tempList;
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
