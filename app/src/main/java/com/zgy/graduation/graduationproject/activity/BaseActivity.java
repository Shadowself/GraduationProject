package com.zgy.graduation.graduationproject.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.PreferencesUtil;
import com.zgy.graduation.graduationproject.util.ViewUtil;

import java.util.Stack;

/**
 * Created by Mr_zhang on 2015/3/31.
 * description:baseActivity for common option and end App
 */
public class BaseActivity extends Activity{

    protected Context mContext = BaseActivity.this;
    private static Stack<Activity> activityStack = new Stack<Activity>();
    protected PreferencesUtil preferencesUtil = null;
    // 标题栏以下部分内容布局类对象
    protected LinearLayout viewContent = null;
    protected TextView comm_title;
    protected ImageView back_main;

    private ProgressDialog pdpd;

    protected static final String[] items = new String[] { "选择本地图片", "拍照" };

    /* 照片名称 */
    protected static final String IMAGE_FILE_NAME = "tempPicture.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_base);
        activityStack.push(this);
        viewContent = (LinearLayout) findViewById(R.id.viewBaseContent);
        viewContent.setVisibility(View.VISIBLE);
        comm_title = (TextView) findViewById(R.id.comm_title);
        back_main = (ImageView) findViewById(R.id.back_main);
        back_main.setVisibility(View.INVISIBLE);
        back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });

        preferencesUtil = new PreferencesUtil(this);
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
                shutdownActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isEmptyStack()){
            activityStack.pop();
        }
    }

    public static boolean isEmptyStack(){
        return activityStack.empty();
    }

    public static Activity peek(){
        if(isEmptyStack()){
            return null;
        } else {
            return activityStack.peek();
        }
    }

    public static int getActivityStackSize(){
        return activityStack.size();
    }

    public static void push(Activity paramActivity){
        activityStack.push(paramActivity);
    }

    public static void remove(Activity paramActivity){
        activityStack.remove(paramActivity);
    }

    /**
     * 从自定义堆栈中清理掉已经停止使用（移除系统栈顶）的activity对象。
     */
    public static void clearActivitiesFromStack(){
        Stack<Activity> localStack = activityStack;
        for (int i = activityStack.size(); i > 0; i--){
            Activity localActivity = (Activity)localStack.get(i - 1);
            localActivity.finish();
        }
        activityStack.clear();
    }

    public static final void shutdownActivities(){
//		TaskExecutor.getInstance().execute(new ClearActivityTask0());
        clearActivitiesFromStack();
        System.exit(0);
    }

    public static class ClearActivityTask0 implements Runnable{
        @Override
        public void run() {
            // TODO Auto-generated method stub
            clearActivitiesFromStack();
        }
    }

    protected void onBackClick(){
        finish();
    }

    protected void showProgressDialog(String message, boolean canBack) {
        closeProgressDialog();
        pdpd = ProgressDialog.show(mContext, "", message, true, true);
        pdpd.setCanceledOnTouchOutside(false);
        pdpd.setCancelable(canBack);
    }

    public void closeProgressDialog() {
        if (pdpd != null && pdpd.isShowing())
            pdpd.dismiss();
    }

}
