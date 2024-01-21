package com.example.edupro;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edupro.model.MessageDao;
import com.example.edupro.ui.chat.MessageAdapter;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private RecyclerView.Adapter chatAdapter;
    private ArrayList<MessageDao> messageArrayList;
    private TextInputEditText messageEditText;
    private ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        messageArrayList = new ArrayList<>();
        messageArrayList.add(new MessageDao("Hello there! I am Eddy, your personal assistant. How can I help you today?", 2));

        chatAdapter = new MessageAdapter(messageArrayList);
        chatRecyclerView.setAdapter(chatAdapter);

        messageEditText = findViewById(R.id.chat_edit_text);
        sendButton = findViewById(R.id.sendbtn);

        handleGeminiAssistant();
    }

    private void handleGeminiAssistant() {
        String apiKey = getString(R.string.apiKey);
        GenerativeModel generativeModel = new GenerativeModel("gemini-pro", apiKey);
        GenerativeModelFutures generativeModelFutures = GenerativeModelFutures.from(generativeModel);

        Content.Builder userContentBuilder = new Content.Builder();
        userContentBuilder.setRole("user");
        userContentBuilder.addText("Hello, I am studying IELTS");
        Content userContent = userContentBuilder.build();

        Content.Builder modelContentBuilder = new Content.Builder();
        modelContentBuilder.setRole("model");
        modelContentBuilder.addText("Great to meet you. What would you like to know?");
        Content modelContent = userContentBuilder.build();

        List<Content> history = Arrays.asList(userContent, modelContent);

        // Initialize the chat
        ChatFutures chat = generativeModelFutures.startChat(history);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.getText() == null || messageEditText.getText().toString().isEmpty()) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ChatActivity.this, SweetAlertDialog.ERROR_TYPE);
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

                ListenableFuture<GenerateContentResponse> response = generativeModelFutures.generateContent(userMessage);
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
                }, ContextCompat.getMainExecutor(ChatActivity.this));
            }
        });
    }
}