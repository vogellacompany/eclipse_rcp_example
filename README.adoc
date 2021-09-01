== Eclipse 4 Book image:https://travis-ci.org/vogellacompany/openreq.svg?branch=master["Build Status", link="https://travis-ci.org/vogellacompany/openreq"] image:https://img.shields.io/badge/License-EPL%202.0-blue.svg["EPL 2.0", link="https://www.eclipse.org/legal/epl-2.0/"]

This is the example code for the Eclipse RCP training sessions from vogella GmbH and from the http://www.vogella.com/books/eclipsercp.html[Eclipse Rich Client Platform book].

=== Running the application

Clone the project and import all projects into the Eclipse IDE.

Then open the _target-platform.target_ file of the _target-platform_ project and 
set this target definition as target platform for your workspace.

When the compile errors are gone the target platform has been properly set and the product located
 in the com.example.e4.rcp.todo.product project can be run.


=== Building the application

Use the following command one the command line to build the application:

mvn clean verify

This requires Maven to be installed on your machine.


