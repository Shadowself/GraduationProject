package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.ViewUtil;

/**
 * Created by Mr_zhang on 2015/3/31.
 */
public class BaseActivity extends Activity {

    protected Context mContext = BaseActivity.this;

    // 标题栏以下部分内容布局类对象
    protected LinearLayout viewContent = null;
    protected TextView comm_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_base);
        viewContent = (LinearLayout) findViewById(R.id.viewBaseContent);
        viewContent.setVisibility(View.VISIBLE);
        comm_title = (TextView) findViewById(R.id.comm_title);
    }

    /**
     * 初始化中间部分内容布局方法
     * */
    public void setChildContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResId, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        viewContent.addView(v,params);
        //设置中间布局可以长按才能触发手势
        viewContent.setLongClickable(true);
    }

    /**
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
     * 再按一次退出程序
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
     */
    private long exitTime = 0;
    public static final String MSG_KEY_AGAIN = "再按一次退出程序";

    /**
     * 按下返回按钮的事件，可以提示：再按一次退出程序。</p>
     * 在需要的窗口中覆盖 <code>onKeyDown</code> 即可，并在代码中直接调用该方法。
     * <pre>
     * @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
    return this.onKeyDown0(keyCode, event);
    }
     * </pre>
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - exitTime > 2000){
                ViewUtil.showToast(mContext,MSG_KEY_AGAIN);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                shutdownActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
