import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SudokuCheck 
{
	//Change the "FILENAME" to yours. Here are some examples
	final static String FILENAME   = "C:\\projectsSpace\\SudokuCheck\\SudokuCheck\\test2.txt";
//	final static String FILENAME   = "C:\\projectsSpace\\SudokuCheck\\SudokuCheck\\test1.txt";
//	final static String FILENAME   = "C:\\projectsSpace\\SudokuCheck\\SudokuCheck\\test.txt";
	
	final static String DELIMITING = ",";
	final static String REGEX      = "[1-9]";
	final static int ARRAY_LENGTH  = 9;
	
	final static String ERROR_TYPE_LENGTH   = "1";
	final static String ERROR_TYPE_DIGITAL  = "2";
	final static String ERROR_TYPE_DUP      = "3";
	final static String ERROR_TOO_MANY_ROWS = "4";
	final static String ERROR_TOO_LESS_ROWS = "5";
	
	//Generated solution.
	static String[][] solution = new String[ARRAY_LENGTH][ARRAY_LENGTH];
	
	public void SudokuCheck()
	{		
	}
	
	public static void main(String[] args) 
	{
		SudokuCheck _sudokuCheck = new SudokuCheck();
		
		if(_sudokuCheck.isValidSolutionSudokuPuzzle(FILENAME)) 
		{
			System.out.println("\n" + "============= Summary the final result for validation check =============" +
							   "\n" + "The contents of the file is a valid solution to a Sudoku puzzle.");
		}
		else
		{
			System.out.println("\n" + "============= Summary the final result for validation check =============" +
					   		   "\n" + "The contents of the file is not a valid solution to a Sudoku puzzle.");
		}
	}
	
	public boolean isValidSolutionSudokuPuzzle(String fileName)
	{
		boolean  valid_solution = true;
		String ERRORR_TYPE = "";

		try (Scanner input = new Scanner(new File(fileName))) 
		{			
			int rowCont = 0;
			String rowArray[];
			
			System.out.println("===================== Validation check for each row =====================");
			//- Step 1:
			//	Read data from file by line, then validate each line, 
			//	finally store each line into solution[][] if validation checking successfully.
			while(input.hasNextLine())
			{
				//Ignore empty lines in file
				String sCurrentLine = input.nextLine();
				if(sCurrentLine == null || "".equals(sCurrentLine.trim())) continue;
				
				//1). Check if there are more than 9 lines in file
				if(rowCont >= ARRAY_LENGTH)	
				{
					valid_solution = false;
					ERRORR_TYPE = ERROR_TOO_MANY_ROWS;
					break;					
				}
								
				//Change the line to array
				rowArray = sCurrentLine.replaceAll("\\s", "").split(DELIMITING);
				
				//2). Check if the length of each row is 9
				if(rowArray.length != ARRAY_LENGTH) 
				{
					valid_solution = false;
					ERRORR_TYPE = ERROR_TYPE_LENGTH;
					
					System.out.println("[" + sCurrentLine + "] -- Error found in line " + rowCont + " for reason of array length!!");
					break;
				}
				
				//3). Check each element in rowArray if it is a single digital number of 1 to 9 
				if(!singleDigitCheck(rowArray))
				{
					valid_solution = false;
					ERRORR_TYPE = ERROR_TYPE_DIGITAL;
					System.out.println("[" + sCurrentLine + "] -- Error found in line " + rowCont + " for reason of invlidation elements!!");
					break;
				}
				
				//4). Check if there are any duplicate elements in each row in rowArray
				if(findDup(rowArray))
				{
					valid_solution = false;
					ERRORR_TYPE = ERROR_TYPE_DUP;
					System.out.println("[" + sCurrentLine + "] -- Error found in line " + rowCont + " for reason of duplicate elements!!");
					break;
				}
				
				//5). After validation checking successfully for each row, store the row to solution[][]
				solution[rowCont] = rowArray;

				System.out.println("row " + rowCont + " -- [" + sCurrentLine + "] -- valid = " + valid_solution);

				rowCont++;
			}
			
			System.out.println("\n" + "==================== Validation check for each column ===================");
			//Step 2:
			//	If validation checking for each line and store each line into solution[][] successfully,
			//	then do validation checking for each column in solution[][],
			//	otherwise, print out error message.
			if(valid_solution)
			{
				if(rowCont == ARRAY_LENGTH)
				{
					//Do validation check for each columns in solution[][]
					for(int i = 0; (i < solution.length && valid_solution); i++)
					{
						List<String> _arrayList = new ArrayList<String>();
						for(int j = 0; (j < solution[i].length); j++)
						{
							_arrayList.add(solution[j][i]);
						}
						
						valid_solution = !findDup(_arrayList);
						System.out.println("column " + i + " -- " + _arrayList.toString() + " -- valid = " + valid_solution);
					}			
					
					if(!valid_solution)
					{
						System.out.println("\n" + "======================== Summary validation check =======================" +
								   		   "\n" + "Error -- duplicate data found in column!!");						
					}
				}
				else
				{
					System.out.println("\n" + "======================== Summary validation check =======================" +
									   "\n" + "Error -- Too less rows!!");
					valid_solution = false;
				}
			}
			else
			{
				System.out.println("\n" + "======================== Summary validation check =======================");
				switch (ERRORR_TYPE) 
				{
					case ERROR_TYPE_LENGTH:  
							System.out.println("Error -- invalidate array length!!");
			                break;
					case ERROR_TYPE_DIGITAL:
							System.out.println("Error -- invalidate data type!!");
			                break;
					case ERROR_TYPE_DUP:
							System.out.println("Error -- duplicate data found in row!!");
			                break;
					case ERROR_TOO_MANY_ROWS:
							System.out.println("Error -- Too many lines in file!!");
			                break;
				}
			}
		} 
		catch (IOException e) 
		{
			valid_solution = false;
			e.printStackTrace();
		}
		
		return valid_solution;		
	}
	
	private static boolean singleDigitCheck(String[] strArray)
	{
		boolean yesOrNo = true;
		for(String str : strArray)
		{
			Pattern pattern = Pattern.compile(REGEX);
			yesOrNo = pattern.matcher(str).matches();
			if(!yesOrNo) break;
		}
		
		return yesOrNo;
	}
	
	private static boolean findDup(String[] rowArray)
	{
		List<String> stringList = new ArrayList<String>(Arrays.asList(rowArray)); 
		return findDup(stringList);
	}
	
	private static boolean findDup(List<String> stringList)
	{
		return (stringList.size() != stringList.stream().distinct().count());
	}

}
