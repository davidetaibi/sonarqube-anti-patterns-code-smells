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


    static final String REPOSITORY = "code_smells";
    static final RuleKey COMPLEX_CLASS = RuleKey.of(REPOSITORY, "complex_class");
    static final RuleKey BLOB_CLASS = RuleKey.of(REPOSITORY, "blob_class");
    static final RuleKey CLASS_DATA_PRIVATE = RuleKey.of(REPOSITORY, "class_data_private");
    static final RuleKey FUNCTIONAL_DECOMPOSITION = RuleKey.of(REPOSITORY, "functional_decomposition");
    static final RuleKey SPAGHETTI_CODE = RuleKey.of(REPOSITORY, "spaghetti_code");
    static final RuleKey ANTISINGLETON = RuleKey.of(REPOSITORY, "antisingleton");
    static final RuleKey BASECLASS_KNOWS_DERIVED = RuleKey.of(REPOSITORY, "baseclass_knows_derived");
    static final RuleKey BASECLASS_ABSTRACT = RuleKey.of(REPOSITORY, "baseclass_abstract");
    static final RuleKey LARGE_CLASS = RuleKey.of(REPOSITORY, "large_class");
    static final RuleKey LAZY_CLASS = RuleKey.of(REPOSITORY, "lazy_class");
    static final RuleKey LONG_METHOD = RuleKey.of(REPOSITORY, "long_method");
    static final RuleKey LONG_PARAMETER_LIST = RuleKey.of(REPOSITORY, "long_parameter_list");
    static final RuleKey MANY_FIELD_ATTRIBUTES_NOT_COMPLEX = RuleKey.of(REPOSITORY, "many_field_attributes_not_complex");
    static final RuleKey MESSAGE_CHAINS = RuleKey.of(REPOSITORY, "message_chains");
    static final RuleKey REFUSED_PARENT_BEQUEST = RuleKey.of(REPOSITORY, "refused_parent_bequest");
    static final RuleKey SPECULATIVE_GENERALITY = RuleKey.of(REPOSITORY, "speculative_generality");
    static final RuleKey SWISS_ARMY_KNIFE = RuleKey.of(REPOSITORY, "swiss_army_knife");
    static final RuleKey TRADITION_BREAKER = RuleKey.of(REPOSITORY, "tradition_breaker");
    static final RuleKey[] ALL_RULES =
            {COMPLEX_CLASS, BLOB_CLASS, CLASS_DATA_PRIVATE, FUNCTIONAL_DECOMPOSITION, SPAGHETTI_CODE,
                    ANTISINGLETON, BASECLASS_KNOWS_DERIVED, BASECLASS_ABSTRACT, LARGE_CLASS, LAZY_CLASS,
                    LONG_METHOD, LONG_PARAMETER_LIST, MANY_FIELD_ATTRIBUTES_NOT_COMPLEX, MESSAGE_CHAINS,
                    REFUSED_PARENT_BEQUEST, SPECULATIVE_GENERALITY, SWISS_ARMY_KNIFE, TRADITION_BREAKER };

    private static final String TAG_CODESMELL = "code-smell";
    private static final String TAG_ANTIPATTERN = "antipattern";

    private static final String ACCEPT_WHITESPACE_PARAM = "acceptWhitespace";
    private static final String ACCEPT_WHITESPACES_DESCRIPTION = "Accept whitespaces on the line";

    @Override
    public void define(final Context context) {

        final NewRepository repository = context.createRepository(REPOSITORY, "java").setName("Code-smell rules repository");


        final NewRule complex_class = repository.createRule(COMPLEX_CLASS.rule())
                .setName("Complex Class")
                .setHtmlDescription("A class having a high cyclomatic complexity")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        complex_class
                .setDebtRemediationFunction(complex_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        complex_class.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule blob = repository.createRule(BLOB_CLASS.rule())
                .setName("Blob class")
                .setHtmlDescription("A large class with different responsibilities that monopolizes most of the system’s processing")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        blob    .setDebtRemediationFunction(blob.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        blob.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule clDataPrivate = repository.createRule(CLASS_DATA_PRIVATE.rule())
                .setName("Class Data Should be Private")
                .setHtmlDescription("A class exposing its attributes, violating the information hiding principle")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        clDataPrivate
                .setDebtRemediationFunction(clDataPrivate.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        clDataPrivate.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule functDecomposition = repository.createRule(FUNCTIONAL_DECOMPOSITION.rule())
                .setName("Functional Decomposition")
                .setHtmlDescription("A class where inheritance and polymorphism are poorly used, declaring many private fields and implementing few methods")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        functDecomposition
                .setDebtRemediationFunction(functDecomposition.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        functDecomposition.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule spaghettiCode = repository.createRule(SPAGHETTI_CODE.rule())
                .setName("Spaghetti Code")
                .setHtmlDescription("A class without structure that declares long methods without parameters")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        spaghettiCode
                .setDebtRemediationFunction(spaghettiCode.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        spaghettiCode.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule antisingleton = repository.createRule(ANTISINGLETON.rule())
                .setName("Antisingleton")
                .setHtmlDescription("A class that provides mutable class variables, which consequently could be used as global variables")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        antisingleton
                .setDebtRemediationFunction(antisingleton.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        antisingleton.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule baseclassknows = repository.createRule(BASECLASS_KNOWS_DERIVED.rule())
                .setName("BaseClassKnowsDerivedClass")
                .setHtmlDescription("A class that invokes or has at least binary-class relationship pointing to one of its subclasses")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        baseclassknows
                .setDebtRemediationFunction(baseclassknows.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        baseclassknows.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule baseclassabstr = repository.createRule(BASECLASS_ABSTRACT.rule())
                .setName("BaseClassShouldBeAbstract")
                .setHtmlDescription("A class that has many subclasses without being abstract")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        baseclassabstr
                .setDebtRemediationFunction(baseclassabstr.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        baseclassabstr.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule large_class = repository.createRule(LARGE_CLASS.rule())
                .setName("Large class")
                .setHtmlDescription("A class that has grown too large in term of LOCs")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        large_class
                .setDebtRemediationFunction(large_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        large_class.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule lazy_class = repository.createRule(LAZY_CLASS.rule())
                .setName("Lazy class")
                .setHtmlDescription("A class that has few fields and methods.")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        lazy_class
                .setDebtRemediationFunction(lazy_class.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        lazy_class.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule long_method = repository.createRule(LONG_METHOD.rule())
                .setName("Long method")
                .setHtmlDescription("A class that has (at least) a method that is very long, in term of LOCs")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        long_method
                .setDebtRemediationFunction(long_method.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        long_method.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule tradition_breaker = repository.createRule(TRADITION_BREAKER.rule())
                .setName("Tradition breaker")
                .setHtmlDescription("A class that inherits from a large parent class but that provides little behaviour and without subclasses")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        tradition_breaker
                .setDebtRemediationFunction(tradition_breaker.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        tradition_breaker.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule long_parameter_list = repository.createRule(LONG_PARAMETER_LIST.rule())
                .setName("Long parameter list")
                .setHtmlDescription("	A class that has (at least) one method with a too long list of parameters in comparison to the average number of parameters per methods in the system")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        long_parameter_list
                .setDebtRemediationFunction(long_parameter_list.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        long_parameter_list.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule many_field_attributes_not_complex = repository.createRule(MANY_FIELD_ATTRIBUTES_NOT_COMPLEX.rule())
                .setName("Many field attribute not complex")
                .setHtmlDescription("A class that declares many attributes but which is not complex and, hence, more likely to be some kind of data class holding values without providing behaviour")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        many_field_attributes_not_complex
                .setDebtRemediationFunction(many_field_attributes_not_complex.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        many_field_attributes_not_complex.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule message_chains = repository.createRule(MESSAGE_CHAINS.rule())
                .setName("Message chains")
                .setHtmlDescription("A class that uses a long chain of method invocations to realise (at least) one of its functionality")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        message_chains
                .setDebtRemediationFunction(message_chains.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        message_chains.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule refused_parent_bequest = repository.createRule(REFUSED_PARENT_BEQUEST.rule())
                .setName("Refused parent bequest")
                .setHtmlDescription("A class that redefines inherited method using empty bodies, thus breaking polymorphism")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        refused_parent_bequest
                .setDebtRemediationFunction(refused_parent_bequest.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        refused_parent_bequest.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule speculative_generality = repository.createRule(SPECULATIVE_GENERALITY.rule())
                .setName("Speculative generality")
                .setHtmlDescription("A class that is defined as abstract but that has very few children, which do not make use of its methods")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        speculative_generality
                .setDebtRemediationFunction(speculative_generality.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        speculative_generality.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);

        final NewRule swiss_army_knife = repository.createRule(SWISS_ARMY_KNIFE.rule())
                .setName("Swiss army knife")
                .setHtmlDescription("A complex class that offers a high number of services, for example, a complex class implementing a high number of interfaces")
                .setTags(TAG_CODESMELL, TAG_ANTIPATTERN)
                .setSeverity(Severity.MAJOR);
        swiss_army_knife
                .setDebtRemediationFunction(swiss_army_knife.debtRemediationFunctions().linearWithOffset("1h", "30min"));
        swiss_army_knife.createParam(ACCEPT_WHITESPACE_PARAM)
                .setDefaultValue("false")
                .setType(RuleParamType.BOOLEAN)
                .setDescription(ACCEPT_WHITESPACES_DESCRIPTION);


        // don't forget to call done() to finalize the definition
        repository.done();
    }
}
