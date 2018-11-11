package cn.edu.nudt.hycloudclient.LocalManagerServer;

import cn.edu.nudt.hycloudclient.crypto.AES;
import cn.edu.nudt.hycloudclient.deletion.DeletionTransfer;
import cn.edu.nudt.hycloudinterface.entity.ModulationTree;
import cn.edu.nudt.hycloudinterface.entity.SegmentList;
import cn.edu.nudt.hycloudinterface.entity.utils.Assist;
import cn.edu.nudt.hycloudinterface.entity.utils.helper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server side use the same ModulationTree class. Thus, we can transfer ModulationTree Object directly.
 * @author roger
 *
 */
public class Server extends Thread{
	private ServerSocket mServerSocket;
	
	public Server(int port) {
		try {
			mServerSocket = new ServerSocket(port);
//			mServerSocket.setSoTimeout(10000);
			
			helper.print("Server started on port: " + mServerSocket.getLocalPort());
			helper.print("\tServerPathPrefix: " + ServerConfig.ServerPathPrefix);
			helper.print("\tServerTreePathPrefix: " + ServerConfig.ServerTreePathPrefix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		while(true) {
			DataInputStream dis = null;
			ObjectInputStream ois = null;
			DataOutputStream dos = null;
			ObjectOutputStream oos = null;
			String remoteTreePath = null;
			String filename = null;
			SegmentList segList = null;
			ModulationTree tree = null;
			try {
				Socket server = mServerSocket.accept();
				dis = new DataInputStream(server.getInputStream());
				int action = dis.readInt();
				
				switch(action) {
				case DeletionTransfer.ObtainRemoteTreeWithDeletion:
					remoteTreePath = dis.readUTF();
					ois = new ObjectInputStream(server.getInputStream());
					segList = (SegmentList) ois.readObject();
					
					helper.print("Action: ObtainRemoteTreeWithDeletion");
					helper.print("\tremoteTreePath: " + remoteTreePath);
					String info = "\tdelete segments: ";
					for(int i = 0; i < segList.size(); i++) {
						info += segList.get(i);
					}
					helper.print(info);
					
					tree = ModulationTree.load(remoteTreePath);
					tree.obtainSubTree(segList);
					
					oos = new ObjectOutputStream(server.getOutputStream());
					oos.writeObject(tree);
					oos.flush();
					break;
				case DeletionTransfer.ObtainRemoteTreeWithoutDeletion:
					remoteTreePath = dis.readUTF();
					
					helper.print("Action: ObtainRemoteTreeWithoutDeletion");
					helper.print("\tremoteTreePath: " + remoteTreePath);
					
					tree = ModulationTree.load(remoteTreePath);
					
					oos = new ObjectOutputStream(server.getOutputStream());
					oos.writeObject(tree);
					oos.flush();
					break;
				case DeletionTransfer.UpdateRemoteTree:
					filename = dis.readUTF();
					ois = new ObjectInputStream(server.getInputStream());
					tree = (ModulationTree) ois.readObject();
					
					remoteTreePath = ServerConfig.ServerPathPrefix + "RemoteTree_" + filename;
					helper.print("Action: UpdateRemoteTree");
					helper.print("\tremoteTreePath: " + remoteTreePath);

					tree.save(remoteTreePath);
					
					dos = new DataOutputStream(server.getOutputStream());
					dos.writeUTF(remoteTreePath);
					dos.flush();
					break;
				case DeletionTransfer.UploadWithEncryption:
					int segmentSize = dis.readInt();
					filename = dis.readUTF();
					
					String remotePath = ServerConfig.ServerTreePathPrefix + "encrypted_" + filename;
					helper.print("Action: UploadWithEncryption");
					helper.print("\tremoteTreePath: " + remotePath);
					
					OutputStream fos = new FileOutputStream(remotePath);
					int bufferSize = Assist.getBufferSize(segmentSize + AES.AES_Block_Size);
					byte[] buffer = new byte[bufferSize];
					int nread = 0;
					while( (nread = dis.read(buffer)) != -1) {
						fos.write(buffer, 0, nread);
					}
					fos.flush();
					fos.close();

					dos = new DataOutputStream(server.getOutputStream());
					dos.writeUTF(remotePath);
					dos.flush();
					break;
				case DeletionTransfer.Retrieve:
					segmentSize = dis.readInt();
					remotePath = dis.readUTF();
					
					helper.print("Action: Retrieve");
					helper.print("\tremoteTreePath: " + remotePath);
					
					InputStream fis = new FileInputStream(remotePath);
					dos = new DataOutputStream(server.getOutputStream());
					
					buffer = new byte[segmentSize + AES.AES_Block_Size];
					nread = 0;
					while( (nread = fis.read(buffer)) != -1) {
						// inputStream.read(byte[]) will block until input data is available, end of file is detected, or an exception is thrown
						// Thus, the sender has to invoke shutdownOutput() to shutdown this side of the socket
						dos.write(buffer, 0, nread);
						dos.flush();
					}
					fis.close();
					server.shutdownOutput();
					break;
				}
				
				if(ois != null) {
					ois.close();
				}
				if(dos != null) {
					dos.close();
				}
				if(oos != null) {
					oos.close();
				}
				dis.close();
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void usage() {
		helper.print("Server: <port>");
		helper.print("e.g., java -jar server.jar 7777");
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			usage();
			System.exit(-1);
		}
		int port = Integer.parseInt(args[0]);
//		int port = 7777;
		Thread t = new Server(port);
		t.start();
	}
	
}
