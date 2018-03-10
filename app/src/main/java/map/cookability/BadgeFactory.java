package map.cookability;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by xiaochengjiang on 3/10/18.
 */

public class BadgeFactory {
    public static RedDotNotificationBadgeView create(Context context) {
        return new RedDotNotificationBadgeView(context);
    }

    public static RedDotNotificationBadgeView createDot(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(10, 10)
                .setTextSize(0)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPE_CIRCLE);
    }

    public static RedDotNotificationBadgeView createCircle(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(16, 16)
                .setTextSize(12)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPE_CIRCLE);
    }

    public static RedDotNotificationBadgeView createRectangle(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(2, 20)
                .setTextSize(12)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPE_RECTANGLE);
    }

    public static RedDotNotificationBadgeView createOval(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(25, 20)
                .setTextSize(12)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPE_OVAL);
    }

    public static RedDotNotificationBadgeView createSquare(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(20, 20)
                .setTextSize(12)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPE_SQUARE);
    }

    public static RedDotNotificationBadgeView createRoundRect(Context context) {
        return new RedDotNotificationBadgeView(context).setBadgeLayoutParams(25, 20)
                .setTextSize(12)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBackgroundShape(RedDotNotificationBadgeView.SHAPTE_ROUND_RECTANGLE);
    }
}
