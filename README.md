# sonarqube-code-smells

<h2> Description </h2>
This plugin detects the Ptidej 5 (https://bitbucket.org/ptidejteam/ptidej-5) antipatterns from source code every time after each analysis.

<h2> List of the antipattern detected ant its definitions </h2>

<b>AntiSingleton:</b> A class that provides mutable class variables, which consequently could be used as global variables.
<p><b>BaseClassKnowsDerivedClass:</b> A class that invokes or has at least binary-class relationship pointing to one of its subclasses.</p>
<b>BaseClassShouldBeAbstract:</b> A class that has many subclasses without being abstract.
<p><b>Blob:</b> A large controller class that depends on data stored in surrounding data classes. A large class declares many fields and methods with a low cohesion.</p>
<p><b>ClassDataShouldBePrivate:</b> A class that exposes its fields, thus violating the principle of encapsulation. </p>
<p><b>ComplexClass:</b>A class that has (at least) one large and complex method, in terms of cyclomatic complexity and LOCs. </p>
<p><b>FunctionalDecomposition:</b> A main class, i.e., a class with a procedural name, such as Compute or Display, in which inheritance and polymorphism are scarcely used, that is associated with small classes, which declare many private fields and implement only a few methods. </p>
<p><b>LargeClass:</b> A class that has grown too large in term of LOCs. </p>
<p><b>LazyClass:</b> 	A class that has few fields and methods. </p>
<p><b>LongMethod:</b> A class that has (at least) a method that is very long, in term of LOCs. </p>
<p><b>LongParameterList:</b> A class that has (at least) one method with a too long list of parameters in comparison to the average number of parameters per methods in the system. </p>
<p><b>ManyFieldAttributesButNotComplex:</b> A class that declares many attributes but which is not complex and, hence, more likely to be some kind of data class holding values without providing behaviour. </p>
<p><b>MessageChains:</b> A class that uses a long chain of method invocations to realise (at least) one of its functionality. </p>
<p><b>RefusedParentBequest:</b> A class that redefines inherited method using empty bodies, thus breaking polymorphism. </p>
<p><b>SpaghettiCode:</b> A class with no structure, declaring long methods with no parameters, and utilising global variables. </p>
<p><b>SpeculativeGenerality:</b> A class that is defined as abstract but that has very few children, which do not make use of its methods </p>
<p><b>SwissArmyKnife:</b> A complex class that offers a high number of services, for example, a complex class implementing a high number of interfaces. </p>
<p><b>TraditionBreaker:</b> A class that inherits from a large parent class but that provides little behaviour and without subclasses.
</p>
 
<h2> Licence </h2>
GNU Public License v2.

<h2> How to install </h2>
<p> -- Put the jar Antipatterns.0.1.jar from the target folder in the plugins folder of your Sonarqube installation.</p>
<p> -- Restart the Sonarqube process in your machine. </p>




