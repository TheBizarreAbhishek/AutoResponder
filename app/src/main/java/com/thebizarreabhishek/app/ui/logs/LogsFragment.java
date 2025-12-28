package com.thebizarreabhishek.app.ui.logs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.thebizarreabhishek.app.R;
import com.thebizarreabhishek.app.databinding.FragmentLogsBinding;
import com.thebizarreabhishek.app.models.ContactSummary;
import com.thebizarreabhishek.app.models.Message;

import java.util.List;

public class LogsFragment extends Fragment {

    private FragmentLogsBinding binding;

    private LogsAdapter adapter;
    private com.thebizarreabhishek.app.helpers.DatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogsBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupListeners();

        dbHelper = new com.thebizarreabhishek.app.helpers.DatabaseHelper(requireContext());
        loadContacts();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new LogsAdapter();
        adapter.setOnContactClickListener(this::showContactMessagesDialog);
        binding.recyclerLogs.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnFilter.setOnClickListener(v -> {
            android.widget.Toast
                    .makeText(requireContext(), "Filtering not implemented yet", android.widget.Toast.LENGTH_SHORT)
                    .show();
        });

        binding.btnClear.setOnClickListener(v -> {
            dbHelper.deleteOldMessages();
            loadContacts();
            android.widget.Toast.makeText(requireContext(), "Logs Cleared", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void loadContacts() {
        new Thread(() -> {
            List<ContactSummary> contacts = dbHelper.getUniqueSenders();
            requireActivity().runOnUiThread(() -> {
                if (contacts.isEmpty()) {
                    android.widget.Toast.makeText(requireContext(), "No logs found", android.widget.Toast.LENGTH_SHORT)
                            .show();
                }
                adapter.setContacts(contacts);
            });
        }).start();
    }

    private void showContactMessagesDialog(ContactSummary contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_contact_messages, null);
        builder.setView(dialogView);

        // Setup dialog header
        TextView tvAvatar = dialogView.findViewById(R.id.tv_dialog_avatar);
        TextView tvName = dialogView.findViewById(R.id.tv_dialog_name);
        ImageView btnClose = dialogView.findViewById(R.id.btn_close);
        RecyclerView recyclerMessages = dialogView.findViewById(R.id.recycler_messages);

        if (contact.getSenderName() != null && !contact.getSenderName().isEmpty()) {
            tvAvatar.setText(String.valueOf(contact.getSenderName().charAt(0)).toUpperCase());
        }
        tvName.setText(contact.getSenderName());

        // Setup messages RecyclerView
        MessagesAdapter messagesAdapter = new MessagesAdapter();
        recyclerMessages.setAdapter(messagesAdapter);

        // Load messages for this contact
        new Thread(() -> {
            List<Message> messages = dbHelper.getAllMessagesBySender(contact.getSenderName());
            requireActivity().runOnUiThread(() -> {
                messagesAdapter.setMessages(messages);
            });
        }).start();

        AlertDialog dialog = builder.create();
        
        // Apply dialog styling
        applyDialogStyling(dialog);
        
        btnClose.setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }

    private void applyDialogStyling(android.app.Dialog dialog) {
        if (dialog == null || dialog.getWindow() == null) return;
        
        android.view.Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_background);
        window.setDimAmount(0.4f);
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            window.addFlags(android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            android.view.WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.setBlurBehindRadius(25);
            window.setAttributes(attributes);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
