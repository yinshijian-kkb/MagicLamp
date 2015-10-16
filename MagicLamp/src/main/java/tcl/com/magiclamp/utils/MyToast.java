package tcl.com.magiclamp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import tcl.com.magiclamp.R;

/**
 * 居中显示toast
 * @author ck
 *
 */
public class MyToast {
    /**
     * 是否显示由此进度条
     */
    private  boolean showPbBar;
    private  Context mContext;
    private  View  mPbBar;
    private  Dialog mDialog;
    /**
     * 当前dlg 是否正在显示
     */
    private  boolean isShowing;
    /**
     * 
     * @param ctx
     * @param showPbBar 是否显示进度条
     */
    public MyToast(Context ctx,boolean showPbBar)
    {
    	mContext = ctx;
    	this.showPbBar = showPbBar;
    	initDialog();
    }
	private void initDialog() {
		// TODO Auto-generated method stub
		   mDialog =new Dialog(mContext, R.style.loading_dialog);
		   mDialog.getWindow().getAttributes().windowAnimations = R.style.toast_anim;
	       View view = View.inflate(mContext, R.layout.dialog_loading, null);
	       mPbBar = view.findViewById(R.id.pb_loadingdialog);
	       if(!showPbBar)
	       {
	          mPbBar.setVisibility(View.GONE);
	       }
	       tv_msg = (TextView) view.findViewById(R.id.tv_msg_loadingdialog);
	       mDialog.setContentView(view);
	       mDialog.setCanceledOnTouchOutside(false);
	       // 屏蔽用户点击物理返回键时 关闭dlg
	       //mDialog.setCancelable(false);
	}
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mDialog.dismiss();
		};
	};
	private TextView tv_msg;
	/**
	 *  设置是否显示右侧进度条
	 * @param boo
	 */
	public void showPbBar(boolean boo)
	{
		showPbBar = boo;
	}
	/**
	 *  显示toast
	 */
	public void show()
	{
		isShowing  = true;
		if(showPbBar)
		{
			mPbBar.setVisibility(View.VISIBLE);
		}else
		{
			mPbBar.setVisibility(View.GONE);
		}
		
		if(mContext != null && mDialog!=null)
		   mDialog.show();
		if(!showPbBar)
		 {
			 // 显示一秒自动消失
			 new Thread(){
				 public void run() {
					 try {
						Thread.sleep(1000);
						mhandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 };
			
			 }.start();
		 }
	}
	/**
	 *  设置显示文本内容
	 */
	public void setContentText(String contentText)
	{
		tv_msg.setText(contentText);
	}
	/**
	 * 隐藏对话框
	 */
	public void dismiss()
	{
		if(isShowing)
		{
		  mDialog.dismiss();
		  isShowing = false;
		}
	}
}
