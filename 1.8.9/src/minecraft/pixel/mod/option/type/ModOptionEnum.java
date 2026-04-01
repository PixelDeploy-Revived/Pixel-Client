package pixel.mod.option.type;

import pixel.mod.option.ModOption;
import pixel.mod.option.ModOptionParent;

public class ModOptionEnum extends ModOption {
	public static class EnumList {
		public static class Element {
			private int index;
			private String name;
			
			public Element(int index, String name) {
				this.index = index;
				this.name = name;
			}
			
			public int getIndex() {
				return index;
			}
			
			public String getName() {
				return name;
			}
		}
		
		private int first;
		private int last;
		private Element[] elements;
		
		public EnumList(int first, int last, Element... elements) {
			this.first = first;
			this.last = last;
			this.elements = elements;
		}
		
		public int getFirst() {
			return first;
		}
		
		public int getLast() {
			return last;
		}
		
		public Element findElement(int index) {
			for (Element e : elements) {
				if (e.getIndex() == index) {
					return e;
				}
			}
			
			return null;
		}
	}
	
	private EnumList enumList;
	
	public ModOptionEnum(ModOptionParent optionParent, String key, EnumList enumList, int index, InGuiSettings inGuiSettings) {
		super(optionParent, key, index, inGuiSettings);
		
		this.enumList = enumList;
	}
	
	public ModOptionEnum(String key, EnumList enumList, int index, InGuiSettings inGuiSettings) {
		super(key, index, inGuiSettings);
		
		this.enumList = enumList;
	}
	
	public EnumList getEnumList() {
		return enumList;
	}
}
