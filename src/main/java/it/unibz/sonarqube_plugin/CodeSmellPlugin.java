package it.unibz.sonarqube_plugin;

import java.util.Arrays;
import java.util.List;
import org.sonar.api.SonarPlugin;



public class CodeSmellPlugin extends SonarPlugin {
	
	public CodeSmellPlugin() { 
		super();
	}

	
	public List getExtensions() {
		return Arrays.asList(MyCodeSmellsDefinition.class,ExampleSensor.class);
	}

}
