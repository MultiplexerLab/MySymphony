package harmony.app.helper;

import android.content.Context;
import android.media.session.MediaController;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CustomController extends android.widget.MediaController{
    Context context;
    public CustomController(Context context, View anchor) {
        super(context);
        this.context=context;
        setAnchorView(anchor);
    }

    @Override
    public void setAnchorView(View view)     {
        Button fullScreen = new Button(context);
        fullScreen.setText("FullScreen");
        Log.e("media controller","Set anchorView");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(view.getWidth(), 0, 5, 20);
        params.gravity =  Gravity.RIGHT;
        addView(fullScreen, params);

        fullScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("media controller","full screen onclick");

               /* Intent i = new Intent("xyxyxyxhx");

                context.sendBroadcast(i);*/

            }
        });
    }
}
