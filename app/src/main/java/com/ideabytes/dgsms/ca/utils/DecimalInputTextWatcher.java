package com.ideabytes.dgsms.ca.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
/*************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : DecimalInputTextWatcher
 * author:  Suman
 * Description :To Restrict Weight Edit text to avoid decimals after . Means after.It
 * should allow only two decimals Ex. 123.34 valid, 11233.456 not valid
 * Modified Date : 27-10-2015
 *****************************************************************************************/

public class DecimalInputTextWatcher implements TextWatcher {

	private String mPreviousValue;
	private boolean mIgnoreIteration;
	private int mDigitsAfterZero;
	private EditText mEditText;

	public DecimalInputTextWatcher(EditText editText, int digitsAfterZero) {
		mDigitsAfterZero = digitsAfterZero;
		mEditText = editText;
		mPreviousValue = "";
		mIgnoreIteration = false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (!mIgnoreIteration)
			mPreviousValue = s.toString();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!mIgnoreIteration) {

			if (!isValid(s.toString())) {
				mIgnoreIteration = true;
				mEditText.setText(mPreviousValue);
				mEditText.setSelection(mPreviousValue.length());
			}

		} else {
			mIgnoreIteration = false;
		}
	}

	private boolean isValid(String s) {
		Pattern patternWithDot = Pattern.compile("[0-9]*((\\.[0-9]{0,"
				+ mDigitsAfterZero + "})?)||(\\.)?");
		Pattern patternWithComma = Pattern.compile("[0-9]*((,[0-9]{0,"
				+ mDigitsAfterZero + "})?)||(,)?");

		Matcher matcherDot = patternWithDot.matcher(s);
		Matcher matcherComa = patternWithComma.matcher(s);

		return matcherDot.matches() || matcherComa.matches();
	}
}
