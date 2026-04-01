package pixel.mod.option;

import java.util.ArrayList;

import pixel.mod.option.type.ModOptionEnum.EnumList;
import pixel.mod.option.type.ModOptionEnum.EnumList.Element;

public enum Brackets {
	NONE(0, "", ""),
	ROUND(1, "(", ")"),
	SQUARE(2, "[", "]"),
	CURLY(3, "{", "}"),
	ANGULAR(4, "<", ">");
	
	private final int index;
	private final String open;
	private final String close;

	private Brackets(int index, String open, String close) {
		this.index = index;
		this.open = open;
		this.close = close;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getOpen() {
		return open;
	}
	
	public String getClose() {
		return close;
	}
	
	public String wrap(String text) {
		return open + text + close;
	}
	
	public String getName() {
		return index == Brackets.NONE.getIndex() ? "None" : open + close;
	}
	
	public static Brackets fromIndex(int index) {
		for (Brackets b : values()) {
			if (b.getIndex() == index) {
				return b;
			}
		}
		
		return NONE;
	}
	
	public static EnumList toEnumList() {
		ArrayList<Element> elements = new ArrayList<Element>();
		
		for (Brackets b : values()) {
			elements.add(new Element(b.getIndex(), b.getName()));
		}
		
		return new EnumList(Brackets.NONE.getIndex(), Brackets.ANGULAR.getIndex(), elements.toArray(new Element[0]));
	}
}
