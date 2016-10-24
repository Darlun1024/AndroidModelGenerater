package com.wsj;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sun.net.NetworkServer;

public class ModelGenerate {
	private Connection mConnection;
	private List<String> mTableList = new ArrayList<>();
	private String mPackageName;//包名
	private String mClassFileRootPath = "";
	public ModelGenerate(String dbPath){
		try {
			Class.forName("org.sqlite.JDBC");
			mConnection = DriverManager.getConnection("jdbc:sqlite:"+dbPath);
	         
	         Statement stat = mConnection.createStatement();
	         String sql  = "select name from sqlite_master where type='table' order by name";
	         ResultSet rs =  stat.executeQuery(sql);
	         while(rs.next()){
	        	String name =  rs.getString("name");
	        	mTableList.add(name);
	         }
	         stat.close();
	         stat = null;
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPackageName(String packageName){
		mPackageName = packageName;
	}
	
	public void generate(){
		initFolder();
		 parseDataBase();
	}
	
	private void initFolder(){
		if(mPackageName!=null){
			String[] array = mPackageName.split("\\.");
			StringBuffer dirPath = new StringBuffer();
			for(int i=0;i<array.length;i++){
				dirPath.append(array[i]+"/");
			}
			File dir = new File(dirPath.toString());
			if(!dir.exists()){
				dir.mkdirs();
			}
			mClassFileRootPath=dir.getAbsolutePath()+"/";
		}
	}
	
	private void parseDataBase(){
		try {
			Statement stat = mConnection.createStatement();
			int size = mTableList.size();
			for(int i=0;i<size;i++){
				String sql = "PRAGMA table_info(["+mTableList.get(i)+"])";
				 ResultSet rs =  stat.executeQuery(sql);
				 ClassStruct struct = new ClassStruct(mTableList.get(i),"","Model",mPackageName);
				 struct.setBaseClassName("BaseModel");
				 System.out.println(mTableList.get(i));
				 while(rs.next()){
					 Variable var = new Variable();
					 var.colID = rs.getInt("cid");
					 var.name = rs.getString("name");
					 var.type = rs.getString("type");
					 var.notNull = rs.getString("notnull");
					 var.defaultValue = rs.getString("dflt_value");
					 var.isPrimaryKey = rs.getBoolean("pk"); 
					 struct.addVariable(var);
				 }
				 System.out.println(struct.getClassText());
				 this.wirteToClassFile(struct);
				 System.out.println("------------------------------");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	/**
	 * 将类写入文件
	 */
	private void wirteToClassFile(ClassStruct struct){
		File file = new File(mClassFileRootPath+struct.getClassName()+".java");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(struct.getClassText().getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
