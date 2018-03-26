package com.dgsw.tu.tuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.dgsw.tu.tuapplication.utils.FriendAddAdapter;
import com.dgsw.tu.tuapplication.utils.FriendAddListItem;
import com.dgsw.tu.tuapplication.utils.FriendsAdapter;

public class SidebarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FriendsAdapter friendListAdapter = new FriendsAdapter(getContext(), R.layout.activity_frend_list_item);
        FriendAddAdapter friendAddAdapter = new FriendAddAdapter(getContext(), R.layout.activity_friend_add_list_item);

        View view = inflater.inflate(R.layout.fragment_sidebar, container, false);
        ListView friendList = (ListView)view.findViewById(R.id.friendsList);
        ListView friendAddList = (ListView)view.findViewById(R.id.friendApplyList);

        if (friendAddAdapter.getCount()==0){
            view.findViewById(R.id.countImage).setVisibility(View.GONE);
            view.findViewById(R.id.countText).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.countImage).setVisibility(View.VISIBLE);
            view.findViewById(R.id.countText).setVisibility(View.VISIBLE);
            TextView countText = (TextView) view.findViewById(R.id.countText);
            countText.setText(friendAddAdapter.getCount());
        }

        friendList.setAdapter(friendListAdapter);
        friendAddList.setAdapter(friendAddAdapter);

        friendListAdapter.notifyDataSetChanged();
        friendAddAdapter.notifyDataSetChanged();

        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.sidebar_profileImage);
        TextView name = (TextView) view.findViewById(R.id.sidebar_profileName);
        name.setText(MyVolley.getinstance(getContext()).getId());
        imageView.setImageUrl(MyVolley.getinstance(getContext()).SERVER_IP+MyVolley.getinstance(getContext()).getProFileImg(), MyVolley.getinstance(getContext()).getImageLoader());//Error get url
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyTimeLineActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}
