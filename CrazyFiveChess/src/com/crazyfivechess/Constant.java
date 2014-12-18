package com.crazyfivechess;

import java.util.UUID;

public class Constant {
	public static final int SERVER_CONNECTING = 0;
	public static final int SERVER_CONNECT_OK = 1;
	public static final int SERVER_WIN = 2;
	public static final int SERVER_FAIL = 3;
	public static final int SERVER_CONNECT_ERROR = 4;
	
	public static final int CLIENT_CONNECTING = 0;
	public static final int CLIENT_CONNECT_OK = 1;
	public static final int CLIENT_WIN = 2;
	public static final int CLIENT_FAIL = 3;
	public static final int CLIENT_CONNECT_ERROR = 4;

	public static boolean serverOrClient = false;		
	
	public static int ScreenWidth;
	public static int ScreenHeight;
	public static int CurScreenW;
	public static int CurScreenH;
	
	public static float padding = 30;//∆Â≈Ãø’œ∂
	public static float paddingTop = 300;
	public static float rect;
	
	public static int lineNum = 14;
	
	public static int[][] pan = new int[lineNum+1][lineNum+1];
	
	public static float maxDistance;
	public static float originalMin; 
	public static int whowin = 0;
	
	public static int CUR_MODE = -1;
	public static int TWO_MODE = 2;	
	public static int ONE_MODE = 1;
	public static String address;//∂‘∑Ω¿∂—¿µÿ÷∑
	public static UUID uuid = UUID.fromString("CBCBEC96-546A-0E12-99E1-66ECF7ACE535");
}
