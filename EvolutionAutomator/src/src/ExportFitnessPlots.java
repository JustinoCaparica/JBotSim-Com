package src;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class ExportFitnessPlots {
	
	public static void main( String[] args ) throws Exception{
		
            
            
            
            

//		String f = "bigdisk/evorbc2/multimaze/"; String[] setups = new String[]{f+"repertoire_obstacle/"};
//		String f = "bigdisk/evorbc2/qualitymetrics/maze_"; String[] setups = new String[]{f+"radial/",f+"distance/",f+"quality/"};
//		String f = "bigdisk/evorbc2/repertoiresize/maze_quality_"; String[] setups = new String[]{f+"5/",f+"10/",f+"20/",f+"30/",f+"50/",f+"100/"};
//		String f = "bigdisk/behaviormapping/maze_type"; String[] setups = new String[]{/*f+"0_20/",f+"1_20/",*/f+"3_20/"};
//		String f = "bigdisk/orientation/"; String[] setups = new String[]{f+"maze_orientation/"};

            String f = "../../experiments/BasicExperiments/";
            String[] setups = new String[]{ //f + "CooperativeForaging-Social-CircleForaging/CooperativeForaging-Social-CircleForaging/"};//,
                                            f + "CooperativeForaging-NonSocial-CircleForaging/CooperativeForaging-NonSocial-CircleForaging/"};//,
                                            //f + "CooperativeForaging-Social-RecruiterOnly/" };

            
            
            File outFile = new File( f + "/" + "results.txt" );
            OutputStreamWriter writer = new FileWriter( outFile );
            
            
            String header = "Setup\tRun\tGeneration\tHighestFitness\tAverageFitness\tLowestFitness";
            System.out.println( header );
            writer.write( header + System.lineSeparator() );
            
            
            
            
            

            for(String setupFolder : setups){
                System.out.println("Writing " + setupFolder);
                new ExportFitnessPlots( setupFolder, writer );
            }
                        
                
            System.out.println("Done. Results writen.");
                    
	}
	
        
        
    /**
     * 
     * @param folder location of
     * results files
     * @param out a writer to write
     * the results
     * @throws Exception 
     */
    public ExportFitnessPlots( String folder, OutputStreamWriter out ) throws Exception{

        File f = new File( folder );

        String result = "";

        for( String s : f.list() ) {
                result += checkSubFolders( new File(folder+s) );
        }

        if ( out != null ) {
            out.write( result );
        }else{
            System.out.print( result );
        }

    }
        
        
        
        
	
	private String checkSubFolders(File folder) throws Exception {
		
		StringBuffer result = new StringBuffer();
		
		if(folder.list() == null) {
			return result.toString();
		}
		
		for(String f : folder.list()) {
			
			String fn = folder.getPath()+"/"+f;
			
			if(f.equals("_fitness.log")) {
				result.append( getFitnessPlot(folder.getPath()) );
			} else if(new File(fn).isDirectory()) {
				result.append(checkSubFolders(new File(fn)));
			}
		}
		
		return result.toString();
	}
	
        
        
        
        
        
	private String getFitnessPlot(String folder) throws Exception {
		
		StringBuffer result = new StringBuffer();
		
		if(!new File(folder+"/_fitness.log").exists())
			return result.toString();
		
		Scanner s = new Scanner(new File(folder+"/_fitness.log"));
		
		String prevLine = "";
		
		String[] split = folder.split("/");
		String prefix = split[split.length-3]+"#"+split[split.length-2]+"\t"+split[split.length-1];
		
		while(s.hasNextLine()) {
			String line = s.nextLine().trim().replaceAll("\\s+", "\t");
			
			String newLine = prefix+"\t"+line;
			newLine = newLine.replaceAll("Actuator", "");
			newLine = newLine.replaceAll("_maze", "");
			
			if(!line.startsWith("#") && !line.equals(prevLine) && !line.isEmpty()) {
				result.append(newLine);
				result.append("\n");
				prevLine = line;
			}
		}
		
		s.close();
		
		return result.toString();
	}	
}