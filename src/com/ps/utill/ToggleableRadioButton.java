package com.ps.utill;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ToggleableRadioButton extends RadioButton {
    // Implement necessary constructors

    public ToggleableRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
    public void toggle() {
        if(isChecked()) {
            if(getParent() instanceof RadioGroup) {
                ((RadioGroup)getParent()).clearCheck();
            }
        } else {
            setChecked(true);
        }
    }
}
