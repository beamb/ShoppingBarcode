package dk.itu.shopping.database;
public class ItemsDbSchema {
	public static final class ItemTable {
		public static final String NAME = "Items";

		public static final class Cols {
			public static final String WHAT = "what";
			public static final String WHERE = "whereC";  //where is a keyword in SQL
			public static final String PICT = "pict";
		}
	}
}
