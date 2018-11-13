package cn.edu.nudt.hycloudclient.database;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudinterface.entity.utils.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;

public class LocalBase {
	private Connection conn = null;
	
	public LocalBase() throws IOException {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + Config.getConfig().getClientDatabasePath());
			
			String sqlCreateDeletionTable = "CREATE TABLE IF NOT EXISTS deletionTable ("
					+ "fid INTEGER PRIMARY kEY AUTOINCREMENT,"
					+ "filename text NOT NULL UNIQUE,"
					+ "localPath text NOT NULL,"
					+ "masterKey text NOT NULL UNIQUE,"
					+ "granularity integer NOT NULL,"
					+ "segmentsNum integer NOT NULL,"
					+ "remotePath text NOT NULL UNIQUE)";
			
			
			Statement st = conn.createStatement();
			st.execute(sqlCreateDeletionTable);
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete(String filename) {
		String sqlDelete = "DELETE FROM deletionTable WHERE filename is ?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sqlDelete);
			
			pst.setString(1, filename);
			int rv = pst.executeUpdate();
			if(rv > 0) helper.print("Database: " + filename + " exists in database, delete the old record");
			
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insert(String filename, 
			String localFilePath, 
			BigInteger masterKey, 
			int granularity, 
			int segmentsNum,
			String remotePath) {
		
		delete(filename);
		
		String sqlInsert = "INSERT INTO deletionTable "
				+ "(filename, localPath, masterKey, granularity, segmentsNum, remotePath) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, filename);
			pst.setString(2, localFilePath);
			pst.setString(3, masterKey.toString(16));
			pst.setInt(4, granularity);
			pst.setInt(5, segmentsNum);
			pst.setString(6, remotePath);

			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BigInteger getMasterKey(String filename) {
		String sqlQuery = "SELECT masterKey FROM deletionTable WHERE filename is ?";
		
		BigInteger masterKey = null;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			
			ResultSet rs = pst.executeQuery();
			
			rs.next();
			masterKey = new BigInteger(rs.getString("masterKey"), 16);
			
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return masterKey;
	}
	
	public String getRemotePath(String filename) {
		String sqlQuery = "SELECT remotePath FROM deletionTable WHERE filename is ?";
		
		String remotePath = null;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			
			ResultSet rs = pst.executeQuery();
			
			rs.next();
			remotePath = rs.getString("remotePath");
			
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remotePath;
	}
	
//	public String getRemoteTreePath(String filename) {
//		String sqlQuery = "SELECT remoteTreePath FROM deletionTable WHERE filename is ?";
//
//		String modulationTreePath = null;
//		try {
//			PreparedStatement pst = conn.prepareStatement(sqlQuery);
//			pst.setString(1, filename);
//
//			ResultSet rs = pst.executeQuery();
//
//			rs.next();
//			modulationTreePath = rs.getString("remoteTreePath");
//
//			rs.close();
//			pst.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return modulationTreePath;
//	}
	
	public int getGranularity(String filename) {
		String sqlQuery = "SELECT granularity FROM deletionTable WHERE filename is ?";
		
		int granularity = 0;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			
			ResultSet rs = pst.executeQuery();
			
			rs.next();
			granularity = rs.getInt("granularity");
					
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return granularity;
	}
	
	public int getSegmentsNum(String filename) {
		String sqlQuery = "SELECT segmentsNum FROM deletionTable WHERE filename is ?";
		
		int granularity = 0;
		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			
			ResultSet rs = pst.executeQuery();
			
			rs.next();
			granularity = rs.getInt("segmentsNum");
					
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return granularity;
	}
	
	public void update(String filename, BigInteger masterKey, String remoteTreePath) {
		String sqlUpdate = "UPDATE deletionTable SET masterKey = ? "
				+ "remoteTreePath = ?"
				+ "WHERE filename is ?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sqlUpdate);
			pst.setString(1, masterKey.toString(16));
			pst.setString(2, remoteTreePath);
			pst.setString(3, filename);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void update(String filename, BigInteger masterKey) {
		String sqlUpdate = "UPDATE deletionTable SET masterKey = ? WHERE filename is ?";
		
		try {
			PreparedStatement pst = conn.prepareStatement(sqlUpdate);
			pst.setString(1, masterKey.toString(16));
			pst.setString(2, filename);
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
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
