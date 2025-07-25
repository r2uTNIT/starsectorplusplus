lazy val root = project
  .in(
    file(".")
  )
  .settings(
    assembly / assemblyJarName := "starsector++.jar",
    target := file("/Program Files (x86)/Fractal Softworks/Starsector/mods/Starsector++/jars"), // this was a pain in the ass to get working, SBT sucks!!
    crossPaths := false, // this makes .class files go to "yourmod/jars/classes" instead of "yourmod/jars/scala-3.3.6/classes", just less folders
    name := "starsector++",
    version := "0.1.0",
    scalaVersion := "3.3.6", // latest LTS (long-term-support) version
    libraryDependencies += "com.thoughtworks.xstream" % "xstream" % "1.4.21", // project won't compile without this!!
    bloopExportJarClassifiers in Global := Some( // metals doesn't recognize dependencies without this!!
        Set("sources")
    )
  )