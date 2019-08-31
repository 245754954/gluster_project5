package cn.edu.nudt.hycloudclient.database;

import cn.edu.nudt.hycloudclient.config.Config;
import cn.edu.nudt.hycloudinterface.entity.UploadInfo;
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
					+ "filename text NOT NULL,"
					+ "blocknum integer NOT NULL,"
					+ "challenge text NOT NULL,"
					+ "storepath text NOT  NULL,"
					+ "real_size integer NOT NULL,"
					+ "blocksize integer NOT NULL,"
					+ "hashchallenge text NOT NULL,"
					+ "w text NOT NULL,"
					+ "y text NOT NULL,"
					+ "p text NOT NULL)";

			String  param_table = "CREATE TABLE IF NOT EXISTS params ("
					+ "pid INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "x text NOT NULL,"
					+ "y text NOT NULL,"
					+ "p text NOT NULL)";
			
			Statement st = conn.createStatement();
			st.execute(sqlCreateTable);
			st.execute(param_table);
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

	public void insert_param(String x, String y,String p) {

		String sqlInsert = "INSERT INTO params "
				+ "(x,y,p) "
				+ "VALUES (?, ?, ?)";
		try
		{
			PreparedStatement pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, x);
			pst.setString(2, y);
			pst.setString(3, p);

			pst.executeUpdate();
			pst.close();
		}
		catch (SQLException e)
		{

			e.printStackTrace();
		}
	}
	
	public void insert(String filename, long blocknum, String challenge,String storepath,long real_size,long blocksize,String hashchallenge,String w,String y,String p) {

		//delete(filename);
		
		String sqlInsert = "INSERT INTO storgeTable "
				+ "(filename, blocknum, challenge,storepath,real_size,blocksize,hashchallenge,w,y,p) "
				+ "VALUES (?, ?, ?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst = conn.prepareStatement(sqlInsert);
			pst.setString(1, filename);
			pst.setLong(2, blocknum);
			pst.setString(3, challenge);
			pst.setString(4,storepath);
			pst.setLong(5,real_size);
			pst.setLong(6,blocksize);
			pst.setString(7,hashchallenge);
			pst.setString(8,w);
			pst.setString(9,y);
			pst.setString(10,p);
			
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


	public UploadInfo get_uploadinfo_by_filename_and_blocknumber(String filename , long blocknumber, String challenge){

		String sqlQuery = "SELECT * FROM storgeTable as t WHERE t.storepath is ? and t.blocknum is ? and t.challenge is ?";



		 String filename_and_path1;
		 String filename1;
		 String challenge1;
		 Long blocknumber1;
		 Long real_size1;
		 Long blocksize1;
		 String hash_result1;
		 String w1;
		 String y1;
		 String p1;
		UploadInfo upload_info = new UploadInfo();

		try {
			PreparedStatement pst = conn.prepareStatement(sqlQuery);
			pst.setString(1, filename);
			pst.setLong(2,blocknumber);
			pst.setString(3,challenge);

			ResultSet rs = pst.executeQuery();

			rs.next();

			blocknumber1 = rs.getLong("blocknum");
			filename_and_path1 = rs.getString("storepath");
			real_size1 = rs.getLong("real_size");
			hash_result1 = rs.getString("hashchallenge");
			challenge1 = rs.getString("challenge");
			blocksize1 = rs.getLong("blocksize");
			filename1 = rs.getString("filename");
			w1 = rs.getString("w");
			y1 = rs.getString("y");
			p1 = rs.getString("p");




			upload_info.setBlocknumber(blocknumber1);
			upload_info.setBlocksize(blocksize1);
			upload_info.setHash_result(hash_result1);
			upload_info.setChallenge(challenge1);
			upload_info.setReal_size(real_size1);
			upload_info.setFilename_and_path(filename_and_path1);
			upload_info.setFilename(filename1);
			upload_info.setW(w1);
			upload_info.setY(y1);
			upload_info.setP(p1);
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}



		return upload_info;
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
