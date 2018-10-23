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
			String startupDoc = Path.Combine(Environment.CurrentDirectory, "settings.xml");
			if (!File.Exists(startupDoc)) {
				Console.WriteLine("Please enter Minecraft Location: ");
				string path = Console.ReadLine();
				
			}

			List<itemPair> factorioItems = parseFactrio();
			for (int i = 0; i < factorioItems.Count; i++) {
				Console.WriteLine(factorioItems[i].name + " " + factorioItems[i].count);
			}
		}

		public static List<itemPair> parseFactrio() {
			List<itemPair> items = new List<itemPair>();
			String fullPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), "Factorio\\script-output\\toMC.dat");
			FileStream fs = new FileStream(fullPath, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);
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
