package com.niceplaces.niceplaces.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class TextUtils {

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                try {

                    if (!isJustify.get()) {

                        final int lineCount = textView.getLineCount();
                        final int textViewWidth = textView.getWidth();

                        for (int i = 0; i < lineCount; i++) {

                            int lineStart = textView.getLayout().getLineStart(i);
                            int lineEnd = textView.getLayout().getLineEnd(i);

                            String lineString = textString.substring(lineStart, lineEnd);

                            if (i == lineCount - 1) {
                                builder.append(new SpannableString(lineString));
                                break;
                            }

                            String trimSpaceText = lineString.trim();
                            String removeSpaceText = lineString.replaceAll(" ", "");

                            float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                            float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                            float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                            SpannableString spannableString = new SpannableString(lineString);
                            for (int j = 0; j < trimSpaceText.length(); j++) {
                                char c = trimSpaceText.charAt(j);
                                if (c == ' ') {
                                    Drawable drawable = new ColorDrawable(0x00ffffff);
                                    drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                    ImageSpan span = new ImageSpan(drawable);
                                    spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }

                            builder.append(spannableString);
                        }

                        textView.setText(builder);
                        isJustify.set(true);
                    }

                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
