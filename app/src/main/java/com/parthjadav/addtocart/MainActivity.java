package com.parthjadav.addtocart;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements QuantityChangeListener {

    private GridLayoutManager mGridLayoutManager;
    RecyclerView recyclerView;
    FoodItemAdapter foodItemAdapter;
    public ArrayList<FoodItem> foodArrayList;
    ArrayList<FoodItem> checkoutArray;
    ImageView mDummyImgView, mCheckoutImgView;
    TextView price_txt, counttxt;
    LinearLayout footer;
    int totalprice = 0;
    int totalcount = 0;
    int actionbarheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodArrayList = new ArrayList<>();
        checkoutArray = new ArrayList<FoodItem>();

        footer = (LinearLayout) findViewById(R.id.footer);
        price_txt = (TextView) findViewById(R.id.price);
        counttxt = (TextView) findViewById(R.id.counttxt);
        mDummyImgView = (ImageView) findViewById(R.id.img_cpy);
        mCheckoutImgView = (ImageView) findViewById(R.id.chk_icon);

        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionbarheight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }


        foodArrayList.add(new FoodItem("no description", "180", "200", "", "001", "Tiffin", "Masala Dosa", 0, 0));
        foodArrayList.add(new FoodItem("no description", "280", "300", "", "002", "Tiffin", "Idli", 0, 0));
        foodArrayList.add(new FoodItem("no description", "80", "100", "", "003", "Tiffin", "Chappatti", 0, 0));
        foodArrayList.add(new FoodItem("no description", "99", "150", "", "004", "Tiffin", "Poori", 0, 0));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        foodItemAdapter = new FoodItemAdapter(this, this, foodArrayList, this);
        recyclerView.setAdapter(foodItemAdapter);
    }

    @Override
    public void onQuantityChange(String menuid, int count, int price, View cardv) {
        if (cardv != null) {
            Bitmap b = FoodOrderApplication.getInstance().loadBitmapFromView(cardv, cardv.getWidth(), cardv.getHeight());
            animateView(cardv, b);
        }

        for (int i = 0; i < foodArrayList.size(); i++) {
            if (foodArrayList.get(i).getMenuid().equals(menuid)) {
                totalprice = totalprice + count * Integer.parseInt(foodArrayList.get(i).getPrice());
                totalcount = totalcount + count;
                if (foodArrayList.get(i).getCount() > 0) {
                    if (checkoutArray.contains(foodArrayList.get(i))) {

                    } else
                        checkoutArray.add(foodArrayList.get(i));

                } else {
                    if (checkoutArray.contains(foodArrayList.get(i))) {

                        checkoutArray.remove(foodArrayList.get(i));
                    }
                }
            }
        }

        price_txt.setText(getResources().getString(R.string.rs) + totalprice);
        counttxt.setText("" + totalcount);
        Animation shake;
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mCheckoutImgView.setAnimation(shake);
    }

    private void animateView(View foodCardView, Bitmap b) {
        mDummyImgView.setImageBitmap(b);
        mDummyImgView.setVisibility(View.VISIBLE);
        int u[] = new int[2];
        mCheckoutImgView.getLocationInWindow(u);
        mDummyImgView.setLeft(foodCardView.getLeft());
        mDummyImgView.setTop(foodCardView.getTop());
        AnimatorSet animSetXY = new AnimatorSet();

        ObjectAnimator rotation = ObjectAnimator.ofFloat(mDummyImgView,
                "rotation", 0f, 360f);

        ObjectAnimator y = ObjectAnimator.ofFloat(mDummyImgView, "translationY", mDummyImgView.getTop(), u[1] - (2 * actionbarheight));
        ObjectAnimator x = ObjectAnimator.ofFloat(mDummyImgView, "translationX", mDummyImgView.getLeft(), u[0] - (2 * actionbarheight));

        ObjectAnimator sy = ObjectAnimator.ofFloat(mDummyImgView, "scaleY", 0.8f, 0.1f);
        ObjectAnimator sx = ObjectAnimator.ofFloat(mDummyImgView, "scaleX", 0.8f, 0.1f);

        animSetXY.playTogether(x, y, sx, sy, rotation);
        animSetXY.setDuration(1000);
        animSetXY.start();
    }

    public Bitmap loadBitmapFromView(View view, int width, int height) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        Log.e("width", "=" + width);
        Log.e("height", "=" + height);
        return returnedBitmap;
    }

}
