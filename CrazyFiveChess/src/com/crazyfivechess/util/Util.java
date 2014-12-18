package com.crazyfivechess.util;

import com.crazyfivechess.Constant;

public class Util {
	public static double[] tupleScoreTable = new double[11]; 
	public static int[][] AIxy = new int[5][2];	
	static{
		tupleScoreTable[0] = 7;  
		tupleScoreTable[1] = 35;  
		tupleScoreTable[2] = 800;  
		tupleScoreTable[3] = 15000;  
		tupleScoreTable[4] = 800000;  
		tupleScoreTable[5] = 15;  
		tupleScoreTable[6] = 400;  
		tupleScoreTable[7] = 1800;  
		tupleScoreTable[8] = 100000;  
		tupleScoreTable[9] = 0;  
		tupleScoreTable[10] = 0;
	}
	
	public static void getTuple(){
		double minscore = 0; 
		int[] score = new int[572];
		int[][][] tuple = new int[572][5][2];
		int count = 0;
		double temp;
		//竖五元组
		for(int i=0;i<=Constant.lineNum;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				tuple[count][0][0] = i;   
				tuple[count][0][1] = j;	
				tuple[count][1][0] = i;   
				tuple[count][1][1] = j+1;
				tuple[count][2][0] = i;   
				tuple[count][2][1] = j+2;
				tuple[count][3][0] = i;   
				tuple[count][3][1] = j+3;
				tuple[count][4][0] = i;   
				tuple[count][4][1] = j+4;		
				temp = checkScore(tuple[count]);
				if(minscore <  temp){
					minscore = temp;
					AIxy = tuple[count];
				}
				count++;
			}
		}
		
		//横五元组
		for(int i=0;i<=Constant.lineNum-4;i++){
			for(int j=0;j<=Constant.lineNum;j++){
				tuple[count][0][0] = i;   
				tuple[count][0][1] = j;	
				tuple[count][1][0] = i+1;   
				tuple[count][1][1] = j;
				tuple[count][2][0] = i+2;   
				tuple[count][2][1] = j;
				tuple[count][3][0] = i+3;   
				tuple[count][3][1] = j;
				tuple[count][4][0] = i+4;   
				tuple[count][4][1] = j;
				temp = checkScore(tuple[count]);
				if(minscore <  temp){
					minscore = temp;
					AIxy = tuple[count];
				}
				count++;
			}
		}		
		
