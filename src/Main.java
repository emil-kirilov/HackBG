import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		ArrayList<HierarchyUnit> packagesInfo = new ArrayList<HierarchyUnit>();

		// Finding the dependencies information
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(
					"resources/all_packages.json"));

			String line;

			// Reading the file line by line 
			while ((line = bfr.readLine()) != null) {
				HierarchyUnit unit = new HierarchyUnit();
			
				
				Pattern searchPattern = Pattern.compile("\"(\\w+)\"\\:\\s\\[(\"\\w+\"(,?\\s?\"\\w+\")*)\\]");
				Matcher matchString = searchPattern.matcher(line);

				//Creating Hierarchy objects 
				if (matchString.find()) {
					line = matchString.group(1);
					
					
					unit.setKey(line);
					unit.getKey();
					
					
					line = matchString.group(2);
					searchPattern= Pattern.compile("(\\w+)");
					matchString = searchPattern.matcher(line);

				while(matchString.find()) {
					line = matchString.group(1);
					unit.addValue(line);
				}
				packagesInfo.add(unit);
				}
			}
			
			bfr.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("File cannot be read!");
		}
		
		
		ArrayList<String> packegesToInstall = new ArrayList<String>();
		
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(
					"resources/dependencies.json"));

			String line;

			while ((line = bfr.readLine()) != null) {
				Pattern searchPattern = Pattern.compile("\\[\"(.+\")\\]");
				Matcher matchString = searchPattern.matcher(line);

				if (matchString.find()) {
					line = matchString.group(1);
					
					searchPattern= Pattern.compile("(\\w+)");
					matchString = searchPattern.matcher(line);

					while(matchString.find()) {
						line = matchString.group(1);
						packegesToInstall.add(line);
					}
				
				}
			}
			
			bfr.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} catch (IOException e) {
			System.out.println("File cannot be read!");
		}

		//Starts installing 
		for(int i = 0; i < packegesToInstall.size(); i++ ) {
			String packageName = packegesToInstall.get(i);
			String path = "resources/installed_modules/"+packageName;
			
			File file = new File("resources/installed_modules");

			String[] names = file.list();
			
			//saves the names of packages already installed
			ArrayList<String> alreadyInstalled = new ArrayList<String>();
			for(int j = 0; j < names.length; j++)
			{
			    if (new File("resources/installed_modules/" + names[j]).isDirectory())
			    {
			    	alreadyInstalled.add(names[j]);
			    }
			}
			
			
			System.out.println("Installing "+ packageName + ".");
			
			if (!new File(path).exists()) {
			   for(int j = 0; j < packagesInfo.size(); j++) {
				   // Checks for necessary packages
				   if(packageName.equals(packagesInfo.get(j).getKey())) {
					   
					   System.out.print("In order to install " + packageName + ", we need ");
					   
					   ArrayList<String> installFirst = new ArrayList<String>();
					   for(int k = 0; k < packagesInfo.get(j).values.size(); k++) {
						   installFirst.add(packagesInfo.get(j).values.get(k));
						  
						   if(k == 0) {
							   System.out.print(packagesInfo.get(j).values.get(k));
						   } else {
							   System.out.print(" and " + packagesInfo.get(j).values.get(k));
						   }
					   }
					   System.out.println(".");
					   
					   for(int n = 0; n < installFirst.size(); n++) {
						   for( int l = 0; l < alreadyInstalled.size(); l++)
							   if (!installFirst.get(n).equals(alreadyInstalled.get(l))) {
								   for(int m = 0; m < packagesInfo.size(); m++) {
									   
									   //  Trying to install the packages one step deeper in the Hierarchy
									   if(packagesInfo.get(m).getKey().equals(installFirst.get(n))) {
										   System.out.println("Installing " + installFirst.get(n) + ".");
										   System.out.print("In order to install " + installFirst.get(n) + ", we need ");
									   }
								   }
								   
								   //Finds out what needs to be installed one more step deeper in the Hierarchy
								   for(int t = 0; t < packagesInfo.size(); t++) {
									   if(packagesInfo.get(t).getKey().equals(installFirst.get(n))) {
												  
												   for(int r = 0; r < packagesInfo.get(t).values.size();r++)
													   if(r == 0) {
														   System.out.println(packagesInfo.get(t).getValueAt(r) + ".");	
														   		// Checks whether we need to install the already installed packages
														   		for( int q = 0; q < alreadyInstalled.size(); q++) {
														   			if (packagesInfo.get(t).getValueAt(r).equals(alreadyInstalled.get(q))) {
														   				System.out.println(packagesInfo.get(t).getValueAt(r) + " is already installed.");
														   			} else {
														   				System.out.println("Installing " + packagesInfo.get(t).getValueAt(r) + ".");
														   		}
														   		}
													   } else {
														   System.out.print(" and " + packagesInfo.get(t).getValueAt(r));	
														   System.out.println("Installing " + packagesInfo.get(t).getValueAt(r) + ".");
													   }
										   }
								   }
								   
							   } else {
								   System.out.println(installFirst.get(n) + " is already installed.");
							   }
					   }
				   }
			   }
			} else {
				System.out.println(packageName + " is already installed.");
			
			}
			
			System.out.print("All done.");
		}
	}

}
