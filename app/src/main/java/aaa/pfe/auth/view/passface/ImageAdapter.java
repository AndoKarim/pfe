package aaa.pfe.auth.view.passface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.Random;

import aaa.pfe.auth.R;


public class ImageAdapter extends BaseAdapter {
    private final String[] arrayOfImages;
    private Context context;

    public ImageAdapter(Context context, String[] arrayOfImages, boolean shuffle) {
        this.context = context;

        if (shuffle)
            this.arrayOfImages = shuffleArray(arrayOfImages);
        else
            this.arrayOfImages = arrayOfImages;
    }

    private static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    private String[] shuffleArray(String[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }

        return ar;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.layout_passface_image, null);


            // set image based on selected text
            ImageView imageView = gridView.findViewById(R.id.imageView);

            String nameInArray = arrayOfImages[position];

            imageView.setImageResource(getImageId(context, "image_" + nameInArray));
            imageView.setTag("image_" + nameInArray);

        } else {
            gridView = convertView;
        }

        return gridView;
    }


    @Override
    public int getCount() {
        return arrayOfImages.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}