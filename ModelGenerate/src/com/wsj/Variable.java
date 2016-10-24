package com.wsj;

public class Variable {

	public int colID;
	public String name;
	public String type;
	public String notNull;
	public String defaultValue;
	public Boolean isPrimaryKey;
	
	private String mVarType;
	private String mVarName;
	public String getVarName(){
		if(mVarName==null)
			mVarName = Util.firstCharToLowerCase(name);
		return mVarName;
	}
	
	public String getVarType(){
		if(mVarType==null)
			mVarType = getTypeString();
		return mVarType;
	}
	
	/**
	 * 获取变量定义字符串
	 * @return
	 */
	public String getVariableDefString(){
		return "public " + getVarType()+" "+getVarName()+";";
	}
	

	
	private String getTypeString(){
		switch (type.toLowerCase()) {
		case "text":
			return "String";
		case "integer":
			return "int";
		case "double":
			return "double";
		case "float":
			return "float";
		case "real":
			return "int";
		default:
			return "String";
		}
	}
}
