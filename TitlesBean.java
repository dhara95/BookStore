// TitlesBean.java
// Class TitlesBean makes a database connection and retrieves
package org.me.webapps.bookstore;

// Java core packages
import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.*;

public class TitlesBean implements Serializable {
	private static final long serialVersionUID = 6723471178342776147L;
	private Connection connection;
	private PreparedStatement titlesQuery;

	// construct TitlesBean object
	public TitlesBean() {
		// attempt database connection and setup SQL statements
		try {
			URL myUrl = getClass().getResource("TitlesBean.class");

			System.out.println(getDatabasePath(myUrl.toString()));

			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection( "jdbc:hsqldb:hsql://localhost/bookdb", "sa", "" );

			titlesQuery = connection
					.prepareStatement("SELECT isbn, title, editionNumber, "
							+ "copyright, publisherID, imageFile, price "
							+ "FROM titles ORDER BY title");
		}

		// process exceptions during database setup
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		// process problems locating data source
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	// return a List of BookBeans
	public List<BookBean> getTitles() {
		List<BookBean> titlesList = new ArrayList<BookBean>();

		// obtain list of titles
		try {
			ResultSet results = titlesQuery.executeQuery();

			// get row data
			while (results.next()) {
				BookBean book = new BookBean();

				book.setISBN(results.getString("isbn"));
				book.setTitle(results.getString("title"));
				book.setEditionNumber(results.getInt("editionNumber"));
				book.setCopyright(results.getString("copyright"));
				book.setPublisherID(results.getInt("publisherID"));
				book.setImageFile(results.getString("imageFile"));
				book.setPrice(results.getDouble("price"));

				titlesList.add(book);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

		return titlesList;
	}

	private String getDatabasePath(String classPath) {
		String path = "";
		String crtToken;

		StringTokenizer tokens = new StringTokenizer(classPath, "/");
		int num = tokens.countTokens();
		tokens.nextToken();

		for (int i = 1; i < num; i++) {
			crtToken = tokens.nextToken();
			if (crtToken.equals("classes")) {
				break;
			}
			path = path + crtToken + "\\";
		}

		return path;
	}

	
	protected void finalize() {
		// attempt to close database connection
		try {
			connection.close();
		}

		// process SQLException on close operation
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}
}
