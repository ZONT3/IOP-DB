package ru.zont.iopdb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Random;

public class RarityView extends View {
    private int[] rarityColors;

    private int rarityValue = -1;

    private Drawable starDrawable;
    private int starSizeDefault = 0;

    private Drawable toDraw;
    private int starMargin;
    private int contentStartX;
    private int contentStartY;
    private int starSize = 0;

    public RarityView(Context context) {
        super(context);
        init(null, 0);
    }

    public RarityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RarityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RarityView, defStyle, 0);

        starDrawable = a.getDrawable(R.styleable.RarityView_starDrawable);
        if (starDrawable == null)
            starDrawable = ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.star,
                    getContext().getTheme());

        assert starDrawable != null;
        starDrawable.setCallback(this);

        rarityColors = new int[]{
            getColorByRes(R.color.rarity_ex),
            getColorByRes(R.color.rarity_1s),
            getColorByRes(R.color.rarity_2s),
            getColorByRes(R.color.rarity_3s),
            getColorByRes(R.color.rarity_4s),
            getColorByRes(R.color.rarity_5s),
            getColorByRes(R.color.rarity_6s),
        };

        starMargin = (int) a.getDimension(
                R.styleable.RarityView_starMargin,
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        1,
                        getResources().getDisplayMetrics()));
        starSizeDefault = (int) a.getDimension(
                R.styleable.RarityView_starHeightDefault,
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        16,
                        getResources().getDisplayMetrics()));

        rarityValue = a.getInteger(R.styleable.RarityView_rarityValue, -1);

        a.recycle();

        invalidateContent();
    }

    private int getColorByRes(int id) {
        return ResourcesCompat.getColor(getResources(), id, getContext().getTheme());
    }

    private void invalidateContent() {
        final int width = getWidth();
        final int height = getHeight();
        if (width <= 0 || height <= 0) return;

        if (rarityValue < 0) rarityValue = new Random().nextInt(rarityColors.length);
        starSize = height;

        int contentWide = getContentWide(starSize);
        if (contentWide > width) {
            contentWide = width;
            starSize = getStarSize(contentWide);
        }

        contentStartX = width / 2 - contentWide / 2;
        contentStartY = height / 2 - starSize / 2;

        toDraw = starDrawable.getConstantState().newDrawable(getResources()).mutate();
        DrawableCompat.setTint(toDraw, getRarityColor());
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int hMode = MeasureSpec.getMode(heightMeasureSpec);
        final int wMode = MeasureSpec.getMode(heightMeasureSpec);
        final int contentWide = getContentWide(heightSize);

        if (hMode != MeasureSpec.EXACTLY)
            heightSize = Math.min(starSizeDefault,
                hMode == MeasureSpec.AT_MOST ? heightSize : starSizeDefault);

        if (wMode != MeasureSpec.EXACTLY) {
            widthSize = Math.min(contentWide,
                    wMode == MeasureSpec.AT_MOST ? widthSize : contentWide);
        }

        if (contentWide > widthSize && hMode != MeasureSpec.EXACTLY)
            heightSize = getStarSize(widthSize);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidateContent();
    }

    private int getContentWide(int starSize) {
        int count = rarityValue > 0 ? rarityValue : 1;
        return starSize * count + starMargin * (count - 1);
    }

    private int getStarSize(int contentWide) {
        int count = rarityValue > 0 ? rarityValue : 1;
        return contentWide / count - starMargin * (count - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (toDraw == null) return;

        int count = rarityValue > 0 ? rarityValue : 1;
        int x = contentStartX;
        for (int i = 0; i < count; i++) {
            toDraw.setBounds(x, contentStartY, x + starSize, contentStartY + starSize);
            x += starSize + starMargin;
            toDraw.draw(canvas);
        }
    }

    private int getRarityColor() {
        if (rarityValue >= 0 && rarityValue < rarityColors.length)
            return rarityColors[rarityValue];
        return 0xff0000;
    }

    public int getRarityValue() {
        return rarityValue;
    }

    public void setRarityValue(int rarityValue) {
        if (rarityValue < 0) throw new IllegalArgumentException();
        this.rarityValue = rarityValue;
        invalidateContent();
    }

    public int getColor() {
        return getRarityColor();
    }

}