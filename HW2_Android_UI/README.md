# HW2_Android_UI
Homework2 for course CS175 in SJTU  
Use Java to finish some **exercises** related to **Android UI**.

The following code is **just the core part and incomplete**, please refer to the specific document for details.

## Ex1
- [x] ex1-1: Complete the relevant attributes of the *lottie* control.
```xml
  app:lottie_rawRes="@raw/material_wave_loading"
  app:lottie_loop="true"
```
- [x] ex1-2: Invoke the right function in *onProgressChanged()*.
```java
    if(fromUser){
        animationView.setProgress(progress/100.0f);
        animationView.resumeAnimation();
//      Log.d("progress",Float.toString(animationView.getProgress()));
    }
```
<p align="center">
<img src="https://user-images.githubusercontent.com/50905239/139236510-4ef96f9b-48cb-4963-92f9-5c4acbc3ba09.png" width="150" height="280"/>
</p>  

## Ex2
- [x] ex2-1: Add *scale* animation.
```java
  // TODO ex2-1：在这里实现另一个 ObjectAnimator，对 target 控件的大小进行缩放，从 1 到 2 循环
  ObjectAnimator animator2 = ObjectAnimator.ofFloat(target,
      "scaleX",
          1f,1.2f,1.4f,1.6f,1.8f,2.0f
  );
  animator2.setRepeatCount(Animation.INFINITE);
  animator2.setRepeatMode(ObjectAnimator.REVERSE);
  animator2.setDuration(2000);
```
- [x] ex2-2: Add *alpha* animation.
```java
    // TODO ex2-2：在这里实现另一个 ObjectAnimator，对 target 控件的透明度进行修改，从 1 到 0.5f 循环
    ObjectAnimator animator3 = ObjectAnimator.ofFloat(target, "alpha",
            1.0f,0.75f, 0.5f, 0.75f,1.0f);
    animator3.setRepeatCount(Animation.INFINITE);
    animator3.setDuration(4000);
```
- [x] ex2-3: Combine into *AnimatorSet*.
```java
    // TODO ex2-3: 将上面创建的其他 ObjectAnimator 都添加到 AnimatorSet 中
    animatorSet = new AnimatorSet();
    animatorSet.playTogether(animator1,animator2,animator3);
    animatorSet.start();
```
<p align="center">
<img src="https://user-images.githubusercontent.com/50905239/139238030-e3bffb9c-8767-4404-a17b-da25c3695a81.png" width="150" height="280"/>
</p>  

## Ex3(Optional)
- [x] Use ViewPager and Fragment to make a sliding interface.
```xml
    <android.support.v4.view.ViewPager
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
```java
    // TODO: ex3-1. 添加 ViewPager 和 Fragment 做可滑动界面
    // Instantiate a ViewPager and a PagerAdapter.
    mPager = (ViewPager) findViewById(R.id.pager);
    pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    mPager.setAdapter(pagerAdapter);
```
- [x] Use TabLayout to add Tab support.
```xml
    <android.support.design.widget.TabLayout
        android:id="@+id/tab_layout"
        android:layout_width="match_parent"
        app:tabIndicatorColor="@android:color/holo_blue_light"
        app:tabTextColor="@android:color/darker_gray"
        app:tabSelectedTextColor="@android:color/black"
        android:layout_height="wrap_content" />
```
```java
    // TODO: ex3-2, 添加 TabLayout 支持 Tab
    mTab=(TabLayout)findViewById(R.id.tab_layout);
    mTab.setTabMode(TabLayout.MODE_FIXED);
    for (String tab:titles){
        mTab.addTab(mTab.newTab().setText(tab));
    }
    mTab.setupWithViewPager(mPager);
```
- [x] Add loading control and list view control.
```java
    <com.airbnb.lottie.LottieAnimationView
        android:id="@+id/my_lottie"
        android:layout_width="150dp"
        android:layout_height="150dp"
        app:lottie_autoPlay="true"
        app:lottie_loop="true"
        android:layout_centerInParent="true"
        app:lottie_repeatMode="reverse"
        app:lottie_rawRes="@raw/material_wave_loading"
        android:layout_gravity="center"
        />
    <ListView
        android:id="@+id/my_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="45dp"
        android:minHeight="25dp"/>

```
- [x] Fade out the lottie control, and fade in the list data 
```java
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
```
Basically, the implementation can achieve the effect.
<p align="center">
<img src="https://user-images.githubusercontent.com/50905239/139240328-94c8b6dd-2237-4f53-8aeb-5667b3ab89ce.png" width="150" height="280"/>
<img src="https://user-images.githubusercontent.com/50905239/139240332-64ef969d-14a5-49bf-93f6-795b9bcbd6d7.png" width="150" height="280"/>
</p> 
But sometimes there may be the following bugs:  

1. Android will load from the cache (Showing:`D/Gabe: call	returning from cache` ),so the fragment will not be reloaded and the animation will not be displayed in these cases. 
2. Switching windows quickly may cause the program to crash.


------
All exercises come from **ByteDance**.

