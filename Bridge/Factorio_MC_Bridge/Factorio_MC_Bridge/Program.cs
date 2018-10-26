using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.IO;

namespace Factorio_MC_Bridge
{
	class Program
	{
		static void Main(string[] args)
		{
			string startupDoc = Path.Combine(Environment.CurrentDirectory, "settings.txt");
			String mcPath = "";
			if (!File.Exists(startupDoc))
			{
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Write, FileShare.ReadWrite);
				StreamWriter sw = new StreamWriter(fs, Encoding.Default);
				Console.WriteLine("Please enter Minecraft Location: ");
				mcPath = Console.ReadLine();
				sw.WriteLine(mcPath);
			}
			else {
				FileStream fs = new FileStream(startupDoc, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
				StreamReader sr = new StreamReader(fs, Encoding.Default);
				mcPath = sr.ReadLine();
			}

			while (Console.ReadLine() != "quit") {
				List<itemPair> factorioItems = parseFactrio();
				List<itemPair> minecraftItems = parseMinecraft(mcPath);
				for (int i = 0; i < factorioItems.Count; i++)
				{
					Console.WriteLine(factorioItems[i].name + " " + factorioItems[i].count);
				}

				for (int i = 0; i < minecraftItems.Count; i++)
				{
					Console.WriteLine(minecraftItems[i].name + " " + minecraftItems[i].count);
				}
			}
		}

		public static List<itemPair> parseMinecraft(String path)
		{
			List<itemPair> items = new List<itemPair>();
			String fullPath = Path.Combine(path, "toFactorio.dat");
			FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
			StreamReader sr = new StreamReader(fs, Encoding.Default);
			while (!sr.EndOfStream)
			{
				string proc = sr.ReadLine();
				string[] temp = proc.Split('~');
				items.Add(new itemPair(temp[0], Int32.Parse(temp[1])));
			}
			return items;
		}

		public static List<itemPair> parseFactrio() {
			List<itemPair> items = new List<itemPair>();
			String fullPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "Factorio\\script-output\\toMC.dat");
			FileStream fs = new FileStream(fullPath, FileMode.OpenOrCreate, FileAccess.Read, FileShare.ReadWrite);
			StreamReader sr = new StreamReader(fs, Encoding.Default);
			while (!sr.EndOfStream) {
				string proc = sr.ReadLine();
				string[] temp = proc.Split(':');
				items.Add(new itemPair(temp[0], Int32.Parse(temp[1])));
			}
			return items;
		}

		public struct itemPair{
			public string name;
			public int count;
			public itemPair(string n, int c) {
				name = n;
				count = c;
			}
		}
	}
}
