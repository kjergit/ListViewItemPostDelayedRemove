package message.kjer.simon.com.cn.newmessagelist;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * @author simon.
 * Date: 2018/7/10.
 * Description:
 */
public class MessageListAdaptet extends BaseAdapter {
    private final Context mContext;
    private LinkedList<TipsMessage> mDataList;

    private LayoutInflater mInflater;

    public MessageListAdaptet(Context context, LinkedList<TipsMessage> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_main_list_item, parent, false);

            holder = new ViewHolder();
            holder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDataList != null && mDataList.size() > 0) {
            int type = mDataList.get(position).getType();
            if (type > 0) {
                holder.contentTv.setTextColor(Utils.getTypeColor(mContext, type));
            }
            String content = mDataList.get(position).getContent();
            if (!TextUtils.isEmpty(content)) {
                holder.contentTv.setText(content);
            }
            final ViewHolder finalHolder = holder;
        holder.contentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.contentTv.removeCallbacks(null);
                TipsMessage t = mDataList.get(position);
                Utils.updateMsgCount(t);
                mDataList.remove(t);
                MessageListAdaptet.this.notifyDataSetChanged();
            }
        });

//            holder.contentTv.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (mDataList != null && mDataList.size() > 0) {
//                        TipsMessage t = mDataList.get(position);
//                        if (t != null) {
//                            Log.d("MainActivity", "---->   postDelayed... position="
//                                    + position +"  t===" + t);
//                            Utils.updateMsgCount(t);
//                            mDataList.remove(t);
//                            Log.d("MainActivity","adapter   ...mDataList size="+mDataList.size());
//                            MessageListAdaptet.this.notifyDataSetChanged();
//                        }
//                    }
//                }
//            }, mDataList.get(position).getHintTime());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView contentTv;
    }
}
