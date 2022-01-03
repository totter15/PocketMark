package com.example.pocketmark.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class LeaveUserTest {

    @DisplayName("Leave User : request convert dto")
    @Test
    public void fromLeaveUserRequest(){
        //Given
        LeaveUser.LeaveUserRequest request = LeaveUser.LeaveUserRequest.builder()
                .leave(true).build();

        //When
        LeaveUser.LeaveUserDto dto = LeaveUser.LeaveUserDto.fromLeaveUserRequest(request);

        //Then
        then(dto.isLeave()).isEqualTo(request.isLeave());
    }

}