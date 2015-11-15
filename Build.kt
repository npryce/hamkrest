
import com.beust.kobalt.*
import com.beust.kobalt.api.*
import com.beust.kobalt.plugin.java.javaCompiler
import com.beust.kobalt.plugin.kotlin.kotlinProject
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.jcenter

val hamkrest = kotlinProject {
    name = "hamkrest"
    group = "com.natpryce"
    artifactId = name
    version = "1.0-SNAPSHOT"

    dependenciesTest {
        compile("junit:junit:4.12")
    }

    assemble {
        mavenJars {
        }
    }

//    test {
//    }

    jcenter {
        publish = true
    }
}
