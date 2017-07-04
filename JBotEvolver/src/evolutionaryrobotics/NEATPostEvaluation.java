package evolutionaryrobotics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import evolutionaryrobotics.populations.Population;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import simulation.util.Arguments;
import taskexecutor.TaskExecutor;
import taskexecutor.results.NEATPostEvaluationResult;
import taskexecutor.tasks.NEATMultipleSamplePostEvaluationTask;

public class NEATPostEvaluation {
	
	protected int runsFirstId = 0;
	protected int runsCount = 0;
	protected int samples = 100;
	protected int sampleIncrement = 100;
	protected int fitnesssamples = 1;
	protected int steps = 0;
	protected double targetfitness = 0;
	protected String dir = "";
	
	protected boolean singleEvaluation = false;
	protected boolean localEvaluation = false;
	
	protected TaskExecutor taskExecutor;
	protected String[] evolutionArgs;
	
	protected boolean showOutput = false;
	protected boolean saveOutput = true;
        
        private String evalFunctionClassName;           //evaluation function
                                                        //classname if different
                                                        //from the one used 
                                                        //previously in evolution
	
	public NEATPostEvaluation(String[] args, String[] extraArgs) {
		this(args);
		this.evolutionArgs = extraArgs;
	}
	
        /**
         * 
         * @param postEvalArguments arguments for the post evaluation,
         * obtained from the .conf file in the --postevaluation item
         */
	public NEATPostEvaluation(String[] postEvalArguments) {
                evalFunctionClassName = null;
            
		for(String s : postEvalArguments) {
			String[] a = s.split("=");
			if(a[0].equals("dir")) dir = a[1];
			if(a[0].equals("samples")) samples = Integer.parseInt(a[1]);
			if(a[0].equals("fitnesssamples")) fitnesssamples = Integer.parseInt(a[1]);
			if(a[0].equals("targetfitness")) targetfitness = Double.parseDouble(a[1]);
			if(a[0].equals("singleevaluation")) singleEvaluation = Integer.parseInt(a[1]) == 1;
			if(a[0].equals("localevaluation")) localEvaluation = Integer.parseInt(a[1]) == 1;
			if(a[0].equals("steps")) steps = Integer.parseInt(a[1]);
			if(a[0].equals("showoutput")) showOutput = (Integer.parseInt(a[1]) == 1);
			if(a[0].equals("sampleincrement")) sampleIncrement = Integer.parseInt(a[1]);
			if(a[0].equals("saveoutput")) saveOutput = Integer.parseInt(a[1]) == 1;
                        if(a[0].equals("evalFunctionClassName")) evalFunctionClassName = a[1];
		}
                
		if(steps != 0) {
			this.evolutionArgs = new String[]{"--environment","+steps="+steps,"--evaluation","+posteval=1"};
		}else {
			this.evolutionArgs = new String[]{"--evaluation","+posteval=1"};
		}
		
		if(!dir.endsWith("/"))
			dir+="/";
		
		File subFolder;
		int currentFolder = 0;
		do {
			currentFolder++;
			subFolder = new File(dir+currentFolder);
		}while(subFolder.exists());
		
		runsFirstId = 1;
		runsCount = --currentFolder;
		
		if(singleEvaluation)
			runsCount = 1;
	}
	
	
	public double[][][] runPostEval() {
		double[][][] result = null;
		
		try{
			String file = "";
			int generationsCount = 0;
			
			if(singleEvaluation){
				file=dir+"/_showbest_current.conf";
				generationsCount = getGenerationNumberFromFile(dir+"/_generationnumber");
			}else{
				file=dir+runsFirstId+"/_showbest_current.conf";
				generationsCount = getGenerationNumberFromFile(dir+runsFirstId+"/_generationnumber");
			}
			
			result = new double[runsCount][generationsCount][fitnesssamples];
			
			String[] newEvolutionArgs = evolutionArgs != null ? new String[evolutionArgs.length+1] : new String[1];
			
			newEvolutionArgs[0] = file;
			
                        
			for(int i = 1 ; i < newEvolutionArgs.length ; i++)
				newEvolutionArgs[i] = evolutionArgs[i-1];
				
                        
                    //debug
                    System.out.println("newEvolutionArgs.length=" + newEvolutionArgs.length);
                    for(int i = 0 ; i < newEvolutionArgs.length ; i++){
                        System.out.println("newEvolutionArgs[" + i + "]=" + newEvolutionArgs[i]);
                    }
                    
                    for(int i = 0 ; i < evolutionArgs.length ; i++){
                        System.out.println("evolutionArgs[" + i + "]=" + evolutionArgs[i]);
                    }
                    //end debug
                        
                        
			JBotEvolver jBotEvolver = new JBotEvolver(newEvolutionArgs);
			
			if (jBotEvolver.getArguments().get("--executor") != null) {
				if(localEvaluation)
					taskExecutor = TaskExecutor.getTaskExecutor(jBotEvolver, new Arguments("classname=ParallelTaskExecutor",true));
				else
					taskExecutor = TaskExecutor.getTaskExecutor(jBotEvolver, jBotEvolver.getArguments().get("--executor"));
				taskExecutor.start();
			}
			
			boolean setNumberOfTasks = false;
			int totalTasks = 0;
			
			StringBuffer data = new StringBuffer();
			FileWriter fw = null;
			
			if(saveOutput)
				 fw = new FileWriter(new File(dir+"/post_details.txt"));
			
                        
                        //int bestRunID = getBestRunID( dir + "/bestControllerByRun.txt" );
                        //int bestGenerationID = getBestGenerationID( dir + "/bestControllerByRun.txt" );
                        int bestGenerationID;
                        HashMap<Integer, Integer> bestGenerationByRun;
                        bestGenerationByRun = loadBestGenerationByRunFile( dir + "/bestControllerByRun.txt" );
                        
                        
//                        if (showOutput ) {
//                            System.out.println("best run:" + bestRunID );
//                            System.out.println("best generation:" + bestGenerationID );
//                    }
                        
                        
                        /**
                         * post-evaluate all runs
                         */
			for(int runID = runsFirstId ; runID <= runsCount; runID++) {
                            if ( showOutput ) {
                                System.out.println("Run " + runID);
                            }
                            if(singleEvaluation)
                                    file = dir+"/show_best/";
                            else
                                    file = dir+runID+"/show_best/";

                            File directory = new File(file);

                            //int numberOfGenerations = directory.listFiles().length;
                            int numberOfGenerations = 1;
                            
                            if(!setNumberOfTasks) {
                                    totalTasks = (runsCount - runsFirstId + 1)*fitnesssamples*samples*numberOfGenerations/sampleIncrement;
                                    taskExecutor.setTotalNumberOfTasks(totalTasks);
                                    setNumberOfTasks = true;
                            }

                            File[] bestControllersFiles = directory.listFiles();
                            sortByNumber(bestControllersFiles);

                            bestGenerationID = bestGenerationByRun.get( runID );
                            
                            int generation;
                            
                            //post-evaluate controller of the best generation
                            for (File bestControllerFile : bestControllersFiles) {
                                    generation = Integer.valueOf(bestControllerFile.getName().substring(8, bestControllerFile.getName().indexOf(".")));

                                    if ( generation != bestGenerationID ) {
                                        continue;           //this is ugly!
                                    }
                                    
                                if ( showOutput ) {
                                    System.out.println("Controller FileName:" + bestControllerFile.getName());
                                }
                                    
                                    newEvolutionArgs[0] = file + bestControllerFile.getName();
                                    jBotEvolver = new JBotEvolver(newEvolutionArgs);

                                    for(int fitnesssample = 0 ; fitnesssample < fitnesssamples ; fitnesssample++) {
                                            for(int sample = 0 ; sample < samples ; sample+=sampleIncrement) {
                                                    HashMap<String, Arguments> argsCopy = jBotEvolver.getArgumentsCopy();
                                                    if ( evalFunctionClassName != null ) {
                                                        argsCopy.put( "--evaluation", new Arguments("posteval=1,classname=" + evalFunctionClassName));
                                                    }
                                                    
                                                    JBotEvolver newJBot = new JBotEvolver(argsCopy,jBotEvolver.getRandomSeed());
                                                    
                                                    //debug
//                                                    System.out.println("argsCopy");
//                                                    for (String s :argsCopy.keySet() ) {
//                                                        System.out.println( s + ":" + argsCopy.get(s) );
//                                                    }
                                                    //end debug
                                                    
                                                    Population pop = newJBot.getPopulation();
                                                    evolutionaryrobotics.neuralnetworks.Chromosome chr = pop.getBestChromosome();
                                                    NEATMultipleSamplePostEvaluationTask t = new NEATMultipleSamplePostEvaluationTask(runID,generation,newJBot,fitnesssample,chr,sample,sample+sampleIncrement,targetfitness);
                                                    taskExecutor.addTask(t);
                                                    if(showOutput)
                                                            System.out.print( sample + " " );
                                            }
                                            if(showOutput)
                                                    System.out.println("fitnessSample:" + fitnesssample);
                                    }

                                    taskExecutor.setDescription(dir+runID+"/"+(generation+1)+" out of "+numberOfGenerations+" (total tasks: "+totalTasks+")");

                                    if(showOutput)
                                            System.out.println();
                            }
				
                            for(int gen = 0 ; gen < numberOfGenerations ; gen++) {

                                    for(int fitnesssample = 0 ; fitnesssample < fitnesssamples ; fitnesssample++) {

                                            for(int sample = 0 ; sample < samples ; sample+=sampleIncrement) {

                                                    NEATPostEvaluationResult sfr = (NEATPostEvaluationResult)taskExecutor.getResult();
                                                    result[sfr.getRun()-1][sfr.getGeneration()][sfr.getFitnesssample()]+= sfr.getFitness()*sampleIncrement/samples;
                                                    String line = sfr.getRun()+" "+sfr.getGeneration()+" "+sfr.getFitnesssample()+" "+sfr.getSample()+" "+sfr.getFitness()+"\n";
                                                    data.append(line);

                                                    if(showOutput) {
                                                            System.out.print(line);
                                                    }
                                            }
                                    }
                            }
			}
			
			if(showOutput)
				System.out.println();
			
			if(saveOutput) {
				fw.append(data);
				fw.close();
			}
			
			/*
			for(int i = runsFirstId ; i <= runsCount ; i++) {
	
				if(singleEvaluation)
					file = dir+"/show_best/";
				else
					file = dir+i+"/show_best/";
				
				File directory = new File(file);
				
				for (int j = 0; j < directory.listFiles().length; j++) {
					for(int fitnesssample = 0 ; fitnesssample < fitnesssamples ; fitnesssample++) {
						for(int sample = 0 ; sample < samples ; sample++) {
							NEATPostEvaluationResult sfr = (NEATPostEvaluationResult)taskExecutor.getResult();
							if(showOutput)
								System.out.println(sfr.getRun()+" "+sfr.getGeneration()+" "+sfr.getFitnesssample()+" "+sfr.getSample()+" "+sfr.getFitness());
							result[sfr.getRun()-1][sfr.getFitnesssample()][sfr.getGeneration()]+= sfr.getFitness()/samples;
						}
					}
				}
				
			}*/
				
			taskExecutor.stopTasks();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	 public void sortByNumber(File[] files) {
	        Arrays.sort(files, new Comparator<File>() {
	            @Override
	            public int compare(File o1, File o2) {
	                int n1 = extractNumber(o1.getName());
	                int n2 = extractNumber(o2.getName());
	                return n1 - n2;
	            }

	            private int extractNumber(String name) {
	                int i = 0;
	                name = name.replace("showbest", "");
	                name = name.replace(".conf", "");
	                try {
	                    i = Integer.parseInt(name);
	                } catch(Exception e) {
	                    i = 0; // if filename does not match the format
	                           // then default to 0
	                }
	                return i;
	            }
	        });

	    }
	
	protected int getGenerationNumberFromFile(String file) {
		Scanner s = null;
		try {
			 s = new Scanner(new File(file));
			
			 return Integer.valueOf(s.next()) + 1;
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(s != null)
				s.close();
		}
		return 0;
	}

        
        /**
         * Gets the ID of the best run from a file
         * @param path the path to the file
         * @return the id of the best run
         */
    private int getBestRunID( String path ) throws FileNotFoundException, IOException {
        
        File file = new File( path );
        BufferedReader fr = new BufferedReader( new FileReader(file) );
        fr.readLine();
        String bestRunLine = fr.readLine();
        return Integer.parseInt( bestRunLine.split(",")[0] );
        
    }

    
    /**
         * Gets the ID of the best generation of 
         * the best run from a file
         * @param path the path to the file
         * @return the id of the best generation
         * of the best run
         */
    private int getBestGenerationID( String path ) throws FileNotFoundException, IOException {
        
        File file = new File( path );
        BufferedReader fr = new BufferedReader( new FileReader(file) );
        fr.readLine();
        String bestRunLine = fr.readLine();
        return Integer.parseInt( bestRunLine.split(",")[1] );
        
    }

    
    /**
     * Loads the best generation by run file
     * to a data structure. If there are
     * more than one generations tied as
     * the best, only one of them gets loaded;
     * the others are ignored.
     * @param path path to the file
     * @return a Map where the keys are
     * the runs IDs and the values are
     * the generations IDs
     */
    private HashMap<Integer, Integer> loadBestGenerationByRunFile( String path ) 
                        throws FileNotFoundException, IOException {
        
        HashMap<Integer, Integer> map;              //map to hold loaded file
        map = new HashMap<>();
        
        File file = new File( path );               //load the file
        BufferedReader fr = new BufferedReader( new FileReader(file) );
        
        fr.readLine();                              //skip the header line
        String bestRunLine = fr.readLine();         //read first data line
        
        
        int run, generation;
        while( bestRunLine != null ){
            run = Integer.parseInt( bestRunLine.split(",")[0] );
            generation = Integer.parseInt( bestRunLine.split(",")[1] );
            map.put( run, generation );             //load data to map
            bestRunLine = fr.readLine();            
        }

        return map;
        
    }


}
