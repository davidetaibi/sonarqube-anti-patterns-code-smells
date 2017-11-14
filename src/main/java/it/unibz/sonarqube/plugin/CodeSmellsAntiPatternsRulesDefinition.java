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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

public class CodeSmellsAntiPatternsRulesDefinition implements RulesDefinition {


    private static Logger LOG = LoggerFactory.getLogger(CodeSmellsAntiPatternsRulesDefinition.class);


    private static final String REPOSITORY = "code_smells";
    public static final RuleKey COMPLEX_CLASS = RuleKey.of(REPOSITORY, "complex_class");
    public static final RuleKey BLOB_CLASS = RuleKey.of(REPOSITORY, "blob_class");
    public static final RuleKey CLASS_DATA_PRIVATE = RuleKey.of(REPOSITORY, "class_data_private");
    public static final RuleKey FUNCTIONAL_DECOMPOSITION = RuleKey.of(REPOSITORY, "functional_decomposition");
    public static final RuleKey SPAGHETTI_CODE = RuleKey.of(REPOSITORY, "spaghetti_code");
    public static final RuleKey ANTISINGLETON = RuleKey.of(REPOSITORY, "antisingleton");
    public static final RuleKey BASECLASS_KNOWS_DERIVED = RuleKey.of(REPOSITORY, "baseclass_knows_derived");
    public static final RuleKey BASECLASS_ABSTRACT = RuleKey.of(REPOSITORY, "baseclass_abstract");
    public static final RuleKey LARGE_CLASS = RuleKey.of(REPOSITORY, "large_class");
    public static final RuleKey LAZY_CLASS = RuleKey.of(REPOSITORY, "lazy_class");
    public static final RuleKey LONG_METHOD = RuleKey.of(REPOSITORY, "long_method");
    public static final RuleKey LONG_PARAMETER_LIST = RuleKey.of(REPOSITORY, "long_parameter_list");
    public static final RuleKey MANY_FIELD_ATTRIBUTES_NOT_COMPLEX = RuleKey.of(REPOSITORY, "many_field_attributes_not_complex");
    public static final RuleKey MESSAGE_CHAINS = RuleKey.of(REPOSITORY, "message_chains");
    public static final RuleKey REFUSED_PARENT_BEQUEST = RuleKey.of(REPOSITORY, "refused_parent_bequest");
    public static final RuleKey SPECULATIVE_GENERALITY = RuleKey.of(REPOSITORY, "speculative_generality");
    public static final RuleKey SWISS_ARMY_KNIFE = RuleKey.of(REPOSITORY, "swiss_army_knife");
    public static final RuleKey TRADITION_BREAKER = RuleKey.of(REPOSITORY, "tradition_breaker");
    public static final RuleKey[] ALL_RULES =
            {COMPLEX_CLASS, BLOB_CLASS, CLASS_DATA_PRIVATE, FUNCTIONAL_DECOMPOSITION, SPAGHETTI_CODE,
                    ANTISINGLETON, BASECLASS_KNOWS_DERIVED, BASECLASS_ABSTRACT, LARGE_CLASS, LAZY_CLASS,
                    LONG_METHOD, LONG_PARAMETER_LIST, MANY_FIELD_ATTRIBUTES_NOT_COMPLEX, MESSAGE_CHAINS,
                    REFUSED_PARENT_BEQUEST, SPECULATIVE_GENERALITY, SWISS_ARMY_KNIFE, TRADITION_BREAKER };

    private static final String TAG_CODESMELL = "code-smell";
    private static final String TAG_ANTIPATTERN = "antipattern";

