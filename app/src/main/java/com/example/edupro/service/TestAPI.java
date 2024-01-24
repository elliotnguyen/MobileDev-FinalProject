package com.example.edupro.service;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAPI {
    public  static void run(){
        ServerAPIService apiService = RetrofitClient.createService();

        WritingGradingRequestModel requestModel = new WritingGradingRequestModel();
        requestModel.setAnswer("In recent decades, technology has become an integral part of our daily lives, revolutionizing the way we communicate, work, and live. While advancements in technology bring numerous benefits, they also raise concerns about their impact on society. This essay will discuss both the positive and negative effects of technology on individuals and communities.On the positive side, technology has significantly improved communication and connectivity. The advent of the internet and social media has made it easier for people to stay connected, regardless of geographical distances. This has facilitated the exchange of ideas, information, and culture on a global scale. Additionally, technology has transformed the workplace, increasing efficiency and productivity through automation and innovative tools.Furthermore, advancements in healthcare technology have led to improved medical treatments and outcomes. Diagnostic tools, robotic surgeries, and telemedicine have enhanced patient care, making it more accessible and efficient. Technology has also played a crucial role in education, providing students with interactive learning experiences and access to a vast array of information.However, the pervasive influence of technology has its drawbacks. One of the primary concerns is the impact on social interactions. The rise of smartphones and social media has led to a decrease in face-to-face communication, contributing to feelings of isolation and loneliness. Moreover, the constant exposure to screens has raised concerns about the negative effects on mental health, particularly among the younger generation.Another downside is the potential for job displacement due to automation. As technology advances, certain manual and routine tasks become automated, leading to job losses in some sectors. This raises questions about the need for retraining and upskilling the workforce to adapt to the evolving job market.In conclusion, while technology has brought about significant advancements that benefit society in various ways, it also poses challenges that need to be addressed. Striking a balance between the positive and negative aspects of technology is crucial to ensure that its impact on society remains constructive. Governments, businesses, and individuals must work together to harness the benefits of technology while mitigating its adverse effects to create a more sustainable and inclusive future.");
        requestModel.setQuestion("Discursive essay: Express a personal opinion on a topical issue or argument, providing reasons and examples");

        Call<WritingGradingResponseModel> call = apiService.gradeWritingAPI(requestModel);
        call.enqueue(new Callback<WritingGradingResponseModel>() {
            @Override
            public void onResponse(Call<WritingGradingResponseModel> call, Response<WritingGradingResponseModel> response) {
                if (response.isSuccessful()) {
                    WritingGradingResponseModel responseModel = response.body();
                    Log.d("api",responseModel.getModelGrade()+"herre");
                    // Handle the response
                } else {
                    // Handle the error
                    Log.d("api2","fail22");
                }
            }

            @Override
            public void onFailure(Call<WritingGradingResponseModel> call, Throwable t) {
                // Handle the failure
                Log.d("api","fail");
                Log.e(" failed", "API call failed: " + t.getMessage());
            }
        });

    }


    public static void runSpeakingAPI(Context ctx) {
        ServerAPIService apiService = RetrofitClient.createService();

        String question = "What do you often do in your free time";
        // Create the text part for the question
        RequestBody questionBody = RequestBody.create(MediaType.parse("text/plain"), question);

        Log.d("path",ctx.getFilesDir().getAbsolutePath());
        String fileName = "audio.mp3";  // Replace with your actual file name

        // Read the MP3 file content from internal storage
        byte[] fileContent = readMP3FileFromDirectory(ctx, fileName);

        // Check if file content is null or empty
        if (fileContent == null || fileContent.length == 0) {
            // Handle the case where the file content is not valid
            Log.e("File content error", "File content is null or empty");
            return;
        }


        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/mp3"), fileContent);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fileName, fileBody);

        // Make the API call with both parts
        Call<SpeakingGradingResponseModel> call = apiService.gradeSpeakingAPI(questionBody, filePart);
        call.enqueue(new Callback<SpeakingGradingResponseModel>() {
            @Override
            public void onResponse(Call<SpeakingGradingResponseModel> call, Response<SpeakingGradingResponseModel> response) {
                if (response.isSuccessful()) {
                    SpeakingGradingResponseModel responseModel = response.body();
                    Log.d("grade", responseModel.getGrade() + "here");
                    Log.d("explanation", responseModel.getExplanation() + "here");
                    // Handle the response
                } else {
                    // Handle the error
                    Log.d("api2", "fail22");
                }
            }

            @Override
            public void onFailure(Call<SpeakingGradingResponseModel> call, Throwable t) {
                // Handle the failure
                Log.d("api", "fail");
                Log.e("failed", "API call failed: " + t.getMessage());
            }
        });
    }



    public static void writeMP3FileToExportDir(Context context) {
        String fileName = "audio.mp3";  // Replace with your actual file name

                // Get the export directory on external storage
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Edupro");


        Log.d("Downloads Path", exportDir.getAbsolutePath());
        // Check if the directory exists, create it if not
        if (!exportDir.exists()) {
            if (!exportDir.mkdirs()) {
                Log.e("Directory creation error", "Failed to create directory: " + exportDir.getAbsolutePath());
                return;
            }
        }

        // Create the file in the export directory
        File outputFile = new File(exportDir, fileName);

        // Write the MP3 file content to the file
//        try {
//            FileOutputStream fos = new FileOutputStream(outputFile);
//            fos.write(fileContent);
//            fos.close();
//
//            // Now, the MP3 file is successfully written to the specified directory
//            Log.d("File write success", "MP3 file written to: " + outputFile.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle the case where file writing failed
//            Log.e("File write error", "Failed to write MP3 file to: " + outputFile.getAbsolutePath());
//        }
    }


    public static  byte[] readMP3FileFromDirectory(Context context, String fileName) {


        // Get the directory on external storage
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Edupro");

        // Check if the directory exists
        if (!directory.exists()) {
            // Handle the case where the directory doesn't exist
            Log.e("Directory not found", "Directory not found: " + directory.getAbsolutePath());
            return null;
        }

        // Create the file in the specified directory
        File file = new File(directory, fileName);

        // Check if the file exists
        if (!file.exists()) {
            // Handle the case where the file doesn't exist
            Log.e("File not found", "File not found: " + file.getAbsolutePath());
            return null;
        }

        // Read the MP3 file content from the file
        byte[] fileContent = readMP3File(file);

        // Check if file content is null or empty
        if (fileContent == null || fileContent.length == 0) {
            // Handle the case where the file content is not valid
            Log.e("File content error", "File content is null or empty");
            return null;
        }

        return fileContent;

    }


    private static byte[] readMP3File(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            fis.close();
            bos.close();

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the case where file reading failed
            Log.e("File read error", "Failed to read MP3 file: " + file.getAbsolutePath());
            return null;
        }
    }
}
