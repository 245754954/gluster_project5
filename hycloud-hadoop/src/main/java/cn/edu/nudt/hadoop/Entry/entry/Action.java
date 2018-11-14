package cn.edu.nudt.hadoop.Entry.entry;



public class Action {
	public final static int VERIFY = 1;

	public static int get(String atrAction) {
		int rv = -1;
		if(atrAction.equalsIgnoreCase("VERIFY")) {
			rv = Action.VERIFY;
		}
		else{

		}
		return rv;
	}
}
