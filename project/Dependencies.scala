import sbt._
import play.sbt.PlayImport._

object Dependencies {

  val h2 = "com.h2database" % "h2" % "1.4.196"

  val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test

  val backendDeps = Seq(guice, h2, evolutions, jdbc, scalaTestPlusPlay)
}
