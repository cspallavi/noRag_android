package com.example.pallavi.norag;

/**
 * Created by Pallavi on 28/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.pallavi.norag.R.id.imageView2;

/**
 * Created by Lincoln on 30/10/15.
 */
public class DivideItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;
/*
    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }
*/
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    public static class PchatAdapter extends RecyclerView.Adapter<PchatAdapter.MyViewHolder> {
        ImageView imageView1,imageView3,imageView4;int sessionid;File f;Bitmap bmp;CircleImageView imageview2;LinearLayoutManager l;
        private List<Get_set_pchat> pchatList;
        Context context;Activity main;




        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView username,message,textView2;ImageView imageview2;
            //RelativeLayout l;
            //SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(PchatAdapter.this);
            //sessionid=sp.getInt("sessionid",-1);
            public MyViewHolder(View view) {
                super(view);
                username = (TextView) view.findViewById(R.id.username);
                message = (TextView) view.findViewById(R.id.message);
                textView2 = (TextView) view.findViewById(R.id.textView2);
                imageView1 = (ImageView) view.findViewById(R.id.imageView1);
                imageview2 = (CircleImageView) view.findViewById(imageView2);
                /*imageView3 = (ImageView) view.findViewById(R.id.imageView3);
                imageView4 = (ImageView) view.findViewById(R.id.imageView4);*/

                //imageView4 = (ImageView) view.findViewById(R.id.imageView4);

                // l=(RelativeLayout)view.findViewById(R.id.list);
            }



        }
        /* public void scroll_to_bottom(RecyclerView recyclerView,LinearLayoutManager lm)
          {
              recyclerView.setLayoutManager(lm);
              lm.setStackFromEnd(true);
              Log.v("inside scroll",":::::::::success");

              l.scrollToPosition(pchatList.size()-1);
          }*/
        public void addelement(Get_set_pchat pchatobj){
            pchatList.add(0,pchatobj);
            notifyItemInserted(0);
        }

        public PchatAdapter(List<Get_set_pchat> pchatList,Activity main) {
            this.pchatList = pchatList;
            this.main=main;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout1_pchat, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Get_set_pchat pchat = pchatList.get(position);

            holder.username.setText(pchat.getusername());
            holder.message.setText(pchat.getmessage());
            Log.v("PALLAVIIIIIIIIIIIIIIIII",pchat.getusername());
            String path="http://www.palzone.ml/service_pallavi";
            f=new File(pchat.getImage());
            /*try {
                FileOutputStream out = new FileOutputStream(path+f);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            Log.v("Pallaviiiiiiiiiiiiiiiii",path+""+f);

            Picasso.with(main).load(path+f).into(holder.imageview2);
        }
        // holder.year.setText(movie.getYear());



        @Override
        public int getItemCount() {
            return pchatList.size();
        }

    }
}


