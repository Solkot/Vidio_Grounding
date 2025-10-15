package vidio.editor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VidioDTO {
    private String userName;
    private String vidioName;
    private String status;   // CUT / MERGE, 나중에 사용자 지정 & 비지정으로 바꿀 예정
    private String text;     // 추가 메타정보
}
