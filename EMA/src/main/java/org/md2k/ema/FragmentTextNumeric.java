package org.md2k.ema;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.md2k.datakitapi.time.DateTime;
import org.md2k.utilities.Report.Log;

import java.util.ArrayList;


/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 * <p/>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p/>
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p/>
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 */
public class FragmentTextNumeric extends FragmentBase {
    private static final String TAG = FragmentTextNumeric.class.getSimpleName();
    EditText editText;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static FragmentTextNumeric create(int pageNumber, String id, String file_name) {
        FragmentTextNumeric fragment = new FragmentTextNumeric();
        fragment.setArguments(getArgument(pageNumber, id, file_name));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void setEditTextFocused() {
        if (editText.getText().toString().equals(Constants.TAP)) {
            editText.setText("");
        }
        editText.setTextColor(getResources().getColor(android.R.color.black));
        updateNext(false);
    }

    void setEditTextNotFocused() {
        if (editText.getText().toString().length() == 0) {
            editText.setText(Constants.TAP);
        }
        if (editText.getText().toString().equals(Constants.TAP))
            editText.setTextColor(getResources().getColor(R.color.teal_100));
        else editText.setTextColor(getResources().getColor(android.R.color.black));
    }

    void setEditText(ViewGroup rootView) {
        editText = (EditText) rootView.findViewById(R.id.editTextNumber);
        setEditTextNotFocused();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String response = editText.getText().toString();
                response = response.trim();
                ArrayList<String> responses = new ArrayList<>();
                responses.add(response);
                if (response.length() > 0) {
                    questionAnswer.setResponse(responses);
                } else questionAnswer.getResponse().clear();

                updateNext(isAnswered());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String response = editText.getText().toString();
                response = response.trim();
                ArrayList<String> responses = new ArrayList<>();
                responses.add(response);
                if (response.length() > 0) {
                    questionAnswer.setResponse(responses);
                } else questionAnswer.getResponse().clear();

            updateNext(isAnswered());

            return false;
        }
    });
/*
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG,"onEditorAction()..");
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String response = editText.getText().toString();
                    response = response.trim();
                    ArrayList<String> responses = new ArrayList<>();
                    responses.add(response);
                    if (response.length() > 0) {
                        questionAnswer.setResponse(responses);
                    } else questionAnswer.getResponse().clear();
                }
                updateNext(isAnswered());
                return false;
            }
        });
*/
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d(TAG, "Focus=" + b);
                if (b)
                    setEditTextFocused();
                else setEditTextNotFocused();

            }
        });
    }

    @Override
    public void onPause() {
        if (!editText.getText().toString().equals(Constants.TAP) && editText.getText().toString().length() != 0) {
            questionAnswer.getResponse().clear();
            questionAnswer.getResponse().add(editText.getText().toString());

        }
        hideKeyboard();
        super.onPause();
    }

    public boolean isAnswered() {
        Log.d(TAG,"isAnswered..."+questionAnswer.getResponse().size());
        int lowerLimit = 0, higherLimit = 0;
        boolean lv = false, rv = false;
        if (questionAnswer.getResponse_option().size() > 0) {
            lowerLimit = Integer.parseInt(questionAnswer.getResponse_option().get(0));
            lv = true;
        }
        if (questionAnswer.getResponse_option().size() > 1) {
            higherLimit = Integer.parseInt(questionAnswer.getResponse_option().get(1));
            rv = true;
        }
        if (questionAnswer.getResponse().size() > 0) {
            Log.d(TAG,"isAnswered..."+questionAnswer.getResponse().get(0));
            try {
                int num = Integer.parseInt(questionAnswer.getResponse().get(0));
                if (lv && num < lowerLimit) {
                    Toast.makeText(getActivity(),"Value must be in between "+lowerLimit+" and "+ higherLimit,Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (rv && num > higherLimit){
                    Toast.makeText(getActivity(),"Value must be in between "+lowerLimit+" and "+ higherLimit,Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }catch(Exception e){
                Toast.makeText(getActivity(),"Value must be in between "+lowerLimit+" and "+ higherLimit,Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
//            Toast.makeText(getActivity(),"Value must be in between "+lowerLimit+" and "+ higherLimit,Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        updateNext(isAnswered());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() mPageNumber=" + mPageNumber);
        final ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_text_numeric, container, false);
        questionAnswer.setPrompt_time(DateTime.getDateTime());
        setQuestionText(rootView, questionAnswer);
        setEditText(rootView);
        return rootView;
    }
}
