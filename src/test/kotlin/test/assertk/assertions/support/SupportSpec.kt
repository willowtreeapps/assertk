package test.assertk.assertions.support

import assertk.assertions.support.show
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on

private class Dummy(private val i: Int) {
  override fun toString(): String = "Dummy=$i"
}

class SupportSpec : Spek({
  val anonymous = object : Any() {}

  describe("show") {
    on("null") {
      assertThat(show(null)).isEqualTo("<null>")
    }
    on("boolean") {
      assertThat(show(true)).isEqualTo("<true>")
    }
    on("byte") {
      assertThat(show(15.toByte())).isEqualTo("<0x0F>")
    }
    on("char") {
      assertThat(show('c')).isEqualTo("<'c'>")
    }
    on("double") {
      assertThat(show(1.234567890123)).isEqualTo("<1.234567890123>")
    }
    on("float") {
      assertThat(show(1.2345f)).isEqualTo("<1.2345f>")
    }
    on("int") {
      assertThat(show(42)).isEqualTo("<42>")
    }
    on("long") {
      assertThat(show(42L)).isEqualTo("<42L>")
    }
    on("short") {
      assertThat(show(42.toShort())).isEqualTo("<42>")
    }
    on("string") {
      assertThat(show("value")).isEqualTo("<\"value\">")
    }
    group("class") {
      on("predefined") {
        assertThat(show(Dummy::class)).isEqualTo("<class test.assertk.assertions.support.Dummy>")
      }
      on("anonymous") {
        assertThat(show(anonymous::class)).isEqualTo("<class test.assertk.assertions.support.SupportSpec\$1\$anonymous\$1>")
      }
    }
    group("java class") {
      // TODO: mkobit: figure if these are needed for multi-platform support
      on("predefined") {
        assertThat(show(Dummy::class.java)).isEqualTo("<test.assertk.assertions.support.Dummy>")
      }
      on("anonymous") {
        assertThat(show(anonymous::class.java)).isEqualTo("<test.assertk.assertions.support.SupportSpec\$1\$anonymous\$1>")
      }
    }
    group("array") {
      on("generic array") {
        val array = arrayOf(Dummy(0), Dummy(1))
        assertThat(show(array)).isEqualTo("<[Dummy=0, Dummy=1]>")
      }
      on("boolean array") {
        assertThat(show(booleanArrayOf(false, true))).isEqualTo("<[false, true]>")
      }
      on("byte array") {
        assertThat(show(byteArrayOf(10, 15))).isEqualTo("<[0x0A, 0x0F]>")
      }
      on("char array") {
        assertThat(show(charArrayOf('a', 'b'))).isEqualTo("<['a', 'b']>")
      }
      on("double array") {
        assertThat(show(doubleArrayOf(1.2345, 6.789))).isEqualTo("<[1.2345, 6.789]>")
      }
      on("float array") {
        assertThat(show(floatArrayOf(1.2345f, 6.789f))).isEqualTo("<[1.2345f, 6.789f]>")
      }
      on("int array") {
        assertThat(show(intArrayOf(42, 8))).isEqualTo("<[42, 8]>")
      }
      on("long array") {
        assertThat(show(longArrayOf(42L, 8L))).isEqualTo("<[42L, 8L]>")
      }
      on("short array") {
        assertThat(show(shortArrayOf(42, -1))).isEqualTo("<[42, -1]>")
      }
    }
    group("collection") {
      on("list") {
        assertThat(show(listOf(1, 2, 3))).isEqualTo("<[1, 2, 3]>")
      }
      on("nested list") {
        assertThat(show(listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6)
        ))).isEqualTo("<[[1, 2, 3], [4, 5, 6]]>")
      }
      on("set") {
        assertThat(show(setOf(1,2,3))).isEqualTo("<[1, 2, 3]>")
      }
    }
    on("map") {
      assertThat(show(mapOf(1 to 5, 2 to 6))).isEqualTo("<{1=5, 2=6}>")
    }
    on("regex") {
      assertThat(show(Regex("^abcd$"))).isEqualTo("</^abcd$/>")
    }
    on("other type") {
      val other = object : Any() {
        override fun toString(): String = "different"
      }
      assertThat(show(other)).isEqualTo("<different>")
    }
    on("different wrapper") {
      assertThat(show(42, "##")).isEqualTo("#42#")
    }
  }
})
