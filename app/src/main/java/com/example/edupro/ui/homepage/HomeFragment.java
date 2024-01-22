package com.example.edupro.ui.homepage;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.edupro.ChatActivity;
import com.example.edupro.R;
import com.example.edupro.databinding.FragmentHomeBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        String apiKey = getString(R.string.apiKey);
//        GenerativeModel generativeModel = new GenerativeModel("gemini-pro", apiKey);
//        GenerativeModelFutures generativeModelFutures = GenerativeModelFutures.from(generativeModel);

//        Content content = new Content.Builder()
//                .addText("Write a story about a dog.")
//                .build();
//        Content.Builder userContentBuilder = new Content.Builder();
//        userContentBuilder.setRole("user");
//        userContentBuilder.addText("Hello, I am studying IELTS");
//        Content userContent = userContentBuilder.build();
//
//        Content.Builder modelContentBuilder = new Content.Builder();
//        modelContentBuilder.setRole("model");
//        modelContentBuilder.addText("Great to meet you. What would you like to know?");
//        Content modelContent = userContentBuilder.build();
//
//        List<Content> history = Arrays.asList(userContent, modelContent);
//
//        // Initialize the chat
//        ChatFutures chat = generativeModelFutures.startChat(history);

        TextView getText = root.findViewById(R.id.getText);
        EditText text = root.findViewById(R.id.editText);
        getText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
//                Content userMessage = new Content.Builder()
//                        .addText(text.getText().toString())
//                        .build();
//
//                ListenableFuture<GenerateContentResponse> response = generativeModelFutures.generateContent(userMessage);
//                Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
//                    @Override
//                    public void onSuccess(@Nullable GenerateContentResponse result) {
//                        if (result == null) {
//                            return;
//                        }
//                        String text = result.getText();
//                        TextView showText = root.findViewById(R.id.showText);
//                        showText.setText(text);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        t.printStackTrace();
//                    }
//                }, ContextCompat.getMainExecutor(requireContext()));
            }
        });
//        ListenableFuture<GenerateContentResponse> future = generativeModelFutures.generateContent(content);
//        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
//            @Override
//            public void onSuccess(@Nullable GenerateContentResponse result) {
//                if (result == null) {
//                    return;
//                }
//                String text = result.getText();
//                TextView textView = root.findViewById(R.id.text_home);
//                textView.setText(text);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        }, ContextCompat.getMainExecutor(requireContext()));
//        future.addCallback(() -> {
//            try {
//                GenerateContentResponse response = future.get();
//                String text = response.getContent().getText();
//                TextView textView = root.findViewById(R.id.text_home);
//                textView.setText(text);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, ContextCompat.getMainExecutor(requireContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}