# original file path
[http://maven.mit.edu/nexus/content/repositories/public/com/sap/conn/jco/sapjco3/3.0.14/]()

# why copy?
jar filename issue
```
JCo initialization failed with java.lang.ExceptionInInitializerError: Illegal JCo archive "sapjco3-3.0.14.jar". It is not allowed to rename or repackage the original archive "sapjco3.jar".
	at com.sap.conn.jco.rt.MiddlewareJavaRfc.<clinit>(MiddlewareJavaRfc.java:230)
	at com.sap.conn.jco.rt.DefaultJCoRuntime.initialize(DefaultJCoRuntime.java:99)
	at com.sap.conn.jco.rt.JCoRuntimeFactory.<clinit>(JCoRuntimeFactory.java:23)
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:315)
	at com.sap.conn.jco.JCo.createJCo(JCo.java:52)
	at com.sap.conn.jco.JCo.<clinit>(JCo.java:26)
	at com.sap.conn.jco.JCoDestinationManager.getDestination(JCoDestinationManager.java:77)
	at me.saro.sap.jco.SapManagerBuilder.build(SapManagerBuilder.java:57)
	at me.saro.sap.jco.SapManagerBuilder.build(SapManagerBuilder.java:34)
```