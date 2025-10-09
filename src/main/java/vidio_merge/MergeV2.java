package vidio_merge;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MergeV2 {

    private final String ffmpegPath;
    private final List<String> inputVideos;
    private final String outputVideo;

    public MergeV2(String ffmpegPath, List<String> inputVideos, String outputVideo) {
        this.ffmpegPath = ffmpegPath;
        this.inputVideos = inputVideos;
        this.outputVideo = outputVideo;
    }

    public void mergeVideos() throws Exception {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

        // 1. 병합용 텍스트 파일 생성
        File listFile = new File("file_list.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(listFile))) {
            for (String video : inputVideos) {
                writer.write("file '" + video.replace("\\", "/") + "'");
                writer.newLine();
            }
        }

        // 2. FFmpegBuilder 생성
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(listFile.getAbsolutePath())
                .overrideOutputFiles(true)
                .addExtraArgs("-f", "concat", "-safe", "0") // concat 옵션
                .addOutput(outputVideo)
                .done();

        // 3. 실행
        FFmpegJob job = executor.createJob(builder);
        job.run();

        System.out.println("✅ 동영상 병합 완료: " + outputVideo);
    }

    // main 예시
//    public static void main(String[] args) {
//        try {
//            MergeV2 merger = new MergeV2(
//                    "C:\\ffmpeg\\bin\\ffmpeg.exe",
//                    List.of(
//                            "C:\\Java_Spring\\vidio\\test1cutffpmeg.mp4",
//                            "C:\\Java_Spring\\vidio\\test1cutffpmeg2.mp4"
//                    ),
//                    "C:\\Java_Spring\\vidio\\merged_output.mp4"
//            );
//
//            merger.merge();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


//package vidio_merge;
//
//import net.bramp.ffmpeg.FFmpeg;
//import net.bramp.ffmpeg.FFmpegExecutor;
//import net.bramp.ffmpeg.builder.FFmpegBuilder;
//import net.bramp.ffmpeg.job.FFmpegJob;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.util.Arrays;
//import java.util.List;
//
//public class MergeV2 {
//
//    public static void main(String[] args) throws Exception {
//        FFmpeg ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
//        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
//
//        List<String> videos = Arrays.asList(
//                "C:\\Java_Spring\\vidio\\test1cutffpmeg.mp4",
//                "C:\\Java_Spring\\vidio\\test1cutffpmeg2.mp4"
//        );
//
//        // 1. 병합용 텍스트 파일 생성
//        File listFile = new File("C:\\Java_Spring\\vidio\\file_list.txt");
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(listFile))) {
//            for (String video : videos) {
//                writer.write("file '" + video.replace("\\", "/") + "'");
//                writer.newLine();
//            }
//        }
//
//        // 2. FFmpegBuilder 생성
//        FFmpegBuilder builder = new FFmpegBuilder()
//                .setInput(listFile.getAbsolutePath())
//                .overrideOutputFiles(true)
//                .addExtraArgs("-f", "concat", "-safe", "0") // concat 옵션
//                .addOutput("C:\\Java_Spring\\vidio\\merged_output.mp4")
//                .done();
//
//        // 3. 실행
//        FFmpegJob job = executor.createJob(builder);
//        job.run();
//
//        System.out.println("병합 완료!");
//    }
//}

