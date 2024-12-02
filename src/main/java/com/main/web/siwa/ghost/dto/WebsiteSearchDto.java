package com.main.web.siwa.ghost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteSearchDto {
    // Dto의 필드(속성)이름과 클라이언트에서 보내는 파라미터 이름과 똑같아야 함.. > 안그러면 컨트롤러에서 null로 받음
    // @ModelAttribute를 쓰면 여기 이름과 같은걸 get set 메소드를 알아서 해줌
    // 이게 싫으면 직관성 좋은 @RequestParam 쓰셈
    private Integer page;
    private Integer size;
    private String keyWord;
    private Long categoryId;
}
