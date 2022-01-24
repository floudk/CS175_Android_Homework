package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView animationView;
    private ListView my_listView;
    private String[] namelist;
    private AnimatorSet animatorSet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View viewRoot = inflater.inflate(R.layout.fragment_placeholder, container, false);

        my_listView = viewRoot.findViewById(R.id.my_list);
        animationView =viewRoot.findViewById(R.id.my_lottie);
//              viewRoot.findViewById(R.id.my_lottie).setVisibility(View.VISIBLE);
////              viewRoot.findViewById(R.id.my_list).setVisibility(View.VISIBLE);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
           namelist = bundle.getStringArray("namelist");
        }else{
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            namelist = new String[]{"err1","err2"};
        }

        Log.d("receive",namelist[0]);
        return viewRoot;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                //fade out
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(animationView,"alpha",1,0f);
                animator1.setInterpolator(new LinearInterpolator());
                animator1.setDuration(500);
                //fade in
                CustomAdapter myAdapter = new CustomAdapter(getContext(), android.R.layout.simple_list_item_1, Arrays.asList(namelist));
                my_listView.setAdapter(myAdapter);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(my_listView,"alpha",0,1f);

                animator2.setInterpolator(new LinearInterpolator());
                animator2.setDuration(500);


                animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator1,animator2);
                animatorSet.start();

            }
        }, 5000);
    }
    private class CustomAdapter extends ArrayAdapter<String> {
        List<String> mStrings;
        public CustomAdapter(Context context, int resource, List<String> strings) {
            super(context, resource);
            mStrings = strings;
        }

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public String getItem(int position) {
            return mStrings.get(position);
        }

    }
}
