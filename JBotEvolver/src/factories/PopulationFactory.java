package factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.zip.GZIPInputStream;
import simulation.Simulator;
import simulation.util.Arguments;
import evolutionaryrobotics.populations.Population;

public class PopulationFactory extends Factory implements Serializable {
	
	public PopulationFactory(Simulator simulator) {
		super(simulator);
	}

	public Population getPopulation(Arguments args) throws Exception {
		
		if(args.getArgumentIsDefined("load")) {
			return loadPopulationFromFile(args);
		}
		
		if (!args.getArgumentIsDefined("name"))
			throw new RuntimeException("Population 'name' not defined: "+args.toString());

		String populationName = args.getArgumentAsString("name");

		try {
			Constructor<?>[] constructors = Class.forName(populationName).getDeclaredConstructors();
			for (Constructor<?> constructor : constructors) {
				Class<?>[] params = constructor.getParameterTypes();
				if (params.length == 2 && params[0] == Simulator.class && params[1] == Arguments.class) {
					return (Population) constructor.newInstance(args);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		throw new RuntimeException("Unknown population: " + populationName);
	}

	private Population loadPopulationFromFile(Arguments args) throws Exception{
		File f = new File(args.getArgumentAsString("load"));
		
		File populationFile = new File(args.getArgumentAsString("parentfolder")+"/populations/"+f.getName());
		
		if(!populationFile.exists())
			populationFile = new File(args.getArgumentAsString("parentfolder")+"/../populations/"+f.getName());
		
		FileInputStream fis = new FileInputStream(populationFile);
		GZIPInputStream gzipIn = new GZIPInputStream(fis);
		ObjectInputStream in = new ObjectInputStream(gzipIn);
		Population population = (Population) in.readObject();
		in.close();
		return population;
	}
}