This sonarqube plugin detects anti patterns and code smells, as defined by Martin Fowler<sup>[1](#myfootnote1)</sup>, by means of the [Ptidej 5 library](https://bitbucket.org/ptidejteam/ptidej-5).

## Antipatterns and code smells detected (Metrics)

**AntiSingleton:** A class that provides mutable class variables, which consequently could be used as global variables.

**BaseClassKnowsDerivedClass:** A class that invokes or has at least binary-class relationship pointing to one of its subclasses.

**BaseClassShouldBeAbstract:** A class that has many subclasses without being abstract.

**Blob:** A large controller class that depends on data stored in surrounding data classes. A large class declares many fields and methods with a low cohesion.

**ClassDataShouldBePrivate:** A class that exposes its fields, thus violating the principle of encapsulation.

**ComplexClass:** A class that has (at least) one large and complex method, in terms of cyclomatic complexity and LOCs.

**FunctionalDecomposition:** A main class, i.e., a class with a procedural name, such as Compute or Display, in which inheritance and polymorphism are scarcely used, that is associated with small classes, which declare many private fields and implement only a few methods.

**LargeClass:** A class that has grown too large in term of LOCs.

**LazyClass:** A class that has few fields and methods.

**LongMethod:** A class that has (at least) a method that is very long, in term of LOCs.

**LongParameterList:** A class that has (at least) one method with a too long list of parameters in comparison to the average number of parameters per methods in the system.

**ManyFieldAttributesButNotComplex:** A class that declares many attributes but which is not complex and, hence, more likely to be some kind of data class holding values without providing behaviour.

**MessageChains:** A class that uses a long chain of method invocations to realise (at least) one of its functionality.

**RefusedParentBequest:** A class that redefines inherited method using empty bodies, thus breaking polymorphism.

**SpaghettiCode:** A class with no structure, declaring long methods with no parameters, and utilising global variables.

**SpeculativeGenerality:** A class that is defined as abstract but that has very few children, which do not make use of its methods

**SwissArmyKnife:** A complex class that offers a high number of services, for example, a complex class implementing a high number of interfaces.

**TraditionBreaker:** A class that inherits from a large parent class but that provides little behaviour and without subclasses.

##  Licence

GNU Public License v3.

##  Compatibility
This plug-in has been tested with Sonarqube 6.3 and 6.7

##  How to install

1. Copy  jar file in the plugins folder of your Sonarqube installation.

2. Restart the Sonarqube process in your machine.

3. Add plugin rules to your Quality Profile. They can be filtered by repository or tag name:
    1. _Repository:_ "Code-smell rules repository Java"
    2. _Tags:_ "antipattern","code-smell"

## Compiling the source code

Use `mvn clean verify` to ensure the launching of integration tests

## Roadmap

* Increasing analysis performances 
* Standalone implementation (without the ptidej-5 libraries) 
* Empirical study to test the code smell effectiveness 


## References

<a name="myfootnote1">1</a>: Fowler, Martin. "CodeSmell". http://martinfowler.com/. Retrieved July 2014
