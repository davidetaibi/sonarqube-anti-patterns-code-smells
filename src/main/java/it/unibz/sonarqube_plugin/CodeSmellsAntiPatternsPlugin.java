package it.unibz.sonarqube_plugin;

import org.sonar.api.Plugin;



public class CodeSmellsAntiPatternsPlugin implements Plugin {

    @Override
    public void define(Context context) {
        context.addExtensions(
                CodeSmellsAntiPatternsRulesDefinition.class,
                CodeSmellsAntiPatternsSensor.class
        );
    }
}
