using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Factorio_MC_Bridge
{
	class ItemMappings
	{
		private Dictionary<String, String> _forward = new Dictionary<String, String>();
		private Dictionary<String, String> _reverse = new Dictionary<String, String>();

		public ItemMappings()
		{
			this.minecraft = new Indexer<String, String>(_forward);
			this.facotrio = new Indexer<String, String>(_reverse);
		}

		public class Indexer<T3, T4>
		{
			private Dictionary<T3, T4> _dictionary;
			public Indexer(Dictionary<T3, T4> dictionary)
			{
				_dictionary = dictionary;
			}
			public T4 this[T3 index]
			{
				get { return _dictionary[index]; }
				set { _dictionary[index] = value; }
			}
		}

		public void Add(String t1, String t2)
		{
			_forward.Add(t1, t2);
			_reverse.Add(t2, t1);
		}

		public Indexer<String, String> minecraft { get; private set; }
		public Indexer<String, String> facotrio { get; private set; }
	}
}
