# Android-UML-Generator

Execution flows in the applications sometimes can be difficult to visualize without having UML diagrams. In particular sequence diagrams allows to capture the flow logic of a program, logic methods, usage program, and logic of services. This type of diagrams can be represented using UML notation and in order to export this diagram some programs like Enterprise Architect use the XMI specification. The goals of this project are to automatically generate sequence diagrams based on the usage of the application, and also link the high­level GUI events into the low­level method calls.

In order to accomplish this, we have one module that functions as an Xposed module.

When our Xposed module detects that a package has loaded, it checks our SharedPrefs file to see if it should listen to that package. If so, it hooks into that package's context, binding a service to it that will listen when the package itself has loaded, when a class has been constructed, and when a method has been called using the Messenger interface.

Currently, our service takes that data and then writes it to a XML file in XMI format.

Our future plan is to implement the XMI-UML conversion itself. We envision that, when the user wants the corresponding XML diagram, our application sends that file up to a server, which will convert it to UML then send it back down to the application. The application will then allow the user to easily transmit this UML diagram either by email or another service, like Dropbox.

# Technologies used:
Xposed - An application that can hook methods, and do certain things before, or after the method is used. Useful for being able to track when a method is used
Lombok - Simplifies the interaction between models and the classes that use them by generating getters/ setters dynamically.
Dagger - A dependency-injection framework, useful in Android for following the Singleton pattern correctly
ButterKnife - An easier way to bind views from XML to code by using annotations
RxJava - For asynchronous method calls, useful for loading work off the main thread
SimpleXML - Writes model objects to XML files
Retrofit - Simplifies the interaction between server and application

# References
http://modeling-languages.com/xmi-nightmares-argouml-xmi-format-change/ - Here we can see where problems from XMI are

# Team Members:
Tim Fulton, Miles Peele, Jake Shor, Tristan Vernon

# Notes
Note, because our project at this point is rather well established, we didn't want to add directories to further confuse the structure. Those we have added an artifacts tag for issues. Here we will tag things that should be in artifacts
