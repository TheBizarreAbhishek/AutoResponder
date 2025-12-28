package com.thebizarreabhishek.app.ui.logs;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thebizarreabhishek.app.databinding.ItemContactMessageBinding;
import com.thebizarreabhishek.app.models.Message;
import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactMessageBinding binding = ItemContactMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContactMessageBinding binding;

        public MessageViewHolder(ItemContactMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.tvIncoming.setText(message.getMessage());
            binding.tvReply.setText(message.getReply());
            binding.tvTimestamp.setText(message.getTimestamp());
        }
    }
}
