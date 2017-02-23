package gamePlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Utilities.Util;
import constants.Constants;

public class ReadFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String filename = null;
	public String[] Category;//stores the categories 
	public String[] PointValue;//stores the point values
	String[] Info = new String[28];//stores each line as a string	
	public String[][] Question = new String [26][];//2D String array of category, point value, question, answer of each question
	protected Scanner input;//stores file input
	public String categoryImage;
	public String enableImage;
	public String disableImage;
	public int totalpeople, totalrating;
	private File file;

	//constructor
	protected ReadFile(File file) throws FileNotFoundException{
		input = new Scanner(file);
		this.file = file;
	 }
	protected void Parse(){
			String lastbutone = new String(""); 
			String lastone = new String("");
			String in = new String(input.nextLine());
			while(in.trim().equals("")){
				in = input.nextLine();
			}
			String[]t = in.split("::");
			categoryImage = t[5];
			Constants.categoryImage =this.categoryImage;
//			Util.ps(Constants.categoryImage);
			Category = new String[5];
			for(int i = 0;i <5; i++){
				Category[i] = t[i];
			}
			if(t.length!=6){
				 JOptionPane.showMessageDialog(new JFrame(),
						 "Wrong number of categories or no category image input");
				 return;
			 }

			in = input.nextLine();
			while(in.trim().equals("")){
				in = input.nextLine();
			}
			t = in.split("::");
			if(t.length!=7){
				 JOptionPane.showMessageDialog(new JFrame(),
						 "Wrong number of categories or no button image input");
				 return;
			 }
			PointValue = new String[5];
			for(int i = 0;i <5; i++){
				PointValue[i] = t[i];
			}
			enableImage = t[5];
			Constants.enableImage = this.enableImage;
			disableImage = t[6];
			Constants.disableImage = this.disableImage;
			 Arrays.sort(Category);
			 Arrays.sort(PointValue);
			 //terminate if # of categories is not 5
			 if(Category.length!=5){
				 JOptionPane.showMessageDialog(new JFrame(),
						 "Wrong number of categories");
				 return;
			 }
			 //terminate if # of value points is not 5
			 if(PointValue.length!=5){
				 JOptionPane.showMessageDialog(new JFrame(),
						 "Wrong number of point values");
				 return;
			 }
			 for(int i = 0; i<3; i++){
				 for(int j = i+1; j<5; j++){
					 //terminate if there are duplicate categories
					 if(Category[i].equalsIgnoreCase(Category[j])){
						 JOptionPane.showMessageDialog(new JFrame(),
								 "Duplicate categories");
						 return;
					 }
					 //terminate if there are duplicate point values
					 if(PointValue[i].equalsIgnoreCase(PointValue[j])){
						 JOptionPane.showMessageDialog(new JFrame(),
								 "Duplicate point value");
						 return;
					 }
				 }
			 }
			//read the questions in
			int QNumber = 0;
			while(input.hasNextLine()){
				String temp = new String(input.nextLine());
				if(temp.trim().length()>0){
					if(temp.startsWith("::")){
						QNumber++;
						Info[QNumber-1] = temp;
						if(QNumber>26){
							JOptionPane.showMessageDialog(new JFrame(),
									 "Incorrect number of questions");
							return;
						}
					}
					else{
						if(QNumber==26){
							Info[QNumber-1] = Info[QNumber-1].concat(lastbutone);
							lastbutone = lastone;
							lastone = temp;	
						}else{
							Info[QNumber-1] = Info[QNumber-1].concat(temp);

						}
					}
				}
			}
			if(lastbutone.equals("")||lastone.equals("")){
				JOptionPane.showMessageDialog(new JFrame(), "incorrect number of input");
				return;
			}
			totalpeople = Integer.parseInt(lastbutone);
			totalrating = Integer.parseInt(lastone);
			Info[26]= totalpeople+"";
			Info[27] = totalrating+"";
			
			//terminate if there are more than 26 questions
			if(QNumber!=26){
				JOptionPane.showMessageDialog(new JFrame(),
						 "Incorrect number of questions");
				return;
			}
			int counter =0;//keep track of the number of FJ questions
			for(int i = 0; i < 25; i++){
				if(Info[i].substring(2,4).equalsIgnoreCase("FJ") ){
					counter++;
					if(counter>1){
						JOptionPane.showMessageDialog(new JFrame(),
								 "There are multiple final Jeopardy question");
						return;
					}
					//move FJ question to the end
					String temp = new String(Info[i]);
					Info[i]=Info[25];
					Info[25] = temp;
				}
				//terminate if the format is not correct;
				if(Info[i].endsWith("::")){
					JOptionPane.showMessageDialog(new JFrame(),
							 "Incorrect format");
					return;
				}
			}
			Arrays.sort(Info,0,25);
			for(int i = 0; i <26; i++){
				Question[i]=Info[i].split("::");
			}
			//check if the questions meet the format
			Check();
		    if(input!=null){
		    	input.close();
		    }
	 }
	protected void Check(){
		//Terminates if there are not exactly four slots in one line.
		for(int i = 0; i <25; i++){
			if(Question[i].length!=5||!Question[i][0].equals("")){
				JOptionPane.showMessageDialog(new JFrame(),
						 "Incorrect format");
				return;
			}
			for(int j = 1; j <5; j++){
				if(Question[i][j].trim().equals("")){
					JOptionPane.showMessageDialog(new JFrame(),
							 "Missing info");
				}
			}
			//terminates if there are not exactly five different point values under five different categoties.
			for(int j = 0; j < 5; j ++){
				for(int k = 0; k<5;k++){
					if(!Question[5*j+k][1].equalsIgnoreCase(Category[j])){
						JOptionPane.showMessageDialog(new JFrame(),
							"Invalid category or "
							+ "wrong number of questions in the same category");
						return;
					}
					if(!Question[5*j+k][2].equalsIgnoreCase(PointValue[k])){
						JOptionPane.showMessageDialog(new JFrame(),
								"Invalid PointValue or "
										+ "not exactly five point values in the same category");
						return;
					}
				}	
			}
		}
		if(Question[25].length!=4||!Question[25][0].equals("")){
			JOptionPane.showMessageDialog(new JFrame(),
					 "Incorrect format");
			return;
		}
		if(!Question[25][1].equalsIgnoreCase("fj")){
			JOptionPane.showMessageDialog(new JFrame(),
					 "Missing Final Jeopardy Question");
			return;
		}
		for(int j = 1; j <4; j++){
			if(Question[25][j].trim().equals("")){
				JOptionPane.showMessageDialog(new JFrame(),
						 "Missing info");
				return;
			}
		}
	 }
	public void write(int rate, int people){
		
		try {
			PrintWriter bw = new PrintWriter(new FileWriter(file));
			for(int i = 0; i <5; i++){
				bw.print(Category[i]+"::");
			}
			bw.println(categoryImage);
			for(int i = 0; i < 5; i++){
				bw.print(PointValue[i]+"::");
			}
			bw.println(enableImage+"::"+disableImage);
			totalpeople += people;
			totalrating += rate;
			Info[26] = totalpeople+"";
			Info[27] = totalrating+"";
			for(int i = 0; i <28; i++){
				bw.println(Info[i]);
			}
			bw.flush();
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}