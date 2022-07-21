// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.16")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.13.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-ebean" % "6.0.0")
addSbtPlugin("au.com.onegeek" %% "sbt-dotenv" % "1.2.88")

