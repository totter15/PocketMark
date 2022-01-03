package com.example.pocketmark.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class ModifyNickNameDtoTest {

    @DisplayName("ModifyNickName : Request convert dto")
    @Test
    public void fromChangeNickNameRequest(){
        //Given
        ModifyNickNameDto.ChangeNickNameRequest request =
                ModifyNickNameDto.ChangeNickNameRequest.builder()
                        .newNickName("JyuKa")
                        .build();

        //When
        ModifyNickNameDto.ChangeNickNameDto dto =
                ModifyNickNameDto.ChangeNickNameDto.fromChangeNickNameRequest(request);

        //Then
        then(dto.getNewNickName()).isEqualTo(request.getNewNickName());


    }

}