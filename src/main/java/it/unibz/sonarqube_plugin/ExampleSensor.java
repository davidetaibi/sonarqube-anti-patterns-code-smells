package it.unibz.sonarqube_plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.measures.CoreMetrics;

import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;

import padl.kernel.IAbstractLevelModel;
import padl.kernel.ICodeLevelModel;
import padl.kernel.IIdiomLevelModel;
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

public class ExampleSensor implements Sensor {

	private Settings settings;
	private final FileSystem fs;
	Logger LOG = LoggerFactory.getLogger(getClass());
	private final ResourcePerspectives perspectives;
	SensorContext context = null;

	/**
	 * Use of IoC to get Settings
	 */
	public ExampleSensor(Settings settings, FileSystem fs, ResourcePerspectives p) {
		this.settings = settings;
		this.fs = fs;
		this.perspectives = p;
	}

	public boolean shouldExecuteOnProject(Project project) {
		// This sensor is executed on any type of projects
		return true;
	}

	public void analyse(Project project, SensorContext sensorContext) {
		context = sensorContext;
		String projectName = project.getName();
		String date = project.getAnalysisDate().toString();

		String[] SMELLS = new String[] { "Blob", "ClassDataShouldBePrivate", "ComplexClass", "FunctionalDecomposition",
				"SpaghettiCode" };

		String rootFolder = settings.getString("sonar.projectBaseDir");
		LOG.info("BaseDir----->" + rootFolder);

		String[] root_paths = new String[] { rootFolder };
		String[] source_paths = new String[] { rootFolder };

		analyseCodeLevelModelFromJavaSourceFilesEclipse(SMELLS, root_paths, source_paths, "Test", ".");

	}

	public static void extractFilesFromDir(final String aPath, final String anExtension,
			final ArrayList<String> aListOfFiles) {

		final File pathFile = new File(aPath);
		final String[] subPaths = pathFile.list();
		if (subPaths != null) {
			for (int i = 0; i < subPaths.length; i++) {
				final String fileName = aPath + '/' + subPaths[i];
				final File file = new File(fileName);
				if (file.isDirectory()) {
					extractFilesFromDir(fileName, anExtension, aListOfFiles);
				} else {
					if (// fileName.indexOf("org.eclipse.") > 0 &&
					fileName.endsWith(anExtension)) {
						System.out.println(fileName);
						aListOfFiles.add(fileName);
					}
				}
			}
		} else {
			throw new RuntimeException(new CreationException("No subdirectories with JAR files in " + aPath));
		}
	}

	public void extractClassesDefects(final String anOutputDirectory, final String aName,
			final IIdiomLevelModel idiomLevelModel, final String codesmellName, final Properties properties,
			final OccurrenceBuilder solutionBuilder) throws IOException {

		final String path2 = anOutputDirectory + "Classes Detected in " + aName + " for " + codesmellName + ".csv";
		/*
		 * final PrintWriter w = new
		 * PrintWriter(ProxyDisk.getInstance().fileTempOutput(path2));
		 */

		final Occurrence[] allOccurrences = solutionBuilder.getAllOccurrences(properties);
		final int nbAllOcc = allOccurrences.length;
		System.out.println("How many classes infected by --> " + codesmellName + " ----> " + nbAllOcc);

		FilePredicates f = this.fs.predicates();
		FilePredicate fp = f.all();
		Iterable<File> files = this.fs.files(fp);

		ArrayList<String> paths = new ArrayList<String>();

		for (File file : files) {
			LOG.info("Usi ptidej o no?");
			LOG.info("Adding to paths list: " + file.getAbsolutePath());
			if(file.getAbsolutePath().endsWith(".java")) {
				paths.add(file.getAbsolutePath());
			}
			
		}
		
		System.out.println("Once created paths list of size----> " + paths.size());

		for (int j = 0; j < nbAllOcc; j++) {
			final Occurrence occ = allOccurrences[j];
			@SuppressWarnings("unchecked")
			final ArrayList<OccurrenceComponent> listOccComponents = (ArrayList<OccurrenceComponent>) occ
					.getComponents();
			if (!listOccComponents.isEmpty()) {
				final OccurrenceComponent solutionComponent = (OccurrenceComponent) listOccComponents.get(0);

				String rawClassName = new String(solutionComponent.getValue());
				System.out.println("Infected class detected by ptidej ---> " + rawClassName);
				// w.println(solutionComponent.getValue());

				String className = rawClassName.substring(rawClassName.lastIndexOf(".") + 1).trim();
				System.out.println("Simply the className ripped from the ptidej result ---> " + className);

				for (String clNamePath : paths) {
				
					System.out.println("If not found back scanning paths with size --->" + paths.size());
					
					//for Windows systems 
					//String clNameAndExtension = clNamePath.substring(clNamePath.lastIndexOf("\\") + 1).trim();
					
					String clNameAndExtension = clNamePath.substring(clNamePath.lastIndexOf("/")+1).trim();
					System.out.println("Class for which a match has to be found (still with extension) ---> "
							+ clNameAndExtension);
					String[] parts = clNameAndExtension.split("\\.");
					String clName = parts[0];
					System.out.println("Class for which a match has to be found(w/o extension) ---> " + clName);
					System.out.println("comparing: "+ clName + " and " + className);
					if (clName.equals(className)) {
						System.out.println(
								"For this class (absolute path path) an issue must be added ---> " + clNamePath);
						InputFile file = fs.inputFile(fs.predicates().hasAbsolutePath((clNamePath)));
						if (file != null) {
							System.out.println("Input file from absolutePath is not null");
						}
						// Resource r = context.getResource(file);
						Issuable issuable = perspectives.as(Issuable.class, file);
						if (issuable != null) {
							System.out.println("Input file from absolutePath is also issuable");
							switch (codesmellName) {
							case "ComplexClass":
								Issue issue = issuable.newIssueBuilder()
										.ruleKey(RuleKey.of("code_smells", "complex_class")).line(10)
										.message("Complex class").build();
								issuable.addIssue(issue);
								break;

							case "Blob":
								Issue issue2 = issuable.newIssueBuilder()
										.ruleKey(RuleKey.of("code_smells", "blob_class")).line(10).message("Blob class")
										.build();
								issuable.addIssue(issue2);
								break;

							case "ClassDataShouldBePrivate":
								Issue issue3 = issuable.newIssueBuilder()
										.ruleKey(RuleKey.of("code_smells", "class_data_private")).line(10)
										.message("Class Data Should Be Private").build();
								issuable.addIssue(issue3);
								break;

							case "FunctionalDecomposition":
								Issue issue4 = issuable.newIssueBuilder()
										.ruleKey(RuleKey.of("code_smells", "functional_decomposition")).line(10)
										.message("Functional Decomposition").build();
								issuable.addIssue(issue4);
								break;

							case "SpaghettiCode":
								Issue issue5 = issuable.newIssueBuilder()
										.ruleKey(RuleKey.of("code_smells", "spaghetti_code")).line(10)
										.message("Spaghetti Code").build();
								issuable.addIssue(issue5);
								break;
							}

						}
						paths.remove(clNamePath);
						break;
					}

				
			
				}
				}

		}
		// w.close();
	}

