package cn.edu.nudt.hycloudclient.Entry;

import cn.edu.nudt.hycloudinterface.utils.helper;

public class Action {
	public final static int SPUT = 1;
	public final static int SGET = 2;
	public final static int SDEL = 3;
	public final static int SDUMP = 4;
	
	public final static int PUT = 5;
	public final static int GET = 6;
	public final static int VERIFY = 7;
	public final static int RESTORE = 8;
    public final static int RECOVERABLE = 9;

	public static int get(String atrAction) {
		int rv = -1;
		if(atrAction.equalsIgnoreCase("sput")) {
			rv = Action.SPUT;
		}else if(atrAction.equalsIgnoreCase("SGET")) {
			rv = Action.SGET;
		}else if(atrAction.equalsIgnoreCase("SDEL")) {
			rv = Action.SDEL;
		}else if(atrAction.equalsIgnoreCase("SDUMP")) {
			rv = Action.SDUMP;
		}else if(atrAction.equalsIgnoreCase("PUT")) {
			rv = Action.PUT;
		}else if(atrAction.equalsIgnoreCase("GET")) {
			rv = Action.GET;
		}else if(atrAction.equalsIgnoreCase("VERIFY")) {
			rv = Action.VERIFY;
		}else if(atrAction.equalsIgnoreCase("RESTORE")) {
			rv = Action.RESTORE;
        }else if(atrAction.equalsIgnoreCase("RECOVERABLE")) {
            rv = Action.RECOVERABLE;
		}else{
			helper.err("Error: wrong action string");
		}
		return rv;
	}
}
