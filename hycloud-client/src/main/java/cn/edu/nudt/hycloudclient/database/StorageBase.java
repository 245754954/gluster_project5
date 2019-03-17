package cn.edu.nudt.hycloudclient.database;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudinterface.utils.helper;

import java.io.IOException;
import java.sql.*;

public class StorageBase {
	private Connection conn = null;
	
	public StorageBase() throws IOException {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + Config.getConfig().getClientDatabasePath());
			
			String sqlCreateTable = "CREATE TABLE IF NOT EXISTS storgeTable ("
					+ "fid INTEGER PRIMARY kEY AUTOINCREMENT,"
					+ "filename text NOT NULL ,"
					+ "blocknum integer NOT NULL,"
					+ "challenge text NOT NULL,"
					+ "storepath text NOT  NULL,"
					+ "hashchallenge text NOT NULL UNIQUE)";
			
			Statement st = conn.createStatement();
			st.execute(sqlCreateTable);
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(String filename) {
		String sqlDelete = "DELETE FROM storgeTable WHERE filename is ?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sqlDelete);
			
			pst.setString(1, filename);
			int rv = pst.executeUpdate();
			if(rv > 0)
				helper.print("Database: " + filename + " exists in database, delete the old record");
			
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insert(String filename, long blocknum, String challenge,String storepath,String hashchallenge) {

		//delete(filename);
		
		String sqlInsert = "INSERT INTO storgeTable "
				+ "(filename, blocknum, challenge,storepath,hashchallenge) "
				+ "VALUES (?, ?, ?,?,?)";
		try {
			PreparedStatement pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, filename);
			pst.setLong(2, blocknum);
			pst.setString(3, challenge);
			pst.setString(4,storepath);
			pst.setString(5,hashchallenge);
			
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getHdfsPath(String filename) {
		String sqlQuery = "SELECT hashchallenge FROM storgeTable WHERE filename is ?";
		
		String hashchallenge = null;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			
			ResultSet rs = pst.executeQuery();
			
			rs.next();
			hashchallenge = rs.getString("hashchallenge");
			
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashchallenge;
	}

	public long getBlockNum(String filename) {
		String sqlQuery = "SELECT blocknum FROM storgeTable WHERE filename is ?";

		long blockNum = 0;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);

			ResultSet rs = pst.executeQuery();

			rs.next();
			blockNum = rs.getLong("blocknum");

			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blockNum;
	}
	
//	public void update(String filename, BigInteger masterKey) {
//		String sqlUpdate = "UPDATE fileKeyRef SET masterKey = ?, "
//				+ "WHERE filename is ?";
//		
//		try {
//			PreparedStatement pst = conn.prepareStatement(sqlUpdate);
//			pst.setString(1, masterKey.toString(16));
//			pst.setString(2, filename);
//			pst.executeUpdate();
//			pst.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] argss) {
//		DatabaseHandler dbh = new DatabaseHandler();
//		
////		dbh.insert("filename_1", "localPath_1", new BigInteger("100"), 64, "remotePath_1", "remoteTreePath_1");
////		dbh.insert("filename_2", "localPath_2", new BigInteger("200"), 64, "remotePath_2", "remoteTreePath_2");
////		dbh.insert("filename_3", "localPath_3", new BigInteger("300"), 64, "remotePath_3", "remoteTreePath_3");
//		
//		BigInteger masterKey = dbh.getMasterKey("filename_2");
//		Log.print(masterKey.toString());
//	}
}
