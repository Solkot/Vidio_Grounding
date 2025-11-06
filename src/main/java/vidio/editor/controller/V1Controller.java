package vidio.editor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vidio.editor.modelServer.ModelServer;
import vidio.editor.service.VService;
import vidio.editor.dto.TimeDTO;
import vidio.editor.dto.VidioDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/");
public class V1Controller {

    private final VService vService;
    private final ModelServer modelserverApi;

    //1. User에게서 동영상과 텍스트를 받음, 단 JSON 형식을 받을 것
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            //@RequestParam ("status") String status, //Cut / Merge
            @RequestPart("meta") String metaJson,  // JSON을 문자열로 받음
            @RequestPart("file") MultipartFile file  // 파일
    ) throws IOException {

        // JSON 파싱 → DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        VidioDTO meta = objectMapper.readValue(metaJson, VidioDTO.class);

        // 저장 경로
        Path userDir = Paths.get("uploads", meta.getUserName());

        // 디렉토리가 없으면 생성
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        // 파일 경로, 파일 저장
        Path filePath = userDir.resolve(file.getOriginalFilename()); //OS 구분자 자동 처리
        //Files.write(filePath, file.getBytes()); //대용량일 경우 413 오류
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("User: " + meta.getUserName() +
                ", Status: " + meta.getStatus() +
                ", Text: " + meta.getText() +
                ", Video saved at: " + filePath.toAbsolutePath());

        //2. FastAPI에게 동영상 및 텍스트 전달
        //보낼때 파일 이름도 함께 보낼 것

        Map<String, Object> fastApiMeta = new HashMap<>();
        fastApiMeta.put("userName", meta.getUserName());
        fastApiMeta.put("vidioName", file.getOriginalFilename());
        fastApiMeta.put("text", meta.getText());
        fastApiMeta.put("status", meta.getStatus());

        String response = modelserverApi.sendVideoToFastApi(fastApiMeta, filePath);

        return ResponseEntity.ok("Upload & send to FastAPI done. Response: " + response);
    }



    //3. FastAPI에게 시간을 전달 받음(JSON)
    @PostMapping(value = "/time")
    public ResponseEntity<Resource> getTime(
            //@RequestParam ("vidio_name") String vidioName,
            //@RequestParam ("status") String status, //Cut / Merge
            @RequestPart("time") String timeJson
    ) throws Exception {
        // JSON 파싱 → DTO 변환
        ObjectMapper objectMapper = new ObjectMapper();
        TimeDTO timeDTO = objectMapper.readValue(timeJson, TimeDTO.class);

        log.info("User: " + timeDTO.getUserName() +
                ", VidioName: " + timeDTO.getVidioName() +
                ", Status: " + timeDTO.getStatus() +
                ", Time: " + timeDTO.getTime());
        // 4 메소드에 Cut, Merge 정보를 넘겨주어야 함
        // 넘겨주는 정보 : 동영상 이름, cut/merge

        //4. Cut / Merge에 따라 동영상을 수정해서 사용자에게 반환
        //Merge의 경우 자른 부분만 빼고 합치기. 굳이 시간을 여러 개 받나? 모델이 그렇게 주나?

        Path mergedPath = vService.cutAndMerge(timeDTO);
        Resource resource = new FileSystemResource(mergedPath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mergedPath.getFileName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(mergedPath))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    //5. 최종적으로 해당 파일 삭제 -> 내부에 존재하는 임시데이터는 javascript에서 해당 페이지를 벗어나면 삭제되도록 진행
    @DeleteMapping("/cleanup")
    public ResponseEntity<String> cleanup(@RequestParam String userName) throws IOException {
        vService.deleteTemporaryDirectory(userName);
        return ResponseEntity.ok("Temporary files deleted for " + userName);
    }
}





