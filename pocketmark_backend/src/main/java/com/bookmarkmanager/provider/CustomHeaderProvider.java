package com.bookmarkmanager.provider;

import org.springframework.http.HttpHeaders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomHeaderProvider {
    private HttpHeaders httpHeaders = new HttpHeaders();

    public void setBasicCORS(){
        this.httpHeaders.set("Access-Control-Allow-Origin","*");
        this.httpHeaders.set("Access-Control-Allow-Methods","GET");
        this.httpHeaders.set("Access-Control-Allow-Headers","Content-Type, Authorization, Content-Length, X-Requested-With");
    }
    
}
