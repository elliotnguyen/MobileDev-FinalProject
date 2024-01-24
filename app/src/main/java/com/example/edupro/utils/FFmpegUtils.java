//package com.example.edupro.utils;
//
//import android.media.MediaDrm;
//import android.util.Log;
//
//import com.arthenica.mobileffmpeg.Config;
//import com.arthenica.mobileffmpeg.FFmpeg;
//import com.arthenica.mobileffmpeg.LogCallback;
//import com.arthenica.mobileffmpeg.LogMessage;
//
//public class FFmpegUtils {
//
//    public static void convertAacToMp3(String inputFilePath, String outputFilePath) {
//        String[] cmd = new String[]{"-i", inputFilePath, "-acodec", "libmp3lame", outputFilePath};
//
//        Config.enableLogCallback(new LogCallback() {
//            @Override
//            public void apply(MediaDrm.LogMessage logMessage) {
//                // Handle FFmpeg log messages
//                Log.d("FFmpeg", logMessage.getText());
//            }
//        });
//
//        int rc = FFmpeg.execute(cmd);
//
//        if (rc == Config.RETURN_CODE_SUCCESS) {
//            Log.d("FFmpeg", "Conversion success!");
//        } else {
//            Log.e("FFmpeg", "Conversion failed with rc=" + rc);
//        }
//
//        Config.enableLogCallback(null); // Disable log callback to avoid memory leaks
//    }
//}
//
//
//
////import java.util.Base64;
////
////public class AudioConverter {
////
////    public static void convertToMp3(String inputFilePath, String outputFilePath) {
////        try {
////            AudioAttributes audio = new AudioAttributes();
////            audio.setCodec("libmp3lame");
////            audio.setBitRate(new Integer(128000)); // Set the bitrate in bps (e.g., 128 kbps)
////            audio.setChannels(new Integer(2));
////            audio.setSamplingRate(new Integer(44100));
////
////            EncodingAttributes attrs = new EncodingAttributes();
////            attrs.setFormat("mp3");
////            attrs.setAudioAttributes(audio);
////
////            Base64.Encoder encoder = new Encoder();
////            encoder.encode(new MultimediaObject(new File(inputFilePath)), new File(outputFilePath), attrs);
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////}
