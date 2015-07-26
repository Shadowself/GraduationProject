package com.zgy.graduation.graduationproject.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.adapter.TestHistoryAdapter;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;
import com.zgy.graduation.graduationproject.util.httpurlUtil;

/**
 * Created by Mr_zhang on 2015/4/21.
 * description : the history for test information
 */
public class TestHistoryActivity extends BaseActivity {
    private static final String TAG = TestHistoryActivity.class.getSimpleName();
    private ListView historyList;
    private TestHistoryAdapter testHistoryAdapter;
    private JSONArray jsonArray = new JSONArray();
    private JSONObject jsonObject = new JSONObject();
    //分享到微信
    private IWXAPI api;
    private static final String APP_ID = "wx9edb790c5fd00fa5";
    // 发送的目标场景，WXSceneSession表示发送到会话
    private static final int WXSceneSession = 0;
    private static final int WXSceneTimeline = 1;

    //PopupWindow
    private PopupWindow popupWindow;
    private View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_testhistory);
        comm_title.setText(R.string.getTestInfoHistory);
        back_main.setVisibility(View.VISIBLE);

        historyList = (ListView) findViewById(R.id.historyList);
        testHistoryAdapter = new TestHistoryAdapter(this);
        historyList.setAdapter(testHistoryAdapter);
        historyList.setEmptyView(findViewById(R.id.warning_text));
        getJsonData();

        parent = findViewById(R.id.main);
        View contentView = getLayoutInflater()
                .inflate(R.layout.popwindow_xml, null);

        TextView shareToFriend = (TextView)contentView.findViewById(R.id.shareToFriend);
        Drawable drawableFriend = mContext.getResources().getDrawable(R.mipmap.share_wechat);
        shareToFriend.setCompoundDrawablesWithIntrinsicBounds(null, drawableFriend, null, null);
        shareToFriend.setCompoundDrawablePadding(2);
        shareToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.showToast(mContext,"分享给朋友");
                sendWxUrl(WXSceneSession, jsonObject);
            }
        });

        TextView shareToCilcle = (TextView)contentView.findViewById(R.id.shareToCilcle);
        Drawable drawableFeed = mContext.getResources().getDrawable(R.mipmap.share_wechat_timeline);
        shareToCilcle.setCompoundDrawablesWithIntrinsicBounds(null, drawableFeed, null, null);
        shareToCilcle.setCompoundDrawablePadding(2);
        shareToCilcle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtil.showToast(mContext,"分享到朋友圈");
                sendWxUrl(WXSceneTimeline, jsonObject);
            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        /**设置PopupWindow弹出和退出时候的动画效果*/
        popupWindow.setAnimationStyle(R.style.animation);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jsonObject = jsonArray.getJSONObject(position);
//                sendWxUrl(WXSceneSession,jsonObject);
                openPopWindow();

            }
        });
        registToWX();

    }

    public void openPopWindow( ) {
        /**设置PopupWindow弹出后的位置*/
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    public void sendWxUrl(int scene,JSONObject jsonObject){

        String text = jsonObject.getString(ReqCmd.TESTRESULT);
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
//         msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        if (scene == 0) {
            // 分享到微信会话
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else {
            // 分享到微信朋友圈
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
//        req.scene = isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        api.sendReq(req);

//        this.finish();

    }

    // 过滤发送内容格式
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public void registToWX(){
        // 注册微信sdk
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
    }

    public void getJsonData(){
        String url = String.format(getString(R.string.testInfo_url), httpurlUtil.getUrl(this));
        JSONObject jsonString = new JSONObject();
        jsonString.put(ReqCmd.FLAG, ReqCmd.CHANGE_FLAG);
        jsonString.put(ReqCmd.STOREHOUSEID,preferencesUtil.getString(ReqCmd.STOREHOUSEID));
        SweetAlertDialogUtils.showProgressDialog(this, getString(R.string.waiting), false);

        HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(mContext);
        httpAsyncTaskManager.requestStream(url, jsonString.toJSONString(), new StringTaskHandler() {
                    @Override
                    public void onNetError() {
                        ViewUtil.showToast(mContext, getString(R.string.network_error));
                    }

                    @Override
                    public void onSuccess(String result) {

                        try {
                            ResData resData = JSONObject.parseObject(result, ResData.class);
                            switch (resData.getCode_()) {
                                // resData.getcode_()=0;
                                case ReqCmd.RESULTCODE_SUCCESS:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    jsonArray = JSON.parseArray(resData.getData());
                                    testHistoryAdapter.addDatas(jsonArray);
                                    historyList.setAdapter(testHistoryAdapter);
                                    LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(TestHistoryActivity.this,R.anim.translate_list_layout));
                                    lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                                    historyList.setLayoutAnimation(lac);
                                    historyList.startLayoutAnimation();
                                    break;
                                default:
                                    ViewUtil.showToast(mContext, resData.getMessage_());
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    public void onFail() {
                        ViewUtil.showToast(mContext, getString(R.string.server_error));
                    }

                    @Override
                    public void onFinish() {
                        SweetAlertDialogUtils.closeProgressDialog();
                    }
                }
        );
    }

}
