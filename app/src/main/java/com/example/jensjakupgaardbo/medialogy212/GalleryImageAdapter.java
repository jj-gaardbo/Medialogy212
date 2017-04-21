package com.example.jensjakupgaardbo.medialogy212;

//package com.journaldev.galleryview;

        import android.content.Context;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Gallery;
        import android.widget.GridView;
        import android.widget.ImageView;


public class GalleryImageAdapter extends BaseAdapter {
    private Context context;

    public Integer[] images = {
            R.drawable.fire, R.drawable.rain
    };

    public GalleryImageAdapter(Context c){
        context = c;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));
        return imageView;
    }




   /* private Context mContext;



    public GalleryImageAdapter(Context context)
    {
        mContext = context;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[index]);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);




        return i;
    }

    public Integer[] mImageIds = {
            R.drawable.fire,
            R.drawable.rain

    };*/

}