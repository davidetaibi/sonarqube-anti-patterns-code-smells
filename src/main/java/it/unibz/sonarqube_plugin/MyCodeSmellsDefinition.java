package it.unibz.sonarqube_plugin;

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

public class MyCodeSmellsDefinition implements RulesDefinition {

	   @Override
	   public void define(Context context) {
		   
		 System.out.println("define method from MyCodeSmellsDefinition started");
	     NewRepository repository = context.createRepository("code_smells", "java").setName("Code-smell rules repository");

	  
	     NewRule complex_class = repository.createRule("complex_class")
	      .setName("Complex Class")
	      .setHtmlDescription("A class having a high cyclomatic complexity")
	      .setTags("code-smell","antipattern")
          .setSeverity(Severity.MAJOR);

	     complex_class
	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
	       .setDebtRemediationFunction(complex_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));

	     complex_class.createParam("acceptWhitespace")
	       .setDefaultValue("false")
	       .setType(RuleParamType.BOOLEAN)
	       .setDescription("Accept whitespaces on the line");
	     
	     NewRule blob = repository.createRule("blob_class")
	   	      .setName("Blob class")
	   	      .setHtmlDescription("A large class with different responsibilities that monopolizes most of the system’s processing")
	   	      .setTags("code-smell","antipattern")
   	   	      .setSeverity(Severity.MAJOR);

	     	 blob
	   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
	   	       .setDebtRemediationFunction(blob.debtRemediationFunctions().linearWithOffset("1h", "30min"));

	     	 blob.createParam("acceptWhitespace")
	   	       .setDefaultValue("false")
	   	       .setType(RuleParamType.BOOLEAN)
	   	       .setDescription("Accept whitespaces on the line");
	     	 
	     	 
		     NewRule clDataPrivate = repository.createRule("class_data_private")
			   	      .setName("Class Data Should be Private")
			   	      .setHtmlDescription("A class exposing its attributes, violating the information hiding principle")
			   	      .setTags("code-smell","antipattern")
 			   	      .setSeverity(Severity.MAJOR);

		     clDataPrivate
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(clDataPrivate.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     clDataPrivate.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");	
		     
		     NewRule functDecomposition = repository.createRule("functional_decomposition")
			   	      .setName("Functional Decomposition")
			   	      .setHtmlDescription("A class where inheritance and polymorphism are poorly used, declaring many private fields and implementing few methods")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     functDecomposition
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(functDecomposition.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     functDecomposition.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
	     	 
		     
		     NewRule spaghettiCode = repository.createRule("spaghetti_code")
			   	      .setName("Spaghetti Code")
			   	      .setHtmlDescription("A class without structure that declares long methods without parameters")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     spaghettiCode
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(spaghettiCode.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     spaghettiCode.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
	     	 
	     

	     // don't forget to call done() to finalize the definition
	     repository.done();
	     System.out.println("define method from MyCodeSmellsDefinition stopped");
	   }
	 }
