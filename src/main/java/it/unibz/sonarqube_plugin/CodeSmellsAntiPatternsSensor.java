package it.unibz.sonarqube_plugin;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.*;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;

import padl.creator.javafile.eclipse.astVisitors.ConditionalModelAnnotator;
import padl.creator.javafile.eclipse.astVisitors.LOCModelAnnotator;
import padl.kernel.IAbstractLevelModel;
import padl.kernel.ICodeLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceBuilder;
import ptidej.solver.OccurrenceComponent;
import padl.creator.javafile.eclipse.CompleteJavaFileCreator;
import sad.designsmell.detection.IDesignSmellDetection;

import util.io.ProxyConsole;
import util.io.ProxyDisk;
import util.io.ReaderInputStream;

public class CodeSmellsAntiPatternsSensor implements Sensor {

	private static Logger LOG = LoggerFactory.getLogger(CodeSmellsAntiPatternsSensor.class);
    private FileSystem fs;
    private SensorContext context;


    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        // Shows at start of analysis
        //LOG.debug("-------------------------> Sensor descriptor");
    }

    @Override
    public void execute(SensorContext sensorContext) {
		context = sensorContext;
		fs = context.fileSystem();

		String[] SMELLS = new String[] { "AntiSingleton",
				"BaseClassKnowsDerivedClass", "BaseClassShouldBeAbstract", "Blob",
				"ClassDataShouldBePrivate", "ComplexClass",
				"FunctionalDecomposition", "LargeClass", "LazyClass", "LongMethod",
				"LongParameterList", "ManyFieldAttributesButNotComplex",
				"MessageChains", "RefusedParentBequest", "SpaghettiCode",
				"SpeculativeGenerality", "SwissArmyKnife", "TraditionBreaker" };

        //TODO: Analyze only sources specified by sonar.sources
        //https://docs.sonarqube.org/display/SCAN/Advanced+SonarQube+Scanner+Usages
		String sonarBaseDir = context.settings().getString("sonar.projectBaseDir");
        String sonarSources = context.settings().getString("sonar.sources");
        String sonarModules = context.settings().getString("sonar.modules");
        LOG.debug("BaseDir " + sonarBaseDir); // if project has modules, this is already the module dir
        LOG.debug("Sources " + sonarSources);
        LOG.debug("Modules " + sonarModules);
        // padl model is built for the project base directory
        // if a single source folder is specified, padl module is built just for it
        String padlRootFolder = sonarBaseDir;
        if (!sonarSources.contains(","))
            padlRootFolder = sonarSources;
        LOG.info("PadlModuleDir " + padlRootFolder);

		String[] root_paths = new String[] { padlRootFolder };
		String[] source_paths = new String[] { padlRootFolder };

		analyseCodeLevelModelFromJavaSourceFilesEclipse(SMELLS, root_paths, source_paths, "Test", ".");

	}

	private void extractClassesDefects(final String codesmellName,
									  final Properties properties,
									  final OccurrenceBuilder solutionBuilder) {


		final Occurrence[] allOccurrences = solutionBuilder.getAllOccurrences(properties);
		LOG.debug("How many classes infected by --> " + codesmellName + " ----> " + allOccurrences.length);

		FilePredicates f = this.fs.predicates();
		FilePredicate fp = f.all();
		Iterable<File> files = this.fs.files(fp);

		ArrayList<String> paths = new ArrayList<String>();

		for (File file : files) {
			LOG.debug("ptidej - Adding to paths list: " + file.getAbsolutePath());
			if(file.getAbsolutePath().endsWith(".java")) {
				paths.add(file.getAbsolutePath());
			}
			
		}
		
		LOG.debug("Once created paths list of size----> " + paths.size());

        for (final Occurrence occ : allOccurrences) {
            @SuppressWarnings("unchecked")
            final ArrayList<OccurrenceComponent> listOccComponents =
                    (ArrayList<OccurrenceComponent>) occ.getComponents();
            if (!listOccComponents.isEmpty()) {
                final OccurrenceComponent solutionComponent;
                if (codesmellName.matches("AntiSingleton|RefusedParentBequest")) {
                    solutionComponent = listOccComponents.get(1);
                } else {
                    solutionComponent = listOccComponents.get(0);
                }

                String rawClassName = new String(solutionComponent.getValue());
                LOG.debug("Infected class detected by ptidej ---> " + rawClassName);

                String className = rawClassName.substring(rawClassName.lastIndexOf(".") + 1).trim();
                LOG.debug("Simply the className ripped from the ptidej result ---> " + className);

                for (String clNamePath : paths) {

                    LOG.debug("If not found back scanning paths with size --->" + paths.size());

                    //for Windows systems
                    String clNameAndExtension = clNamePath.substring(clNamePath.lastIndexOf("\\") + 1).trim();

                    //String clNameAndExtension = clNamePath.substring(clNamePath.lastIndexOf("/") + 1).trim();
                    LOG.debug("Class for which a match has to be found (still with extension) ---> " + clNameAndExtension);
                    String[] parts = clNameAndExtension.split("\\.");
                    String clName = parts[0];
                    LOG.debug("Class for which a match has to be found(w/o extension) ---> " + clName);
                    LOG.debug("comparing: " + clName + " and " + className);
                    if (clName.equals(className)) {
                        LOG.debug("For this class (absolute path path) an issue must be added ---> " + clNamePath);
                        InputFile file = fs.inputFile(fs.predicates().hasAbsolutePath(clNamePath));
                        if (file != null) {
                            LOG.debug("Input file from absolutePath is not null");
                        }

                        NewIssue newIssue;
                        NewIssueLocation primaryLocation;
                        switch (codesmellName) {
                            case "ComplexClass":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.COMPLEX_CLASS);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Complex class");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "Blob":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.BLOB_CLASS);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Blob class");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "ClassDataShouldBePrivate":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.CLASS_DATA_PRIVATE);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Class data should be private");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;

                            case "FunctionalDecomposition":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.FUNCTIONAL_DECOMPOSITION);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Functional decomposition");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "SpaghettiCode":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.SPAGHETTI_CODE);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Spaghetti code");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "AntiSingleton":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.ANTISINGLETON);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Anti-singleton");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "BaseClassKnowsDerivedClass":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.BASECLASS_KNOWS_DERIVED);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Base-class knows derived class");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "BaseClassShouldBeAbstract":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.BASECLASS_ABSTRACT);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Base-class should be abstract");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "LargeClass":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.LARGE_CLASS);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Large class");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "LazyClass":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.LAZY_CLASS);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Lazy class");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "LongMethod":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.LONG_METHOD);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Long method");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "LongParameterList":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.LONG_PARAMETER_LIST);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Long parameter list");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "ManyFieldAttributesButNotComplex":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.MANY_FIELD_ATTRIBUTES_NOT_COMPLEX);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Many field attributes but not complex");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "MessageChains":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.MESSAGE_CHAINS);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Message chains");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "RefusedParentBequest":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.REFUSED_PARENT_BEQUEST);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Refused parent bequest");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "SpeculativeGenerality":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.SPECULATIVE_GENERALITY);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Speculative generality");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "SwissArmyKnife":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.SWISS_ARMY_KNIFE);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Swiss army knife");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                            case "TraditionBreaker":
                                newIssue = context.newIssue()
                                        .forRule(MyCodeSmellsDefinition.TRADITION_BREAKER);
                                primaryLocation = newIssue.newLocation()
                                        .on(file)
                                        .at(file.selectLine(1))
                                        .message("Tradition breaker");
                                newIssue.at(primaryLocation);
                                newIssue.save();
                                break;
                        }
                        paths.remove(clNamePath);
                        break;
                    }


                }
            }

        }
	}

	private void analyseCodeLevelModelFromJavaSourceFilesEclipse(
	        final String[] someSmells,
			final String[] someSourceRootPaths,
            final String[] someSourceFilePaths,
            final String aName,
			final String anOutputDirectoryName) {

		try {
			final long startTime = System.currentTimeMillis();
			final CompleteJavaFileCreator creator = new CompleteJavaFileCreator(someSourceRootPaths,
					new String[] { "" }, someSourceFilePaths);
			final ICodeLevelModel codeLevelModel = Factory.getInstance().createCodeLevelModel(aName);
			codeLevelModel.create(creator);
			final long endTime = System.currentTimeMillis();
			LOG.info("Code level model built in "+ (endTime - startTime)+" ms.");
			LOG.info("Code level model contains "+codeLevelModel.getNumberOfTopLevelEntities()+ " top-level entities.");

			// try {
			final LOCModelAnnotator annotator2 = new LOCModelAnnotator(codeLevelModel);
			creator.applyAnnotator(annotator2);
			// }
			// catch (final UnsupportedSourceModelException e) {
			// e.printStackTrace();
			// }

			// try {
			final ConditionalModelAnnotator annotator1 = new ConditionalModelAnnotator(codeLevelModel);
			creator.applyAnnotator(annotator1);
			// }
			// catch (final UnsupportedSourceModelException e) {
			// e.printStackTrace();
			// }

			// Create the output directory if needed.
			final String newOutputDirectoryName = anOutputDirectoryName + aName + File.separatorChar;

			// Yann 2013/05/30
			// Not necessary thanks to ProxyDisk
			// final File newOutputDirectiory = new
			// File(newOutputDirectoryName);
			// if (!newOutputDirectiory.exists()) {
			// newOutputDirectiory.mkdirs();
			// }
			analyseCodeLevelModel(someSmells, aName, codeLevelModel, newOutputDirectoryName);
		} catch (final SecurityException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		} catch (final IllegalArgumentException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		} catch (final CreationException e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
		}
		// catch (final UnsupportedSourceModelException e) {
		// e.printStackTrace(Output.getInstance().errorOutput());
		// }
	}

	private void analyseCodeLevelModel(final String[] someSmells,
                                            final String aName,
                                            final IAbstractLevelModel idiomLevelModel,
                                            final String anOutputDirectory) {

		try {
			for (final String antipatternName : someSmells) {
				final long startTime = System.currentTimeMillis();
				final Class<?> detectionClass = Class.forName("sad.designsmell.detection.repository." + antipatternName
						+ '.' + antipatternName + "Detection");
				final IDesignSmellDetection detection = (IDesignSmellDetection) detectionClass.newInstance();

				detection.detect(idiomLevelModel);

				final String path = anOutputDirectory + "DetectionResults in " + aName + " for " + antipatternName
						+ ".ini";
				detection.output(new PrintWriter(ProxyDisk.getInstance().fileTempOutput(path)));

				final Properties properties = new Properties();
				properties.load(new ReaderInputStream(ProxyDisk.getInstance().fileTempInput(path)));
				final OccurrenceBuilder solutionBuilder = OccurrenceBuilder.getInstance();
				final Occurrence[] solutions = solutionBuilder.getCanonicalOccurrences(properties);

				LOG.debug(solutions.length+" solutions for "+ antipatternName+" in "+aName+" in "+(System.currentTimeMillis() - startTime)+" ms.");

				extractClassesDefects(antipatternName, properties, solutionBuilder);
			}
		} catch (final Exception e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			throw new RuntimeException(e);
		}
	}


}