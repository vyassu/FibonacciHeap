
JAVAFLAGS = -g
JC = javac
JVM= java 

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JAVAFLAGS) $*.java

CLASSES = \
        Fibbo.java \
        hashtagcounter.java \
        Node.java

MAIN = hashtagcounter 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class


