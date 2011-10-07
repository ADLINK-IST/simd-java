import sbt._
import Keys._

object SimdJavaBuild extends Build
{
   lazy val simdjava = Project("simd-java", file(".")) dependsOn(idlib)
   lazy val idlib = Project("idlib", file("idlib")) settings(
      name := "idlib"
   )
}
