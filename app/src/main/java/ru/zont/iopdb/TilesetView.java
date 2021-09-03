package ru.zont.iopdb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TilesetView extends View {
    private int emptyColor = Color.RED;
    private int tileColor = Color.GREEN;
    private int selfColor = Color.BLUE;
    private float preferredTileSize = 0;
    private float hGapSize = 0;
    private float vGapSize = 0;

    private int[] thisSet;
    private int eTileSize;
    private int startX;
    private int startY;

    private Paint emptyPaint;
    private Paint tilePaint;
    private Paint selfPaint;

    public TilesetView(Context context) {
        super(context);
        init(null, 0);
    }

    public TilesetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TilesetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TilesetView, defStyle, 0);

        emptyColor = a.getColor(
                R.styleable.TilesetView_emptyColor,
                getResources().getColor(R.color.tile_gray));
        tileColor = a.getColor(
                R.styleable.TilesetView_effectColor,
                getResources().getColor(R.color.tile_cyan));
        selfColor = a.getColor(
                R.styleable.TilesetView_selfColor,
                getResources().getColor(R.color.tile_white));
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        hGapSize = a.getDimension(
                R.styleable.TilesetView_horizontalGapsSize,
                getResources().getDimension(R.dimen.tileset_defaultGap));
        vGapSize = a.getDimension(
                R.styleable.TilesetView_verticalGapsSize,
                getResources().getDimension(R.dimen.tileset_defaultGap));
        preferredTileSize = a.getDimension(
                R.styleable.TilesetView_verticalGapsSize,
                getResources().getDimension(R.dimen.tileset_preferredTileSize));
        a.recycle();

        invalidateContent();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidateContent();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (thisSet == null || thisSet.length != 9) return;

        int x = startX;
        int y = startY;
        for (int i = 0; i < thisSet.length; i++) {
            final int t = thisSet[i];
            Paint paint = t == 1 ? tilePaint : t == 2 ? selfPaint : emptyPaint;
            canvas.drawRect(x, y, x + eTileSize, y + eTileSize, paint);

            if (i % 3 == 2) {
                x = startX;
                y += eTileSize + vGapSize;
            } else x += eTileSize + hGapSize;
        }
    }

    private void invalidateContent() {
        final int w = getWidth();
        final int h = getHeight();

        if (calculateBounds(w, h)) return;

        emptyPaint = new Paint();
        emptyPaint.setColor(emptyColor);
        tilePaint = new Paint();
        tilePaint.setColor(tileColor);
        selfPaint = new Paint();
        selfPaint.setColor(selfColor);

        if (isInEditMode()) {
            thisSet = new int[]{
                    0,1,1,
                    2,0,0,
                    0,1,1
            };
        }

        invalidate();
    }

    private boolean calculateBounds(int w, int h) {
        final float tilesSizeX = w - getFixedSizeX() - getPaddingLeft() - getPaddingRight();
        final float tilesSizeY = h - getFixedSizeY() - getPaddingTop() - getPaddingBottom();

        if (tilesSizeX <= 0 || tilesSizeY <= 0) {
            setWillNotDraw(true);
            eTileSize = (int) preferredTileSize;
            return true;
        }
        setWillNotDraw(false);

        final float scaleX = tilesSizeX / (getPreferredViewWidth() - getFixedSizeX());
        final float scaleY = tilesSizeY / (getPreferredViewHeight() - getFixedSizeY());

        eTileSize = (int) (Math.min(scaleX, scaleY) * preferredTileSize);
        startX = scaleX > scaleY ? w / 2 - getPreferredViewWidth(eTileSize) / 2 : 0;
        startY = scaleX < scaleY ? h / 2 - getPreferredViewHeight(eTileSize) / 2 : 0;

        startX += getPaddingLeft();
        startY += getPaddingTop();
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        final int wMode = MeasureSpec.getMode(widthMeasureSpec);
        final int h = MeasureSpec.getSize(heightMeasureSpec);
        final int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int rw;
        int rh;
        final int prefSizeX = getPreferredViewWidth();
        final int prefSizeY = getPreferredViewHeight();
        if (wMode == hMode && hMode == MeasureSpec.AT_MOST) {
            rw = Math.min(w, prefSizeX);
            rh = Math.min(h, prefSizeY);
        } else {
            rw = wMode == MeasureSpec.EXACTLY ? w
                    : wMode == MeasureSpec.AT_MOST
                    ? Math.min(prefSizeX, w) : prefSizeX;
            rh = hMode == MeasureSpec.EXACTLY ? h
                    : hMode == MeasureSpec.AT_MOST
                    ? Math.min(prefSizeY, h) : prefSizeY;
        }

        calculateBounds(rw, rh);
        setMeasuredDimension(
                wMode == MeasureSpec.EXACTLY ? rw : getPreferredViewWidth(eTileSize),
                hMode == MeasureSpec.EXACTLY ? rh : getPreferredViewHeight(eTileSize)
        );
    }

    private int getPreferredViewWidth() {
        return getPreferredViewWidth(preferredTileSize);
    }

    private int getPreferredViewHeight() {
        return getPreferredViewHeight(preferredTileSize);
    }

    private int getPreferredViewWidth(float tileSize) {
        return (int) (tileSize * 3 + getFixedSizeX());
    }

    private int getPreferredViewHeight(float tileSize) {
        return (int) (tileSize * 3 + getFixedSizeY());
    }

    private float getFixedSizeX() {
        return hGapSize * 2;
    }

    private float getFixedSizeY() {
        return vGapSize * 2;
    }

    public void setTileset(int[] set) {
        thisSet = set;
        invalidate();
    }
}