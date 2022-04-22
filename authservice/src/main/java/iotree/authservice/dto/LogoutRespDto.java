package iotree.authservice.dto;

import lombok.Data;

/**
 * /rest/logout 요청의 응답으로 전송되는 데이터 클래스
 */
@Data
public class LogoutRespDto {
    // TODO 클라이언트 logout 요청 결과를 담기 윈한 필드 선언 추가.
    //  클라이언트와 프로토콜 규격을 정해지면 규격에 맞게 필드를 추가한다.
    //  아래 필드는 단순 샘플이다.
    private int code = 0;
}
