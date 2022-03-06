package com.niceplaces.niceplaces.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.TextView
import java.util.concurrent.atomic.AtomicBoolean

object TextUtils {
    fun justify(textView: TextView) {
        val isJustify = AtomicBoolean(false)
        val textString = textView.text.toString()
        val textPaint = textView.paint
        val builder = SpannableStringBuilder()
        textView.post {
            try {
                if (!isJustify.get()) {
                    val lineCount = textView.lineCount
                    val textViewWidth = textView.width
                    for (i in 0 until lineCount) {
                        val lineStart = textView.layout.getLineStart(i)
                        val lineEnd = textView.layout.getLineEnd(i)
                        val lineString = textString.substring(lineStart, lineEnd)
                        if (i == lineCount - 1) {
                            builder.append(SpannableString(lineString))
                            break
                        }
                        val trimSpaceText = lineString.trim { it <= ' ' }
                        val removeSpaceText = lineString.replace(" ".toRegex(), "")
                        val removeSpaceWidth = textPaint.measureText(removeSpaceText)
                        val spaceCount = trimSpaceText.length - removeSpaceText.length.toFloat()
                        val eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount
                        val spannableString = SpannableString(lineString)
                        for (j in 0 until trimSpaceText.length) {
                            val c = trimSpaceText[j]
                            if (c == ' ') {
                                val drawable: Drawable = ColorDrawable(0x00ffffff)
                                drawable.setBounds(0, 0, eachSpaceWidth.toInt(), 0)
                                val span = ImageSpan(drawable)
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }
                        builder.append(spannableString)
                    }
                    textView.text = builder
                    isJustify.set(true)
                }
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }
}