/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bloodshare.bloodshareandroid.ui.login.phoneLogin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bloodshare.bloodshareandroid.R;


public final class CompletableProgressDialog extends DialogFragment {
    private static final String TAG = CompletableProgressDialog.class.getSimpleName();

    private ProgressBar mProgress;
    private TextView mMessageView;
    private CharSequence mMessage;
    private ImageView mSuccessImage;

    public static CompletableProgressDialog show(FragmentManager manager) {
        CompletableProgressDialog dialog = new CompletableProgressDialog();
        dialog.show(manager, TAG);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.completable_progress_dialog, null);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mMessageView = (TextView) rootView.findViewById(R.id.progress_msg);
        mSuccessImage = (ImageView) rootView.findViewById(R.id.progress_success_imaage);

        if (mMessage != null) {
            setMessage(mMessage);
        }

        return new AlertDialog.Builder(getActivity()).setView(rootView).create();
    }

    public void onComplete(String msg) {
        setMessage(msg);
        mProgress.setVisibility(View.GONE);
        mSuccessImage.setVisibility(View.VISIBLE);
    }

    public void setMessage(CharSequence message) {
        if (mProgress != null) {
            mMessageView.setText(message);
        } else {
            mMessage = message;
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
