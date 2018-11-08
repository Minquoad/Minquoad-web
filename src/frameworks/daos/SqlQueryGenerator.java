package frameworks.daos;

public abstract class SqlQueryGenerator {

	public static String[] all() {
		return toArray("*");
	}

	public static String generateSelectQuery(String table, String[] returningColumn, String[] whereColumns) {
		String query = "SELECT ";
		if (isAll(returningColumn)) {
			query += "*";
		} else {
			query += "\"";
			query += implodeParameters(returningColumn, "\", \"");
			query += "\"";
		}
		query += " FROM \"" + table + "\"";
		query = addConditions(query, whereColumns);
		return query + ";";
	}

	public static String generateInsertQuery(String table, String[] valueColumns, String[] returningColumn) {
		String query = "INSERT INTO \"" + table + "\" (\"";
		query += implodeParameters(valueColumns, "\", \"");
		query += "\") VALUES (";

		String[] markups = new String[valueColumns.length];
		for (int i = 0; i < valueColumns.length; i++) {
			markups[i] = "?";
		}

		query += implodeParameters(markups, ", ");
		query += ")";
		if (returningColumn != null && returningColumn.length != 0) {
			query += " RETURNING ";
			if (isAll(returningColumn)) {
				query += "*";
			} else {
				query += "\"";
				query += implodeParameters(returningColumn, "\", \"");
				query += "\"";
			}
		}
		return query + ";";
	}

	public static String generateUpdateQuery(String table, String[] setColumns, String[] whereColumns) {
		String query = "UPDATE \"" + table + "\" SET \"";
		query += implodeParameters(setColumns, "\"=?, \"");
		query += "\"=?";
		query = addConditions(query, whereColumns);
		return query + ";";
	}

	public static String generateDeleteQuery(String table, String[] whereColumns) {
		String query = "DELETE FROM \"" + table + "\"";
		query = addConditions(query, whereColumns);
		return query + ";";
	}

	public static String addConditions(String query, String[] whereColumns) {
		if (whereColumns != null && whereColumns.length != 0) {
			query += " WHERE \"";
			query += implodeParameters(whereColumns, "\"=? AND \"");
			query += "\"=?";
		}
		return query;
	}

	public static String implodeParameters(String[] parameters, String separator) {
		String implodedString = "";
		boolean firstLoop = true;
		for (String parameter : parameters) {
			if (!firstLoop) {
				implodedString += separator;
			}
			implodedString += parameter;
			firstLoop = false;
		}
		return implodedString;
	}

	public static String[] toArray(String string) {
		String[] array = new String[1];
		array[0] = string;
		return array;
	}

	public static boolean isAll(String[] columns) {
		return columns != null && columns.length == 1 && columns[0].equals("*");
	}

}