		//右斜五元组
		for(int i=0;i<=Constant.lineNum-4;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				tuple[count][0][0] = i;   
				tuple[count][0][1] = j;	
				tuple[count][1][0] = i+1;   
				tuple[count][1][1] = j+1;
				tuple[count][2][0] = i+2;   
				tuple[count][2][1] = j+2;
				tuple[count][3][0] = i+3;   
				tuple[count][3][1] = j+3;
				tuple[count][4][0] = i+4;   
				tuple[count][4][1] = j+4;
				temp = checkScore(tuple[count]);
				if(minscore <  temp){
					minscore = temp;
					AIxy = tuple[count];
				}
				count++;
			}
		}		
		
		//左斜五元组
		for(int i=4;i<=Constant.lineNum;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				tuple[count][0][0] = i-4;   
				tuple[count][0][1] = j+4;	
				tuple[count][1][0] = i-3;   
				tuple[count][1][1] = j+3;
				tuple[count][2][0] = i-2;   
				tuple[count][2][1] = j+2;
				tuple[count][3][0] = i-1;   
				tuple[count][3][1] = j+1;
				tuple[count][4][0] = i ;   
				tuple[count][4][1] = j;
				temp = checkScore(tuple[count]);
				if(minscore <  temp){
					minscore = temp;
					AIxy = tuple[count];
				}
				count++;
			}
		}				
	}
	
	private static double checkScore(int[][] temp){
		int black = 0;
		int white = 0;
		
		for(int i=0;i<5;i++){			
			if(Constant.pan[temp[i][0]][temp[i][1]]==1){
				white++;
			}else if(Constant.pan[temp[i][0]][temp[i][1]]==2){
				black++;
			}
		}
		
		if(white >0 && black>0){
			return 0.0;
		}else if(white == 4){
			return tupleScoreTable[8];
		}else if(black == 4){
			return tupleScoreTable[4];
		}else if(white == 3){
			return tupleScoreTable[7];
		}else if(black == 3){
			return tupleScoreTable[3];
		}else if(white == 2){
			return tupleScoreTable[6];
		}else if(black == 2){
			return tupleScoreTable[2];
		}else if(white == 1){
			return tupleScoreTable[5];
		}else if(black == 1){
			return tupleScoreTable[1];
		}else if(white==0 && black==0){
			return tupleScoreTable[0];
		}
		
		return 0.0;
	}
	
	public static void getAI(){
		getTuple();
		for(int i=4;i>=0;i--){
			if(Constant.pan[AIxy[i][0]][AIxy[i][1]]==0){
				Constant.pan[AIxy[i][0]][AIxy[i][1]]=2;
				return;
			}
		}		
	}

	public static int[] getCloseXY(float x,float y){
		double min = Constant.originalMin;
		int[] result = new int[2];
		result[0] = result[1] = -1;
		for(int i=0;i<=Constant.lineNum;i++){
			for(int j=0;j<=Constant.lineNum;j++){
				float ox = Constant.padding+i*Constant.rect;
				float oy = Constant.paddingTop+j*Constant.rect;
				double temp = (x-ox)*(x-ox) + (y-oy)*(y-oy);
				if(temp < min && temp<=Constant.maxDistance){
					min = temp;					
					result[0] = i;
					result[1] = j;
				}
			}
		}		
		return result;
	}		
	
	public static boolean checkWin(){
		if(checkShu() || checkHeng() || checkYouXie() || checkZuoXie()) return true;
		return false;
	}
	
	//横
	private static boolean checkHeng(){
		for(int i=0;i<=Constant.lineNum-4;i++){
			for(int j=0;j<=Constant.lineNum;j++){
				if(Constant.pan[i][j]==1 
						&& Constant.pan[i+1][j]==1
						&& Constant.pan[i+2][j]==1
						&& Constant.pan[i+3][j]==1
						&& Constant.pan[i+4][j]==1){
					Constant.whowin = 1;
					return true;
				}else if(Constant.pan[i][j]==2
						&& Constant.pan[i+1][j]==2
						&& Constant.pan[i+2][j]==2
						&& Constant.pan[i+3][j]==2
						&& Constant.pan[i+4][j]==2){
					Constant.whowin = 2;
					return true;
				}
			}
		}			
		return false;
	}
	
	//竖
	private static boolean checkShu(){
		for(int i=0;i<=Constant.lineNum;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				if(Constant.pan[i][j]==1 
						&& Constant.pan[i][j+1]==1
						&& Constant.pan[i][j+2]==1
						&& Constant.pan[i][j+3]==1
						&& Constant.pan[i][j+4]==1){
					Constant.whowin = 1;
					return true;
				}else if(Constant.pan[i][j]==2
						&& Constant.pan[i][j+1]==2
						&& Constant.pan[i][j+2]==2
						&& Constant.pan[i][j+3]==2
						&& Constant.pan[i][j+4]==2){
					Constant.whowin = 2;
					return true;
				}
			}
		}			
		return false;
	}
	
	//右斜
	private static boolean checkYouXie(){
		for(int i=0;i<=Constant.lineNum-4;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				if(Constant.pan[i][j]==1 
						&& Constant.pan[i+1][j+1]==1
						&& Constant.pan[i+2][j+2]==1
						&& Constant.pan[i+3][j+3]==1
						&& Constant.pan[i+4][j+4]==1){
					Constant.whowin = 1;
					return true;
				}else if(Constant.pan[i][j]==2
						&& Constant.pan[i+1][j+1]==2
						&& Constant.pan[i+2][j+2]==2
						&& Constant.pan[i+3][j+3]==2
						&& Constant.pan[i+4][j+4]==2){
					Constant.whowin = 2;
					return true;
				}
			}
		}			
		return false;
	}
	
	//左斜
	private static boolean checkZuoXie(){
		for(int i=4;i<=Constant.lineNum;i++){
			for(int j=0;j<=Constant.lineNum-4;j++){
				if(Constant.pan[i][j]==1 
						&& Constant.pan[i-1][j+1]==1
						&& Constant.pan[i-2][j+2]==1
						&& Constant.pan[i-3][j+3]==1
						&& Constant.pan[i-4][j+4]==1){
					Constant.whowin = 1;
					return true;
				}else if(Constant.pan[i][j]==2
						&& Constant.pan[i-1][j+1]==2
						&& Constant.pan[i-2][j+2]==2
						&& Constant.pan[i-3][j+3]==2
						&& Constant.pan[i-4][j+4]==2){
					Constant.whowin = 2;
					return true;
				}
			}
		}			
		return false;
	}
}
