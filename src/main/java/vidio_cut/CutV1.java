package vidio_cut;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import java.io.File;

/**
 * inputPath: 원본 동영상 경로
 * outputPath: 잘라낸 동영상 저장 경로
 * startTime: 시작 시간(초)
 * endTime: 종료 시간(초)
 */

public class CutV1 {

    private final String inputPath;
    private final String outputPath;
    private final long startTime; // 시작 시간(초)
    private final long endTime;   // 종료 시간(초)

    public CutV1(String inputPath, String outputPath, long startTime, long endTime) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // 인스턴스 메서드로 변경
    public void cutVideo() {
        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;

        try {
            grabber = new FFmpegFrameGrabber(new File(inputPath));
            grabber.start();

            recorder = new FFmpegFrameRecorder(outputPath,
                    grabber.getImageWidth(),
                    grabber.getImageHeight(),
                    grabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // H264 강제
            recorder.setFormat("mp4");
            recorder.setFrameRate(grabber.getFrameRate()); // 원본 fps 사용
            recorder.start();

            // 시작 시간으로 이동 (마이크로초 단위)
            grabber.setTimestamp(startTime * 1_000_000);

            Frame frame;
            while ((frame = grabber.grab()) != null) {
                if (grabber.getTimestamp() > endTime * 1_000_000) break;
                recorder.record(frame);
            }

            System.out.println("✅ 동영상 자르기 완료: " + outputPath);

        } catch (Exception e) {
            System.err.println("동영상 처리 중 오류 발생:");
            e.printStackTrace();
        } finally {
            try {
                if (recorder != null) recorder.stop();
                if (grabber != null) grabber.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // main 예시
//    public static void main(String[] args) {
//        CutV1 cutter = new CutV1(
//                "C:\\Java_Spring\\vidio\\test1.mp4",
//                "C:\\Java_Spring\\vidio\\test1cut.mp4",
//                180,   // 시작 시간 3분
//                240    // 종료 시간 4분
//        );
//
//        cutter.cutVideo();
//    }
}



//package vidio_cut;
//
//import org.bytedeco.ffmpeg.global.avcodec;
//import org.bytedeco.javacv.*;
//
//import java.io.File;
//
///**
// * inputPath: 원본 동영상 경로
// * outputPath: 잘라낸 동영상 저장 경로
// * startSec: 시작 시간(초)
// * endSec: 종료 시간(초)
// */
//
//public class CutV1 {
//
//    private final String inputPath;
//    private final String outputPath;
//    private final long startTime;
//    private final long endTime;
//
//    public CutV1(String inputPath, String outputPath, long startTime, long endTime) {
//        this.inputPath = inputPath;
//        this.outputPath = outputPath;
//        this.startTime = startTime;
//        this.endTime = endTime;
//    }
//
//    public static void cutVideo() {
//        FFmpegFrameGrabber grabber = null;
//        FFmpegFrameRecorder recorder = null;
//
//        try {
//            grabber = new FFmpegFrameGrabber(new File(inputPath));
//            grabber.start();
//
//            recorder = new FFmpegFrameRecorder(outputPath,
//                    grabber.getImageWidth(),
//                    grabber.getImageHeight(),
//                    grabber.getAudioChannels());
//            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // h264 강제
//            recorder.setFormat("mp4");
//            recorder.setFrameRate(30); // 재인코딩 시 fps 지정
//            recorder.start();
//
//            // timestamp 기반으로 정확하게 구간 자르기
//            grabber.setTimestamp((long) (startSec * 1_000_000)); // 마이크로초 단위
//            Frame frame;
//            while ((frame = grabber.grab()) != null) {
//                if (grabber.getTimestamp() > endSec * 1_000_000) break; // 종료 시간 초과 시 중단
//                recorder.record(frame);
//            }
//
//            System.out.println("동영상 자르기 완료!");
//
//        } catch (Exception e) {
//            System.err.println("동영상 처리 중 오류 발생:");
//            e.printStackTrace();
//        } finally {
//            try {
//                if (recorder != null) recorder.stop();
//                if (grabber != null) grabber.stop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


//    public static void main(String[] args) {
//        // 예시: 2분 ~ 3분 구간 자르기 (정확히 1분)
//        cutVideo("C:\\Java_Spring\\vidio\\test1.mp4",
//                "C:\\Java_Spring\\vidio\\test1JavaC2.mp4",
//                3*60, 4*60);
//    }
//}

//import org.bytedeco.ffmpeg.avcodec.AVPacket;
//import org.bytedeco.javacv.*;
//
//import java.io.File;
//
//public class CutV1 {
//
//    public static void cutVideo(String inputPath, String outputPath, double startSec, double endSec) throws Exception {
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File(inputPath));
//        //FFmpegFrameGrabber : 영상 파일을 frame 단위로 읽어오기 위한 객체
//        grabber.start(); //실제로 파일을 열고 분석 시작.
//
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputPath,
//                grabber.getImageWidth(),
//                grabber.getImageHeight(),
//                grabber.getAudioChannels());
//        //원본 영상의 width/height, audio channel 그대로 적용해서 인코딩
//        recorder.setFormat("mp4"); //이 부분의 경우 필드로 하여 적용을 유연하게 변경하게 할 수 있음
//        recorder.setFrameRate(grabber.getFrameRate()); // framerate 그대로 적용해서 인코딩
//        recorder.start();
//
//        Frame frame;
//        double frameRate = grabber.getFrameRate(); //초 단위를 frame 단위로 변환.
//        long startFrame = (long) (startSec * frameRate);
//        long endFrame = (long) (endSec * frameRate);
//        //frameRate=30fps, startSec=5 → startFrame=150.
//
//        // 프레임 이동
//        grabber.setFrameNumber((int) startFrame);
//
//        for (long i = startFrame; i < endFrame && (frame = grabber.grab()) != null; i++) {
//            recorder.record(frame);
//        }
//        //grabber.setFrameNumber() → 시작 위치를 지정 프레임으로 이동.
//        //startFrame부터 endFrame까지 반복하며 frame을 읽고 recorder에 기록.
//        //즉, startSec ~ endSec 구간만 잘라내어 새로운 파일 생성.
//
//        recorder.stop();
//        grabber.stop();
//    }
//
//    public static void main(String[] args) throws Exception {
//        cutVideo("\"C:\\Java_Spring\\vidio\\인공지능종합설계_2주차.mp4\"",
//                "\"C:\\Java_Spring\\vidio\\인공지능종합설계_2주차_JavaC.mp4\"",
//                120, 180);
//        System.out.println("동영상 자르기 완료!");
//    }
//}


