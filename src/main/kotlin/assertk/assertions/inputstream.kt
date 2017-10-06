package assertk.assertions

import assertk.Assert
import assertk.assertions.support.expected
import java.io.InputStream

fun Assert<InputStream>.hasSameContentAs(other: InputStream) {
    val buf1 = ByteArray(4096)
    val buf2 = ByteArray(4096)

    var size = 0L
    while(true) w@ {
        val len1 = actual.read(buf1)
        val len2 = other.read(buf2)

        when {
            len1<len2 -> expected("to have the same size, but actual has size ${size+len1} which is smaller then the expected stream")
            len1>len2 -> expected("to have the same size, but the expected stream size ${size+len2} which is larger then the acual stream")
            len1==-1 -> return@w
        }

        for(i in 0..len2) {
            val b1 = buf1[i]
            val b2 = buf2[i]
            if(b1!=b2) {
                expected("to have the same content, but found first difference at pos ${size+i}")
            }
        }

        size += len2
    }
}

fun Assert<InputStream>.hasNotSameContentAs(other: InputStream) {
    val buf1 = ByteArray(4096)
    val buf2 = ByteArray(4096)

    var size = 0L
    var equals = true
    while(true) {
        val len1 = actual.read(buf1)
        val len2 = other.read(buf2)

        if(len1!=len2) {
            equals = false
            break
        }
        if(len1==-1) {
            equals = true
            break
        }

        if(!buf1.contentEquals(buf2)) {
            equals = false
            break
        }
    }

    if(equals) {
        expected("stream not to be equal, but they were equal")
    }
}

