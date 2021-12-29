package com.example.pocketmark.dto;

import lombok.*;

public class LeaveUser {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class LeaveUserDto{
        private boolean leave;

        public static LeaveUserDto fromLeaveUserRequest(LeaveUserRequest request){
            return LeaveUserDto.builder()
                    .leave(request.isLeave())
                    .build()
                    ;
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class LeaveUserRequest{
        private boolean leave;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class LeaveUserResponse{
        private boolean leave;
    }



}
