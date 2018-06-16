package com.arieftb.pdfsign.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.arieftb.pdfsign.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PDFPageRecAdapter extends RecyclerView.Adapter<PDFPageRecAdapter.ViewHolder> {

    private List<String> pdfPages;
    private Context context;

    public PDFPageRecAdapter(List<String> pdfPages, Context context) {
        this.pdfPages = pdfPages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_page_pdf, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String pdfPage = pdfPages.get(position);

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//
//        holder.ivPagePdf.getLayoutParams().height = height;
//        holder.ivPagePdf.getLayoutParams().width = width;

//        holder.ivPagePdf.requestLayout();

        Glide.with(context)
                .load(pdfPage)
                .apply(new RequestOptions().fitCenter())
                .into(holder.ivPagePdf);

        holder.ivPagePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return pdfPages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPagePdf;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPagePdf = itemView.findViewById(R.id.iv_page_pdf);
        }


    }
}
