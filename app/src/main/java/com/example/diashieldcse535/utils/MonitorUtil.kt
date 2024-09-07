package com.example.diashieldcse535.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import kotlin.math.min

class MonitorUtil {

    companion object{

        fun heartRateCalculator(uri: Uri): Int {
            val result: Int

            val retriever = MediaMetadataRetriever()
            val frameList = ArrayList<Bitmap>()
            try {
                retriever.setDataSource(uri.path)
                val duration =
                    retriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT
                    )
                val frameDuration = min(duration!!.toInt(), 425)
                var i = 10
                while (i < frameDuration) {
                    val bitmap = retriever.getFrameAtIndex(i)
                    bitmap?.let { frameList.add(it) }
                    i += 15
                }
            } catch (e: Exception) {
                Log.d("MediaPath", "convertMediaUriToPath: ${e.stackTrace} ")
            } finally {
                retriever.release()
                var redBucket: Long
                var pixelCount: Long = 0
                val a = mutableListOf<Long>()
                for (i in frameList) {
                    redBucket = 0
                    for (y in 350 until 450) {
                        for (x in 350 until 450) {
                            val c: Int = i.getPixel(x, y)
                            pixelCount++
                            redBucket += Color.red(c) + Color.blue(c) +
                                    Color.green(c)
                        }
                    }
                    a.add(redBucket)
                }
                val b = mutableListOf<Long>()
                for (i in 0 until a.lastIndex - 5) {
                    val temp =
                        (a.elementAt(i) + a.elementAt(i + 1) + a.elementAt(i + 2)
                                + a.elementAt(
                            i + 3
                        ) + a.elementAt(
                            i + 4
                        )) / 4
                    b.add(temp)
                }
                var x = b.elementAt(0)
                var count = 0
                for (i in 1 until b.lastIndex) {
                    val p = b.elementAt(i)
                    if ((p - x) > 3500) {
                        count += 1
                    }
                    x = b.elementAt(i)
                }
                val rate = ((count.toFloat()) * 60).toInt()
                result = (rate / 4)
            }
            return result
        }
    }
}