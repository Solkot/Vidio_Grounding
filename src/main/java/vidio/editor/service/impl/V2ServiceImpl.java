package vidio.editor.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vidio.editor.dto.TimeDTO;
import vidio.editor.service.VService;
import vidio.editor.service.time.modTime;
import vidio.editor.vidio.cut.CutVidio;
import vidio.editor.vidio.merge.MergeVidio;
import vidio.editor.vidio.util.GetVidioEndTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class V2ServiceImpl implements VService {

    private final CutVidio cutVidio;
    private final MergeVidio mergeVidio;
    private final GetVidioEndTime getVidioEndTime;

    public Path cutAndMerge(TimeDTO timeDTO) throws Exception {
        String status = timeDTO.getStatus().toUpperCase();
        String userId = timeDTO.getUserName();
        String videoName = timeDTO.getVidioName();

        // 사용자 폴더 생성
        Path userDir = Paths.get("uploads").resolve(userId);
        Files.createDirectories(userDir);

        // 원본 영상 경로
        Path videoPath = userDir.resolve(videoName);
        String videoAbsolutePath = videoPath.toAbsolutePath().toString().replace("\\", "/");
        log.info("Video path: {}", videoAbsolutePath);

        int videoEnd = (int) getVidioEndTime.getEndTime(videoAbsolutePath);

        // CUT / MERGE 시간 계산
        List<int[]> timeRanges;
        if ("CUT".equals(status)) {
            timeRanges = modTime.getTime(timeDTO.getTime());
        } else if ("MERGE".equals(status)) {
            timeRanges = modTime.getRemainTime(timeDTO.getTime(), videoEnd);
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        Path tempDir = userDir.resolve(videoName.replace(".mp4", ""));
        Files.createDirectories(tempDir);

        List<String> cutPaths = new ArrayList<>();
        for (int i = 0; i < timeRanges.size(); i++) {
            int[] range = timeRanges.get(i);
            String cutFileName = String.format("cut_%02d.mp4", i + 1);
            Path cutPath = tempDir.resolve(cutFileName);

            log.info("Cutting range {}: {}~{}", i + 1, range[0], range[1]);
            cutVidio.cutVideo(videoAbsolutePath, cutPath.toAbsolutePath().toString().replace("\\", "/"), range[0], range[1] - range[0]);

            cutPaths.add(cutPath.toAbsolutePath().toString().replace("\\", "/"));
        }

        // 병합 결과 폴더
        Path mergedDir = userDir.resolve("Merged");
        Files.createDirectories(mergedDir);

        String mergedFileName = "merged_" + videoName;
        Path mergedPath = mergedDir.resolve(mergedFileName);

        mergeVidio.mergeVideos(cutPaths, mergedPath.toAbsolutePath().toString().replace("\\", "/"));
        log.info("Video merge complete: {}", mergedPath.toAbsolutePath());

        return mergedPath;
    }

    public void deleteTemporaryDirectory(String userId) throws IOException {
        Path userDir = Paths.get("uploads").resolve(userId);
        if (Files.exists(userDir)) {
            Files.walk(userDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            log.info("Temporary directory deleted: {}", userDir.toAbsolutePath());
        }
    }
}


//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class V1Service {
//    private final CutVidio cutVidio;
//    private final MergeVidio mergeVidio;
//    private final GetVidioEndTime getVidioEndTime;
//
//    public Path cutAndMerge(TimeDTO timeDTO) throws Exception {
//        String status = timeDTO.getStatus().toUpperCase();
//        String userId = timeDTO.getUserName();
//
//        Path uploadDir = Paths.get("uploads");
//        Path userDir = uploadDir.resolve(userId);
//        Files.createDirectories(userDir);
//        Path filePath = uploadDir.resolve(userId + "_" + timeDTO.getVidioName());
//        String videoPath = filePath.toAbsolutePath().toString().replace("\\", "/");
//
//        log.info("Video path: {}", videoPath);
//        int videoEnd = (int) getVidioEndTime.getEndTime(videoPath);
//        List<int[]> timeRanges;
//        if ("CUT".equals(status)) {
//            timeRanges = modTime.getTime(timeDTO.getTime());
//        } else if ("MERGE".equals(status)) {
//            timeRanges = modTime.getRemainTime(timeDTO.getTime(), videoEnd);
//        } else {
//            throw new IllegalArgumentException("Invalid status: " + status);
//        }
//
//        List<String> cutPaths = new ArrayList<>();
//
//        for (int i = 0; i < timeRanges.size(); i++) {
//            int[] range = timeRanges.get(i);
//            String cutFileName = String.format("cut_%02d.mp4", i + 1);
//            Path cutPath = userDir.resolve(cutFileName);
//            log.info("✂ Cutting range {}: {}~{}", i + 1, range[0], range[1]);
//            cutVidio.cutVideo(videoPath, cutPath.toString().replace("\\", "/"), range[0], range[1] - range[0]);
//            cutPaths.add(cutPath.toString().replace("\\", "/"));
//        }
//
//        String mergedFileName = userId + "_merged.mp4";
//        Path mergedPath = uploadDir.resolve(mergedFileName);
//        mergeVidio.mergeVideos(cutPaths, mergedPath.toString().replace("\\", "/"));
//        //deleteDirectoryRecursively(userDir.toFile());
//
//        return mergedPath;
//    }
//
//    public void deleteTemporaryDirectory (String userId) throws IOException {
//        Path userDir = Paths.get("uploads").resolve(userId);
//        if (Files.exists(userDir)) {
//            Files.walk(userDir)
//                    .sorted(Comparator.reverseOrder())
//                    .map(Path::toFile)
//                    .forEach(File::delete);
//            log.info("Temporary directory deleted: {}", userDir);
//        }
//    }
//}

//    public static void main(String[] args) throws IOException{
//        TimeDTO meta = new TimeDTO();
//        //"홍길동", "test1.mp4", "CUT", "[20, 80], [100, 120], [130, 180]"
//        meta.setUserName("홍길동");
//        meta.setVidioName("test1.mp4");
//        meta.setStatus("Merge");
//        meta.setTime("[20, 80], [100, 120], [130, 180]");
//
//        v1service v1service = new v1service(new getVidioEndTime());
//        v1service.cutAndMerge(meta);
//    }

