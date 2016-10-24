package com.wsj;

import java.util.ArrayList;
import java.util.List;

public class ClassStruct {
	List<Variable> mVarList = new ArrayList<>();
	String mClassName;
	String mTableName;//表格名称
	String mBaseClassName;//基类名称
	String mPrefix,mSuffix;
	String mPackageName;
	public ClassStruct(String tableName,String prefix,String suffix,String packageName){
		mTableName  = tableName;
		mSuffix     = suffix;
		mPrefix     = prefix;
		mPackageName = packageName;
	}
	
	public void setBaseClassName(String name){
		mBaseClassName = name;
	}
	
	public String getClassName(){
		return mClassName;
	}
	
	/**
	 * 返回该类的定义文件
	 * @return
	 */
	public String getClassText(){
		StringBuffer classText = new StringBuffer();
		classText.append("package "+mPackageName+";\n");
		classText.append("import android.database.Cursor;\n");
		classText.append("import java.util.ArrayList;\n");
		classText.append("import java.util.List;\n");
		classText.append("public class ");
		mClassName  = generateName(mTableName, mPrefix, mSuffix);
		classText.append(mClassName);
		if(mBaseClassName!=null&&mBaseClassName.length()>1){
			classText.append(" extends " + mBaseClassName);
		}
		classText.append("{\n");
		
		for(Variable var:mVarList){
			classText.append(var.getVariableDefString());
			classText.append("\n");
		}
		classText.append(generateInsertMethod());
		classText.append(generateQueryMethod());
		classText.append("}\n");
	
		return classText.toString();
	}
	
	/**
	 * 插入数据方法
	 * @return
	 */
	private String generateInsertMethod(){
		//" String sql = \"INSERT INTO tab_table(ID,NAME) VALUES( \"+model.id+\",'\" +model.name+\"')\";"
		StringBuffer method = new StringBuffer();
		method.append("public static void insert("+mClassName+" model){\n");
		method.append("String sql = \"INSERT INTO "+mTableName+"(");
		StringBuffer buffColNames = new StringBuffer();
		StringBuffer buffColValues = new StringBuffer();
		for(Variable var:mVarList){
			buffColNames.append(var.name+",");
			if(var.getVarType().equals("String"))
				buffColValues.append("'\"+model."+var.getVarName()+"+\"',");
			else	
				buffColValues.append("\"+model."+var.getVarName()+"+\",");
		}
		buffColNames.deleteCharAt(buffColNames.length()-1);
		int index = buffColValues.lastIndexOf(",");
		buffColValues.deleteCharAt(index);
		method.append(buffColNames);
		method.append(") VALUES ( ");
		method.append(buffColValues);
		method.append(")\";\n");
		method.append("//请补充sql执行语句\n");
		method.append("}\n");
		return method.toString();
	}
	
	/**
	 * 查询方法
	 * @return
	 */
	private String generateQueryMethod(){
		StringBuffer method = new StringBuffer();
		method.append("public static List<"+mClassName+"> findBySQL(String sql){\n");
		method.append("//请补充sql执行语句\n");
		method.append("Cursor cursor = mSQLHelper.query(sql);\n");
		method.append("List<"+mClassName+"> list = new ArrayList<>();\n");
		method.append("  while(cursor.moveToNext()){\n");
		method.append("    "+mClassName+" model = new "+mClassName+"();\n");
		for(Variable var:mVarList){
			method.append("       model."+var.getVarName()+"= cursor.get"+Util.firstCharToUpCase(var.getVarType())
			+"(cursor.getColumnIndex(\""+var.name+"\"));\n");
		}
		method.append("     list.add(model);\n");
		method.append("  }\n");
		method.append("  return list;\n");
		method.append("}\n");
		return method.toString();
	}
	
	
	
	/**
	 * 生成类名，首字母大写，其它小写，去掉下划线
	 * @param name
	 * @return
	 */
	private static String generateName(String tableName,String prefix,String suffix){
		StringBuffer strbf = new StringBuffer(prefix);
		String[] array = tableName.split("_");
		for(int i=0;i<array.length;i++){
			String str = array[i];
			str = str.toLowerCase();
			str = Util.firstCharToUpCase(str);
			strbf.append(str);
		}
		strbf.append(suffix);
		return strbf.toString();
	}
	
	/**
	 * 增加变量
	 * @param var
	 */
	public void addVariable(Variable var){
		mVarList.add(var);
	}
	
	
}
