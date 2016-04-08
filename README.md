# Android-UML-Generator

Execution flows in the applications sometimes can be difficult to visualize without having UML diagrams. In particular sequence diagrams allows to capture the flow logic of a program, logic methods, usage program, and logic of services. This type of diagrams can be represented using UML notation and in order to export this diagram some programs like Enterprise Architect use the XMI specification. The goals of this project are to automatically generate sequence diagrams based on the usage of the application, and also link the high­level GUI events into the low­level method calls.

In order to accomplish this, we have two different sets of code. 

The first bunch of code is used to create an Xposed module. Xposed doesn't work like a standard GUI application, as such, you create a module that you access through the general Xposed application. 

The second set of code is a GUI application. This application lists all applications on the phone, when the user selects one, our Xposed module listens to the GUI applcation. The GUI applications creates a DEX file with the string of the package source directory code. With this, it then is able to parse through the application to find all classes and methods of that package (application).

At this point the Xposed module is listening and gathers the name of all available methods and and classes and hooks them. With this we are able to to tell when a method is used.

The next step is to create an XML document, that contains the information about when a method is called.

Finally, once the XML document is correct, we will convert the XML into an XMI UML diagram.

# Technologies used:
Xposed - An application that can hook methods, and do certain things before, or after the method is used. Useful for being able to track when a method is used

# References
http://modeling-languages.com/xmi-nightmares-argouml-xmi-format-change/ - Here we can see where problems from XMI are

# Team Members:
Tim Fulton, Miles Peele, Jake Shor, Tristan Vernon

# Notes
Note, because our project at this point is rather well established, we didn't want to add directories to further confuse the structure. Those we have added an artifacts tag for issues. Here we will tag things that should be in artifacts
