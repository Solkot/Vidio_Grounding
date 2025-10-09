package vidio_merge;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

public class MergeV1 {

    private final String[] inputVideos;
    private final String outputVideo;

    public MergeV1(String[] inputVideos, String outputVideo) {
        this.inputVideos = inputVideos;
        this.outputVideo = outputVideo;
    }

    public void mergeVideos() {
        FFmpegFrameRecorder recorder = null;

        try {
            for (int i = 0; i < inputVideos.length; i++) {
                try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideos[i])) {
                    grabber.start();

                    if (i == 0) {
                        // 첫 번째 영상 기준으로 recorder 초기화
                        recorder = new FFmpegFrameRecorder(outputVideo,
                                grabber.getImageWidth(),
                                grabber.getImageHeight(),
                                grabber.getAudioChannels());
                        recorder.setFormat("mp4");
                        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // H264 강제
                        recorder.setFrameRate(grabber.getFrameRate() > 0 ? grabber.getFrameRate() : 30);
                        recorder.start();
                    }

                    Frame frame;
                    while ((frame = grabber.grabFrame()) != null) {
                        recorder.record(frame);
                    }

                    grabber.stop();
                }
            }

            if (recorder != null) {
                recorder.stop();
            }

            System.out.println("✅ 동영상 병합 완료: " + outputVideo);

        } catch (Exception e) {
            System.err.println("동영상 병합 중 오류 발생:");
            e.printStackTrace();
        } finally {
            try {
                if (recorder != null) recorder.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // main 예시
//    public static void main(String[] args) {
//        String[] files = {
//                "C:\\Java_Spring\\vidio\\test1JavaC.mp4",
//                "C:\\Java_Spring\\vidio\\test1JavaC2.mp4"
//        };
//        String output = "C:\\Java_Spring\\vidio\\merged.mp4";
//
//        MergeV1 merger = new MergeV1(files, output);
//        merger.mergeVideos();
//    }
}


//package vidio_merge;
//
//import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.javacv.*;
//
//public class MergeV1 {
//
//    public static void mergeVideos(String[] inputs, String outputPath) {
//        FFmpegFrameRecorder recorder = null;
//
//        try {
//            for (int i = 0; i < inputs.length; i++) {
//                try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputs[i])) {
//                    grabber.start();
//
//                    if (i == 0) {
//                        // 첫 번째 영상 기준으로 recorder 초기화
//                        recorder = new FFmpegFrameRecorder(outputPath,
//                                grabber.getImageWidth(),
//                                grabber.getImageHeight(),
//                                grabber.getAudioChannels());
//                        recorder.setFormat("mp4");
//                        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // h264 코덱 강제
//                        recorder.setFrameRate(grabber.getFrameRate() > 0 ? grabber.getFrameRate() : 30); // fps 지정
//                        recorder.start();
//                    }
//
//                    Frame frame;
//                    while ((frame = grabber.grabFrame()) != null) {
//                        recorder.record(frame);
//                    }
//
//                    grabber.stop();
//                }
//            }
//
//            if (recorder != null) {
//                recorder.stop();
//            }
//
//            System.out.println("동영상 병합 완료!");
//
//        } catch (Exception e) {
//            System.err.println("동영상 병합 중 오류 발생:");
//            e.printStackTrace();
//        } finally {
//            try {
//                if (recorder != null) recorder.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        String[] files = {
//                "C:\\Java_Spring\\vidio\\test1JavaC.mp4",
//                "C:\\Java_Spring\\vidio\\test1JavaC2.mp4"
//        };
//        mergeVideos(files, "C:\\Java_Spring\\vidio\\merged.mp4");
//    }
//}
