package com.wsj;

public class Util {

	public static String firstCharToLowerCase(String string){
		String temp = string.substring(0, 1);
		string =  string.replaceFirst(temp,temp.toLowerCase());
		return string;
	}
	
	public static String firstCharToUpCase(String string){
		String temp = string.substring(0, 1);
		string =  string.replaceFirst(temp,temp.toUpperCase());
		return string;
	}
	
}
