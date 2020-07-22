package com.example.simpletodoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
  List<String> list;
  OnLongClickListener longClickListener;
  OnSingleClickListener singleClickListener;

  public interface OnLongClickListener{
    void onItemLongClicked(int position);
  }

  public interface OnSingleClickListener{
    void onItemSingleClicked(int position);
  }

  public ItemsAdapter(List<String> list, OnLongClickListener longClickListener,
                      OnSingleClickListener singleClickListener) {
    this.list = list;
    this.longClickListener = longClickListener;
    this.singleClickListener = singleClickListener;
  }



  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // inflating
      View todoView =
          LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,
                                                           parent,
                                                           /*attachToRoot=*/ false);

      return new ViewHolder(todoView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String item = list.get(position);
    holder.bind(item);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{
    TextView tvItem;

      public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvItem = itemView.findViewById(android.R.id.text1);
      }

    public void bind(String item) {
      tvItem.setText(item);
      // single click to edit the item
      tvItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          singleClickListener.onItemSingleClicked(getAdapterPosition());
        }
      });

      // long press to delete the item
      tvItem.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          longClickListener.onItemLongClicked(getAdapterPosition());
          return true;
        }
      });
    }
  }
}
