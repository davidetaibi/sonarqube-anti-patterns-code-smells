/*
* Sonarqube anti-pattern and code smell detection plugin
* Copyright (C) 2017  Giuseppe Aina, Alexandru Stan, Davide Taibi, Gustavs Venters
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package it.unibz.sonarqube.plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import padl.kernel.ICodeLevelModel;
import padl.kernel.exception.CreationException;
import padl.kernel.impl.Factory;
import ptidej.solver.Occurrence;
import ptidej.solver.OccurrenceBuilder;
import ptidej.solver.OccurrenceComponent;
import padl.creator.javafile.eclipse.CompleteJavaFileCreator;
import sad.designsmell.detection.IDesignSmellDetection;

public class CodeSmellsAntiPatternsSensor implements Sensor {

    private static final String ANTISINGLETON = "AntiSingleton";
    private static final String BASE_CLASS_KNOWS_DERIVED_CLASS = "BaseClassKnowsDerivedClass";
    private static final String BASE_CLASS_SHOULD_BE_ABSTRACT = "BaseClassShouldBeAbstract";
    private static final String BLOB = "Blob";
    private static final String CLASS_DATA_SHOULD_BE_PRIVATE = "ClassDataShouldBePrivate";
    private static final String COMPLEX_CLASS = "ComplexClass";
    private static final String FUNCTIONAL_DECOMPOSITION = "FunctionalDecomposition";
    private static final String LARGE_CLASS = "LargeClass";
    private static final String LAZY_CLASS = "LazyClass";
    private static final String LONG_METHOD = "LongMethod";
    private static final String LONG_PARAMETER_LIST = "LongParameterList";
    private static final String MANY_FIELD_ATTRIBUTES_BUT_NOT_COMPLEX = "ManyFieldAttributesButNotComplex";
    private static final String MESSAGE_CHAINS = "MessageChains";
    private static final String REFUSED_PARENT_BEQUEST = "RefusedParentBequest";
    private static final String SPAGHETT_ICODE = "SpaghettiCode";
    private static final String SPECULATIVE_GENERALITY = "SpeculativeGenerality";
    private static final String SWISS_ARMY_KNIFE = "SwissArmyKnife";
    private static final String TRADITION_BREAKER = "TraditionBreaker";


    private static final String[] CODE_SMELLS = new String[] { ANTISINGLETON,
            BASE_CLASS_KNOWS_DERIVED_CLASS, BASE_CLASS_SHOULD_BE_ABSTRACT, BLOB,
            CLASS_DATA_SHOULD_BE_PRIVATE, COMPLEX_CLASS,
            FUNCTIONAL_DECOMPOSITION, LARGE_CLASS, LAZY_CLASS, LONG_METHOD,
            LONG_PARAMETER_LIST, MANY_FIELD_ATTRIBUTES_BUT_NOT_COMPLEX,
            MESSAGE_CHAINS, REFUSED_PARENT_BEQUEST, SPAGHETT_ICODE,
            SPECULATIVE_GENERALITY, SWISS_ARMY_KNIFE, TRADITION_BREAKER };



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

        String sonarBaseDir = context.settings().getString("sonar.projectBaseDir");
        String sonarSources = context.settings().getString("sonar.sources");
        String sonarModules = context.settings().getString("sonar.modules");
        LOG.debug("BaseDir " + sonarBaseDir);
        LOG.debug("Sources " + sonarSources);
        LOG.debug("Modules " + sonarModules);

        //https://docs.sonarqube.org/display/SCAN/Advanced+SonarQube+Scanner+Usages
        List<String> sourcePathList = new ArrayList<>();
        if (sonarModules == null && sonarSources == null)
            sourcePathList.add(".");
        else if (sonarModules == null)
            Collections.addAll(sourcePathList, sonarSources.split(","));
        else if (sonarSources == null)
            Collections.addAll(sourcePathList, sonarModules.split(","));
        else
            for (String moduleName : sonarModules.split(","))
                for (String sourceName : sonarSources.split(","))
                    sourcePathList.add(moduleName + File.separatorChar + sourceName);
        LOG.debug("Source path entries " + sourcePathList.toString());

        String[] sourcePathEntries = sourcePathList.toArray(new String[sourcePathList.size()]);
        String[] classpathEntries = new String[] { "" };
        String[] sourceFiles = new String[] { "." };

        final long startTime = System.currentTimeMillis();
        final CompleteJavaFileCreator creator = new CompleteJavaFileCreator(sourcePathEntries, classpathEntries, sourceFiles);
        final ICodeLevelModel codeLevelModel = Factory.getInstance().createCodeLevelModel("Codesmells");
        try {
            codeLevelModel.create(creator);
        } catch (CreationException e) {
            LOG.error("Could not create code level model, creating took " + (System.currentTimeMillis() - startTime) + " ms", e);
            return;
        }
        LOG.info("Code level model built in " + (System.currentTimeMillis() - startTime)+" ms");
        LOG.info("Code level model contains " + codeLevelModel.getNumberOfTopLevelEntities() + " top-level entities");

        final LOCModelAnnotator locModelAnnotator = new LOCModelAnnotator(codeLevelModel);
        creator.applyAnnotator(locModelAnnotator);
        final ConditionalModelAnnotator conditionalModelAnnotator = new ConditionalModelAnnotator(codeLevelModel);
        creator.applyAnnotator(conditionalModelAnnotator);

        for (final String codesmellName : CODE_SMELLS) {
            try {
                final Class<?> detectionClass = Class.forName("sad.designsmell.detection.repository." + codesmellName
                        + '.' + codesmellName + "Detection");
                final IDesignSmellDetection detection = (IDesignSmellDetection) detectionClass.newInstance();
                detection.detect(codeLevelModel);
                final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                detection.output(new PrintWriter(byteOutput));

                final Properties properties = new Properties();
                properties.load(new ByteArrayInputStream(byteOutput.toByteArray()));

                final OccurrenceBuilder solutionBuilder = OccurrenceBuilder.getInstance();
                //final Occurrence[] solutions = solutionBuilder.getCanonicalOccurrences(properties);
                final Occurrence[] allOccurrences = solutionBuilder.getAllOccurrences(properties);
                LOG.debug("Classes infected by " + codesmellName + ": " + allOccurrences.length);
                saveClassesDefects(codesmellName, allOccurrences);

            } catch (ClassNotFoundException e) {
                LOG.error("Detection class not found for " + codesmellName, e);
            } catch (IllegalAccessException | InstantiationException e) {
                LOG.error("Could not instantiate IDesignSmellDetection for " + codesmellName, e);
            } catch (IOException e) {
                LOG.error("Could not load properties from memory for " + codesmellName, e);
            }
        }
    }

    private void saveClassesDefects(final String codesmellName, final Occurrence[] allOccurrences) {
        FilePredicates f = this.fs.predicates();
        FilePredicate fp = f.all();
        Iterable<File> files = fs.files(fp);
        ArrayList<String> paths = new ArrayList<>();
        for (File file : files) {
            if(file.getAbsolutePath().endsWith(".java")) {
                LOG.debug("Adding to ptidej paths: " + file.getAbsolutePath());
                paths.add(file.getAbsolutePath());
            }
        }

        for (final Occurrence occ : allOccurrences) {
            @SuppressWarnings("unchecked")
            final ArrayList<OccurrenceComponent> listOccComponents = (ArrayList<OccurrenceComponent>) occ.getComponents();
            if (!listOccComponents.isEmpty()) {
                final OccurrenceComponent solutionComponent;
                if (codesmellName.matches(ANTISINGLETON + "|" + REFUSED_PARENT_BEQUEST)) {
                    solutionComponent = listOccComponents.get(1);
                } else {
                    solutionComponent = listOccComponents.get(0);
                }

                String rawClassName = new String(solutionComponent.getValue());
                String className = rawClassName.substring(rawClassName.lastIndexOf(".") + 1).trim();
                LOG.debug("Infected class detected by ptidej: " + rawClassName);

                for (String clNamePath : paths) {
                    String clNameAndExtension = clNamePath.substring(clNamePath.lastIndexOf(File.separatorChar) + 1).trim();
                    String[] parts = clNameAndExtension.split("\\.");
                    String clName = parts[0];
                    LOG.debug("comparing: " + clName + " and " + className);
                    if (clName.equals(className)) {
                        InputFile file = fs.inputFile(fs.predicates().hasAbsolutePath(clNamePath));
                        saveIssue(codesmellName, file);
                        paths.remove(clNamePath);
                        break;
                    }
                }
            }
        }
    }

    private void saveIssue(String codesmellName, InputFile file) {
        if (file == null) {
            LOG.error("Input file from absolutePath is null");
            return;
        }
        NewIssue newIssue;
        NewIssueLocation primaryLocation;
        switch (codesmellName) {
            case COMPLEX_CLASS:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.COMPLEX_CLASS);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Complex class");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case BLOB:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.BLOB_CLASS);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Blob class");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case CLASS_DATA_SHOULD_BE_PRIVATE:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.CLASS_DATA_PRIVATE);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Class data should be private");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;

            case FUNCTIONAL_DECOMPOSITION:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.FUNCTIONAL_DECOMPOSITION);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Functional decomposition");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case SPAGHETT_ICODE:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.SPAGHETTI_CODE);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Spaghetti code");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case ANTISINGLETON:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.ANTISINGLETON);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Anti-singleton");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case BASE_CLASS_KNOWS_DERIVED_CLASS:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.BASECLASS_KNOWS_DERIVED);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Base-class knows derived class");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case BASE_CLASS_SHOULD_BE_ABSTRACT:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.BASECLASS_ABSTRACT);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Base-class should be abstract");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case LARGE_CLASS:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.LARGE_CLASS);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Large class");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case LAZY_CLASS:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.LAZY_CLASS);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Lazy class");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case LONG_METHOD:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.LONG_METHOD);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Long method");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case LONG_PARAMETER_LIST:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.LONG_PARAMETER_LIST);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Long parameter list");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case MANY_FIELD_ATTRIBUTES_BUT_NOT_COMPLEX:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.MANY_FIELD_ATTRIBUTES_NOT_COMPLEX);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Many field attributes but not complex");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case MESSAGE_CHAINS:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.MESSAGE_CHAINS);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Message chains");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case REFUSED_PARENT_BEQUEST:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.REFUSED_PARENT_BEQUEST);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Refused parent bequest");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case SPECULATIVE_GENERALITY:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.SPECULATIVE_GENERALITY);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Speculative generality");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case SWISS_ARMY_KNIFE:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.SWISS_ARMY_KNIFE);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Swiss army knife");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            case TRADITION_BREAKER:
                newIssue = context.newIssue()
                        .forRule(CodeSmellsAntiPatternsRulesDefinition.TRADITION_BREAKER);
                primaryLocation = newIssue.newLocation()
                        .on(file)
                        .at(file.selectLine(1))
                        .message("Tradition breaker");
                newIssue.at(primaryLocation);
                newIssue.save();
                break;
            default:
                LOG.error("No such code-smell defined: " + codesmellName);
        }
    }

}