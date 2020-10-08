package com.jeannypr.scientificstudy.Utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.jeannypr.scientificstudy.R;

import static java.lang.reflect.Array.getInt;

/**
 * Created by kannuk on 06/03/2020
 **/
public class GradientTextView extends AppCompatTextView {

    public int getGradientStartColor() {
        return gradientStartColor;
    }

    public void setGradientStartColor(int gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
    }

    public int getGradientEndColor() {
        return gradientEndColor;
    }

    public void setGradientEndColor(int gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
    }

    int gradientStartColor;
    int gradientEndColor;

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomGradientColor);
        gradientStartColor = ta.getColor(R.styleable.CustomGradientColor_gradientStartColor, -1);
        gradientEndColor = ta.getColor(R.styleable.CustomGradientColor_gradientEndColor, -1);
        ta.recycle();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {
          /*  getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    ContextCompat.getColor(getContext(), R.color.colorPrimary),
                    ContextCompat.getColor(getContext(), R.color.colorAccent),
                    Shader.TileMode.CLAMP));*/

            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                   gradientStartColor,
                    gradientEndColor,
                    Shader.TileMode.CLAMP));
        }
    }
}