	public final void analyseCodeLevelModelFromJavaSourceFilesEclipse(final String[] someSmells,
			final String[] someSourceRootPaths, final String[] someSourceFilePaths, final String aName,
			final String anOutputDirectoryName) {

		System.out.print("Analysing ");
		System.out.print(aName);
		System.out.println("...");

		// Output.getInstance().setNormalOutput(new PrintWriter(System.out));
		// Output.getInstance().setDebugOutput(new PrintWriter(System.out));
		// Output.getInstance().setErrorOutput(new PrintWriter(System.err));

		try {
			final long startTime = System.currentTimeMillis();
			final CompleteJavaFileCreator creator = new CompleteJavaFileCreator(someSourceRootPaths,
					new String[] { "" }, someSourceFilePaths);
			final ICodeLevelModel codeLevelModel = Factory.getInstance().createCodeLevelModel(aName);
			codeLevelModel.create(creator);
			final long endTime = System.currentTimeMillis();
			System.out.print("Model built in ");
			System.out.print(endTime - startTime);
			System.out.println(" ms.");
			System.out.print("Model contains ");
			System.out.print(codeLevelModel.getNumberOfTopLevelEntities());
			System.out.println(" top-level entities.");

			// try {
			final padl.creator.javafile.eclipse.astVisitors.LOCModelAnnotator annotator2 = new padl.creator.javafile.eclipse.astVisitors.LOCModelAnnotator(
					codeLevelModel);
			creator.applyAnnotator(annotator2);
			// }
			// catch (final UnsupportedSourceModelException e) {
			// e.printStackTrace();
			// }

			// try {
			final padl.creator.javafile.eclipse.astVisitors.ConditionalModelAnnotator annotator1 = new padl.creator.javafile.eclipse.astVisitors.ConditionalModelAnnotator(
					codeLevelModel);
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

	public final void analyseCodeLevelModel(final String[] someSmells, final String aName,
			final IAbstractLevelModel idiomLevelModel, final String anOutputDirectory) {

		try {
			for (int i = 0; i < someSmells.length; i++) {
				final String antipatternName = someSmells[i];

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

				System.out.print(solutions.length);
				System.out.print(" solutions for ");
				System.out.print(antipatternName);
				System.out.print(" in ");
				System.out.print(aName);
				System.out.print(" in ");
				System.out.print(System.currentTimeMillis() - startTime);
				System.out.println(" ms.");

				extractClassesDefects(anOutputDirectory, aName, null, antipatternName, properties, solutionBuilder);
			}
		} catch (final Exception e) {
			e.printStackTrace(ProxyConsole.getInstance().errorOutput());
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}