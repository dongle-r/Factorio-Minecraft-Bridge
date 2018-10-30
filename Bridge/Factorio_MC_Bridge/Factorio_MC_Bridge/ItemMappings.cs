//Thank you to user Enigmativity on Stack Overflow for the solution to the bi-directional dictionary
//https://stackoverflow.com/questions/10966331/two-way-bidirectional-dictionary-in-c

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Factorio_MC_Bridge
{
	class ItemMappings<T1, T2>{
		private Dictionary<T1, T2> _forward = new Dictionary<T1, T2>();
		private Dictionary<T2, T1> _reverse = new Dictionary<T2, T1>();

		public ItemMappings(){
			this.minecraft = new Indexer<T1, T2>(_forward);
			this.facotrio = new Indexer<T2, T1>(_reverse);
		}

		public class Indexer<T3, T4>{
			private Dictionary<T3, T4> _dictionary;
			public Indexer(Dictionary<T3, T4> dictionary){
				_dictionary = dictionary;
			}
			public T4 this[T3 index]{
				get { return _dictionary[index]; }
				set { _dictionary[index] = value; }
			}
		}

		public void Add(T1 t1, T2 t2){
			_forward.Add(t1, t2);
			_reverse.Add(t2, t1);
		}

		public Indexer<T1, T2> minecraft { get; private set; }
		public Indexer<T2, T1> facotrio { get; private set; }
	}
}
