using System;
using System.Collections.Generic;
using System.Text;

namespace Factorio_MC_Bridge {
	class MinecraftObject {
		public string command_name { get; set; }
		public string rec_type { get; set; }
		public List<object> rec_objects { get; set; }
		public MinecraftObject(string _command_name, string _rec_type, List<object> objects) {
			command_name = _command_name;
			rec_type = _rec_type;
			rec_objects = objects;
		}
	}
}
