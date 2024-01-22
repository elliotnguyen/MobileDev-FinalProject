package com.example.edupro.ui.practice.writing.practice.write;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.edupro.R;
import com.example.edupro.ui.dialog.SweetAlertDialog;
import com.example.edupro.ui.practice.writing.practice.WritingPracticeViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.squareup.picasso.Picasso;

public class WritingAnswerFragment extends Fragment {
    private static final String TAG = "WritingAnswerFragment";
    private WritingPracticeViewModel writingPracticeViewModel;
    private TextView wordCountTextView;
    private TextInputEditText writingAnswerEditText;
    private ShapeableImageView answerImageView;
    private RelativeLayout scanBtn;
    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermission;
    private String[] storagePermission;

    private TextRecognizer textRecognizer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View writingAnswer = inflater.inflate(R.layout.fragment_writing_practice_question_write, container, false);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getParentFragment() != null) {
            writingPracticeViewModel = new ViewModelProvider(parentFragment.getParentFragment()).get(WritingPracticeViewModel.class);
        }

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //observeAnyChange();

        handleWritingAnswer(writingAnswer);
        handleClearAnswer(writingAnswer);
        handleImageAnswer(writingAnswer);

        return writingAnswer;
    }

//    private void observeAnyChange() {
//        writingPracticeViewModel.getCurrentAnswer().observe(getViewLifecycleOwner(), currentAnswer -> {
//            if (currentAnswer != null) {
//                writingAnswerEditText.setText(currentAnswer);
//            }
//        });
//    }



    private void handleClearAnswer(View writingAnswer) {
        RelativeLayout clearAnswerTextView = writingAnswer.findViewById(R.id.writing_practice_question_clear_text);
        clearAnswerTextView.setOnClickListener(v -> {
            writingPracticeViewModel.setCurrentAnswer("");
            writingAnswerEditText.setText("");
        });
    }

    private void handleWritingAnswer(View writingAnswer) {
        writingAnswerEditText = writingAnswer.findViewById(R.id.writing_practice_question_write_edit_text);
        writingAnswerEditText.setText(writingPracticeViewModel.getCurrentAnswer().getValue());

        wordCountTextView = writingAnswer.findViewById(R.id.writing_practice_count_words_2);
        if (writingAnswerEditText.getText() != null) {
            int wordCount = countWords(writingAnswerEditText.getText().toString());
            wordCountTextView.setText(String.valueOf(wordCount));
        }
        else {
            wordCountTextView.setText("0");
        }

        writingAnswerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = writingAnswerEditText.getText().toString();

                int wordCount = countWords(text);
                wordCountTextView.setText(String.valueOf(wordCount));
                writingPracticeViewModel.setCurrentAnswer(text);
            }
        });
    }

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
    }

    private void handleImageAnswer(View writingAnswer) {
        scanBtn = writingAnswer.findViewById(R.id.writing_practice_question_scan_text);
        answerImageView = writingAnswer.findViewById(R.id.writing_practice_question_write_image_view);
        scanBtn.setOnClickListener(v -> {
            answerImageView.setVisibility(View.VISIBLE);
            //recognizeTextFromImage();
            showInputImageDialog();
        });
    }

    private void recognizeTextFromImage() {
        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Preparing image...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            InputImage image = InputImage.fromFilePath(requireContext(), imageUri);
            dialog.setTitleText("Recognizing text...");

            Task<Text> textTaskResult = textRecognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        dialog.dismissWithAnimation();
                        String resultText = visionText.getText();
                        writingPracticeViewModel.setCurrentAnswer(resultText);
                        writingAnswerEditText.setText(resultText);
                    })
                    .addOnFailureListener(e -> {
                        dialog.dismissWithAnimation();
                        SweetAlertDialog errorDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                        errorDialog.setTitleText("Oops...");
                        errorDialog.setContentText("Something went wrong!");
                        errorDialog.show();
                    });
        } catch (Exception e) {
            dialog.dismissWithAnimation();
            Toast.makeText(requireContext(), "Failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), scanBtn);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Gallery");

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == 1) {
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickImageCamera();
                }
            } else if (id == 2) {
//                if (!checkStoragePermission()) {
//                    requestStoragePermission();
//                } else {
                    pickImageGallery();
                //}
            }
            return true;
        });

        popupMenu.show();
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imageUri = result.getData().getData();
                            answerImageView.setImageURI(imageUri);
                            recognizeTextFromImage();
                        }
                    } else {
                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                        dialog.setTitleText("Oops...");
                        dialog.setContentText("Something went wrong!");
                        dialog.show();
                    }
                }
            }
    );

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(android.provider.MediaStore.Images.Media.TITLE, "New Picture");
        values.put(android.provider.MediaStore.Images.Media.DESCRIPTION, "From the Camera");

        imageUri = requireContext().getContentResolver().insert(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    answerImageView.setImageURI(imageUri);
                    recognizeTextFromImage();
                } else {
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Oops...");
                    dialog.setContentText("Something went wrong!");
                    dialog.show();
                }
            }
    );

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(requireActivity(), storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return cameraResult && storageResult;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), cameraPermission, CAMERA_REQUEST_CODE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case CAMERA_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && storageAccepted) {
//                        pickImageCamera();
//                    } else {
//                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
//                        dialog.setTitleText("Permission denied");
//                        dialog.setContentText("Camera and Storage permission are required");
//                        dialog.show();
//                    }
//                }
//            }
//            break;
//            case STORAGE_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (storageAccepted) {
//                        pickImageGallery();
//                    } else {
//                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
//                        dialog.setTitleText("Permission denied");
//                        dialog.setContentText("Storage permission is required");
//                        dialog.show();
//                    }
//                }
//            }
//            break;
//        }
//    }
}
