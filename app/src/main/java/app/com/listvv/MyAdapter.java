package app.com.listvv;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rohit on 6/13/2017.
 */

public class MyAdapter extends ArrayAdapter {

    private Context context;
    private int layoutRes;
    private ArrayList<EntryBean> arrayList;

    private LayoutInflater inflater;

    public MyAdapter(Context context, int resource, ArrayList<EntryBean> arrayList) {
        super(context, resource, arrayList);

        this.context = context;
        this.layoutRes = resource;
        this.arrayList = arrayList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        View view  = inflater.inflate(layoutRes, null);

        TextView cView = (TextView) view.findViewById(R.id.c_btn);
        TextView entryTextView = (TextView) view.findViewById(R.id.text);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView timeTextView = (TextView) view.findViewById(R.id.time);

        EntryBean bean = arrayList.get(position);

        entryTextView.setText(bean.getText());
        dateTextView.setText(bean.getDate());
        timeTextView.setText(bean.getTime());

        Random random = new Random(256);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();

//        int strokeWidth = 5; // 3px not dp
//        int roundRadius = 15; // 8px not dp
        int strokeColor = Color.parseColor("#2E3135");
        int fillColor = Color.parseColor("#DFDFE0");

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setShape(GradientDrawable.OVAL);
//        -

        Log.d("455", "getView: "+color);

        cView.setText(bean.getText().toUpperCase().charAt(0)+"");
        cView.setBackground(gd);


        cView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "this is image", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("1234", "getView: "+position +"     "+bean);

        return view;
    }
}
