package it.unibz.sonarqube_plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

public class MyCodeSmellsDefinition implements RulesDefinition {

	private static Logger LOG = LoggerFactory.getLogger(MyCodeSmellsDefinition.class);

	   @Override
	   public void define(Context context) {
		   
		 LOG.debug("define method from MyCodeSmellsDefinition started");
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
			   	       
		     NewRule antisingleton = repository.createRule("antisingleton")
			   	      .setName("Antisingleton")
			   	      .setHtmlDescription("A class that provides mutable class variables, which consequently could be used as global variables")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     antisingleton
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(antisingleton.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     antisingleton.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule baseclassknows = repository.createRule("baseclass_knows_derived")
			   	      .setName("BaseClassKnowsDerivedClass")
			   	      .setHtmlDescription("A class that invokes or has at least binary-class relationship pointing to one of its subclasses")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     baseclassknows
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(baseclassknows.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     baseclassknows.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule baseclassabstr = repository.createRule("baseclass_abstract")
			   	      .setName("BaseClassShouldBeAbstract")
			   	      .setHtmlDescription("A class that has many subclasses without being abstract")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     baseclassabstr
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(baseclassabstr.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     baseclassabstr.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule large_class = repository.createRule("large_class")
			   	      .setName("Large class")
			   	      .setHtmlDescription("A class that has grown too large in term of LOCs")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     large_class
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(large_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     large_class.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule lazy_class = repository.createRule("lazy_class")
			   	      .setName("Lazy class")
			   	      .setHtmlDescription("A class that has few fields and methods.")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     lazy_class
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(lazy_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     lazy_class.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule long_method = repository.createRule("long_method")
			   	      .setName("Long method")
			   	      .setHtmlDescription("A class that has (at least) a method that is very long, in term of LOCs")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     long_method
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(long_method.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     long_method.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule tradition_breaker = repository.createRule("tradition_breaker")
			   	      .setName("Tradition breaker")
			   	      .setHtmlDescription("A class that inherits from a large parent class but that provides little behaviour and without subclasses")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     tradition_breaker
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(tradition_breaker.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     tradition_breaker.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule long_parameter_list = repository.createRule("long_parameter_list")
			   	      .setName("Long parameter list")
			   	      .setHtmlDescription("	A class that has (at least) one method with a too long list of parameters in comparison to the average number of parameters per methods in the system")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     long_parameter_list
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(long_parameter_list.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     long_parameter_list.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule many_field_attributes_not_complex = repository.createRule("many_field_attributes_not_complex")
			   	      .setName("Many field attribute not complex")
			   	      .setHtmlDescription("A class that declares many attributes but which is not complex and, hence, more likely to be some kind of data class holding values without providing behaviour")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     many_field_attributes_not_complex
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(many_field_attributes_not_complex.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     many_field_attributes_not_complex.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule message_chains = repository.createRule("message_chains")
			   	      .setName("Message chains")
			   	      .setHtmlDescription("A class that uses a long chain of method invocations to realise (at least) one of its functionality")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     message_chains
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(message_chains.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     message_chains.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule refused_parent_bequest = repository.createRule("refused_parent_bequest")
			   	      .setName("Refused parent bequest")
			   	      .setHtmlDescription("A class that redefines inherited method using empty bodies, thus breaking polymorphism")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     refused_parent_bequest
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(refused_parent_bequest.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     refused_parent_bequest.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule speculative_generality = repository.createRule("speculative_generality")
			   	      .setName("Speculative generality")
			   	      .setHtmlDescription("A class that is defined as abstract but that has very few children, which do not make use of its methods")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     speculative_generality
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(speculative_generality.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     speculative_generality.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
		     
		     NewRule swiss_army_knife = repository.createRule("swiss_army_knife")
			   	      .setName("Swiss army knife")
			   	      .setHtmlDescription("A complex class that offers a high number of services, for example, a complex class implementing a high number of interfaces")
			   	      .setTags("code-smell","antipattern")
			   	      .setSeverity(Severity.MAJOR);

		     swiss_army_knife
			   	       .setDebtSubCharacteristic("INTEGRATION_TESTABILITY")
			   	       .setDebtRemediationFunction(swiss_army_knife.debtRemediationFunctions().linearWithOffset("1h", "30min"));

		     swiss_army_knife.createParam("acceptWhitespace")
			   	       .setDefaultValue("false")
			   	       .setType(RuleParamType.BOOLEAN)
			   	       .setDescription("Accept whitespaces on the line");
	     	 
	     

	     // don't forget to call done() to finalize the definition
	     repository.done();
	     LOG.debug("define method from MyCodeSmellsDefinition stopped");
	   }
	 }
