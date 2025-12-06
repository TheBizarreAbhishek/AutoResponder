package com.thebizarreabhishek.app.ui.logs;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thebizarreabhishek.app.databinding.ItemLogBinding;
import com.thebizarreabhishek.app.models.Message;
import java.util.ArrayList;
import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.LogViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    public void setMessages(List<Message> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }

    public void clearMessages() {
        this.messageList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLogBinding binding = ItemLogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private final ItemLogBinding binding;

        public LogViewHolder(ItemLogBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.tvName.setText(message.getSender());
            binding.tvIncomingMessage.setText("Received: " + message.getMessage());
            binding.tvReplyMessage.setText("Replied: " + message.getReply());
            binding.tvTime.setText(message.getTimestamp());

            // Set first letter as avatar text
            if (message.getSender() != null && !message.getSender().isEmpty()) {
                binding.tvAvatar.setText(String.valueOf(message.getSender().charAt(0)).toUpperCase());
            } else {
                binding.tvAvatar.setText("?");
            }
        }
    }
}
