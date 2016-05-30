# sonarqube-code-smells

<h2> Description </h2>
This plugin detects the Ptidej 5 antipatterns from source code every time each analysis.

<h2> List of the antipattern detected ant its definitions </h2>

<b>AntiSingleton:</b> A class that provides mutable class variables, which consequently could be used as global variables.
<b>BaseClassKnowsDerivedClass:</b> A class that invokes or has at least binary-class relationship pointing to one of its subclasses.
<b>BaseClassShouldBeAbstract:</b> 	A class that has many subclasses without being abstract.
