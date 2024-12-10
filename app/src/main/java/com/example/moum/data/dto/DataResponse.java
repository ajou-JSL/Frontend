package com.example.moum.data.dto;

import java.util.List;

public class DataResponse<T> {
    private List<T> content;  // 실제 데이터는 content 필드 안에 있음

    // Getter and Setter
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
