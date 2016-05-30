# sonarqube-code-smells

<h2> Description </h2>
This plugin detects the Ptidej 5 antipatterns from source code every time each analysis.

<h2> List of the antipattern detected ant its definitions </h2>

AntiSingleton: A class that provides mutable class variables, which consequently could be used as global variables.
BaseClassKnowsDerivedClass: A class that invokes or has at least binary-class relationship pointing to one of its subclasses.
BaseClassShouldBeAbstract: 	A class that has many subclasses without being abstract.
