package com.project.user.mapsapp.FloatingButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

/**
 * Created by User on 22/06/2016.
 */
public class AddFloatingButton extends FloatingActionButton {
    int mPlusColor;
    public AddFloatingButton(Context context) {
        super(context);
    }

    public AddFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddFloatingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, com.getbase.floatingactionbutton.R.styleable.AddFloatingActionButton, 0, 0);
        mPlusColor = attr.getColor(com.getbase.floatingactionbutton.R.styleable.AddFloatingActionButton_fab_plusIconColor, getColor(android.R.color.white));
        attr.recycle();

        super.init(context, attributeSet);
    }

    public int getPlusColor() {
        return mPlusColor;
    }

    public void setPlusColorResId(@ColorRes int plusColor) {
        setPlusColor(getColor(plusColor));
    }

    public void setPlusColor(int color) {
        if (mPlusColor != color) {
            mPlusColor = color;
            updateBackground();
        }
    }
    public void setIcon(@DrawableRes int icon) {
        throw new UnsupportedOperationException("Use FloatingActionButton if you want to use custom icon");
    }
    Drawable getIconDrawable() {
        final float iconSize = getDimension(com.getbase.floatingactionbutton.R.dimen.fab_icon_size);
        final float iconHalfSize = iconSize / 2f;

        final float plusSize = getDimension(com.getbase.floatingactionbutton.R.dimen.fab_plus_icon_size);
        final float plusHalfStroke = getDimension(com.getbase.floatingactionbutton.R.dimen.fab_plus_icon_stroke) / 2f;
        final float plusOffset = (iconSize - plusSize) / 2f;

        final Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.drawRect(plusOffset, iconHalfSize - plusHalfStroke, iconSize - plusOffset, iconHalfSize + plusHalfStroke, paint);
                canvas.drawRect(iconHalfSize - plusHalfStroke, plusOffset, iconHalfSize + plusHalfStroke, iconSize - plusOffset, paint);
            }
        };

        ShapeDrawable drawable = new ShapeDrawable(shape);

        final Paint paint = drawable.getPaint();
        paint.setColor(mPlusColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        return drawable;
    }
}
