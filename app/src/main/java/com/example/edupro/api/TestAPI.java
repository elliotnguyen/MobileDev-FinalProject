package com.example.edupro.api;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAPI {
    public  static void run(){
        ServerAPIService apiService = RetrofitClient.createService();

        WritingGradingRequestModel requestModel = new WritingGradingRequestModel();
        requestModel.setAnswer("In recent decades, technology has become an integral part of our daily lives, revolutionizing the way we communicate, work, and live. While advancements in technology bring numerous benefits, they also raise concerns about their impact on society. This essay will discuss both the positive and negative effects of technology on individuals and communities.On the positive side, technology has significantly improved communication and connectivity. The advent of the internet and social media has made it easier for people to stay connected, regardless of geographical distances. This has facilitated the exchange of ideas, information, and culture on a global scale. Additionally, technology has transformed the workplace, increasing efficiency and productivity through automation and innovative tools.Furthermore, advancements in healthcare technology have led to improved medical treatments and outcomes. Diagnostic tools, robotic surgeries, and telemedicine have enhanced patient care, making it more accessible and efficient. Technology has also played a crucial role in education, providing students with interactive learning experiences and access to a vast array of information.However, the pervasive influence of technology has its drawbacks. One of the primary concerns is the impact on social interactions. The rise of smartphones and social media has led to a decrease in face-to-face communication, contributing to feelings of isolation and loneliness. Moreover, the constant exposure to screens has raised concerns about the negative effects on mental health, particularly among the younger generation.Another downside is the potential for job displacement due to automation. As technology advances, certain manual and routine tasks become automated, leading to job losses in some sectors. This raises questions about the need for retraining and upskilling the workforce to adapt to the evolving job market.In conclusion, while technology has brought about significant advancements that benefit society in various ways, it also poses challenges that need to be addressed. Striking a balance between the positive and negative aspects of technology is crucial to ensure that its impact on society remains constructive. Governments, businesses, and individuals must work together to harness the benefits of technology while mitigating its adverse effects to create a more sustainable and inclusive future.");
        requestModel.setQuestion("Discursive essay: Express a personal opinion on a topical issue or argument, providing reasons and examples");

        Call<WritingGradingResponseModel> call = apiService.postData(requestModel);
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
}