    @Override
    public void define(Context context) {

        NewRepository repository = context.createRepository(REPOSITORY, "java").setName("Code-smell rules repository");


        NewRule complex_class = repository.createRule("complex_class")
                .setName("Complex Class")
                .setHtmlDescription("A class having a high cyclomatic complexity")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        complex_class
                .setDebtRemediationFunction(complex_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        complex_class.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule blob = repository.createRule("blob_class")
                .setName("Blob class")
                .setHtmlDescription("A large class with different responsibilities that monopolizes most of the system’s processing")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        blob
                .setDebtRemediationFunction(blob.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        blob.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");


        NewRule clDataPrivate = repository.createRule("class_data_private")
                .setName("Class Data Should be Private")
                .setHtmlDescription("A class exposing its attributes, violating the information hiding principle")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        clDataPrivate
                .setDebtRemediationFunction(clDataPrivate.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        clDataPrivate.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule functDecomposition = repository.createRule("functional_decomposition")
                .setName("Functional Decomposition")
                .setHtmlDescription("A class where inheritance and polymorphism are poorly used, declaring many private fields and implementing few methods")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        functDecomposition
                .setDebtRemediationFunction(functDecomposition.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        functDecomposition.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");


        NewRule spaghettiCode = repository.createRule("spaghetti_code")
                .setName("Spaghetti Code")
                .setHtmlDescription("A class without structure that declares long methods without parameters")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        spaghettiCode
                .setDebtRemediationFunction(spaghettiCode.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        spaghettiCode.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule antisingleton = repository.createRule("antisingleton")
                .setName("Antisingleton")
                .setHtmlDescription("A class that provides mutable class variables, which consequently could be used as global variables")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        antisingleton
                .setDebtRemediationFunction(antisingleton.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        antisingleton.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule baseclassknows = repository.createRule("baseclass_knows_derived")
                .setName("BaseClassKnowsDerivedClass")
                .setHtmlDescription("A class that invokes or has at least binary-class relationship pointing to one of its subclasses")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        baseclassknows
                .setDebtRemediationFunction(baseclassknows.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        baseclassknows.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule baseclassabstr = repository.createRule("baseclass_abstract")
                .setName("BaseClassShouldBeAbstract")
                .setHtmlDescription("A class that has many subclasses without being abstract")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        baseclassabstr
                .setDebtRemediationFunction(baseclassabstr.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        baseclassabstr.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule large_class = repository.createRule("large_class")
                .setName("Large class")
                .setHtmlDescription("A class that has grown too large in term of LOCs")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        large_class
                .setDebtRemediationFunction(large_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        large_class.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule lazy_class = repository.createRule("lazy_class")
                .setName("Lazy class")
                .setHtmlDescription("A class that has few fields and methods.")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        lazy_class
                .setDebtRemediationFunction(lazy_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        lazy_class.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule long_method = repository.createRule("long_method")
                .setName("Long method")
                .setHtmlDescription("A class that has (at least) a method that is very long, in term of LOCs")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        long_method
                .setDebtRemediationFunction(long_method.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        long_method.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule tradition_breaker = repository.createRule("tradition_breaker")
                .setName("Tradition breaker")
                .setHtmlDescription("A class that inherits from a large parent class but that provides little behaviour and without subclasses")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        tradition_breaker
                .setDebtRemediationFunction(tradition_breaker.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        tradition_breaker.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule long_parameter_list = repository.createRule("long_parameter_list")
                .setName("Long parameter list")
                .setHtmlDescription("	A class that has (at least) one method with a too long list of parameters in comparison to the average number of parameters per methods in the system")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        long_parameter_list
                .setDebtRemediationFunction(long_parameter_list.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        long_parameter_list.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule many_field_attributes_not_complex = repository.createRule("many_field_attributes_not_complex")
                .setName("Many field attribute not complex")
                .setHtmlDescription("A class that declares many attributes but which is not complex and, hence, more likely to be some kind of data class holding values without providing behaviour")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        many_field_attributes_not_complex
                .setDebtRemediationFunction(many_field_attributes_not_complex.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        many_field_attributes_not_complex.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule message_chains = repository.createRule("message_chains")
                .setName("Message chains")
                .setHtmlDescription("A class that uses a long chain of method invocations to realise (at least) one of its functionality")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        message_chains
                .setDebtRemediationFunction(message_chains.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        message_chains.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule refused_parent_bequest = repository.createRule("refused_parent_bequest")
                .setName("Refused parent bequest")
                .setHtmlDescription("A class that redefines inherited method using empty bodies, thus breaking polymorphism")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        refused_parent_bequest
                .setDebtRemediationFunction(refused_parent_bequest.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        refused_parent_bequest.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule speculative_generality = repository.createRule("speculative_generality")
                .setName("Speculative generality")
                .setHtmlDescription("A class that is defined as abstract but that has very few children, which do not make use of its methods")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        speculative_generality
                .setDebtRemediationFunction(speculative_generality.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        speculative_generality.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");

        NewRule swiss_army_knife = repository.createRule("swiss_army_knife")
                .setName("Swiss army knife")
                .setHtmlDescription("A complex class that offers a high number of services, for example, a complex class implementing a high number of interfaces")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        swiss_army_knife
                .setDebtRemediationFunction(swiss_army_knife.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        swiss_army_knife.createParam("acceptWhitespace")
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription("Accept whitespaces on the line");


        // don't forget to call done() to finalize the definition
        repository.done();
    }
}
