package com.example.edupro.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.databinding.FragmentChatBinding;
import com.example.edupro.model.MessageDao;
import com.example.edupro.service.GeminiService;
import com.example.edupro.ui.dialog.SweetAlertDialog;

import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private RecyclerView.Adapter chatAdapter;
    private ArrayList<MessageDao> messageArrayList;
    private TextInputEditText messageEditText;
    private ImageView sendButton;
    private FragmentChatBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View chatView = inflater.inflate(R.layout.fragment_chat, container, false);
        binding = FragmentChatBinding.inflate(inflater, container, false);

        RecyclerView chatRecyclerView = chatView.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        messageArrayList = new ArrayList<>();
        messageArrayList.add(new MessageDao("Hello there! I am Eddy, your personal assistant. How can I help you today?", 2));

        chatAdapter = new MessageAdapter(messageArrayList);
        chatRecyclerView.setAdapter(chatAdapter);

        messageEditText = chatView.findViewById(R.id.chat_edit_text);
        sendButton = chatView.findViewById(R.id.sendbtn);

        handleGeminiAssistant();
        handleCancelChatRoom(chatView);

        return chatView;
    }

    private void handleGeminiAssistant() {
//        String apiKey = getString(R.string.apiKey);
//        GenerativeModel generativeModel = new GenerativeModel("gemini-pro", apiKey);
//        GenerativeModelFutures generativeModelFutures = GenerativeModelFutures.from(generativeModel);
        GeminiService geminiService = new GeminiService(getString(R.string.apiKey), "gemini-pro");

//        Content.Builder userContentBuilder = new Content.Builder();
//        userContentBuilder.setRole("user");
//        userContentBuilder.addText("Hello, I am studying IELTS");
//        Content userContent = userContentBuilder.build();
//
//        Content.Builder modelContentBuilder = new Content.Builder();
//        modelContentBuilder.setRole("model");
//        modelContentBuilder.addText("Great to meet you. What would you like to know?");
//        Content modelContent = userContentBuilder.build();

        //List<Content> history = Arrays.asList(userContent, modelContent);

        // Initialize the chat
        //ChatFutures chat = generativeModelFutures.startChat(history);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.getText() == null || messageEditText.getText().toString().isEmpty()) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Error");
                    sweetAlertDialog.setContentText("Please enter your message");
                    sweetAlertDialog.setCancelable(true);
                    sweetAlertDialog.show();
                }

                messageArrayList.add(new MessageDao(messageEditText.getText().toString(), 1));
                chatAdapter.notifyItemInserted(messageArrayList.size() - 1);

                Content userMessage = new Content.Builder()
                        .addText(messageEditText.getText().toString())
                        .build();

                messageEditText.setText("");

                ListenableFuture<GenerateContentResponse> response = geminiService.getGenerativeModelFutures().generateContent(userMessage);
                Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(@Nullable GenerateContentResponse result) {
                        if (result == null) {
                            return;
                        }
                        String text = result.getText();
                        messageArrayList.add(new MessageDao(text, 2));
                        chatAdapter.notifyItemInserted(messageArrayList.size() - 1);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(requireContext()));
            }
        });
    }

    private void handleCancelChatRoom(View view) {
        TextView cancelChatRoom = view.findViewById(R.id.assistant_chat_window_back_button);
        cancelChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_home);
            }
        });
    }
}
