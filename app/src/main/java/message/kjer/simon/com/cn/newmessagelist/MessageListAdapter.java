package message.kjer.simon.com.cn.newmessagelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static message.kjer.simon.com.cn.newmessagelist.R.layout.adapter_main_list_item;

/**
 * @author simon.
 * Date: 2018/7/10.
 * Description:
 */
public class MessageListAdapter extends BaseAdapter {
    private final Context mContext;
    private LinkedList<TipsMessage> mDataList;

    private LayoutInflater mInflater;
    private Animation animation;
    public static final String Tag="MessageListAdapter";

    public MessageListAdapter(Context context, LinkedList<TipsMessage> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(context);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.item_animation);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (convertView == null) {
        convertView = mInflater.inflate(adapter_main_list_item, parent, false);
        holder = new ViewHolder();
        holder.contentTv = (MyDataTextView) convertView.findViewById(R.id.content_tv);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        if (mDataList != null && mDataList.size() > 0 && holder.contentTv.getMessage() == null) {
            final ViewHolder finalHolder = setItemData(position, holder);
            //点击移除
            holder.contentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeClickItem((MyDataTextView) v, finalHolder, position);
                }
            });

            Log.e(Tag,"!isFirstadd ="+!mDataList.get(position).isFirstAdd());
        if(!mDataList.get(position).isFirstAdd()){

            finalHolder.contentTv.startAnimation(animation);
            mDataList.get(position).setFirstAdd(true);
        }
            postRemoveItem(position, holder);
        }
        return convertView;
    }

    private void removeClickItem(MyDataTextView v, ViewHolder finalHolder, int position) {
        finalHolder.contentTv.removeCallbacks(null);
        MyDataTextView vv = v;
        TipsMessage msg = vv.getMessage();
//                    TipsMessage t = mDataList.get(position);
        Log.e(Tag, " position=" + position + "  index=" + mDataList
                .indexOf(msg));
        Utils.updateMsgCount(msg);
        mDataList.remove(msg);
        MessageListAdapter.this.notifyDataSetChanged();
    }

    @NonNull
    private ViewHolder setItemData(int position, ViewHolder holder) {
        holder.contentTv.setMessage(mDataList.get(position));
        int type = mDataList.get(position).getType();
        if (type > 0) {
            holder.contentTv.setTextColor(Utils.getTypeColor(mContext, type));
        }
        String content = mDataList.get(position).getContent();
        if (!TextUtils.isEmpty(content)) {
            holder.contentTv.setText(content);
        }
        final ViewHolder finalHolder = holder;

        holder.contentTv.setMessage(mDataList.get(position));
        return finalHolder;
    }

    private void postRemoveItem(int position, ViewHolder holder) {
        final ViewHolder finalHolder1 = holder;
//                //type 1
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mDataList != null &&
//                        mDataList.size() > position) {
//                    TipsMessage t = mDataList.get(position);
//                    if (t != null) {
//                        Log.e(Tag, "adapter  content=== " + mDataList.get
//                                (position).getContent());
//                        mDataList.remove(t);
//                        checkMap.remove(finalHolder1.contentTv.hashCode());
//                        Utils.updateMsgCount(t);
//                        MessageListAdaptet.this.notifyDataSetChanged();
//                    }
//                }
//            }
//        }, mDataList.get(position).getHintTime());

        //type 2
        holder.contentTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalHolder1.contentTv.removeCallbacks(null);
                if (mDataList != null && mDataList.size() > 0) {
                    TipsMessage message = finalHolder1.contentTv.getMessage();
                    int indexOf = mDataList.indexOf(message);
                    if (indexOf >= 0) {
                        Log.e(Tag, "----------adapter  content= "
                                + message.getContent() + "  indexOf=" + indexOf);
//                    TipsMessage t = mDataList.get(position);
                        mDataList.remove(message);
                        checkMap.remove(finalHolder1.contentTv.hashCode());
                        Utils.updateMsgCount(message);
                        MessageListAdapter.this.notifyDataSetChanged();
                    }
                }
            }
        }, mDataList.get(position).getHintTime());

//      holder.contentTv.setTag(finalHolder1.contentTv.hashCode(), true);

        //type3
//       updateItemViews(holder, position);
    }

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Boolean> checkMap = new HashMap<>();

    private class ViewHolder {
        MyDataTextView contentTv;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MessageListAdapter.this.notifyDataSetChanged();
        }
    };


    private void updateItemViews(final ViewHolder holder, final int position) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                Log.e("MainActivity", "updateItemViews  ===>   hashCode()=" + holder.contentTv
//                        .hashCode());
                holder.contentTv.removeCallbacks(null);
                if (mDataList != null && mDataList.size() >= 0) {
                    TipsMessage message = holder.contentTv.getMessage();
                    int indexOf = mDataList.indexOf(message);
                    Log.e(Tag, "adapter  content=== " + message.getContent() +
                            "  position=" + position + "  indexOf=" + indexOf);
//                    TipsMessage t = mDataList.get(position);
                    mDataList.remove(message);
                    checkMap.remove(holder.contentTv.hashCode());
                    Utils.updateMsgCount(message);
                    mHandler.sendEmptyMessage(0);
//                    MessageListAdaptet.this.notifyDataSetChanged();
                }
            }
        };
        timer.schedule(timerTask, mDataList.get(position).getHintTime());
    }
}