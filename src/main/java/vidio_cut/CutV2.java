package vidio_cut;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.util.concurrent.TimeUnit;

public class CutV2 {

    private final String ffmpegPath;
    private final String ffprobePath;
    private final String inputVideo;
    private final String outputVideo;
    private final long startOffsetSeconds;
    private final long durationSeconds;

    public CutV2(String ffmpegPath,
                 String ffprobePath,
                 String inputVideo,
                 String outputVideo,
                 long startOffsetSeconds,
                 long durationSeconds) {
        this.ffmpegPath = ffmpegPath;
        this.ffprobePath = ffprobePath;
        this.inputVideo = inputVideo;
        this.outputVideo = outputVideo;
        this.startOffsetSeconds = startOffsetSeconds;
        this.durationSeconds = durationSeconds;
    }

    public void cutVideo() throws Exception {
        // FFmpeg, FFprobe 객체 생성
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        // FFmpegBuilder 설정
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputVideo)
                .addOutput(outputVideo)
                .setStartOffset(startOffsetSeconds, TimeUnit.SECONDS)
                .setDuration(durationSeconds, TimeUnit.SECONDS)
                .done();

        // FFmpegExecutor 실행
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        System.out.println("동영상 잘라내기 완료: " + outputVideo);
    }

    // 예시 main
//    public static void main(String[] args) {
//        try {
//            // 외부에서 입력 받도록 변경 가능 (예: args[], Scanner, config 등)
//            CutV2 cutter = new CutV2(
//                    "C:\\ffmpeg\\bin\\ffmpeg.exe",
//                    "C:\\ffmpeg\\bin\\ffprobe.exe",
//                    "C:\\Java_Spring\\vidio\\test1.mp4",
//                    "C:\\Java_Spring\\vidio\\test1cutffmpeg2.mp4",
//                    3 * 60,     // 시작 시간 3분
//                    60          // 길이 60초
//            );
//
//            cutter.cutVideo();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


//package vidio_cut;
//
//import net.bramp.ffmpeg.FFmpeg;
//import net.bramp.ffmpeg.FFmpegExecutor;
//import net.bramp.ffmpeg.FFprobe;
//import net.bramp.ffmpeg.builder.FFmpegBuilder;
//
//import java.util.concurrent.TimeUnit;
//
//public class CutV2 {
//
//    public static void main(String[] args) throws Exception {
//        FFmpeg ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
//        FFprobe ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
//
//        FFmpegBuilder builder = new FFmpegBuilder()
//                .setInput("C:\\Java_Spring\\vidio\\test1.mp4")
//                .addOutput("\"C:\\Java_Spring\\vidio\\test1cutffpmeg2.mp4\"")
//                .setStartOffset(3*60, TimeUnit.SECONDS)  // 10초부터
//                .setDuration(60, TimeUnit.SECONDS)     // 20초 길이 잘라냄
//                .done();
//
//        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
//        executor.createJob(builder).run();
//
//        System.out.println("✅ 동영상 잘라내기 완료");
//    }
//}





