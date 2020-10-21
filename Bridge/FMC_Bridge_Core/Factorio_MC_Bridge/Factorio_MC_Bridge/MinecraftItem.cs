using System;
using System.Collections.Generic;
using System.Text;

namespace Factorio_MC_Bridge {
	class MinecraftItem {
		public MinecraftItem(string _name, int _count, int _metadata, List<string> _nbt) {
			this.itemName = _name;
			this.count = _count;
			this.metadata = _metadata;
			this.nbt = _nbt;
		}

		public string itemName { get; set; }
		public int count { get; set; }
		public int metadata { get; set; }
		public List<string> nbt { get; set; }
	}
}
