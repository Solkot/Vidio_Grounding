package vidiotest;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vidio_cut.CutV1;
import vidio_cut.CutV2;
import vidio_merge.MergeV1;
import vidio_merge.MergeV2;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class testvidio {

    private static final Logger log = LoggerFactory.getLogger(testvidio.class);

    @Test
    public void CutV1Test1() throws Exception {
        String inputPath = "C:\\Java_Spring\\vidio\\test1.mp4";
        String outputPath = "C:\\Java_Spring\\vidio\\test1JavaC1.mp4";
        int startTime = 60;
        int endTime = 120;

        Instant start = Instant.now();
        CutV1 cutV1 = new CutV1(inputPath, outputPath, startTime, endTime);
        cutV1.cutVideo();
        Instant finish = Instant.now();

        log.info("CutV1Test1 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }

    @Test
    public void CutV1Test2() throws Exception {
        String inputPath = "C:\\Java_Spring\\vidio\\test1.mp4";
        String outputPath = "C:\\Java_Spring\\vidio\\test1JavaC2.mp4";
        int startTime = 120;
        int endTime = 180;

        Instant start = Instant.now();
        CutV1 cutV1 = new CutV1(inputPath, outputPath, startTime, endTime);
        cutV1.cutVideo();
        Instant finish = Instant.now();

        log.info("CutV1Test2 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }

    @Test
    public void CutV2Test1() throws Exception {
        String ffmpegPath = "C:\\ffmpeg\\bin\\ffmpeg.exe";
        String ffprobePath = "C:\\ffmpeg\\bin\\ffprobe.exe";
        String inputPath = "C:\\Java_Spring\\vidio\\test1.mp4";
        String outputPath = "C:\\Java_Spring\\vidio\\test1cutffmpeg1.mp4";
        int startTime = 60;
        int length = 60;

        Instant start = Instant.now();
        CutV2 cutV2 = new CutV2(ffmpegPath, ffprobePath, inputPath, outputPath, startTime, length);
        cutV2.cutVideo();
        Instant finish = Instant.now();

        log.info("CutV2Test1 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }

    @Test
    public void CutV2Test2() throws Exception {
        String ffmpegPath = "C:\\ffmpeg\\bin\\ffmpeg.exe";
        String ffprobePath = "C:\\ffmpeg\\bin\\ffprobe.exe";
        String inputPath = "C:\\Java_Spring\\vidio\\test1.mp4";
        String outputPath = "C:\\Java_Spring\\vidio\\test1cutffmpeg2.mp4";
        int startTime = 120;
        int length = 60;

        Instant start = Instant.now();
        CutV2 cutV2 = new CutV2(ffmpegPath, ffprobePath, inputPath, outputPath, startTime, length);
        cutV2.cutVideo();
        Instant finish = Instant.now();

        log.info("CutV2Test2 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }

    @Test
    public void MergeV1Test() throws Exception {
        String inputPath1 = "C:\\Java_Spring\\vidio\\test1JavaC1.mp4";
        String inputPath2 = "C:\\Java_Spring\\vidio\\test1JavaC2.mp4";
        String[] inputpaths = {inputPath1, inputPath2};
        String outputPath = "C:\\Java_Spring\\vidio\\test1JavaCMerge.mp4";

        Instant start = Instant.now();
        MergeV1 mergeV1 = new MergeV1(inputpaths, outputPath);
        mergeV1.mergeVideos();
        Instant finish = Instant.now();

        log.info("MergeV1Test 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }

    @Test
    public void MergeV2Test() throws Exception {
        String ffmpegPath = "C:\\ffmpeg\\bin\\ffmpeg.exe";
        String inputPath1 = "C:\\Java_Spring\\vidio\\test1cutffmpeg1.mp4";
        String inputPath2 = "C:\\Java_Spring\\vidio\\test1cutffmpeg2.mp4";
        List<String> inputpaths = List.of(inputPath1, inputPath2);
        String outputPath = "C:\\Java_Spring\\vidio\\test1ffmpegMerge.mp4";

        Instant start = Instant.now();
        MergeV2 mergeV2 = new MergeV2(ffmpegPath, inputpaths, outputPath);
        mergeV2.mergeVideos();
        Instant finish = Instant.now();

        log.info("MergeV2Test 완료 | 소요 시간: {} ms", Duration.between(start, finish).toMillis());
    }
}

