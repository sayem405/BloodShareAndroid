/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications copyright (C) 2017 Google Inc
 */

package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


import com.bloodshare.bloodshareandroid.R;

import java.util.Collections;


/**
 * This element inserts spaces between characters in the edit text and expands the width of the
 * spaces using spannables. This is required since Android's letter spacing is not available until
 * API 21.
 */
public final class SpacedEditText extends android.support.v7.widget.AppCompatEditText {
    private float proportion;
    private SpannableStringBuilder originalText;

    public SpacedEditText(Context context) {
        super(context);
    }

    public SpacedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    void initAttrs(Context context, AttributeSet attrs) {
        originalText = new SpannableStringBuilder("");
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpacedEditText);
        //Controls the ScaleXSpan applied on the injected spaces
        proportion = array.getFloat(R.styleable.SpacedEditText_spacingProportion, 1);
        array.recycle();
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        originalText = new SpannableStringBuilder(text);
        final SpannableStringBuilder spacedOutString = getSpacedOutString(text);
        super.setText(spacedOutString, TextView.BufferType.SPANNABLE);
    }

    /**
     * Set the selection after recalculating the index intended by the caller.
     */
    @Override
    public void setSelection(int index) {
        //if the index is the leading edge, there are no spaces before it.
        //for all other cases, the index is preceeded by index - 1 spaces.
        int spacesUptoIndex;
        if (index == 0) {
            spacesUptoIndex = 0;
        } else {
            spacesUptoIndex = index - 1;
        }
        final int recalculatedIndex = index + spacesUptoIndex;

        super.setSelection(recalculatedIndex);
    }

    private SpannableStringBuilder getSpacedOutString(CharSequence text) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        final int textLength = text.length();
        int lastSpaceIndex = -1;

        //Insert a space in front of all characters upto the last character
        //Scale the space without scaling the character to preserve font appearance
        for (int i = 0; i < textLength - 1; i++) {
            builder.append(text.charAt(i));
            builder.append(" ");
            lastSpaceIndex += 2;
            builder.setSpan(new ScaleXSpan(proportion), lastSpaceIndex, lastSpaceIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //Append the last character
        if (textLength != 0) builder.append(text.charAt(textLength - 1));

        return builder;
    }

    public Editable getUnspacedText() {
        return this.originalText;
    }

    public static class BucketedTextChangeListener implements TextWatcher {
        public interface ContentChangeCallback {
            /**
             * Idempotent function invoked by the listener when the edit text changes and is of expected
             * length
             */
            void whileComplete();

            /**
             * Idempotent function invoked by the listener when the edit text changes and is not of
             * expected length
             */
            void whileIncomplete();
        }

        private final EditText editText;
        private final ContentChangeCallback callback;
        private final String[] postFixes;
        private final String placeHolder;
        private final int expectedContentLength;

        public BucketedTextChangeListener(EditText editText, int expectedContentLength, String
                placeHolder, ContentChangeCallback callback) {
            this.editText = editText;
            this.expectedContentLength = expectedContentLength;
            this.postFixes = generatePostfixArray(placeHolder, expectedContentLength);
            this.callback = callback;
            this.placeHolder = placeHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int ignoredParam1, int ignoredParam2, int
                ignoredParam3) {
            // The listener is expected to be used in conjunction with the SpacedEditText.

            // Approach
            // 1) Strip all spaces and hyphens introduced by the SET for aesthetics
            final String numericContents = s.toString().replaceAll(" ", "").replaceAll(placeHolder, "");

            // 2) Trim the content to acceptable length.
            final int enteredContentLength = Math.min(numericContents.length(), expectedContentLength);
            final String enteredContent = numericContents.substring(0, enteredContentLength);

            // 3) Reset the text to be the content + required hyphens. The SET automatically inserts
            // spaces requires for aesthetics. This requires removing and resetting the listener to
            // avoid recursion.
            editText.removeTextChangedListener(this);
            editText.setText(enteredContent + postFixes[expectedContentLength - enteredContentLength]);
            editText.setSelection(enteredContentLength);
            editText.addTextChangedListener(this);

            // 4) Callback listeners waiting on content to be of expected length
            if (enteredContentLength == expectedContentLength && callback != null) {
                callback.whileComplete();
            } else if (callback != null) {
                callback.whileIncomplete();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        /**
         * For example, passing in ("-", 6) would return the following result:
         * {"", "-", "--", "---", "----", "-----", "------"}
         *
         * @param repeatableChar the char to repeat to the specified length
         * @param length the maximum length of repeated chars
         * @return an increasing sequence array of chars up the specified length
         */
        private String[] generatePostfixArray(CharSequence repeatableChar, int length) {
            final String[] ret = new String[length + 1];

            for (int i = 0; i <= length; i++) {
                ret[i] = TextUtils.join("", Collections.nCopies(i, repeatableChar));
            }

            return ret;
        }
    }
}
