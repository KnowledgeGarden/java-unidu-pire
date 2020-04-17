# java-unidu-spire
Spire, Distributed Datalog, HySpirit

This is a clone of the [Pire project] (https://www.is.inf.uni-due.de/projects/java-unidu/) with<br/>
* Upgrade patches to JDK 1.8
* Conversion (incomplete) to a Maven project

It is based on the very last distribution; it is known that the project ended at the tragic death of its developer Dipl.-Inform. Henrik Nottelmann

Permission to continue with the project was granted by Prof. Dr.-Ing. Norbert Fuhr

Some publications are listed [here](https://www.is.inf.uni-due.de/projects/pire/)

## Comments
* Maven is incomplete because some dependencies are not available in Maven. Those dependencies are in the /lib directory; they must be added to the classpath (both in an IDE such as Eclipse, and in shell scripts)
* A sample shellscript *pdlExample.sh* is included to illustrate how to bring it up
* Mavenizing provoked a few _classpath_ issues which - as a hack - were resolved in the file de.unidu.is.util.SystemUtilities
* A missing file *common_words* was added to /conf


