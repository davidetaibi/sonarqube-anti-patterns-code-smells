package it.unibz.sonarqube_plugin;

import java.util.Arrays;
import java.util.List;
import org.sonar.api.SonarPlugin;



public class CodeSmellsAntiPatternsPlugin extends SonarPlugin {
	
	public CodeSmellsAntiPatternsPlugin() { 
		super();
	}

	
	public List getExtensions() {
		return Arrays.asList(MyCodeSmellsDefinition.class,CodeSmellsAntiPatternsSensor.class);
	}

}
