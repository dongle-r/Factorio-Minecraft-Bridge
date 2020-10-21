using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Factorio_MC_Bridge
{
	class ItemPair
	{
		public string name;
		public int count;
		public ItemPair(string n, int c)
		{
			name = n;
			count = c;
		}

		public void incCount(int x)
		{
			count += x;
		}
	}
}
