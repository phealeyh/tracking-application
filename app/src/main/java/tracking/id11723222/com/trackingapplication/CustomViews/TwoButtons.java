package tracking.id11723222.com.trackingapplication.CustomViews;

/**
 * Created by phealeyhang on 31/10/15.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import tracking.id11723222.com.trackingapplication.Constants;
import tracking.id11723222.com.trackingapplication.R;

public class TwoButtons extends View {

    private Path path;
    private Path path1;
    private Region region;
    private Region region1;

    private ButtonClickEvents mButtonClickEvent;

    /**
     * This will set the declaration of the two image button clicks that need
     * to be later implemented by the TrackingActivity class.
     */
    public interface ButtonClickEvents{
        public void startButtonClick();
        public void resetButtonClick();
    }
    /**
     *Sets the member Click Event to the one given
     *by the given paramater
     *@param buttonClickEvent
     *
     */

    public void setOnButtonClickEvent(ButtonClickEvents buttonClickEvent) {
        mButtonClickEvent = buttonClickEvent;
    }

    /**
     * This constructor will call the inherited class's constructor and
     * pass in the context and attrs variables given as arguments.
     *@param context
     *@param attrs
     */

    public TwoButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**This method will draw the square with diagonal line
     * travelling across the bottom-left to top-right. It will
     * then color the regions red and blue, respectively.
     *@param canvas
     *
     */

    @Override
    protected void onDraw(Canvas canvas) {
        //generate area canvas
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.BLACK);
        canvas.drawPaint(paint);


        paint.setStrokeWidth(Constants.INITIAL_LOCATION);
        paint.setColor(android.graphics.Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Point a = new Point(Constants.GAP, Constants.GAP);
        Point b = new Point(Constants.GAP, getHeight() - 2 * Constants.GAP);
        Point c = new Point(getWidth() - 2 * Constants.GAP, Constants.GAP);

        path = new Path();
        path.setFillType(FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.close();

        canvas.drawPath(path, paint);

        //set start region
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        paint.setColor(Color.BLUE);
        Point a1 = new Point(getWidth()- Constants.GAP, getHeight()- Constants.GAP);
        Point b1 = new Point(getWidth()- Constants.GAP, 2* Constants.GAP);
        Point c1 = new Point(2* Constants.GAP, getHeight()- Constants.GAP);

        //draw diagonal line
        path1 = new Path();
        path1.setFillType(FillType.EVEN_ODD);
        path1.moveTo(a1.x, a1.y);
        path1.lineTo(b1.x, b1.y);
        path1.lineTo(c1.x, c1.y);

        path1.close();
        canvas.drawPath(path1, paint);
        //set reset region

        RectF rectF1 = new RectF();
        path1.computeBounds(rectF1, true);
        region1 = new Region();
        region1.setPath(path1, new Region((int) rectF1.left, (int) rectF1.top, (int) rectF1.right, (int) rectF1.bottom));


    }
    /**
     *
     * Set up the listeners associated with the given
     * regions
     *@param event
     *
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                Point point = new Point();
                point.x = (int) event.getX();
                point.y = (int) event.getY();

                invalidate();

                if(region.contains((int)point.x,(int) point.y)) {
                    if(mButtonClickEvent!=null){
                        mButtonClickEvent.startButtonClick();
                    }
                }else if(region1.contains((int)point.x,(int) point.y)) {
                    if(mButtonClickEvent!=null) {
                        mButtonClickEvent.resetButtonClick();
                    }
                }

                return true;
        }

        return false;
    }

}
