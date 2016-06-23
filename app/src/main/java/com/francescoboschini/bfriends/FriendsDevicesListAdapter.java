package com.francescoboschini.bfriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import io.realm.RealmResults;

public class FriendsDevicesListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private RealmResults<FriendDevice> mData;

    public FriendsDevicesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(RealmResults<FriendDevice> data) {
        mData = data;
    }

    public int getCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView =  mInflater.inflate(R.layout.friend_device_item, null);

            holder = new ViewHolder();

            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.addressTv = (TextView) convertView.findViewById(R.id.tv_address);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendDevice device	= mData.get(position);

        holder.nameTv.setText(device.getName());
        holder.addressTv.setText(device.getMacAddress());

        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView addressTv;
    }
}

