package com.wsj;

public class Main {
	
	public static void main(String[] args){
		String dbPath      = args[0];
		String packageName = args[1];
		ModelGenerate mg   = new ModelGenerate(dbPath);
		mg.setPackageName(packageName);
		mg.generate();
	}

}
